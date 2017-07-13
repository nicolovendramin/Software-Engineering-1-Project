package it.polimi.ingsw.cg25.model.dto;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOCity implements DTO {

	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = -6115878605294973223L;
	/**
	 * The List of DTO bonus of the city
	 */
	private final List<DTOBonus> bonuses;
	/**
	 * The List of DTO emporiums of the city
	 */
	private final List<DTOEmporium> emporiums;
	/**
	 * The name of the city
	 */
	private final String name;
	/**
	 * The DTOCityColor object of the city (color + color bonus)
	 */
	private final DTOCityColor color;

	/**
	 * DTOCity class constructor
	 * @param c the City type object to convert
	 * @exception NullPointerException when the argument is null
	 */
	public DTOCity(City c) {
		if(c == null)
			throw new NullPointerException("You can't create a DTOCity without a valid City!");
		this.name = c.getName();
		this.emporiums = DTOEmporium.convertAll(c.getCityEmporiums());
		this.bonuses = DTOBonus.convertAll(c.getCityBonus());
		this.color = new DTOCityColor(c.getColor());
	}

	/**
	 * @return the dto object that contains the color of the city and
	 * its bonuses
	 */
	public DTOCityColor getColor() {
		return this.color;
	}
	
	/**
	 * @return the name of the city
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the List of DTO bonuses of the city
	 */
	public List<DTOBonus> getBonuses() {
		return bonuses;
	}

	/**
	 * @return the List of DTO Emporiums of the city
	 */
	public List<DTOEmporium> getEmporiums() {
		return emporiums;
	}

	/**
	 * This method allows to convert a List of City type objects into
	 * a List of DTOCity objects
	 * @param cities a List of cities
	 * @return a List of DTOCity objects
	 */
	public static List<DTOCity> convertAll(List<City> cities){
		List<DTOCity> converted = new ArrayList<>();
		for(City c : cities)
			converted.add(new DTOCity(c));
		return converted;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bonuses.hashCode();
		result = prime * result + color.hashCode();
		result = prime * result + emporiums.hashCode();
		result = prime * result + name.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object city) {
		if(city == null)
			return false;
		if(city.getClass() == City.class) {
			City c = (City)city;
			//Confronta il nome delle città, i bonus, gli empori ed il colore (HSB)
			return this.name.equals(c.getName()) && this.bonuses.equals(c.getCityBonus()) &&
					this.emporiums.equals(c.getCityEmporiums()) && this.color.getCityColor().equals(c.getColor().getColor());
		}
		if(city.getClass() == DTOCity.class) {
			DTOCity c = (DTOCity)city;
			return this.name.equals(c.getName());
		}
		return false;
	}
	
	/**
	 * The method allows to find a non-DTO city in a List of cities
	 * which is equal to this DTOCity object
	 * @param candidates a List of City type objects
	 * @return a City type object of the previous List
	 * @throws ElementNotFoundException if the list of candidates does not contain
	 * a city that can be mapped into this DTO City
	 */
	public City decode(List<City> candidates) throws ElementNotFoundException {
		for(City c : candidates)
			if(this.equals(c))
				return c;
		throw new ElementNotFoundException();
	}

	/**
	 * It compares a List of DTOCity objects to a List of City objects and
	 * returns a List of decoded City objects (see decode method)
	 * @param toDecode the List of DTOCity objects to decode
	 * @param candidates the List of City objects to compare
	 * @return a List of decoded cities
	 * @throws ElementNotFoundException if a DTOCity object does not appear
	 * in the List of condidates
	 */
	public static List<City> decodeAll(List<DTOCity> toDecode, List<City> candidates) throws ElementNotFoundException {
		List<DTOCity> toDecodeCopy = new ArrayList<>(toDecode);
		List<City> decoded = new ArrayList<>();
		List<City> candidateCopy = new ArrayList<>(candidates);
		for(DTOCity dc : toDecodeCopy){
			City temp = dc.decode(candidateCopy);
			decoded.add(temp);
			candidateCopy.remove(temp);
			}
		return decoded;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("City: Name = ");
		sb.append(name);
		sb.append(", Color = ");
		sb.append(this.getColor().getCityColor().printTag());
		sb.append(", Bonus = ");
		if(bonuses.isEmpty())
			sb.append("none");
		else {
			for(DTOBonus b : bonuses)
				sb.append(b + ", ");
			sb.setLength(sb.length() - 2);
		}
		sb.append(", Emporiums = ");
		if(emporiums.isEmpty())
			sb.append("none");
		else {
			for(DTOEmporium emp : emporiums)
				sb.append(emp.getColor().printTag() + ", ");
			sb.setLength(sb.length() - 2);
		}
		return sb.toString();
	}
	
}
