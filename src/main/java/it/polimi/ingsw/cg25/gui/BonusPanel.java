package it.polimi.ingsw.cg25.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOAssistantBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOCoinBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTODrawPoliticsCardBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTONobilityPointBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOPermitCardBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOReusePermitBonus;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOVictoryPointBonus;
/**
 * 
 * @author nicolo
 *
 */
@SuppressWarnings("serial")
public class BonusPanel extends JPanel {
	
	/**
	 * This is the graphical representation of the bonus
	 */
	private JLayeredPane bonusGraphic = new JLayeredPane();
	
	/**
	 * Constructor the punel 
	 * @param font is the font to be used for the texts
	 * @param bonusQuantity the amount of the bonus
	 */
	public void buildPanel(Font font, int bonusQuantity) {
		this.setBounds(0,0,100,48);
		this.setLayout(null);
		this.bonusGraphic.setOpaque(false);
		this.setOpaque(false);
		bonusGraphic.setBounds(5 + (30-bonusGraphic.getWidth())/2, (48-bonusGraphic.getHeight()	)/2,
				bonusGraphic.getWidth(), bonusGraphic.getHeight());
		this.add(bonusGraphic);
		JLabel quantityLabel = new JLabel(Integer.toString(bonusQuantity));
		quantityLabel.setFont(new Font(font.getFontName(),Font.PLAIN,20));
		quantityLabel.setHorizontalAlignment(JLabel.CENTER);
		quantityLabel.setVerticalAlignment(JLabel.CENTER);
		quantityLabel.setForeground(Color.BLACK);
		quantityLabel.setBounds(40, 0, 50, 48);
		this.add(quantityLabel);
	}
	
	/**
	 * Constructor for the BonusPanel basing on the bonus received as input
	 * @param bonus is the bonus to be represented in graphics
	 * @param font is the font to be used for all the texts in this graphic representation
	 */
	public BonusPanel(DTOBonus bonus,Font font){
		if(bonus instanceof DTOVictoryPointBonus)
			victoryPoint(bonus, font);
		else if(bonus instanceof DTOCoinBonus)
			coinBonus(bonus, font);
		else if(bonus instanceof DTOAssistantBonus)
			assistantBonus(bonus, font);
		else if(bonus instanceof DTONobilityPointBonus)
			nobilityBonus(bonus, font);
		else if(bonus instanceof DTODrawPoliticsCardBonus)
			drawPoliticsBonus(bonus, font);
		else if(bonus instanceof DTOPermitCardBonus)
			drawPermitBonus(bonus, font);
		else if(bonus instanceof DTOReusePermitBonus)
			reusePermitBonus(bonus, font);
		else
			getDefaultGraphic(bonus);
		buildPanel(font,bonus.getBnsQnty());
		bonusGraphic.setToolTipText(bonus.toString());
	}
	
	/**
	 * This is the routine when the bonus is a victoryPointBonus
	 * @param bonus is the bonus
	 * @param font is the font to be use for the texts
	 */
	public void victoryPoint(DTOBonus bonus,Font font){
		try {
			JLabel bonusGraphic = new JLabel();
			bonusGraphic.setBounds(0, 0, 28, 37);
			bonusGraphic.setToolTipText(bonus.toString());
			this.bonusGraphic.setBounds(0, 0, 28, 37);
			bonusGraphic.setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/vicIcon.png")).getScaledInstance(28, 37, Image.SCALE_SMOOTH)));
			this.bonusGraphic.add(bonusGraphic);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			getDefaultGraphic(bonus);
		}
	}
	
	/**
	 * This is the routine when the bonus is a assistantBonus
	 * @param bonus is the bonus
	 * @param font is the font to be use for the texts
	 */
	public void assistantBonus(DTOBonus bonus,Font font){
		try {
			JLabel bonusGraphic = new JLabel();
			bonusGraphic.setBounds(0, 0, 20, 43);
			bonusGraphic.setToolTipText(bonus.toString());
			this.bonusGraphic.setBounds(0, 0, 20, 43);
			bonusGraphic.setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/asIcon.png")).getScaledInstance(20, 43, Image.SCALE_SMOOTH)));
			this.bonusGraphic.add(bonusGraphic);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			getDefaultGraphic(bonus);
		}
	}
	
