/**
 * 
 */
package it.polimi.ingsw.cg25.model;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import it.polimi.ingsw.cg25.actions.Action;
import it.polimi.ingsw.cg25.actions.ChooseActionTypeAction;
import it.polimi.ingsw.cg25.actions.ChooseMainActionAction;
import it.polimi.ingsw.cg25.actions.ChooseQuickActionAction;
import it.polimi.ingsw.cg25.actions.DisplayAction;
import it.polimi.ingsw.cg25.actions.DisplayInteraction;
import it.polimi.ingsw.cg25.actions.ErrorAction;
import it.polimi.ingsw.cg25.actions.PassAction;
import it.polimi.ingsw.cg25.communication.UnserializedVectorPacket;
import it.polimi.ingsw.cg25.exceptions.CannotPassException;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.exceptions.EndGameException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.gamegenerics.ActionBasedGame;
import it.polimi.ingsw.cg25.gamegenerics.GameLogger;
import it.polimi.ingsw.cg25.gamegenerics.Match;
import it.polimi.ingsw.cg25.gamegenerics.TurnBasedGame;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dto.GameStatus;
import it.polimi.ingsw.cg25.model.trade.Market;
import it.polimi.ingsw.cg25.onlinegenerics.Applet;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

/**
 * @author Davide
 *
 */
public class MatchCD4 extends Match<PlayerCD4> implements TurnBasedGame<PlayerCD4>, ActionBasedGame, Applet {

	/**
	 * The board of the match
	 */
	private final BoardCD4 board;
	
	/**
	 * Available main actions for the current player
	 */
	private int currentPlayerMainActions = 1;
	
	/**
	 * Available quick actions for the current player
	 */
	private int currentPlayerQuickActions = 1;
	
	/**
	 * Random object
	 */
	private Random random;
	
	/**
	 * Round counter
	 */
	private int roundNumber = -1;
	
	/**
	 * Checks if the current round is the last of the game
	 */
	private boolean lastRound;
	
	/**
	 * Checks if someone has won 
	 */
	private boolean someoneWon;
	
	/**
	 * Action stack for the current player
	 * @see Action
	 */
	private Deque<Action> actionsStack;
	
	/**
	 * Model proxy object
	 */
	private ModelProxy proxy;
	
	/**
	 * Logger of the match
	 */
	private GameLogger logger;
	
	/**
	 * Determines if the game has the market
	 */
	private final boolean hasMarket;

	/**
	 * Market of the match
	 */
	private final Market market;
	
	/**
	 * Determines if the game is running
	 */
	private boolean running = true;
	
	/**
	 * Flag for computing final scores only once
	 */
	private boolean finalScore = false;

	/**
	 * The number of emporiums per player to win the game
	 */
	public final int numberOfEmporiumsToWin;
	
	/**
	 * Match constructor (without logger)
	 * @param board {@link BoardCD4} the board for the match
	 * @param proxy {@link ModelProxy} the model proxy object
	 * @param hasMarket boolean true for a game with market
	 * @param numberOfEmporiumsToWin is the number of emporium that the player has to build to finish the game
	 */
	public MatchCD4(BoardCD4 board, ModelProxy proxy, boolean hasMarket,int numberOfEmporiumsToWin) {
		if(board == null || proxy == null)
			throw new IllegalArgumentException("Match constructor: Paramters cannot be null");
		// Initialize the random seed
		this.random = new Random(System.nanoTime());
		this.actionsStack = new ArrayDeque<>();
		this.proxy = proxy;
		this.board = board;
		this.roundNumber = 0;
		this.logger = new GameLogger();
		this.logger.log("Match started");
		if(numberOfEmporiumsToWin<=0)
			this.numberOfEmporiumsToWin = 10;
		else
			this.numberOfEmporiumsToWin = Math.min(numberOfEmporiumsToWin, this.getBoard().getCities().size());
		// Market
		this.hasMarket = hasMarket;
		if (this.hasMarket)
			this.market = new Market(this, proxy);
		else
			this.market = null;
	}
	
