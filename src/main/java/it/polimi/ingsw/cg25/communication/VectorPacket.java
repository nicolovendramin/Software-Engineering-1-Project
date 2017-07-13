package it.polimi.ingsw.cg25.communication;

import java.io.Serializable;
/**
 * 
 * @author nicolo
 *
 * @param <T> is the tyoe of the content of the packets. Must be Serializable
 */
public class VectorPacket<T extends Serializable> extends Packet<T> {
	
	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = 4713311210359875101L;
	private final int publicDestination;
	private int publicSource;
	
	/**
	 * This is the constructor for this public packet
	 * @param content is what you want to send (must be the specified type)
	 * @param type is the label you want to put on the packet
	 * @param destination is the id of the destination
	 * @param source is the id of the sender
	 */
	public VectorPacket(T content, String type, int destination, int source) {
		super(content, type);
		this.publicDestination = destination;
		this.publicSource = source;
	}
	
	@Override
	public void setPublicSource(int viewId){
		this.publicSource = viewId;
	}
	
	/**
	 * Returns the public source of the packet 
	 */
	public int getPublicSource(){
		return this.publicSource;
	}
	
	@Override
	public int getDestination(){
		return this.publicDestination;
	}
	
	/**
	 * This is the getter for the content. In this case it cannot Throw exceptions
	 * because the packet is public
	 * @return the content of the packet, or null if it was illegally casted
	 */
	public T getContent(){
		try{
			return super.getContent(0);
		}catch(IllegalAccessException e){
			return null;
		}
	}
	
	@Override
	public T getContent(int id){
		return this.getContent();
	}
	
}
