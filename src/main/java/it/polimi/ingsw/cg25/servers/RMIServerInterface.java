package it.polimi.ingsw.cg25.servers;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.cg25.views.RMIViewRemote;
/**
 * The remote interface for the RMIServer
 * @author nicolo
 *
 */
public interface RMIServerInterface extends Remote{
	
	/**
	 * This method is exposed to the clients to connecto to the rmiserver
	 * @param client is the rmiview that the server wull use to ineract with the client
	 * @return the RMIView that the client will use to interact with the server
	 * @throws RemoteException 
	 */
	public RMIViewRemote connectToRmi(RMIViewRemote client) throws RemoteException;
	
}
