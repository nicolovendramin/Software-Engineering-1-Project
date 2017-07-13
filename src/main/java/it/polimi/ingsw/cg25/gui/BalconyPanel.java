package it.polimi.ingsw.cg25.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import it.polimi.ingsw.cg25.model.dto.DTOCouncelor;
import it.polimi.ingsw.cg25.model.dto.DTOCouncil;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class BalconyPanel extends JLayeredPane implements Updatable<DTOCouncil> {

	/**
	 * The width of the balcony
	 */
	private static final int BAL_WIDTH = 140;
	/**
	 * The height of the balcony
	 */
	private static final int BAL_HEIGHT = 44;
	/**
	 * The space between councelors
	 */
	private static final int COUN_SPACING = 25;
	/**
	 * The image of the balcony
	 */
	private Image balcony;
	/**
	 * The List of labels which represent councelors
	 */
	private final List<Councelor> councelors;
	
	/**
	 * BalconyPanel class constructor without title and font
	 * @param x the horizontal position of the panel
	 * @param y the vertical position of the panel
	 * @param w the width of the panel
	 * @param h the height of the panel
	 * @throws IOException if a problem occures while loading digital resources
	 */
	public BalconyPanel(int x, int y, int w, int h) throws IOException {
		this(x, y, w, h, 42, null, null);
	}
	
	/**
	 * BalconyPanel class constructor
	 * @param x the horizontal position of the panel
	 * @param y the vertical position of the panel
	 * @param w the width of the panel
	 * @param h the height of the panel
	 * @param sideDistance the horizontal position of the balcony label in the panel
	 * @param title the title given to the balcony label in the panel
	 * @param font the font to be used
	 * @throws IOException if something goes wront while loading digital resources
	 */
	public BalconyPanel(int x, int y, int w, int h, int sideDistance, String title, Font font) throws IOException {
		this.setLayout(null);
		int p = 45;
		if(title==null) 
			p = 0;
		this.setBounds(x, y, w, h+p);
		this.setOpaque(false);
		loadResources();
		
		Image balconyIcon = balcony.getScaledInstance(BAL_WIDTH, BAL_HEIGHT, Image.SCALE_SMOOTH);
		
		JLabel bal = new JLabel();
		bal.setBounds(sideDistance, 30, BAL_WIDTH, BAL_HEIGHT+p);
		bal.setIcon(new ImageIcon(balconyIcon));
		if(p!=0){
			bal.setText(title);
			bal.setVerticalTextPosition(JLabel.BOTTOM);
			bal.setHorizontalTextPosition(JLabel.CENTER);
			this.setFont(new Font(font.getFontName(), Font.PLAIN, 18));
			this.setForeground(Color.BLACK);
			this.setLayer(bal, 2);
		}
		this.add(bal);
		
		councelors = new ArrayList<>();
		
		for (int i = 0; i < 4; i++) {
			Councelor c = new Councelor(sideDistance+20 + i*COUN_SPACING,0);
			councelors.add(c);
			c.setBounds(c.getX(), c.getY()+p/3, c.getWidth(), c.getHeight());
			this.add(c);
			this.setLayer(c, 10);
		}
	}
	
	/**
	 * This method is used to load digital resources used within this class
	 * @throws IOException if something goes wrong while loading
	 */
	private void loadResources() throws IOException {
		balcony = ImageIO.read(new File("src/main/resources/balcony.png"));
	}

	@Override
	public void update(DTOCouncil object) {
		List<DTOCouncelor> invertedCoun = new ArrayList<>(object.getCouncelors());
		//Inverti consiglieri
		Collections.reverse(invertedCoun);
		for(int i = 0; i < this.councelors.size(); i++) {
			this.councelors.get(i).setColor(invertedCoun.get(i).getColor());
		}
	}
	
}
