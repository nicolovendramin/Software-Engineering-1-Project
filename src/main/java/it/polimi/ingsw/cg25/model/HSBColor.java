package it.polimi.ingsw.cg25.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Giovanni
 *
 */
public class HSBColor extends Color {
	
	/**
	 * Serial number for serial communication
	 */
	private static final long serialVersionUID = 2242342507440260071L;
	/**
	 * the "name" of the color, e.g : "Black"
	 */
	private String tag;
	
	/**
	 * The purpouse of this method is to create a new tagged HSB color from an old one with a null tag
	 * @param toBeRetagged the color to tag
	 * @param tag the color name
	 */
	public HSBColor(HSBColor toBeRetagged, String tag)
	{
		this(toHSB(toBeRetagged)[0], toHSB(toBeRetagged)[1], toHSB(toBeRetagged)[2], tag);
	}
	
	/**
	 * HSBColor class constructor
	 * @param hue
	 * @param saturation
	 * @param brightness
	 * @param tag the tag to assign to the color
	 */
	public HSBColor(float hue, float saturation, float brightness, String tag)
	{
		this(hue, saturation, brightness);
		this.tag = tag;
	}
	
	/**
	 * HSBColor class constructor
	 * @param hue HSB H value (interval 0-360)
	 * @param saturation HSB S value (interval 0-100)
	 * @param brightness HSB B value (interval 0-100)
	 */
	public HSBColor(float hue, float saturation, float brightness) {
		super(HSBtoRGB(hue/360, saturation/100, brightness/100));
		
		if(hue < 0 || hue > 360)
			throw new IllegalArgumentException("Check H parameter!");
		if(saturation < 0 || saturation > 100)
			throw new IllegalArgumentException("Check S parameter!");
		if(brightness < 0 || brightness > 100)
			throw new IllegalArgumentException("Check B parameter!");
	}
	
	/**
	 * The method takes a HSBColor object and returns the HSB color's parameters
	 * @param c the HSBColor object
	 * @return an array of floats containing hue, saturation and brightness values
	 */
	public static float[] toHSB(HSBColor c){
		float[] hsbvals = new float[3];
		RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbvals);	
		return hsbvals;
	}
	
	
	/**
	 * @return the tag of the color
	 */
	public String printTag()
	{
		return this.tag;
	}
	
	/**
	 * This method returns a List of n different HSBColor objects with an associated tag.
	 * Only the H parameter is modified among the different kind of colors while
	 * S and B are kept constant at 100.
	 * @param n is the number of different colors we want to get back from the function
	 * @return a list of n different colors
	 */
	public static List<HSBColor> getNDifferent(int n)
	{
		List<HSBColor> temp = new ArrayList<>();
		int step = 0;
		if(n<=0)
			throw new IllegalArgumentException("You should ask at least 1 colour. Negative or zero argument is not accepted!");
		if(n>=1)
			temp.add(new HSBColor(0, 0, 100, "White"));
		if(n>=2)
			temp.add(new HSBColor(0, 0, 0, "Black"));
		if(n>=3)
			step = 350/(n-2);
		int currentHue = 0;
		String currentTag = null;
		String token;
		int tagCounter = 1;
		for(int i=2; i<n; i++)
		{
			if(tagger(currentHue).equals(currentTag))
				tagCounter = tagCounter + 1;
			else {
				tagCounter = 1;
				currentTag = tagger(currentHue);
			}
			if(tagCounter != 1)
				token = new StringBuilder(currentTag).append(Integer.toString(tagCounter)).toString();
			else token = currentTag;
			temp.add(new HSBColor(currentHue, 100, 100, token));
			currentHue = currentHue + step;
		}
		return temp;
	}
	
	/**
	 * It takes a general hue and gives back the tag corresponding to its placement in the spectrum
	 * @param hue the hue value
	 * @return the tag corresponding to the placement of that hue in the spectrum
	 */
	private static String tagger(int hue)
	{
		if(hue >= 300 && hue < 340)
			return "Fucsia";
		else if(hue >= 260 && hue < 300)
			return "Purple";
		else if(hue >= 210 && hue < 260)
			return "Blue";
		else if(hue >= 165 && hue < 210)
			return "Light Blue";
		else if(hue >= 135 && hue < 165)
			return "Water Green";
		else if(hue >= 70 && hue < 135)
			return "Green";
		else if(hue >= 50 && hue < 70)
			return "Yellow";
		else if(hue >= 15 && hue < 50)
			return "Orange";
		else
			return "Red";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
		return result;
	}

	/**
	 * This overrided equals compares the tag of two HSBColor objects
	 * as well as the rgb parameters using equals from the superclass
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		HSBColor other = (HSBColor) obj;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HSBColor [r=" + this.getRed() + ",g=" + this.getGreen() + ",b=" + this.getBlue() + ",tag=" + tag + "]";
	}
	
}
