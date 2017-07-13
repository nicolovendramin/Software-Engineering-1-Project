package it.polimi.ingsw.cg25.actions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.Predicate;

/**
 * 
 * @author nicolo
 *
 * @param <C> the Type of the object between which to choose
 */
public class MultipleChoiceInteraction<C extends Serializable> extends Interaction {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = 6894972788381212623L;
	/**
	 * A List of serializable objects to choose among
	 */
	private List<C> choices;
	/**
	 * The List of chosen objects
	 */
	private List<C> reply;
	/**
	 * The minimum number of objects you can choose
	 */
	private int lowerBound = -1;
	/**
	 * The maximum number of objects you can choose
	 */
	private int upperBound = -1;
	
	/**
	 * Constructor which sets the options to the given one and the question to the default question
	 * @param options are th options between to choose
	 * @throws IllegalArgumentException when the options are null or empty
	 */
	public MultipleChoiceInteraction(List<C> options) {
		this.setQuestion("Choose between the following elements");
		if(options==null || options.isEmpty())
			throw new IllegalArgumentException("Too few options");
		this.choices = options;
	}
	

	/**
	 * Creates a new MultipleChoiceInteraction filtering the options of the given one
	 * using the specified predicate
	 * @param former is the SingleChoiceInteraction whose options we want to filter
	 * @param filter is the predicate we want to use as filter
	 */
	public MultipleChoiceInteraction(MultipleChoiceInteraction<C> former, Predicate<C> filter)
	{
		List<C> newChoices = new ArrayList<>();
		former.choices.stream().filter(filter).forEach(newChoices::add);
		
		if(newChoices.isEmpty())
			this.choices = former.choices;
		else {
			if(newChoices.size() >= this.lowerBound)
				this.choices = newChoices;
			else
				this.choices = former.choices;	
			this.lowerBound = former.lowerBound;
			this.upperBound = former.upperBound;
		}
	}
	
	/**
	 * Creates the interaction specifying both the options and the question
	 * @param question is the question of the Interaction
	 * @param options are the elements beetween whoose to choose
	 */
	public MultipleChoiceInteraction(String question, List<C> options)
	{
		this(options);
		super.setQuestion(question);
	}
	
	/**
	 * Creates a new MultipleChoiceInteraction with the bounds for the number of element to choose
	 * @param question is the question of the INteraction
	 * @param options is the group of elements between whose to choose
	 * @param lowB is the min number of elements that can be chosen(set -1 to leave unspecified)
	 * @param upB is the max number of elements that can be chosen (set -1 to leave unspecified)
	 */
	public MultipleChoiceInteraction(String question,List<C> options,int lowB,int upB) throws IllegalArgumentException
	{
		this(question, options);
		this.lowerBound = lowB;
		this.upperBound = upB;
		if(options.size()<lowB)
			throw new IllegalArgumentException("Too few arguments");
		this.setQuestion(questionBounder(question));
	}
	
	/**
	 * This formats the question to specify the bounds
	 * @param question is the new question for the interaction
	 * @return the question with the bounds specified
	 */
	private String questionBounder(String question)
	{
		StringBuilder s = new StringBuilder();
		s.append(question);
		if(lowerBound != -1) 
			s.append("\nNot less than ").append(Integer.toString(lowerBound));
		if(upperBound != -1) 
			s.append(", no more than ").append(Integer.toString(upperBound));
		return s.toString();
	}
	
	public List<C> getChoices(){
		return this.choices;
	}
	
	/**
	 * This is made to register the reply to the interaction
	 * @param reply is the string encoding the reply
	 */
	@Override
	public void registerReply(String reply) {
		//Create a Set of Integers for indexes
		Set<Integer> indexes = new HashSet<>();
		StringTokenizer st = new StringTokenizer(reply,",");
		this.reply = new ArrayList<>();
		
		while(st.hasMoreTokens())
		{
			try {
			String temp = st.nextElement().toString();
			indexes.add(Integer.parseInt(temp));
			}
			catch(NumberFormatException e) {
				throw new IllegalArgumentException("The given input is not valid!", e);
			}
		}
		
		for(Integer i : indexes) {
			try
			{
				this.reply.add(this.choices.get(i));
			}
			catch(IndexOutOfBoundsException e)
			{
				throw new IllegalArgumentException("The given input is not valid!",e);
			}
		}
		if(this.reply.size()>this.upperBound && upperBound> 0) 
		{
			this.reply = null;
			throw new IllegalArgumentException("Too much items selected");
		}
		if(this.reply.size()<this.lowerBound && lowerBound >= 0)
		{
			this.reply = null;
			throw new IllegalArgumentException("You need to select more items");
		}
	}
	
	/**
	 * This method returns a formatted string representation of the interaction
	 */
	@Override
	public String printOptions() {
		StringBuilder s = new StringBuilder(this.getQuestion());
		for(int i=0;i<choices.size();i++)
		{
			s.append("\n").append(Integer.toString(i)).append(")").append(choices.get(i).toString());
		}
		return s.toString();
	}
	
	/**
	 * This method returns the elements chosen as reply for the Interaction
	 * @return the chosen elements
	 * @throws NullPointerException if called before that the reply has been registered
	 */
	public List<C> getReply(){
		if(this.reply == null) 
			throw new NullPointerException("The reply has not been registered yet");
		return this.reply;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((choices == null) ? 0 : choices.hashCode());
		result = prime * result + lowerBound;
		result = prime * result + ((reply == null) ? 0 : reply.hashCode());
		result = prime * result + upperBound;
		return result;
	}

	/**
	 * The equals for Interaction is called to check if the object is equal to this interaction
	 * @param o is the object to be checked
	 * @return true if the object is equal false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if(o==null)
			return false;
		try{
		if(o.getClass() != MultipleChoiceInteraction.class)
			return false;
		@SuppressWarnings("unchecked")
		MultipleChoiceInteraction<C> ot = (MultipleChoiceInteraction<C>)o;
		return ot.choices.equals(this.choices);
		}catch(Exception e){
			return false;
		}
			
	}
	
	/**
	 * This is made to register an equivalent Interaction as a reply for this Interaction
	 * @param i is the Interaction containing the reply
	 */
	@Override
	public void registerReply(Interaction i) {
		if(i==null)
			throw new IllegalArgumentException();
		if(i.getClass()!=this.getClass())
			throw new IllegalArgumentException();
		else
		{
			@SuppressWarnings("unchecked")
			MultipleChoiceInteraction<C> c = (MultipleChoiceInteraction<C>)i;
			reply = c.reply;
		}
	}
	
	/**
	 * Checks if the interaction has some bounds
	 * @return true if at least one between upper and lower bound exists
	 */
	public boolean isBounded(){
		return upperBound>0 || lowerBound>0;
	}
	
	/**
	 * Gets the upper Bound of the interaction.
	 * @return the upper bound or -1 if there is not any uooer bound
	 */
	public int getUpperBound(){
		return this.upperBound;
	}
	
	/**
	 * Gets the lower Bound of the interaction.
	 * @return the upper bound or -1 if there is not any lower bound
	 */
	public int getLowerBound(){
		return this.lowerBound;
	}
}
