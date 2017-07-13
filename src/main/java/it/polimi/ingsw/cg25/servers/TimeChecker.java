package it.polimi.ingsw.cg25.servers;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author nicolo
 *
 */
public abstract class TimeChecker implements Runnable{
	
	/**
	 * List of dates to be checked by the timechecker
	 */
	private List<Date> expirationDates;
	/**
	 * Status variable to know whether the thread must keep executing or if it should die
	 */
	private boolean end = false;
	
	/**
	 * Constructor
	 * @param expirationDates the dates to check
	 */
	public TimeChecker(List<Date> expirationDates){
		this.expirationDates = expirationDates;
	}
	
	/**
	 * Returns the date under control
	 * @return the list of dates that the time checker is checking
	 */
	public List<Date> getExpirationDates(){
		return this.expirationDates;
	}
	
	/**
	 * 
	 * @return a boolean value that represents the status of the thread. Alive = true
	 */
	public boolean isEnded(){
		return this.end;
	}
	
	/**
	 * Kills the time checker
	 */
	public void kill(){
		this.end = true;
	}
}
