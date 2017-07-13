package it.polimi.ingsw.cg25.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;

import it.polimi.ingsw.cg25.model.dto.DTOEmporium;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class CityEmporiums extends JLabel {

	/**
	 * CityEmporium class constructor. This is a custom JLabel to
	 * represents emporiums on map
	 * @param x the horizontal position of the emporium
	 * @param y the vertical position of the emporium
	 * @param emp the DTOEmporium to represent
	 * @throws NullPointerException if emp is null
	 */
	public CityEmporiums(int x, int y, DTOEmporium emp) {
		if(emp == null)
			throw new NullPointerException("You cannot create an emporium label with a null emporium!");
		
		this.setOpaque(true);
		this.setBounds(x, y, 8, 8);
		this.setBackground(emp.getColor());
		
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		this.setBorder(border);
	}
	
}
