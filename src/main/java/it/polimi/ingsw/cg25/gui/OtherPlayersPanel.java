package it.polimi.ingsw.cg25.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import it.polimi.ingsw.cg25.model.dto.DTOPlayerCD4;
import it.polimi.ingsw.cg25.model.dto.GameStatus;

@SuppressWarnings("serial")
public class OtherPlayersPanel extends JLayeredPane implements Updatable<GameStatus> {

	/**
	 * Scroll Pane to obtain an extended panel
	 */
	private JScrollPane scrollPlayer;
	/**
	 * This is the panel inside scrollPlayer
	 */
	private PlayersContainer playersContainer;
	/**
	 * Initial update ? first update : generic update
	 */
	private boolean initSet = true;
	/**
	 * Panel width
	 */
	private int w;
	/**
	 * Panel height
	 */
	private int h;

	/**
	 * OtherPlayersPanel class constructor
	 * @param x the horizontal position of the panel
	 * @param y the vertical position of the panel
	 * @param w the width of the panel
	 * @param h the height of the panel
	 * @param font the font to be used within this panel
	 */
	public OtherPlayersPanel(int x, int y, int w, int h, Font font) {
		this.setLayout(null);
		this.setBounds(x, y, w, h);
		this.setOpaque(false);
		this.w = w;
		this.h = h;
		
		JPanel backPanel = new JPanel();
		backPanel.setOpaque(false);
		backPanel.setLayout(new GridBagLayout());
		backPanel.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.add(backPanel);
		this.setLayer(backPanel, 5);
		
		JLabel label = new JLabel();
		label.setText("Other Players");
		label.setFont(new Font(font.getName(), Font.BOLD, 25));
		backPanel.add(label);
		
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		this.setBorder(border);
		
		playersContainer = new PlayersContainer(font);
	}
	
	/**
	 * This method loads the structure of this panel. This JLayeredPane contains a
	 * JScrollPane with playersContainer as parameter
	 * @param otherPlayers the List of other players to create the object
	 */
	private void load(List<DTOPlayerCD4> otherPlayers) {
		for(DTOPlayerCD4 pl : otherPlayers)
			playersContainer.addPlayer(pl);
			
		playersContainer.setPreferredSize(new Dimension(221, 135*otherPlayers.size() + 2));
		
		scrollPlayer = new JScrollPane(playersContainer);
		scrollPlayer.setVisible(true);
		scrollPlayer.setOpaque(false);
		scrollPlayer.getViewport().setOpaque(false);
		scrollPlayer.setBounds(0, 0, w, h);
		scrollPlayer.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(scrollPlayer);
		this.setLayer(scrollPlayer, 10);
	}
	
	@Override
	public void update(GameStatus object) {
		if(initSet) {
			load(object.getOtherPlayers());
			initSet = false;
		}
		else {
			for(int i = 0; i < object.getOtherPlayers().size(); i++) {
				playersContainer.getPlayersPanes().get(i).updateString(object.getOtherPlayers().get(i));
			}
		}
		this.repaint();
	}
	
}