	/**
	 * Match constructor (with logger)
	 * @param board the {@link BoardCD4} for the match
	 * @param proxy the {@link ModelProxy} object
	 * @param hasMarket true for a game with market
	 * @param numberOfEmporiumsToWin is the number of emporium that the player has to build to finish the game
	 * @param logger the {@link GameLogger} for the match
	 */
	public MatchCD4(BoardCD4 board, ModelProxy proxy, boolean hasMarket,int numberOfEmporiumsToWin, GameLogger logger){
		this(board,proxy,hasMarket,numberOfEmporiumsToWin);
		if(board == null || proxy == null)
			throw new IllegalArgumentException("Match constructor: Paramters cannot be null");
		this.logger = logger;
		this.logger.log("Match started");
	}

	/**
	 * Increments the number of available main action for the current player
	 * 
	 * @param additionalMainActions
	 *            int the increment
	 */
	public void addMainAction(int additionalMainActions) {
		this.currentPlayerMainActions += additionalMainActions;
	}

	/**
	 * Decrements (1 step) the number of available quick action for the current
	 * player
	 * 
	 * @throws CannotPerformActionException
	 *             if the current player cannot do a quick action
	 */
	public void useQuickAction() throws CannotPerformActionException {
		if (this.currentPlayerQuickActions <= 0)
			throw new CannotPerformActionException();
		this.currentPlayerQuickActions = this.currentPlayerQuickActions - 1;
	}

	/**
	 * Decrements (1 step) the number of available main action for the current
	 * player
	 * 
	 * @throws CannotPerformActionException
	 *             if the current player cannot do a main action
	 */
	public void useMainAction() throws CannotPerformActionException {
		if (this.currentPlayerMainActions <= 0)
			throw new CannotPerformActionException();
		this.currentPlayerMainActions = this.currentPlayerMainActions - 1;
	}

	/**
	 * Sends a {@link GameStatus} to a player
	 * @param player {@link PlayerCD4} the receiver player 
	 */
	public void informPlayer(PlayerCD4 player) {
		DisplayAction<GameStatus> a = new DisplayAction<>(this);
		a.setup(new DisplayInteraction<GameStatus>(new GameStatus(this, player)));
		this.proxy.sendAction(new UnserializedVectorPacket<Action>(a, "GameStatus", player.getUserID(), 0));
	}

	/**
	 * Gets the board of the match
	 * @return {@link BoardCD4} the board of the match
	 */
	public BoardCD4 getBoard() {
		return this.board;
	}

	/**
	 * Returns the quantity of available main action for the current player
	 * 
	 * @return int quantity of available main action
	 */
	public int getRemainingMainActions() {
		return this.currentPlayerMainActions;
	}

	/**
	 * Returns the quantity of available main action for the current player
	 * 
	 * @return int quantity of available main action
	 */
	public int getRemainingQuickActions() {
		return this.currentPlayerQuickActions;
	}

