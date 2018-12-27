package rpg.core.objects.items;

import rpg.core.interfaces.IItem;
import rpg.core.objects.Player;
import rpg.helpers.Factory;
import rpg.helpers.State;
import rpg.local.TextMessages;

/**
 * Blueprint for any item that is only for crafting and cannot be used.
 * @author kleinesfilmroellchen
 * @version 0.0.0009
 * @since 0.0.0009
 */
public class CraftingI implements IItem {

	private String	name;
	private String	desc;
	private String	id;
	private int		val;

	private String msg;

	public CraftingI(String name, String desc, String id, int val) {
		this.name = name;
		this.desc = desc;
		this.id = id;
		this.val = val;
	}

	@Override
	public int getSellValue() {
		return val;
	}

	@Override
	public String getLastFailMessage() {
		return msg;
	}

	@Override
	public String getDescription() {
		return desc;
	}
	
	public String toString() {
		return this.getShortDisplay();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public State use(Player player) {
		msg = String.format(Factory.__("msg.game.cannotuseitem"), name); //$NON-NLS-1$
		return State.notAllowed;
	}

	@Override
	public State equip(Player player) {
		msg = String.format(Factory.__("msg.game.cannotequipitem"), name); //$NON-NLS-1$
		return State.notAllowed;
	}

	@Override
	public String getID() {
		return id;
	}

}
