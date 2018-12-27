package rpg.core.objects.items;

import rpg.core.GameConst;
import rpg.core.interfaces.IItem;
import rpg.core.objects.Player;
import rpg.helpers.State;
import rpg.local.TextMessages;

/**
 * Armour is a special type of equipment that the player can wear through his
 * "armour" slot. Armour gives passive defence but also mobility decrease,
 * specified by the "weight".
 * @author malub
 */
public class Armour implements IItem {

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
		return String.format(IItem.__("msg.items.armourinfo"), protection, weight); //$NON-NLS-1$
	}

	@Override
	public String getName() {
		return name;
	}

	public String getShortDisplay() {
		return this.getName() + " (" + IItem.__("msg.items.armour") + ")";
	}

	public String toString() {
		return this.getShortDisplay();
	}

	public int getProtection() {
		return protection;
	}

	public double getWeight() {
		return weight;
	}

	@Override
	public State use(Player player) {
		msg = IItem.__("msg.error.notusable"); //$NON-NLS-1$
		return State.failed;
	}

	@Override
	public State equip(Player player) {
		if (player.armour.isFull()) {
			msg = IItem.__("msg.error.alreadyequipped"); //$NON-NLS-1$
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
