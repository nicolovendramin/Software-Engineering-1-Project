package it.polimi.ingsw.cg25.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.model.dashboard.NobilityCell;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusCreator;

/**
 * 
 * @author Giovanni
 *
 */
public class NobilityCellParser implements Parser {

	/**
	 * The List of nobility cells created by the parser
	 */
	private final List<NobilityCell> nobilityCells;
	/**
	 * The bonus factory to be used while parsing
	 */
	private final BonusCreator bonusCreator;

	/**
	 * NobilityCellParser class constructor
	 * @param bonusCreator the bonus factory used to create bonuses
	 */
	public NobilityCellParser(BonusCreator bonusCreator) {
		nobilityCells = new ArrayList<>();
		this.bonusCreator = bonusCreator;
	}

	@Override
	public void parseLineByLine(InputStreamReader inStrRdr) throws IOException {
		// Crea un buffer reader
		BufferedReader br = new BufferedReader(inStrRdr);
		String line;
		// Opera su ogni riga fino alla fine del file
		while ((line = br.readLine()) != null) {
			if(!(line.startsWith("#"))) {
				// Crea riga su cui operare eliminando il terminatore
				// Le stringhe sono immutabili
				String newLine = line.replaceAll(";", "");
				// Dividi la stringa in base al carattere "|"
				String[] parts = newLine.split("\\|");

				//Crea ArrayList per bonus vuoto
				List<Bonus> bonuses = new ArrayList<>();
				
				if(parts.length == 1)
					nobilityCells.add(new NobilityCell(Integer.parseInt(parts[0]), bonuses));
				else {
					for (int i = 1; i < parts.length; i++) {
						String[] bnsParts = parts[i].split(",");
						bonuses.add(bonusCreator.createBonus(bnsParts[0], bnsParts[1]));
					}

					nobilityCells.add(new NobilityCell(Integer.parseInt(parts[0]), bonuses));
				}
			}

		}

		br.close();

	}

	/**
	 * @return a reference to the ArrayList of nobility cells created by the parser
	 * after calling parseLineByLine method
	 */
	public List<NobilityCell> getNobilityCells() {
		return nobilityCells;
	}

}
