package it.polimi.ingsw.cg25.model.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graphs;

import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.dto.dtobonus.DTOBonus;

/**
 * 
 * @author Giovanni
 *
 */
public class GameStatus implements DTO {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -5336621975628010780L;
	/**
	 * The player who requests the game status
	 */
	private final DTOPlayerCD4 requestingPlayer;
	/**
	 * A List containing the other players
	 */
	private final List<DTOPlayerCD4> otherPlayers;
	/**
	 * A reference to the DTO board of the game
	 */
	private final DTOBoardCD4 board;
	/**
	 * is the match running?
	 */
	private boolean isRunning;
	/**
	 * is it the last round?
	 */
	private boolean isLastRound;
	/**
	 * a List of players ordered by their rank
	 */
	private final List<DTOPlayerCD4> highScores;
	
	/**
	 * GameStatus class constructor
	 * @param match a reference to a precise running match
	 * @param requestingPlayer the player whose statistics have to be printed
	 * @exception NullPointerException when match or requestingPlayer is null
	 */
	public GameStatus(MatchCD4 match, PlayerCD4 requestingPlayer) {
		if(match == null)
			throw new NullPointerException("You can't create a game status without a valid match!");
		if(requestingPlayer == null)
			throw new NullPointerException("Who is requesting the game status?");
		
		//Check the match status when game status is created
		//It's used to display the end game message
		this.isRunning = match.isRunning() ? true : false;
		//It's used to display the last round message
		this.isLastRound = match.isLastRound() ? true : false;
		
		if(!this.isRunning)
			this.highScores = DTOPlayerCD4.convertAll(match.whoWon());
		else this.highScores = new ArrayList<>();
		
		this.requestingPlayer = new DTOPlayerCD4(requestingPlayer);
		this.otherPlayers = DTOPlayerCD4.convertAll(match.getPlayers());
		//Rimuovi da otherPlayer il player corrente
		for(DTOPlayerCD4 pl : otherPlayers) {
			if(pl.equals(requestingPlayer)) {
				otherPlayers.remove(pl);
				break;
			}
		}
		this.board = new DTOBoardCD4(match.getBoard());
		
	}
	
	/**
	 * The overrided toString is used in order to create a kind of user-friendly interface
	 * when using CLI. It shows up all the game statistics to the players
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append("******************************************************************\n");
		sb.append("*                         GAME STATUS                            *\n");
		sb.append("******************************************************************\n");
		sb.append("\n");
		
		if(isLastRound)
			sb.append(lastRoundMessage());
		if(!isRunning)
			sb.append(finalMessage());
		
		//PLAYERS' INFORMATION
		sb.append(playersInfo());
		
		//REGIONS' INFORMATION
		sb.append(regionsInfo());
		
		//CONSIGLI
		sb.append(councilsInfo());
		
		//PERCORSO NOBILTA'
		sb.append(nobilityTrackInfo());
		
		//CITTA' E GRAFO
		sb.append(citiesAndGraphInfo());
		
		//Set is Running
		
		return sb.toString();
	}
	
	/**
	 * This method is used to print the whole players' statistics
	 * @return a StringBuilder object
	 */
	private StringBuilder playersInfo() {
		StringBuilder sb = new StringBuilder();
		int index;
		
		//CURRENT PLAYER INFORMATION
		sb.append("Player stats:\n");
		sb.append(requestingPlayer);
			
		//PERMIT COPERTE E SCOPERTE DEL CURRENT PLAYER
		//PERMIT NON USATE
		sb.append("\tPermits to be used: \n");
		index = 1;
		for(DTOPermitCard p : requestingPlayer.getPermitsToBeUsed()) {
			sb.append("\t\t" + index + ") " + p.toString() + "\n");
			index++;
		}
		//PERMIT USATE
		sb.append("\tUsed permits: \n");
		index = 1;
		for(DTOPermitCard p : requestingPlayer.getUsedPermits()) {
			sb.append("\t\t" + index + ") " + p.toString() + "\n");
			index++;
		}
			
		//OTHER PLAYERS INFORMATION
		sb.append("\n");
		sb.append("Other players' stats:\n");
		for(DTOPlayerCD4 pl : otherPlayers) {
			sb.append("Name: " + pl.getName() + "\n");
			sb.append("\tColor: " + pl.getPlayerColor().printTag() + "\n");
			sb.append("\tVictory points: " + pl.getPocket().getVictoryPoints() + "\n");
			sb.append("\tNobility points: " + pl.getPocket().getNobilityRank() + "\n");
			sb.append("\tCoins: " + pl.getPocket().getCoins() + "\n");
			sb.append("\tAvailable assistants: " + pl.getPocket().getAssistants() + "\n");
		}
		sb.append("\n");
		
		return sb;
	}
	
