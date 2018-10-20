package rpg.helpers;

import java.lang.reflect.InvocationTargetException;
import rpg.core.GameConst;
import rpg.core.objects.*;
import rpg.core.objects.items.*;

/**
 * Creator of various objects.
 * @author kleinesfilmroellchen
 * @version 0.0.0009
 * @since 0.0.0009
 */
public final class Factory {
	public static final Armour leatherArmour() {
		return new Armour(GameConst.LEATHER_DEF, GameConst.LEATHER_DEF * GameConst.ARMOUR_WEIGHT, "Lederr�stung",
				"leatherArmour");
	}

	public static final Armour bronzeArmour() {
		return new Armour(GameConst.BRONZE_DEF, GameConst.BRONZE_DEF * GameConst.ARMOUR_WEIGHT, "Bronzer�stung",
				"bronzeArmour");
	}

	public static final Armour ironArmour() {
		return new Armour(GameConst.IRON_DEF, GameConst.IRON_DEF * GameConst.ARMOUR_WEIGHT, "Eisenr�stung", "ironArmour");
	}

	public static final Armour silverArmour() {
		return new Armour(GameConst.SILVER_DEF, GameConst.SILVER_DEF * GameConst.ARMOUR_WEIGHT, "Silberr�stung",
				"silverArmour");
	}

	public static final Armour magicArmour() {
		return new Armour(GameConst.MAGIC_DEF, GameConst.MAGIC_DEF * GameConst.ARMOUR_WEIGHT, "Magieelementr�stung",
				"magicArmour");
	}

	public static final Item leather() {
		return new CraftingI("Leder",
				"Das Leder eines Tieres. Wird zur Herstellung von weiteren Items ben�tigt.",
				"leather", 4);
	}

	public static final Item wood() {
		return new CraftingI("Holz",
				"Ein Holzblock aus einem stabilen Baum. Holz ist f�r alles M�gliche zu gebrauchen.",
				"wood", 5);
	}

	public static final Item stick() {
		return new CraftingI("Stock",
				"Ein stabiler Holzstab f�r Werkzeuge und Waffen",
				"stick", 1);
	}

	public static final Item iron() {
		return new CraftingI("Eisen",
				"Hochwertiges raffiniertes Metall aus der Erde. Sehr stabil, f�r exzellente Werkzeuge.",
				"iron", 10);
	}

	public static final Item bronze() {
		return new CraftingI("Bronze",
				"Eine Legierung aus Kupfer und Zinn und das �lteste Werkzeugmetall. F�r den Anfang sehr gut.",
				"bronze", 8);
	}

	public static final Item silver() {
		return new CraftingI("Silber",
				"Dieses edle Metall kann zur Herstellung der sch�rfsten Waffen verwendet werden und ist nahezu unzerst�rbar.",
				"silver", 20);
	}

	public static final Item magicElement() {
		return new CraftingI("Magieelement",
				"Ein wundersames Material aus den Tiefen der Erde, st�rker als jedes andere Metall.",
				"magicElement", 50);
	}

	public static final Item byId(String id) {
		try {
			// finds a method with the id's name, calls and returns it
			return (Item) Factory.class.getMethod(id).invoke(null);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Exception in factory method " + id + "()", e);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Unknown id " + id, e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
