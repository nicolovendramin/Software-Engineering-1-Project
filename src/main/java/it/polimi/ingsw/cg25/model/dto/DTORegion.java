package it.polimi.ingsw.cg25.model.dto;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.model.dashboard.topological.Region;

/**
 * 
 * @author Giovanni
 *
 */
public class DTORegion implements DTO {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -3176358769242539795L;
	/**
	 * The name of the region
	 */
	private final String name;
	/**
	 * The List of DTO cities of the DTO region
	 */
	private final List<DTOCity> cities;
	/**
	 * The List of faced up permits of the DTO region
	 */
	private final List<DTOPermitCard> faceUpPermits;
	/**
	 * The DTO council of the DTO region
	 */
	private final DTOCouncil council;
	/**
	 * The DTO reward card of the DTO region
	 */
	private final DTORewardCard bonusCard;
	
	/**
	 * DTORegion class constructor
	 * @param region the Region type object to be converted
	 * @exception NullPointerException if region is null
	 */
	public DTORegion(Region region) {
		if(region == null)
			throw new NullPointerException("You can't create a DTORegion without a valid region");
		
		this.name = region.getName();
		this.cities = DTOCity.convertAll(region.getCities());
		this.council = new DTOCouncil(region.getCouncil());
		this.faceUpPermits = DTOPermitCard.convertAll(region.getFaceUpPermits());
		this.bonusCard = new DTORewardCard(region.getBonusCard());
	}

	/**
	 * This method allows to convert a List of Region type objects into
	 * a List of DTORegion objects
	 * @param regions the List of regions to be converted
	 * @return the List of DTO converted regions 
	 * @throws IllegalArgumentException if regions is null
	 */
	public static List<DTORegion> convertAll(List<Region> regions) {
		if (regions == null)
			throw new IllegalArgumentException("Null parameter is not allowed.");
		List<DTORegion> converted = new ArrayList<>();
		for(Region r : regions)
			converted.add(new DTORegion(r));
		return converted;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bonusCard.hashCode();
		result = prime * result + cities.hashCode();
		result = prime * result + council.hashCode();
		result = prime * result + faceUpPermits.hashCode();
		result = prime * result + name.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object r) {
		if(r == null)
			return false;
		if(r.getClass() == Region.class) {
			Region reg = (Region)r;
			//Confronta il nome della regione, le città, le tessere permesso a faccia in su, il consiglio e la bonus card
			return this.name.equals(reg.getName()) && this.cities.equals(reg.getCities()) &&
					this.faceUpPermits.equals(reg.getFaceUpPermits()) && this.council.equals(reg.getCouncil());
		}
		if(r.getClass() == DTORegion.class) {
			DTORegion reg = (DTORegion)r;
			//Confronta il nome della regione, le città, le tessere permesso a faccia in su, il consiglio e la bonus card
			return this.name.equals(reg.getName()) && this.cities.equals(reg.getCities()) &&
					this.faceUpPermits.equals(reg.getFaceUpPermits()) && this.council.equals(reg.getCouncil());
		}
		return false;
	}

	/**
	 * The method allows to find a non-DTO region in a List of regions
	 * which is equal to this DTORegion object
	 * @param candidates a List of Region type objects
	 * @return a Region type object of the previous List
	 * @throws ElementNotFoundException if the list of candidates does not contain
	 * a region that can be mapped into this DTO region
	 */
	public Region decode(List<Region> candidates) throws ElementNotFoundException {
		for(Region r : candidates)
			if(this.equals(r))
				return r;
		throw new ElementNotFoundException();
	}
	
	/**
	 * @return the name of the region
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return a List of DTOCities in the region
	 */
	public List<DTOCity> getCities() {
		return cities;
	}

	/**
	 * @return the council of the region
	 */
	public DTOCouncil getCouncil() {
		return council;
	}

	/**
	 * @return the List of faced up permits of the region
	 */
	public List<DTOPermitCard> getFaceUpPermits() {
		return faceUpPermits;
	}

	/**
	 * @return the DTO reward card linked to the region
	 */
	public DTORewardCard getBonusCard() {
		return bonusCard;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Region: Name = ");
		sb.append(name);
		
		return sb.toString();
	}
	
}
