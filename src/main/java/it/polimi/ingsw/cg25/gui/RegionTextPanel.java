package it.polimi.ingsw.cg25.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import it.polimi.ingsw.cg25.model.dto.DTORegion;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class RegionTextPanel extends JPanel implements Updatable<DTORegion> {

	/**
	 * A label with the region's name
	 */
	private final JLabel regionName;
	/**
	 * Initial setup
	 */
	private boolean initSet = true;
	/**
	 * The font to use
	 */
	private final Font font;
	
	/**
	 * RegionTextPanel class constructor
	 * @param x the horizontal position of the panel
	 * @param y the vertical position of the panel
	 * @param w the width of the panel
	 * @param h the height of the panel
	 * @param region the DTORegion object in order to build this panel with its info
	 * @param font the font to be used
	 */
	public RegionTextPanel(int x, int y, int w, int h, DTORegion region, Font font) {
		this.setLayout(new GridBagLayout());
		this.setBounds(x, y, w, h);
		this.setOpaque(false);
		this.setBackground(Color.WHITE);
		this.font = font;
		
		regionName = new JLabel();
		regionName.setFont(new Font(font.getName(), Font.BOLD, 22));
		regionName.setText(region.getName());
		regionName.setToolTipText(createString(region));
		this.add(regionName);
	}
	
	/**
	 * This method creates a string which represents bonuses related to the
	 * reward card linked to a specific region. This string is used to create tooltip text
	 * @param region the DTO region to be represented by this panel
	 * @return the string as mentioned above
	 */
	private String createString(DTORegion region) {
		StringBuilder sb = new StringBuilder();
		
		if(region.getBonusCard().getBonuses().isEmpty())
			return "No available bonuses";
		else {
			sb.append("BONUS: ");
			for(DTOBonus b : region.getBonusCard().getBonuses()) {
				sb.append(b + ", ");
			}
			sb.setLength(sb.length() - 2);
		}
		return sb.toString();
	}

	@Override
	public void update(DTORegion object) {
		regionName.setToolTipText(createString(object));
		
		if(initSet) {
			this.regionName.addMouseListener(new MouseListener() {
				
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
					regionName.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					regionName.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					new CardDisplayer(font, object.getBonusCard().getBonuses());
				}
			});
			initSet = false;
		}
	}

}