	@Override
	public synchronized void receiveAction(Action change) {
		String action = "Action";
		// Received an action while the match isn't running
		if (!this.running) {
			String eMessage = "Match has received an action while it was not running.";
			logger.warning(eMessage);
			ErrorAction<MatchCD4> a = new ErrorAction<>(this);
			a.setup(eMessage);
			this.proxy.sendAction(
					new UnserializedVectorPacket<Action>(a, "Error", this.getCurrentPlayer().getUserID(), 0));
			proxy.kill();
			return;
		}
		try {
			int exAction = this.currentPlayerMainActions;
			int exqAction = this.currentPlayerQuickActions;
			int precRound = this.roundNumber;
			PlayerCD4 prevP = this.getCurrentPlayer();
			if(change.isAborted())
			{
				Action act = this.getStartingAction();
				act.setup();
				this.proxy.sendAction(new UnserializedVectorPacket<Action>(act, "Action",
						this.getCurrentPlayer().getUserID(), 0));
				return;
			}
			Action c = change.doAction();
			if(!running && change.getClass()==PassAction.class)
			{
				return;
			}
			if ((exAction != currentPlayerMainActions || exqAction != this.currentPlayerQuickActions)
					&& this.getCurrentPlayer() == prevP) {
				this.informPlayer(this.getCurrentPlayer());
			}
			if (this.getCurrentPlayer() == prevP && this.roundNumber == precRound) {
				c.setup();
				this.proxy.sendAction(
						new UnserializedVectorPacket<Action>(c, action, this.getCurrentPlayer().getUserID(), 0));
			} 
			else if (this.roundNumber == precRound || !this.hasMarket)
				this.beginTurn();
		} catch (CannotPerformActionException e) {
			logger.log(e, "There has been an error executing the requested action");
			ErrorAction<MatchCD4> a = new ErrorAction<>(this);
			a.setup(e.getMessage());
			this.proxy.sendAction(
					new UnserializedVectorPacket<Action>(a, action, this.getCurrentPlayer().getUserID(), 0));
		} catch (CannotSetupActionException e) {
			logger.log(e, "Couldn't setup the action");
			ErrorAction<MatchCD4> a = new ErrorAction<>(this);
			a.setup(e.getMessage());
			this.proxy.sendAction(
					new UnserializedVectorPacket<Action>(a, action, this.getCurrentPlayer().getUserID(), 0));
		}

	}

	/**
	 * Begin of a player's turn:<br>
	 * Resets counters of available actions, gives the current player a
	 * {@link PoliticsCard} if the relative deck is not empty, broadcasts the
	 * game status to all players of the match
	 */
	@Override
	public synchronized void beginTurn() {
		if(this.roundNumber==-1)
		{
			this.roundNumber = 0;
			this.running = true;
		}
		this.currentPlayerMainActions = 1;
		this.currentPlayerQuickActions = 1;
		try {
			this.getCurrentPlayer().addPoliticsCard(this.getBoard().getPoliticsDeck().drawCard());
		} catch (NoCardsException e) {
			logger.log(e, "The player could not draw any card because the deck is empty");
		} finally {
			this.broadCastGameStatus();
		}
		if(!running)
			return;
		Action s = new ChooseActionTypeAction<MatchCD4>(this);
		try {
			s.setup();
		} catch (Exception e) {
			logger.log(e, "There was a problem. No action can be setup");
		} // This exception will never be thrown cause at least one Action can
			// always be performed
		this.proxy
				.sendAction(new UnserializedVectorPacket<Action>(s, "Action", this.getCurrentPlayer().getUserID(), 0));
	}

	/**
	 * Broadcasts the game status to all players of the match
	 * 
	 * @see {@link DisplayInteraction}
	 */
	public void broadCastGameStatus() {
		for (PlayerCD4 p : players) {
			if (p.isActive()) {
				DisplayAction<GameStatus> a = new DisplayAction<>(this);
				a.setup(new DisplayInteraction<GameStatus>(new GameStatus(this, p)));
				this.proxy.sendAction(new UnserializedVectorPacket<Action>(a, "GameStatus", p.getUserID(), 0));
			}
		}
	}
	

	/**
	 * Ends the turn of the current player and sets the next player to current
	 */
	@Override
	public synchronized void endTurn() throws CannotPassException {

		if (!this.canPass() && this.getCurrentPlayer().isActive())
			throw new CannotPassException();
		
		if (this.hasWon()) {
			this.someoneWon = true;
			// Setting the current player (temp winner) inactive for the last round
			this.getCurrentPlayer().setExcluded(true);
		}
		
		// Count all active players
		int activeCounter = 0;
		for(int i=0;i<players.size();i++){
			if(players.get(i).isActive()){
				activeCounter ++;
			}
			if(activeCounter > 1)
				break;
		}
		// Zero or only one active player is left: end the game immediatly
		if(activeCounter <2)
			this.endGame();
		
		try {
			PlayerCD4 nextP = this.nextPlayer();
			if (players.indexOf(nextP) <= this.currentPlayerIndex) {
				// NB: endRound() calls Market if needed.
				this.endRound();
			}
			this.setCurrentPlayer(nextP);
		} catch (EndGameException e) {
			this.endGame();
		}
	}

