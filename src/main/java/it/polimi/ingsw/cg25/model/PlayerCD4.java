package it.polimi.ingsw.cg25.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.UndirectedSubgraph;

import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.exceptions.OneEmporiumOnlyException;
import it.polimi.ingsw.cg25.gamegenerics.Player;
import it.polimi.ingsw.cg25.model.dashboard.Emporium;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dashboard.topological.Region;
import it.polimi.ingsw.cg25.onlinegenerics.OnlineUser;

/**
 * 
 * @author Giovanni
 *
 */
public class PlayerCD4 extends Player implements OnlineUser {

	/**
	 * The pocket of the player
	 */
	private final PocketCD4 pocket;
	/**
	 * The match where the player is playing
	 */
	private final MatchCD4 currentMatch;
	/**
	 * is the player active?
	 */
	private boolean active;
	/**
	 * is the player excluded from the turn?
	 */
	private boolean excluded;
	/**
	 * the view ID linked to the player
	 */
	private final int userID;
	/**
	 * the current hand of the player
	 */
	private final List<PoliticsCard> politicsCards;
	/**
	 * the player's emporiums
	 */
	private final List<Emporium> playerEmporiums;
	/**
	 * the List of permit cards to be used
	 */
	private final List<PermitCard> permitsToBeUsed;
	/**
	 * the List of permit cards to use
	 */
	private final List<PermitCard> usedPermits;

	/**
	 * PlayerCD4 class constructor
	 * @param viewID the id of the view linked to the player
	 * @param name the name of the player
	 * @param color the color of the player
	 * @param currentMatch the match where the player is playing
	 * @param pocket the pocket of the player
	 */
	public PlayerCD4(int viewID, String name, HSBColor color, MatchCD4 currentMatch, PocketCD4 pocket) {
		super(name, color);
		
		if(currentMatch == null)
			throw new NullPointerException("You can't create a PlayerCD4 without a match!");
		if(pocket == null)
			throw new NullPointerException("You can't create a PlayerCD4 without a pocket!");
		
		politicsCards = new ArrayList<>();
		playerEmporiums = new ArrayList<>();
		permitsToBeUsed = new ArrayList<>();
		usedPermits = new ArrayList<>();
		this.userID = viewID;
		this.currentMatch = currentMatch;
		this.pocket = pocket;
	}

	/**
	 * Add a main action in this turn for this player
	 * @param additionalMainActions
	 */
	public void addMainAction(int additionalMainActions) {
		this.currentMatch.addMainAction(additionalMainActions);
	}

	/**
	 * Add a politics card to the player's hand
	 * @param polCard the politics card to add
	 */
	public void addPoliticsCard(PoliticsCard polCard) {
		this.politicsCards.add(polCard);
	}

	/**
	 * @return the pocket of the player
	 */
	public PocketCD4 getPocket() {
		return pocket;
	}

	/**
	 * @return a reference to the hand of the player
	 */
	public List<PoliticsCard> getPoliticsCards() {
		return politicsCards;
	}

	/**
	 * @return a reference to the ArrayList of permit cards the player can use
	 */
	public List<PermitCard> getPermitsToBeUsed() {
		return permitsToBeUsed;
	}

	/**
	 * @return a reference to the ArrayList of permit cards the player has
	 *         already used
	 */
	public List<PermitCard> getUsedPermits() {
		return usedPermits;
	}

	/**
	 * @return a reference to the List of player's emporiums
	 */
	public List<Emporium> getPlayerEmporiums() {
		return playerEmporiums;
	}

	/**
	 * @return the match where the player is playing
	 */
	public MatchCD4 getCurrentMatch() {
		return currentMatch;
	}

	/**
	 * Discard a card from the player's hand
	 * @param card the card to discard
	 */
	public void discardPolitics(PoliticsCard card) {
		Iterator<PoliticsCard> i = this.politicsCards.iterator();
		while(i.hasNext()) {
			PoliticsCard pc = i.next();
			//Both cards are jolly
			if(card.getParty().getIsJolly() && pc.getParty().getIsJolly()) {
				i.remove();
				break;
			}
			//Both cards are not a jolly
			else if(pc.getParty().sameParty(card.getParty()) && !pc.getParty().getIsJolly()
					&& !card.getParty().getIsJolly()) {
				i.remove();
				break;
			}
		}
	}

