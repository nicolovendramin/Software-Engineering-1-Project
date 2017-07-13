package it.polimi.ingsw.cg25.model.dto;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.model.dashboard.NobilityCell;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class DTONobilityCell implements DTO {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -5519719974795081923L;
	/**
	 * Nobility cell id
	 */
	private final int id;
	/**
	 * the List of bonuses of the nobility cell
	 */
	private final List<DTOBonus> bonus;
	
	/**
	 * NobilityCell class constructor
	 * @param cell the NobilityCell object to convert in a
	 * data transfer object
	 * @exception IllegalArgumentException if the NobilityCell object is invalid
	 */
	public DTONobilityCell(NobilityCell cell) {
		if(cell == null)
			throw new IllegalArgumentException("You can't create a DTO nobility cell without a valid NobilityCell!");
		this.id = cell.getId();
		this.bonus = DTOBonus.convertAll(cell.getBonusList());
	}
	
	public static List<DTONobilityCell> convertAll(List<NobilityCell> toConvert) {
		List<DTONobilityCell> converted = new ArrayList<>();
		for (NobilityCell nc : toConvert)
			converted.add(new DTONobilityCell(nc));

		return converted;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bonus == null) ? 0 : bonus.hashCode());
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object nc) {
		if (nc == null)
			return false;
		if(nc.getClass() == NobilityCell.class) {
			NobilityCell cell = (NobilityCell)nc;
			return (this.id == cell.getId()) && this.bonus.equals(cell.getBonusList());
		}
		if(nc.getClass() == DTONobilityCell.class) {
			DTONobilityCell cell = (DTONobilityCell)nc;
			return (this.id == cell.getId()) && this.bonus.equals(cell.getBonus());
		}
		return false;
	}

	/**
	 * @return the id of the cell
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @return a List of bonuses contained in the cell
	 */
	public List<DTOBonus> getBonus() {
		return bonus;
	}

	@Override
	public String toString() {
		return "ID = " + id + ", Bonus = " + bonus;
	}
	
}
