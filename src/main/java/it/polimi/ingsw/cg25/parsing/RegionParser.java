package it.polimi.ingsw.cg25.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Random;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.model.dashboard.Councelor;
import it.polimi.ingsw.cg25.model.dashboard.Council;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusCreator;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitDeck;
import it.polimi.ingsw.cg25.model.dashboard.cards.RewardCard;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dashboard.topological.Region;

/**
 * 
 * @author Giovanni
 *
 */
public class RegionParser implements Parser {

	/**
	 * The bonus factory
	 */
	private final BonusCreator creator;
	/**
	 * The List of regions created by the parser
	 */
	private final List<Region> regions;
	/**
	 * The List of cities previously created by CitiesParser
	 */
	private final List<City> cities;
	/**
	 * The List of unemployed councelors
	 */
	private final List<Councelor> councelors;
	/**
	 * The king's council
	 */
	private Council kingCouncil;

	/**
	 * RegionParser class constructor
	 * @param cities the List of cities to assign to the regions created by CitiesParser
	 * @param creator a bonus concrete creator
	 * @param councelors the List of councelors created by PoliticsParser
	 */
	public RegionParser(List<City> cities, BonusCreator creator, List<Councelor> councelors) {
		this.creator = creator;
		this.regions = new ArrayList<>();
		this.cities = cities;
		this.councelors = councelors;
		
		//Crea il consiglio del Re
		this.kingCouncil = new Council(createCouncilQueue(), "King's Council");
	}
	
	@Override
	public void parseLineByLine(InputStreamReader inStrRdr) throws IOException, CannotCreateGameException {
		BufferedReader br = new BufferedReader(inStrRdr);
		String line;
		line = br.readLine();
		//Rimuovi commenti iniziali
		while(line.startsWith("#"))
			line = br.readLine();
		//Elimina il terminatore
		String numOfRegions = line.replaceAll(";", "").split(",")[1];
		
		for(int i = 0; i < Integer.parseInt(numOfRegions); i++) {
			line = br.readLine();
			String[] parts = line.replaceAll(";", "").split("\\|");
			String regionName = parts[0];
			String numFacedUpPermits = parts[1];
			String[] cityNames = parts[2].split(",");
			List<City> regionCities = new ArrayList<>();
			//Costruzione delle città della regione
			for(int j = 0; j < cityNames.length; j++) {
				try {
					regionCities.add(cityFromName(cityNames[j]));
				} catch (Exception e) {
					throw new CannotCreateGameException("City parsing failed", e);
				}
			}
			List<Bonus> tempBonus = new ArrayList<>();
			for(int k = 3; k < parts.length; k++) {
				String[] bonusParam = parts[k].split(",");
				tempBonus.add(creator.createBonus(bonusParam[0], bonusParam[1]));
			}
			//Carta reward per la regione
			RewardCard reward = new RewardCard(tempBonus);
			//Parsing delle permit tiles
			String permitQnt = br.readLine().replaceAll(";", "").split(",")[1];
			//Array di permit della regione
			List<PermitCard> regionPermits = new ArrayList<>();
			for(int n = 0; n < Integer.parseInt(permitQnt); n++) {
				line = br.readLine();
				String newLine = line.replaceAll(";", "");
				String[] permitCities = newLine.split("\\|")[0].split(",");
				List<City> permitCardCities = new ArrayList<>();
				List<Bonus> permitBonus = new ArrayList<>();
				//Costruzione delle città della permit
				for(int m = 0; m < permitCities.length; m++) {
					try {
						permitCardCities.add(cityFromName(permitCities[m]));
					} catch (Exception e) {
						throw new CannotCreateGameException("City parsing failed", e);
					}
				}
				//Costruzione dei bonus della permit 
				for(int x = 1; x < newLine.split("\\|").length; x++) {
					String[] bonusParam = newLine.split("\\|")[x].split(",");
					permitBonus.add(creator.createBonus(bonusParam[0], bonusParam[1]));
				}
				regionPermits.add(new PermitCard(permitCardCities, permitBonus));
			}
			
			//Controllo permits e faced-up permits
			if(Integer.parseInt(numFacedUpPermits) > regionPermits.size())
				throw new IllegalArgumentException("The number of faced up permits is greater than the number of permit cards");
			else regions.add(new Region(regionName, regionCities, new PermitDeck(regionPermits), new Council(createCouncilQueue(), regionName),
					reward, Integer.parseInt(numFacedUpPermits)));
			
		}
		
	}

	/**
	 * The method return a city from its name
	 * @param name the name of the city
	 * @return a City object
	 * @throws Exception if the collection of cities does not contain the specified one
	 */
	private City cityFromName(String name) {
		for (int i = 0; i < this.cities.size(); i++) {
			if (this.cities.get(i).getName().equals(name))
				return this.cities.get(i);
		}
		//Se il ciclo non ritorna
		throw new NoSuchElementException(name + " has not been created!");
	}

	/**
	 * This method is used to create a council from a List of unemployed councelors
	 * @return a Council object
	 */
	private Queue<Councelor> createCouncilQueue() {
		Random random = new Random(System.nanoTime());
		Queue<Councelor> tempQueue = new LinkedList<>();
		
		for(int i=0; i<4; i++) {
			int index = random.nextInt(this.councelors.size());
			tempQueue.add(this.councelors.get(index));
			this.councelors.remove(index);
		}
		
		return tempQueue;
	}

	/**
	 * @return the List of regions created after executing this.parseLineByLine()
	 */
	public List<Region> getRegions() {
		return regions;
	}

	/**
	 * @return a reference to the king's council created after executing this.parseLineByLine()
	 */
	public Council getKingCouncil() {
		return kingCouncil;
	}

	/**
	 * @return the List of unemployed councelors after executing this.parseLineByLine
	 * and creating the councils of the regions
	 */
	public List<Councelor> getCouncelors() {
		return councelors;
	}
	
}
