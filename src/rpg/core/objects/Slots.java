package rpg.core.objects;

import java.util.*;
import java.util.stream.Collectors;
import rpg.core.interfaces.IItem;
import rpg.helpers.Factory;
import rpg.local.TextMessages;

/**
 * Slots are used to hold items in designated spots. Slots support limitations
 * on item types (implementor classes) and limit the number of holdable items.
 * @author kleinesfilmroellchen
 * @param <T> limiting item type, must be implementor of Item
 * @see rpg.core.interfaces.IItem
 */
@SuppressWarnings("rawtypes")
public class Slots<T extends IItem> implements Collection<IItem> {

	public static class ContainingException extends RuntimeException {
		/** @inheritdoc */
		private static final long serialVersionUID = 5952786505146662031L;

		public ContainingException(String msg) {
			super(msg);
		}
	}

	private List<IItem> elements;

	/**
	 * Creates slots with the specified elements.
	 */
	public <U extends IItem> Slots(U[] elmts) {
		this.elements = Arrays.asList(elmts);
	}

	/**
	 * Creates slots with the specified collection content.
	 */
	public Slots(Collection<? extends IItem> elmts) {
		this(elmts.toArray(new IItem[1]));
	}

	/**
	 * Creates slots with the specified number of elements in it.
	 */
	public Slots(int slots) {
		this.elements = Arrays.asList(new IItem[slots]);
	}

	@Override
	public int size() {
		return this.elements.size();
	}

	@Override
	public boolean isEmpty() {
		return this.size() <= 0;
	}

	@Override
	public boolean contains(Object o) {
		return o == null ? false : this.elements.stream().anyMatch(elmt -> elmt.equals(o));
	}

	@Override
	public boolean add(IItem e) {
		if (this.isFull()) {
			// No replacement found
			throw new RuntimeException(Factory.__("msg.error.slotsfull"));
		}

		for (int i = 0; i++ < this.elements.lastIndexOf(null);) {
			IItem elmt = this.elements.get(i);
			if (elmt == null) {
				this.elements.set(i, e);
				return true;
			} else if (elmt.equals(e)) {
				// already contains new element
				throw new ContainingException(String.format(Factory.__("msg.error.contains"), elmt.toString()));
			}
		}
		// not allowed to happen normally
		return false;
	}

	public IItem itemAt(int index) {
		return this.elements.get(index);
	}

	/**
	 * Returns any element in the slot. More precisely, returns the first non-null
	 * element of the iterator. If there is no non-null element, returns an empty
	 * Optional. <br>
	 * <br>
	 * This method is recommended for retrieving an element of a slots with size 1.
	 */
	public Optional<IItem> any() {
		IItem current = null;
		Iterator<IItem> it = this.iterator();
		while (it.hasNext() && current == null)
			current = it.next();
		// will either wrap null or a valid element
		return Optional.ofNullable(current);
	}

	/**
	 * Changes the slots to have a new size. It is not allowed to resize the slots
	 * if one or more contained items wouldn't fit into the new Slots.
	 * @return true if the resizement was sucessful.
	 */
	public boolean resize(int newSize) {
		if (newSize < this.size()) return false;
		// nothing to do
		if (newSize == this.size()) return true;

		// greater size
		IItem[] newElmtsArr = new IItem[newSize];
		for (int i = 0; i < this.elements.size(); ++i) {
			newElmtsArr[i] = this.elements.get(i);
		}
		this.elements = Arrays.asList(newElmtsArr);

		return true;
	}

	/**
	 * Finds out whether the slots are completely full.
	 */
	public boolean isFull() {
		return this.elements.stream().noneMatch(elmt -> elmt == null);
	}

	/**
	 * Adds an element and, if necessary, replaces the element with the specified
	 * index.
	 * @param idx the index to be replaced at if necessary.
	 * @return the replaced element, if any had to be replaced. If there wasn't an
	 * element replaced, returns {@code null}.
	 */
	public IItem addReplace(IItem o, int idx) {
		// Replacement required
		if (this.isFull())
			return this.elements.set(idx, (IItem) o);
		// Insert at first free position
		else
			for (IItem elmt : this.elements) {
				if (elmt == null) elmt = o;
			}

		return null;
	}

	/**
	 * Empties the Slots as by invocating the {@code clear} method and returns all
	 * elements as an ArrayList.
	 */
	public ArrayList<IItem> empty() {
		ArrayList<IItem> formerElmts = new ArrayList<IItem>(this.elements);
		this.elements.removeAll(elements);
		return formerElmts;
	}

	/**
	 * Checks whether all of the items of the collection are present in at least the
	 * amount as in the collection.<br>
	 * <br>
	 * e.g. if {@code items} contains 3 "wood" items, only returns true if this also
	 * has at least three "wood" items.<br>
	 * <br>
	 * Checking is always done against IDs.
	 * @param items
	 * @return
	 */
	public boolean hasAll(Collection<IItem> items) {
		List<IItem> toFind = new ArrayList<>(items);
		List<IItem> lookup = new LinkedList<IItem>(this);

		for (int i = 0; i < toFind.size(); ++i) {
			IItem current = toFind.get(i);
			// find everything in this slot that is the same item (ID) as the currently
			// checked item
			Iterator<IItem> matching = lookup.stream().filter(item -> item.getID().equals(current.getID())).iterator();
			if (!matching.hasNext()) {
				// there is no item with that id found
				return false;
			} else {
				// "use up" one item from the slot
				lookup.remove(matching.next());
			}
		}
		// every item of the items list was found
		return true;
	}

	@Override
	public boolean remove(Object o) {
		return elements.remove(o);
	}

	@Override
	public void clear() {
		this.elements.clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsAll(Collection<?> c) {
		// checks if both collections have type argument T (extends Item)
		return c.getClass().isAssignableFrom(this.getClass()) ? hasAll((Collection<IItem>) c) : false;
	}

	@Override
	public boolean addAll(Collection c) {
		return false;
	}

	public boolean removeAll(Collection<?> c) {
		if (!c.getClass().isAssignableFrom(this.getClass())) return false;

		ArrayList<IItem> toRemove = new ArrayList<IItem>((Collection<IItem>) c);

		for (int i = 0; i < toRemove.size(); ++i) {
			IItem current = toRemove.get(i);
			// find everything in this slot that is the same item (ID) as the currently
			// checked item
			Iterator<IItem> matching = this.stream().filter(item -> item.getID().equals(current.getID())).iterator();
			if (!matching.hasNext()) {
				// there is no item with that id found
				return false;
			} else {
				// "use up" one item from the slot
				this.remove(matching.next());
			}
		}
		// every item of the items list was found
		return true;
	}

	/** Not allowed on this collection. */
	public boolean retainAll(Collection c) {
		return false;
	}

	// Returners
	public Iterator<IItem> iterator() {
		return this.elements.iterator();
	}

	public Object[] toArray() {
		return this.elements.toArray();
	}

	@SuppressWarnings("unchecked")
	public IItem[] toArray(Object[] a) {
		return (IItem[]) this.toArray();
	}

	public String toString() {
		return String.format(TextMessages._t("msg.tostring.slots"), toDisplay(), super.hashCode());
	}

	public String toDisplay() {
		return String.join(", ", this.elements.stream()
				.map(i -> i == null ? IItem.dummy().getShortDisplay() : i.getShortDisplay())
				.collect(Collectors.toList()));
	}
}
