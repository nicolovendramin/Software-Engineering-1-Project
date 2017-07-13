package it.polimi.ingsw.cg25.communication;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.communication.UnserializedVectorPacket;

public class UnserializedVectorPacketTest {

	private UnserializedVectorPacket<String> p;
	
	@Before
	public void setUp() {
		//Content, type, destination, source
		p = new UnserializedVectorPacket<>("Message", "Action", 3, 2);
	}
	
	@Test
	public void testGetPublicSource() {
		assertEquals(p.getPublicSource(), 2);
	}
	
	@Test
	public void testGetDestination() {
		assertEquals(p.getDestination(), 3);
	}
	
	@Test
	public void testGetType() {
		assertEquals(p.getType(), "Action");
	}
	
	@Test
	public void testGetContent() {
		assertEquals(p.getContent(3), "Message");
		assertEquals(p.getContent(), "Message");
	}

}