	/**
	 * Return the next active and not excluded player
	 * (may be the same as in the previous turn)
	 * 
	 * @return {@link PlayerCD4} the next player
	 * @throws EndGameException
	 *             if only no players are still active and not excluded
	 */
	@Override
	public PlayerCD4 nextPlayer() throws EndGameException {
		PlayerCD4 p;
		for (int index = this.currentPlayerIndex + 1; index != this.currentPlayerIndex; index = (index + 1)
				% this.players.size()) {
			p = this.players.get(index % this.players.size());
			if (p.isActive() && !p.isExcluded())
				return p;
		}
		if(this.getCurrentPlayer().isActive() && !this.getCurrentPlayer().isExcluded())
			return this.getCurrentPlayer();
		throw new EndGameException();

	}

	/**
	 * Check if the current player can pass
	 * 
	 * @return boolean
	 */
	@Override
	public boolean canPass() {
		return this.getRemainingMainActions() < 1;
	}

	/**
	 * Checks if the current player has won
	 */
	@Override
	public boolean hasWon() {
		return this.getCurrentPlayer().getPlayerEmporiums().size() >=  this.numberOfEmporiumsToWin;
	}

	/**
	 * Gives the final points (according to game's rules) and determines the winner
	 * @return List<{@link PlayerCD4}> : the players' list sorted by victory points
	 * @see #giveFinalPointsNobility()
	 * @see #giveFinalPointsPermit()
	 */
	public List<PlayerCD4> whoWon() {
		
		if(!this.finalScore){
			this.giveFinalPointsNobility();
			this.giveFinalPointsPermit();
		}
		
		List<PlayerCD4> scoreBoard = new ArrayList<>();
		scoreBoard.addAll(this.getPlayers().stream().filter(p -> p.isActive()).collect(Collectors.toList()));
		
		this.sortByVictoryPoints(scoreBoard);
		if(scoreBoard.size()>1){
			// Checks for ex aequo
			PlayerCD4 p1 = scoreBoard.get(0);
			PlayerCD4 p2 = scoreBoard.get(1);
			if(p1.getPocket().getVictoryPoints().getSupply() ==  p2.getPocket().getVictoryPoints().getSupply()){
				/*
				 * Ex Aequo logic:
				 * 1. Sum assistants and politics cards
				 * 2. If still in ex aequo randomly choose the winner
				 */
				int extraBonus1 = p1.getPocket().getAssistants().getSupply() + p1.getPoliticsCards().size();
				int extraBonus2 = p2.getPocket().getAssistants().getSupply() + p2.getPoliticsCards().size();
				if(extraBonus1 == extraBonus2)
					(this.random.nextBoolean() ? p1 : p2 ).getPocket().addPocketable(new VictoryPoint(1));
				else
					((extraBonus1 > extraBonus2) ? p1 : p2 ).getPocket().addPocketable(new VictoryPoint(1));
				this.sortByVictoryPoints(scoreBoard);
			}
		}
		
		List<PlayerCD4> inactiveOnes = new ArrayList<>();
		inactiveOnes.addAll(this.getPlayers().stream().filter(p -> !p.isActive()).collect(Collectors.toList()));
		this.sortByVictoryPoints(inactiveOnes);
		scoreBoard.addAll(inactiveOnes);
		this.finalScore = true;
		return scoreBoard;	
	}

	/**
	 * Return the counter of rounds from the beginning of the match
	 * 
	 * @return int
	 */
	@Override
	public int getRoundNumber() {
		return this.roundNumber;
	}

