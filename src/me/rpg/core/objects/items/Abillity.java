package me.rpg.core.objects.items;

import me.rpg.core.objects.Item;
import me.rpg.core.objects.Player;
import me.rpg.helpers.State;

/**
 * Will later be the class for all abillities
 * @author kleinesfilmröllchen
 * @version 0.0.0008
 * @since 0.0.0008
 * @see me.rpg.core.objects.Item Item interface
 *
 */
public class Abillity implements Item {
	
	private String lastFailMessage = "";
	
	private String description = "Fähigkeit ohne Beschreibung";
	
	private String name = "Fähigkeit";
	
	@Override
	public int getSellValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getLastFailMessage() {
		// TODO Auto-generated method stub
		return lastFailMessage;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public State use(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public State equip(Player player) {
		// TODO Auto-generated method stub
		return null;
	}

}
