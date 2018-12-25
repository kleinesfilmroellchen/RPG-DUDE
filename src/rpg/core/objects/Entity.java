package rpg.core.objects;

import rpg.core.*;
import rpg.local.TextMessages;

/**
 * The abstract superclass for all movable, attackable and interactable objects in the game,
 * called entitys. This class provides a framework on how each entity should interface with other objects
 * and what properties it has. Because each entity has a position, Entity itself extends the {@code Coordinates} class.<br>
 * This class provides default behaviors for many of the functionality an entity has, but the subclasses can override these.
 * The description of every method states the general contract of this method and what it should do.
 * @author DJH
 * @author kleinesfilmröllchen
 * @version 0.0.0008
 * @since 0.0.0002
 * @see rpg.core.objects.Coordinates Coordinates
 */
public abstract class Entity extends Coordinates {
	
	/**Entity health, as a measure of how much damage it can take.*/
	public int health;
	/**Maximum health the entity can ever have.*/
	protected int maxHealth;
	/**Attack stat. Determines how devastating and powerful attacks are.*/
	public int atk;
	/**Defence stat. Determines how good the entity defends against attacks.*/
	public int def;
	/**Mobillity stat. Determines both how easily the entity dodges attacks and how easily it can flee out of battles.*/
	public double mob;
	
	/**Multiplier. Determines by which number the defence is multiplied when actively defending in battles.*/
	public double defMult = GameConst.DEFENSE_MULTIPLIER;
	/**Used by methods and set by the battle-controlling code to set whether this entity is actively defending.*/
	public boolean usesDefence = false;
	
	/**Reward for killing the entity. Unused for some entities.*/
	public int reward = 0;
	
	/**Superclass constructor.*/
	public Entity(int x, int y, int z) {
		super(x,y,z);
	}
	
	/**Superclass constructor.*/
	public Entity() {
		super();
	}

	/**Returns the entity's name.*/
	public abstract String name();
	
	/**Returns stats about the entity.*/
	public String toString() {
		return this.name() + ": [" + x + "|" + y + "|" + z + "] HP: " + health + "/" + maxHealth +" A: " + atk + " V: " + def;
	}
	
	/**Moves the entity one floor up.*/
	public void floorUp() {
		this.addZ(1);
	}
	/**Moves the entity one floor down.*/
	public void floorDown() {
		this.addZ(-1);
	}
	
	/**
	 * Used when the Entity takes damage without defending.<br>
	 * The general contract of this function is that it deals damage to the entity according to the game fight rules.
	 * The method template represents the "normal" way of taking damage without defending.
	 * @param dmg The damage to be taken by the Entity.
	 * @return The actual damage applied, can be used for display, further calculations etc.
	 * @see rpg.core.Entity#takeDefDamage(int) takeDefDamage()
	 */
	public int takeDamage(int dmg) {
		int damageTaken = (dmg - this.def);
		return this.damage(damageTaken);
	}
	
	/**
	 * Used when the Entity takes damage while defending.<br>
	 * The general contract of this function is that it deals damage to the entity according to the game fight rules.
	 * The method template represents the "normal" way of taking damage while defending.
	 * @param dmg The damage to be taken by the Entity.
	 * @return The actual damage applied.
	 * @see rpg.core.Entity#takeDamage(int) takeDamage()
	 */
	
	public int takeDefDamage(int dmg) {
		int damageTaken = (int)Math.round(dmg - (this.def * this.defMult));
		return this.damage(damageTaken);
	}
	
	/**
	 * Does exactly the damage given to the function. Used as "final executor" for all damage taking methods.
	 * Subclasses don't need to overwrite this.
	 * @param damageTaken The damage to be taken by the Entity.
	 * @return The actual damage applied, can be used for display, further calculations etc.
	 */
	protected int damage(int damageTaken) {
		//prevent negative
		if (damageTaken < 0) return 0;
				
		this.health -= damageTaken;
		return damageTaken;
	}
	
	/**
	 * Attacks the given other entity and deals damage to it. (Replaces attackDummy in favor of a generic function)
	 * @param enemy The Entity to be attacked.
	 */
	public void attack(Entity enemy) {
		RPG_MAIN.printfln(TextMessages._t("msg.game.attacks"), this.name(), enemy.name());
		if(enemy.usesDefence) RPG_MAIN.printfln(TextMessages._t("msg.game.defending"), enemy.name());
		
		int damageTaken = enemy.usesDefence ? enemy.takeDefDamage(this.atk) : enemy.takeDamage(this.atk);
		RPG_MAIN.printf(TextMessages._t("msg.game.takesdamage"), enemy.name(), damageTaken); 
		RPG_MAIN.printfln(damageTaken == 0 ? TextMessages._t("msg.game.attackuseless") : "");
	}
	
	/**Checks whether the entity is dead.*/
	public boolean isDead() {
		return health <= 0;
	}
}
