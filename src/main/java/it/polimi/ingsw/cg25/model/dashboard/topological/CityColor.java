package it.polimi.ingsw.cg25.model.dashboard.topological;

import java.util.List;

import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonusable;

/**
 * 
 * @author Giovanni
 *
 */
public class CityColor implements Bonusable {
	
	/**
	 * The HSBColor linked to the CityColor object
	 */
	private final HSBColor color;
	/**
	 * The List of bonuses of the CityColor object
	 */
	private final List<Bonus> colorReward;
	
	/**
	 * CityColor class constructor
	 * @param color the color of the city
	 * @param colorReward the ArrayList of bonuses related to this color
	 * @exception NullPointerException when the color or the colorReward List is null
	 */
	public CityColor(HSBColor color, List<Bonus> colorReward) {
		if(color == null)
			throw new NullPointerException("You can't create a CityColor without a color!");
		if(colorReward == null)
			throw new NullPointerException("You can't create a CityColor without a colorReward List!");
		this.color = color;
		this.colorReward = colorReward;
	}

	@Override
	public void takeAllBonuses(PlayerCD4 target) {
		for(Bonus b : colorReward) {
			b.acquireBonus(target);
		}
		//Azzera i bonus relativi al colore
		this.colorReward.clear();
	}
	
	/**
	 * @return the color of the city
	 */
	public HSBColor getColor() {
		return color;
	}

	/**
	 * @return the reference to the bonus List of the city
	 */
	public List<Bonus> getColorReward() {
		return colorReward;
	}

}
