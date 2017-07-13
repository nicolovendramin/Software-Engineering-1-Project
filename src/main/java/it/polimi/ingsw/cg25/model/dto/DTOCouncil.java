package it.polimi.ingsw.cg25.model.dto;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.dashboard.Council;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOCouncil implements DTO {

	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = -656992796377561550L;
	/**
	 * The List of councelors
	 */
	private final List<DTOCouncelor> councelors;
	/**
	 * The attribute "location" contains the name of the region where
	 * the balcony is and specifies if the council belongs to the king
	 */
	private final String location;
	
	/**
	 * DTOCouncil class constructor
	 * @param councelors an object whose type is {@link Council}
	 * @exception NullPointerException when Council is null
	 */
	public DTOCouncil(Council councelors) {
		if(councelors == null)
			throw new NullPointerException("You can't create a DTOCouncil without a valid Council!");
		this.councelors = DTOCouncelor.convertAll(councelors.getCouncelors());
		this.location = councelors.getLocation();
	}

	/**
	 * It converts to DTO objects all the councelors included in toConvert
	 * @param toConvert the List of councelors to convert
	 * @return the List of converted councelors
	 */
	public static List<DTOCouncil> convertAll(List<Council> toConvert) {
		List<DTOCouncil> converted = new ArrayList<>();
		for (Council coun : toConvert)
			converted.add(new DTOCouncil(coun));

		return converted;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + councelors.hashCode();
		result = prime * result + location.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object c) {
		if(c == null)
			return false;
		if(c.getClass() == Council.class)
		{
			Council coun = (Council)c;
			return this.councelors.equals(coun.getCouncelors()) && 
				this.location.equals(coun.getLocation());
		}
		if(c.getClass() == DTOCouncil.class){
			DTOCouncil coun = (DTOCouncil)c;
			return this.councelors.equals(coun.getCouncelors()) &&
					this.location.equals(coun.getLocation());
		}
		return false;
	}

	/**
	 * It returns the corresponding Council to this DTO object among a List of councils
	 * @param candidates the List of councils
	 * @return the corresponding council
	 * @throws ElementNotFoundException if the DTO object does not correpond to any council
	 */
	public Council decode(List<Council> candidates) throws ElementNotFoundException {
		for(Council c : candidates)
			if(this.equals(c))
				return c;
		throw new ElementNotFoundException();
	}
	
	/**
	 * @return the List of councelors
	 */
	public List<DTOCouncelor> getCouncelors() {
		return councelors;
	}
	
	/**
	 * @return the location of the council
	 */
	public String getLocation() {
		return location;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if("King's Council".equals(location))
			sb.append(location);
		else sb.append(location + " Council");
		
		return sb.toString();
	}
	
}
