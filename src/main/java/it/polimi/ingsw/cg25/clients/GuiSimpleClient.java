package it.polimi.ingsw.cg25.clients;
import it.polimi.ingsw.cg25.actions.DisplayInteraction;
import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.SwingUtilities;

import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.communication.VectorPacket;
import it.polimi.ingsw.cg25.gui.CoFGui;
import it.polimi.ingsw.cg25.gui.guilisteners.QuitAction;
import it.polimi.ingsw.cg25.model.dto.GameStatus;
import it.polimi.ingsw.cg25.observer.Observable;
import it.polimi.ingsw.cg25.observer.Observer;
import it.polimi.ingsw.cg25.servers.RMIServerInterface;
import it.polimi.ingsw.cg25.views.RMIView;
import it.polimi.ingsw.cg25.views.RMIViewRemote;
import it.polimi.ingsw.cg25.views.SocketView;

/**
 * 
 * @author Giovanni <------
 * @author Nicolo
 *
 */
public class GuiSimpleClient extends Observable<VectorPacket<Interaction>> implements Observer<VectorPacket<Interaction>> {

	/**
	 * The graphical interface from wich this client takes the inputs
	 */
	private CoFGui gui;
	
	/**
	 * This code Just starts the gui, setting up the interface and asking the 
	 * event dispatcher to start it. It terminates after that the interface has been loaded
	 * and shown
	 * @param is the args of the main method. No args are required nor used
	 */
	public static void main(String[] args) {
		
		//Creo un oggetto runnable e definisco al suo interno il metodo run()
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//Inizializza la GUI
				GuiSimpleClient client = new GuiSimpleClient();
				try {
					client.gui = new CoFGui(client);
					client.gui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(VectorPacket<Interaction> change) {
		if(change.getType().equals("Chat"))
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					gui.chat(change.getContent().printOptions());
				}
				
			});
		if(change.getType().equals(("Info")))
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					gui.displayMessage(change.getContent().printOptions(), false);	
				}
			});
		if(change.getType().equals(("Error")))
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					gui.displayMessage(change.getContent().printOptions(), true);
				}
			});
		if(change.getType().equals(("Action")))
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					gui.updateAction(change.getContent());
				}
			});
		if(change.getType().equals("GameStatus"))
		SwingUtilities.invokeLater(new Runnable(){
		
			public void run(){
				@SuppressWarnings("unchecked")
				DisplayInteraction<GameStatus> updatedGameStatus = (DisplayInteraction<GameStatus>)(change.getContent());
				gui.update(updatedGameStatus.getContent());
			}
		});
		if(change.getType().equals(("Quit"))){
			SwingUtilities.invokeLater(new Runnable(){
		
				public void run(){
				gui.displayMessage(change.getContent().printOptions().replace("quit",""), true);
				QuitAction action = new QuitAction();
				action.actionPerformed(null);
				}
			});
		}
		if(change.getType().equals(("Disconnection"))){
			SwingUtilities.invokeLater(new Runnable(){
		
				public void run(){
				gui.disconnect(change.getContent().printOptions());
				}
			});
		}
		if(change.getType().equals(("Connection"))){
			SwingUtilities.invokeLater(new Runnable(){
		
				public void run(){
				gui.connect(change.getContent().printOptions());
				}
			});
		}
	}
	
	/**
	 * Tries to start the connection
	 * @param port the port on which to try tthe attempt
	 * @param ip of the server
	 * @param connectionType can be one of "Rmi" and "Socket", with the clear meaning
	 * @return whether the connection was successfull or not
	 */
	public boolean start(int port, String ip, String connectionType) {
		if(connectionType.equals("Rmi")){
			try {
				RMIView<VectorPacket<Interaction>> view;
				view = new RMIView<>(0);
			Registry registry;
			registry = LocateRegistry.getRegistry(ip, port);
			RMIServerInterface server = (RMIServerInterface)registry.lookup("CD4");
			RMIViewRemote twin;
				twin = server.connectToRmi(view);
	
			view.attachTwin(twin);
			this.attachObserver(view);
			view.getObservableRmi().attachObserver(this);
			gui.displayMessage("Connection accepted  ", false);
			return true;
			} catch (RemoteException | NotBoundException e1) {
				gui.displayMessage(e1.getMessage(), true);
				return false;
			}
		}
		else if(connectionType.equals("Socket")){
			SocketView<VectorPacket<Interaction>,VectorPacket<Interaction>> view;
			Socket socket;
			try {
				socket = new Socket(ip,port);
			view = new SocketView<>(socket,0);
			view.attachObserver(this);
			this.attachObserver(view);
			view.run("RemoteInput");
			gui.displayMessage("Connection accepted  ", false);
			return true;
			} catch (IOException e) {
				gui.displayMessage(e.getMessage(), true);
				return false;
			}
		}
		return false;
	}

	/**
	 * This sends to the server a reply to the interaction that has been filled by the client
	 * @param actionInteraction is the filled actionInteraction
	 */
	public void sendAction(Interaction actionInteraction) {
		this.notifyObservers(new VectorPacket<Interaction>(actionInteraction,"Action",0,0));
	}
	
	/**
	 * Asks the server to change the username of the player associated with this client
	 * @param newName the new name for the client
	 */
	public void changeUsername(String newName){
		this.notifyObservers(new VectorPacket<Interaction>(new DisplayInteraction<String>(newName),"SetUsername",0,0));
	}
	
	/**
	 * Asks the server to disconnect the player associated with this client
	 * @param message the disconnection message
	 */
	public void disconnect(String message){
		this.notifyObservers(new VectorPacket<Interaction>(new DisplayInteraction<String>((message == null ? "" : message)), "Disconnection", 0,0));
	}
	
	/**
	 * Asks the server for a game status to update the interface
	 * @param message is the optional message for the request
	 */
	public void refresh(String message){
		this.notifyObservers(new VectorPacket<Interaction>(new DisplayInteraction<String>(""), "Status", 0,0));
		
	}

	/**
	 * Asks the server to reconnect the player associated with this client
	 * @param message the reconnection message
	 */
	public void connect(String message){
		this.notifyObservers(new VectorPacket<Interaction>(new DisplayInteraction<String>((message == null ? "" : message)), "Connection", 0,0));
	}
}
