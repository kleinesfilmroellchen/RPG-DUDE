package me.rpg.core.objects.items;

import me.rpg.core.GameConst;
import me.rpg.core.objects.Item;
import me.rpg.core.objects.Player;
import me.rpg.helpers.State;

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

	public Armour(int protection, double weight, String name) {
		this.weight = weight;
		this.protection = protection;
		this.name = name;
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

}
