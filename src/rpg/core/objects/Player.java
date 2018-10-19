package rpg.core.objects;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import json.*;
import rpg.core.RPG_MAIN;
import rpg.core.objects.items.Abillity;
import rpg.helpers.Functionality;
import utils.BetterMath;

/**
 * The Player object. Currently only instantiated once.
 * @author DJH
 * @author kleinesfilmröllchen
 * @since 0.0.0001
 * @version 0.0.0007
 */
public class Player extends Entity {

	private int		xp;
	private int		necessaryXP;
	private int		level;
	private String	name;

	/** Controls whether the player reached max level. */
	public boolean maxedOut = false;

	/** All stats to be used for future levels. */
	public ArrayList<Stats>	futureStats	= new ArrayList<Stats>();
	/** Basic stats of level 1, defined by the constructor parameters. */
	private Stats				baseStats;
	private Coordinates		startPos;

	public Slots<Abillity> abilities = new Slots<Abillity>(3);

	/** Player armour item, will be */
	public Slots<Item>	armour			= new Slots<Item>(1);
	public Slots<Item>	holdingItems	= new Slots<Item>(2);
	public Slots<Item>	bagItems			= new Slots<Item>(15);

	/** Name wrapper. */
	public String name() {
		return this.name;
	}

	/** Experience points wrapper. */
	public int xp() {
		return this.xp;
	}

	/** Necessary experience points wrapper. */
	public int necessaryXP() {
		return this.necessaryXP;
	}

	public Player(String n, Stats startingStats) {
		this.name = n;

		this.xp = 0;
		this.level = 1;
		this.health = this.maxHealth = startingStats.maxHealth;
		this.def = startingStats.def;
		this.atk = startingStats.atk;
		this.mob = startingStats.mob;
		this.necessaryXP = startingStats.necessaryXP;

		this.baseStats = startingStats.clone();
		this.startPos = new Coordinates(this.getCoords());
	}

	/**
	 * Constructor that should be used in all cases
	 * @param n Player name
	 * @param startingStats Stats to use for the start
	 * @param absolutePath path to map
	 * @throws JSONException if map not correct format
	 * @throws FileNotFoundException if file path invalid
	 */
	public Player(String n, Stats startingStats, String absolutePath)
			throws JSONException, FileNotFoundException, IOException {

		super();

		JSONObject map = new JSONObject(Functionality.readFileContents(absolutePath));
		try {
			JSONArray posArray = map.getJSONObject("playerdata").getJSONArray("start-pos");
			this.setCoords(posArray.getInt(0), posArray.getInt(1), posArray.getInt(2));
		} catch (JSONException je) {
			RPG_MAIN.println("Player data not found, using default values");
		}

		this.name = n;

		this.xp = 0;
		this.level = 1;
		this.health = this.maxHealth = startingStats.maxHealth;
		this.def = startingStats.def;
		this.atk = startingStats.atk;
		this.mob = startingStats.mob;
		this.necessaryXP = startingStats.necessaryXP;

		this.baseStats = startingStats.clone();
		this.startPos = new Coordinates(this.getCoords());
	}

	/** A nice formatted stats String. */
	public String statString() {
		return "\nName: " + this.name +
				"\nBisherige Stats:" +
				"\nLevel:        " + this.level +
				"\nExp:          " + this.xp + " / " + this.necessaryXP +
				"\nLeben:        " + this.health + " / " + this.maxHealth +
				"\nVerteidigung: " + this.def +
				"\nAngriff:      " + this.atk +
				"\nMobilität:    " + this.mob;
	}

	/**
	 * Adds experience points.
	 * @param exp The experience to add.
	 */
	public void addExp(int exp) {
		this.xp += exp;

		if (this.maxedOut) {
			RPG_MAIN.println(this.name + " hat bereits den höchsten Level erreicht.");
			return;
		}

		while (this.xp >= this.necessaryXP && !this.maxedOut) {
			// theoretical index into future stats is to high: no level up possible
			if (this.level - 1 > this.futureStats.size() - 1) {
				RPG_MAIN.println(
						this.name + " hat den maximalen Level von " + this.level + " erreicht. Herzlichen Glückwunsch!");
				this.maxedOut = true;
				break;
			}

			RPG_MAIN.println("\n\nLevel " + (this.level + 1) + " erreicht!\n");
			RPG_MAIN.println("Bisherige Stats:");
			RPG_MAIN.println("Level:            " + this.level);
			RPG_MAIN.println("Maximales Leben:  " + this.maxHealth);
			RPG_MAIN.println("Verteidigung:     " + this.def);
			RPG_MAIN.println("Angriff:          " + this.atk);
			RPG_MAIN.println("Mobilität:        " + this.mob);

			this.xp -= this.necessaryXP;
			this.level++;

			// Future stats start at level 2 and are zero-indexed: subtract 2 from level
			int statIndex = this.level - 2;
			this.maxHealth = this.futureStats.get(statIndex).maxHealth;
			this.atk = this.futureStats.get(statIndex).atk;
			this.def = this.futureStats.get(statIndex).def;
			this.necessaryXP = this.futureStats.get(statIndex).necessaryXP;
			this.mob = this.futureStats.get(statIndex).mob;

			RPG_MAIN.println("\nNeue Stats:");
			RPG_MAIN.println("Level:            " + this.level);
			RPG_MAIN.println("Maximales Leben:  " + this.maxHealth);
			RPG_MAIN.println("Verteidigung:     " + this.def);
			RPG_MAIN.println("Angriff:          " + this.atk);
			RPG_MAIN.println("Mobilität:        " + this.mob);
		}
	}

	/**
	 * Adds the given amount of health to the player while keeping the health in the
	 * correct range.
	 * @param h The health to be added.
	 */
	public int addHealth(int h) {
		this.health += Math.abs(h);
		this.health = BetterMath.clamp(health, maxHealth, 0);
		return this.health;
	}

	/**
	 * Sets all stats back to the base stats declared in the beginning. Used for
	 * "killing" the player.
	 */
	public void resetToBaseStats() {
		this.atk = baseStats.atk;
		this.def = baseStats.def;
		this.maxHealth = this.health = baseStats.maxHealth;
		this.necessaryXP = baseStats.necessaryXP;
		this.level = 1;
		this.maxedOut = false;
	}
}
