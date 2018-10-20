package rpg.core;

import java.util.*;
import java.util.stream.Stream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import json.JSONException;
import rpg.core.objects.*;
import rpg.helpers.*;
import utils.FileBuffer;

// Symbols for copying: <>|

/**
 * The all-mighty master class doing everything. Might get a little cramped :-)
 * @author DJH
 * @author kleinesfilmröllchen
 * @version 0.0.0009
 * @since 0.0.0001
 */
public class RPG_MAIN {

	// Variables
	/** Scanner for easy text input access. */
	public static Scanner	input		= new Scanner(System.in);
	/** Print stream for logging */
	static PrintStream		logger;
	/** Controls whether debug mode is active. */
	private static boolean	debug		= false;
	/** Controls whether logging to a file is enabled. */
	private static boolean	logging	= false;

	/** Enemies in this map */
	private static ArrayList<Enemy>	enemies;
	/** Enemy prefabs. Are later used to create real enemies. */
	private static ArrayList<Enemy>	enemyPrefabs;
	/** The player entity. */
	private static Player				p;
	/**
	 * The room array. Is indexed by
	 * {@code [z or floor level][x coordinate][y coordinate]}.
	 */
	private static Room[][][]			rooms;

	/** Signals that the asyncronous setup is ready. */
	public static volatile boolean asyncReady = false;

