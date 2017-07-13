package it.polimi.ingsw.cg25.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import it.polimi.ingsw.cg25.gui.guilisteners.NobilityClick;
import it.polimi.ingsw.cg25.model.dto.DTONobilityCell;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class NobilityCellLabel extends JLabel {

	/**
	 * Image used for those cells that do not contain any bonus
	 */
	private Image nobCell;
	/**
	 * Image used for those cells that contain at least a bonus
	 */
	private Image nobCellB;
	/**
	 * This string is used for tooltip
	 */
	private final String bonuses;
	/**
	 * The List of DTO bonuses contained in the DTO cell used to build this label
	 */
	private final List<DTOBonus> realBonuses;
	
	/**
	 * NobilityCellLabel class constructor
	 * @param cell the DTO nobility cell to draw in the gui
	 * @param guiFont the font used in all the gui panels
	 * @throws IOException if some resources fail while loading
	 */
	public NobilityCellLabel(DTONobilityCell cell, Font guiFont) throws IOException {
		loadResources();
		this.realBonuses = cell.getBonus();
		if(cell.getBonus().isEmpty()) {
			Image scaledNobCell = nobCell.getScaledInstance(43, 65, Image.SCALE_SMOOTH);
			this.setIcon(new ImageIcon(scaledNobCell));
		}
		else {
			Image scaledNobCellB = nobCellB.getScaledInstance(43, 65, Image.SCALE_SMOOTH);
			this.setIcon(new ImageIcon(scaledNobCellB));
			this.addMouseListener(new NobilityClick(this,guiFont));
		}
		
		bonuses = createString(cell);
		this.setToolTipText(bonuses);
		
		this.setSize(43, 65);
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setVerticalTextPosition(JLabel.CENTER);
		this.setText(Integer.toString(cell.getId()));
		this.setFont(new Font("SansSerif", Font.BOLD, 15));
		this.setForeground(Color.WHITE);
		
		this.repaint();
	}
	
	/**
	 * This method is used in order to load graphics resources for this class
	 * @throws IOException if any resource fails to load
	 */
	private void loadResources() throws IOException {
		nobCell = ImageIO.read(new File("src/main/resources/nobCell.png"));
		nobCellB = ImageIO.read(new File("src/main/resources/nobCellB.png"));
	}
	
	/**
	 * Create a string which represents all the bonuses included in the current
	 * nobility cell
	 * @param cell the DTO nobility cell to analize
	 * @return the bonus string, null if cell has no bonus
	 */
	private String createString(DTONobilityCell cell) {
		StringBuilder sb = new StringBuilder();
		if(cell.getBonus().isEmpty())
			return null;
		else {
			sb.append("BONUS: ");
			for(DTOBonus b : cell.getBonus())
				sb.append(b + ", ");
		}
		sb.setLength(sb.length() - 2);
		return sb.toString();
	}

	/**
	 * @return a reference to the string which contains the bonus of the cell
	 */
	public String getBonuses() {
		return bonuses;
	}
	
	/**
	 * @return the List of DTO bonuses included in the DTO nobility cell which was
	 * used in order to build this label
	 */
	public List<DTOBonus> getRealBonuses() {
		return this.realBonuses;
	}
	
}
