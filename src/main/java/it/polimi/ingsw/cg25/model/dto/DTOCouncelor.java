package it.polimi.ingsw.cg25.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dashboard.Councelor;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOCouncelor implements DTO {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = 6581787222561339717L;
	/**
	 * The color of the councelor
	 */
	private final HSBColor color;
	
	/**
	 * DTOCouncelor class constructor
	 * @param counc the Councelor type object to turn into a DTO
	 * @exception NullPointerException when the Councelor is null
	 */
	public DTOCouncelor(Councelor counc) {
		if(counc == null)
			throw new NullPointerException("You can't create a DTOCouncelor without a valid Councelor!");
		this.color = counc.getParty().getColor();
	}
	
	/**
	 * This method allows to convert a Queue of councelors into a List
	 * of DTO councelors. It's used to create a DTO council 
	 * @param toConvert the Queue of councelors to be converted
	 * @return the List of DTO converted councelors
	 */
	public static List<DTOCouncelor> convertAll(Queue<Councelor> toConvert) {
		List<DTOCouncelor> converted = new ArrayList<>();
		for (Councelor coun : toConvert)
			converted.add(new DTOCouncelor(coun));

		return converted;
	}
	
	/**
	 * This method allows to convert a List of councelors into a List
	 * of DTO councelors. It's used to create a pool of unemployed councelors
	 * @param toConvert the List of councelors to be converted
	 * @return the List of DTO converted councelors
	 */
	public static List<DTOCouncelor> convertAll(List<Councelor> toConvert) {
		List<DTOCouncelor> converted = new ArrayList<>();
		for (Councelor coun : toConvert)
			converted.add(new DTOCouncelor(coun));

		return converted;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object c) {
		if(c == null)
			return false;
		if(c.getClass() == Councelor.class)
		{
			Councelor coun = (Councelor)c;
			return this.color.equals(coun.getParty().getColor());
		}
		if(c.getClass() == DTOCouncelor.class)
		{
			DTOCouncelor coun = (DTOCouncelor)c;
			return this.color.equals(coun.color);
		}
		return false;
	}

	/**
	 * The method allows to find a non-DTO councelor in a List of councelors
	 * which is equal to this DTOCouncelor object
	 * @param candidates the List of councelors
	 * @return a Councelor type object equals to this dto councelor
	 * @throws ElementNotFoundException if the List of councelors does not contain
	 * an element that can be mapped on this dto object
	 */
	public Councelor decode(List<Councelor> candidates) throws ElementNotFoundException {
		for(Councelor c : candidates)
			if(this.equals(c))
				return c;
		throw new ElementNotFoundException();
	}
	
	/**
	 * @return the color of the councelor
	 */
	public HSBColor getColor() {
		return color;
	}

	@Override
	public String toString() {
		return "Councelor: Party = " + this.getColor().printTag();
	}

}