	/**
	 * Main function.
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] _args) throws IOException {
		//// Test code belongs here

		//// Async setup code: time consuming stuff belongs into this method.
		// That stuff is executed on a separate thread without blocking the main program
		//// thread.
		Thread setupThread = new Thread(() -> {
			// long time = System.currentTimeMillis();
			Functionality.initGenerator();
			try {
				File log = FileBuffer.getFile("log.txt");
				logger = new PrintStream(log);

				rooms = Loaders.loadRoomsFromMap("map1.json");

				loadEnemyPrefabs();
				loadEnemiesFromMap();
				Loaders.loadItemsInMap("map1.json", rooms);

			} catch (IOException e) {
				System.err.println(TextMessages._t("RPG_MAIN.1")); //$NON-NLS-1$
				System.exit(1);
			} catch (SecurityException e) {
				System.err.println(TextMessages._t("RPG_MAIN.2")); //$NON-NLS-1$
				System.exit(1);
			}
			logger.flush();
			RPG_MAIN.asyncReady = true;
			// System.out.println("Setup thread finished after " +
			// (System.currentTimeMillis() - time) + " milliseconds.");
		});
		setupThread.setName("Setup Async");
		setupThread.start();

		//// Sync setup code
		try {
			// Argument checking
			Stream<String> args = Arrays.stream(_args);
			if (args.anyMatch(s -> s.equalsIgnoreCase("manual"))) {
				manual.ManualMain.manualInit();
				System.exit(0);
			}

			// Tests arguments for at least one called "debug" and if so, sets debug
			// automatically
			args = Arrays.stream(_args);
			debug = args.anyMatch(s -> s.equalsIgnoreCase("debug"));
			// Tests arguments for log or logging and activates input/output logging
			// automatically
			args = Arrays.stream(_args);
			logging = args.anyMatch(
					s -> s.equalsIgnoreCase("log") || s.equalsIgnoreCase("logging") || s.equalsIgnoreCase("logger"));

			// Wait until async setup is ready
			if (logging) while (!asyncReady);;
			input.useDelimiter("\\p{javaWhitespace}+"); //$NON-NLS-1$

			setupPlayer();

		} catch (FileNotFoundException e) {
			println(TextMessages._t("RPG_MAIN.10") + e.getMessage()); //$NON-NLS-1$
			println(Arrays.toString(e.getStackTrace()));
			System.exit(1);
		} catch (JSONException e) {
			println(e.getMessage());
			println(TextMessages._t("RPG_MAIN.11")); //$NON-NLS-1$
			println(Arrays.toString(e.getStackTrace()));
			System.exit(1);
		} catch (NumberFormatException e) {
			println(TextMessages._t("RPG_MAIN.12")); //$NON-NLS-1$
			println(Arrays.toString(e.getStackTrace()));
			System.exit(1);
		} catch (Exception e) {
			println(TextMessages._t("RPG_MAIN.13")); //$NON-NLS-1$
			println(Arrays.toString(e.getStackTrace()));
			System.exit(1);
		}

		while (!asyncReady);

		//////// Main game loop
		do {
			println(TextMessages._t("RPG_MAIN.actionRequest1") + p.name() + TextMessages._t("RPG_MAIN.actionRequest2")); //$NON-NLS-1$ //$NON-NLS-2$
			char opt = getOpt();

			if (opt == 'e') {
				println(TextMessages._t("RPG_MAIN.exit") + p.name() + TextMessages._t("RPG_MAIN.excl")); //$NON-NLS-1$ //$NON-NLS-2$
				logger.close();
				break;
			}
			switch (opt) {

			case 'p':
				println("Items aufnehmen ist bald verfügbar!");
				break;

			case 'h':
				println(Help.getHelp());
				if (debug) println(Help.getDebugHelp());
				break;

			case 'm':
				askForMovement();
				break;

			case 's':
				println(p.statString());
				break;

			case 'l':
				if (!logging) {
					println(TextMessages._t("RPG_MAIN.18")); //$NON-NLS-1$
				} else {
					println(TextMessages._t("RPG_MAIN.19")); //$NON-NLS-1$
					logger.flush();
				}
				logging = !logging;
				break;

			case 'u':
				// player room
				Room pr = rooms[p.getZ()][p.getX()][p.getY()];
				// print a lot of info
				println(((Coordinates) pr).toString());
				println(pr.roomName + TextMessages._t("RPG_MAIN.24") + pr.roomDescription);
				println((pr.walkIsAllowed(0, 1) ? "Du kannst nach Norden gehen.\n" : "") +
						(pr.walkIsAllowed(0, -1) ? "Du kannst nach Süden gehen.\n" : "") +
						(pr.walkIsAllowed(1, 0) ? "Du kannst nach Osten gehen.\n" : "") +
						(pr.walkIsAllowed(-1, 0) ? "Du kannst nach Westen gehen.\n" : ""));

				boolean seenSth = false;
				for (Entity e : enemies) {
					if (Coordinates.coordEqual(e, p)) {
						println(TextMessages._t("RPG_MAIN.25") + e.name() + TextMessages._t("RPG_MAIN.excl")); //$NON-NLS-1$ //$NON-NLS-2$
						seenSth = true;
					}
				}
				if (!seenSth) println(TextMessages._t("RPG_MAIN.27")); //$NON-NLS-1$
				break;

			case 'a':
				ArrayList<Enemy> inRoom = new ArrayList<Enemy>(enemies);
				inRoom.removeIf(e -> !Coordinates.coordEqual(e, p));

				if (inRoom.size() > 0) {
					for (int i = 0; i < inRoom.size(); i++) {
						Enemy e = inRoom.get(i);
						println(TextMessages._t("RPG_MAIN.28") + e.name() + TextMessages._t("RPG_MAIN.29")); //$NON-NLS-1$ //$NON-NLS-2$
						char selection = getOpt();

						if (selection == 'y' || selection == 'j') {

							// execute battle
							battle(e, p);

							if (e.isDead()) {
								enemies.remove(i);
							}
							if (p.isDead()) {
								println(TextMessages._t("RPG_MAIN.30") + p.name() + TextMessages._t("RPG_MAIN.31")); //$NON-NLS-1$ //$NON-NLS-2$
								p.resetToBaseStats();
							}
							break;
						}
					}
				} else
					println(TextMessages._t("RPG_MAIN.32")); //$NON-NLS-1$
				break;

			case 'i':
				println(Help.gameInfo());
				break;

			case 'd':
				if (debug) {
					debug = false;
					println(TextMessages._t("RPG_MAIN.33")); //$NON-NLS-1$
				} else {
					debug = true;
					println(TextMessages._t("RPG_MAIN.34")); //$NON-NLS-1$
				}
				break;

			case 'x':
				if (debug) {
					println(TextMessages._t("RPG_MAIN.35")); //$NON-NLS-1$
					p.addExp(input.nextInt());
				} else {
					println(TextMessages._t("RPG_MAIN.36")); //$NON-NLS-1$
				}
				break;

			case 'o':
				if (debug) {

					println(TextMessages._t("RPG_MAIN.37")); //$NON-NLS-1$
					println(TextMessages._t("RPG_MAIN.38")); //$NON-NLS-1$
					int extraLevels = getInt();
					for (int i = 0; i < extraLevels; i++) {
						p.addExp(p.necessaryXP());
						if (p.maxedOut) break;
					}
				} else {
					println(TextMessages._t("RPG_MAIN.39")); //$NON-NLS-1$
				}
				break;

			case 'z':
				if (debug) {
					println(TextMessages._t("RPG_MAIN.40")); //$NON-NLS-1$
					int h = getInt();
					p.addHealth(h);
				} else {
					println(TextMessages._t("RPG_MAIN.41")); //$NON-NLS-1$
				}
				break;

			default:
				println(TextMessages._t("RPG_MAIN.42")); //$NON-NLS-1$
				break;
			}

		} while (true);
	}

	/**
	 * Loads all enemies from the enemies.json file as prefabs and puts them into a
	 * lookup table. Those are later used to create the real enemies.
	 */
	private static void loadEnemyPrefabs() throws JSONException, IOException {
		ArrayList<Enemy> dict = Loaders.getEnemies("enemies.json"); //$NON-NLS-1$
		enemyPrefabs = dict;
	}

