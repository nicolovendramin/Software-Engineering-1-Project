package it.polimi.ingsw.cg25.gui;

import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import it.polimi.ingsw.cg25.model.dto.DTOEmporium;
import it.polimi.ingsw.cg25.model.dto.DTORegion;
import it.polimi.ingsw.cg25.model.dto.GameStatus;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class MapPanel extends JLayeredPane implements Updatable<GameStatus> {
	
	/**
	 * Background layer level
	 */
	private static final int BACK_LABEL_LAYER = 0;
	/**
	 * Layer level used for regions' labels and permit cards
	 */
	private static final int MAP_LABEL_LAYER = 1;
	/**
	 * Layer level used for the map image
	 */
	private static final int MAP_LAYER = 2;
	/**
	 * Layer level used by the cities placed on the map image
	 */
	private static final int COMP_LAYER = 3;
	/**
	 * Layer lavel for emporiums and colors' labels
	 */
	private static final int COL_EMP_LAYER = 4;
	/**
	 * Region panel width
	 */
	private static final int REG_CONST_X = 244;
	/**
	 * Region panel height
	 */
	private static final int REG_CONST_Y = 40;
	/**
	 * Map image
	 */
	private Image map;
	/**
	 * Crown image
	 */
	private Image crown;
	/**
	 * Label for the crown
	 */
	private JLabel crw;
	/**
	 * The font to be used in any writing
	 */
	private final Font font;
	/**
	 * Regions' panels
	 */
	private final List<RegionTextPanel> regionPanels;
	/**
	 * A List of city colors' labels
	 */
	private final List<CityColorLabel> cityColLabels;
	/**
	 * A List of permit cards' panes
	 */
	private final List<PermitPane> permitPanes;
	/**
	 * Initial update ? first update : generic update
	 */
	private boolean initSet = true;
	
	/**
	 * Points where cities must be placed. Follow the order in "cities"
	 * configuration file
	 */
	private static final CityPoint[] points = {
			new CityPoint(87, 48), new CityPoint(276, 45), new CityPoint(330, 242), 
			new CityPoint(500, 45), new CityPoint(610, 162), new CityPoint(185, 85), 
			new CityPoint(112, 238), new CityPoint(416, 178), new CityPoint(500, 130), 
			new CityPoint(113, 150), new CityPoint(385, 48), new CityPoint(600, 70),
			new CityPoint(215, 199), new CityPoint(490, 228), new CityPoint(290, 139)};
	
	/**
	 * MapPanel class constructor
	 * @param x the horizontal position of the panel
	 * @param y the vertical position of the panel
	 * @param w the width of the panel
	 * @param h the height of the panel
	 * @param font the font to use for any writing
	 */
	public MapPanel(int x, int y, int w, int h, Font font) {
		this.setLayout(null);
		this.setBounds(x, y, w, h);
		this.font = font;
		
		this.regionPanels = new ArrayList<>();
		this.cityColLabels = new ArrayList<>();
		this.permitPanes = new ArrayList<>();
		
		//Background label
		JLabel backLabel = new JLabel();
		backLabel.setBounds(255, 225, 225, 30);
		backLabel.setFont(new Font(font.getName(), Font.BOLD, 30));
		backLabel.setText("The Council of Four");
		this.add(backLabel);
		this.setLayer(backLabel, BACK_LABEL_LAYER);
		
		permitPanes.add(new PermitPane(53, 410, font));
		permitPanes.add(new PermitPane(123, 410, font));
		permitPanes.add(new PermitPane(297, 410, font));
		permitPanes.add(new PermitPane(367, 410, font));
		permitPanes.add(new PermitPane(541, 410, font));
		permitPanes.add(new PermitPane(611, 410, font));
		
		for(PermitPane pp : permitPanes) {
			this.add(pp);
			this.setLayer(pp, MAP_LABEL_LAYER);
		}
		
		try {
			crown = ImageIO.read(new File("src/main/resources/crown.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		crw = new JLabel(new ImageIcon(crown.getScaledInstance(25, 18, Image.SCALE_SMOOTH)));
		crw.setSize(crw.getIcon().getIconWidth(), crw.getIcon().getIconHeight());
	}
	
	/**
	 * This method loads the correct map and cities
	 * @param gs a GameStatus object to parse
	 * @throws IOException if the map file is missing
	 */
	private void mapLoader(GameStatus gs) throws IOException {
		//If Arkon-Burgen is not present
		if(!gs.getBoard().getRoadNetwork().containsEdge(gs.getBoard().getCities().get(0),
				gs.getBoard().getCities().get(9))) {
			map = ImageIO.read(new File("src/main/resources/map1s.png"));
		}
		else {
			//If Indur-Juvelar is present
			if(gs.getBoard().getRoadNetwork().containsEdge(gs.getBoard().getCities().get(7),
					gs.getBoard().getCities().get(10))) {
				map = ImageIO.read(new File("src/main/resources/map2s.png"));
			}
			else map = ImageIO.read(new File("src/main/resources/map3s.png"));
		}
		
		Image scaledMap = map.getScaledInstance(this.getWidth(), 360, Image.SCALE_SMOOTH);
		JLabel map = new JLabel(new ImageIcon(scaledMap));
		map.setBounds(0, REG_CONST_Y, map.getIcon().getIconWidth(), map.getIcon().getIconHeight());
		this.add(map);
		this.setLayer(map, MAP_LAYER);
		
		for(int i = 0; i < gs.getBoard().getCities().size(); i++) {
			//Add city
			CityLabel city = new CityLabel(points[i].getX(), points[i].getY(), 
					gs.getBoard().getCities().get(i), this.font);
			this.add(city);
			this.setLayer(city, COMP_LAYER);
			//Add city color
			CityColorLabel cc = new CityColorLabel(points[i].getX() + 75, points[i].getY() + 5,font);
			this.cityColLabels.add(cc);
			this.add(cc);
			this.setLayer(cc, COL_EMP_LAYER);
		}
	}

	@Override
	public void update(GameStatus object) {
		if(initSet) {
			for(int i = 0; i < object.getBoard().getRegions().size(); i++) {
				regionPanels.add(new RegionTextPanel(0 + i*REG_CONST_X, 0, REG_CONST_X, REG_CONST_Y, 
						object.getBoard().getRegions().get(i), font));
				this.add(regionPanels.get(i));
				this.setLayer(regionPanels.get(i), MAP_LABEL_LAYER);
			}
			try {
				mapLoader(object);
			} catch (IOException e) {
				System.out.println("Failed while loading the map!");
				e.printStackTrace();
			}
			this.add(crw);
			this.setLayer(crw, COL_EMP_LAYER + 1);
			
			initSet = false;
		}
		//Do always - update color and emporiums - update permit cards
		for(int i = 0; i < cityColLabels.size(); i++) {
			cityColLabels.get(i).update(object.getBoard().getCities().get(i).getColor());
			int index = 0;
			for(DTOEmporium emp : object.getBoard().getCities().get(i).getEmporiums()) {
				CityEmporiums em = new CityEmporiums(points[i].getX() - 8, points[i].getY() + index + 12, emp);
				index += 8;
				this.add(em);
				this.setLayer(em, COL_EMP_LAYER);
			}
		}
		
		//Handles the case when faced up permits are less than 2 per region
		int index = 0;
		for(DTORegion r : object.getBoard().getRegions()) {
			if(r.getFaceUpPermits().size() == 0) {
				permitPanes.get(index).setEmpty(true);
				permitPanes.get(index + 1).setEmpty(true);
				index += 2;
			}
			else if(r.getFaceUpPermits().size() == 1) {
				permitPanes.get(index).update(r.getFaceUpPermits().get(0));
				permitPanes.get(index + 1).setEmpty(true);
				index += 2;
			}
			else if(r.getFaceUpPermits().size() == 2) {
				permitPanes.get(index).update(r.getFaceUpPermits().get(0));
				permitPanes.get(index + 1).update(r.getFaceUpPermits().get(1));
				index += 2;
			}
			else throw new IllegalArgumentException("An error occured while setting up permit cards!");
		}
		
		//Set the crown position
		int kingIndex = object.getBoard().getCities().indexOf(object.getBoard().getKingCity());
		crw.setLocation(points[kingIndex].getX() + 25, points[kingIndex].getY() + 70);
		
		//Update regions' bonus
		for(int i = 0; i < regionPanels.size(); i++)
			regionPanels.get(i).update(object.getBoard().getRegions().get(i));
		
		this.repaint();
	}
	
}
