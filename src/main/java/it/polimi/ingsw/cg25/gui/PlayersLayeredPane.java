package it.polimi.ingsw.cg25.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.border.Border;

import it.polimi.ingsw.cg25.model.dto.DTOPlayerCD4;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class PlayersLayeredPane extends JLayeredPane {

	/**
	 * The background image of the label
	 */
	private Image paperVector;
	/**
	 * The string representing player's statistics
	 */
	private String vectorString;
	/**
	 * JLabel with all the player's statistics
	 */
	private JLabel playerStat;
	
	/**
	 * PlayerLabel class constructor
	 * @param player the DTO player used to build the label
	 * @param font the font to be used for any writing
	 * @throws IOException if something goes wrong in loading resources
	 */
	public PlayersLayeredPane(DTOPlayerCD4 player, Font font, int yPos) throws IOException {
		loadResources();
		//Initialize the panel
		this.setBounds(0, yPos, 220, 135);
		this.setOpaque(false);
		this.setLayout(null);
		
		//Put a label inside the panel
		playerStat = new JLabel();
		Image scaledVector = paperVector.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
		playerStat.setIcon(new ImageIcon(scaledVector));
		vectorString = createString(player);
		playerStat.setBounds(0, 0, this.getWidth(), this.getHeight());
		playerStat.setHorizontalTextPosition(JLabel.CENTER);
		playerStat.setVerticalTextPosition(JLabel.CENTER);
		playerStat.setText(vectorString);
		playerStat.setFont(new Font(font.getFontName(), Font.PLAIN, 15));
		playerStat.setForeground(Color.BLACK);
		//Add the label and set the layer level
		this.add(playerStat);
		this.setLayer(playerStat, 1);
		
		//Add the color box to the panel
		JLabel color = new JLabel();
		color.setBounds(105, 114, 12, 12);
		color.setOpaque(true);
		color.setBackground(player.getPlayerColor());
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		color.setBorder(border);
		this.add(color);
		this.setLayer(color, 10);
	}
	
	/**
	 * This method loads digital resources for this layered panel
	 * @throws IOException if something goes wrong while loading
	 */
	private void loadResources() throws IOException {
		paperVector = ImageIO.read(new File("src/main/resources/paperVector.png"));
	}
	
	/**
	 * This method creates a string with the whole player's statistics
	 * @param player the DTO player used to create the string
	 * @return the string just mentioned above
	 */
	private String createString(DTOPlayerCD4 player) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>" + player.getName() + "<br/>");
		sb.append("Victory Points: " + player.getPocket().getVictoryPoints() + "<br/>");
		sb.append("Nobility Points: " + player.getPocket().getNobilityRank() + "<br/>");
		sb.append("Coins: " + player.getPocket().getCoins()+ "<br/>");
		sb.append("Assistants: " + player.getPocket().getAssistants() + "<br/>");
		sb.append("Color:</html>");
		
		return sb.toString();
	}
	
	/**
	 * Update the string linked to the player's label contained in this panel
	 * @param player the DTO player used to build and update the string
	 */
	public void updateString(DTOPlayerCD4 player) {
		playerStat.setText(createString(player));
	}
	
}
