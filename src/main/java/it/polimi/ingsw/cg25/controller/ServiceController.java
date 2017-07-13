package it.polimi.ingsw.cg25.controller;

import java.util.Date;

import it.polimi.ingsw.cg25.actions.DisplayInteraction;
import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.communication.VectorPacket;
import it.polimi.ingsw.cg25.onlinegenerics.OnlineApplication;
import it.polimi.ingsw.cg25.proxies.ViewProxy;
/**
 * 
 * @author nicolo
 *
 */
public class ServiceController {

	/**
	 * Reference to the game on which this controller applies the required services
	 */
	private OnlineApplication game;
	/**
	 * The view Proxy used by the controller as an interface with the users
	 */
	private ViewProxy proxy;
	/**
	 * Just a string to avoid rewriting of "Error" ad packet type
	 */
	private static final String ERROR = "Error";

	/**
	 * Constructor for service Controller
	 * @param game the online application that acts as model for this controller
	 */
	public ServiceController(OnlineApplication game) {
		if(game == null)
			throw new IllegalArgumentException("Need a valid game to instantiate the service controller");
		this.game = game;
	}
	
	/**
	 * Sets the interface to be used with the views
	 * @param proxy the viewProxy to be set
	 */
	public void setProxy(ViewProxy proxy) {
		if(proxy == null)
			throw new IllegalArgumentException("No null proxies are accepted");
		this.proxy = proxy;
	}
	
	/**
	 * This method is called from the viewProxy to ask the service controller to 
	 * perform a service
	 * @param packet the VectorPacket containing the non-action interaction
	 */
	public void sendInteraction(VectorPacket<Interaction> packet){
		if(packet == null)
			throw new IllegalArgumentException("Null packet received");
		Date data = new Date();
		String msg;
		switch(packet.getType()){
		
		 // A user is asking to be disconnected from the game
		case "Disconnection":
			game.disconnect(packet.getPublicSource(),packet.getContent().printOptions());
			msg = "[info] the user " + game.addressBook(packet.getPublicSource()) + " is no longer connected. " + packet.getContent().printOptions();
			proxy.send(new DisplayInteraction<String>(msg), "Info",-packet.getPublicSource());
			proxy.send(new DisplayInteraction<String>("You have been disconnected.".concat(packet.getContent().printOptions())), "Disconnection", packet.getPublicSource());
			break;
			
		// A user is asking to be connected to the game
		case "Connection":
			try{
				game.connect(packet.getPublicSource());
				msg = "[info] the user " + game.addressBook(packet.getPublicSource()) + " has come back to the game. " + packet.getContent().printOptions();
				proxy.send(new DisplayInteraction<String>(msg), "Info",-packet.getPublicSource());
				proxy.send(new DisplayInteraction<String>("You have been reconnected.".concat(packet.getContent().printOptions())), "Connection", packet.getPublicSource());
			}
			catch(IllegalArgumentException e){
				proxy.send(new DisplayInteraction<String>(e.getMessage()), ERROR,packet.getPublicSource());
			}
			break;
			
		 // A user is sending a message in the public chat
		case "Chat":
			msg = 
					"[Public Chat] " + 
					data.toString() + " - " + game.addressBook(packet.getPublicSource())
					+ " : " + packet.getContent().printOptions();
			game.chat(msg);
			proxy.send(new DisplayInteraction<String>(msg), packet.getType());
			break;
			
		// A user is sending a private message to an user 
		case "Pm":
			String []parts = packet.getContent().printOptions().split("@@");
			int dest = 0;
			try{
				dest = game.nameService(parts[1]);
				msg = 
						"[Private Chat] " + 
								data.toString() + " - " + game.addressBook(packet.getPublicSource())
								+ " : " + parts[0];
				proxy.send(new DisplayInteraction<String>(msg),"Chat",dest);
				break;
			}catch(IllegalArgumentException | IndexOutOfBoundsException e){
				proxy.send(new DisplayInteraction<String>("Something went wrong with your Pm"), ERROR,packet.getPublicSource());
				break;
			}
			
		case "SetUsername":
			try{
				String formerName = game.addressBook(packet.getPublicSource());
				game.changeUsername(packet.getPublicSource(), packet.getContent().printOptions());
				msg = "[info] the user " + formerName + " has changed his name to " + packet.getContent().printOptions();
				proxy.send(new DisplayInteraction<String>(msg), "Info");
			}catch(Exception e){
				proxy.send(new DisplayInteraction<String>(e.getMessage()), ERROR,packet.getPublicSource());
				return;
			}
			break;
			
		case "Status":
			try{
				proxy.send(new DisplayInteraction<>(this.game.getStatus(packet.getPublicSource())),"GameStatus",packet.getPublicSource());
			}catch(Exception e){
				proxy.send(new DisplayInteraction<String>("Either the game is not running or the player is unknown"), ERROR,packet.getPublicSource());
				return;
			}
			break;
		case "Quit":
			try{
				this.game.disconnect(packet.getPublicSource(), "Quitted the game");
				proxy.send(new DisplayInteraction<String>("[info]" + new Date().toString() + " the player " + this.game.addressBook(packet.getPublicSource()) + " has left the game."), "Info",-packet.getPublicSource());
				proxy.send(new DisplayInteraction<String>("[Quit]" + new Date().toString() + " You have just left the game. "), "Info",packet.getPublicSource());
			}catch(Exception e){
				proxy.send(new DisplayInteraction<String>("Who are you?"), ERROR );
			}
			break;

		// The type of the message is not a known service
		default:
			proxy.send(new DisplayInteraction<String>("Not recognised option"),ERROR,packet.getPublicSource());
			break;
		}
	}
	
}
