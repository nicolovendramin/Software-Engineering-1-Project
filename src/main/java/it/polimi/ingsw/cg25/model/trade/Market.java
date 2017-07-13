/**
 * 
 */
package it.polimi.ingsw.cg25.model.trade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.cg25.actions.Action;
import it.polimi.ingsw.cg25.actions.BuyingAction;
import it.polimi.ingsw.cg25.actions.ChooseActionTypeAction;
import it.polimi.ingsw.cg25.actions.ErrorAction;
import it.polimi.ingsw.cg25.actions.PassAction;
import it.polimi.ingsw.cg25.actions.SellAssistant;
import it.polimi.ingsw.cg25.actions.SellPermit;
import it.polimi.ingsw.cg25.actions.SellPolitics;
import it.polimi.ingsw.cg25.communication.UnserializedVectorPacket;
import it.polimi.ingsw.cg25.exceptions.CannotPassException;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.gamegenerics.ActionBasedGame;
import it.polimi.ingsw.cg25.gamegenerics.GameLogger;
import it.polimi.ingsw.cg25.gamegenerics.TurnBasedGame;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * @author Davide
 *
 */
public class Market implements TurnBasedGame<PlayerCD4>, ActionBasedGame {
	/**
	 * Is the market in the first phase? First phase = selling Second phase =
	 * buying
	 */
	private boolean firstPhase;
	/**
	 * Is the market opened?
	 */
	private boolean open;
	/**
	 * List of on sale {@link Product}
	 */
	private List<Product<? extends Sellable>> onSale;
	/**
	 * List of {@link PlayerCD4} used for market turns
	 */
	private List<PlayerCD4> players;
	/**
	 * Current player index in {@link #players}
	 */
	private int currentPlayerIndex;
	/**
	 * Reference for the {@link MatchCD4}
	 */
	private MatchCD4 match;
	/**
	 * Reference to the {@link ModelProxy}
	 */
	private ModelProxy proxy;
	/**
	 * Counter for round's number
	 */
	private int roundNumber;
	/**
	 * Unique tag for next product (i.d. barcode)
	 */
	private int tag = 0;

	/**
	 * Market constructor
	 * 
	 * @param match
	 *            {@link MatchCD4} the related match
	 * @param proxy
	 *            {@link ModelProxy} the related model proxy
	 */
	public Market(MatchCD4 match, ModelProxy proxy) {
		if(match == null || proxy == null)
			throw new NullPointerException();
		this.match = match;
		this.onSale = new ArrayList<>();
		this.players = new ArrayList<>();
		this.proxy = proxy;
	}

	/**
	 * Get the players
	 * 
	 * @return {@link List} of {@link PlayerCD4}
	 */
	public List<PlayerCD4> getPlayers() {
		return this.players;
	}

	/**
	 * Opens a new market session: <br>
	 * <ul><li>Set market phase to the first phase</li>
	 * <li>Gets active players from the match with proper order</li>
	 * <li>Begin first market's turn</li></ul>
	 */
	public void openMarket() {
		this.open = true;
		this.firstPhase = true;
		this.match.getPlayers().stream().filter(p -> p.isActive()).forEach(this.players::add);
		this.match.getLogger().log("Market opened");
		this.setOrder(this.players);
		this.roundNumber = 0;
		this.onSale.clear();
		this.beginTurn();
	}

	/**
	 * Closes a market session<br><ul>
	 * <li>Gives back to owner unsold products</li>
	 * <li>Begins the turn of the match</li></ul>
	 */
	public void closeMarket() {
		// Give back unsold products to owner
		for (Product<? extends Sellable> product : this.onSale) {
			// Player is always in the player's list.
			product.achieveProduct(product.getOwner());
		}
		this.onSale.clear();
		this.match.getLogger().log("Market closed");
		this.open = false;
		// Checks if the current player in match is still active
		if(!this.match.getCurrentPlayer().isActive())
			try {
				this.match.endTurn();
			} catch (CannotPassException e) {
				this.getLogger().log(e,"Passed: first player after closing market is inactive");
			}
		match.broadCastGameStatus();
		this.match.beginTurn();
	}

