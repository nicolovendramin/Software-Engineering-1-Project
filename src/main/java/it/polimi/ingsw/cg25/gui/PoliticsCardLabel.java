package it.polimi.ingsw.cg25.gui;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard;
/**
 * 
 * @author nicolo
 *
 */
@SuppressWarnings("serial")
public class PoliticsCardLabel extends JLabel {
	
	/**
	 * PoliticsCardLabel class constructor
	 * @param politicsCard the DTO politics card used to build this label
	 */
	public PoliticsCardLabel(DTOPoliticsCard politicsCard,int width,int height) {
		this.setBounds(0, 0, width, height);
		this.setOpaque(true);
		
		try {
			if(politicsCard.isJolly()) {
				//Jolly
				ImageIcon image = new ImageIcon(ImageIO.read(
						new File("src/main/resources/PolCardJolly.png")).getScaledInstance(width, height, Image.SCALE_SMOOTH));
				this.setIcon(image);
			}
			else {
				//Not a Jolly
				ImageIcon image = new ImageIcon(ImageIO.read(
						new File("src/main/resources/PolCard.png")).getScaledInstance(width, height, Image.SCALE_SMOOTH));
				this.setIcon(image);
				this.setBackground(politicsCard.getColor());
			}
		} catch (IOException e) {
			System.out.println("Failed while loading politics card resource!");
			e.printStackTrace();
		}
	}

	/**
	 * Alternative constructor used to build a label of a fixed size
	 * @param politicsCard the politics card used to build the label
	 */
	public PoliticsCardLabel(DTOPoliticsCard politicsCard) {
		this(politicsCard,65,90);
	}
}
