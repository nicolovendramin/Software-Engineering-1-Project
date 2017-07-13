package it.polimi.ingsw.cg25.views;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.communication.Packet;
import it.polimi.ingsw.cg25.communication.VectorPacket;
import it.polimi.ingsw.cg25.observer.Observable;
import it.polimi.ingsw.cg25.observer.Observer;
import it.polimi.ingsw.cg25.views.RMIViewRemote;
/**
 * 
 * @author nicolo
 *
 * @param <E> must be a subtype of Packet<Interaction>
 */
public class RMIView <E extends Packet<Interaction>> extends UnicastRemoteObject implements Serializable,Observer<E>,RMIViewRemote{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1530365924657032493L;
	/**
	 * The id of the view
	 */
	private final int myId;
	/**
	 * The stub of the remote view that is connected to this
	 */
	private RMIViewRemote twin; 
	/**
	 * The flag that represents the fact that the view is active
	 */
	private boolean active = true;
	/**
	 * This object is the observable for this class
	 */
	private final transient RMIObservable observableRmi;
	
	/**
	 * This class is needed because we couldn't make RMIView extends both UnicastRemoteObject and observable
	 * @author nicolo
	 *
	 */
	public class RMIObservable extends Observable<VectorPacket<Interaction>>{
		
	}

	/**
	 * Initialize an RMIView 
	 * @param id is the id to assign to the view
	 * @param twin is the stub of the remote rmi view linked to this one
	 * @throws RemoteException
	 */
	public RMIView(int id,RMIViewRemote twin) throws RemoteException{
		this.myId = id;
		this.twin = twin;
		this.observableRmi = new RMIObservable();
	}
	
	/**
	 * Constructor which doesn't requires the stub of the twin
	 * @param id is the id of the view
	 * @throws RemoteException
	 */
	public RMIView(int id) throws RemoteException{
		this.myId = id;
		this.observableRmi = new RMIObservable();
	}
	
	/**
	 * This constructor builds a view basing on an existing view
	 * @param temp
	 * @param id
	 * @throws RemoteException
	 */
	public RMIView(RMIView<E> temp,int id) throws RemoteException{
		this.myId= id;
		this.twin = temp.twin;
		this.observableRmi = new RMIObservable();
	}
	
	/**
	 * This method links this view to its remote twin
	 * @param r is the stub of the remote view
	 */
	public void attachTwin(RMIViewRemote r){
		this.twin = r;
	}
	
	@Override
	public void input(VectorPacket<Interaction> change) throws RemoteException{
		if(!active) 
			throw new RemoteException("The connection has been closed");
		Interaction e = change.getContent();
		VectorPacket<Interaction> vp = new VectorPacket<Interaction>(e, change.getType(), change.getDestination(), myId);
		this.observableRmi.notifyObservers(vp);
	}

	@Override
	public void update() {
		// empty update
	}

	@Override
	public void update(E change) {
		Interaction e;
		if(!this.active)
			return;
		try {
			e = change.getContent(myId);
			if(change.getContent(myId).printOptions().startsWith("quit") && "Quit".equals(change.getType()))
			{
				this.active = false;
			}
			twin.input(new VectorPacket<Interaction>(e,change.getType(),0,myId));
			if(!this.active)
			{
				twin = null;
				//try to see if the rmi now dies
				unexportObject(this, true);
			}
		} catch (IllegalAccessException e1) {
			return;
		} catch (RemoteException e1) {
			this.active = false;
		}
	}
	
	/**
	 * Needed to access the observable element of this class
	 * @return the obsevable rmi view
	 */
	public RMIObservable getObservableRmi(){
		return this.observableRmi;
	}
}
