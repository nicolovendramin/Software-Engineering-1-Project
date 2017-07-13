package it.polimi.ingsw.cg25.model.dto.dtobonus;

import it.polimi.ingsw.cg25.model.dashboard.bonus.AssistantBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CityBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.CoinBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.DrawPoliticsCardBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.MainActionBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.NobilityPointBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.PermitCardBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.ReusePermitBonus;
import it.polimi.ingsw.cg25.model.dashboard.bonus.VictoryPointBonus;

/**
 * 
 * The purpouse of this interface is that of containing methods useful to
 * realize a visitor pattern (in this case related to bonuses). Here the are 
 * all the necessary to create any kind of dto bonus starting from a normal bonus
 * by using the overloading feature provided in Java language.
 * 
 * @author Giovanni
 *
 */
public interface BonusVisitor {

	public DTOAssistantBonus visit(AssistantBonus bonus);
	
	public DTOCityBonus visit(CityBonus bonus);
	
	public DTOCoinBonus visit(CoinBonus bonus);
	
	public DTODrawPoliticsCardBonus visit(DrawPoliticsCardBonus bonus);
	
	public DTOMainActionBonus visit(MainActionBonus bonus);
	
	public DTONobilityPointBonus visit(NobilityPointBonus bonus);
	
	public DTOPermitCardBonus visit(PermitCardBonus bonus);
	
	public DTOReusePermitBonus visit(ReusePermitBonus bonus);
	
	public DTOVictoryPointBonus visit(VictoryPointBonus bonus);
	
}
