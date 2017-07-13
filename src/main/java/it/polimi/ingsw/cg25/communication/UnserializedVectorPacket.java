package it.polimi.ingsw.cg25.communication;
/**
 * 
 * @author nicolo
 *
 * @param <T> is the tyoe of the content of the packets. No limits for this type
 */
public class UnserializedVectorPacket<T> {
	
	private T content;
	private final int destination;
	private final int source;
	private final String type;
	
	/**
	 * Constructor for the UnserializedVectorPacket
	 * @param content the content of the packet
	 * @param type the type labelling the packet
	 * @param destination the id of the destination view
	 * @param source the id of the source view
	 */
	public UnserializedVectorPacket(T content, String type, int destination, int source) {
		this.content = content;
		this.destination = destination;
		this.source = source;
		this.type = type;
	}
	
	/**
	 * Gets the source of the packet
	 * @return the source of this packet
	 */
	public int getPublicSource(){
		return this.source;
	}
	
	/**
	 * Gets the destination of the packet
	 * @return the destination of this packet
	 */
	public int getDestination(){
		return this.destination;
	}
	
	/**
	 * Gets the type of the packet
	 * @return the type of the packet
	 */
	public String getType(){
		return this.type;
	}
	
	/**
	 * Gets the content of this packet
	 * @return the content of the packet
	 */
	public T getContent(){
		return this.content;
	}
	
	/**
	 * Gets the content of this packet. Just to uniform to the packet interface
	 * and allow compatibility
	 * @return the content of the packet
	 */
	public T getContent(int id){
		return this.content;
	}
	
}