	@Override
	public void endRound() throws EndGameException {
		// Increments the counter of round's number
		this.roundNumber++;
		if (lastRound)
			throw new EndGameException();
		else {
			{
				boolean flag = this.lastRound;
				this.lastRound = this.someoneWon;
				if(this.lastRound && !flag)
				{
					DisplayAction<String> change = new DisplayAction<>(this);
					change.setup(new DisplayInteraction<String>("Attention!The last round has just started"));
					this.getPlayers().stream().filter(p -> p.isActive()).forEach(p -> this.proxy.sendAction(new UnserializedVectorPacket<Action>(change, "Info", p.getUserID(), 0)));
				}	
			}
			if (this.hasMarket)
				this.market.openMarket();
			else
				this.beginTurn();
		}

	}

	/**
	 * Makes the players' order random
	 * 
	 * @param order
	 *            List<{@link PlayerCD4}> the list of players to be shuffled
	 */
	@Override
	public void setOrder(List<PlayerCD4> order) {
		Collections.shuffle(order, this.getRandom());
	}

	/**
	 * Return the current player
	 * 
	 * @return {@link PlayerCD4} the current player
	 */
	@Override
	public PlayerCD4 getCurrentPlayer() {
		return this.players.get(currentPlayerIndex);
	}

	private void setCurrentPlayer(PlayerCD4 player) {
		this.currentPlayerIndex = this.getPlayers().indexOf(player);
	}

	/**
	 * Method called when a game is finished. It sets the game as "not running"
	 * and after that it broadcasts the game status to all the players.
	 * 
	 * @see #broadCastGameStatus()
	 */
	public void endGame() {
		logger.log("The game is over.");
		this.running = false;
		this.broadCastGameStatus();
		System.out.println("We have a winner! GAME ENDED");
		logger.log("We have a winner! GAME ENDED");
		proxy.kill();
	}

	/**
	 * Gets the random object for the match
	 * @return {@link Random}
	 */
	public Random getRandom() {
		return this.random;
	}

	/**
	 * Adds a {@link PlayerCD4} to the match's players list
	 * 
	 * @param player
	 *            {@link PlayerCD4} the player to add
	 */
	@Override
	public void addPlayer(PlayerCD4 player) {
		this.players.add(player);
	}

	/**
	 * Checks if there are no more action in the stack
	 * @return boolean
	 */
	public boolean actionStackIsEmpty() {
		return this.actionsStack.isEmpty();
	}

	/**
	 * Adds an action to the stack
	 * @param a the {@link Action} to add
	 */
	public void pushAction(Action a) {
		this.actionsStack.add(a);
	}

	/**
	 * Gets an action from the stack
	 * @throws NullPointerException if the stack is empty
	 * @return the {@link Action} on top of the stack
	 */
	public Action popAction() {
		if (this.actionsStack.isEmpty())
			throw new NullPointerException("No Actions in the stack");
		else
			return this.actionsStack.removeLast();
	}

	/**
	 * Gets the action stack
	 * @return Deque<{@link Action}> the action stack
	 */
	public Deque<Action> getActionsStack() {
		return actionsStack;
	}

	/**
	 * Sets the action stack
	 * @param actionsStack Deque<{@link Action}> the action stack
	 */
	public void setActionsStack(Deque<Action> actionsStack) {
		this.actionsStack = actionsStack;
	}

	@Override
	public List<Action> getAvailableActions() {
		List<Action> temp = new ArrayList<>();
		if (this.currentPlayerMainActions > 0)
			temp.add(new ChooseMainActionAction(this));
		if (this.currentPlayerQuickActions > 0)
			temp.add(new ChooseQuickActionAction(this));
		if (this.currentPlayerMainActions < 1)
			temp.add(new PassAction<MatchCD4>(this));
		return temp;
	}

	@Override
	public Action getStartingAction() {
		return new ChooseActionTypeAction<MatchCD4>(this);
	}

	@Override
	public GameLogger getLogger() {
		return this.logger;
	}

