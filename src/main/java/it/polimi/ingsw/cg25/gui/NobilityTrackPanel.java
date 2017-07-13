package it.polimi.ingsw.cg25.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import it.polimi.ingsw.cg25.model.dto.DTONobilityCell;
import it.polimi.ingsw.cg25.model.dto.GameStatus;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class NobilityTrackPanel extends JLayeredPane implements Updatable<GameStatus> {
	
	/**
	 * Scroll Pane to obtain an extended panel
	 */
	private JScrollPane scrollTrack;
	/**
	 * This panel is located inside scrollTrack
	 */
	private NobTrackContainer nobTrackContainer;
	/**
	 * The font to be used for any writing
	 */
	private final Font font;
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
	 * NobilityTrackPanel class constructor
	 * @param x the horizontal position of the panel
	 * @param y the vertical position of the panel
	 * @param w the width of the panel
	 * @param h the height of the panel
	 * @param font the font to be used with any writing
	 * @throws IOException if a problem while loading resources occurres
	 */
	public NobilityTrackPanel(int x, int y, int w, int h, Font font) throws IOException {
		this.font = font;
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
		label.setText("Nobility Track");
		label.setFont(new Font(font.getName(), Font.BOLD, 25));
		backPanel.add(label);
		
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		this.setBorder(border);
		
		nobTrackContainer = new NobTrackContainer();
	}

	/**
	 * This method is used to load the structure of this panel. It's composed by
	 * a JScrollPane with this.nobilityTrack in it
	 * @param nobilityTrack the List of DTO nobility cells used to create this component
	 */
	private void load(List<DTONobilityCell> nobilityTrack) {
		for(DTONobilityCell nc : nobilityTrack)
			nobTrackContainer.addCell(nc,font);
		
		scrollTrack = new JScrollPane(nobTrackContainer);
		scrollTrack.setVisible(true);
		scrollTrack.setOpaque(false);
		scrollTrack.getViewport().setOpaque(false);
		scrollTrack.setBounds(0, 0, w, h);
		scrollTrack.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(scrollTrack);
		this.setLayer(scrollTrack, 10);
	}
	
	@Override
	public void update(GameStatus object) {
		if(initSet) {
			load(object.getBoard().getNobilityTrack());
			initSet = false;
			this.repaint();
		}
	}
	
	/**
	 * @return a reference to the nobility track container
	 */
	public JPanel getNobTrackContainer() {
		return nobTrackContainer;
	}
	
}
