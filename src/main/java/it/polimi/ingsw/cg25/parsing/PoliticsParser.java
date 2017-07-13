package it.polimi.ingsw.cg25.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.dashboard.Councelor;
import it.polimi.ingsw.cg25.model.dashboard.Party;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;

/**
 * 
 * @author Giovanni
 *
 */
public class PoliticsParser implements Parser {

	/**
	 * The List of councelors created by the parser
	 */
	private final List<Councelor> councelors = new ArrayList<>();
	/**
	 * The List of politics card created by the parser
	 */
	private final List<PoliticsCard> politicsDeck = new ArrayList<>();

	@Override
	public void parseLineByLine(InputStreamReader inStrRdr) throws IOException {
		// Crea un buffer reader
		BufferedReader br = new BufferedReader(inStrRdr);
		String line;
		// Numero di linee lette
		int readLines = 0;
		List<HSBColor> colors = new ArrayList<>();

		// Opera su ogni riga
		while ((line = br.readLine()) != null) {
			if(!(line.startsWith("#"))) {
				readLines++;
				// Crea riga su cui operare eliminando il terminatore
				// Le stringhe sono immutabili
				String newLine = line.replaceAll(";", "");
				// Dividi la stringa in base al carattere ","
				String[] parts = newLine.split(",");

				// Crea i colori
				if (readLines == 1) {
					colors = HSBColor.getNDifferent(Integer.parseInt(parts[1]));
				}
				if (readLines == 2) {
					for (int j = 0; j < colors.size(); j++) {
						for (int i = 0; i < Integer.parseInt(parts[1]); i++) {
							councelors.add(new Councelor(new Party(colors.get(j), false)));
						}
					}
				}
				if (readLines > 2) {
					for (int i = 0; i < Integer.parseInt(parts[2]); i++) {
						if (parts[0].equals("jolly"))
							politicsDeck.add(new PoliticsCard(new Party(new HSBColor(0, 0, 0, "Jolly"), true)));
						else
							// L'indice per il colore parte da 1, quindi serve -1
							politicsDeck.add(new PoliticsCard(new Party(colors.get(Integer.parseInt(parts[0]) - 1), false)));
					}
				}
			}
		}

		//Shuffling councelors ArrayList
		Collections.shuffle(councelors);
		
		br.close();

	}

	/**
	 * @return the List of councelors created after calling parseLineByLine method
	 */
	public List<Councelor> getCouncelors() {
		return councelors;
	}

	/**
	 * @return the List of politics cards created after calling parseLineByLine method
	 */
	public List<PoliticsCard> getPoliticsDeck() {
		return politicsDeck;
	}

}
