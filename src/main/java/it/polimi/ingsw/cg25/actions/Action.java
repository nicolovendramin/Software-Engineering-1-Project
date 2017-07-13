package it.polimi.ingsw.cg25.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.gamegenerics.ActionBasedGame;

/**
 * 
 * @author nicolo
 *
 */
public abstract class Action {
	/**
	 * The action is made up of this List of interactions
	 */
	private List<Interaction> interactions;
	/**
	 * The index of the current executed interaction
	 */
	private int executingInteraction;
	/**
	 * Stores the information about whether the action has been filled or has been aborted
	 */
	private boolean aborted;
	/**
	 * This constructor just links the action to its model and initialize an
	 * empty interaction list
	 * 
	 */
	public Action() {
		this.interactions = new ArrayList<>();
	}

	/**
	 * Simple getter for the model field
	 * 
	 * @return the model attribute of the Action
	 */
	public abstract ActionBasedGame getModel();

	public abstract Action doAction() throws CannotPerformActionException;

	/**
	 * Tells if the interactions to be solved before launching the action
	 * execution are over
	 * 
	 * @return true when you still have at least one interaction to perform
	 */
	public boolean hasNext() {
		boolean toBeReturned;
		try {
			if(this.executingInteraction>=this.interactions.size())
				return false;
			if (this.interactions.get(executingInteraction) == null)
				toBeReturned = false;
			else
				toBeReturned = true;
		} catch (IndexOutOfBoundsException e) {
			toBeReturned = false;
			this.getModel().getLogger().log(e, "executing Interaction is greater than number of Interactions");
		}
		return toBeReturned;
	}

	/**
	 * @return the next interaction to be executed
	 */
	public Interaction next() {
		try {
			return this.interactions.get(executingInteraction);
		} catch (IndexOutOfBoundsException e) {
			this.getModel().getLogger().log(e, "executing Interaction is greater than number of Interactions");
			throw new NoSuchElementException("no more interactions for this action");
		}
	}

	/**
	 * Adds the given interactions to the one of the Action
	 * @param i the interaction to add
	 */
	public void setInteraction(Interaction i) {
		this.interactions.add(i);
	}

	/**
	 * Default (void) implementation for the setup method for all those Actions who doesn't 
	 * need a specific setup
	 */
	public void setup() throws CannotSetupActionException {
		return;
	}

	/**
	 * This methods sends the received reply to the currently executing
	 * interaction, if the parsing in the interaction fails it throws the
	 * exception
	 * 
	 * @param reply
	 *            is the interaction filled and received
	 * @throws IllegalArgumentException
	 *             thrown when the parsing in the interaction fails or the
	 *             String has no sense for the current Interaction
	 */
	public void sendReply(Interaction reply) {
		if(reply != null && reply.getQuestion().equals("[[Abort]]\n"))
			this.abort();
		else if (interactions.get(executingInteraction).equals(reply)) {
			this.interactions.get(executingInteraction).registerReply(reply);
			executingInteraction++;
		} else
			return;
	}

	/**
	 * @return the list of interactions of the executing action
	 */
	public List<Interaction> getInteractions() {
		return this.interactions;
	}

	/**
	 * Aborts the action
	 */
	public void abort(){
		this.interactions .clear();
		this.aborted = true;
	}
	
	public boolean isAborted(){
		return aborted;
	}
	/**
	 * 
	 * @param i
	 *            the index of the iteration that you want to be returned
	 * @return the ith iteration
	 */
	public Interaction getInteraction(int i) {
		return this.interactions.get(i);
	}
	
	
}
