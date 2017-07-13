package it.polimi.ingsw.cg25.model.dto;

import java.util.List;

import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dashboard.topological.CityColor;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOCityColor implements DTO {

	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = 2895577158111423433L;
	/**
	 * A HSB color as city color
	 */
	private final HSBColor cityColor;
	/**
	 * The List of bonus linked to the city color
	 */
	private final List<DTOBonus> colorReward;
	
	/**
	 * DTOCityColor class constructor
	 * @param cityColor a CityColor object to be turned into a dto object
	 * @exception NullPointerException when cityColor is null
	 */
	public DTOCityColor(CityColor cityColor) {
		if(cityColor == null)
			throw new NullPointerException("You can't create a DTOCityColor without a valid CityColor!");
		this.cityColor = cityColor.getColor();
		this.colorReward = DTOBonus.convertAll(cityColor.getColorReward());
	}

	/**
	 * @return the city color
	 */
	public HSBColor getCityColor() {
		return cityColor;
	}

	/**
	 * @return the bonus List linked to the color
	 */
	public List<DTOBonus> getColorReward() {
		return colorReward;
	}
	
}
