package it.polimi.ingsw.cg25.views;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.communication.Packet;
import it.polimi.ingsw.cg25.communication.VectorPacket;
import it.polimi.ingsw.cg25.observer.Observable;
import it.polimi.ingsw.cg25.observer.Observer;
/**
 * 
 * @author nicolo
 *
 * @param <T> the type of packets that this view sends to its observers
 * @param <E> the tupe of packets that this view observes for
 */
public class SocketView<T extends Packet<Interaction>,E extends Packet<Interaction>> extends Observable<T> implements Observer<E>{

	/**
	 * The thread that listens on the socket for this view
	 */
	private SocketInputThread<T,E> input;
	/**
	 * The socketOutput stream on which this view writes the messages for its twin
	 */
	private ObjectOutputStream socketOut;
	/**
	 * The id of this view
	 */
	private final int myId;
	/**
	 * The state of the view. true = active
	 */
	private boolean active = true;
	/**
	 * The communication socket used to exchange objects with the twin
	 */
	private final Socket socket;
	
	/**
	 * Constructor which instantiate a socket view
	 * @param socket the socket to be used for communication
	 * @param id the id to assign to the view
	 * @throws IOException 
	 */
	public SocketView(Socket socket,int id) throws IOException{
		this.socket = socket;
		this.myId = id;
	}
	
	/**
	 * Builds a SocketView starting from an existing one
	 * @param socketView is the existring view to be reinstantiated
	 * @param id is the new id to assign to the view
	 */
	public SocketView(SocketView<T,E> socketView,int id){
		this.myId = id;
		this.socket = socketView.socket;
	}
	

	/**
	 * Runs the input Thread assigning it a name to make debugging easier
	 * @param inputThreadName is the name to be assigned to the inputThread
	 */
	public void run(String inputThreadName) {
		try {
			socketOut = new ObjectOutputStream(socket.getOutputStream());
			socketOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		input = new SocketInputThread<>(socket,this,myId);
		Thread t = new Thread(input);
		t.setName(inputThreadName);
		t.start();
	}


	@Override
	public void update() {
		this.closeView();
	}

	/**
	 * Stops the view. closes the socket
	 */
	public void stop() {
		try {
			socket.close();
		} catch (IOException e) {
			//If this exception is called is just becaus the connection has already been closed on the other side
			//so we don't actually need to handle it.
		}
	}

	/**
	 * Closes the view, kills the input thread and close the socket
	 */
	public void closeView(){
		//the update method
		try {
			socketOut.close();
		} catch (IOException e) {
			//this exception is thrown only if the game wass already closed on the server side, so basically if you are 
			//trying to close an already-closed socket. I won't handle it because this is something that must happen sistematically
		}
		this.stop();
		this.active = false;
		this.input.kill();
	}
	
	@Override
	public void update(E change) {
		if(!this.active)
			return;
		Interaction interaction = null;
		try{
			try{
				interaction = change.getContent(myId);
			}catch(IllegalAccessException e){
				return;
			}
			socketOut.writeObject(new VectorPacket<Interaction>(interaction,change.getType(),this.myId,0));
			socketOut.flush();
		}catch(IOException e){
			this.closeView();	
		}
		finally{
			if(interaction != null && interaction.printOptions().startsWith("quit") && "Quit".equals(change.getType()))
				this.closeView();			
		}
	}

}