	/**
	 * This method is used to print regions' information
	 * @return a StringBuilder object
	 */
	private StringBuilder regionsInfo() {
		StringBuilder sb = new StringBuilder();
		int index;
		
		//REGIONS' INFORMATION
		sb.append("Regions' information:\n");
		index = 1;
		for(DTORegion r : this.board.getRegions()) {
			sb.append("\t" + index + ") Name: " + r.getName() + ", Cities: ");
			for(DTOCity c : r.getCities())
				sb.append(c.getName() + ", ");
			sb.append("Region's bonus: ");
			for(DTOBonus b : r.getBonusCard().getBonuses())
				sb.append(b + ", ");
			sb.setLength(sb.length() - 2);
			sb.append("\n");
			int index2 = 1;
			for(DTOPermitCard p : r.getFaceUpPermits()) {
				sb.append("\t\t" + index2 + ") " + p + "\n");
				index2++;
			}
			sb.append("\n");
			index++;
		}
		
		return sb;
	}
	
	/**
	 * This method is used to print councils' information
	 * @return a StringBuilder object
	 */
	private StringBuilder councilsInfo() {
		StringBuilder sb = new StringBuilder();
		int index;
		
		//CONSIGLI
		sb.append("Councils:\n");
		index = 1;
		List<DTOCouncelor> invertedCoun;
		
		sb.append("\t" + index + ") King's council: ");
		invertedCoun = new ArrayList<>(board.getKingCouncil().getCouncelors());
		//Inverti consiglieri
		Collections.reverse(invertedCoun);
		for(DTOCouncelor c : invertedCoun) {
			sb.append(c.getColor().printTag() + ", ");
		}
		sb.setLength(sb.length() - 2);
		sb.append("\n");
		sb.append("\t\t" + "King Reward: ");
		if(board.getKingReward() == null)
			sb.append("No available cards");
		else {
			for(DTOBonus b : board.getKingReward().getBonuses()) {
				sb.append(b + ", ");
			}
			sb.setLength(sb.length() - 2);
			sb.append("\n");
		}
		
		for(DTORegion reg : board.getRegions()) {
			sb.append("\t" + index + ") " + reg.getName() + ": ");
			invertedCoun = new ArrayList<>(reg.getCouncil().getCouncelors());
			//Inverti consiglieri
			Collections.reverse(invertedCoun);
			for(DTOCouncelor c : invertedCoun) {
				sb.append(c.getColor().printTag() + ", ");
			}
			index++;
			sb.setLength(sb.length() - 2);
			sb.append("\n");
		}
		sb.append("\n");
		
		return sb;
	}

	/**
	 * This method is used to print the nobility track
	 * @return a StringBuilder object
	 */
	private StringBuilder nobilityTrackInfo() {
		StringBuilder sb = new StringBuilder();

		//PERCORSO NOBILTA'
		sb.append("Nobility Track:\n");
		for(DTONobilityCell nc : board.getNobilityTrack()) {
			if(nc.getBonus().isEmpty())
				sb.append("\t" + nc.getId() + ") Bonus: none\n");
			else {
				sb.append("\t" + nc.getId() + ") Bonus: ");
				for(DTOBonus b : nc.getBonus()) {
					sb.append(b + ", ");
				}
				sb.setLength(sb.length() - 2);
				sb.append("\n");
			}
		}
		sb.append("\n");
		
		return sb;
	}
	
