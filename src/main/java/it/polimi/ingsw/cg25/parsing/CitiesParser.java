package it.polimi.ingsw.cg25.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusCreator;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dashboard.topological.CityColor;

/**
 * 
 * @author Giovanni
 *
 */
public class CitiesParser implements Parser {

	/**
	 * The List of cities created by the parser
	 */
	private final List<City> cities;
	/**
	 * The bonus factory
	 */
	private final BonusCreator bonusCreator;

	/**
	 * CitiesParser class constructor
	 * @param bonusCreator the bonus factory to be used in the parser
	 */
	public CitiesParser(BonusCreator bonusCreator) {
		cities = new ArrayList<>();
		this.bonusCreator = bonusCreator;
	}

	@Override
	public void parseLineByLine(InputStreamReader inStrRdr) throws IOException {
		BufferedReader br = new BufferedReader(inStrRdr);
		String line;
		//Gruppi di colori diversi di città
		List<CityColor> groups = new ArrayList<>();
		List<HSBColor> colors;
		List<List<Bonus>> bonus = new ArrayList<>();
		// Leggi a prima riga 
		line = br.readLine();
		//Rimuovi commenti iniziali
		while(line.startsWith("#"))
			line = br.readLine();
		String newLine;
		//Rimuovi il terminatore
		newLine = line.replaceAll(";", "");
		String[] parts = newLine.split(",");
		int numOfCities = Integer.parseInt(parts[1]);
		//Istanzia un ArrayList di bonus da allocare alle città in base al numero di città specificato
		for(int i = 0; i < numOfCities; i++) {
			line = br.readLine();
			newLine = line.replaceAll(";", "");
			//Se c'è almeno un bonus scritto sul file
			List<Bonus> cityBonus = new ArrayList<>();
			if(!("".equals(newLine))){
				String[] bonuses = newLine.split("\\|");
				for(int j = 0; j < bonuses.length; j++) {
					String[] bonusParam = bonuses[j].split(",");
					cityBonus.add(bonusCreator.createBonus(bonusParam[0], bonusParam[1]));
				}
			}
			bonus.add(cityBonus);
		}
		
		//Da qui in poi l'array di bonus è pronto
		Collections.shuffle(bonus);
		
		//Leggi un'altra riga
		line = br.readLine();
		newLine = line.replaceAll(";", "");
		parts = newLine.split(",");
		//Istanzia un certo numero di colori in accordo al file
		colors = HSBColor.getNDifferent(Integer.parseInt(parts[1]));
		
		for(int i = 0; i < colors.size(); i++) {
			line = br.readLine();
			newLine = line.replaceAll(";", "");
			if(!("".equals(newLine))) {
				parts = newLine.split("\\|");
				String[] bonusParam;
				List<Bonus> colorBonus = new ArrayList<>();
				for (int j = 0; j < parts.length; j++) {
					bonusParam = parts[j].split(",");
					colorBonus.add(bonusCreator.createBonus(bonusParam[0], bonusParam[1]));
				}
				groups.add(new CityColor(colors.get(i), colorBonus));
			}
			else groups.add(new CityColor(colors.get(i), new ArrayList<>()));
		}
		
		int index = 0;
		//Ora bisogna creare le città
		while ((line = br.readLine()) != null) {
			newLine = line.replaceAll(";", "");
			parts = newLine.split(",");
			String name = parts[0];
			int colorIndex = Integer.parseInt(parts[1]);
			cities.add(new City(name, groups.get(colorIndex), bonus.get(index)));
			index++;
		}
		br.close();

	}

	/**
	 * @return the List of cities created by the parser after calling parseLineByLine method
	 */
	public List<City> getCities() {
		return cities;
	}

}
