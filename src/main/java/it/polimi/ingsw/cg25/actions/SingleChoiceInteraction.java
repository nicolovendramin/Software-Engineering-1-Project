package it.polimi.ingsw.cg25.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * 
 * @author nicolo
 *
 * @param <C> The type of the object of the choice
 */
public class SingleChoiceInteraction<C extends Serializable> extends Interaction{

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = -2954141885603509304L;
	/**
	 * A List of serializable objects to choose among
	 */
	private List<C> choices;
	/**
	 * The chosen object
	 */
	private C reply;
	
	/**
	 * SingleChoiceInteraction class constructor
	 * @param question the question to display
	 * @param options the List of options you can choose among
	 */
	public SingleChoiceInteraction(String question, List<C> options)
	{
		this(options);
		super.setQuestion(question);
	}
	
	/**
	 * Creates a new SingleChoiceInteraction filtering the options of the given one
	 * using the specified predicate
	 * @param former is the SingleChoiceInteraction whose options we want to filter
	 * @param filter is the predicate we want to use as filter
	 */
	public SingleChoiceInteraction(SingleChoiceInteraction<C> former, Predicate<C> filter)
	{
		super.setQuestion(former.getQuestion());
		List<C> newChoices = new ArrayList<>();
		former.choices.stream().filter(filter).forEach(newChoices::add);
		if(!newChoices.isEmpty())
			this.choices = newChoices;
		else
			this.choices = former.choices;
	}
	
	public SingleChoiceInteraction(List<C> options)
	{
		this.choices = options;
		if(options==null || options.isEmpty())
			throw new IllegalArgumentException("You have nothing to choose between");
		super.setQuestion("Choose one of the following elements");
	}
	
	@Override
	public void registerReply(String reply) {
		try{
			int index = Integer.parseInt(reply);
			this.reply = choices.get(index);
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException(e);
		}		
	}
	
	@Override
	public String printOptions() {
		StringBuilder s = new StringBuilder(this.getQuestion());
		for(int i=0;i<choices.size();i++)
		{
			s.append("\n").append(Integer.toString(i)).append(")").append(choices.get(i).toString());
		}
		return s.toString();
	}

	public List<C> getChoices(){
		return this.choices;
	}

	public C getReply() {
		if(this.reply == null) 
			throw new NullPointerException("The reply has not been registered yet");
		return this.reply;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((choices == null) ? 0 : choices.hashCode());
		result = prime * result + ((reply == null) ? 0 : reply.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object o) {
		try{
			if(o==null)
			{
				return false;
			}
			if(o.getClass() != SingleChoiceInteraction.class)
			{
				return false;
			}
			@SuppressWarnings("unchecked")
			SingleChoiceInteraction<C> e = (SingleChoiceInteraction<C>)o;
			return e.choices.equals(this.choices);
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public void registerReply(Interaction i) {
		if(i==null)
			throw new IllegalArgumentException();
		if(i.getClass()!=this.getClass())
			throw new IllegalArgumentException();
		else
		{
			@SuppressWarnings("unchecked")
			SingleChoiceInteraction<C> c = (SingleChoiceInteraction<C>)i;
			reply = c.reply;
		}
	}

}