	/**
	 * Build an emporium in the specified city and acquire all the bonuses of
	 * directly connected cities with a player's emporium.<br>
	 * Furthermore the method checks if the player should acquire a city color reward and/or a region reward<br>
	 * Gives 3 victory points if the player builds its last emporium
	 * @param city the city where to build
	 * @throws OneEmporiumOnlyException this exception is thrown when the current player already has 
	 * an emporium in the specified city
	 */
	public void buildEmporium(City city) throws OneEmporiumOnlyException {
		// Temp array with cities where the player has an emporium
		Set<City> citiesWithEmp = new HashSet<>();
		
		//Check if the player has already built an emporium in "city"
		for (Emporium emp : city.getCityEmporiums()) {
			if (emp.getOwner() == this)
				throw new OneEmporiumOnlyException();
		}

		// Otherwise build an emporium
		Emporium emporium = new Emporium(this, city);
		this.playerEmporiums.add(emporium);
		city.addEmporium(emporium);
		
		// If last emporium, give 3 victory points
		if(this.getPlayerEmporiums().size() == currentMatch.numberOfEmporiumsToWin)
			this.getPocket().addPocketable(new VictoryPoint(3));
		
		// Now populate citiesWithEmp
		for(City c : currentMatch.getBoard().getCities()) {
			for(Emporium emp : c.getCityEmporiums()) {
				if(emp.getOwner() == this) {
					citiesWithEmp.add(c);
					break;
				}
			}
		}

		// Get the bonuses
		// Create a subgraph
		UndirectedSubgraph<City, DefaultEdge> subGraph = new UndirectedSubgraph<>(
				currentMatch.getBoard().getRoadNetwork(), citiesWithEmp,
				currentMatch.getBoard().getRoadNetwork().edgeSet());

		// Create a subgraph connectivity inspector
		ConnectivityInspector<City, DefaultEdge> b = new ConnectivityInspector<>(subGraph);
		
		// Acquire all bonuses
		//ConnectedSetOf contains also "city"
		for(City c : b.connectedSetOf(city)) {
			c.takeAllBonuses(this);
		}
		
		// Init flags for allocating king reward cards
		boolean regionFlag = false;
		boolean colorFlag = false;
		
		// Check if the player has an emporium in all the cities of a region
		for(Region r : this.currentMatch.getBoard().getRegions()) {
			if(citiesWithEmp.containsAll(r.getCities())) {
				r.getBonusCard().takeAllBonuses(this);
				regionFlag = true;
			}
		}
		
		// Check if the player has an emporium in all the cities of a color
		List<City> sameColorWithEmp = new ArrayList<>();
		//Tutte le città in cui ci sono empori dello stesso colore di quella in cui sto costruendo
		citiesWithEmp.stream().filter(cwe -> cwe.getColor().getColor().equals(city.getColor().getColor())).
			forEach(c -> sameColorWithEmp.add(c));
		
		// A List of cities of the same color of the one where I'm building
		List<City> sameColor = new ArrayList<>();
		// Se il colore della città dell'emporio è uguale al colore della città della board...
		this.currentMatch.getBoard().getCities().stream().filter(cb -> cb.getColor().getColor().equals(city.getColor().
				getColor())).forEach(c -> sameColor.add(c));
		
		// Alloca tutti i bonus del colore
		if(sameColorWithEmp.containsAll(sameColor)) {
			sameColorWithEmp.get(0).getColor().takeAllBonuses(this);
			colorFlag = true;
		}
		
		//Alloca la prima reward card
		if(regionFlag || colorFlag)
			try {
				currentMatch.getBoard().getKingTiles().drawCard().takeAllBonuses(this);
			} catch (NoCardsException e) {
				// If no cards then do nothing
			}
	}

	@Override
	public void setStatus(boolean active) {
		this.active = active;
	}

	@Override
	public boolean isActive() {
		return this.active;
	}

	@Override
	public int getUserID() {
		return this.userID;
	}

	/**
	 * @return the excluded
	 */
	public boolean isExcluded() {
		return excluded;
	}

	/**
	 * @param excluded the excluded to set
	 */
	public void setExcluded(boolean excluded) {
		this.excluded = excluded;
	}
	
}