	/**
	 * @return the market if exists, NULL if not
	 */
	public Market getMarket() {
		return market;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public synchronized Serializable getStatus(int id) throws ElementNotFoundException {
		PlayerCD4 requestingPlayer = null;
		for (PlayerCD4 p : this.getPlayers())
			if (p.getUserID() == id) {
				requestingPlayer = p;
				break;
			}
		if (requestingPlayer == null)
			throw new ElementNotFoundException();
		return new GameStatus(this, requestingPlayer);
	}
	
	/**
	 * Computes and gives the finals Victory Points (VP) related to the Nobility Track (NP)<br>
	 * According to game's rules:<br>
	 * <ul>
	 * <li>The player with more NP earns 5 VP</li>
	 * <li>The player closest to him earns 2 VP</li>
	 * <li>If more players are tied for the first NP place, they all wins 5 VP and no one earns 2 VP</li>
	 * <li>If more players are tied for the second NP place, they all earn 2 VP</li>
	 * </ul>
	 */
	private void giveFinalPointsNobility(){
		List<PlayerCD4> temp = new ArrayList<>();
		temp.addAll(this.getPlayers());
		
		Collections.sort(temp ,new Comparator<PlayerCD4>() {
			@Override
			public int compare(PlayerCD4 o1, PlayerCD4 o2) {
				int n1 = o1.getPocket().getNobilityRank().getSupply();
				int n2 = o2.getPocket().getNobilityRank().getSupply();
				if(n1==n2)
					return 0;
				return ( n1 < n2 ) ? -1 : 1;
			}
		}.reversed());

		// two greatest values
		int max1=temp.get(0).getPocket().getNobilityRank().getSupply();
		int max2=-1;
		for(int i=1;i<temp .size();i++){
			int newN = temp .get(i).getPocket().getNobilityRank().getSupply();
			if( newN != max1)
				max2 = newN;
			else
				break;
			if(max2!=-1)
				break;
		}

		for(int i=0; i<temp.size();i++){
			PlayerCD4 t = temp.get(i);
			int n = t.getPocket().getNobilityRank().getSupply();
			if(n==max1)
				t.getPocket().addPocketable(new VictoryPoint(5));
			else if(n==max2)
				t.getPocket().addPocketable(new VictoryPoint(2));
		}
	}
	
	/**
	 * Computes and gives the finals Victory Points (VP) related to Permit Cards (PC)<br>
	 * According to game's rules:<br>
	 * <ul>
	 * <li>The player with more PC earns 3 VP</li>
	 * <li>If more players are tied for the first PC place, they all wins 3 VP</li>
	 * </ul>
	 */
	private void giveFinalPointsPermit(){
		List<PlayerCD4> temp = new ArrayList<>();
		temp.addAll(this.getPlayers());
		Collections.sort(temp, new Comparator<PlayerCD4>() {
			@Override
			public int compare(PlayerCD4 o1, PlayerCD4 o2) {
				int n1 = o1.getPermitsToBeUsed().size();
				int n2 = o2.getPermitsToBeUsed().size();
				if(n1==n2)
					return 0;
				return ( n1 < n2 ) ? -1 : 1;	
			}
		}.reversed());
		
		int maxNPermit = temp.get(0).getPermitsToBeUsed().size();
		
		temp.forEach(new Consumer<PlayerCD4>(){
			@Override
			public void accept(PlayerCD4 t) {
				int n = t.getPermitsToBeUsed().size();
				if(n==maxNPermit)
					t.getPocket().addPocketable(new VictoryPoint(3));
			}
		});
	}

	/**
	 * Checks if the current round is the last
	 * @return boolean
	 */
	public boolean isLastRound() {
		return lastRound;
	}

	/**
	 * Checks if the match has a market
	 * @return boolean
	 */
	public boolean hasMarket() {
		return hasMarket;
	}
	
	private void sortByVictoryPoints(List<PlayerCD4> toSort){
		Collections.sort(toSort, new Comparator<PlayerCD4>() {
			@Override
			public int compare(PlayerCD4 o1, PlayerCD4 o2) {
				int n1 = o1.getPocket().getVictoryPoints().getSupply();
				int n2 = o2.getPocket().getVictoryPoints().getSupply();
				if(n1==n2)
					return 0;
				return ( n1 < n2 ) ? -1 : 1;	
			}
		}.reversed());
	}
}
