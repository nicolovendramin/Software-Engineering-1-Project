/**
 * 
 */
package it.polimi.ingsw.cg25.servers;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import it.polimi.ingsw.cg25.actions.DisplayInteraction;
import it.polimi.ingsw.cg25.controller.ActionController;
import it.polimi.ingsw.cg25.controller.ServiceController;
import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.exceptions.CannotPassException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.gamegenerics.GameLogger;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.BoardCD4;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.dashboard.Emporium;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.onlinegenerics.OnlineApplication;
import it.polimi.ingsw.cg25.onlinegenerics.User;
import it.polimi.ingsw.cg25.proxies.ModelProxy;
import it.polimi.ingsw.cg25.proxies.ViewProxy;

/**
 * @author Davide
 * @author nicolo
 *
 */
public class GameCD4 implements Runnable, OnlineApplication {

	/**
	 * The match which is the applet of this game
	 */
	private MatchCD4 match;
	/**
	 * The max number of players that can be accepted in the game
	 */
	private final int maxLenght;
	/**
	 * The min number of active players needed to start the game
	 */
	private final int minLenght;
	/**
	 * The view proxy used by the 2 controllers of the game
	 */
	private final ViewProxy viewProxy;
	/**
	 * The actionController of the match
	 */
	private final ActionController actionController;
	/**
	 * The list of the users that have joined the game
	 */
	private List<User> users;
	/**
	 * The service controller used to dispatch and to receive services to be applied
	 * on this game
	 */
	private final ServiceController serviceController;
	/**
	 * If not null represents the moment in which the game will be considered closed
	 */
	private Date endingTimeStamp = null;

	/**
	 * The constructor of the Game
	 * 
	 * @param min
	 *            is the minimum number of player needed to start the game
	 * @param max
	 *            is the maximum number of players that can join the game
	 * @param actionTimeOut
	 *            is the maximum time the user has to reply to an Action Query
	 * @throws FileNotFoundException
	 * @throws CannotCreateGameException
	 */
	public GameCD4(int min, int max, int actionTimeOut,BoardCD4 board,boolean hasMarket,int numberOfEmporiumsToWin) throws FileNotFoundException, CannotCreateGameException {
		this.users = new ArrayList<>();
		this.maxLenght = max;
		this.minLenght = min;
		this.serviceController = new ServiceController(this);
		this.actionController = new ActionController(actionTimeOut);
		this.viewProxy = new ViewProxy(this.actionController, this.serviceController);
		this.actionController.setViewProxy(this.viewProxy);
		this.serviceController.setProxy(viewProxy);
		ModelProxy temp = new ModelProxy();
		temp.attachObserver(actionController);
		actionController.attachObserver(temp);
		GameLogger logger = new GameLogger("MatchLog", GameLogger.DEFAULT_LOG_PATH);
		this.match = new MatchCD4(board, temp, hasMarket,numberOfEmporiumsToWin,logger);
	}

	/**
	 * Disconnects the indicated user from the game, Put it as inactive also in
	 * the players' list of the match and, if it was the current player it makes
	 * the turns go ahead
	 */
	@Override
	public synchronized void disconnect(int id, String report) {
		// We should log the report maybe?
		for (User u : users)
			if (u.getUserID() == id) {
				u.setStatus(false);
			}

		if (match.isRunning()) {
			for (PlayerCD4 p : match.getPlayers())
				if (p.getUserID() == id) {
					p.setStatus(false);
					
					if(match.hasMarket() && match.getMarket().isOpen()){
						// Here we are during the market
						if(p == match.getMarket().getCurrentPlayer()){
							try {
								match.getMarket().endTurn();
								match.getMarket().beginTurn();
							} catch (CannotPassException e) {
								match.getLogger().log(e, "Passing because current player is inactive (during market)");
							}
						}
					}else{
						// Here we are during the match
						if(p == match.getCurrentPlayer()){
							// Current player becomes inactive
							try {
								match.endTurn();
								match.beginTurn();
							} catch (CannotPassException e) {
								match.getLogger().log(e, "Passing because current player is inactive");
							}
						}
					}
				}
		}
		notifyAll();
	}

	@Override
	public void chat(String message) {
		//in our specific application the chat doesn't record the messages on the server
	}

	/**
	 * This function gives back the Id of the view associated with the specified
	 * userName or throws IllegalArgumentException if not found
	 */
	@Override
	public int nameService(String name) {
		for (User p : this.users) {
			if (p.getName().equals(name))
				return p.getUserID();
		}
		throw new IllegalArgumentException("I cannot associate this name with an user of this game");
	}

	/**
	 * This method returns the username associated with the given id or throws
	 * IllegalArgumentException if the id is not known
	 */
	@Override
	public String addressBook(int id) {
		for (User p : this.users)
			if (p.getUserID() == id)
				return p.getName();
		throw new IllegalArgumentException("I don't know the name of the user you are referring to sorry");
	}

	/**
	 * This method allows to a user identified by a certain id to change its
	 * username in case the match has not started yet the name will be changed
	 * in the players' list too, otherwise it will also be changed at
	 * OnlineApplicationLevel
	 */
	@Override
	public synchronized void changeUsername(int id, String newName) {
		String name = newName.replaceAll(" ", "");
		if (name == null || name.length() == 0)
			throw new IllegalArgumentException("You must specify a name");
		for (User p : this.users)
			if (p.getName().equals(name)) {
				notifyAll();
				throw new IllegalArgumentException("The username is already used by another player of your same match");
			}
		for (User p : this.users)
			if (p.getUserID() == id) {
				p.rename(name);
				notifyAll();
				return;
			}
		notifyAll();
		throw new IllegalArgumentException("I cannot recognise your identity sorry");
	}