	/**
	 * Loads the actual enemies from the map file and sets their rooms. The private
	 * field {@code enemies} is set to the loaded enemies.
	 * @throws JSONException if something has incorrect format or values.
	 * @throws IOException if something with I/O goes wrong.
	 */
	private static void loadEnemiesFromMap() throws JSONException, IOException {
		RPG_MAIN.enemies = Loaders.loadEnemiesOnMap("map1.json", enemyPrefabs); //$NON-NLS-1$
	}

	/**
	 * Sets up the player, including levels etc.
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws JSONException
	 */
	private static void setupPlayer() throws JSONException, FileNotFoundException, IOException {
		print(TextMessages._t("RPG_MAIN.45")); //$NON-NLS-1$
		p = new Player(input.nextLine(), GameConst.P_BASE_LEVEL, "map1.json"); //$NON-NLS-1$
		p.futureStats = GameConst.requestPlayerLevels();

		println(TextMessages._t("RPG_MAIN.47") + p.name() + TextMessages._t("RPG_MAIN.48") + GameConst.VERSION //$NON-NLS-1$ //$NON-NLS-2$
				+ TextMessages._t("RPG_MAIN.49")); //$NON-NLS-1$
	}

	/**
	 * Takes care of the battle mechanisms and is responsible for the whole battle.
	 * @param e The entity attacked by the player.
	 * @param p The player attacking the entity.
	 * @param callerOut The output stream to be used for status information.
	 * @throws IOException
	 */
	public static void battle(Enemy e, Player p) throws IOException {
		println(TextMessages._t("RPG_MAIN.nl") + p.name() + TextMessages._t("RPG_MAIN.51") + e.name() //$NON-NLS-1$ //$NON-NLS-2$
				+ TextMessages._t("RPG_MAIN.52")); //$NON-NLS-1$

		boolean flightSuccessful = false;
		while (true) {
			boolean playerAttack = true;

			p.usesDefence = e.usesDefence = false;

			if (flightSuccessful) break;

			// Player fight decisions
			println(TextMessages._t("RPG_MAIN.nl") + p.name() + TextMessages._t("RPG_MAIN.54")); //$NON-NLS-1$ //$NON-NLS-2$
			char playerAction = getOpt();

			switch (playerAction) {
			case 'a':
				// Auto-execute of attack
				playerAttack = true;
				break;
			case 'h':
				// in case of help ask player once more for action
				println(Help.getBattleHelp());
				continue;

			case 'd':
				// defence: no player attack
				p.usesDefence = true;
				playerAttack = false;
				break;

			case 's':
				// Stats: ask player once more for action
				println(p.statString() + TextMessages._t("RPG_MAIN.nl")); //$NON-NLS-1$
				println(e.statString());
				continue;

			// All not implemented or invalid actions
			case 'i':
				println(TextMessages._t("RPG_MAIN.56")); //$NON-NLS-1$
				continue;

			case 'm':
				println(TextMessages._t("RPG_MAIN.57")); //$NON-NLS-1$
				continue;

			case 'f':
				// Flight attempt
				println(p.name() + TextMessages._t("RPG_MAIN.58")); //$NON-NLS-1$
				flightSuccessful = Functionality.fleeAttempt(p.mob, e.mob);
				if (flightSuccessful) {
					println(TextMessages._t("RPG_MAIN.59")); //$NON-NLS-1$
					// skipping enemy attack and essentially ending fight
					continue;
				}
				// unsuccessful flight: player doesn't get to attack
				else
					println(TextMessages._t("RPG_MAIN.60")); //$NON-NLS-1$
				playerAttack = false;
				break;

			default:
				println(TextMessages._t("RPG_MAIN.61")); //$NON-NLS-1$
				continue;
			}

			if (!p.usesDefence && playerAttack) p.attack(e);
			if (debug) println(e.def + TextMessages._t("RPG_MAIN.62") + p.atk + TextMessages._t("RPG_MAIN.63") + e.health); //$NON-NLS-1$ //$NON-NLS-2$
			if (!e.usesDefence) e.attack(p);
			if (debug) println(p.def + TextMessages._t("RPG_MAIN.64") + e.atk + TextMessages._t("RPG_MAIN.65") + p.health); //$NON-NLS-1$ //$NON-NLS-2$

			// Exit conditions
			if (p.isDead()) {
				println(TextMessages._t("RPG_MAIN.66") + p.name() + TextMessages._t("RPG_MAIN.67")); //$NON-NLS-1$ //$NON-NLS-2$
				p.resetToBaseStats();
				break;
			}
			if (e.isDead()) {
				println(e.name() + TextMessages._t("RPG_MAIN.68") + p.name() + TextMessages._t("RPG_MAIN.69") //$NON-NLS-1$ //$NON-NLS-2$
						+ p.name() + TextMessages._t("RPG_MAIN.70") + e.reward + TextMessages._t("RPG_MAIN.71")); //$NON-NLS-1$ //$NON-NLS-2$
				p.addExp(e.reward);
				break;
			}
		}

		println(TextMessages._t("RPG_MAIN.72")); //$NON-NLS-1$
	}