	/**
	 * This method is used to print the last round message
	 * @return a StringBuilder object whose toString is the last round message
	 */
	private StringBuilder lastRoundMessage() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("*************************** LAST ROUND ***************************\n");
		sb.append("\n");
		
		return sb;
	}
	
	/**
	 * This method is used to print the game over message
	 * @return a StringBuilder object whose toString is the game over message
	 */
	private StringBuilder finalMessage() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("*************************** GAME OVER ****************************\n");
		sb.append("The winner is: " + highScores.get(0).getName() + ", Victory Points: " 
				+ highScores.get(0).getPocket().getVictoryPoints() + "\n");
		sb.append("Other players:\n");
		for(int i = 1; i < highScores.size(); i++)
			sb.append("\t" + i + ") " + highScores.get(i).getName() + ", Victory Points: "
					+ highScores.get(i).getPocket().getVictoryPoints() + "\n");
		sb.append("\n");
		
		return sb;
	}
	
	/**
	 * This method is used to print cities and graph information
	 * @return a StringBuilder object
	 */
	private StringBuilder citiesAndGraphInfo() {
		StringBuilder sb = new StringBuilder();
		
		//CITTA' E GRAFO
		sb.append("Directly connected cities:\n");
		for(DTOCity c : board.getCities()) {
			List<DTOCity> neighbors = Graphs.neighborListOf(board.getRoadNetwork(), c);
			//Se la città è la città del re
			if(board.getKingCity().getName().equals(c.getName()))
				sb.append("\t" + c.getName() + " [KING] -> ");
			else sb.append("\t" + c.getName() + " -> ");
			for(DTOCity connCity : neighbors) {
				sb.append(connCity.getName() + ", ");
			}
			sb.setLength(sb.length() - 2);
			sb.append("\n");
			sb.append("\t\tCity's color: " + c.getColor().getCityColor().printTag() +
					", Color Reward: ");
			if(c.getColor().getColorReward().isEmpty())
				sb.append("none");
			else {
				for(DTOBonus b : c.getColor().getColorReward())
					sb.append(b + ", ");
				sb.setLength(sb.length() - 2);
			}
			sb.append("\n");
					
			//BONUS CITTA'
			sb.append("\t\tCity's Bonus: ");
			if(c.getBonuses().isEmpty())
				sb.append("none");
			else {
				for(DTOBonus b : c.getBonuses())
					sb.append(b + ", ");
				sb.setLength(sb.length() - 2);
			}
			sb.append("\n");
			
			sb.append("\t\tCity's Emporiums: ");
			if(c.getEmporiums().isEmpty())
				sb.append("none");
			else { 
				for(DTOEmporium emp : c.getEmporiums())
					sb.append(emp.getColor().printTag() + ", ");
				sb.setLength(sb.length() - 2);
			}
			sb.append("\n");
		}
		sb.append("\n");
		
		return sb;
	}

	/**
	 * @return the DTOBoard related to this GameStatus
	 */
	public DTOBoardCD4 getBoard() {
		return board;
	}
	
	/**
	 * @return the requesting player
	 */
	public DTOPlayerCD4 getRequestingPlayer() {
		return requestingPlayer;
	}

	/**
	 * @return the other players who are taking part in the game
	 */
	public List<DTOPlayerCD4> getOtherPlayers() {
		return otherPlayers;
	}
	
	/**
	 * is the match running?
	 * @return true or false
	 */
	public boolean isRunning() {
		return this.isRunning;
	}
	
	/**
	 * @return the sorted List of DTO players who took part in the game
	 * in order to show their rank
	 */
	public List<DTOPlayerCD4> getHighScorers() {
		return this.highScores;
	}
	
}
