package it.polimi.ingsw.cg25.actions;

import java.io.Serializable;
/**
 * 
 * @author nicolo
 *
 * @param <T> is the type of the content 
 */
public class DisplayInteraction<T extends Serializable> extends Interaction{
	
	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -6952387647348493852L;
	
	/**
	 * The content of the displayInteraction
	 */
	private T contentToDisplay;
	
	/**
	 * Constructor for the displayInteraction
	 * @param message is the question of the interaciont
	 * @param content is the content to display
	 */
	public DisplayInteraction(String message, T content){
		if(message == null || content == null)
			throw new IllegalArgumentException("No null content nor message is allowed");
		this.setQuestion(message.concat(message.equals("") ? "" : "\n"));
		this.contentToDisplay = content;
	}
	
	/**
	 * Simple constructor with no message
	 * @param content is the content that the interaction has to display
	 */
	public DisplayInteraction(T content){
		this("",content);
	}
	
	@Override
	public void registerReply(String reply) {
		return;
	}

	public void registerReply(){
		return;
	}

	@Override
	public String printOptions() {
		return this.getQuestion().concat(this.contentToDisplay.toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((contentToDisplay == null) ? 0 : contentToDisplay.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		return true;
	}

	@Override
	public void registerReply(Interaction i) {
		if(i==null)
			throw new IllegalArgumentException();
		if(i.getClass()!=this.getClass())
			throw new IllegalArgumentException();
		else
		{
			return;
		}
	}
	
	/**
	 * This method returns the content of the display interaction
	 * @return
	 */
	public T getContent(){
		return this.contentToDisplay;
	}
}
