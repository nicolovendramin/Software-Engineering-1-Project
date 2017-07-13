/**
 * 
 */
package it.polimi.ingsw.cg25.model.dashboard.cards;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import it.polimi.ingsw.cg25.exceptions.NoCardsException;

/**
 * @author Davide
 * 
 */
public abstract class Deck<C extends Card> {
	
	/**
	 * Representation of Deck as Queue
	 */
	protected Queue<C> drawPile;
	/**
	 * Rando object for shuffling
	 */
	protected Random random;
	
	/**
	 * Create a new deck of cards
	 * @param cards List<Card> the cards
	 * @param shuffle boolean random shuffle needed
	 * @throws IllegalArgumentException if null pointer or no cards are given
	 */
	public Deck(List<C> cards, boolean shuffle) {
		
		if(cards == null || cards.contains(null))
			throw new IllegalArgumentException("Null pointer for cards");
		
		if(cards.isEmpty())
			throw new IllegalArgumentException("You're trying to create an empty deck!");

		// Initialize random seed
		random = new Random(System.nanoTime());
		
		// Shuffle if requested
		if(shuffle)
			this.shuffle(cards);
		
		drawPile = new LinkedList<>();
		this.enqueue(cards);
		
	}
	
	/**
	 * Returns but does not removes the head of the deck queue
	 * @return the head of the deck queue or null if empty 
	 */
	public C peek() {
		return drawPile.peek();
	}
	
	/**
	 * Shuffle a list of cards
	 * @param cards List<Card> cards to be shuffled
	 * @return List<Card> shuffled
	 */
	protected List<C> shuffle(List<C> cards) {
		Collections.shuffle(cards, this.getRandom());
		return cards;
	}
	
	/**
	 * Return the random object used for shuffling cards
	 * @return the {@link Random} object
	 */
	protected Random getRandom() {
		return this.random;
	}
	
	/**
	 * Draw a card
	 * (May be overridden in concrete deck class)
	 * @return {@link Card} the card drawn
	 * @throws NoCardsException if no cards are left
	 */
	public C drawCard() throws NoCardsException {
		if(drawPile.isEmpty())
			throw new NoCardsException();
		return drawPile.poll();
	}
	
	/**
	 * Enqueue a card in the draw pile
	 * @param cards List<Cards> the cards to be enqueued
	 */
	public void enqueue(List<C> cards) {
		Iterator<C> i = cards.iterator();
		while(i.hasNext())
			drawPile.add(i.next());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Deck [Stack = " + drawPile + "]";
	}
}
