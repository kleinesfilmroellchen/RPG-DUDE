package rpg.core;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;
import java.io.*;
import json.JSONException;
import json.JSONObject;
import rpg.core.interfaces.IItem;
import rpg.core.objects.*;
import rpg.helpers.*;
import rpg.local.TextMessages;
import utils.FileBuffer;

// Symbols for copying: <>|

/**
 * The all-mighty master class doing everything. <br>
 * <br>
 * String and Console message agreement:
 * <ul>
 * <li>Every complete (output) message is ended by a SINGLE newline
 * (System.lineSeparator()).</li>
 * <li>String methods with multiple lines NEVER END OR START with a
 * newline.</li>
 * <li>The same goes for PROPERTY Strings.</li>
 * <li>ANSI Control characters (especially backspace) should never affect more
 * than the current line, message or return string.</li>
 * </ul>
 * It is, of course, allowed to add extra newlines
 * @author DJH
 * @author kleinesfilmröllchen
 * @version 0.0.0009
 * @since 0.0.0001
 */
public class RPG_MAIN {

	// Variables
	/**
	 * Locale of the game, input by the user or determined by the system
	 * environment.
	 */
	public static Locale		gamelang;
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

	/**
	 * Main function.
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] _args) throws IOException, InterruptedException {
		//// Test code belongs here
		// System.out.println(Locale.getDefault().toLanguageTag());

		//// Async config loader callable
		ExecutorService ex = Executors.newSingleThreadExecutor();
		Future<JSONObject> configGetter = ex.submit(() -> {
			JSONObject config = Loaders
					.loadConfig(Arrays.stream(_args).filter(str -> str.matches("config=\\w+")).findFirst()
							.orElse("config=stdconfig.json").replaceAll("config=", "res/"));

			// setup language
			gamelang = Locale.forLanguageTag(config.getString("lang"));
			TextMessages.setLang(gamelang);
			// this makes all string formatting behave according to locale rules
			Locale.setDefault(gamelang);

			return config;
		});
		//// Async setup code: time consuming stuff belongs into this method.
		//// That stuff is executed on a separate thread without blocking the main
		//// program thread.
		Thread setupThread = new Thread(null, () -> {
			// long time = System.currentTimeMillis();
			Functionality.initGenerator();
			try {
				// might block while waiting for other thread
				JSONObject config = configGetter.get();
				// parse arguments into stream
				File log = FileBuffer.getFile("log.txt");
				logger = new PrintStream(log);

				rooms = Loaders.loadRoomsFromMap("res/" + config.getString("map"));

				loadEnemyPrefabs(config);
				loadEnemiesFromMap(config);
				Loaders.loadItemsInMap("res/" + config.getString("map"), rooms);

			} catch (IOException e) {
				System.err.println(__("msg.error.loggercreate")); //$NON-NLS-1$
				System.exit(5);
			} catch (SecurityException e) {
				System.err.println(__("msg.error.writepermission")); //$NON-NLS-1$
				System.exit(6);
			} catch (JSONException e) {
				System.err.println(__("msg.error.json")); //$NON-NLS-1$
				System.exit(2);
			} catch (Exception e) {
				System.err.println(__("msg.error.unknown"));
				e.printStackTrace();
				System.exit(4);
			} finally {
				logger.flush();
			}
			// System.out.println("Setup thread finished after " +
			// (System.currentTimeMillis() - time) + " milliseconds.");
		}, "SetupAsync");
		setupThread.start();

		//// Sync setup code
		try {
			// parse arguments into stream
			Stream<String> args = Arrays.stream(_args);
			// Argument checking
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
			if (logging) setupThread.join();
			input.useDelimiter("\\p{javaWhitespace}+"); //$NON-NLS-1$

			////// THE GAME HAS STARTED
			setupPlayer(configGetter.get());

			printfln(__("msg.splashscreen"), p.name(), GameConst.VERSION); //$NON-NLS-1$

		} catch (FileNotFoundException e) {
			println(__("msg.error.fnf") + e.getMessage()); //$NON-NLS-1$
			println(Arrays.toString(e.getStackTrace()));
			System.exit(1);
		} catch (JSONException e) {
			println(e.getMessage());
			println(__("msg.error.json")); //$NON-NLS-1$
			println(Arrays.toString(e.getStackTrace()));
			System.exit(2);
		} catch (NumberFormatException e) {
			println(__("msg.error.numberformat")); //$NON-NLS-1$
			println(Arrays.toString(e.getStackTrace()));
			System.exit(3);
		} catch (Exception e) {
			println(__("msg.error.unknown")); //$NON-NLS-1$
			println(Arrays.toString(e.getStackTrace()));
			System.exit(4);
		}

		setupThread.join();
		ex.shutdown();

		//////// Main game loop
		do {
			printfln("%n" + __("msg.select.action"), p.name()); //$NON-NLS-1$ //$NON-NLS-2$
			char opt = getOpt();

			if (opt == 'e') {
				printfln(__("msg.exit"), p.name()); //$NON-NLS-1$ //$NON-NLS-2$
				logger.close();
				break;
			}
			switch (opt) {

			case 'p':
				println(__("msg.itemdisclaimer"));
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
					println(__("msg.logenable")); //$NON-NLS-1$
				} else {
					println(__("msg.logdisable")); //$NON-NLS-1$
					logger.flush();
				}
				logging = !logging;
				break;

			case 'u':
				// player room
				Room pr = rooms[p.getZ()][p.getX()][p.getY()];
				// print a lot of info
				println(((Coordinates) pr).toString());
				printfln(__("msg.game.lookroom"), pr.getX(), pr.getY(), pr.getZ(), pr.roomName,
						pr.roomDescription);
				println(
						(pr.walkIsAllowed(0, 1) ? String.format(__("msg.game.canwalk") + System.lineSeparator(),
								__("msg.game.north")) : "") +
								(pr.walkIsAllowed(0, -1)
										? String.format(__("msg.game.canwalk") + System.lineSeparator(),
												__("msg.game.south"))
										: "")
								+
								(pr.walkIsAllowed(1, 0)
										? String.format(__("msg.game.canwalk") + System.lineSeparator(),
												__("msg.game.east"))
										: "")
								+
								(pr.walkIsAllowed(-1, 0)
										? String.format(__("msg.game.canwalk") + System.lineSeparator(),
												__("msg.game.west"))
										: ""));

				boolean seenSth = false;
				for (Entity e : enemies) {
					if (Coordinates.coordEqual(e, p)) {
						printfln(__("msg.game.see"), e.name()); //$NON-NLS-1$ //$NON-NLS-2$
						seenSth = true;
					}
				}
				if (!seenSth) println(__("msg.game.seenothing")); //$NON-NLS-1$

				seenSth = false;
				for (IItem item : pr.items) {
					printfln(__("msg.game.iteminroom"), item.getName());
					seenSth = true;
				}
				if (!seenSth) println(__("msg.game.noitem")); //$NON-NLS-1$

				break;

			case 'a':
				ArrayList<Enemy> inRoom = new ArrayList<Enemy>(enemies);
				inRoom.removeIf(e -> !Coordinates.coordEqual(e, p));

				if (inRoom.size() > 0) {
					for (int i = 0; i < inRoom.size(); i++) {
						Enemy e = inRoom.get(i);
						printfln(__("msg.select.attack"), e.name()); //$NON-NLS-1$ //$NON-NLS-2$
						char selection = getOpt();

						if (selection == 'y' || selection == 'j') {

							// execute battle
							battle(e, p);

							if (e.isDead()) {
								enemies.remove(i);
							}
							if (p.isDead()) {
								printfln(__("msg.game.defeat"), p.name()); //$NON-NLS-1$ //$NON-NLS-2$
								p.resetToBaseStats();
							}
							break;
						}
					}
				} else
					println(__("msg.game.noattackable")); //$NON-NLS-1$
				break;

			case 'p':
				ArrayList<IItem> inRoom = new ArrayList<IItem>();
				break;

			case 'i':
				println(Help.gameInfo());
				break;

			case 'd':
				if (debug) {
					debug = false;
					println(__("msg.debugdisable")); //$NON-NLS-1$
				} else {
					debug = true;
					println(__("msg.debugenable")); //$NON-NLS-1$
				}
				break;

			case 'x':
				if (debug) {
					println(__("msg.debug.xp")); //$NON-NLS-1$
					p.addExp(getInt());
				} else {
					println(__("msg.debuginactive")); //$NON-NLS-1$
				}
				break;

			case 'o':
				if (debug) {
					println(__("msg.debug.lvl")); //$NON-NLS-1$
					int extraLevels = getInt();
					for (int i = 0; i < extraLevels; i++) {
						p.addExp(p.necessaryXP());
						if (p.maxedOut) break;
					}
				} else {
					println(__("msg.debuginactive")); //$NON-NLS-1$
				}
				break;

			case 'z':
				if (debug) {
					println(__("msg.debug.hp")); //$NON-NLS-1$
					int h = getInt();
					p.addHealth(h);
				} else {
					println(__("msg.debuginactive")); //$NON-NLS-1$
				}
				break;

			default:
				println(__("msg.error.unknowncmd")); //$NON-NLS-1$
				break;
			}

		} while (true);
	}

	private static String __(String lookup) {
		if (debug) { return lookup + ": " + IItem.__(lookup); }
		return IItem.__(lookup);
	}

	/**
	 * Loads all enemies from the enemies.json file as prefabs and puts them into a
	 * lookup table. Those are later used to create the real enemies.
	 * @param config TODO
	 */
	private static void loadEnemyPrefabs(JSONObject config) throws JSONException, IOException {
		ArrayList<Enemy> dict = Loaders.getEnemies("res/" + config.getString("enemies")); //$NON-NLS-1$
		enemyPrefabs = dict;
	}

