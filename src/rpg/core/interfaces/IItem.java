package rpg.core.interfaces;

import rpg.core.objects.Entity;
import rpg.core.objects.Player;
import rpg.helpers.*;
import rpg.local.TextMessages;

/**
 * A base class for all items. All items will inherit from this. <b>Note:</b>
 * The {@code equals()} method must <b>NOT</b> be overridden, because the
 * "strict" equality of the default {@code Object} implementation is required
 * for the slots collections.
 * @author kleinesfilmröllchen
 * @since 0.0.0004
 * @version 0.0.0009
 * @see rpg.core.objects.items Item subclasses
 */
public interface IItem {
	/**
	 * A text message wrapper for all item implementors
	 * @param key message key
	 * @return the corresponding message
	 */
	static String __(String key) {
		return Entity.__(key);
	}

	/** Returns the basic sell value the item has. */
	public int getSellValue();

	/**
	 * Returns the last fail message. Methods in this class override the fail
	 * message if an error occurs during their execution.
	 */
	public String getLastFailMessage();

	/** Returns a long description for this item. */
	public String getDescription();

	/** Returns the item's name. */
	public String getName();

	/** Returns the item's unique id. */
	public String getID();

	/**
	 * Returns the short display string for this item. Default method only returns
	 * the name.
	 */
	public default String getShortDisplay() {
		return this.getName();
	}

	/**
	 * Method called to use the item.<br>
	 * The general contract of this method is that the passed <code>Player</code>
	 * object can be modified by any means, but the modifications must clearly be
	 * stated in the item's class description.<br>
	 * <br>
	 * The method returns any of the available states while following these
	 * contracts:
	 * <ul>
	 * <li>{@code failed} is returned if the method creates some sort of error or is
	 * unable to execute a command. In this case, an additional message is put into
	 * the item's {@code lastFailMessage} field.</li>
	 * <li>{@code finished} is returned if the method was able to do its task as
	 * expected.</li>
	 * <li>{@code notAllowed} is returned if the item usage is not allowed by any
	 * means. In this case, an additional message is put into the item's
	 * {@code lastFailMessage} field.</li>
	 * </ul>
	 * @see rpg.helpers.State State
	 * @param player A player to modify through using the item.
	 * @return The final state of execution.
	 */
	public abstract State use(Player player);

	/**
	 * Method called to equip the item, i.e. apply it to a certain item slot the
	 * player has, such as armor etc.<br>
	 * The general contract of this method is that the passed <code>Player</code>
	 * object can be modified by any means, but the modifications must clearly be
	 * stated in the item's class description.<br>
	 * <br>
	 * The method returns any of the available states while following these
	 * contracts:
	 * <ul>
	 * <li>{@code failed} is returned if the method creates some sort of error or is
	 * unable to execute a command. In this case, an additional message is put into
	 * the item's {@code lastFailMessage} field.</li>
	 * <li>{@code finished} is returned if the method was able to do its task as
	 * expected.</li>
	 * <li>{@code notAllowed} is returned if the item equipment is not allowed by
	 * any means. In this case, an additional message is put into the item's
	 * {@code lastFailMessage} field.</li>
	 * </ul>
	 * @see rpg.helpers.State State
	 * @param player A player to modify through equipping the item.
	 * @return The final state of execution.
	 */
	public abstract State equip(Player player);

	/**
	 * Returns a dummy item which has no use and should only be used for display
	 * purposes: The player should never aquire such an item.
	 */
	public static IItem dummy() {
		return new IItem() {

			public int getSellValue() {
				return 0;
			}

			public String getLastFailMessage() {
				return String.format(__("msg.error.notusable"), getName());
			}

			public String getDescription() {
				return __("msg.items.nodescription");
			}

			public String getName() {
				return __("msg.items.dummy");
			}
			public String getShortDisplay() {return __("msg.nothing");}

			public String getID() {
				return "dummy";
			}

			public State use(Player player) {
				return State.notAllowed;
			}

			public State equip(Player player) {
				return State.notAllowed;
			}

		};
	}
}
