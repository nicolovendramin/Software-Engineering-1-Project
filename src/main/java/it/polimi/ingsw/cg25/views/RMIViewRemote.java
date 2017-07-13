package it.polimi.ingsw.cg25.views;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.communication.VectorPacket;
/**
 * 
 * @author nicolo
 *
 */
public interface RMIViewRemote extends Remote,Serializable{

	/**
	 * This is the method that the RMIView exposes to the client. It gives the
	 * possibility to send to the server a VectorPacket containing an interaction
	 * @param change is the packet to be sent to the server
	 * @throws RemoteException 
	 */
	public void input(VectorPacket<Interaction> change) throws RemoteException;
	
}
