package it.polimi.ingsw.cg25.gui.guilisteners;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import it.polimi.ingsw.cg25.gui.CardDisplayer;
import it.polimi.ingsw.cg25.gui.KingRewardPanel;

/**
 * 
 * @author Giovanni
 *
 */
public class RewardClick implements MouseListener {

	/**
	 * A reference to the king reward panel
	 */
	private final KingRewardPanel krp;
	/**
	 * The font to be used for any writing
	 */
	private final Font font;
	
	/**
	 * RewardClick listener class constructor
	 * @param krp the king reward panel to link to this listener
	 * @param font the font used all over this gui
	 */
	public RewardClick(KingRewardPanel krp, Font font) {
		this.krp = krp;
		this.font = font;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		new CardDisplayer(font, krp.getRealBonuses());
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		krp.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		krp.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
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
