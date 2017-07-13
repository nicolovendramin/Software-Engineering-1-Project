package it.polimi.ingsw.cg25.clients;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import it.polimi.ingsw.cg25.views.RMIView;
import it.polimi.ingsw.cg25.actions.DisplayInteraction;
import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.communication.VectorPacket;
import it.polimi.ingsw.cg25.gamegenerics.GameLogger;
import it.polimi.ingsw.cg25.observer.Observable;
import it.polimi.ingsw.cg25.observer.Observer;
import it.polimi.ingsw.cg25.servers.RMIServerInterface;
import it.polimi.ingsw.cg25.views.RMIViewRemote;
import it.polimi.ingsw.cg25.views.SocketView;
/**
 * 
 * @author nicolo
 *
 */
public class CliSimpleClient extends Observable<VectorPacket<Interaction>> implements Observer<VectorPacket<Interaction>> {
	
	/**
	 * Reprents,if not null, the action interaction which the user is currently filling
	 */
	private Interaction actionInteraction;
	/**
	 * The writer for the ouput
	 */
	private PrintWriter writer;
	/**
	 * The logger of the cli interface
	 */
	private GameLogger logger;
	/**
	 * A state variable to indicate if the user is still 
	 */
	private boolean end = false;
	
	
	/**
	 * The main which starts the client and, after that the process is
	 * closed advertises the event to its views
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String []args) throws IOException, InterruptedException {
		CliSimpleClient client = new CliSimpleClient();
		client.start();
		Interaction finish = new DisplayInteraction<String>("quit");
		client.notifyObservers(new VectorPacket<Interaction>(finish,"Quit",0,0));
		client.removeAllObservers();
	}
	
	/**
	 * This routine starts the client asking him which kind of connection he prefers and other connection datas.
	 * The user can choose between Socket or RMI
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void start() throws IOException, InterruptedException {
		//The CliClient
		//is a client in which you have to choose how to connect to the server
		//rmi or socket connection are available
		//and after this you just send the Interactions you receive on the right canal
		Scanner input = new Scanner(System.in);
		this.writer = new PrintWriter(System.out);
		this.logger = new GameLogger("CLISimpleClientLogger", GameLogger.DEFAULT_LOG_PATH);
		this.logger.log("CLI Simple Client started");
		ExecutorService executor = Executors.newCachedThreadPool();
		end = false;
		writer.println("******************* Command Line Interface *******************\n");
		while(!end) {
			writer.println("Choose connection type, write \"Socket\" or \"Rmi\". Type \'?\' for help");
			writer.flush();
			String conTypeInit = input.nextLine();
			//Lower case
			String conType = conTypeInit.toLowerCase().replaceAll(" ","");
			if("socket".equals(conType)) {
				end = socketStart(executor,input);
			}
			else if("rmi".equals(conType)) {
				end = rmiStart(executor,input);
			}
			else if("?".equals(conType)) {
				writer.println("You will be kindly requested to insert in the order:");
				writer.println("\t1) the connection type, Socket or RMI");
				writer.println("\t2) the IP server address");
				writer.println("\t3) the number of the server welcome socket\n");
				writer.flush();
			}
			else {
				writer.println("Command not recognised!\n");
				writer.flush();
			}
		}
		//Qui è già avvenuta l'inizializzazione
		while(end) {
			String command = input.nextLine();
			if(!end)
				;
			else if("quit".equals(command))
			{
				Interaction finish = new DisplayInteraction<String>("quit");
				this.notifyObservers(new VectorPacket<Interaction>(finish,"Quit",0,0));
				end = false;
			}
			else
				//Parsa il comando post-inizializzazione
				this.process(command);
		}
		writer.println("It was nice while it lasted.");
		writer.flush();
		this.logger.close();
		return;
	}
	
	/**
	 * The starting procedure for RMIconnection
	 * @param executor is the executor that runs the rmi routine
	 * @param is the scanner of the input line
	 * @return whether the connection was successfull or not
	 * @throws RemoteException if the specified port or ip is wrong
	 */
	private boolean rmiStart(ExecutorService executor,Scanner input) throws RemoteException{
		RMIView<VectorPacket<Interaction>> view = new RMIView<>(0);
		writer.println("Insert the IP address of the server (in dotted decimal notation):");
		writer.flush();
		String ip = input.nextLine();
		writer.println("Insert the number of the port of the welcome socket:");
		writer.flush();
		try{
			int port = Integer.parseInt(input.nextLine());
			Registry registry = LocateRegistry.getRegistry(ip, port);
			RMIServerInterface server = (RMIServerInterface)registry.lookup("CD4");
			RMIViewRemote twin = server.connectToRmi(view);
			view.attachTwin(twin);
			this.attachObserver(view);
			view.getObservableRmi().attachObserver(this);
			writer.println("Connection accepted. Type \'?\' for help");
			writer.flush();
		}
		catch(Exception e){
			logger.log(e, "Conncection to RMI server is failed");
			writer.println("Connection failed, please check your inputs and retry.");
			writer.println(e.getMessage());
			writer.flush();
			return false;
		}
		return true;
		
	}
	
