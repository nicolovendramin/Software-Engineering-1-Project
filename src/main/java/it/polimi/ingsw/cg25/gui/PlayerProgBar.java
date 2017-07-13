package it.polimi.ingsw.cg25.gui;

import javax.swing.JProgressBar;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class PlayerProgBar extends JProgressBar {

	/**
	 * PlayerProgBar class constructor
	 * @param x the horizontal position of the progress bar
	 * @param y the vertical position of the progress bar
	 * @param w the width of the progress bar
	 * @param h the height of the prigress bar
	 */
	public PlayerProgBar(int x, int y, int w, int h) {
		this.setBounds(x, y, w, h);
		this.setStringPainted(true);
	}
	
	@Override
	public void setValue(int value) {
		super.setValue(value);
		super.setString(Integer.toString(value));
	}
}
