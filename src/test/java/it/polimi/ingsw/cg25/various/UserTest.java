package it.polimi.ingsw.cg25.various;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.onlinegenerics.User;

public class UserTest {

	private User user;
	
	@Before
	public void setUp() {
		user = new User(1, "nome");
	}
	
	@Test
	public void testSetStatus() {
		user.setStatus(false);
		assertFalse(user.isActive());
	}
	
	@Test
	public void testGetUserId() {
		assertEquals(user.getUserID(), 1);
	}
	
	@Test
	public void testGetName() {
		assertEquals(user.getName(), "nome");
	}
	
	@Test
	public void testSetName() {
		user.rename("player");
		assertEquals(user.getName(), "player");
	}
	
	@Test
	public void testToString() {
		assertTrue(user.toString().equals("User [userID=" + user.getUserID() + ", active=" + user.isActive() + "]" ));
	}
	
}
