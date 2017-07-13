package it.polimi.ingsw.cg25.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import it.polimi.ingsw.cg25.model.dto.DTOCity;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class CityLabel extends JLabel {

	/**
	 * First kind of city (image)
	 */
	private Image icon0;
	/**
	 * Second king of city (image)
	 */
	private Image icon1;
	/**
	 * The List of bonuses related to the city represented by this label
	 */
	private final List<DTOBonus> realBonuses;
	
	/**
	 * CityLabel class constructor
	 * @param x the horizontal position of the label
	 * @param y the vertical position of the label 
	 * @param city the DTO city used to build the label
	 * @param font the font used for any writing
	 */
	public CityLabel(int x, int y, DTOCity city, Font font) {
		try {
			loadResources();
		} catch (IOException e) {
			System.out.println("Failed while loading city graphics resources!");
			e.printStackTrace();
		}
		
		//Deterministic icon allocation
		int index = city.getName().hashCode()%2;
		if(index == 0)
			this.setIcon(new ImageIcon(icon0));
		else this.setIcon(new ImageIcon(icon1));
		
		this.setBounds(x, y, this.getIcon().getIconWidth(), this.getIcon().getIconHeight() + 28);
		this.setText(city.getName());
		this.setVerticalTextPosition(JLabel.TOP);
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setFont(new Font(font.getFontName(), Font.PLAIN, 18));
		this.setForeground(Color.WHITE);
		
		//Save city's bonuses
		realBonuses = new ArrayList<>(city.getBonuses());
		//Add mouse listener
		this.addMouseListener(new MouseListener() {
			
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
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				new CardDisplayer(font, realBonuses);
			}
		});
		//Set tooltip text
		this.setToolTipText(createString(city));
	}
	
	/**
	 * Alternative constructor used to create a city label with the city name
	 * of a specific color. It's used while displaying cities in the action window
	 * @param x the horizontal position of the label
	 * @param y the vertical position of the label
	 * @param city the DTO city used to build this label
	 * @param font the font used for any writing
	 * @param color the color the be set as foreground color
	 */
	public CityLabel(int x, int y, DTOCity city, Font font, Color color) {
		this(x, y, city, font);
		this.setForeground(color);
	}
	
	/**
	 * This method loads digital resources used within this class
	 * @throws IOException if an error occurres while loading
	 */
	private void loadResources() throws IOException {
		icon0 = ImageIO.read(new File("src/main/resources/castles/castle1.png"));
		icon1 = ImageIO.read(new File("src/main/resources/castles/castle2.png"));
	}
	
	/**
	 * This method create a string which represents all the bonuses included
	 * in the DTOCity object used to build the label
	 * @param city the DTO city used to build the label
	 * @return the string as mentioned above
	 */
	private String createString(DTOCity city) {
		StringBuilder sb = new StringBuilder();
		
		if(city.getBonuses().isEmpty())
			return null;
		else {
			sb.append("BONUS: ");
			for(DTOBonus b : city.getBonuses()) {
				sb.append(b + ", ");
			}
			sb.setLength(sb.length() - 2);
		}
		return sb.toString();
	}
	
}
