package it.polimi.ingsw.cg25.actions;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.model.MatchCD4;

/**
 * 
 * @author nicolo
 *
 */
public abstract class MainAction extends MatchAction{
	
	/**
	 * MainAction class constructor
	 * @param model the current match where actions are performed
	 */
	public MainAction(MatchCD4 model) {
		super(model);
	}
	
	/**
	 * @param model a reference to a MatchCD4 object 
	 * @return the different possible main actions the player can perform
	 */
	public static List<MainAction> getMainActions(MatchCD4 model){
		List<MainAction> choices = new ArrayList<>();
		choices.add(new PermitAction(model));
		choices.add(new ElectionAction(model));
		choices.add(new BuildWithPermitAction(model));
		choices.add(new BuildWithKingAction(model));
		return choices;
	}
	
}
