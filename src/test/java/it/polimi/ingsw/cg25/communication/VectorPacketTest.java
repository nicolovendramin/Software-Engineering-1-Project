package it.polimi.ingsw.cg25.communication;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.communication.VectorPacket;

public class VectorPacketTest {

	private VectorPacket<String> p;
	
	@Before
	public void setUp() {
		//Content, type, destination, source
		p = new VectorPacket<>("Message", "Flow", 1, 2);
	}

	@Test
	public void testSetPublicSource() {
		p.setPublicSource(3);
		assertEquals(p.getPublicSource(), 3);
	}
	
	@Test
	public void testGetPublicSource() {
		assertEquals(p.getPublicSource(), 2);
	}
	
	@Test
	public void testGetDestination() {
		assertEquals(p.getDestination(), 1);
	}
	
	@Test
	public void testGetContent() {
		assertEquals(p.getContent(), "Message");
		assertEquals(p.getContent(1), "Message");
	}
	
}
