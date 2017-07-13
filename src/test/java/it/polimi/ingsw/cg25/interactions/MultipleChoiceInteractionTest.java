package it.polimi.ingsw.cg25.interactions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

public class MultipleChoiceInteractionTest {

	private List<City> cities;
	
	@Before
	public void setUp() throws FileNotFoundException, IOException {
		CitiesParser parser = new CitiesParser(new BonusConcreteCreator());
		//I'm gonna use the reduced city file and create MultipleChoiceInteraction with DTOCity
		parser.parseLineByLine(new InputStreamReader(new FileInputStream("src/test/resources/cities.txt")));
		cities = parser.getCities();
	}
	
	@Test
	public void testMultipleChoiceInteractionQuestionAndOptions() {
		MultipleChoiceInteraction<DTOCity> mci = 
				new MultipleChoiceInteraction<>("Choose one or more cities:", DTOCity.convertAll(cities));
		assertEquals("Choose one or more cities:", mci.getQuestion());
		//Check option lines
		String[] lines = mci.printOptions().split("\r\n|\r|\n");
		//1 question + 2 options
		assertEquals(3, lines.length);
		assertTrue(lines[1].contains("Milano"));
		assertTrue(lines[2].contains("Torino"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCantCreateAMultipleChoiceInteractionWithNullOptions() {
		new MultipleChoiceInteraction<>("Choose one or more cities:", null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCantCreateAMultipleChoiceInteractionWithEmptyOptionsList() {
		new MultipleChoiceInteraction<>("Choose one or more cities:", new ArrayList<DTOCity>());
	}
	
	@Test
	public void testRegisterReply() {
		MultipleChoiceInteraction<DTOCity> mci = 
				new MultipleChoiceInteraction<>("Choose a city:", DTOCity.convertAll(cities));
		//Select Milano and Torino
		mci.registerReply("0,1");
		//Check if reply is Milano and Torino
		assertTrue(mci.getReply().equals(DTOCity.convertAll(cities)));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongReply() {
		MultipleChoiceInteraction<DTOCity> mci = 
				new MultipleChoiceInteraction<>("Choose one or more cities:", DTOCity.convertAll(cities));
		//IndexOutOfBound
		mci.registerReply("4");
	}
	
	@Test(expected=NullPointerException.class)
	public void testReplyNotRegisteredYet() {
		MultipleChoiceInteraction<DTOCity> mci = 
				new MultipleChoiceInteraction<>("Choose one or more cities:", DTOCity.convertAll(cities));
		mci.getReply();
	}
	
	@Test
	public void testRegisterReplyWithInteraction() {
		MultipleChoiceInteraction<DTOCity> mci = 
				new MultipleChoiceInteraction<>("Choose one or more cities:", DTOCity.convertAll(cities));
		//Register Milano
		mci.registerReply("0");
		MultipleChoiceInteraction<DTOCity> in = new MultipleChoiceInteraction<>(DTOCity.convertAll(cities));
		//Register Torino
		in.registerReply("1");
		mci.registerReply(in);
		//Check reply for sci now is Torino
		assertTrue(mci.getReply().equals(in.getReply()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRegisterReplyWithNullInteraction() {
		MultipleChoiceInteraction<DTOCity> mci = 
				new MultipleChoiceInteraction<>("Choose one or more cities:", DTOCity.convertAll(cities));
		Interaction in = null;
		mci.registerReply(in);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testRegisterReplyWithDifferentTypeOfInteraction() {
		MultipleChoiceInteraction<DTOCity> mci = 
				new MultipleChoiceInteraction<>("Choose a city:", DTOCity.convertAll(cities));
		Interaction in = new SingleChoiceInteraction<>(DTOCity.convertAll(cities));
		mci.registerReply(in);
	}
	
	@Test
	public void testEqualsAndHashCode() {
		MultipleChoiceInteraction<DTOCity> mci = 
				new MultipleChoiceInteraction<>("Choose one or more cities:", DTOCity.convertAll(cities));
		assertTrue(mci.hashCode() == mci.hashCode());
		//Null
		assertFalse(mci.equals(null));
		//Different types
		Interaction in = new SingleChoiceInteraction<>(DTOCity.convertAll(cities));
		assertFalse(mci.equals(in));
		//Same types, same choices
		MultipleChoiceInteraction<DTOCity> mci2 = 
				new MultipleChoiceInteraction<>("Choose one or more cities:", DTOCity.convertAll(cities));
		assertTrue(mci.equals(mci2));
		assertFalse(mci.hashCode() == mci2.hashCode());
		//Same types, different choices
		cities.remove(0);
		MultipleChoiceInteraction<DTOCity> mci3 = 
				new MultipleChoiceInteraction<>("Choose one or more cities:", DTOCity.convertAll(cities));
		assertFalse(mci.equals(mci3));
	}
	
	@Test
	public void testMultipleChoiceWithPredicate() {
		MultipleChoiceInteraction<DTOCity> mci = 
				new MultipleChoiceInteraction<>("Choose one or more cities:", DTOCity.convertAll(cities));
		//Try to filter Torino (delete)
		MultipleChoiceInteraction<DTOCity> mci2 =
				new MultipleChoiceInteraction<>(mci, c -> c.getName().equals("Milano"));
		//Now choice 0 must be Milano in sci2
		mci2.registerReply("0");
		assertTrue(mci2.getReply().get(0).getName().equals("Milano"));
		//Try with empty choices
		mci2 = new MultipleChoiceInteraction<>(mci, c -> c.getClass() != DTOCity.class);
		mci2.registerReply("0,1");
		//Second choice is still Torino
		assertTrue(mci2.getReply().get(1).getName().equals("Torino"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testTooMuchItems() {
		MultipleChoiceInteraction<DTOCity> mci = 
				new MultipleChoiceInteraction<>("Choose one or more cities:", DTOCity.convertAll(cities), 1, 1);
		mci.registerReply("0,1");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMoreItemsNeeded() {
		MultipleChoiceInteraction<DTOCity> mci = 
				new MultipleChoiceInteraction<>("Choose one or more cities:", DTOCity.convertAll(cities), 2, 2);
		mci.registerReply("0");
	}
	
	@Test
	public void testMultipleChoiceInteractionWithBoundaries() {
		MultipleChoiceInteraction<DTOCity> mci = 
				new MultipleChoiceInteraction<>("Choose one or more cities:", DTOCity.convertAll(cities), 1, 2);
		//Question must have one more line
		String[] lines = mci.getQuestion().split("\r\n|\r|\n");
		assertEquals(2, lines.length);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMultipleChoiceInteractionWithWrongOptionsAndBoundaries() {
		cities.remove(1);
		new MultipleChoiceInteraction<>("Choose one or more cities:", DTOCity.convertAll(cities), 2, 2);
	}
	
}
