package it.polimi.ingsw.cg25.gui;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

import it.polimi.ingsw.cg25.model.dto.DTOPlayerCD4;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class PlayerPanel extends JLayeredPane implements Updatable<DTOPlayerCD4> {

	/**
	 * Progress bar width
	 */
	private static final int PROG_WIDTH = 160;
	/**
	 * Progress bar height
	 */
	private static final int PROG_HEIGTH = 20;
	/**
	 * Spece between progress bars
	 */
	private static final int PROG_SPACING = 45;
	/**
	 * Background layer level
	 */
	private static final int BACK_LAYER = 0;
	/**
	 * Components layer level
	 */
	private static final int COMP_LAYER = 1;
	/**
	 * Image for the background
	 */
	private Image paperVectorPL;
	/**
	 * Victory icon image
	 */
	private Image victoryIcon;
	/**
	 * Nobility icon image
	 */
	private Image nobilityIcon;
	/**
	 * Coin icon image
	 */
	private Image coinIcon;
	/**
	 * Assistant icon image
	 */
	private Image asIcon;
	/**
	 * Action button
	 */
	private JButton mainAct = new JButton("Actions");
	/**
	 * Perit cards button
	 */
	private JButton permits = new JButton("Permit Cards");
	/**
	 * Politics card button
	 */
	private JButton politics = new JButton("Politics Cards");
	/**
	 * The current player
	 */
	private DTOPlayerCD4 me;
	/**
	 * Initial setup ? first update : generic update
	 */
	private boolean initSet = true;
	/**
	 * Progress bars for player's statistics
	 */
	private final List<PlayerProgBar> bars;
	
	/**
	 * PlayerPanel class constructor initializes the player panel
	 * @param x the horizontal position of the panel
	 * @param y the vertical position of the panel
	 * @param w the width of the panel
	 * @param h the height of the panel
	 * @param font the font for any writing
	 * @param gui the gui reference
	 */
	public PlayerPanel(int x, int y, int w, int h, Font font, CoFGui gui) {
		this.setLayout(null);
		//this.gui = gui;
		this.setBounds(x, y, w, h);
		
		try {
			loadResources();
		} catch (IOException e) {
			System.out.println("Failed while loading player's resources!");
		}
		
		Image scaledPVector = paperVectorPL.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		JLabel paperBack = new JLabel(new ImageIcon(scaledPVector));
		paperBack.setBounds(0, 0, paperBack.getIcon().getIconWidth(), paperBack.getIcon().getIconHeight());
		this.add(paperBack);
		this.setLayer(paperBack, BACK_LAYER);
		
		Image scaledVicIcon = victoryIcon.getScaledInstance(28, 37, Image.SCALE_SMOOTH);
		JLabel vic = new JLabel(new ImageIcon(scaledVicIcon));
		vic.setBounds(20, 36, vic.getIcon().getIconWidth(), vic.getIcon().getIconHeight());
		this.add(vic);
		this.setLayer(vic, COMP_LAYER);
		
		Image scaledNobIcon = nobilityIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		JLabel nob = new JLabel(new ImageIcon(scaledNobIcon));
		nob.setBounds(20, 85, nob.getIcon().getIconWidth(), nob.getIcon().getIconHeight());
		this.add(nob);
		this.setLayer(nob, COMP_LAYER);
		
		Image scaledCoinIcon = coinIcon.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		JLabel coin = new JLabel(new ImageIcon(scaledCoinIcon));
		coin.setBounds(20, 129, coin.getIcon().getIconWidth(), coin.getIcon().getIconHeight());
		this.add(coin);
		this.setLayer(coin, COMP_LAYER);
		
		Image scaledAsIcon = asIcon.getScaledInstance(20, 43, Image.SCALE_SMOOTH);
		JLabel as = new JLabel(new ImageIcon(scaledAsIcon));
		as.setBounds(25, 170, as.getIcon().getIconWidth(), as.getIcon().getIconHeight());
		this.add(as);
		this.setLayer(as, COMP_LAYER);
		
		bars = new ArrayList<>();
		
		for (int i = 0; i < 4; i++) {
			PlayerProgBar bar = new PlayerProgBar(64, 45 + i*PROG_SPACING, PROG_WIDTH, PROG_HEIGTH);
			bar.setValue(0);
			bars.add(bar);
			this.add(bar);
			this.setLayer(bar, COMP_LAYER);
		}
		
		JLabel player = new JLabel();
		player.setBounds(182, 4, 50, 25);
		player.setText("Player");
		player.setFont(new Font(gui.getFont().getFontName(), Font.PLAIN, 18));
		this.add(player);
		this.setLayer(player, COMP_LAYER);
		
		JLabel color = new JLabel();
		color.setBounds(25, 222, 50, 25);
		color.setText("Color:");
		color.setFont(new Font(gui.getFont().getFontName(), Font.PLAIN, 16));
		this.add(color);
		this.setLayer(color, COMP_LAYER);
		mainAct.setBounds(125, 220, 100, 25);
		mainAct.setFont(new Font(gui.getFont().getFontName(), Font.PLAIN, 13));
		mainAct.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.getChoiceFrame().display();
				
			}
			
		});
		this.add(mainAct);
		this.setLayer(mainAct, 1);
		mainAct.setMnemonic('a');
		politics.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(me != null && me.getHand()!=null && me.getHand().size()!=0)
					new CardDisplayer(me.getHand());
				else
					JOptionPane.showMessageDialog(null, "No Politics Cards to display", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		});
		politics.setBounds(20, 255, 100, 25);
		politics.setFont(new Font(gui.getFont().getFontName(), Font.PLAIN, 13));
		politics.setMnemonic('p');
		this.add(politics);
		this.setLayer(politics, 1);
		
		permits.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(me != null && me.getPermitsToBeUsed()!=null && me.getPermitsToBeUsed().size()!=0)
					new CardDisplayer(me.getPermitsToBeUsed(), gui.getFont());
				else
					JOptionPane.showMessageDialog(null, "No Permit Cards to display", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		});
		permits.setBounds(125, 255, 100, 25);
		permits.setFont(new Font(gui.getFont().getFontName(), Font.PLAIN, 13));
		permits.setMnemonic('c');
		this.add(permits);
		this.setLayer(permits, 1);
	}
	
	/**
	 * This method loads all the graphics resources
	 * @throws IOException if some resources are missing
	 */
	private void loadResources() throws IOException {
		victoryIcon = ImageIO.read(new File("src/main/resources/vicIcon.png"));
		nobilityIcon = ImageIO.read(new File("src/main/resources/nobIcon.png"));
		coinIcon = ImageIO.read(new File("src/main/resources/coinIcon.png"));
		asIcon = ImageIO.read(new File("src/main/resources/asIcon.png"));
		paperVectorPL = ImageIO.read(new File("src/main/resources/paperVectorPL.png"));
	}

	@Override
	public void update(DTOPlayerCD4 object) {
		this.me = object;
		if(initSet) {
			JLabel colorBox = new JLabel();
			colorBox.setBounds(75, 223, 20, 20);
			colorBox.setOpaque(true);
			colorBox.setBackground(object.getPlayerColor());
			this.add(colorBox);
			this.setLayer(colorBox, COMP_LAYER);
			initSet = false;
		}
		bars.get(0).setValue(object.getPocket().getVictoryPoints());
		bars.get(1).setValue(object.getPocket().getNobilityRank());
		bars.get(2).setValue(object.getPocket().getCoins());
		bars.get(3).setValue(object.getPocket().getAssistants());
		
		this.repaint();
	}
	
}
