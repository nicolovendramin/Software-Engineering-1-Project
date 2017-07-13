package it.polimi.ingsw.cg25.interactions;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.ChooseActionTypeAction;
import it.polimi.ingsw.cg25.actions.DisplayAction;
import it.polimi.ingsw.cg25.actions.DisplayInteraction;
import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.actions.SingleChoiceInteraction;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

public class DisplayInteractionTest {

	private PocketCD4 pocket1;
	private PocketCD4 pocket2;
	private MatchCD4 model;
	private ModelProxy proxy;
	private PlayerCD4 player1;
	private PlayerCD4 player2;
	
	@Before
	public void setUp() throws Exception {
		BoardFactory factory = new BoardFactory(new FileReader("src/test/resources/nobilityCells.txt"),
				new FileReader("src/test/resources/politics.txt"),
				new FileReader("src/test/resources/cities.txt"),
				new FileReader("src/test/resources/graph.txt"),
				new FileReader("src/test/resources/king.txt"),
				new FileReader("src/test/resources/regions.txt"));
		proxy = new ModelProxy();
		model = new MatchCD4(factory.getBoard(), proxy, true,10);
		pocket1 = new PocketCD4(new Coin(100),new Assistant(100),new NobilityRank(1),new VictoryPoint(0));
		pocket2 = new PocketCD4(new Coin(100),new Assistant(100),new NobilityRank(1),new VictoryPoint(0));
		player1 = new PlayerCD4(1,"gio",HSBColor.getNDifferent(2).get(0),model,pocket1);
		player2 = new PlayerCD4(2, "nicolo", HSBColor.getNDifferent(2).get(1), model,pocket2);
		player1.setStatus(true);
		player2.setStatus(true);
		model.addPlayer(player1);
		model.addPlayer(player2);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void noNullContent() throws Exception {
		new DisplayInteraction<String>(null);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void noNullMessage() throws Exception {
		new DisplayInteraction<String>(null,"ciao");
	}

	@Test
	public void getContent() {
		DisplayInteraction<String> interaction = new DisplayInteraction<String>("Hello World");
		assertTrue(interaction.getContent().equals("Hello World"));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void registerNullReply(){
		Interaction ciao = null;
		DisplayInteraction<String> interaction = new DisplayInteraction<String>("Hello World");
		interaction.registerReply(ciao);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void registerReplyWithDifferentClasses() {
		List<String> strings = new ArrayList<>();
		strings.add("Hello");
		Interaction hello = new SingleChoiceInteraction<String>(strings);
		DisplayInteraction<String> interaction = new DisplayInteraction<String>("Hello World");
		interaction.registerReply(hello);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void registerOtherInteractionAsReply(){
		List<String> strings = new ArrayList<>();
		strings.add("Hello");
		Interaction hello = new SingleChoiceInteraction<String>(strings);
		DisplayInteraction<String> interaction = new DisplayInteraction<String>("Hello World");
		//This triggers the exception
		interaction.registerReply(hello);
	}

	@Test
	public void testHashCode() {
		DisplayInteraction<String> interaction = new DisplayInteraction<String>("Hello World");
		DisplayInteraction<String> interaction2 = new DisplayInteraction<String>("Hello");
		assertTrue(interaction.hashCode() == interaction.hashCode());
		assertFalse(interaction.hashCode() == interaction2.hashCode());
	}
	
	@Test
	public void okRegisterReplies(){
		DisplayInteraction<String> interaction = new DisplayInteraction<String>("Hello World");
		//Do nothing
		interaction.registerReply();
		assertEquals("Hello World", interaction.getContent());
		//Do nothing
		interaction.registerReply("Hello");
		assertEquals("Hello World", interaction.getContent());
		//Do nothing
		interaction.registerReply(new DisplayInteraction<String>("Hi"));
		assertEquals("Hello World", interaction.getContent());
	}
	
	@Test
	public void assertTrueEquals(){
		assertTrue(new DisplayInteraction<String>("hello").equals(new DisplayInteraction<String>("Hello")));
	}

	@Test
	public void printOptionsTest(){
		DisplayInteraction<String> interaction = new DisplayInteraction<String>("Hello World");
		assertEquals(interaction.printOptions()
				,interaction.getQuestion().concat(interaction.getContent()));
	}
	
	@Test
	public void testDisplayAction() throws CannotPerformActionException {
		DisplayAction<String> da = new DisplayAction<>(model);
		DisplayInteraction<String> interaction = new DisplayInteraction<String>("Hello World");
		da.setup(interaction);
		assertEquals(da.getInteraction(0).printOptions()
				,interaction.getQuestion().concat(interaction.getContent()));
		assertEquals(da.doAction().getClass(), ChooseActionTypeAction.class);
	}
	
}
