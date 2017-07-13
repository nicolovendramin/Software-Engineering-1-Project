package it.polimi.ingsw.cg25.proxies;

import it.polimi.ingsw.cg25.actions.Action;
import it.polimi.ingsw.cg25.communication.UnserializedVectorPacket;
import it.polimi.ingsw.cg25.observer.Observable;
import it.polimi.ingsw.cg25.observer.Observer;
/**
 * 
 * @author nicolo
 *
 */
public class ModelProxy extends Observable<UnserializedVectorPacket<Action>> implements Observer<Action>{
	
	@Override
	public void update() {
	}

	/**
	 * Called every time the Observer(the controller in our case) has a ready action for the model
	 */
	@Override
	public void update(Action change) {
		change.getModel().receiveAction(change);
		
	}
	
	/**
	 * Simply notifies the observers of the action received as parameter 
	 * by one of the models.
	 * @param change is the action to be notified to the controller
	 */
	public void sendAction(UnserializedVectorPacket<Action> change){
		this.notifyObservers(change);
	}
	
	/**
	 * This command asks the modelProxy to kill the controllers which are observing it
	 */
	public void kill(){
		this.notifyObservers();
	}
}