	/**
	 * Return the current player
	 * 
	 * @return {@link PlayerCD4} the current player
	 */
	public PlayerCD4 getCurrentPlayer() {
		return this.players.get(this.currentPlayerIndex);
	}

	/**
	 * Begin of player's turn
	 */
	@Override
	public void beginTurn() {
		if(!this.isOpen())
			return;
		Action a = new ChooseActionTypeAction<>(this);
		try {
			a.setup();
		} catch (CannotSetupActionException e) {
			this.match.getLogger().log(e, "Something is wrong with the market actions");
		}
		this.match.broadCastGameStatus();
		this.proxy
				.sendAction(new UnserializedVectorPacket<Action>(a, "Action", this.getCurrentPlayer().getUserID(), 0));
	}

	/**
	 * End of player's turn Get the next player Calls {@link #endRound()} or
	 * {@link #closeMarket()} if needed
	 * 
	 * @see #nextPlayer()
	 * @see #endRound()
	 * @see #closeMarket()
	 */
	@Override
	public void endTurn() throws CannotPassException {
		// Check canPass isn't really needed in market.
		if (!this.canPass())
			throw new CannotPassException();

		PlayerCD4 nextP = this.nextPlayer();

		if (players.indexOf(nextP) < this.currentPlayerIndex) {
			// NB: endRound() takes care of phases and closes market if needed
			this.endRound();
			// NextPlayer may be changed by this.setOrded() called in
			// this.endRound()
			nextP = this.nextPlayer();
			this.setCurrentPlayer(nextP);
		} else if (players.indexOf(nextP) == this.currentPlayerIndex) {
			// Only one player is still active
			this.closeMarket();
		} else {
			this.setCurrentPlayer(nextP);
		}
	}

	/**
	 * Set the current player
	 * 
	 * @param player
	 *            {@link PlayerCD4} the player that will be set to current
	 */
	private void setCurrentPlayer(PlayerCD4 player) {
		this.currentPlayerIndex = this.players.indexOf(player);
	}

	/**
	 * Return the next player
	 * 
	 * @return {@link PlayerCD4} the next player
	 */
	@Override
	public PlayerCD4 nextPlayer() {
		PlayerCD4 p;
		for (int index = this.currentPlayerIndex + 1; index != this.currentPlayerIndex; index = (index + 1)
				% this.players.size()) {
			p = this.players.get(index % this.players.size());
			if (p.isActive())
				return p;
		}
		// If no Market players are still active close the Market.
		// Don't end the whole game: some inactive players in the match
		// (not doing trading) may have become active now.
		this.closeMarket();
		return null;

	}

	// Used in the actions
	@Override
	public boolean canPass() {
		return true;
	}

	// UnUsed
	@Override
	public boolean hasWon() {
		return false;
	}

	@Override
	public int getRoundNumber() {
		return this.roundNumber;
	}

	/**
	 * Set the proper order of players<br>
	 * First phase: same as match order<br>
	 * Second phase: random order
	 */
	@Override
	public void setOrder(List<PlayerCD4> order) {
		if (this.firstPhase) {
			// Every *active* player (upon match's order) can sale some product
			this.players.clear();
			this.match.getPlayers().stream().filter(p -> p.isActive()).forEach(this.players::add);
		} else {
			// Every player (random order) can buy some product
			Collections.shuffle(order, this.match.getRandom());
		}
	}

	/**
	 * Close a round of the market:<br>
	 * Changes market phase sets the proper order of players 
	 * and increments the round counter
	 */
	@Override
	public void endRound() {
		this.roundNumber++;
		if (this.roundNumber > 1)
			this.closeMarket();
		this.firstPhase = !this.firstPhase;
		this.setOrder(this.players);
	}

