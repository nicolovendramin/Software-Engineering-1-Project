package it.polimi.ingsw.cg25.gui.guilisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import it.polimi.ingsw.cg25.actions.DisplayInteraction;
import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.clients.GuiSimpleClient;
import it.polimi.ingsw.cg25.communication.VectorPacket;

/**
 * 
 * @author Giovanni
 *
 */
public class QuitAction implements ActionListener {
	
	/**
	 * A reference to the gui client
	 */
	private final GuiSimpleClient gui;
	
	/**
	 * QuitAction class constructor
	 * @param gui the gui client object
	 */
	public QuitAction(GuiSimpleClient gui) {
		this.gui = gui;
	}
	
	/**
	 * Quit the game
	 */
	public QuitAction(){
		this.gui = null;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(gui!=null)
			this.gui.notifyObservers(new VectorPacket<Interaction>(new DisplayInteraction<String>("quit"), "Quit", 0, 0));
		System.exit(0);
	}

}
