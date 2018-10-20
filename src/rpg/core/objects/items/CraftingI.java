package rpg.core.objects.items;

import rpg.core.objects.Item;
import rpg.core.objects.Player;
import rpg.helpers.State;

/**
 * Blueprint for any item that is only for crafting and cannot be used.
 * @author kleinesfilmroellchen
 * @version 0.0.0009
 * @since 0.0.0009
 */
public class CraftingI implements Item {

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

	@Override
	public String getName() {
		return name;
	}

	@Override
	public State use(Player player) {
		msg = String.format("%s kann nicht verwendet werden!", name);
		return State.notAllowed;
	}

	@Override
	public State equip(Player player) {
		msg = String.format("%s kann nicht ausgerüstet werden!", name);
		return State.notAllowed;
	}

	@Override
	public String getID() {
		return id;
	}

}
