package it.polimi.ingsw.cg25;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import it.polimi.ingsw.cg25.model.Assistant;
import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.NobilityRank;
import it.polimi.ingsw.cg25.model.PlayerCD4;
import it.polimi.ingsw.cg25.model.PocketCD4;
import it.polimi.ingsw.cg25.model.VictoryPoint;
import it.polimi.ingsw.cg25.model.dashboard.Party;
import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.Bonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.cards.PermitCard;
import it.polimi.ingsw.cg25.model.dashboard.cards.PoliticsCard;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.parsing.BoardFactory;
import it.polimi.ingsw.cg25.proxies.ModelProxy;

public class ActionTest {
	private static PocketCD4 pocket1;
	public static MatchCD4 model;
	public static ModelProxy proxy;
	public static PlayerCD4 player1;
	public static PlayerCD4 player2;
	public static PlayerCD4 player3;
	public static PlayerCD4 player4;
	public static int unemployedNumber;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		BoardFactory factory = new BoardFactory(new FileReader("src/test/resources/nobilityCells.txt"),
				new FileReader("src/test/resources/politics.txt"),
				new FileReader("src/test/resources/cities.txt"),
				new FileReader("src/test/resources/graph.txt"),
				new FileReader("src/test/resources/king.txt"),
				new FileReader("src/test/resources/regions.txt"));
		proxy = new ModelProxy();
		model = new MatchCD4(factory.getBoard(),proxy,true,10);
		pocket1 = new PocketCD4(new Coin(100),new Assistant(100),new NobilityRank(1),new VictoryPoint(0));
		PocketCD4 pocket2 = new PocketCD4(new Coin(100),new Assistant(100),new NobilityRank(1),new VictoryPoint(0));
		PocketCD4 pocket3 = new PocketCD4(new Coin(100),new Assistant(100),new NobilityRank(1),new VictoryPoint(0));
		PocketCD4 pocket4 = new PocketCD4(new Coin(100),new Assistant(100),new NobilityRank(1),new VictoryPoint(0));
		player1 = new PlayerCD4(1,"gio",HSBColor.getNDifferent(2).get(0),model,pocket1);
		player2 = new PlayerCD4(2, "nicolo", HSBColor.getNDifferent(2).get(1), model,pocket2);
		player3 = new PlayerCD4(3,"dado",HSBColor.getNDifferent(2).get(0),model,pocket3);
		player4 = new PlayerCD4(4, "marco", HSBColor.getNDifferent(2).get(1), model,pocket4);
		player4.setStatus(true);
		player1.setStatus(true);
		player3.setStatus(true);
		player2.setStatus(true);
		model.addPlayer(player1);
		model.addPlayer(player2);
		model.addPlayer(player3);
		model.addPlayer(player4);
		unemployedNumber = model.getBoard().getUnemployedCouncelors().size();
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0,0,0),true)));
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0,0,0),true)));
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0,0,0),true)));
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0,0,0),true)));
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0,0,0),true)));
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0,0,0),true)));
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0,0,0),true)));
		model.getCurrentPlayer().addPoliticsCard(new PoliticsCard(new Party(new HSBColor(0,0,0),true)));
		List<City> cities = new ArrayList<>();
		List<Bonus> bonuses = new ArrayList<>();
		bonuses.add(new CoinBonus(19));
		cities.add(model.getBoard().getCities().get(0));
		model.getCurrentPlayer().getPermitsToBeUsed().add(new PermitCard(cities,bonuses));
		List<Bonus> bonuses2 = new ArrayList<>(bonuses);
		bonuses2.add(new AssistantBonus(20));
		model.getCurrentPlayer().getUsedPermits().add(new PermitCard(cities,bonuses2));
	}
	
	public MatchCD4 getModel(){
		return model;
	}
	
	@AfterClass
	public static void tearDownAfterClass(){
		model.getLogger().close();
	}
}
