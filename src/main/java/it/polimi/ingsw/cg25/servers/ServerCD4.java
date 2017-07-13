package it.polimi.ingsw.cg25.servers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;

import it.polimi.ingsw.cg25.views.RMIView;
import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.communication.Packet;
import it.polimi.ingsw.cg25.communication.VectorPacket;
import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.gamegenerics.GameLogger;
import it.polimi.ingsw.cg25.model.BoardCD4;
import it.polimi.ingsw.cg25.onlinegenerics.User;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.views.SocketView;

/**
 * @author nicolo ;-)
 *
 */
public class ServerCD4 {

	/**
	 * The list of the active games
	 */
	private List<GameCD4> games = new ArrayList<>();
	/**
	 * The list of the games that are waiting to start
	 */
	private List<GameCD4> pendingGames = new ArrayList<>();
	/**
	 * The expiration dates of the pending games. At the i-th position
	 * of this array is stored the expirqation date of the i-th pendingGame
	 */
	private List<Date> expirationDates = new ArrayList<>();
	/**
	 * The thread used by the server to check if there are some pending games
	 * to be closed
	 */
	private ServerTimeChecker serverTimeChecker;
	/**
	 * The number of millisecond to wait before kicking a game from the pending list 
	 * if it can not start
	 */
	private final int gameSetUpTimeOut = 40000;
	/**
	 * Minimum number of users to start the next game
	 */
	private int minUsers = 2;
	/**
	 * Action timer for the next game
	 */
	private int actionTimer = 120000;
	/**
	 * Max number of user for the next game
	 */
	private int maxUsers = 4;
	/**
	 * The number of the port on which open the socket server
	 */
	private int socketPort;
	/**
	 * The number of the port on which open the RMI server
	 */
	private int rmiPort;
	/**
	 * The next id to be assigned to a view
	 */
	private int ids = 0;
	/**
	 * The executor that is encharged of execute the different threads of the server
	 */
	private ExecutorService executor;
	/**
	 * The socket antenna of this server
	 */
	private SocketServer socketServer;
	/**
	 * The rmi antenna of this server
	 */
	private RMIServer rmiServer;
	/**
	 * The output stream writer
	 */
	private PrintWriter writer;
	/**
	 * The logger on which the server logs exceptions
	 */
	private GameLogger logger;
	/**
	 * The queue of boards to be used for the new games to start
	 */
	private Queue<BoardCD4> boardPool;
	/**
	 * The thread that checks if there is any active game to be closed because
	 * it has ended
	 */
	private expiredGameChecker exipedGamesChecker;
	/**
	 * This flag is made to decide if the next game will have market or not
	 */
	private boolean hasMarket = true;

	/**
	 * The number of emporiums to win for the next game
	 */
	private int numberOfEmporiumsToWin = 10;
	/**
	 * 
	 * @author nicolo
	 *
	 */
	private class expiredGameChecker implements Runnable{

		/**
		 * This flags is made to know if the thread must go on working or not
		 */
		private boolean end = false;
		
		@Override
		public void run() {
		while(!end){	
			synchronized(games){
				List<GameCD4> toBeRemovedGames = new ArrayList<>();
				for(GameCD4 game : games){
					if(!game.isRunning()){
						toBeRemovedGames.add(game);
						game.close();
					}
				}
				if(toBeRemovedGames.size()!=0){
					writer.println(Integer.toString(toBeRemovedGames.size()) + " games have been removed from the list because it/they ended at least 5 min ago");
					writer.flush();
				}
				games.removeAll(toBeRemovedGames);
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				logger.log(e);
				Thread.currentThread().interrupt();
			}
			}
		
		}
		
		/**
		 * Kills this timechecker
		 */
		public void kill(){
			this.end = true;
		}
	}
	
	/**
	 * This class populates the board queue of some new boards
	 * 
	 * @author nicolo
	 *
	 */
	private class BoardPoolPopulator implements Runnable {

		/**
		 * Stores the information relating how many boards this thread must create
		 */
		private int numberOfBoardsToAdd;
		/**
		 * counts the number of errors encountered while instantiating the boards
		 */
		private int errors;
		private int i;
		/**
		 * The path where the configuration files are stored
		 */
		private String path = "src/main/resources/configurations/";
		
