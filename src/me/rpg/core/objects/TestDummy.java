package me.rpg.core.objects;

import java.io.IOException;
import java.util.Scanner;

import me.rpg.core.RPG_MAIN;

/**
 * A test dummy that is used to test certain functionality.
 * Will be removed sooner or later.
 * @author DJH
 * @author kleinesfilmröllchen
 */
public class TestDummy extends Entity {
	
	/**
	 * Constructor that asks the user to configure the dummy.
	 * @param coords The coordinates the dummy will be spawned.
	 * @param callerOut The output stream that is used to ask the user.
	 * @param scan The input scanner to be used for input from the user.
	 * @throws IOException 
	 */
	public TestDummy(int[] coords, Scanner scan) throws IOException {
		super();
		
		reward = 25;
		
		while (true) {
			RPG_MAIN.println("\nWie viel Leben darf der Dummy haben?");
			health = this.maxHealth = RPG_MAIN.getInt();
			if (health <= 0) {
				RPG_MAIN.println("\nDer Dummy darf nicht 0 oder weniger Leben haben!");
			} else break;
		}
		
		while (true) {
			RPG_MAIN.println("\nMit welchem Schaden darf der Dummy angreifen?");
			atk = RPG_MAIN.getInt();
			if (atk < 0) {
				RPG_MAIN.println("Der Dummy darf nicht weniger als 0 Schaden haben!");
			} else break;
		}
		
		while (true) {
			RPG_MAIN.println("\nWie viel Verteidigung darf der Dummy haben?");
			def = RPG_MAIN.getInt();
			if (def < 0) {
				RPG_MAIN.println("Der Dummy darf nicht weniger als 0 Verteidigung haben!");
			} else break;
		}
		
		while (true) {
			RPG_MAIN.println("\nWelche Mobilität darf der Dummy haben?");
			mob = RPG_MAIN.getFloat();
			if (atk < 0) {
				RPG_MAIN.println("Der Dummy darf nicht weniger als 0 Mobilität haben!");
			} else break;
		}
		
		this.setCoords(coords[0], coords[1], coords[2]);
	}
	
	@Override
	public String name() {
		return "Dummy";
	}
}
