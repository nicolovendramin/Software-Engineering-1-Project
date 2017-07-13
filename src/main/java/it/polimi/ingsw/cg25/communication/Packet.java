package it.polimi.ingsw.cg25.communication;

import java.io.Serializable;
/**
 * 
 * @author nicolo
 *
 * @param <T> is the tyoe of the content of the packets. Must be Serializable
 */
public class Packet<T extends Serializable> implements Serializable {

	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = 415188018246828753L;
	/**
	 * The content of the package whose type is T
	 */
	private T content;
	/**
	 * A string to define the packet type. E.g.: chat, game status, action...
	 */
	private String packetType;
	/**
	 * 0 means broadcast, negative IDs represent the single user to be excluded from the access.
	 * As instance: -12 means that all but the user 12 can access the content of the packet
	 */
	private final int destination;

	/**
	 * Packet class constructor. Destination is not specifies and it will
	 * get the default value of 0 (broadcast)
	 * @param content the object linked to this packet
	 * @param type the packet type
	 */
	public Packet(T content, String type) {
		this.content = content;
		this.packetType = type;
		this.destination = 0;
	}
	
	/**
	 * Packet class constructor with destination attribute
	 * @param content the object linked to this packet
	 * @param type the packet type
	 * @param destination the destination of the packet
	 */
	public Packet(T content, String type, int destination) {
		this.content = content;
		this.packetType = type;
		this.destination = destination;
	}	
	
	/**
	 * Function to understand if you are the correct receiver of a message
	 * @param id is the id of the receiver
	 * @return true if the receiver is the destination of the packet, false otherwise
	 */
	public boolean isForMe(int id) {
		return (destination == 0 || id==destination) || (destination<0 && id!=-destination);
	}
	
	/**
	 * This method gives the access to the content of the packet. But only if you are it's receiver
	 * @param id is the id of the component who wants to access the packet
	 * @return the content if the id of the reader is correct
	 * @throws IllegalAccessException if the reader has not capabilities to access the packet
	 */
	public T getContent(int id) throws IllegalAccessException {
		if(!isForMe(id)) {
			throw new IllegalAccessException("You can not get this content");
		}
		else {
			return this.content;
		}
	}
	
	/**
	 * this returns the type of the packet ("Flow","Action",....)
	 * @return the type of the packet
	 */
	public String getType() {
		return this.packetType;
	}
	
	/**
	 * This method is made to get the destination of the packet
	 * @return the destination of the packet
	 * @throws IllegalAccessException if the destination is private
	 */
	public int getDestination() throws IllegalAccessException{
		throw new IllegalAccessException();
	}
	
	/**
	 * This method is made to get the destination of the packet
	 * @throws IllegalAccessException if the public source is private and cannot be changed
	 */
	public void setPublicSource(int viewId) throws IllegalAccessException{
		throw new IllegalAccessException();
	}
	
	/**
	 * This method is made to get the destination of the packet
	 * @param change a packet to be repacked in a signed packet
	 * @return another Packet signed with the id of the parameter
	 * @throws IllegalAccessException if the public source is private and cannot be changed
	 */
	public <C extends Packet<T>> C sign(C change) throws IllegalAccessException{
		throw new IllegalAccessException("Cannot sign a Packet");
	}
	
}
