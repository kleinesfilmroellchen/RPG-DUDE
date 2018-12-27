package rpg.core.objects;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import json.*;
import rpg.core.RPG_MAIN;
import rpg.core.interfaces.IItem;
import rpg.core.objects.items.Abillity;
import utils.BetterMath;
import utils.FileBuffer;

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

	/** Player armour item. */
	public Slots<IItem>	armour			= new Slots<IItem>(1);
	/** Items the player holds: active in battles. */
	public Slots<IItem>	holdingItems	= new Slots<IItem>(2);
	/** Items in the player's bag/inventory. */
	public Slots<IItem>	bagItems			= new Slots<IItem>(15);

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
	 * Deprecated self-reading constructor
	 * @param n Player name
	 * @param startingStats Stats to use for the start
	 * @param absolutePath path to map
	 * @throws JSONException if map not correct format
	 * @throws FileNotFoundException if file path invalid
	 * @deprecated Use {@link #Player(String,Stats,Coordinates)} instead; this
	 * causes problems all over
	 */
	public Player(String n, Stats startingStats, String absolutePath)
			throws JSONException, FileNotFoundException, IOException {

		super();

		JSONObject map = new JSONObject(FileBuffer.getContent(absolutePath));
		try {
			JSONArray posArray = map.getJSONObject("playerdata").getJSONArray("start-pos"); //$NON-NLS-1$ //$NON-NLS-2$
			this.setCoords(posArray.getInt(0), posArray.getInt(1), posArray.getInt(2));
		} catch (JSONException je) {
			RPG_MAIN.println(__("msg.error.playerdatamissing")); //$NON-NLS-1$
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

	/**
	 * Constructor that should be used in all cases
	 * @param n Player name
	 * @param startingStats Stats to use for the start
	 * @param position Starting position of the player
	 * @throws JSONException if map not correct format
	 * @throws FileNotFoundException if file path invalid
	 */
	public Player(String n, Stats startingStats, Coordinates position)
			throws JSONException, FileNotFoundException, IOException {

		super(position.getX(), position.getY(), position.getZ());

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

	/** A nicely formatted stats String. */
	public String statString() {
		return String.format(__("msg.game.stats.level") + "%n", this.level) + super.statString(); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Adds experience points.
	 * @param exp The experience to add.
	 */
	public void addExp(int exp) {
		this.xp += exp;

		if (this.maxedOut) {
			RPG_MAIN.printfln(__("msg.game.playermaxed"), this.name()); //$NON-NLS-1$
			return;
		}

		// first check ensures that player has enough xp for the levelup
		while (this.xp >= this.necessaryXP && !this.maxedOut) {
			// theoretical index into future stats is to high: no level up possible
			if (this.level - 1 > this.futureStats.size() - 1) {
				RPG_MAIN.printfln(__("msg.game.playerreachedmax"), this.name(), this.level); //$NON-NLS-1$
				this.maxedOut = true;
				break;
			}

			RPG_MAIN.printfln(__("msg.game.levelup"), //$NON-NLS-1$
					(this.level + 1));
			RPG_MAIN.println(this.statString());

			this.xp -= this.necessaryXP;
			++this.level;

			// Future stats start at level 2 and are zero-indexed: subtract 2 from level
			int statIndex = this.level - 2;
			this.maxHealth = this.futureStats.get(statIndex).maxHealth;
			this.atk = this.futureStats.get(statIndex).atk;
			this.def = this.futureStats.get(statIndex).def;
			this.necessaryXP = this.futureStats.get(statIndex).necessaryXP;
			this.mob = this.futureStats.get(statIndex).mob;

			RPG_MAIN.println(System.lineSeparator() + __("msg.game.levelnow")); //$NON-NLS-1$
			RPG_MAIN.println(this.statString());
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
	 * "killing" the player. Also resets the player position
	 */
	public void resetToBaseStats() {
		super.x = this.startPos.x;
		super.y = this.startPos.y;
		super.z = this.startPos.z;

		this.atk = baseStats.atk;
		this.def = baseStats.def;
		this.maxHealth = this.health = baseStats.maxHealth;
		this.necessaryXP = baseStats.necessaryXP;
		this.level = 1;
		this.maxedOut = false;
	}
}
