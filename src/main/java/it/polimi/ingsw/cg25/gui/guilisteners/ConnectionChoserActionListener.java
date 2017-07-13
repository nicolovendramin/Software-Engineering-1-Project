package it.polimi.ingsw.cg25.gui.guilisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import it.polimi.ingsw.cg25.gui.CoFGui;
import it.polimi.ingsw.cg25.gui.ConnectionChoser;
/**
 * 
 * @author nicolo
 *
 */
public class ConnectionChoserActionListener implements ActionListener{
	/**
	 * The interface of this listener
	 */
	private final CoFGui myFrame;
	
	/**
	 * Simple constructor for the listener
	 * @param myFrame the interface to call methods on
	 */
	public ConnectionChoserActionListener(CoFGui myFrame){
		this.myFrame = myFrame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ConnectionChoser choser = new ConnectionChoser(myFrame);
		choser.setVisible(true);
	}

}
