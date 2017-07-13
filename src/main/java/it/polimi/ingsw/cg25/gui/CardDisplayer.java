package it.polimi.ingsw.cg25.gui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import it.polimi.ingsw.cg25.model.dto.DTOPermitCard;
import it.polimi.ingsw.cg25.model.dto.DTOPoliticsCard;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;
/**
 * 
 * @author nicolo
 *
 */
@SuppressWarnings("serial")
public class CardDisplayer extends JDialog {

	/**
	 * The scrollPane that contains the elements to be displayed
	 */
	private final JScrollPane scrollPane;
	/**
	 * The layered pane where the content is actually put
	 */
	private final JLayeredPane contentPane;
	
	/**
	 * Constructor for the CardDisplayer
	 * @param title is the title of the JDialog
	 */
	private CardDisplayer(String title) {
		this.setSize(330, 330);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setModal(true);
		this.setLayout(null);
		
		contentPane = new JLayeredPane();
		this.scrollPane = new JScrollPane(this.contentPane);
		
		JLayeredPane pane = new JLayeredPane();
		pane.setOpaque(false);
		pane.setBounds(0, 0, this.getWidth(), this.getHeight());
		this.setContentPane(pane);
		JLabel background = new JLabel();
		
		try {
			background.setIcon(new ImageIcon(ImageIO.read(new File("src/main/resources/Old_paper1.png")).
					getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH)));
			this.add(background);
			background.setBounds(0, 0, background.getIcon().getIconWidth(), background.getIcon().getIconHeight());
			pane.setLayer(background, 0);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.scrollPane.setBounds(0, 0, pane.getWidth() - 6, pane.getHeight() - 11);
		this.scrollPane.setBorder(null);
		this.setTitle(title);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		contentPane.setOpaque(false);
		pane.add(scrollPane, BorderLayout.CENTER);
		pane.setLayer(scrollPane, 3);
	}
	
	/**
	 * This method initialize the displayer with the components toDisplay
	 * @param toDisplay is the list of components that have to be displayed
	 */
	private void init(List<JComponent> toDisplay) {
		int currentWidth = 5;
		int currentHeight = 5;
		int inBetweenDistance = 10;
		
		for(int i=0; i<toDisplay.size(); i++) {
			if(currentWidth+toDisplay.get(i).getWidth()>this.getWidth()-5) {
				currentWidth = 5;
				currentHeight+=toDisplay.get(i).getHeight()+5;
			}
			toDisplay.get(i).setBounds(currentWidth,currentHeight,toDisplay.get(i).getWidth(),toDisplay.get(i).getHeight());
			currentWidth+=toDisplay.get(i).getWidth()+inBetweenDistance;
			contentPane.add(toDisplay.get(i));
			this.contentPane.setSize(new Dimension(330,currentHeight+toDisplay.get(0).getHeight()+20));
			this.contentPane.setLayer(toDisplay.get(i), 3);
			this.contentPane.setPreferredSize(new Dimension(330,currentHeight+toDisplay.get(0).getHeight()+20));
			contentPane.repaint();
		}
		repaint();
	}
	
	/**
	 * This constructor is used to display the player's politics cards
	 * @param cards a List of DTO politics cards to display
	 */
	public CardDisplayer(List<DTOPoliticsCard> cards) {
		this("Your Politics Cards");
		List<JComponent> toDisplay = new ArrayList<>();
		for(DTOPoliticsCard c : cards){
			toDisplay.add(new PoliticsCardLabel(c));
		}
		this.init(toDisplay);
		this.setVisible(true);
	}
	
	/**
	 * This class constructor is used in order to display the current player's hand
	 * of permit cards
	 * @param cards the permit cards to display
	 * @param gui a reference to a CoFGui, which is our main gui window
	 */
	public CardDisplayer(List<DTOPermitCard> cards, Font guiFont) {
		this("Your Permit Cards");
		List<JComponent> toDisplay = new ArrayList<>();
		for(DTOPermitCard c : cards) {
			PermitPane permitPanel = new PermitPane(0, 0, guiFont);
			permitPanel.update(c);
			toDisplay.add(permitPanel);		
		}
		this.init(toDisplay);
		this.setVisible(true);
	}
	
	/**
	 * This kind of class constructor is used in order to create a window
	 * and display all the bonuses of a clickable object that has many of them
	 * @param guiFont the font used in this gui
	 * @param bonuses the List of DTO bonuses
	 */
	public CardDisplayer(Font guiFont, List<DTOBonus> bonuses) {
		this("Bonuses");
		List<JComponent> toDisplay = new ArrayList<>();
		for(DTOBonus b : bonuses)
		{
			BonusPanel bp = new BonusPanel(b, guiFont);
			toDisplay.add(bp);
		}
		this.init(toDisplay);
		this.setVisible(true);
	}
	
}
