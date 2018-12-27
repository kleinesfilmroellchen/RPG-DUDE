package rpg.core.objects;

/**
 * Base class for enemies.<br>
 * <br>
 * First, enemies are instantiated to serve as a prototype, and they are
 * declared through a file called {@code enemies.json} which sits in the root
 * directory of the program execution. Later, when the maps are retrieved, any
 * enemy there will be instantiated and put into the entity list.<br>
 * Enemies are the entities that a player can battle. Other entities are not
 * attackable.
 * @author kleinesfilmröllchen
 * @version 0.0.0008
 * @since 0.0.0006
 * @see rpg.core.objects.Entity Entity superclass
 */
public class Enemy extends Entity {

	/**
	 * Name of the enemy. <br>
	 * <strong>NOTE: this is the displayed name of the enemy and given by the JSON
	 * value for the key "descn" (standing for description name). You CAN'T search
	 * for an enemy by using its description name, you have to use a string that
	 * equals the JSON value for the key "name".</strong><br>
	 * <br>
	 * This string is displayed in information menues and may later be used to check
	 * if two enemies are the same (or similar). Until then, it remains as a
	 * "cosmetic" value.
	 */
	private String name = __("msg.game.enemy");

	/**
	 * "Identification" string for the enemy. This string is actually used to refer
	 * to this enemy type in maps. It is determined by the JSON value for the key
	 * "name" and <strong>must</strong> be unique.
	 */
	public String id;

	/**
	 * Description for the enemy, as given by the JSON value for the key "desc".
	 */
	private String description = __("msg.game.enemydesc");

	/**
	 * The constructor to set all properties. This is especially the only way to
	 * write to private fields such as {@code name} or {@code description}.
	 */
	public Enemy(int health, int atk, int def, double mob, int reward, String name, String description) {
		this.health = this.maxHealth = health;
		this.reward = reward;
		this.atk = atk;
		this.def = def;
		this.mob = mob;
		this.name = name;
		this.description = description;
	}

	@Override
	public String name() {
		return this.name;
	}

	/**
	 * Retrieves the description for this enemy (serves as a write-protect method
	 * for the description).
	 */
	public String description() {
		return this.description;
	}

	public String statString() {
		return super.statString() + String.format("%n" + __("msg.game.stats.reward"), this.reward);
	}

	public Enemy clone() {
		Enemy e = new Enemy(this.health, this.atk, this.def, this.mob, this.reward, this.name, this.description);
		e.id = this.id;
		return e;
	}

}
