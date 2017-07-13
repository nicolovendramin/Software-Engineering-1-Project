package it.polimi.ingsw.cg25.gui;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import it.polimi.ingsw.cg25.model.dto.DTOPermitCard;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class PermitPane extends JLayeredPane implements Updatable<DTOPermitCard> {

	/**
	 * The string of bonuses of the card
	 */
	private String bonuses;
	/**
	 * The background label
	 */
	private JLabel background;
	/**
	 * The label with the starting letter of the cities
	 */
	private JLabel whereToBuild;
	/**
	 * A Panel which contains whereToBuild
	 */
	private JPanel foreground;
	/**
	 * Initial update ? first update : generic update
	 */
	private boolean initSet = true;
	/**
	 * The list of cities where you can build with this card
	 */
	private String cities;
	/**
	 * Label background for the card
	 */
	private ImageIcon icon;
	/**
	 * If there are no more cards to draw, we use this icon
	 */
	private ImageIcon iconEmpty;
	/**
	 * The font to use for any writing
	 */
	private Font font;
	
	/**
	 * PermitPane class constructor
	 * @param x the horizontal position of this panel
	 * @param y the vertical position of this panel
	 * @param font the font used in this panel. It's the same of all the other gui panels
	 */
	public PermitPane(int x, int y, Font font) {
		this.setLayout(null);
		this.setBounds(x, y, 65, 65);
		this.setOpaque(true);
		this.font = font;
		try {
			loadResources();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		background = new JLabel();
		background.setIcon(icon);
		background.setBounds(0, 0, background.getIcon().getIconWidth(), background.getIcon().getIconHeight());
		
		this.add(background);
		this.setLayer(background, 0);
		
		foreground = new JPanel();
		foreground.setLayout(new GridBagLayout());
		foreground.setBounds(8, 10, 50, 20);
		foreground.setOpaque(false);
		this.add(foreground);
		this.setLayer(foreground, 10);
		
		whereToBuild = new JLabel();
		whereToBuild.setFont(new Font(font.getFontName(), Font.BOLD, 14));
		foreground.add(whereToBuild);
	}
	
	/**
	 * Load the needed resources
	 * @throws IOException if any resource is missing
	 */
	private void loadResources() throws IOException {
		icon = new ImageIcon(ImageIO.read(new File("src/main/resources/permitTop.png")).getScaledInstance(
					this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));
		iconEmpty = new ImageIcon(ImageIO.read(new File("src/main/resources/permitback.png")).getScaledInstance(
					this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH));
	}

	/**
	 * Set the icon when there are no more permits
	 * @param empty true or false. False do nothing
	 */
	public void setEmpty(boolean empty) {
		if(empty) {
			background.setIcon(iconEmpty);
			this.bonuses = createString(null);
		}
	}
	
	@Override
	public void update(DTOPermitCard object) {
		//Always update bonus and cities
		this.bonuses = createString(object);
		this.background.setToolTipText(bonuses);
		cities = object.getCities().get(0).getName().substring(0, 1).toUpperCase();
		for(int i = 1; i < object.getCities().size(); i++)
			cities = cities.concat(" ").concat(object.getCities().get(i).getName().substring(0, 1).toUpperCase());
		
		if(initSet) {
			this.background.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					//Do nothing
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					//Do nothing
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					background.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					background.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					new CardDisplayer(font, object.getBonuses());
				}
			});
			initSet = false;
		}
		
		whereToBuild.setText(cities);
		this.repaint();
	}
	
	/**
	 * Create the bonus string
	 * @param pc the DTO permit card to analize
	 * @return the string containing the bonuses of the card, null if there are no bonuses
	 */
	private String createString(DTOPermitCard pc) {
		StringBuilder sb = new StringBuilder();
		if(pc == null)
			sb.append("No available cards");
		else {
			if(pc.getBonuses().isEmpty())
				return null;
			else {
				sb.append("BONUS: ");
				for(DTOBonus b : pc.getBonuses()) {
					sb.append(b + ", ");
				}
				sb.setLength(sb.length() - 2);
			}
		}
		return sb.toString();
	}

	/**
	 * Alternative constructor used to display cards, as instance, in card displayer
	 * @param font the font to use
	 * @param object the permit card to build the object
	 */
	public PermitPane(Font font, DTOPermitCard object) {
		this.setLayout(null);
		this.setBounds(0, 0, 42, 42);
		this.setOpaque(true);
		this.font = font;
		try {
			loadResources();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		background = new JLabel();
		background.setIcon(icon);
		background.setBounds(0, 0, background.getIcon().getIconWidth(), background.getIcon().getIconHeight());
		
		this.add(background);
		this.setLayer(background, 0);
		
		foreground = new JPanel();
		foreground.setLayout(new GridBagLayout());
		foreground.setBounds(3, 3, 36, 20);
		foreground.setOpaque(false);
		this.add(foreground);
		this.setLayer(foreground, 10);
		
		whereToBuild = new JLabel();
		whereToBuild.setFont(new Font(font.getFontName(), Font.BOLD, 12));
		foreground.add(whereToBuild);
		
		this.background.setToolTipText(object.getCities().toString());
		cities = object.getCities().get(0).getName().substring(0, 1).toUpperCase();
		for(int i = 1; i < object.getCities().size(); i++)
			cities = cities.concat(" ").concat(object.getCities().get(i).getName().substring(0, 1).toUpperCase());
		whereToBuild.setText(cities);
		this.repaint();
	}
	
}
