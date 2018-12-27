package rpg.helpers;

import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import json.*;
import rpg.core.objects.*;
import rpg.local.TextMessages;
import utils.FileBuffer;
import rpg.core.*;
import rpg.core.interfaces.IItem;

/**
 * A loader class with the file loading functionality.
 * @author kleinesfilmröllchen
 * @version 0.0.0008
 * @since 0.0.0007
 */
public class Loaders {
	/**
	 * Loads the player position
	 * @param mapname Path to the map (without /res)
	 * @return Coordinates with player starting position
	 */
	public static Coordinates loadPlayerPos(String mapname) throws JSONException, FileNotFoundException, IOException {
		JSONObject map = new JSONObject(FileBuffer.getContent(mapname));
		JSONArray coords = map.getJSONObject("playerdata").getJSONArray("start-pos");
		return new Coordinates(coords.getInt(0), coords.getInt(1), coords.getInt(2));
	}

	/**
	 * Loads the configuration file for the game and returns a JSON object whose
	 * values are guaranteed to contain something
	 * @param configname Name of configuration file to be loaded
	 * @return JSON object with configuration values, no required value will be
	 * unset
	 * @throws IOException
	 */
	public static JSONObject loadConfig(String configname) throws IOException {
		// finds first specified config file; if there is none, uses stdconfig.json
		JSONObject config = new JSONObject(FileBuffer.getContent(configname));

		// create values if not present
		if (!config.has("map")) config.put("map", "map1.json");
		if (!config.has("enemies")) config.put("enemies", "enemies.json");
		if (!config.has("lang")) {
			//use system locale
			Locale sysloc = Locale.getDefault();
			config.put("lang", sysloc.toLanguageTag());
		}

		return config;
	}

	/**
	 * Loads the map.
	 * @param absolutePath The path to the JSON map file.
	 */
	public static Room[][][] loadRoomsFromMap(String absolutePath) throws JSONException, FileNotFoundException,
			IOException, NumberFormatException, ArrayIndexOutOfBoundsException {
		JSONObject map = new JSONObject(FileBuffer.getContent(absolutePath));
		// Map version doesn't equal this game's version
		if (!map.getString("compatible-version").equalsIgnoreCase(GameConst.VERSION)) {
			System.err.printf(
					Factory.__("msg.error.incompatibleversion"),
					GameConst.VERSION);
			System.exit(1);
		}

		JSONArray rMatrix = map.getJSONArray("room-matrix");
		// Room 3D Array
		Room[][][] roomArrayMatrix = new Room[rMatrix.length()] // Number of floors
		[rMatrix.getJSONObject(0).getJSONArray("rooms").length()] // Number of room rows (y) in a floor
		[rMatrix.getJSONObject(0).getJSONArray("rooms").getJSONArray(0).length()]; // Number of rooms (x) in a row

		// for every floor in the room matrix
		for (int i = 0; i < rMatrix.length(); i++) {
			JSONObject floor = rMatrix.getJSONObject(i);
			// Index where to add into the array list later
			int floorLevel = floor.getInt("floor");
			// single floor 2d matrix array
			JSONArray singleFloorMatrix = floor.getJSONArray("rooms");

			// for every row in the single floor matrix array
			for (int y = 0; y < singleFloorMatrix.length(); ++y) {
				// get the room row
				JSONArray roomRow = singleFloorMatrix.getJSONArray(y);

				for (int x = 0; x < roomRow.length(); ++x) {
					int roomNum = roomRow.getInt(x);

					// we now have the room exit identifying number.
					// extract exits by bitwise operations
					boolean ladderUp = ((roomNum >> 5) & 0x1) == 1 ? true : false;
					boolean ladderDown = ((roomNum >> 4) & 0x1) == 1 ? true : false;
					boolean exitNorth = ((roomNum >> 3) & 0x1) == 1 ? true : false;
					boolean exitSouth = ((roomNum >> 2) & 0x1) == 1 ? true : false;
					boolean exitEast = ((roomNum >> 1) & 0x1) == 1 ? true : false;
					boolean exitWest = (roomNum & 0x1) == 1 ? true : false;

					// Create the room
					Room room = new Room(x, y, floorLevel,
							exitNorth, exitSouth, exitEast, exitWest, ladderUp, ladderDown);
					// add room to matrix
					roomArrayMatrix[floorLevel][x][y] = room;

				} // end of one room row

			} // end of one floor

		} // end of all floors and whole matrix

		// Room names and descriptions
		JSONArray roomDescriptions = map.getJSONArray("room-descriptions");
		for (int i = 0; i < roomDescriptions.length(); ++i) {
			JSONObject descObj = roomDescriptions.getJSONObject(i);
			JSONArray posArray = descObj.getJSONArray("pos");
			roomArrayMatrix[posArray.getInt(2)][posArray.getInt(0)][posArray.getInt(1)].roomName = descObj
					.getString("name");
			roomArrayMatrix[posArray.getInt(2)][posArray.getInt(0)][posArray.getInt(1)].roomDescription = descObj
					.getString("description");
		}

		return roomArrayMatrix;
	}

