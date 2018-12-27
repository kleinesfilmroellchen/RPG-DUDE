package rpg.helpers;

import java.lang.reflect.InvocationTargetException;
import rpg.core.GameConst;
import rpg.core.interfaces.IItem;
import rpg.core.objects.*;
import rpg.core.objects.items.*;
import rpg.local.TextMessages;

/**
 * Creator of various objects.
 * @author kleinesfilmroellchen
 * @version 0.0.0009
 * @since 0.0.0009
 */
public final class Factory {
	public static final Armour leatherArmour() {
		return new Armour(GameConst.LEATHER_DEF, GameConst.LEATHER_DEF * GameConst.ARMOUR_WEIGHT, Factory.__("msg.items.leatherarmour"), //$NON-NLS-1$
				"leatherArmour"); //$NON-NLS-1$
	}

	public static final Armour bronzeArmour() {
		return new Armour(GameConst.BRONZE_DEF, GameConst.BRONZE_DEF * GameConst.ARMOUR_WEIGHT, Factory.__("msg.items.bronzearmour"), //$NON-NLS-1$
				"bronzeArmour"); //$NON-NLS-1$
	}

	public static final Armour ironArmour() {
		return new Armour(GameConst.IRON_DEF, GameConst.IRON_DEF * GameConst.ARMOUR_WEIGHT, Factory.__("msg.items.ironarmour"), "ironArmour"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public static final Armour silverArmour() {
		return new Armour(GameConst.SILVER_DEF, GameConst.SILVER_DEF * GameConst.ARMOUR_WEIGHT, Factory.__("msg.items.silverarmour"), //$NON-NLS-1$
				"silverArmour"); //$NON-NLS-1$
	}

	public static final Armour magicArmour() {
		return new Armour(GameConst.MAGIC_DEF, GameConst.MAGIC_DEF * GameConst.ARMOUR_WEIGHT, Factory.__("msg.items.magicarmour"), //$NON-NLS-1$
				"magicArmour"); //$NON-NLS-1$
	}

	public static final IItem leather() {
		return new CraftingI(Factory.__("msg.items.leather"), //$NON-NLS-1$
				Factory.__("msg.items.leatherdesc"), //$NON-NLS-1$
				"leather", 4); //$NON-NLS-1$
	}

	public static final IItem wood() {
		return new CraftingI(Factory.__("msg.items.wood"), //$NON-NLS-1$
				Factory.__("msg.items.wooddesc"), //$NON-NLS-1$
				"wood", 5); //$NON-NLS-1$
	}

	public static final IItem stick() {
		return new CraftingI(Factory.__("msg.items.stick"), //$NON-NLS-1$
				Factory.__("msg.items.stickdesc"), //$NON-NLS-1$
				"stick", 1); //$NON-NLS-1$
	}

	public static final IItem iron() {
		return new CraftingI(Factory.__("msg.items.iron"), //$NON-NLS-1$
				Factory.__("msg.items.irondesc"), //$NON-NLS-1$
				"iron", 10); //$NON-NLS-1$
	}

	public static final IItem bronze() {
		return new CraftingI(Factory.__("msg.items.bronze"), //$NON-NLS-1$
				Factory.__("msg.items.bronzedesc"), //$NON-NLS-1$
				"bronze", 8); //$NON-NLS-1$
	}

	public static final IItem silver() {
		return new CraftingI(Factory.__("msg.items.silver"), //$NON-NLS-1$
				Factory.__("msg.items.silverdesc"), //$NON-NLS-1$
				"silver", 20); //$NON-NLS-1$
	}

	public static final IItem magicElement() {
		return new CraftingI(Factory.__("msg.items.magicelmt"), //$NON-NLS-1$
				Factory.__("msg.items.magicelmtdesc"), //$NON-NLS-1$
				"magicElement", 50); //$NON-NLS-1$
	}

	public static final IItem byId(String id) {
		try {
			// finds a method with the id's name, calls and returns it
			return (IItem) Factory.class.getMethod(id).invoke(null);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(String.format(Factory.__("msg.error.factory"), id), e); 
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(String.format(Factory.__("msg.error.unknownid"), id), e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String __(String key) {
		return TextMessages._t(key);
	}
}
