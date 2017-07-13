package it.polimi.ingsw.cg25.gui.guilisteners;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import it.polimi.ingsw.cg25.gui.AboutWindow;

/**
 * 
 * @author Giovanni
 *
 */
public class AboutAction implements ActionListener {
	
	/**
	 * The window with "about" information
	 */
	private AboutWindow aboutWnd;
	/**
	 * The font to be used for any writing
	 */
	private Font font;
	
	/**
	 * AboutAction class constructor
	 * @param font a custom font to be used
	 */
	public AboutAction(Font font) {
		this.font = font;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		aboutWnd = new AboutWindow(font);
		aboutWnd.setVisible(true);
	}

}