	/**
	 * Loads the actual enemies from the map file and sets their rooms. The private
	 * field {@code enemies} is set to the loaded enemies.
	 * @param config TODO
	 * @throws JSONException if something has incorrect format or values.
	 * @throws IOException if something with I/O goes wrong.
	 */
	private static void loadEnemiesFromMap(JSONObject config) throws JSONException, IOException {
		RPG_MAIN.enemies = Loaders.loadEnemiesOnMap("res/" + config.getString("map"), enemyPrefabs); //$NON-NLS-1$
	}

	/**
	 * Sets up the player, including levels etc.
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws JSONException
	 */
	private static void setupPlayer(JSONObject config) throws JSONException, FileNotFoundException, IOException {
		println(__("msg.select.name")); //$NON-NLS-1$
		p = new Player(
				getStr(),
				GameConst.P_BASE_LEVEL,
				Loaders.loadPlayerPos("res/" + config.getString("map"))); // $NON-NLS-1$ // $NON-NLS-2$
		p.futureStats = GameConst.requestPlayerLevels();
	}

	/** @return Current room where the player is. */
	public static Room currentRoom() {
		return rooms[p.getZ()][p.getX()][p.getY()];
	}

	/**
	 * Takes care of the battle mechanisms and is responsible for the whole battle.
	 * @param e The entity attacked by the player.
	 * @param p The player attacking the entity.
	 * @param callerOut The output stream to be used for status information.
	 * @throws IOException
	 */
	public static void battle(Enemy e, Player p) throws IOException {
		printfln(__("msg.game.battlestart"), p.name(), e.name()); //$NON-NLS-1$

		boolean flightSuccessful = false;
		while (true) {
			boolean playerAttack = true;

			p.usesDefence = e.usesDefence = false;

			if (flightSuccessful) break;

			// Player fight decisions
			printfln("%n" + __("msg.game.inbattle"), p.name()); //$NON-NLS-1$
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
				println(p.statString() + System.lineSeparator()); // $NON-NLS-1$
				println(e.statString());
				continue;

			// All not implemented or invalid actions
			case 'i':
				println(__("msg.itemdisclaimer")); //$NON-NLS-1$
				continue;

			case 'm':
				println(__("msg.magicdisclaimer")); //$NON-NLS-1$
				continue;

			case 'f':
				// Flight attempt
				printfln(__("msg.game.tryflee"), p.name()); //$NON-NLS-1$
				flightSuccessful = Functionality.fleeAttempt(p.mob, e.mob);
				if (flightSuccessful) {
					println(__("msg.game.couldflee")); //$NON-NLS-1$
					// skipping enemy attack and essentially ending fight
					continue;
				}
				// unsuccessful flight: player doesn't get to attack
				else
					println(__("msg.game.couldnotflee")); //$NON-NLS-1$
				playerAttack = false;
				break;

			default:
				println(__("msg.error.unknowncmd")); //$NON-NLS-1$
				continue;
			}

			if (!p.usesDefence && playerAttack) p.attack(e);
			if (debug) printfln(__("msg.debug.attackpe"), e.def, p.atk, e.health); //$NON-NLS-1$ //$NON-NLS-2$
			if (!e.usesDefence) e.attack(p);
			if (debug) printfln(__("msg.debug.attackep"), p.def, e.atk, p.health); //$NON-NLS-1$ //$NON-NLS-2$

			// Exit conditions
			if (p.isDead()) {
				printfln(__("msg.game.defeated"), p.name()); //$NON-NLS-1$ //$NON-NLS-2$
				p.resetToBaseStats();
				break;
			}
			if (e.isDead()) {
				printfln(__("msg.game.defeating"), e.name(), p.name(), p.name(), e.reward); //$NON-NLS-1$ //$NON-NLS-2$
				p.addExp(e.reward);
				break;
			}
		}

