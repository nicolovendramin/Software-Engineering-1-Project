package it.polimi.ingsw.cg25.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusCreator;
import it.polimi.ingsw.cg25.model.dashboard.cards.RewardCard;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;

/**
 * 
 * @author Giovanni
 *
 */
public class KingParser implements Parser {

	/**
	 * The king's city
	 */
	private City kingCity;
	/**
	 * The List of king reward cards
	 */
	private final List<RewardCard> kingRewards;
	/**
	 * The List of cities previously created by CitiesParser
	 */
	private final List<City> cities;
	/**
	 * The bonus factory used in this parser
	 */
	private final BonusCreator creator;
	
	/**
	 * KingParser class constructor
	 * @param cities the List of cities to be used while parsing
	 * @param creator the bonus factory
	 */
	public KingParser(List<City> cities, BonusCreator creator) {
		this.cities = cities;
		this.creator = creator;
		kingRewards = new ArrayList<>();
	}
	
	@Override
	public void parseLineByLine(InputStreamReader inStrRdr) throws IOException {
		BufferedReader br = new BufferedReader(inStrRdr);
		List<Bonus> tempBonus = new ArrayList<>();
		String line;
		line = br.readLine();
		//Elimina commenti iniziali
		while(line.startsWith("#")) {
			line = br.readLine();
		}
		try{
			kingCity = this.cityFromName(line.replaceAll(";", ""));
		}
		catch(Exception e){
			throw new IllegalArgumentException(e);
		}
		
		String[] splits;
		String[] bonusParam;
		
		while((line = br.readLine()) != null)
		{
			String newLine = line.replaceAll(";", "");
			splits = newLine.split("\\|");
			for(int i=0; i < splits.length; i++)
			{
				bonusParam = splits[i].split(",");
				tempBonus.add(creator.createBonus(bonusParam[0], bonusParam[1]));
			}
			kingRewards.add(new RewardCard(tempBonus));
			tempBonus = new ArrayList<>();
		}
		
		//Empty the king's city bonus List
		kingCity.getCityBonus().clear();
		
		br.close();
	}

	/**
	 * @return the king's city
	 */
	public City getKingCity() {
		return this.kingCity;
	}
	
	/**
	 * @return the List of reward cards
	 */
	public List<RewardCard> getKingRewards(){
		return this.kingRewards;
	}
	
	/**
	 * The method return a city from its name
	 * @param name the name of the city
	 * @return a City object
	 * @throws Exception if the collection of cities does not contain the specified one
	 */
	private City cityFromName(String name) {
		for (int i = 0; i < this.cities.size(); i++) {
			if (this.cities.get(i).getName().equals(name.replaceAll(" ","")))
				return this.cities.get(i);
		}
		throw new NoSuchElementException("the city ".concat(name).concat(" is not in the cities").concat(cities.toString()));
	}
	
}
