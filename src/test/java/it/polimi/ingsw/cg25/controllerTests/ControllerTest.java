package it.polimi.ingsw.cg25.controllerTests;

import static org.junit.Assert.*;
import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.ActionTest;
import it.polimi.ingsw.cg25.actions.Action;
import it.polimi.ingsw.cg25.actions.ChooseActionTypeAction;
import it.polimi.ingsw.cg25.actions.DisplayInteraction;
import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.communication.UnserializedVectorPacket;
import it.polimi.ingsw.cg25.communication.VectorPacket;
import it.polimi.ingsw.cg25.controller.ActionController;
import it.polimi.ingsw.cg25.controller.ServiceController;
import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.proxies.ViewProxy;
import it.polimi.ingsw.cg25.servers.GameCD4;

public class ControllerTest extends ActionTest{

	private ServiceController controller;
	private ActionController actionController = new ActionController(137);
	private boolean disconnectedNumberZero = false;
	private boolean connectedNumberTen = false;
	private String chatText = "";
	private boolean wentWrongPm = false;
	private String returnedStatus = "";
	private boolean quitted = false;
	private String userNameZero = "";
	private String numberZeroReport;
	
	private class FakeGame extends GameCD4{

		public final static String status = "status";
		
		public FakeGame()
				throws FileNotFoundException, CannotCreateGameException {
			super(0, 3, 137, model.getBoard(), true,10);
		}
		
		@Override
		public void disconnect(int id,String report){
			if(report.equals("Quitted the game"))
				quitted = true;
			disconnectedNumberZero = id ==0;
			numberZeroReport = (id == 0) ? report : ""; 
		}
		
		@Override
		public void connect(int id){
			connectedNumberTen = id ==10;
		}
		
		@Override
		public String addressBook(int id){
			return "id";
		}
		
		@Override 
		public int nameService(String name){
		try{
			return Integer.parseInt(name);
		}catch(NumberFormatException e){
			wentWrongPm = true;
			throw new IllegalArgumentException();
		}
		}
		
		@Override 
		public void chat(String msg){
			chatText = chatText.concat(msg);
		}
		
		@Override 
		public String getStatus(int id){
			returnedStatus = status.concat(Integer.toString(id));
			return returnedStatus;
		}
		
		@Override
		public void changeUsername(int id,String un){
			if(id==0)
				userNameZero = un;
		}
	}
	
	@Before
	public void setUp() throws Exception {
		this.controller = new ServiceController(new FakeGame());
		ViewProxy proxy = new ViewProxy(actionController, controller);
		actionController.setViewProxy(proxy);
		controller.setProxy(proxy);
	}

	@Test
	public void constructorTest() throws FileNotFoundException, CannotCreateGameException{
		assertTrue(new ServiceController(new FakeGame())!=null);
	}
	
	@Test
	public void disconnectionTest() {
		actionController.getViewProxy().update(new VectorPacket<Interaction>(new DisplayInteraction<String>("Go away"),"Disconnection",10,10));
		assertFalse(this.disconnectedNumberZero);
		assertEquals(this.numberZeroReport,"");
		actionController.getViewProxy().update(new VectorPacket<Interaction>(new DisplayInteraction<String>("Had to go out"),"Disconnection",0,0));
		assertTrue(this.disconnectedNumberZero);
		assertEquals(this.numberZeroReport,"Had to go out");
	}
	
	@Test
	public void connectonTest() {
		actionController.getViewProxy().update(new VectorPacket<Interaction>(new DisplayInteraction<String>("Go away"),"Connection",0,0));
		assertFalse(this.connectedNumberTen);
		actionController.getViewProxy().update(new VectorPacket<Interaction>(new DisplayInteraction<String>("Had to go out"),"Connection",10,10));
		assertTrue(this.connectedNumberTen);
	}
	
	@Test
	public void chatTest() {
		String chatBefore = this.chatText;
		actionController.getViewProxy().update(new VectorPacket<Interaction>(new DisplayInteraction<String>("Go away"),"Chat",0,0));
		assertTrue(chatText.contains(chatBefore) && chatText.contains("Go away"));
		chatBefore = this.chatText;
		actionController.getViewProxy().update(new VectorPacket<Interaction>(new DisplayInteraction<String>("Had to go out"),"Chat",10,10));
		assertTrue(chatText.contains(chatBefore) && chatText.contains("Had to go out"));
	}
	
	@Test
	public void pmTest() {
		String chatBefore = this.chatText;
		actionController.getViewProxy().update(new VectorPacket<Interaction>(new DisplayInteraction<String>("Go away@@2"),"Pm",0,0));
		assertTrue(this.chatText==chatBefore);
	}
	
	@Test
	public void pmWithWrongAddressTest(){
		actionController.getViewProxy().update(new VectorPacket<Interaction>(new DisplayInteraction<String>("Go away@@asd"),"Pm",0,0));
		assertTrue(this.wentWrongPm);
	}

	@Test
	public void testDefaultCase(){
		actionController.getViewProxy().update(new VectorPacket<Interaction>(new DisplayInteraction<String>("Go away@@asd"),"NoCaseExisting",0,0));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void setNullProxyTest() throws FileNotFoundException, CannotCreateGameException {
		new ServiceController(new FakeGame()).setProxy(null);
	}

	@Test (expected = IllegalArgumentException.class)
	public void sendInteractionNullPacketTest() {
		controller.sendInteraction(null);
	}
	
	@Test 
	public void statusTest(){
		String prevStatus = returnedStatus;
		actionController.getViewProxy().update(new VectorPacket<Interaction>(new DisplayInteraction<String>(""),"Status",0,0));
		assertEquals(returnedStatus,FakeGame.status.concat("0"));
		assertTrue(prevStatus != returnedStatus);
	}
	
	@Test
	public void changeUsernameTest(){
		String prevUsername = userNameZero;
		actionController.getViewProxy().update(new VectorPacket<Interaction>(new DisplayInteraction<String>("mario"),"SetUsername",10,10));
		assertTrue(prevUsername == userNameZero);
		actionController.getViewProxy().update(new VectorPacket<Interaction>(new DisplayInteraction<String>("nicolo"),"SetUsername",0,0));
		assertEquals(userNameZero,"nicolo");
	}
	
	@Test
	public void quitTest(){
		actionController.getViewProxy().update(new VectorPacket<Interaction>(new DisplayInteraction<String>("mario"),"Quit",10,10));
		assertTrue(quitted);
	}
	
	@Test
	public void ActionControllerTest() throws CannotSetupActionException{
		Action action = new ChooseActionTypeAction<MatchCD4>(model);
		action.setup();
		this.actionController.update(new UnserializedVectorPacket<Action>(action,"Action",0,10));
		this.actionController.update();
	}

	@Test 
	public void ActionControllerTimeoutEndingTest() throws CannotSetupActionException, InterruptedException{
		ActionController c = new ActionController(0);
		c.setViewProxy((ViewProxy)actionController.getViewProxy());
		Action action = new ChooseActionTypeAction<MatchCD4>(model);
		action.setup();
		c.update(new UnserializedVectorPacket<Action>(action,"Action",0,10));
		Thread.sleep(1100);
		ViewProxy proxy = (ViewProxy)(actionController.getViewProxy());
		assertTrue(proxy.getFocusedPlayer() == -1);
	}
}
