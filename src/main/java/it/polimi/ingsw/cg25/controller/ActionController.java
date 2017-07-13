package it.polimi.ingsw.cg25.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import it.polimi.ingsw.cg25.actions.Action;
import it.polimi.ingsw.cg25.actions.DisplayInteraction;
import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.communication.UnserializedVectorPacket;
import it.polimi.ingsw.cg25.communication.VectorPacket;
import it.polimi.ingsw.cg25.observer.Observable;
import it.polimi.ingsw.cg25.observer.Observer;
import it.polimi.ingsw.cg25.proxies.ViewProxy;
import it.polimi.ingsw.cg25.servers.TimeChecker;
/**
 * This controller is asked to manage the filling of the action from the current player
 * and to check the time to avoid lazy users to kill the match
 * 
 * @author nicolo
 *
 */
public class ActionController extends Observable<Action> implements Observer<UnserializedVectorPacket<Action>> {

	/**
	 * The action which the controller is asking the user to fill
	 */
	private Action currentlyExecutingAction;
	/**
	 * The reference to the viewProxy that the ActionController uses as an interface 
	 * with the users
	 */
	private ViewProxy viewProxy;
	/**
	 * The number of milliseconds to be used as action time out
	 */
	private final int actionTimer;
	/**
	 * The timer used to kick lazy users
	 */
	private ActionTimeChecker timer;
	/**
	 * The type of the last packet received from the modelProxy
	 */
	private String currentPacketType;
	
	/**
	 * 
	 * @author nicolo
	 *
	 */
	private class ActionTimeChecker extends TimeChecker{

		/**
		 * Simple constructor for the action time checker
		 * @param expirationDates is the list of dates to check
		 */
		public ActionTimeChecker(List<Date> expirationDates) {
			super(expirationDates);
		}

		/**
		 * This constructor instantiates a timechecker which only controls the
		 * given date
		 * @param expirationDate is the date to be checked
		 */
		public ActionTimeChecker(Date expirationDate) {
			super(new ArrayList<>());
			this.getExpirationDates().add(expirationDate);
		}
		
		@Override
		public void run() {
		while(!this.isEnded())
		{
			for(int i = 0;i<this.getExpirationDates().size();i++)
				if(this.getExpirationDates().get(i).before(new Date()))
				{
					int focus = viewProxy.getFocusedPlayer();
					viewProxy.changeFocus(-1);
					viewProxy.update(new VectorPacket<Interaction>(new DisplayInteraction<String>("The client timeout for replying to the action is over."),"Disconnection",0,focus));
					this.kill();
					return;
				}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				currentlyExecutingAction.getModel().getLogger().log(e, "Failure in Action timeout checker");
			}		
			
			} 
		}
		
		@Override
		public void kill(){
			super.kill();
			this.getExpirationDates().clear();
		}
	}
	
	/**
	 * Constructor for the action controller
	 * @param actionTimer the amount of millisecond to wait before kicking a lazy user
	 */
	public ActionController(int actionTimer){
		this.actionTimer = actionTimer;
	}
	
	/**
	 * This function sets the viewProxy of the server 
	 * @param viewProxy is the viewProxy that we want to use for this specific Controller
	 */
	public void setViewProxy(ViewProxy viewProxy){
		this.viewProxy = viewProxy;
	}

	@Override
	public void update() {
		this.timer.kill();
	}

	/**
	 * This packet is the one containing the new Action to be performed
	 * coming from the model. When this method is called the controller starts
	 * going through the different interactions of the action asking the player to fill them
	 */
	@Override
	public void update(UnserializedVectorPacket<Action> change) {
		Action c = change.getContent();
		this.viewProxy.changeFocus(change.getDestination());
		this.currentlyExecutingAction = c;
		this.currentPacketType = change.getType();
		this.start("Action".equals(change.getType()));
	}

	/**
	 * this method is exposed to the viewProxy to be able to send back the Interaction once
	 * that it has been filled. If the interaction which is given back is not correct the 
	 * controller asks again the view for the same interaction
	 * @param filledInteraction is the interaction correctly filled
	 */
	public void sendReply(Interaction filledInteraction) {
		//We must check that the interaction returned is the one that we were waiting for
		this.timer.kill();
		this.currentlyExecutingAction.sendReply(filledInteraction);
		if(this.currentlyExecutingAction.hasNext())
		{
				this.viewProxy.sendToFocusedPlayer(this.currentlyExecutingAction.next(),this.currentPacketType);
				countDown();
		}
		else {
			this.notifyObservers(currentlyExecutingAction);
		}
	}

	private void countDown(){
		this.timer = new ActionTimeChecker(new Date(System.currentTimeMillis()+actionTimer));
		Thread t = new Thread(timer);
		t.start();
	}
	
	/**
	 * @return the ViewProxy used by this controller to communicate with its views
	 */
	public Observer<VectorPacket<Interaction>> getViewProxy() {
		return this.viewProxy;
	}

	/**
	 * This method just initiate the controller's work of defining all the 
	 * interactions of the action
	 */
	public void start(boolean timerStarting) {
		if (currentlyExecutingAction.hasNext())
		{
			viewProxy.sendToFocusedPlayer(this.currentlyExecutingAction.getInteraction(0),this.currentPacketType);
			if(timerStarting)
				countDown();
		}
		else
			this.notifyObservers(currentlyExecutingAction);
	}

}
