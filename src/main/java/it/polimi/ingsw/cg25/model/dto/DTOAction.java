package it.polimi.ingsw.cg25.model.dto;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg25.actions.Action;
import it.polimi.ingsw.cg25.actions.ActionFabric;
import it.polimi.ingsw.cg25.gamegenerics.ActionBasedGame;
/**
 * 
 * @author nicolo
 *
 */
public class DTOAction implements DTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2109699360725928285L;
	/**
	 * The string that represents the action
	 */
	private String action;
	
	/**
	 * Simple constructor for the DTOAction
	 * @param a is the action to be DTO-ized
	 */
	public DTOAction(Action a){
		this.action = a.toString();
	}
	
	/**
	 * Returns the string representation of this object
	 * @return the action field
	 */
	public String getAction(){
		return this.action;
	}
	
	/**
	 * Static method to convert a list of actions and obtain a list of DTOAction
	 * @param actions is the list of actions to convert
	 * @return the Converted actions in a list
	 */
	public static List<DTOAction> convertAll(List<? extends Action> actions){
		List<DTOAction> converted = new ArrayList<>();
		for(Action a : actions)
			converted.add(new DTOAction(a));
		return converted;
	}
	
	/**
	 * Method which decodes a dtoAction and returns the corresponding real action
	 * @param model is the action based for which to which the DTOAction refers
	 * @return the action of the model specified which represent this in real world
	 */
	public Action decode(ActionBasedGame model){
		ActionFabric fabric = new ActionFabric(model);
		return fabric.produce(this.action);
	}
	
	@Override
	public String toString(){
		return this.action;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object o){
		if(o==null)
			return false;
		if(o.getClass() == Action.class)
			return o.toString().equals(action);
		if(o.getClass() == DTOAction.class)
			return o.toString().equals(action);
		
		return false;
	}
	

}
