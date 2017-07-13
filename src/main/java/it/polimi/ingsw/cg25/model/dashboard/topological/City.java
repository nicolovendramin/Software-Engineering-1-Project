package it.polimi.ingsw.cg25.model.dashboard.topological;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dashboard.Emporium;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonusable;

/**
 * 
 * @author Giovanni
 *
 */
public class City implements Bonusable {
	
	/**
	 * The name of the city
	 */
	private final String name;
	/**
	 * The color of the city
	 */
	private final CityColor color;
	/**
	 * The bonuses of the city
	 */
	private final List<Bonus> cityBonus;
	/**
	 * The emporiums of the city
	 */
	private final List<Emporium> cityEmporiums;

	/**
	 * City class constructor
	 * @param name the name of the city
	 * @param color the color of the city
	 * @param cityBonus the ArrayList of bonus of the city
	 * @exception NullPointerException when the name of the city or the color or the list
	 * of bonuses is null
	 */
	public City(String name, CityColor color, List<Bonus> cityBonus) {
		cityEmporiums = new ArrayList<>();
		if(name == null)
			throw new NullPointerException("You can't create a city without a name!");
		if(color == null)
			throw new NullPointerException("You can't create a city without a color!");
		if(cityBonus == null)
			throw new NullPointerException("You can't create a city without an ArrayList of bonuses!");
		this.name = name;
		this.color = color;
		this.cityBonus = cityBonus;
	}

	/**
	 * @return the city's name
	 */
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	@Override
	public void takeAllBonuses(PlayerCD4 target) {
		for(Bonus b : cityBonus) {
			b.acquireBonus(target);
		}
	}
	
	/**
	 * Add an emporium to this city
	 * @param emporium the emporium to add
	 */
	public void addEmporium(Emporium emporium) {
		cityEmporiums.add(emporium);
	}
	
	/**
	 * @return the number of emporiums in a city
	 */
	public int getNumEmp() {
		return cityEmporiums.size();
	}

	/**
	 * @return the color of the city
	 */
	public CityColor getColor() {
		return color;
	}

	/**
	 * @return the ArrayList of bonus of the city
	 */
	public List<Bonus> getCityBonus() {
		return cityBonus;
	}

	/**
	 * @return the ArrayList of emporiums of the city
	 */
	public List<Emporium> getCityEmporiums() {
		return cityEmporiums;
	}
	
}
