package it.polimi.ingsw.cg25.actions;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.model.MatchCD4;
/**
 * 
 * @author nicolo
 *
 */
public abstract class QuickAction extends MatchAction{

	public QuickAction(MatchCD4 model) {
		super(model);
	}

	/**
	 * Static method that returns all the possible actions of this type,
	 * initialized on the specified model
	 * @param model
	 * @return the list of all the possible quick actions
	 */
	public static List<QuickAction> getQuickActions(MatchCD4 model){
		List<QuickAction> choices = new ArrayList<>();
		choices.add(new ChangePermitQuickAction(model));
		choices.add(new ElectionQuickAction(model));
		choices.add(new HireAssistantQuickAction(model));
		choices.add(new MainActionQuickAction(model));
		return choices;
	}
}
