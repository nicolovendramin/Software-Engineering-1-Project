package it.polimi.ingsw.cg25.model.dto;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dashboard.Emporium;

/**
 * 
 * @author Giovanni
 *
 */
public class DTOEmporium implements DTO {
	
	/**
	 * Serial number for serialization
	 */
	private static final long serialVersionUID = -8793337790557251320L;
	/**
	 * This is the color of the owner
	 */
	private final HSBColor color;

	/**
	 * DTOEmporium class constructor
	 * @param emporium the emporium to be turned into a DTO object
	 * @exception NullPointerException when emporium is null
	 */
	public DTOEmporium(Emporium emporium) {
		if(emporium == null)
			throw new NullPointerException("You can't create a DTOEmporium without a valid emporium!");
		this.color = emporium.getOwner().getColor();

	}

	/**
	 * This method allows to convert a List of Emporium type objects into
	 * a List of DTOEmporium objects
	 * @param toConvert a List of emporiums
	 * @return a List of DTOEmporium objects
	 */
	public static List<DTOEmporium> convertAll(List<Emporium> toConvert) {
		List<DTOEmporium> converted = new ArrayList<>();
		for (Emporium emp : toConvert)
			converted.add(new DTOEmporium(emp));

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
	public boolean equals(Object emp) {
		if(emp == null)
			return false;
		if(emp.getClass() == Emporium.class) {
			Emporium emporium = (Emporium)emp;
			return this.color.equals(emporium.getOwner().getColor());
		}
		if(emp.getClass() == DTOEmporium.class) {
			DTOEmporium emporium = (DTOEmporium)emp;
			return this.color.equals(emporium.getColor());
		}
		return false;
	}

	/**
	 * @return the color of the emporium
	 */
	public HSBColor getColor() {
		return color;
	}

	@Override
	public String toString() {
		return "Emporium color -> " + color;
	}

}
