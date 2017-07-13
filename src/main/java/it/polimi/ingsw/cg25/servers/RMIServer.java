package it.polimi.ingsw.cg25.servers;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import it.polimi.ingsw.cg25.views.RMIView;
import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.communication.Packet;
import it.polimi.ingsw.cg25.gamegenerics.GameLogger;
import it.polimi.ingsw.cg25.views.RMIViewRemote;
/**
 * 
 * @author nicolo
 *
 */
public class RMIServer implements Runnable, RMIServerInterface {

	/**
	 * The server port
	 */
	private final int port;
	/**
	 * The reference to the server
	 */
	private ServerCD4 server;
	/**
	 * The reference to the registry on which the rmi server is published
	 */
	private final Registry registry;
	/**
	 * The logger for the exceptions
	 */
	private GameLogger logger;

	/**
	 * Creates an RMIServer
	 * @param port is the port where to publish the server
	 * @param logger is the reference to the logger to be used to log exceptions
	 * @param server is the reference to the server for which the rmi antenna works
	 * @throws RemoteException if there are problems locating the registry on the specified port
	 */
	public RMIServer(int port, GameLogger logger,ServerCD4 server) throws RemoteException {
		this.logger = logger;
		this.port = port;
		this.server = server;
		registry = LocateRegistry.createRegistry(port);
	}

	@Override
	public void run() {
		try {
			RMIServerInterface gameRemote = (RMIServerInterface) UnicastRemoteObject.exportObject(this, port);
			registry.bind("CD4", gameRemote);
			System.out.println("The RMIServer is now published on the registry waiting for connections on port " + this.port);
			logger.log("The RMIServer is now published on the registry (port "+this.port+")");
		} catch (RemoteException e) {
			logger.log(e,"The start of the RMI connection has experienced some problem. Restart the Server, possibly using another RMI port");
			System.out.println(
					"The start of the RMI connection has experienced some problem. Restart the Server, possibly using another RMI port");
		} catch (AlreadyBoundException e) {
			logger.log(e);
		}

	}

	/**
	 * Closes the server and unbinds it from the registry
	 */
	public void kill() {
		try {
			this.registry.unbind("CD4");
		} catch (RemoteException | NotBoundException e) {
			logger.log(e);
		}
	}

	@Override
	public RMIViewRemote connectToRmi(RMIViewRemote client) throws RemoteException {
		RMIView<Packet<Interaction>> view = new RMIView<>(0,client);
		System.out.println(new Date() + " Client accepted for RMI connection");
		logger.log("Client accepted for RMI connection");
		return server.connect(view);
	}
}
