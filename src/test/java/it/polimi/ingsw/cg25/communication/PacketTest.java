package it.polimi.ingsw.cg25.communication;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.communication.Packet;

public class PacketTest {
	
	private Packet<String> p;
	private Packet<String> p1;
	private Packet<String> p2;
	
	@Before
	public void setUp() throws Exception {
		p = new Packet<>("Message", "Action", 1);
		p1 = new Packet<>("Message1", "Action");
		p2 = new Packet<>("Message2", "Flow", -1);
	}

	@Test
	public void testGetType() {
		assertEquals(p.getType(), "Action");
		assertEquals(p1.getType(), "Action");
		assertEquals(p2.getType(), "Flow");
	}
	
	@Test(expected=IllegalAccessException.class)
	public void testGetDestination() throws IllegalAccessException {
		p.getDestination();
	}
	
	@Test(expected=IllegalAccessException.class)
	public void testSetPublicSource() throws IllegalAccessException {
		p.setPublicSource(1);
	}
	
	@Test(expected=IllegalAccessException.class)
	public void testSign() throws IllegalAccessException {
		p.sign(p1);
	}
	
	@Test
	public void testIsForMe() {
		assertTrue(p.isForMe(1));
		assertFalse(p.isForMe(2));
		
		//Broadcast packet always returns true
		assertTrue(p1.isForMe(1));
		assertTrue(p1.isForMe(-1));
		
		//View 1 is excluded
		assertFalse(p2.isForMe(1));
		assertTrue(p2.isForMe(2));
	}
	
	@Test(expected=IllegalAccessException.class)
	public void testCantGetContent() throws IllegalAccessException {
		p.getContent(2);
	}
	
	@Test
	public void testGetContent() throws IllegalAccessException {
		assertEquals(p.getContent(1), "Message");
		assertEquals(p1.getContent(1), "Message1");
		assertEquals(p2.getContent(2), "Message2");
	}

}
