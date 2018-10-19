package rpg.helpers;

import java.io.*;
import java.util.*;
import rpg.core.*;
import utils.BetterRandom;

/**
 * A class containing functions for later use. Most of those are moved, some may stay :-)
 * @author kleinesfilmröllchen
 * @version 0.0.0008
 * @since 0.0.0004
 */
public class Functionality {
	
	/**
	 * A very good pseudo random number generator for this class and the whole package to use.
	 * @see utils.BetterRandom
	 */
	public static BetterRandom generator;
	
	/**
	 * A flag for the main function to listen to, signalizes when the generator is ready.
	 * @see rpg.helpers.Functionality#generator Random number generator
	 * @see rpg.helpers.Functionality#initGenerator
	 */
	public static boolean generatorReady = false;
	
	/**
	 * Initializes the random generator and then sets a flag for the main function to listen to.
	 * @see rpg.helpers.Functionality#generator Random number generator
	 * @see rpg.helpers.Functionality#generatorReady Ready flag set by this function
	 */
	public static void initGenerator() {
		generator = new BetterRandom();
		generatorReady = true;
	}
	
	/**
	 * Simulates a flight attempt.<br>
	 * The attempt is calculated as {@code 2^r * me/mm < r1 * FLIGHT_P}, where r and r1 are random values,
	 * mm is the player's mobility and me is the enemy's mobility.
	 * 
	 * @param mobMe The player's mobility value
	 * @param mobEnemy The enemy's mobility value
	 * @return Whether the attempt was successful (true) or not (false)
	 * @see rpg.core.GameConst#FLIGHT_P
	 */
	public static boolean fleeAttempt(double mobMe, double mobEnemy) {
		double a2 = generator.nextDouble();
		double a1 = generator.nextDouble() * (mobEnemy / mobMe);
		return a1 <= a2 * GameConst.FLIGHT_P;
	}
	
	
	
	/**
	 * Extracts all of the pure text out of a file.
	 * @param absolutePath The absolute path to the file.
	 * @return The contents of the file.
	 * @throws FileNotFoundException If the file doesn't exist or is not accessible.
	 * @throws IOException If something with I/O goes wrong.
	 */
	public static String readFileContents(String absolutePath) throws FileNotFoundException, IOException {
	    File file = new File(absolutePath);
	    StringBuilder fileContents = new StringBuilder((int)file.length());
	    Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));
	    String lineSeparator = System.getProperty("line.separator");
	    
	    while(scanner.hasNextLine()) {
	        fileContents.append(scanner.nextLine() + lineSeparator);
	    }
	        
	    scanner.close();
	    return fileContents.toString();
	}
}
