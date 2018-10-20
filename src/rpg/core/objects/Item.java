package rpg.core.objects;

import rpg.helpers.*;

/**
 * A base class for all items. All items will inherit from this.
 * @author kleinesfilmröllchen
 * @since 0.0.0004
 * @version 0.0.0009
 * @see rpg.core.objects.items Item subclasses
 */
public interface Item {

	/** Returns the basic sell value the item has. */
	public int getSellValue();

	/**
	 * Returns the last fail message. Methods in this class override the fail
	 * message if an error occurs during their execution.
	 */
	public String getLastFailMessage();

	/** Returns a description for this item. */
	public String getDescription();

	/** Returns the item's name. */
	public String getName();

	/** Returns the item's unique id. */
	public String getID();

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
}
