package it.polimi.ingsw.cg25.various;

import static org.junit.Assert.*;
import org.junit.Test;

import it.polimi.ingsw.cg25.exceptions.CannotCreateGameException;
import it.polimi.ingsw.cg25.exceptions.CannotPassException;
import it.polimi.ingsw.cg25.exceptions.CannotPerformActionException;
import it.polimi.ingsw.cg25.exceptions.CannotSetupActionException;
import it.polimi.ingsw.cg25.exceptions.ElementNotFoundException;
import it.polimi.ingsw.cg25.exceptions.EndGameException;
import it.polimi.ingsw.cg25.exceptions.NoCardsException;
import it.polimi.ingsw.cg25.exceptions.NoGoodCardsException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughAssistantsException;
import it.polimi.ingsw.cg25.exceptions.NotEnoughCoinException;
import it.polimi.ingsw.cg25.exceptions.OneEmporiumOnlyException;

/**
 * 
 * @author Giovanni
 *
 */
public class ExceptionTest {

	private String msg = "msg";
	
	@Test
	public void testCannotPerformActionException() {
		try {
			throw new CannotPerformActionException();
		} catch(CannotPerformActionException e) {
			assertEquals(e.getMessage(), "The action couldn't be performed. Try again.");
		}
		
		try {
			throw new CannotPerformActionException(msg);
		} catch(CannotPerformActionException e) {
			assertEquals(e.getMessage(), msg);
		}
		
		try {
			throw new CannotPerformActionException(msg, new IllegalArgumentException());
		} catch(CannotPerformActionException e) {
			assertEquals(e.getMessage(), msg);
			assertEquals(e.getCause().getClass(), IllegalArgumentException.class);
		}
	}
	
	@Test
	public void testOneEmporiumOnlyException() {
		try {
			throw new OneEmporiumOnlyException();
		} catch(OneEmporiumOnlyException e) {
			assertEquals(e.getMessage(), "You cannot build more than one emporium!");
		}
		
		try {
			throw new OneEmporiumOnlyException(msg);
		} catch(OneEmporiumOnlyException e) {
			assertEquals(e.getMessage(), msg);
		}
	}
	
	@Test
	public void testCannotSetupActionException() {
		try {
			throw new CannotSetupActionException();
		} catch(CannotSetupActionException e) {
			assertEquals(e.getMessage(), "There was an error setting up this Action. Try again");
		}
		
		try{
			throw new CannotSetupActionException(msg);
		} catch(CannotSetupActionException e) {
			assertEquals(e.getMessage(), msg);
		}
		
		try {
			throw new CannotSetupActionException(msg, new IllegalArgumentException());
		} catch(CannotSetupActionException e) {
			assertEquals(e.getMessage(), msg);
			assertEquals(e.getCause().getClass(), IllegalArgumentException.class);
		}
	}
	
	@Test
	public void testNotEnoughCoinException() {
		try {
			throw new NotEnoughCoinException(msg);
		} catch(NotEnoughCoinException e){
			assertEquals(e.getMessage(), msg);
		}
		
		try {
			throw new NotEnoughCoinException();
		} catch(NotEnoughCoinException e) {
			assertEquals(e.getMessage(), "Bounced payment!");
		}
	}
	
	@Test
	public void testEndGameException() {
		try {
			throw new EndGameException(msg);
		} catch(EndGameException e){
			assertEquals(e.getMessage(), msg);
		}
	}
	
	@Test
	public void testNoCardsException() {
		try {
			throw new NoCardsException(msg);
		} catch(NoCardsException e){
			assertEquals(e.getMessage(), msg);
		}
		
		try {
			throw new NoCardsException();
		} catch(NoCardsException e){
			assertEquals(e.getMessage(), "No cards are left.");
		}
	}
	
	@Test
	public void testNoGoodCardsException() {
		try {
			throw new NoGoodCardsException(msg);
		} catch(NoGoodCardsException e) {
			assertEquals(e.getMessage(), msg);
		}
		
		try {
			throw new NoGoodCardsException();
		} catch(NoGoodCardsException e) {
			assertEquals(e.getMessage(), "You cannot satisfy the council!");
		}
	}
	
	@Test
	public void testNotEnoughAssistantsException() {
		try {
			throw new NotEnoughAssistantsException(msg);
		} catch(NotEnoughAssistantsException e) {
			assertEquals(e.getMessage(), msg);
		}
		
		try {
			throw new NotEnoughAssistantsException();
		} catch(NotEnoughAssistantsException e) {
			assertEquals(e.getMessage(), "You don't have enough assistants!");
		}
	}
	
	@Test
	public void testCannotCreateGameException() {	
		try {
			throw new CannotCreateGameException();
		} catch(CannotCreateGameException e) {
			assertEquals(e.getMessage(), "Game's creation failed.");
		}
		
		try {
			throw new CannotCreateGameException(msg);
		} catch(CannotCreateGameException e) {
			assertEquals(e.getMessage(), msg);
		}
		
		try {
			throw new CannotCreateGameException(msg, new IllegalArgumentException());
		} catch(CannotCreateGameException e) {
			assertEquals(e.getMessage(), msg);
			assertEquals(e.getCause().getClass(), IllegalArgumentException.class);
		}
	}
	
	@Test
	public void testElementNotFoundException() {	
		try {
			throw new ElementNotFoundException();
		} catch(ElementNotFoundException e) {
			assertEquals(e.getMessage(), "Element not found");
		}
		
		try {
			throw new ElementNotFoundException(msg);
		} catch(ElementNotFoundException e) {
			assertEquals(e.getMessage(), msg);
		}
	}

	@Test
	public void testCannotPassException() {	
		try {
			throw new CannotPassException();
		} catch(CannotPassException e) {
			assertEquals(e.getMessage(), "You shall not pass!");
		}
		
		try {
			throw new CannotPassException(msg);
		} catch(CannotPassException e) {
			assertEquals(e.getMessage(), msg);
		}
	}

}
