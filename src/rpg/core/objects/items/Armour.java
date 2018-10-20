package rpg.core.objects.items;

import rpg.core.GameConst;
import rpg.core.objects.Item;
import rpg.core.objects.Player;
import rpg.helpers.State;

/**
 * Armour is a special type of equipment that the player can wear through his
 * "armour" slot. Armour gives passive defence but also mobility decrease,
 * specified by the "weight".
 * @author malub
 */
public class Armour implements Item {

	private String msg;

	private int		protection;
	private double	weight;
	private String	name;

	private String id;

	public Armour(int protection, double weight, String name, String id) {
		this.weight = weight;
		this.protection = protection;
		this.name = name;
		this.id = id;
	}

	@Override
	public int getSellValue() {
		return this.protection * GameConst.SELL_FACTOR;
	}

	@Override
	public String getLastFailMessage() {
		return msg;
	}

	@Override
	public String getDescription() {
		return String.format("Rüstungen schützen dich vor Angriffen.%n- Schutz: %d%n- Gewicht: %.2f", protection, weight);
	}

	@Override
	public String getName() {
		return name;
	}

	public int getProtection() {
		return protection;
	}

	public double getWeight() {
		return weight;
	}

	@Override
	public State use(Player player) {
		msg = "Eine Rüstung kann nicht benutzt werden!";
		return State.failed;
	}

	@Override
	public State equip(Player player) {
		if (player.armour.isFull()) {
			msg = "Du hast bereits eine Rüstung angelegt!";
			return State.notAllowed;
		}
		player.armour.add(this);
		return State.finished;
	}

	@Override
	public String getID() {
		return id;
	}

}
