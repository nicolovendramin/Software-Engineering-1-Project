package it.polimi.ingsw.cg25.gui.guilisteners;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import it.polimi.ingsw.cg25.gui.CardDisplayer;
import it.polimi.ingsw.cg25.gui.CityColorLabel;

/**
 * 
 * @author Giovanni
 *
 */
public class ColorClick implements MouseListener {

	/**
	 * A reference to the city color label
	 */
	private final CityColorLabel color;
	/**
	 * The font to be used for any writing
	 */
	private final Font font;
	
	/**
	 * ColorClick listener class constructor
	 * @param color a reference to the city color label to link to this listener
	 * @param guiFont the font used all over the gui
	 */
	public ColorClick(CityColorLabel color, Font font) {
		this.color = color;
		this.font = font;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		new CardDisplayer(font, color.getRealBonuses());
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		color.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		color.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		//Do nothing

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		//Do nothing

	}

}