	/**
	 * A user can ask to be reconnected
	 */
	@Override
	public synchronized void connect(int id) {
		for (User u : users)
			if (u.getUserID() == id) {
				if (u.isActive())
					throw new IllegalArgumentException("You are already connected to the game");
				u.setStatus(true);
			}
		if (match.isRunning()) {
			for (PlayerCD4 p : match.getPlayers())
				if (p.getUserID() == id)
					p.setStatus(true);
		}
		notifyAll();
	}

	/**
	 * Default implementation. Launches the game with 20 sec of waiting before
	 * the start
	 */
	@Override
	public void run() {
		this.run(20000);
	}

	/**
	 * This method closes the game and asks the view Proxy to communicate the end of the game to 
	 * all the connected users 
	 */
	public void close() {
		this.viewProxy.send(new DisplayInteraction<String>("quitThe game has been closed."), "Quit", 0);
	}

	/**
	 * When the game is executed the game is instantiated following all the
	 * rules
	 * @throws InterruptedException 
	 */
	public void run(int time) {
		/*
		 * Advertises to all the players that the match is going to start and
		 * that they won't have the chance to change their player nickname after
		 * the game has been launched.
		 */
		this.viewProxy.send(
				new DisplayInteraction<String>("Get Ready! The game is going to start in 20 seconds\n"
						+ "Please if you want to change you Player nickname do it now\n"
						+ "You will still be able to change your chat username after the game have been launched"),
				"Info");
		try {
			Thread.sleep(time);
		} catch (InterruptedException e1) {
			match.getLogger().log(e1,"The thread has been interrupted.");
			Thread.currentThread().interrupt();
		}
		List<HSBColor> colors = HSBColor.getNDifferent(this.users.size());
		for (int i = 0; i < this.users.size(); i++) {
			PlayerCD4 temp = new PlayerCD4(users.get(i).getUserID(), users.get(i).getName(), colors.get(i), this.match,
					new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0)));
			temp.setStatus(true);
			this.match.addPlayer(temp);
		}
		match.setOrder(match.getPlayers());
		for (int i = 0; i < this.users.size(); i++) {
			match.getPlayers().get(i).getPocket().addPocketable(new Coin(10 + i));
			match.getPlayers().get(i).getPocket().addPocketable(new Assistant(1 + i));
			for (int j = 0; j < 6; j++) {
				try {
					match.getPlayers().get(i).getPoliticsCards().add(match.getBoard().getPoliticsDeck().drawCard());
				} catch (NoCardsException e) {
					match.getLogger().log(e, "Not enough cards for all players!");
				}
			}
		}

		/*
		 * This is the routine for the short games. It is runned only if the
		 * number of player in the game is limited to 2 people to fill the map
		 * with some emporiums to make things more interesting
		 */
		if (this.users.size() <= 2) {
			Random random = new Random();
			int bound = Math.abs(random.nextInt(6))+3;
			List<HSBColor> col = HSBColor.getNDifferent(4);
			List<PlayerCD4> players = new ArrayList<>();
			PlayerCD4 p1 = new PlayerCD4(0, "NullPlayer", col.get(2), this.match,
					new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0)));
			PlayerCD4 p2 = new PlayerCD4(0, "NullPlayer", col.get(3), this.match,
					new PocketCD4(new Coin(0), new Assistant(0), new NobilityRank(0), new VictoryPoint(0)));
			players.add(p1);
			players.add(p2);
			for (int j = 0; j < bound; j++) {
				int ind = Math.abs(random.nextInt());
				City c = this.match.getBoard().getCities().get(ind % match.getBoard().getCities().size());
				System.out.println("Places an emporium of the color " + players.get(ind % 2).getColor().printTag()
						+ " in " + c.getName());
				c.addEmporium(new Emporium(players.get(ind % 2), c));
			}
		}
		match.beginTurn();
	}

	/**
	 * @return the match
	 */
	public MatchCD4 getMatch() {
		return match;
	}

	/**
	 * @param match
	 *            the match to set
	 */
	public void setMatch(MatchCD4 match) {
		this.match = match;
	}

	/**
	 * @return the viewProxy
	 */
	public ViewProxy getViewProxy() {
		return viewProxy;
	}

	/**
	 * @return the actionController
	 */
	public ActionController getActionController() {
		return actionController;
	}

	/**
	 * This methof is meant to check if the game is full
	 * 
	 * @return true if it is full false otherwise
	 */
	public boolean isFull() {
		return this.users.size() >= maxLenght;
	}

	/**
	 * this method is meant to check if the game can start
	 * 
	 * @return True if can start false if not
	 */
	public boolean canStart() {
		return this.users.stream().filter(u -> u.isActive()).count() >= minLenght;
	}

	@Override
	public synchronized void addUser(User e) {
		if (e == null)
			throw new NullPointerException();
		this.users.add(e);
		notifyAll();
	}

	@Override
	public boolean isRunning() {
		if(!match.isRunning() && this.endingTimeStamp == null)
			this.endingTimeStamp = new Date(System.currentTimeMillis()+300000);
		return (match.isRunning() || this.endingTimeStamp.after(new Date()));
	}

	@Override
	public Serializable getStatus(int id) throws ElementNotFoundException {
		return match.getStatus(id);
	}
}
