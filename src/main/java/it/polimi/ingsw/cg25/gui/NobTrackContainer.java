package it.polimi.ingsw.cg25.gui;

import java.awt.Font;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import it.polimi.ingsw.cg25.model.dto.DTONobilityCell;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class NobTrackContainer extends JPanel {
	
	/**
	 * NobTrackContainer class constructor. It uses BoxLayout
	 */
	public NobTrackContainer() {
		//Align horizontally
		BoxLayout box = new BoxLayout(this, BoxLayout.X_AXIS);
		this.setLocation(0, 0);
		this.setLayout(box);
		this.setOpaque(false);
	}

	/**
	 * This method adds a cell as a label to the gui nobility track
	 * @param cellToAdd the DTO cell to be added
	 * @param guiFont the font to be used
	 */
	public void addCell(DTONobilityCell cellToAdd, Font guiFont) {
		try {
			this.add(new NobilityCellLabel(cellToAdd, guiFont));
		} catch (IOException e) {
			System.out.println("Failed while adding a nobility cell!");
			e.printStackTrace();
		}
		this.repaint();
	}
	
}
