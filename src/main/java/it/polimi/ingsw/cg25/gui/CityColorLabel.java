package it.polimi.ingsw.cg25.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

import it.polimi.ingsw.cg25.gui.guilisteners.ColorClick;
import it.polimi.ingsw.cg25.model.dto.DTOCityColor;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class CityColorLabel extends JLabel implements Updatable<DTOCityColor> {

	/**
	 * The string representing the bonuses of this color. It's used for tooltip text
	 */
	private String bonuses;
	/**
	 * The List of bonuses related to this color
	 */
	private List<DTOBonus> realBonuses;
	/**
	 * The font to use
	 */
	private Font font;
	/**
	 * Initialization
	 */
	private boolean initSet = true;
	
	/**
	 * CityColorLabel class constructor
	 * @param x the horizontal position of the label
	 * @param y the vertical position of the label
	 * @param font the font to be used
	 */
	public CityColorLabel(int x, int y,Font font) {
		this.setBounds(x, y, 15, 15);
		this.setOpaque(true);
		this.font = font;
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		this.setBorder(border);
	}
	
	@Override
	public void update(DTOCityColor object) {
		this.bonuses = createString(object);
		this.setToolTipText(bonuses);
		this.realBonuses = object.getColorReward();
		if(initSet) {
			this.addMouseListener(new ColorClick(this,font));
			this.setBackground(object.getCityColor());
			initSet = false;
		}
		
	}
	
	/**
	 * This method create a string which represents all the bonuses included
	 * in the DTOCityColor object used to build the label
	 * @param cityColor the DTO city color used to build the label
	 * @return the string as mentioned above
	 */
	private String createString(DTOCityColor cityColor) {
		StringBuilder sb = new StringBuilder();
		
		if(cityColor.getColorReward().isEmpty())
			return null;
		else {
			sb.append("BONUS: ");
			for(DTOBonus b : cityColor.getColorReward()) {
				sb.append(b + ", ");
			}
			sb.setLength(sb.length() - 2);
		}
		return sb.toString();
	}

	/**
	 * @return the string which represents the color's bonuses
	 */
	public String getBonuses() {
		return bonuses;
	}
	
	/**
	 * @return a reference to the List of bonuses related to the label
	 */
	public List<DTOBonus> getRealBonuses() {
		return realBonuses;
	}

}
