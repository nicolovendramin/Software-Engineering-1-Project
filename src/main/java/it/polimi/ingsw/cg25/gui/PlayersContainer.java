package it.polimi.ingsw.cg25.gui;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import it.polimi.ingsw.cg25.model.dto.DTOPlayerCD4;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class PlayersContainer extends JPanel {

	/**
	 * The List of players' panes. One for each other player
	 */
	private final List<PlayersLayeredPane> playersPanes;
	/**
	 * The font to be used
	 */
	private final Font font;
	/**
	 * Vertical position of the next player's pane
	 */
	private int yPos = 0;
	
	/**
	 * PlayersContainer class constructor
	 * @param font the font to be used for any writing
	 */
	public PlayersContainer(Font font) {
		//Align labels vertically
		this.setLayout(null);
		this.setBounds(0, 0, 240, 516);
		this.setOpaque(false);
		playersPanes = new ArrayList<>();
		this.font = font;
	}
	
	/**
	 * This method add a panel in playersPanes List starting from a DTO player
	 * @param playerToAdd the DTO player to be turned into a player label
	 */
	public void addPlayer(DTOPlayerCD4 playerToAdd) {
		try {
			PlayersLayeredPane pPane = new PlayersLayeredPane(playerToAdd, font, yPos);
			this.add(pPane);
			playersPanes.add(pPane);
			yPos += 135;
		} catch (IOException e) {
			System.out.println("Failed while adding a player!");
			e.printStackTrace();
		}
		this.repaint();
	}

	/**
	 * @return the List of players' layered panes
	 */
	public List<PlayersLayeredPane> getPlayersPanes() {
		return playersPanes;
	}
	
}
