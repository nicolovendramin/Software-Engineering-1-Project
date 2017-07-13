package it.polimi.ingsw.cg25.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable<C> {
	
	private final List<Observer<C>> observers;
	
	/**
	 * Observable class constructor. It creates an empty observers' ArrayList
	 */
	public Observable(){
		this.observers = new ArrayList<>();
	}
	
	/**
	 * Notify all the observers attached to the object
	 */
	public void notifyObservers(){
		for(Observer<C> o : this.observers)
			o.update();	
	}
	
	/**
	 * Notify all the observers attached to the object
	 * @param change: the object to be passed to observers
	 */
	public void notifyObservers(C change){
		for(Observer<C> o : this.observers)
			o.update(change);
	}
	
	/**
	 * Attach an observer
	 * @param observer: the observer
	 */
	public void attachObserver(Observer<C> observer){
		this.observers.add(observer);
	}
	
	/**
	 * Remove an observer from the observer list
	 * @param observer: the observer to remove
	 */
	public void removeObserver(Observer<C> observer){
		this.observers.remove(observer);
	}
	
	/**
	 * This method clears the List of observers
	 */
	public void removeAllObservers(){
		this.observers.clear();
	}
}
