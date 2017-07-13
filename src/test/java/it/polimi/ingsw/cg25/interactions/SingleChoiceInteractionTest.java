package it.polimi.ingsw.cg25.interactions;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.Interaction;
import it.polimi.ingsw.cg25.actions.MultipleChoiceInteraction;
import it.polimi.ingsw.cg25.actions.SingleChoiceInteraction;
import it.polimi.ingsw.cg25.model.dashboard.bonus.BonusConcreteCreator;
import it.polimi.ingsw.cg25.model.dashboard.topological.City;
import it.polimi.ingsw.cg25.model.dto.DTOCity;
import it.polimi.ingsw.cg25.parsing.CitiesParser;

public class SingleChoiceInteractionTest {

	private List<City> cities;
	
	@Before
	public void setUp() throws FileNotFoundException, IOException {
		CitiesParser parser = new CitiesParser(new BonusConcreteCreator());
		//I'm gonna use the reduced city file and create SingleChoiceInteraction with DTOCity
		parser.parseLineByLine(new InputStreamReader(new FileInputStream("src/test/resources/cities.txt")));
		cities = parser.getCities();
	}
	
	@Test
	public void testSingleChoiceInteractionQuestionAndOptions() {
		SingleChoiceInteraction<DTOCity> sci = 
				new SingleChoiceInteraction<>("Choose a city:", DTOCity.convertAll(cities));
		assertEquals("Choose a city:", sci.getQuestion());
		//Check option lines
		String[] lines = sci.printOptions().split("\r\n|\r|\n");
		//1 question + 2 options
		assertEquals(3, lines.length);
		assertTrue(lines[1].contains("Milano"));
		assertTrue(lines[2].contains("Torino"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCantCreateASingleChoiceInteractionWithNullOptions() {
		new SingleChoiceInteraction<>("Choose a city:", null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCantCreateASingleChoiceInteractionWithEmptyOptionsList() {
		new SingleChoiceInteraction<>("Choose a city:", new ArrayList<DTOCity>());
	}
	
	@Test
	public void testRegisterReply() {
		SingleChoiceInteraction<DTOCity> sci = 
				new SingleChoiceInteraction<>("Choose a city:", DTOCity.convertAll(cities));
		//Select Milano
		sci.registerReply("0");
		//Check if it is Milano
		assertTrue(sci.getReply().equals(DTOCity.convertAll(cities).get(0)));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongReply() {
		SingleChoiceInteraction<DTOCity> sci = 
				new SingleChoiceInteraction<>("Choose a city:", DTOCity.convertAll(cities));
		//IndexOutOfBound
		sci.registerReply("2");
	}
	
	@Test(expected=NullPointerException.class)
	public void testReplyNotRegisteredYet() {
		SingleChoiceInteraction<DTOCity> sci = 
				new SingleChoiceInteraction<>("Choose a city:", DTOCity.convertAll(cities));
		sci.getReply();
	}
	
	@Test
	public void testRegisterReplyWithInteraction() {
		SingleChoiceInteraction<DTOCity> sci = 
				new SingleChoiceInteraction<>("Choose a city:", DTOCity.convertAll(cities));
		//Register Milano
		sci.registerReply("0");
		SingleChoiceInteraction<DTOCity> in = new SingleChoiceInteraction<>(DTOCity.convertAll(cities));
		//Register Torino
		in.registerReply("1");
		sci.registerReply(in);
		//Check reply for sci now is Torino
		assertTrue(sci.getReply().equals(in.getReply()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRegisterReplyWithNullInteraction() {
		SingleChoiceInteraction<DTOCity> sci = 
				new SingleChoiceInteraction<>("Choose a city:", DTOCity.convertAll(cities));
		Interaction in = null;
		sci.registerReply(in);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRegisterReplyWithDifferentTypeOfInteraction() {
		SingleChoiceInteraction<DTOCity> sci = 
				new SingleChoiceInteraction<>("Choose a city:", DTOCity.convertAll(cities));
		Interaction in = new MultipleChoiceInteraction<>(DTOCity.convertAll(cities));
		sci.registerReply(in);
	}
	
	@Test
	public void testEqualsAndHashCode() {
		SingleChoiceInteraction<DTOCity> sci = 
				new SingleChoiceInteraction<>("Choose a city:", DTOCity.convertAll(cities));
		assertTrue(sci.hashCode() == sci.hashCode());
		//Null
		assertFalse(sci.equals(null));
		//Different types
		Interaction in = new MultipleChoiceInteraction<>(DTOCity.convertAll(cities));
		assertFalse(sci.equals(in));
		//Same types, same choices
		SingleChoiceInteraction<DTOCity> sci2 = 
				new SingleChoiceInteraction<>("Choose a city:", DTOCity.convertAll(cities));
		assertTrue(sci.equals(sci2));
		assertFalse(sci.hashCode() == sci2.hashCode());
		//Same types, different choices
		cities.remove(1);
		SingleChoiceInteraction<DTOCity> sci3 = 
				new SingleChoiceInteraction<>("Choose a city:", DTOCity.convertAll(cities));
		assertFalse(sci.equals(sci3));
	}
	
	@Test
	public void testSingleChoiceWithPredicate() {
		SingleChoiceInteraction<DTOCity> sci = 
				new SingleChoiceInteraction<>("Choose a city:", DTOCity.convertAll(cities));
		//Try to filter Milano (delete)
		SingleChoiceInteraction<DTOCity> sci2 =
				new SingleChoiceInteraction<>(sci, c -> c.getName().equals("Torino"));
		//Now choice 0 must be Torino in sci2
		sci2.registerReply("0");
		assertTrue(sci2.getReply().getName().equals("Torino"));
		//Try with empty choices
		sci2 = new SingleChoiceInteraction<>(sci, c -> c.getClass() != DTOCity.class);
		sci2.registerReply("0");
		//First choice is still Milano
		assertTrue(sci2.getReply().getName().equals("Milano"));
	}
	
}
