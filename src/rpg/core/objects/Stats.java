package rpg.core.objects;

/**
 * Dumb data storage class for future stats
 * @author kleinesfilmröllchen
 * @since 0.0.0004
 * @version 0.0.0007
 */
public class Stats {
	public int maxHealth;
	public int atk;
	public int def;
	public int necessaryXP;
	public double mob;
	
	/**
	 * Default constructor to set all values.
	 * @param maxHealth The maximum health for these stats
	 * @param atk The attack value for these stats
	 * @param def The defense value for these stats
	 * @param mob The mobility value for these stats
	 * @param necessaryXP The necessary experience points for the next level
	 */
	public Stats(int maxHealth, int atk, int def, double mob, int necessaryXP) {
		this.maxHealth = maxHealth;
		this.atk = atk;
		this.def = def;
		this.mob = mob;
		this.necessaryXP = necessaryXP;
	}
	
	public Stats clone() {
		return new Stats(this.maxHealth, this.atk, this.def, this.mob, this.necessaryXP);
	}
}
