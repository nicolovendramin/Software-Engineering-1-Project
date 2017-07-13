package it.polimi.ingsw.cg25.actions;

import java.util.NoSuchElementException;
/**
 * 
 * @author nicolo
 *
 */
public class AskQuantityInteraction extends Interaction {

		/**
		 * Serial number for serialization
		 */
		private static final long serialVersionUID = -8897091257409742127L;
		/**
		 * the price of the product, -1 if not set yet
		 */
		private int price = -1;
		/**
		 * the upper bound specifies the max number of objects the player
		 * wants to insert in a product
		 */
		private int upperBound=-1;
		
		/**
		 * This constructor creates a default AskQuantityInteraction without upper bound and with
		 */
		public AskQuantityInteraction(){
			super.setQuestion("Insert the amount");
		}
		
		/**
		 * This constructor builds the interaction with no upper bound and using the given message as question
		 * @param message is the question you want to set for the interaction
		 */
		public AskQuantityInteraction(String message){
			this.upperBound = -1;
			super.setQuestion(message);
		}
		
		/**
		 * This constructor build the interaction with a custom message and a limited upper bound for the reply
		 * @param message is the question you want to set for the interaction
		 * @param upperBound is the maximum quantity that can be registered as a reply
		 */
		public AskQuantityInteraction(String message, int upperBound){
			if(upperBound<=0)
				throw new IllegalArgumentException("The upper Bound must be at least greater than zero!");
			this.upperBound = upperBound;
			super.setQuestion(message);
		}
		
		/**
		 * This method never returns a negative element. If the reply has not been registered yet
		 * it throws a NoSuchElementException
		 * @return the reply to the askQuantityInteraction, if registered
		 * @throws NoSuchElementException if the reply has not been registered yet
		 */
		public int getReply(){
			if(this.price<0)
				throw new NoSuchElementException("The reply has not been registered yet");
			return this.price;
		}
		
		/**
		 * In this interaction the reply is registered
		 */
		@Override
		public void registerReply(String reply) {
			try{
				int tempPrice = Integer.parseInt(reply);
				if(tempPrice<0)
					throw new IllegalArgumentException("The quantity must be major than zero");
				if(this.upperBound>0 && tempPrice > this.upperBound)
					throw new IllegalArgumentException("Too high. Fly down!");
				this.price = tempPrice;
			}catch(NumberFormatException e){
				throw new IllegalArgumentException("You are asked to type a number",e);
			}
		}

		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + upperBound;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AskQuantityInteraction other = (AskQuantityInteraction) obj;
			if (upperBound != other.upperBound)
				return false;
			return true;

		}

		@Override
		public void registerReply(Interaction i) {
			if(i == null || !i.equals(this))
				throw new IllegalArgumentException();
			else
			{
				AskQuantityInteraction casted = (AskQuantityInteraction)i;
				this.price = casted.price;
			}
		}

		@Override
		public String printOptions() {
			return this.getQuestion();
		}
		
}
