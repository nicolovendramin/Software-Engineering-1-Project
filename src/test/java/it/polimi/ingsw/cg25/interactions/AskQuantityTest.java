package it.polimi.ingsw.cg25.interactions;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.AskQuantityInteraction;
import it.polimi.ingsw.cg25.actions.Interaction;

public class AskQuantityTest {
	AskQuantityInteraction interaction;
	
	@Before
	public void setUp() throws Exception {
		interaction = new AskQuantityInteraction();
	}

	@Test
	public void testHashCode() {
		int tese = interaction.hashCode();
		interaction.registerReply("0");
		assertFalse(tese!=interaction.hashCode());
	}

	@Test
	public void testRegisterReplyString() {
		
	}

	@Test
	public void testPrintOptions() {
		assertEquals(interaction.printOptions(), "Insert the amount");
	}

	@Test
	public void testEqualsObject() {
		AskQuantityInteraction inter = new AskQuantityInteraction();
		assertEquals(interaction,inter);
		inter.registerReply("0");
		assertEquals(inter,interaction);
		assertFalse(inter.equals("CIAO"));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRegisterReplyInteraction() {
		Interaction inte = new AskQuantityInteraction("Che voi",12);
		interaction.registerReply(inte);
	}
	
	@Test 
	public void testRegisterReplyInteractionRight() {
		Interaction inte = new AskQuantityInteraction();
		inte.registerReply("0");
		interaction.registerReply(inte);
		assertTrue(interaction.getReply()==0);
	}

	@Test
	public void testAskQuantityInteraction() {
		assertTrue(interaction!=null);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testAskQuantityInteractionnullString() {
		interaction = new AskQuantityInteraction(null);
	}

	@Test
	public void testAskQuantityInteractionString() {
		interaction = new AskQuantityInteraction("ciao");
		assertEquals("ciao", interaction.printOptions());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAskQuantityInteractionStringInt() {
		interaction = new AskQuantityInteraction("ciao", 12);
		interaction.registerReply("13");
	}
	
	@Test
	public void testAskQuantityInteractionStringRightInt() {
		interaction = new AskQuantityInteraction("ciao", 12);
		interaction.registerReply("11");
		assertTrue(interaction.getReply()==11);
	}
	

	@Test(expected = IllegalArgumentException.class)
	public void testAskQuantityInteractionnullStringInt() {
		interaction = new AskQuantityInteraction(null, 12);
	}

	
	@Test (expected = NoSuchElementException.class)
	public void testGetReplyBeforeRegistration() {
		interaction.getReply();
	}
	
	@Test
	public void testEqualsWithsameObject(){
		assertEquals(interaction,interaction);
		assertFalse(interaction.equals(null));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRegisterNegativeReply(){
		interaction.registerReply("-1");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRegisterNotNumberReply(){
		interaction.registerReply("ad");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void constructAskQuantityInteractionWithNegativeBound(){
		interaction = new AskQuantityInteraction("Ciao", -2);
	}

	
}
