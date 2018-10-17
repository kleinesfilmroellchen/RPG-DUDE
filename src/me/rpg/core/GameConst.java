package me.rpg.core;

import java.io.File;
import java.util.ArrayList;
import me.rpg.core.objects.Stats;

/**
 * A class with <strong>all</strong> the important gameplay constants.
 * Capitalizing is standard with such variables. Methods are usually used to
 * return things like ArrayLists and others that can't be expressed in a single
 * constant statement.
 * @author kleinesfilmröllchen
 * @version 0.0.0009
 * @since 0.0.0004
 */
public final class GameConst {
	/**
	 * Determines fleeing probability in fights. Currently at {@value}. More
	 * precisely, it determines the standard raise of the first random value that
	 * "certifies" the second random value.
	 * @see me.rpg.helpers.Functionality#fleeAttempt(double, double) Flee Attempt
	 * Function
	 */
	public static final double FLIGHT_P = 0.6d;

	/**
	 * Determines with what the defence value of an entity is multiplied when that
	 * entity defends on purpose.
	 */
	public static final double DEFENSE_MULTIPLIER = 2d;

	public static final String VERSION = "0.0.0009";

	/**
	 * Contains all player level stats and returns them, if needed.
	 * @return an ArrayList with all the player level stats.
	 * @see me.rpg.core.GameConst#P_BASE_LEVEL Player Level 1
	 */
	public static final ArrayList<Stats> requestPlayerLevels() {
		ArrayList<Stats> playerLevels = new ArrayList<Stats>();

		// Fixed player levels (level 1 not included)
		// THESE ARE CONSTANTS AND CAN BE CHANGED FOR BALANCE
		playerLevels.add(new Stats(100, 2, 2, 2, 25)); // Level 2
		playerLevels.add(new Stats(120, 3, 3, 3, 50)); // Level 3
		playerLevels.add(new Stats(145, 3, 4, 3.5f, 75)); // Level 4
		playerLevels.add(new Stats(165, 4, 5, 4.5f, 90)); // Level 5
		playerLevels.add(new Stats(190, 5, 5, 5f, 110)); // Level 6

		return playerLevels;
	}

	/**
	 * Level 1 Stats for the player.
	 * @see me.rpg.core.GameConst#requestPlayerLevels Other levels (2+)
	 */
	public static final Stats P_BASE_LEVEL = new Stats(20, 1, 1, 1, 25);

	/**
	 * The absolute Path for wherever the program is run. Already adds a "\" so a
	 * file name can directly be appended to it.
	 */
	public static final String absolutePath = new File(".").getAbsolutePath() + "\\";
}
