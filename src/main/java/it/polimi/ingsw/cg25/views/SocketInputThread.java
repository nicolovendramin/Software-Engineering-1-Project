package it.polimi.ingsw.cg25.views;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.communication.Packet;
/**
 * 
 * @author nicolo
 *
 * @param <T> the type of packet dispatched by the view of this input thread
 * @param <E> the tyoe of packet observed by the view of this input thread
 */
public class SocketInputThread<T extends Packet<Interaction>,E extends Packet<Interaction>> implements Runnable {

	/**
	 * The socket on which the view communicates with its remote twin
	 */
	private final Socket socket;
	/**
	 * The view for which this thread provides the inputs
	 */
	private final SocketView<T,E> view;
	/*
	 * The status of the thread. False = alive
	 */
	private boolean end = false;
	/**
	 * The id of the view for which this Input Thread listens the socket
	 */
	private final int myViewId;
	
	/**
	 * Constructor
	 * @param socket is the socket used for communication
	 * @param view is the view of this thread
	 * @param id is the view Id
	 */
	public SocketInputThread(Socket socket,SocketView<T,E> view,int id) {
		this.view = view;
		this.socket = socket;
		this.myViewId = id;
	}
	

	@Override
	public void run() {
		ObjectInputStream socketIn;
		try {
			socketIn = new ObjectInputStream(socket.getInputStream());
		try {
		while(!end){
			@SuppressWarnings("unchecked")
			T change = (T)socketIn.readObject();
			try{
				change.setPublicSource(myViewId);
			}catch(IllegalAccessException e){
			}
			this.view.notifyObservers(change);
		}	
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch(IOException  e){
			this.view.closeView();
		}
		try {
			socketIn.close();
		} catch (IOException e) {
		}
		} catch (IOException e1) {
		}
	}
	
	/**
	 * This methods kills the inputThread so that no more communications are received 
	 * from the socket
	 */
	protected void kill(){
		this.end = true;
	}

}
