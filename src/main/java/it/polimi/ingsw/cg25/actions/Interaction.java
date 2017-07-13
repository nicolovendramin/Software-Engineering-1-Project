package it.polimi.ingsw.cg25.actions;

import java.io.Serializable;

/**
 * 
 * @author nicolo
 *
 */
public abstract class Interaction implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5321267526403773805L;
	/**
	 * The question of the interaction
	 */
	private String question;
	
	/**
	 * getter method for the question attribute
	 * @return the value of the question attribute
	 */
	public String getQuestion(){
		return this.question;
	}
	
	/**
	 * 
	 * @param question is the value that you want to set for the question attribute
	 */
	public void setQuestion(String question){
		if(question == null)
			throw new IllegalArgumentException();
		this.question = question;
	}
	
	/**
	 *  
	 * @param reply is the String in which the reply is encoded
	 */
	public abstract void registerReply(String reply);
	
	/**
	 * 
	 * @return the string representation of the interaction including both the questions and, if existing, the options
	 */
	public abstract String printOptions();
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		return result;
	}

	@Override
	public abstract boolean equals(Object obj);
	
	/**
	 * This method register the reply to the question of the interaction
	 * @param i the interaction in which the reply has been saved
	 */
	public abstract void registerReply(Interaction i);

	
}