	/**
	 * Reads a JSON (Java Script Object Notation) file and creates enemies from the
	 * data found in the file. All enemies it can find are returned.
	 * @param absolutePath The absolute Path to the file.
	 * @return All Entities, parsed from the JSON.
	 * @throws JSONException if the JSON contains syntactical errors at an unhandled
	 * position in the method.
	 * @throws ClassCastException if certain values are not of the correct type.
	 * @throws IOException if something with I/O goes wrong.
	 */
	public static ArrayList<Enemy> getEnemies(String absolutePath)
			throws JSONException, ClassCastException, IOException {
		File f = new File(absolutePath);
		if (f.isDirectory()) throw new FileNotFoundException(String.format(Factory.__("msg.error.fnf"), f.getAbsolutePath()));

		JSONArray array = new JSONArray(FileBuffer.getContent(absolutePath));
		ArrayList<Enemy> returner = new ArrayList<Enemy>(array.length());

		for (int i = 0; i < array.length(); i++) {
			JSONObject o = array.getJSONObject(i);
			try {
				returner.add(new Enemy(
						o.getInt("health"),
						o.getInt("atk"),
						o.getInt("def"),
						o.getNumber("mob").doubleValue(),
						o.getInt("reward"),
						o.getString("descn"),
						o.getString("desc")));
				returner.get(i).id = o.getString("name");
			} catch (JSONException je) {
				System.err.printf(Factory.__("msg.error.incompatibleenemy"), absolutePath);
				je.printStackTrace();
			}
		}
		return returner;
	}

	/**
	 * Returns the one requested enemy out of the file.
	 * @param absolutePath The absolute path to the enemies file.
	 * @param id The id (JSON key "name") of the enemy that is to be found.
	 * @return The enemy, if there is one with the given id. Otherwise, {@code null}
	 * is returned.
	 * @throws JSONException If something is wrong with the JSON syntax.
	 */
	public static Enemy getEnemy(String absolutePath, String id) throws JSONException, IOException {
		ArrayList<Enemy> array = getEnemies(absolutePath);
		for (Enemy e : array) {
			if (e.id.equals(id)) return e;
		}

		return null;
	}

	/**
	 * Loads actual enemy instances and their positions from a map JSON file.
	 * @param absolutePath The absolute path to the map file.
	 * @param dict The enemy dictionary which is used to create instances by cloning
	 * the respective enemy in the dictionary.
	 * @return All enemies of the map with correct settings.
	 * @throws IOException if something with I/O goes wrong.
	 * @throws FileNotFoundException if the file cannot be found.
	 * @throws JSONException if the JSON is unreadable or an undefined enemy is
	 * present in the map.
	 */
	public static ArrayList<Enemy> loadEnemiesOnMap(String absolutePath, ArrayList<Enemy> dict)
			throws JSONException, FileNotFoundException, IOException {
		ArrayList<Enemy> returnEnemies = new ArrayList<Enemy>();

		JSONArray enemies = new JSONObject(FileBuffer.getContent(absolutePath)).getJSONArray("enemies");
		for (int i = 0; i < enemies.length(); i++) {
			JSONObject enemyObj = enemies.getJSONObject(i);
			// find an enemy in the dictionary that has the id of the current enemy
			Optional<Enemy> reference = dict.stream()
					.filter(element -> element.id.equalsIgnoreCase(enemyObj.getString("id"))).findFirst();
			// if the optional contains a non-null value we found something in the
			// dictionary
			if (reference.isPresent()) {
				// clone the enemy prefab
				returnEnemies.add(reference.get().clone());
				// set the real position
				JSONArray posArray = enemyObj.getJSONArray("pos");
				returnEnemies.get(returnEnemies.size() - 1).setCoords(posArray.getInt(0), posArray.getInt(1),
						posArray.getInt(2));
			} else {
				// this should not happen: throw exception
				throw new JSONException(String.format(Factory.__("msg.error.invalidenemyid"), enemyObj.getString("id")));
			}
		}

		return returnEnemies;
	}

	/**
	 * Loads all the items from the map. The Factory is used for this.
	 * @param path the file path to the map that should be loaded.
	 * @param rooms the rooms that the items will be put into, depending on their
	 * position. This is modified.
	 * @throws JSONException if the JSON has wrong format.
	 * @throws FileNotFoundException if the path is invalid.
	 * @throws IOException if I/O goes bad.
	 * @see rpg.helpers.Factory
	 */
	public static void loadItemsInMap(String path, Room[][][] rooms)
			throws JSONException, FileNotFoundException, IOException {

		JSONObject file = new JSONObject(Functionality.readFileContents(path));
		JSONArray array = file.getJSONArray("items");
		for (Object itemObj : array) {
			JSONObject itemJson = (JSONObject) itemObj;
			ArrayList<IItem> itemlist = new ArrayList<>();
			int count = itemJson.getInt("count");
			for (int i = 0; i < count; ++i)
				itemlist.add(Factory.byId(itemJson.getString("id")));
			int x = itemJson.getJSONArray("pos").getInt(0),
					y = itemJson.getJSONArray("pos").getInt(1),
					z = itemJson.getJSONArray("pos").getInt(2);
			rooms[z][x][y].items.addAll(itemlist);
		}
	}
}
