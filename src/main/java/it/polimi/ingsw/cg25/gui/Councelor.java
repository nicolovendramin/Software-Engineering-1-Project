package it.polimi.ingsw.cg25.gui;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dto.DTOCouncelor;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class Councelor extends JLabel {
	
	/**
	 * A label with the color of the councelor
	 */
	private JLabel colorLabel;
	/**
	 * The councelor's shape image
	 */
	private BufferedImage coun;
	/**
	 * The shape icon used in the this label
	 */
	private ImageIcon counIcon;
	
	/**
	 * Councelor class constructor
	 * @param x the horizontal position of the label
	 * @param y the vertical position of the label
	 * @param sizeFactor the size factor used to resize the component. This label can
	 * be used to build balconies, but also to display councelors while choosing an action and
	 * the its size is different in both cases
	 */
	public Councelor(int x, int y, int sizeFactor) {
		try {
			coun = ImageIO.read(new File("src/main/resources/counBrighter.png"));
		} catch (IOException e) {
			System.out.println("Failed while loading councelor image!");
			e.printStackTrace();
		}
		counIcon = new ImageIcon(coun.getScaledInstance(20*sizeFactor, 30*sizeFactor, Image.SCALE_SMOOTH));
		this.setBounds(x, y, counIcon.getIconWidth(), counIcon.getIconHeight());
		this.setIcon(counIcon);
		colorLabel = new JLabel();
		colorLabel.setOpaque(false);
		colorLabel.setBounds(4*sizeFactor, 16*sizeFactor, 12*sizeFactor, 12*sizeFactor);
		this.add(colorLabel);
	}
	
	/**
	 * Alternative class constructor with a default scale factor of 1
	 * @param x the horizontal position of the label
	 * @param y the vertical position of the label
	 */
	public Councelor(int x, int y) {
		this(x, y, 1);
	}
	
	/**
	 * Councelor class constructor in order to build a councelor label
	 * with a particular color
	 * @param councelor the DTO councelor used to create the label
	 * @param factorSize the factor size
	 */
	public Councelor(DTOCouncelor councelor, int factorSize) {
		this(0, 0, factorSize);
		this.setColor(councelor.getColor());
	}

	/**
	 * This method is used to change the councelor's color
	 * @param color the color to set
	 */
	protected void setColor(HSBColor color) {
		colorLabel.setOpaque(true);
		colorLabel.setBackground(color);
		this.repaint();
	}

}