	/**
	 * Add a product to the on sale list
	 * 
	 * @param product
	 *            {@link Product} the product to add
	 */
	public void addProduct(Product<? extends Sellable> product) {
		if(product == null)
			throw new NullPointerException("Null product reference");
		this.onSale.add(product);
	}

	/**
	 * Remove a product to the on sale list
	 * 
	 * @param product
	 *            {@link Product} the product to remove
	 */
	public void removeProduct(Product<?> product) {
		this.onSale.remove(product);
	}

	@Override
	public List<Action> getAvailableActions() {
		List<Action> temp = new ArrayList<>();
		if (firstPhase) {
			temp.add(new SellPermit(this));
			temp.add(new SellPolitics(this));
			temp.add(new SellAssistant(this));
		} else
			temp.add(new BuyingAction(this));
		temp.add(new PassAction<Market>(this));
		return temp;
	}

	@Override
	public Action getStartingAction() {
		return new ChooseActionTypeAction<Market>(this);
	}

	@Override
	public void receiveAction(Action change) {
		if (!this.open)
			return;
		try {
			PlayerCD4 prevP = this.getCurrentPlayer();
			int prevRound = this.roundNumber;
			if(change.isAborted())
			{
				Action action = this.getStartingAction();
				action.setup();
				this.proxy.sendAction(new UnserializedVectorPacket<Action>(action, "Action",
						this.getCurrentPlayer().getUserID(), 0));
				return;
			}
			Action returned = change.doAction();
			if (!this.open)
				return;
			if (prevP != this.getCurrentPlayer() && this.roundNumber == prevRound) {
				this.beginTurn();
			} else if (this.roundNumber != prevRound) {
				this.beginTurn();
			} else {
				this.match.informPlayer(getCurrentPlayer());
				returned.setup();
				this.proxy.sendAction(new UnserializedVectorPacket<Action>(returned, "Action",
						this.getCurrentPlayer().getUserID(), 0));
			}
		} catch (CannotPerformActionException e) {
			this.getLogger().log(e, "There has been an error executing the requested action");
			ErrorAction<Market> a = new ErrorAction<>(this);
			try {
				a.setup(e.getMessage());
			} catch (Exception e1) {
				this.getLogger().log(e1, "There was an error setting un the ErrorAction");
				a.setup("Some error occurred");
			}
			this.proxy.sendAction(
					new UnserializedVectorPacket<Action>(a, "Action", this.getCurrentPlayer().getUserID(), 0));
		} catch (CannotSetupActionException e) {
			this.getLogger().log(e, "Couldn't setup the action");
			ChooseActionTypeAction<Market> a = new ChooseActionTypeAction<>(this);
			this.proxy.sendAction(
					new UnserializedVectorPacket<Action>(a, "Action", this.getCurrentPlayer().getUserID(), 0));
		}

	}

	/**
	 * Get the next product's available tag
	 * <br> <b>MUST</b> be called when creating a new product
	 * @return int the tag to use
	 * @see #tag
	 */
	public int getNextProductTag() {
		this.tag = this.tag + 1;
		return tag - 1;
	}

	/**
	 * Return the Match's Game Logger
	 * 
	 * @return {@link GameLogger} Match's Game Logger
	 */
	@Override
	public GameLogger getLogger() {
		return this.match.getLogger();
	}

	/**
	 * Checks the market's status
	 * 
	 * @return boolean true if market is open
	 */
	public boolean isOpen() {
		return this.open;
	}

	/**
	 * Return on sale products
	 * 
	 * @return {@link List} of {@link Product} on sale products
	 */
	public List<Product<? extends Sellable>> getProducts() {
		return onSale;
	}

	/**
	 * Return the phase of the market
	 * @return true if in first phase, false if in second phase
	 */
	public boolean getPhase() {
		return firstPhase;
	}

}
