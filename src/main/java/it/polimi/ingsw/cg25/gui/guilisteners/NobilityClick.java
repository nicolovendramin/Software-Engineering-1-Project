package it.polimi.ingsw.cg25.gui.guilisteners;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import it.polimi.ingsw.cg25.gui.CardDisplayer;
import it.polimi.ingsw.cg25.gui.NobilityCellLabel;

/**
 * 
 * @author Giovanni
 *
 */
public class NobilityClick implements MouseListener {

	/**
	 * A reference to the nobility cell label
	 */
	private final NobilityCellLabel cell;
	/**
	 * The font to be used for any writing
	 */
	private final Font guiFont;

	/**
	 * NobilityClick listener class constructor
	 * @param cell a reference to the nobility cell label to link to this listener
	 * @param guiFont the font used all over the gui
	 */
	public NobilityClick(NobilityCellLabel cell, Font guiFont) {
		this.cell = cell;
		this.guiFont = guiFont;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		new CardDisplayer(guiFont, cell.getRealBonuses());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		cell.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		cell.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//Do nothing
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//Do nothing
	}

}
