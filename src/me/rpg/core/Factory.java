package me.rpg.core;

import me.rpg.core.objects.*;
import me.rpg.core.objects.items.*;

/**
 * Creator of various objects.
 * @author kleinesfilmroellchen
 * @version 0.0.0009
 * @since 0.0.0009
 */
final class Factory {
	public static final Armour leatherArmour() {
		return new Armour(GameConst.LEATHER_DEF, GameConst.LEATHER_DEF * GameConst.ARMOUR_WEIGHT, "Lederr�stung");
	}

	public static final Armour bronzeArmour() {
		return new Armour(GameConst.BRONZE_DEF, GameConst.BRONZE_DEF * GameConst.ARMOUR_WEIGHT, "Bronzer�stung");
	}

	public static final Armour ironArmour() {
		return new Armour(GameConst.IRON_DEF, GameConst.IRON_DEF * GameConst.ARMOUR_WEIGHT, "Eisenr�stung");
	}

	public static final Armour silverArmour() {
		return new Armour(GameConst.SILVER_DEF, GameConst.SILVER_DEF * GameConst.ARMOUR_WEIGHT, "Silberr�stung");
	}

	public static final Armour magicArmour() {
		return new Armour(GameConst.MAGIC_DEF, GameConst.MAGIC_DEF * GameConst.ARMOUR_WEIGHT, "Magieelementr�stung");
	}
}
