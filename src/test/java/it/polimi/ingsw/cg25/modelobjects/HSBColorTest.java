package it.polimi.ingsw.cg25.modelobjects;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import it.polimi.ingsw.cg25.model.Coin;
import it.polimi.ingsw.cg25.model.HSBColor;

/**
 * 
 * @author Giovanni
 *
 */
@SuppressWarnings("unused")
public class HSBColorTest {

	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateAColorWithAGT360H() {
		HSBColor color = new HSBColor(2000, 100, 100, "color");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateAColorWithALT0H() {
		HSBColor color = new HSBColor(-4, 100, 100, "color");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateAColorWithAGT100S() {
		HSBColor color = new HSBColor(300, 120, 100, "color");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateAColorWithALT0S() {
		HSBColor color = new HSBColor(300, -6, 100, "color");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateAColorWithAGT100B() {
		HSBColor color = new HSBColor(200, 100, 900, "color");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotBePossibleToCreateAColorWithALT0B() {
		HSBColor color = new HSBColor(200, 50, -10, "color");
	}
	
	@Test
	public void testPrintTag() {
		HSBColor color = new HSBColor(200, 50, 100, "color");
		assertTrue("color".equals(color.printTag()));
	}
	
	@Test
	public void testColorRetagging() {
		HSBColor color = new HSBColor(200, 50, 100);
		assertTrue(color.printTag() == null);
		//Create a tagged color
		HSBColor color2 = new HSBColor(color, "retagged");
		assertTrue("retagged".equals(color2.printTag()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void ShouldNotPossibleToCreateLessThanOneColor() {
		HSBColor.getNDifferent(0);
	}
	
	@Test
	public void testGetNDifferent() {
		List<HSBColor> colors = HSBColor.getNDifferent(11);
		assertEquals(11, colors.size());
		assertEquals("White", colors.get(0).printTag());
		assertEquals("Black", colors.get(1).printTag());
		assertEquals("Red", colors.get(2).printTag());
		assertEquals("Orange", colors.get(3).printTag());
		assertEquals("Green", colors.get(4).printTag());
		assertEquals("Green2", colors.get(5).printTag());
		assertEquals("Water Green", colors.get(6).printTag());
		assertEquals("Light Blue", colors.get(7).printTag());
		assertEquals("Blue", colors.get(8).printTag());
		assertEquals("Purple", colors.get(9).printTag());
		assertEquals("Fucsia", colors.get(10).printTag());

	}
	
	@Test
	public void testEquals() {
		HSBColor color = new HSBColor(185, 90, 20, "color");
		HSBColor color2 = new HSBColor(200, 50, 100, "color2");
		HSBColor color3 = new HSBColor(100, 80, 99);
		HSBColor color4 = new HSBColor(185, 90, 20, "color");
		
		assertTrue(color.equals(color));
		assertFalse(color.equals(color2));
		//null
		assertFalse(color.equals(null));
		//Different types
		assertFalse(color.equals(new Coin(10)));
		//One tag null
		assertFalse(color3.equals(color));
		//Different tags
		assertFalse(color.equals(color2));
		assertFalse(color2.equals(color3));
		//Same color and tag
		assertTrue(color.equals(color4));
	}
	
	@Test
	public void testHashCode() {
		HSBColor color = new HSBColor(300, 50, 100, "color");
		HSBColor color2 = new HSBColor(200, 70, 100, "color2");
		
		assertTrue(color.hashCode() == color.hashCode());
		assertFalse(color.hashCode() == color2.hashCode());
	}
	
	@Test
	public void testToString() {
		HSBColor color = new HSBColor(200, 80, 100);
		assertEquals("HSBColor [r=51,g=187,b=255,tag=null]", color.toString());
	}
	
}
