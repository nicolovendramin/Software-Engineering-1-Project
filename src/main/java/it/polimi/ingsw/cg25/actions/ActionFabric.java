package it.polimi.ingsw.cg25.actions;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.gamegenerics.ActionBasedGame;
import it.polimi.ingsw.cg25.model.MatchCD4;
import it.polimi.ingsw.cg25.model.trade.Market;
/**
 * 
 * @author nicolo
 *
 */
public class ActionFabric {
	/**
	 * The model used by actions
	 */
	private final ActionBasedGame model;
	
	/**
	 * The constructor needs an ActionBasedModel.
	 * @param model. 
	 * @throws IllegalArgumentException if model is null
	 */
	public ActionFabric(ActionBasedGame model){
		if(model==null)
			throw new IllegalArgumentException();
		this.model = model;
	}
	
	/**
	 * This method is meant to return an Action given its String representation
	 * @param a the string representation of the action that we want to obtain
	 * @return the Action corresponding to the input string
	 */
	public Action produce(String a){
		if(a == null)
			throw new IllegalArgumentException("Need to specify a non-null string");
		List<Action> ac = new ArrayList<>();
		ac.add(new ChooseActionTypeAction<>(model));
		ac.add(new ErrorAction<>(model));
		if(model instanceof MatchCD4)
		{
			MatchCD4 ma = (MatchCD4)model;
			ac.add(new PassAction<MatchCD4>(ma));
			ac.addAll(MainAction.getMainActions(ma));
			ac.addAll(QuickAction.getQuickActions(ma));
			ac.add(new ChooseQuickActionAction(ma));
			ac.add(new ChooseMainActionAction(ma));
			ac.add(new ReusePermitBonusAction(ma));
			ac.add(new PermitCardBonusAction(ma));
			ac.add(new CityBonusAction(ma));
		}
		if( model instanceof Market)
		{
			Market tr = (Market)model;
			ac.add(new BuyingAction(tr));
			ac.add(new SellPermit(tr));
			ac.add(new SellPolitics(tr));
			ac.add(new SellAssistant(tr));
			ac.add(new PassAction<Market>(tr));
		}
		for(Action action: ac)
		{
			if(action.toString().equals(a)) 
				return action;
		}
		throw new IllegalArgumentException("The string does not correspond to a known action!");
	}

}