	/**
	 * This is the routine when the bonus is a coinBonus
	 * @param bonus is the bonus
	 * @param font is the font to be use for the texts
	 */
	public void coinBonus(DTOBonus bonus,Font font){
		try {
			JLabel bonusGraphic = new JLabel();
			bonusGraphic.setBounds(0, 0, 30, 30);
			bonusGraphic.setToolTipText(bonus.toString());
			this.bonusGraphic.setBounds(0, 0, 30, 30);
			bonusGraphic.setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/coinIcon.png")).getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
			this.bonusGraphic.add(bonusGraphic);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			getDefaultGraphic(bonus);
		}
	}
	
	/**
	 * This is the routine when the bonus is a nobilityBonus
	 * @param bonus is the bonus
	 * @param font is the font to be use for the texts
	 */
	public void nobilityBonus(DTOBonus bonus,Font font){
		try {
			JLabel bonusGraphic = new JLabel();
			bonusGraphic.setBounds(0, 0, 30, 30);
			bonusGraphic.setToolTipText(bonus.toString());
			this.bonusGraphic.setBounds(0, 0, 30, 30);
			bonusGraphic.setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/nobIcon.png")).getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
			this.bonusGraphic.add(bonusGraphic);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			getDefaultGraphic(bonus);
		}
	}
	
	/**
	 * This is the routine when the bonus is a drawPoliticsBonus
	 * @param bonus is the bonus
	 * @param font is the font to be use for the texts
	 */
	public void drawPoliticsBonus(DTOBonus bonus,Font font){
		try {
			JLabel bonusGraphic = new JLabel();
			bonusGraphic.setBounds(0, 0, 30, 42);
			bonusGraphic.setToolTipText(bonus.toString());
			this.bonusGraphic.setBounds(0, 0, 30, 42);
			bonusGraphic.setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/PolCardJolly.png")).getScaledInstance(30, 42, Image.SCALE_SMOOTH)));
			this.bonusGraphic.add(bonusGraphic);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			getDefaultGraphic(bonus);
		}
	}
	
	/**
	 * This is the routine when the bonus is a reusePermitBonus
	 * @param bonus is the bonus
	 * @param font is the font to be use for the texts
	 */
	public void reusePermitBonus(DTOBonus bonus,Font font){
		try {
			JLabel bonusGraphic = new JLabel();
			bonusGraphic.setBounds(0, 0, 30, 30);
			bonusGraphic.setToolTipText(bonus.toString());
			this.bonusGraphic.setBounds(0, 0, 30, 30);
			bonusGraphic.setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/permitTop.png")).getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
			this.bonusGraphic.add(bonusGraphic);
			this.bonusGraphic.setLayer(bonusGraphic, 1);
			JLabel reuseSymbol = new JLabel();
			reuseSymbol.setBounds(5, 5, 20, 20);
			reuseSymbol.setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/reuse.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
			this.bonusGraphic.add(reuseSymbol);
			this.bonusGraphic.setLayer(reuseSymbol, 10);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			getDefaultGraphic(bonus);
		}
	}
	
	/**
	 * This is the routine when the bonus is a drawPermitBonus
	 * @param bonus is the bonus
	 * @param font is the font to be use for the texts
	 */
	public void drawPermitBonus(DTOBonus bonus,Font font){
		try {
			JLabel bonusGraphic = new JLabel();
			bonusGraphic.setBounds(0, 0, 30, 30);
			bonusGraphic.setToolTipText(bonus.toString());
			this.bonusGraphic.setBounds(0, 0, 30, 30);
			bonusGraphic.setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/permitTop.png")).getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
			this.bonusGraphic.add(bonusGraphic);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			getDefaultGraphic(bonus);
		}
	}
	
	/**
	 * This is the routine when the bonus is another type of bonus for which
	 * we don't have any specific graphic
	 * @param bonus is the bonus
	 * @param font is the font to be use for the texts
	 */
	private void getDefaultGraphic(DTOBonus bonusString){
		try {			
			JLabel bonusGraphic = new JLabel();
			bonusGraphic.setBounds(0, 0, 30, 30);
			bonusGraphic.setToolTipText(bonusString.toString());
			this.bonusGraphic.setBounds(0, 0, 30, 30);
			bonusGraphic.setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/defaultBonusIcon.png")).getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
			this.bonusGraphic.add(bonusGraphic); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JLabel bonusGraphic = new JLabel(bonusString.getClass().toString());
			bonusGraphic.setBounds(0, 0, 30, 30);
			this.bonusGraphic.setBounds(0, 0, 30, 30);
			this.bonusGraphic.add(bonusGraphic);
		}
	}

}