	/**
	 * The starting procedure for socket connection
	 * @param executor is the executor that runs the socket routine
	 * @param is the scanner of the input line
	 * @return whether the connection was successfull or not
	 */
	private boolean socketStart(ExecutorService executor, Scanner input) {
		writer.println("Insert the IP address of the server (in dotted decimal notation):");
		writer.flush();
		String ip = input.nextLine();
		writer.println("Insert the number of the port of the welcome socket:");
		writer.flush();
		try{
			SocketView<VectorPacket<Interaction>,VectorPacket<Interaction>> view;
			Socket socket;
			int port = Integer.parseInt(input.nextLine());
			socket = new Socket(ip,port);
			view = new SocketView<>(socket,0);
			view.attachObserver(this);
			this.attachObserver(view);
			view.run("RemoteInput");
			writer.println("Connection accepted. Type \'?\' for help");
			writer.flush();
			return true;
			}
		catch(NumberFormatException e){
			writer.println("You made some mistakes compiling the required fields. Try again from scratch!");
			writer.flush();
			this.logger.log(e,"You made some mistakes compiling the required fields. Try again from scratch!");
			return false;
		}
		catch(UnknownHostException e){
			writer.println("I don't know the specified host sorry.");
			writer.flush();
			this.logger.log(e, "I don't know the specified host sorry.");
			return false;
		}
		catch(IOException e){
			writer.println("Uncorrect host port\n");
			writer.flush();
			this.logger.log(e, "Uncorrect host port");
			return false;
		}
		catch(IllegalArgumentException e){
			writer.println("You made some mistakes compiling the required fields. Try again from scratch!");
			writer.flush();
			this.logger.log(e,"You made some mistakes compiling the required fields. Try again from scratch!");
			return false;
		}
	}
	
	/**
	 * This method is meant to process the command received as parameter, to parse
	 * it and to perform the corresponding action
	 * @param command is the String description of the command
	 */
	public void process(String command){
		//Here we should parse the input and send t
		if(command.startsWith("[Action]"))
		{
			try {
				if(this.actionInteraction == null)
				{
					writer.println("You are not currently asked to perform actions!");
					writer.flush();
				}
			Interaction temp = this.actionInteraction;
			if(command.contains("abort")){
				temp = new DisplayInteraction<String>("[[Abort]]","[[Abort]]");
			}
			else
			{
				this.actionInteraction.registerReply(command.replaceFirst("\\[Action\\]", ""));
			}
			this.notifyObservers(new VectorPacket<Interaction>(temp,"Action",0,0));
			if(temp==this.actionInteraction)
				actionInteraction = null;
			}
			catch(Exception e){
				writer.println(e.getMessage());
				writer.flush();
				this.logger.log(e, "Erorr in CLI.process()");
			}
		}
		else if(command.startsWith("[Chat]")) {
			Interaction inter = new DisplayInteraction<String>(command.replaceFirst("\\[Chat\\]", ""));
			this.notifyObservers(new VectorPacket<Interaction>(inter,"Chat",0,0));
		}
		else if(command.startsWith("[Pm]"))
		{
			String fixed = command.replaceFirst("\\[Pm\\]", "");
			Interaction inter = new DisplayInteraction<String>(fixed);
			this.notifyObservers(new VectorPacket<Interaction>(inter,"Pm",0,0));
		}
		else if(command.startsWith("[Refresh]"))
		{
			String fixed = command.replaceFirst("\\[Refresh\\]", "");
			Interaction inter = new DisplayInteraction<String>(fixed);
			this.notifyObservers(new VectorPacket<Interaction>(inter,"Status",0,0));
		}
		else if(command.startsWith("[SetUsername]")) {
			String newUserName = command.replaceFirst("\\[SetUsername\\]","");
			Interaction inter = new DisplayInteraction<String>(newUserName);
			this.notifyObservers(new VectorPacket<Interaction>(inter,"SetUsername",0,0));
		}
		else if(command.startsWith("[Disconnect]")) {
			String report = command.replaceFirst("\\[Disconnect\\]","Quit message:\"").concat("\"");
			Interaction inter = new DisplayInteraction<String>(report);
			this.notifyObservers(new VectorPacket<Interaction>(inter,"Disconnection",0,0));
		}
		else if(command.startsWith("[Connect]")) {
			String report = command.replaceFirst("\\[Connect\\]","Reconnection message:\"").concat("\"");
			Interaction inter = new DisplayInteraction<String>(report);
			this.notifyObservers(new VectorPacket<Interaction>(inter,"Connection",0,0));
		}
		else if("?".equals(command)) {
			writer.println("Useful commands:");
			writer.println("\t[SetUsername]<name>\t\tset the user's nickname");
			writer.println("\t[Action]<action number>\t\tchoose the selected action");
			writer.println("\t[Action]abort\t\t\texit an action and start it again");
			writer.println("\t[Chat]<message>\t\t\tsend the message to all the other players");
			writer.println("\t[Pm]<message>@@<receiver>\tsend a private message to the specified player");
			writer.println("\t[Disconnect]<report message>\tdisconnects you from the game");
			writer.println("\t[Connect]<report message>\treconnects you to the game");
			writer.println("\t[Refresh]\t\t\tasks the server to send you the updated status of the game");
			writer.flush();
		}
		else {
			writer.println("Command not recognised!\n");
			writer.flush();
		}
	}

	@Override
	public void update() {

	}

	@Override
	public void update(VectorPacket<Interaction> change) {
		if("Action".equals(change.getType()))
		{
			this.actionInteraction = change.getContent();
			writer.println(this.actionInteraction.printOptions());
			writer.flush();
		}
		else if("Quit".equals(change.getType()) && change.getContent().printOptions().contains("quit"))
		{
			writer.println("The server has closed the connection.\n" + change.getContent().printOptions().replaceFirst("quit", ""));
			writer.flush();
			this.end = false;
		}
		else
		{
			writer.println(change.getContent().printOptions());
			writer.flush();
		}
	}
}