		println(__("msg.game.battleend")); //$NON-NLS-1$
	}

	/**
	 * Asks the user for movement input. The player will be moved in the direction
	 * the user specified.
	 * @throws IOException
	 */
	public static void askForMovement() throws IOException {
		printf(__("msg.select.dir")); //$NON-NLS-1$
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
			print(__("msg.error.nosuchdir")); //$NON-NLS-1$
			return;
		}

		// Checks whether there is an exit in the given direction
		if (rooms[p.getZ()][p.getX()][p.getY()].walkIsAllowed(x, y)) {
			p.add(x, y, 0);
			printfln(__("msg.game.moveto"), p.getX(), p.getY(), p.getZ(), rooms[p.getZ()][p.getX()][p.getY()].roomName);
		} else {
			printfln(__("msg.error.noexit"), (dir == 'n' ? __("msg.game.north") //$NON-NLS-1$
					: dir == 's' ? __("msg.game.south") //$NON-NLS-1$
							: dir == 'o' ? __("msg.game.east") : __("msg.game.west"))); //$NON-NLS-1$
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
			print(debug ? __("msg.debug.char") : __("msg.select.char")); //$NON-NLS-1$ //$NON-NLS-2$
			try {
				opt = input.next().toLowerCase().charAt(0);
			} catch (InputMismatchException e) {
				println(__("msg.error.invalidchar")); //$NON-NLS-1$
			} catch (StringIndexOutOfBoundsException e) {
				println(__("msg.error.insufficientlength")); //$NON-NLS-1$
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
		print(debug ? __("msg.debug.val") : __("msg.select.val")); //$NON-NLS-1$ //$NON-NLS-2$
		int i = Integer.MAX_VALUE;
		String in;
		do {
			in = input.next();
			try {
				i = Integer.parseInt(in);
				break;
			} catch (NumberFormatException e) {
				print(__("msg.error.noint")); //$NON-NLS-1$
			}
		} while (i == Integer.MAX_VALUE);

		logger.println(Integer.toString(i));
		input.skip(".*");

		return i;
	}

	public static String getStr() throws IOException {
		String opt = "";
		do {
			print(debug ? __("msg.debug.string") : __("msg.select.string")); //$NON-NLS-1$ //$NON-NLS-2$
			try {
				opt = input.next().toLowerCase();
			} catch (InputMismatchException e) {
				println(__("msg.error.invalidchar")); //$NON-NLS-1$
			} catch (StringIndexOutOfBoundsException e) {
				println(__("msg.error.insufficientlength")); //$NON-NLS-1$
			}
		} while (opt.length() <= 0);

		logger.println(opt);
		input.skip(".*"); //$NON-NLS-1$

		return opt;
	}

	/**
	 * Helper to get a float as input from the user
	 * @return
	 * @throws IOException
	 */
	public static float getFloat() throws IOException {
		print(debug ? __("msg.debug.val") : __("msg.select.val")); //$NON-NLS-1$ //$NON-NLS-2$
		float i = Integer.MAX_VALUE;
		String in;
		do {
			in = input.next();
			try {
				i = Float.parseFloat(in);
				break;
			} catch (NumberFormatException e) {
				print(__("msg.error.nofloat")); //$NON-NLS-1$
			}
		} while (i == Integer.MAX_VALUE);

		logger.println(Float.toString(i));
		input.skip(".*");

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

	/**
	 * Prints a string while first passing it through the
	 * {@code String.format(s, args)}.
	 * @param s String to format and print.
	 * @param args Formatting arguments.
	 * @see String#format(String, Object...)
	 */
	public static void printf(String s, Object... args) {
		print(String.format(s, args));
	}

	/**
	 * Prints a string while first passing it through the
	 * {@code String.format(s, args)}. The line is terminated.
	 * @param s String to format and print.
	 * @param args Formatting arguments.
	 * @see String#format(String, Object...)
	 */
	public static void printfln(String s, Object... args) {
		println(String.format(s, args));
	}

}
