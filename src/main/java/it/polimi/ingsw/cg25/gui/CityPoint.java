package it.polimi.ingsw.cg25.gui;

/**
 * 
 * @author Giovanni
 *
 */
public class CityPoint {

	/**
	 * The horizontal position
	 */
	private int x;
	/**
	 * The vertical position
	 */
	private int y;
	
	/**
	 * CityPoint class constructor
	 * @param x the x coordinate of the point
	 * @param y the y coordinate of the point
	 */
	public CityPoint(int x, int y) {
		if(x < 0 || y < 0)
			throw new IllegalArgumentException("Illegal point coordinates");
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x integer value
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y integer value
	 */
	public int getY() {
		return y;
	}
	
}
