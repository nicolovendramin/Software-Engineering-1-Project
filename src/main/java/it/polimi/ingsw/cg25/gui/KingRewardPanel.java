package it.polimi.ingsw.cg25.gui;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.polimi.ingsw.cg25.gui.guilisteners.RewardClick;
import it.polimi.ingsw.cg25.model.dto.DTORewardCard;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("serial")
public class KingRewardPanel extends JPanel implements Updatable<DTORewardCard> {

	/**
	 * Background image for king reward card
	 */
	private Image kingReward;
	/**
	 * A List of bonuses of the actual card
	 */
	private List<DTOBonus> realBonuses;
	/**
	 * This string represents the bonuses in realBonuses
	 */
	private String bonuses;
	/**
	 * The font to be used in any writing
	 */
	private Font font;
	/**
	 * Initial update ? first update : generic update
	 */
	private boolean initSet = true;
	/**
	 * This label sets the crown
	 */
	private JLabel label;
	
	/**
	 * KingRewardPanel class constructor
	 * @param x the horizontal position of this panel
	 * @param y the vertical position of this panel
	 * @param w the width of this panel
	 * @param h the height of this panel
	 * @param font the font to be used. It's the same of all the other gui panels
	 */
	public KingRewardPanel(int x, int y, int w, int h, Font font) {
		this.setLayout(new GridBagLayout());
		this.setBounds(x, y, w, h);
		this.setOpaque(false);
		this.font = font;
		try {
			loadResources();
		} catch (IOException e) {
			System.out.println("Failed while loading KingRewardPanel resources!");
			e.printStackTrace();
		}
		
		Image scaledRewCard = kingReward.getScaledInstance(90, 51, Image.SCALE_SMOOTH);
		label = new JLabel(new ImageIcon(scaledRewCard));
		label.setSize(label.getIcon().getIconWidth(), label.getIcon().getIconHeight());
		this.add(label);
	}
	
	/**
	 * The purpouse of this method is to load graphics resources
	 * @throws IOException if any image is missing
	 */
	private void loadResources() throws IOException {
		kingReward = ImageIO.read(new File("src/main/resources/kingReward.png"));
	}
	
	@Override
	public void update(DTORewardCard object) {
		this.bonuses = createString(object);
		label.setToolTipText(bonuses);
		this.realBonuses = object.getBonuses();
		if(initSet) {
			this.label.addMouseListener(new RewardClick(this, font));
			initSet = false;
		}
	}
	
	/**
	 * Create a string which represents all the bonuses included in the current
	 * king reward card
	 * @param krc the DTO king reward card to analize
	 * @return the bonus string
	 */
	private String createString(DTORewardCard krc) {
		StringBuilder sb = new StringBuilder();
		if(krc == null)
			sb.append("No available cards");
		else {
			sb.append("BONUS: ");
			if(krc.getBonuses().isEmpty())
				sb.append("None");
			else {
				for(DTOBonus b : krc.getBonuses()) {
					sb.append(b + ", ");
				}
				sb.setLength(sb.length() - 2);
			}
		}
		return sb.toString();
	}
	
	/**
	 * @return a reference to the string which contains the bonus of the king reward card
	 */
	public String getBonuses() {
		return bonuses;
	}
	
	/**
	 * @return the List of DTO bonuses included in the DTO reward card which was
	 * used in order to build this panel
	 */
	public List<DTOBonus> getRealBonuses() {
		return this.realBonuses;
	}

}