	/**
	 * Asks the user for movement input. The player will be moved in the direction
	 * the user specified.
	 * @throws IOException
	 */
	public static void askForMovement() throws IOException {
		print(TextMessages._t("RPG_MAIN.dirRequest")); //$NON-NLS-1$
		byte x = 0, y = 0;
		char dir = getOpt();
		switch (dir) {
		case 'n':
			y = -1;
			break;
		case 's':
			y = 1;
			break;
		case 'w':
			x = -1;
			break;
		case 'o':
			x = 1;
			break;
		default:
			print(TextMessages._t("RPG_MAIN.noSuchDir")); //$NON-NLS-1$
			return;
		}

		// Checks whether there is an exit in the given direction
		if (rooms[p.getZ()][p.getX()][p.getY()].walkIsAllowed(x, y)) {
			p.add(x, y, 0);
			println(TextMessages._t("RPG_MAIN.movesto1") + p.getX() + TextMessages._t("RPG_MAIN.|") + p.getY()
					+ TextMessages._t("RPG_MAIN.|") + p.getZ() + TextMessages._t("RPG_MAIN.movesto2")
					+ rooms[p.getZ()][p.getX()][p.getY()].roomName);
		} else {
			println(TextMessages._t("RPG_MAIN.75") //$NON-NLS-1$
					+ (dir == 'n' ? TextMessages._t("RPG_MAIN.76") //$NON-NLS-1$
							: dir == 's' ? TextMessages._t("RPG_MAIN.77") //$NON-NLS-1$
									: dir == 'o' ? TextMessages._t("RPG_MAIN.78") : TextMessages._t("RPG_MAIN.79")) //$NON-NLS-1$ //$NON-NLS-2$
					+ TextMessages._t("RPG_MAIN.excl")); //$NON-NLS-1$
		}
	}

