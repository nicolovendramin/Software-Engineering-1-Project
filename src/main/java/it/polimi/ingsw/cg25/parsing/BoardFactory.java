package it.polimi.ingsw.cg25.parsing;

import java.io.IOException;
import java.io.InputStreamReader;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.model.BoardCD4;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusConcreteCreator;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusCreator;
import it.polimi.ingsw.cg25.model.dashboard.cards.KingRewardsDeck;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsDeck;

/**
 * 
 * @author Giovanni
 *
 */
public class BoardFactory {

	/**
	 * The complete and fully init board
	 */
	private BoardCD4 board;
	/**
	 * The bonus creator used by the parser classes
	 */
	private BonusCreator bonusCreator;

	/**
	 * BoardFactory class constructor
	 * @param nobilityCells the {@link InputStreamReader} related to the List of nobility cells
	 * @param politics the {@link InputStreamReader} related to politics cards and councelors
	 * @param cities the {@link InputStreamReader} related to the cities and their bonuses
	 * @param graph the {@link InputStreamReader} related to the graph and connections among cities
	 * @param king the {@link InputStreamReader} related to the king and the king's rewards deck
	 * @param regions the {@link InputStreamReader} related to the regions
	 * @throws CannotCreateGameException if an IOException occurred during the creation of the game
	 */
	public BoardFactory(InputStreamReader nobilityCells, InputStreamReader politics, InputStreamReader cities,
			InputStreamReader graph, InputStreamReader king, InputStreamReader regions) throws CannotCreateGameException {
		
		//Crea il creatore dei bonus
		bonusCreator = new BonusConcreteCreator();
		
		try {
			//Creazione nobility cells
			NobilityCellParser nobCellPar = new NobilityCellParser(bonusCreator);
			nobCellPar.parseLineByLine(nobilityCells);
			
			//Creazione carte politiche e consiglieri
			PoliticsParser polPar = new PoliticsParser();
			polPar.parseLineByLine(politics);
			PoliticsDeck polDeck = new PoliticsDeck(polPar.getPoliticsDeck());
			
			//Creazione città
			CitiesParser cPar = new CitiesParser(bonusCreator);
			cPar.parseLineByLine(cities);
			
			//Creazione grafo
			GraphParser graphPar = new GraphParser(cPar.getCities());
			graphPar.parseLineByLine(graph);
			
			//Creazione re
			KingParser kPar = new KingParser(cPar.getCities(), bonusCreator);
			kPar.parseLineByLine(king);
			KingRewardsDeck kingRewDeck = new KingRewardsDeck(kPar.getKingRewards());
			
			//Creazione Regioni
			RegionParser regPar = new RegionParser(cPar.getCities(), bonusCreator, polPar.getCouncelors());
			regPar.parseLineByLine(regions);
			
			//ATTENZIONE: usare l'array di consiglieri della regione per definire il gruppo di unemployedCouncelors
			board = new BoardCD4(regPar.getRegions(), regPar.getKingCouncil(), polDeck, 
					kingRewDeck, nobCellPar.getNobilityCells(), regPar.getCouncelors(), 
					kPar.getKingCity(), cPar.getCities(), graphPar.getRoadNetwork());
		
		}
		catch (IOException e) {
			throw new CannotCreateGameException("An error during the creation of the game occurred!", e);
		}
	}

	/**
	 * @return a BoardCD4 type board set accordingly to the readers accepted by
	 *         the class constructor
	 */
	public BoardCD4 getBoard() {
		return board;
	}

}