		/**
		 * Simple constructor
		 * @param numberOfBoardsToAdd is the number of boards that must be added to the board queue
		 */
		private BoardPoolPopulator(int numberOfBoardsToAdd){
			this.numberOfBoardsToAdd = numberOfBoardsToAdd;
		}
		
		@Override
		public void run() {
			for(int i=0;i<numberOfBoardsToAdd;i++){
				try {
					boardPool.add(generateRandomBoard());
				} catch (Exception e) {
					System.out.println(e.getMessage());
					if(errors<5)
						numberOfBoardsToAdd++;
					else{
						writer.println("Game initialization failed too many times. We suggest you to shut down the server and check the files");
						writer.flush();
					}
				}
			}
			
		}
		
		/**
		 * Generates a Random Board by submitting to the parser a consistent and partially random
		 * set of files
		 * @return a valid board
 		 * @throws Exception if the tool is not able to combine the files in a correct way
		 */
		public BoardCD4 generateRandomBoard() throws Exception{
			String[] fileFilters = {"nobilityCells","politics","cities","graph","king","regions"};
			ArrayList<String> selected = new ArrayList<>();
			for(i = 0; i<fileFilters.length; i++) {
				String[] files = new File(path).list(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						if(i>=3 && !selected.get(2).replace("cities", "").equals(name.replace(fileFilters[i], "")))
							return false;
						if(!name.contains(fileFilters[i]) || !".txt".equals(name.substring(name.lastIndexOf("."))))
						{
							return false;
						}
						else
							return true;
					}
					
				});
				Random random = new Random(System.nanoTime());
				selected.add(files[Math.abs(random.nextInt(files.length))]);
			}
			try {
				BoardFactory factory = new BoardFactory(new FileReader(path.concat(selected.get(0))),
						new FileReader(path.concat(selected.get(1))),
						new FileReader(path.concat(selected.get(2))),
						new FileReader(path.concat(selected.get(3))),
						new FileReader(path.concat(selected.get(4))),
						new FileReader(path.concat(selected.get(5))));
				writer.println("A board has been added to the pool using the files " + selected);
				writer.flush();
				return factory.getBoard();
			} catch (FileNotFoundException | CannotCreateGameException e) {
				writer.println("A problem occured with one of the configuration files in the directory");
				writer.flush();
				throw(e);
			}
		
		}
	}
	
	/**
	 * Launches a game of the pending game list
	 * @param index the index in the list of pending games of the game to be launched
	 * @throws IllegalArgumetnException if the index is out of the list or the game can not be launched
	 */
	public synchronized void launchGame(int index){
		GameCD4 game;
		try{
			game = pendingGames.get(index);
		}catch(IndexOutOfBoundsException e){
			throw new IllegalArgumentException(e);
		}
		if(!game.canStart()) 
			throw new IllegalArgumentException("The game cannot be started");
		System.out.println("Launching a game...");
		logger.log("Launching a game...");
		games.add(game);			
		pendingGames.remove(index);
		expirationDates.remove(index);
		Thread t = new Thread(game);
		t.setName("Game#"+ids);
		t.start();
	}
	
	/**
	 * This is a routine needed to remove a game from the list of the pending games
	 * @param index
	 */
	public synchronized void removePendingGame(int index){
		pendingGames.get(index).close();
		pendingGames.remove(index);
		expirationDates.remove(index);
		System.out.println("The game have been removed from the pending game list because of timeout end");
	}
	
	/**
	 * This method is called to start one of the pending games
	 * @param game is the game to be started
	 * @throws IllegalArgumentException if the game is not in the list or can not be started
	 */
	public void launchGame(GameCD4 game){
		launchGame(pendingGames.indexOf(game));
	}
	
	/**
	 * This method connects an Rmi view to the firstly available game running in the server
	 * @param view the view that asked to be connected
	 * @return the real view that has to be sent to the client to be used as a stub
	 * @throws RemoteException when something goes wrong with RMI
	 */
	public synchronized RMIView<Packet<Interaction>> connect(RMIView<Packet<Interaction>> view) throws RemoteException {
		this.ids ++;
		User user = new User(this.ids,"User#".concat(Integer.toString(this.ids)));
		user.setStatus(true);
		if(pendingGames.isEmpty()) 
		{
			initGameList();
		}
		if(pendingGames.isEmpty())
			return null;
		GameCD4 game = pendingGames.get(0);
		game.addUser(user);
		if(game.canStart() && expirationDates.get(0).after(new Date(System.currentTimeMillis()+20000)))
		{
			Date d = expirationDates.get(0);
			expirationDates.add(0,new Date(System.currentTimeMillis()+20000));
			expirationDates.remove(d);
			writer.println("A game has reached the minimum number of players to start. Launching in 20 sec");
			writer.flush();
		}
		RMIView<Packet<Interaction>> newView = new RMIView<>(view,this.ids);
		newView.getObservableRmi().attachObserver(game.getViewProxy());
		game.getViewProxy().attachObserver(newView);
		try{
			if(game.isFull())
				launchGame(game);
		}
		catch(Exception e){
			writer.println("Unable to start a GameCD4 "+e.getMessage());
			writer.flush();
			logger.log(e,"Unable to start a GameCD4");
		}
		return newView;
	}

	/**
	 * This method connects Socket views to the first game available running in the server
	 * @param socketView is the SocketView which encapsulated the socket resulting from the 
	 * server socket's accepting routine
	 */
	public synchronized void connect(SocketView<VectorPacket<Interaction>,Packet<Interaction>> socketView){
		this.ids ++;
		User user = new User(this.ids,"User#".concat(Integer.toString(this.ids)));
		user.setStatus(true);
		if(pendingGames.isEmpty())
		{
			initGameList();
		}
		if(pendingGames.isEmpty())
			return;
		GameCD4 game = pendingGames.get(0);
		game.addUser(user);
		if(game.canStart() && expirationDates.get(0).after(new Date(System.currentTimeMillis()+20000)))
		{
			Date d = expirationDates.get(0);
			expirationDates.add(0,new Date(System.currentTimeMillis()+20000));
			expirationDates.remove(d);
			writer.println("A game has reached the minimum number of players to start. Launching in 20 sec");
			writer.flush();
		}
		SocketView<VectorPacket<Interaction>,Packet<Interaction>> newView = new SocketView<>(socketView,this.ids);
		game.getViewProxy().attachObserver(newView);
		newView.attachObserver(game.getViewProxy());
		newView.run(user.getName());
		try{
			if(game.isFull())
				launchGame(game);
		}
		catch(Exception e){
			writer.println("Unable to start a GameCD4 "+e.getMessage());
			writer.flush();
			logger.log(e,"Unable to start a GameCD4");
		}
	}
	
	/**
	 * This method starts a new Game List 
	 */
	private void initGameList(){
		try {
			if(this.boardPool.isEmpty()){
				writer.println("We are failing in building the game boards so the match cannot be initialized");
				writer.flush();
				return;
			}
			pendingGames.add(new GameCD4(minUsers,maxUsers,actionTimer,this.boardPool.poll(),hasMarket,numberOfEmporiumsToWin));
			Thread t = new Thread(new BoardPoolPopulator(1));
			t.run();
		} catch (FileNotFoundException e) {
			logger.log(e, "Unable to build a new GameCD4");
		} catch (CannotCreateGameException c){
			logger.log(c, "Game creation failed");
		}
		expirationDates.add(new Date(System.currentTimeMillis()+ gameSetUpTimeOut));
		serverTimeChecker.kill();
		serverTimeChecker = new ServerTimeChecker(expirationDates,this);
		Thread t = new Thread(serverTimeChecker);
		t.setName("Timer");
		t.start();
	}
	
	/**
	 * This routine starts the server asking the admin for the connection parameters.
	 * Then stucks on waiting for commands from the System.input 
	 * @throws IOException when there are problems with scanners and printers
	 */
	public void startServer() throws IOException{
		//lancio i tre thread in ascolto, uno su command line uno su socket e uno su rmi
		//lancia l'esecuzione del thread che periodicamente checka se qualche match va avviatp
		boolean end = false;
		boolean retry = true;
		this.boardPool = new LinkedList<>();
		writer = new PrintWriter(System.out);
		BoardPoolPopulator t = new BoardPoolPopulator(10);
		t.run();
		this.exipedGamesChecker = new expiredGameChecker();
		Thread checker = new Thread(this.exipedGamesChecker);
		checker.setName("GameStatusChecker");
		checker.start();
		serverTimeChecker = new ServerTimeChecker(expirationDates,this);
		Scanner scan = new Scanner(System.in);
		while(retry){
			writer.println("Insert Socket port number and RMI port. Separate with comma.");
			writer.flush();
			String in = scan.nextLine();
			try{
				String []ports = in.split(",");
				socketPort = Integer.parseInt(ports[0]);
				if(socketPort > Math.pow(2, 16))
					throw new NumberFormatException("Port number out of range");
				rmiPort = Integer.parseInt(ports[1]);
				if(rmiPort > Math.pow(2, 16))
					throw new NumberFormatException("Port number out of range");
				if(socketPort == rmiPort)
					throw new NumberFormatException("Rmi and Socket must listen on different ports");
				writer.flush();
				retry = false;
			}catch(NumberFormatException|IndexOutOfBoundsException e){
				retry = true;
				writer.println("Error. Retry.");
				writer.flush();
				logger.log(e,"Unable to read port number input");
			}
		}
		executor = Executors.newCachedThreadPool();
		socketServer = new SocketServer(socketPort,logger,this);
		try {
			rmiServer = new RMIServer(rmiPort,logger,this);
			executor.submit(rmiServer);
		} catch (RemoteException e) {
			writer.println("Couldn't start RMI server.");
			writer.flush();
			logger.log(e,"Unable to start RMI server");
		}
		executor.submit(socketServer);
		
		writer.println("Server is now running, waiting for incoming connection");
		writer.println("Type '?' for help");
		writer.flush();
		
		/*
		 * Our servers has a command line console in which we can type some simple commands
		 * if the command typed is quit the command line closes herself and
		 */
		while(!end){
			String line = scan.nextLine();
			if(line.equals("quit"))
				end = true;
			else if(line.equals("quit and kill"))
			{
				end = true;
				for(GameCD4 game : pendingGames)
					game.close();
				for(GameCD4 game : games)
					game.close();
			}
			else
			{
				line = line.replaceAll(" ","").toLowerCase();
				this.process(line);
			}
		}
		//When the server is closed no more threads are launched
		socketServer.kill();
		rmiServer.kill();
		exipedGamesChecker.kill();
		executor.shutdownNow();
		scan.close();
		writer.println("Server shutdown completed");
		writer.flush();
		logger.log("Server shutdown completed");
		logger.close();
	}
	
	/**
	 * Simply the main of the server application
	 * @param args are not used
	 * @throws IOException when the server starting routine fails due to some issuse with IO
	 */
	public static void main(String[] args) throws IOException{
		System.out.println("The server has been turned on. Wait for service initialization.");
		ServerCD4 server = new ServerCD4();
		server.logger = new GameLogger("ServerLog", GameLogger.DEFAULT_LOG_PATH);
		server.logger.attachHandler(new ConsoleHandler(), GameLogger.MIN_EX_LEVEL);
		server.logger.log("The server has been turned on. Wait for service initialization.");
		server.startServer();
	}
	
	/**
	 * This is a stupid command parser method
	 * @param command is the command we want to execute
	 */
	private void process(String command) {
		Date date = new Date();
		/*
		 * The command active games number asks is given to know the number of games that are currently played
		 */
		if(command.equals("activegamesnumber"))
			System.out.println(date + " At the moment " + games.size() + " games are running.");
		/*
		 * The command pending games number asks is given to know the number of games that are currently waiting to start
		 */
		else if(command.equals("pendinggamesnumber"))
			System.out.println(date + " At the moment " + pendingGames.size() + " games are waiting to start.");
		/*
		 * This command sets the min number of players to the specified one
		 */
		else if(command.contains("setminnumber:"))
		{
			boolean force = command.startsWith("!");
			try{
				int i = Integer.parseInt(command.split(":")[1]);
				if(i<0)
				{
					writer.println("[FAILURE]should be positive");
					writer.flush();
					return;
				}
				if(i>maxUsers && !force)
				{
					writer.println("[WARNING] You are trying to set min number higher than max number.\n"
							+ "If you are convinced of what you are doing restate the command starting it with '!'");
					writer.flush();
				}
				else{
					System.out.println(date + " updated min number of player to " + i);
					minUsers = i;
				}
			}catch(NumberFormatException e){
				System.out.println("Number format incorrect");
			}
		}
		/*
		 * This command sets the max number of players to the specified one
		 */
		else if(command.contains("setmaxnumber:"))
		{
				boolean force = command.startsWith("!");
				try{
					int i = Integer.parseInt(command.split(":")[1]);
					if(i<0)
					{
						writer.println("[FAILURE]should be positive");
						writer.flush();
						return;
					}
					if(i<minUsers && !force)
					{
						writer.println("[WARNING] You are trying to set max number lower than min number.\n"
								+ "If you are convinced of what you are doing restate the command starting it with '!'");
						writer.flush();
					}
					else{
						System.out.println(date + " updated max number of player to " + i);
						maxUsers = i;
				}
			}catch(NumberFormatException e){
				System.out.println("Number format incorrect");
			}
		}
		else if(command.contains("emporiumstowin:"))
		{
				try{
					int i = Integer.parseInt(command.split(":")[1]);
					if(i<0)
					{
						writer.println("[FAILURE]should be positive");
						writer.flush();
						return;
					}
					this.numberOfEmporiumsToWin = i;
					System.out.println(date + " updated number of emporiums to win to " + i);
				}
			catch(NumberFormatException e){
				System.out.println("Number format incorrect");
			}
		}
		/*
		 * Set the max time an user can take toperform an action before being considered inactive
		 * to be set to a value lower than 20 s you need to force it adding the ! prefix
		 */
		else if(command.contains("setactiontimeout:"))
		{
			boolean force = command.startsWith("!");
			try{
				int i = Integer.parseInt(command.split(":")[1]);
				if(i<0)
				{
					writer.println("[FAILURE]should be positive");
					writer.flush();
					return;
				}
				if(i<20 && !force)
				{
					writer.println("[WARNING] if the action timeout is too short the game may result not playable.\n"
							+ "If you are convinced of what you are doing restate the command starting it with '!'");
					writer.flush();
				}
				else	
				{
					actionTimer = i*1000;
					System.out.println(date + " updated max action timeout to " + i + " [s]");
				}
			}catch(NumberFormatException e){
				System.out.println("Number format incorrect");
			}
		}
		/*
		 * This command enables the use of the market
		 */
		else if(command.contains("enablemarket"))
		{
			this.hasMarket = true;
			writer.println(date +" Market enabled for new games");
			writer.flush();
		}
		/*
		 * This command disables the use of the market
		 */
		else if(command.contains("disablemarket"))
		{
			this.hasMarket = false;
			writer.println(date+" Market disables for new games");
			writer.flush();
		}
		else if("?".equals(command)){
			writer.println("Here's a list of some useful server commands");
			writer.println("(if you are in a hurry you can write them with no spaces)");
			writer.println("");
			writer.println("\tactive games number\t\tget the number of running active games");
			writer.println("\tpending games number\t\tget the number of penging games");
			writer.println("\tset min number:<integer>\tset the minimum number of player per game");
			writer.println("\tset max number:<integer>\tset the maximum number of player per game");
			writer.println("\temporiums to win:<integer>\tset the number of emporium to build to end the game");
			writer.println("\tset action timeout:<integer>\tset the action timeout timer");
			writer.println("\tenable market\t\t\tenable the use of the Market for new games");
			writer.println("\tdisable market\t\t\tdisable the use of the Market for new games");
			writer.println("\tquit\t\t\t\tserver shutdown (without killing active and pending games)");
			writer.println("\tquit and kill\t\t\tserver shutdown (with no mercy. This is not very kind if there are active games)");
			writer.flush();
		}
		else{
			writer.println(date + " Command not recognised.");
			writer.flush();
		}
	}

}