	/**
	 * Helper to get a character as input from the user.
	 * @return The input character.
	 * @throws IOException
	 */
	public static char getOpt() throws IOException {
		char opt = '9';
		do {
			print(debug ? TextMessages._t("RPG_MAIN.81") : TextMessages._t("RPG_MAIN.sel")); //$NON-NLS-1$ //$NON-NLS-2$
			try {
				opt = input.next().toLowerCase().charAt(0);
			} catch (InputMismatchException e) {
				println(TextMessages._t("RPG_MAIN.83")); //$NON-NLS-1$
			} catch (StringIndexOutOfBoundsException e) {
				println(TextMessages._t("RPG_MAIN.84")); //$NON-NLS-1$
			}
		} while (!Character.isLetter(opt));

		logger.println(opt);
		input.skip(".*"); //$NON-NLS-1$

		return opt;
	}

	/**
	 * Helper to get an integer as input from the user.
	 * @return The input integer
	 * @throws IOException
	 */
	public static int getInt() throws IOException {
		print(debug ? TextMessages._t("RPG_MAIN.dint") : TextMessages._t("RPG_MAIN.val")); //$NON-NLS-1$ //$NON-NLS-2$
		int i = Integer.MAX_VALUE;
		String in;
		do {
			in = input.next();
			try {
				i = Integer.parseInt(in);
				break;
			} catch (NumberFormatException e) {
				print(TextMessages._t("RPG_MAIN.88")); //$NON-NLS-1$
			}
		} while (i == Integer.MAX_VALUE);

		logger.println(Integer.toString(i));
		System.in.skip(System.in.available());
		System.out.println(System.in.available());

		return i;
	}

	/**
	 * Helper to get a float as input from the user
	 * @return
	 * @throws IOException
	 */
	public static float getFloat() throws IOException {
		print(debug ? TextMessages._t("RPG_MAIN.dint") : TextMessages._t("RPG_MAIN.val")); //$NON-NLS-1$ //$NON-NLS-2$
		float i = Integer.MAX_VALUE;
		String in;
		do {
			in = input.next();
			try {
				i = Float.parseFloat(in);
				break;
			} catch (NumberFormatException e) {
				print(TextMessages._t("RPG_MAIN.91")); //$NON-NLS-1$
			}
		} while (i == Integer.MAX_VALUE);

		logger.println(Float.toString(i));
		input.close();
		input = new Scanner(System.in);
		System.in.skip(System.in.available());
		System.out.println(System.in.available());

		return i;
	}

	/**
	 * Prints a String, if logging is active it also prints to the logging stream.
	 * @param s The string to be printed.
	 */
	public static void print(String s) {
		System.out.print(s);
		if (logging) {
			logger.print(s);
		}
	}

	/**
	 * Prints a String, if logging is active it also prints to the logging stream.
	 * Terminates the line at the end.
	 * @param s The string to be printed.
	 */
	public static void println(String s) {
		System.out.println(s);
		if (logging) {
			logger.println(s);
		}
	}

}
