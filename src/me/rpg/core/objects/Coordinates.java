package me.rpg.core.objects;

/**
 * Class for storing coordinates and calculations with coordinates.<br>
 * Most objects that have some sort of "position" derive from this.
 * 
 * @since 0.0.0001
 * @author DJH
 * @author kleinesfilmröllchen
 * @version 0.0.0008
 */
public class Coordinates {
	
	/**East or West position relative to origin.*/
	protected int x;
	/**North or south position relative to origin.*/
	protected int y;
	/**Floor relative to starting floor (ground floor).*/
	protected int z;
	
	/**
	 * Default constructor; sets all the coordinates.
	 * @param x the x coordinate (east / west)
	 * @param y the y coordinate (north / south)
	 * @param z the z coordinate (floor)
	 */
	public Coordinates(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Coordinates(int x, int y) { this(x, y, 0);}
	public Coordinates() { this(0, 0, 0); }
	
	/**
	 * Sets the coordinates to the parameter's values.
	 */
	public void setCoords(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Adds the parameters to the current coordinates.
	 */
	public void add(int x, int y, int z) {
		this.addX(x);
		this.addY(y);
		this.addZ(z);
	}
	
	/**
	 * Only adds on the x coordinate. Identical to {@code add(x, 0, 0)}.
	 */
	public void addX(int x) {
		this.x += x;
	}
	/**
	 * Only adds on the y coordinate. Identical to {@code add(0, y, 0)}.
	 */
	public void addY(int y) {
		this.y += y;
	}
	/**
	 * Only adds on the z coordinate. Identical to {@code add(0, 0, z)}.
	 */
	public void addZ(int z) {
		this.z += z;
	}
	
	/**
	 * Conversion to conventional array
	 * @return The coordinates of this object with {@code [0] = x, [1] = y, [2] = z}.
	 */
	public int[] getCoords() {
		return new int[] {x, y, z};
	}
	/**
	 * Gets one coordinate by "name".
	 * @param l The "name" of the coordinate to get. Can be "x", "y" or "z". Case-insensitive.
	 * @return The specified coordinate's value.
	 * @throws IllegalArgumentException If the argument character was non of the above specified.
	 */
	public int getCoord(char l) throws IllegalArgumentException {
		l = Character.toLowerCase(l);
		
		if (l == 'x')
			return this.x;
		else if (l == 'y')
			return this.y;
		else if (l == 'z')
			return this.z;
		else
			throw new IllegalArgumentException("Argument character must be 'x', 'y' or 'z' (case-insensitive)");
	}
	
	public int getX() {return x;}
	public int getY() {return y;}
	public int getZ() {return z;}
	
	/**
	 * Checks whether two coordinate objects contain the same location in all dimensions.
	 * @param o1 The first Coordinate object.
	 * @param o2 The second Coordinate object.
	 */
	public static boolean coordEqual(Coordinates o1, Coordinates o2) {
		return (o1 != null && o2 != null) ? (o1.x == o2.x && o1.y == o2.y && o1.z == o2.z) : false; 
	}
	/**
	 * Checks whether this coordinate is directly neighboring another coordinate.
	 * @param other The other coordinate to check.
	 */
	public boolean neighboring(Coordinates other) {
		//Difference in x/y position
		int differenceX = Math.abs(this.x - other.x), differenceY = Math.abs(this.y - other.y), differenceZ = Math.abs(this.z - other.z);
		return  (differenceX == 1 && differenceY == 0 && differenceZ == 0) || 
				(differenceX == 0 && differenceY == 1 && differenceZ == 0) ||
				(differenceX == 0 && differenceY == 0 && differenceZ == 1);
	}
	
	/**
	 * Calculates the x direction in which a neighbor lies. 
	 * @param other The other Coordinates to check.
	 * @return -1 if neighbor further east, 1 if neighbor further west and 0 if neighbor is equal or no neighbor at all.
	 */
	public int neighborDirX(Coordinates other) {
		if (!this.neighboring(other)) return 0;
		//x greater == neighbor to the left (-1), equality not possible 
		return this.x > other.x ? -1 : 1;
	}
	
	/**
	 * Calculates the y direction in which a neighbor lies. 
	 * @param other The other Coordinates to check.
	 * @return -1 if neighbor is further south, 1 if neighbor further north and 0 if neighbor is equal or no neighbor at all.
	 */
	public int neighborDirY(Coordinates other) {
		if (!this.neighboring(other)) return 0;
		//y greater == neighbor south (-1), equality not possible 
		return this.y > other.y ? -1 : 1;
	}
	
	/**
	 * Calculates the z direction in which a neighbor lies. 
	 * @param other The other Coordinates to check.
	 * @return -1 if neighbor is lower, 1 if neighbor is higher and 0 if neighbor is equal or no neighbor at all.
	 */
	public int neighborDirZ(Coordinates other) {
		if (!this.neighboring(other)) return 0;
		//z greater == neighbor lower (-1), equality not possible 
		return this.z > other.z ? -1 : 1;
	}
}
