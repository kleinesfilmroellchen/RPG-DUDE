package rpg.core.objects.items;

import rpg.core.interfaces.IItem;
import rpg.core.objects.Player;
import rpg.helpers.State;

/**
 * Will later be the class for all abillities.
 * @author kleinesfilmröllchen
 * @version 0.0.0009
 * @since 0.0.0008
 * @see rpg.core.interfaces.IItem Item interface
 */
public class Abillity implements IItem {

	private String lastFailMessage = "";

	private String description = "Fähigkeit ohne Beschreibung";

	private String name = "Fähigkeit";

	@Override
	public int getSellValue() {
		return 0;
	}

	@Override
	public String getLastFailMessage() {
		return lastFailMessage;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public State use(Player player) {
		return null;
	}

	@Override
	public State equip(Player player) {
		return null;
	}

	@Override
	public String getID() {
		return "abillity";
	}

}
