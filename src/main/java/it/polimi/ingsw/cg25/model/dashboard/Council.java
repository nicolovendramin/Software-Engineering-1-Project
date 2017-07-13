package it.polimi.ingsw.cg25.model.dashboard;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import it.polimi.ingsw.cg25.exceptions.NoGoodCardsException;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;

/**
 * 
 * @author Giovanni
 *
 */
public class Council {

	/**
	 * This is the name of the region where the council is located.
	 */
	private final String location;
	/**
	 * The balcony is represented as a queue
	 */
	private final Queue<Councelor> councelors;
	
	/**
	 * Council class constructor
	 * @param councelors the queue of counselors who join the council
	 * @exception IllegalArgumentException when councelors List is empty or location is null and when
	 * the number of councelors per queue is different from 4
	 */
	public Council(Queue<Councelor> councelors, String location) {
		if(councelors == null || councelors.isEmpty())
			throw new IllegalArgumentException("You can't create a Council without councelors!");
		if(location == null)
			throw new IllegalArgumentException("You can't create a Council without a location!");
		if(councelors.size() != 4)
			throw new IllegalArgumentException("Each coucil can only have 4 councelors!");
		this.councelors = councelors;
		this.location = location;
	}
	
	/**
	 * Elect a councelor
	 * @param councelor the new councelor to be elected
	 * @return the councelor who leaves the council
	 */
	public Councelor electCouncelor(Councelor councelor) {
		councelors.add(councelor);
		return councelors.remove();
	}
	
	/**
	 * Compare the color of the cards in the list with the party of the councelors
	 * and return the amount of money to pay in order to satisfy the council.
	 * Pay attention: the method removes matching cards
	 * @param givenHand the ArrayList of cards to compare
	 * @return the amount of coins to pay to satisfy the council
	 */
	public int goodHand(List<PoliticsCard> givenHand) throws NoGoodCardsException {
		int goodCards = 0;
		int numOfJolly = 0;
		
		//Count jolly and remove them from givenHand
		Iterator<PoliticsCard> it = givenHand.iterator();
		while(it.hasNext()) {
			PoliticsCard card = it.next();
			if(card.getParty().getIsJolly()) {
				numOfJolly++;
				goodCards++;
				it.remove();
			}
		}
		
		//Now for each councelor do...
		for(Councelor coun : this.councelors) {
			Iterator<PoliticsCard> i = givenHand.iterator();
			while(i.hasNext()) {
				//We are sure that there are no jolly
				PoliticsCard card = i.next();
				if(card.getParty().sameParty(coun.getParty())) {
					if(!card.getParty().getIsJolly()) {
						i.remove();
						goodCards++;
						break;
					}
				}
			}
		}
		
		switch(goodCards) {
			case(1):
				return 10 + numOfJolly;
			case(2):
				return 7 + numOfJolly;
			case(3):
				return 4 + numOfJolly;
			case(4):
				return 0 + numOfJolly;
			default:
				throw new NoGoodCardsException();
		}
				
	}
	
	/**
	 * @return the councelors queue
	 */
	public Queue<Councelor> getCouncelors() {
		return councelors;
	}
	
	/**
	 * @return the location of this council
	 */
	public String getLocation() {
		return location;
	}
	
}
