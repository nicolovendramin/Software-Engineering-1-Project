package it.polimi.ingsw.cg25.proxies;

import it.polimi.ingsw.cg25.actions.DisplayInteraction;
import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.communication.Packet;
import it.polimi.ingsw.cg25.communication.VectorPacket;
import it.polimi.ingsw.cg25.controller.ActionController;
import it.polimi.ingsw.cg25.controller.ServiceController;
import it.polimi.ingsw.cg25.observer.Observable;
import it.polimi.ingsw.cg25.observer.Observer;

/**
 * 
 * 
 * @author nicolo
 *
 */

public class ViewProxy extends Observable<Packet<Interaction>> implements Observer<VectorPacket<Interaction>> {
	
	/**
	 * The action controller for this proxy
	 */
	ActionController controller;
	/**
	 * The service controller for this proxy
	 */
	ServiceController serviceController;
	/**
	 * The id of the focused player
	 */
	int currentFocus = 1;

	/**
	 * Constructor for the view proxy
	 * @param actionController is the action controller for this proxy
	 * @param serviceController is the service controller for this proxy
	 */
	public ViewProxy(ActionController actionController,ServiceController serviceController) {
		this.controller = actionController;
		this.serviceController = serviceController;
	}

	/**
	 * This method changes the id of the focused player
	 * @param id the new id to be set
	 */
	public void changeFocus(int id) {
		this.currentFocus = id;
	}

	@Override
	public void update() {
	}
	
	/**
	 * This method is made to know the focus of the view proxy
	 * @return the id of the focused player or -1 if the proxy is not focusing any player
	 */
	public int getFocusedPlayer(){
		return this.currentFocus;
	}

	@Override
	public void update(VectorPacket<Interaction> change) {
		String action = "Action";
		Interaction received = change.getContent();
		if(change.getType().equals(action) && change.getPublicSource()!=currentFocus)
		{
			this.send(new DisplayInteraction<String>("It's not your turn to perfrom actions. Be polite"), "Error",change.getPublicSource());
		}
		if(change.getType().equals(action) && change.getPublicSource()==this.currentFocus)
		{
			this.controller.sendReply(received);
		}
		else if(!change.getType().equals(action))
		{
			try{
				this.serviceController.sendInteraction(change);
			}catch(Exception e){
				this.notifyObservers(new VectorPacket<Interaction>(new DisplayInteraction<String>(e.getMessage()),"Error",0,0));
			}
		}
	}

	/**
	 * This method asks the viewProxy to broadcast the message to all of its views
	 * @param message the Interaction message to be broadcasted
	 * @param type the label to put on the packet
	 */
	public void send(Interaction message,String type) {
		this.notifyObservers(new VectorPacket<Interaction>(message,type,0,0));
	}
	
	/**
	 * The send method asks to send the packet to the specified id
	 * @param message is the interaction message to be sent
	 * @param type is the label to be put on the packet
	 * @param id is the id of the view that must receive the packet
	 */
	public void send(Interaction message,String type,int id){
		this.notifyObservers(new Packet<Interaction>(message,type,id));
	}
	/**
	 * This method asks to send the packet to the focused player
	 * @param message is the interaction message to be sent
	 * @param type is the type to put on the packet
	 */
	public void sendToFocusedPlayer(Interaction m,String type){
		this.send(m,type,this.currentFocus);
	}

}
