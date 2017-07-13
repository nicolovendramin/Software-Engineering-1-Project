package it.polimi.ingsw.cg25.model.dashboard.topological;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.model.dashboard.Council;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitDeck;
import it.polimi.ingsw.cg25.model.dashboard.cards.RewardCard;

/**
 * 
 * @author Giovanni
 *
 */
public class Region {
	
	/**
	 * The name of the region
	 */
	private final String name;
	/**
	 * The cities of the region
	 */
	private final List<City> cities;
	/**
	 * The faced up permits of the region
	 */
	private final List<PermitCard> faceUpPermits;
	/**
	 * The deck of permit cards
	 */
	private final PermitDeck permitTiles;
	/**
	 * The council of the region
	 */
	private final Council council;
	/**
	 * The reward card linked to the region
	 */
	private final RewardCard bonusCard;
	/**
	 * The number of faced up cards
	 */
	private final int numFacedUpPermits;

	/**
	 * Region class constructor
	 * @param name the name of the region
	 * @param cities a List of cities of the region
	 * @param permitTiles the reference to a permit deck
	 * @param council the region's council
	 * @param bonusCard the region's bonus card
	 * @param numFacedUpPermits the number of faced up permits
	 * @exception NullPointerException when at least one of the arguments is null
	 * @exception IllegalArgumentException when the List of cities is empty or the number of faced up cards
	 * is less than 1
	 */
	public Region(String name, List<City> cities, PermitDeck permitTiles, Council council, RewardCard bonusCard,
			int numFacedUpPermits) {
		if(name == null)
			throw new NullPointerException("You can't create a region without a name!");
		if(cities == null)
			throw new NullPointerException("You can't create a region without a List of cities!");
		if(cities.isEmpty())
			throw new IllegalArgumentException("You can't create a region without at least a city!");
		if(permitTiles == null)
			throw new NullPointerException("You can't create a region without a permit deck!");
		if(council == null)
			throw new NullPointerException("You can't create a region without a council!");
		if(bonusCard == null)
			throw new NullPointerException("You can't create a region without a reward card!");
		if(numFacedUpPermits < 1)
			throw new IllegalArgumentException("The number of faced up permits is invalid!");
		
		this.name = name;
		this.cities = cities;
		this.permitTiles = permitTiles;

		faceUpPermits = new ArrayList<>();
		this.numFacedUpPermits = numFacedUpPermits;
		
		for (int i = 1; i <= numFacedUpPermits; i++) {
			try {
				faceUpPermits.add(permitTiles.drawCard());
			} catch (NoCardsException e) {
				//Do nothing
			}
		}

		this.council = council;
		this.bonusCard = bonusCard;
	}

	/**
	 * @return the name of the region
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the ArrayList of the cities in this region
	 */
	public List<City> getCities() {
		return cities;
	}

	/**
	 * @return the council of the region
	 */
	public Council getCouncil() {
		return council;
	}

	/**
	 * @return the bonus of the region
	 */
	public RewardCard getBonusCard() {
		return bonusCard;
	}

	/**
	 * @return the reference to the faceUpPermits ArrayList
	 */
	public List<PermitCard> getFaceUpPermits() {
		return faceUpPermits;
	}

	/**
	 * @param index the index of the faced up permit you want to draw
	 * @return the selected faced up permit card
	 * @throws NoCardsException if there are no more cards to return
	 */
	public PermitCard getPermit(int index) throws NoCardsException {
		if (faceUpPermits.isEmpty())
			throw new NoCardsException();
		PermitCard tempCard = faceUpPermits.remove(index);
		// Aggiunge una nuova carta scoperta
		try {
			this.faceUpPermits.add(permitTiles.drawCard());
		} catch (NoCardsException exc) {

		}
		// Ritorna la carta scelta
		return tempCard;
	}

	/**
	 * changeFaceUpPermits allows the player to change the faced up
	 * permits of a region
	 * @throws NoCardsException if the deck of permits is empty
	 */
	public void changeFaceUpPermits() throws NoCardsException {
		// Enqueue all the faced-up permit cards
		permitTiles.enqueue(faceUpPermits);
		faceUpPermits.clear();

		for (int i = 1; i <= numFacedUpPermits; i++) {
			this.faceUpPermits.add(permitTiles.drawCard());
		}
	}
	
}
