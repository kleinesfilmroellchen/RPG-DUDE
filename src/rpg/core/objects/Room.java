package rpg.core.objects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import rpg.core.interfaces.IItem;
import rpg.helpers.Factory;
import rpg.local.TextMessages;

/**
 * Stores information about a certain room.
 * @author kleinesfilmröllchen
 * @version 0.0.0006
 * @since 0.0.0006
 */
public class Room extends Coordinates {

	/** Whether this room has an exit/door to the south */
	private boolean	exitSouth;
	/** Whether this room has an exit/door to the north */
	private boolean	exitNorth;
	/** Whether this room has an exit/door to the east */
	private boolean	exitEast;
	/** Whether this room has an exit/door to the west */
	private boolean	exitWest;
	/** Whether this room has a ladder upwards, so an exit to upper floors */
	private boolean	ladderUp;
	/** Whether this room has a ladder downwards, so an exit to lower floors */
	private boolean	ladderDown;

	/** The name of this room. */
	public String	roomName				= Factory.__("msg.game.unnamedroom"); //$NON-NLS-1$
	/** The description for this room. */
	public String	roomDescription	= Factory.__("msg.game.roomdesc"); //$NON-NLS-1$

	/** List of all items in this room. */
	public List<IItem> items = new LinkedList<>();

	/**
	 * Constructor which allows exits to all sides, but not up and down (open space
	 * default)
	 */
	public Room(int x, int y, int z) {
		this(x, y, z, true, true, true, true, false, false);
	}

	public Room(int x, int y, int z, boolean exit_n, boolean exit_s, boolean exit_e, boolean exit_w, boolean ladderU,
			boolean ladderD) {
		super(x, y, z);

		this.exitEast = exit_e;
		this.exitNorth = exit_n;
		this.exitSouth = exit_s;
		this.exitWest = exit_w;
		this.ladderDown = ladderD;
		this.ladderUp = ladderU;
	}

	/**
	 * Checks whether a walk/move from this room is allowed or not, based on the
	 * exits a room has.
	 * @param xdir The movement in east +1 /west -1 direction in relation to this
	 * room
	 * @param ydir The movement in north +1/south -1 direction in relation to this
	 * room
	 * @return whether the walk is allowed, or false if both directions are
	 * different from 0.
	 */
	public boolean walkIsAllowed(int xdir, int ydir) {
		// Moving diagonally is never allowed, and staying is also "forbidden"
		if ((xdir != 0 && ydir != 0) || (xdir == 0 && ydir == 0)) return false;

		if (xdir == -1 && exitWest) return true;
		if (xdir == 1 && exitEast) return true;
		if (ydir == -1 && exitNorth) return true;
		if (ydir == 1 && exitSouth) return true;

		return false;
	}

	/**
	 * Checks whether two Rooms are fully connected. This means that
	 * <ul>
	 * <li>The rooms are direct neighbors</li>
	 * <li>The player always has the possibility to go from one room to another AND
	 * back (no one-way doors)</li>
	 * </ul>
	 * @param a The first room to check
	 * @param b The second room to check
	 * @return Whether the given rooms are fully connected and neighboring.
	 * @see rpg.core.objects.Room#fullyConnected(Room) Instance method
	 */
	public static boolean fullyConnected(Room a, Room b) {
		// Precondition for connection
		if (a.neighboring(b)) {
			// Directions in which neighbor could lie
			int xdir = a.neighborDirX(b), ydir = a.neighborDirY(b), zdir = a.neighborDirZ(b);

			// All possibilities to connect two rooms
			return (xdir == -1 && a.exitEast && b.exitWest) || (xdir == 1 && a.exitWest && b.exitEast)
					|| (ydir == -1 && a.exitNorth && b.exitSouth) || (ydir == 1 && a.exitSouth && b.exitNorth)
					|| (zdir == -1 && a.ladderDown && b.ladderUp) || (zdir == 1 && a.ladderUp && b.ladderDown);
		}
		return false;
	}

	/**
	 * Checks the connection from itself to another room. Identical to
	 * {@code Room.fullyConnected(this, other)}.
	 * @param other The second, other room to check against.
	 * @see rpg.core.objects.Room#fullyConnected(Room, Room) Static method
	 */
	public boolean fullyConnected(Room other) {
		return Room.fullyConnected(this, other);
	}

	/**
	 * Sets an exit to a given state of openness. Does nothing if more than one of
	 * the arguments are different from 0.
	 * @param x East (1) or West (-1) exit.
	 * @param y North (1) or South (-1) exit.
	 * @param open Whether the specified exit should be opened (true) or not
	 * (false).
	 */
	public void setExit(int x, int y, boolean open) {
		// Invalid exit directions
		if ((x != 0 && y != 0) || (x == 0 && y == 0)) return;

		if (x == 1)
			exitWest = open;
		else if (x == -1)
			exitEast = open;
		else if (y == 1)
			exitNorth = open;
		else if (y == -1) exitSouth = open;
	}

	/**
	 * Checks whether a ladder downwards or upwards exists.
	 * @param z Whether to check upward ladder (1) or downward ladder (-1).
	 * @return The state of the ladder, or {@code false} if z = 0.
	 */
	public boolean getLadder(int z) {
		return z == -1 ? this.ladderDown : (z == 1 ? this.ladderUp : false);
	}

	public String toString() {
		return this.roomName + ": " + super.toString(); // $NON-NLS-1$ //$NON-NLS-1$
	}
}
