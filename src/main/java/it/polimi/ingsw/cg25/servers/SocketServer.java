package it.polimi.ingsw.cg25.servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.communication.Packet;
import it.polimi.ingsw.cg25.communication.VectorPacket;
import it.polimi.ingsw.cg25.gamegenerics.GameLogger;
import it.polimi.ingsw.cg25.views.SocketView;
/**
 * 
 * @author nicolo
 *
 */
public class SocketServer implements Runnable{

	/**
	 * The port on which the socket server waits for the client's connections
	 */
	private final int port;
	/**
	 * The server for which this SocketServer works as an antenna
	 */
	private ServerCD4 server;
	/**
	 * The flag to establish whether the server must go on running or not
	 */
	private boolean condition = true;
	/**
	 * The logger for the exceptions
	 */
	private GameLogger logger;
	
	/**
	 * Simmple constructor for the SocketServer
	 * @param port is the number of the welcome socket for this socket antenna
	 * @param logger is the logger to be used to log exceptions
	 * @param server is the server to which this antenna has to redirect the connections
	 */
	public SocketServer(int port, GameLogger logger,ServerCD4 server){
		this.port = port;
		this.logger = logger;
		this.server = server;
	}
	
	@Override
	public void run() {
		System.out.println("The Server is waiting for socket connections on port " + port);
		this.logger.log("Socket server is waiting for connections (port " + port+")");
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
			while(condition){
				Socket socket = serverSocket.accept();
				System.out.println(new Date() + " Client accepted for socket connection");
				logger.log("Client accepted for socket connection");
				SocketView<VectorPacket<Interaction>,Packet<Interaction>> view = new SocketView<>(socket,0);
				server.connect(view);
			}
		} catch (IOException e) {
			this.logger.log(e,"IOException while waiting/accepting Socket connections");
		}
		finally{			
			try {
				if(serverSocket!=null)
					serverSocket.close();
			} catch (IOException  e) {
				logger.log(e);
			}
		}
	}

	/**
	 * Kills the socketServer. After this method has been called no more
	 * connections are accepted by this socket antenna
	 */
	public void kill(){
		this.condition = false;
	}
	
}
