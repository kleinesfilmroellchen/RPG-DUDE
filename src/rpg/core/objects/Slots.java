package rpg.core.objects;

import java.util.*;

/**
 * Slots are used to hold items in designated spots. Slots support limitations
 * on item types
 * @author kleinesfilmroellchen
 * @param <T> limiting item type, must be subclass of Item
 * @see rpg.core.objects.Item
 */
public class Slots<T extends Item> implements Collection<Item> {

	private List<Item> elements;

	/**
	 * Creates slots with the specified elements.
	 */
	public <U extends Item> Slots(U[] elmts) {
		this.elements = Arrays.asList(elmts);
	}

	/**
	 * Creates slots with the specified collection content.
	 */
	public Slots(Collection<? extends Item> elmts) {
		this(elmts.toArray(new Item[1]));
	}

	/**
	 * Creates slots with the specified number of elements in it.
	 */
	public Slots(int slots) {
		this.elements = Arrays.asList(new Item[slots]);
	}

	@Override
	public int size() {
		return this.elements.size();
	}

	@Override
	public boolean isEmpty() {
		return this.size() == 0;
	}

	@Override
	public boolean contains(Object o) {
		return o == null ? false : this.elements.stream().anyMatch(elmt -> elmt.equals(o));
	}

	@Override
	public boolean add(Item e) {
		if (this.isFull()) {
			// No replacement found
			throw new RuntimeException("Slots are full, no element can be added without replacement");
		}

		for (Item elmt : this.elements) {
			if (elmt == null) {
				elmt = e;
				return true;
			} else if (elmt.equals(e)) {
				// already contains new element
				return false;
			}
		}
		// not allowed to happen normally
		return false;
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

		Item[] newElmtsArr = new Item[newSize];
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
		return this.elements.stream().anyMatch(elmt -> elmt == null);
	}

	/**
	 * Adds an element and, if necessary, replaces the element with the specified
	 * index.
	 * @param idx the index to be replaced at if necessary.
	 * @return the replaced element, if any had to be replaced. If there wasn't an
	 * element replaced, returns {@code null}.
	 */
	public Item addReplace(Item o, int idx) {
		// Replacement required
		if (this.isFull())
			return this.elements.set(idx, (Item) o);
		// Insert at first free position
		else
			for (Item elmt : this.elements) {
				if (elmt == null) elmt = o;
			}

		return null;
	}

	/**
	 * Empties the Slots as by invocating the {@code clear} method and returns all
	 * elements as an ArrayList.
	 */
	public ArrayList<Item> empty() {
		ArrayList<Item> formerElmts = new ArrayList<Item>(this.elements);
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
	public boolean hasAll(Collection<Item> items) {
		ArrayList<Item> toFind = new ArrayList<>(items);
		ArrayList<Item> lookup = new ArrayList<Item>(this);

		for (int i = 0; i < toFind.size(); ++i) {
			Item current = toFind.get(i);
			// find everything in this slot that is the same item (ID) as the currently
			// checked item
			Iterator<Item> matching = lookup.stream().filter(item -> item.getID().equals(current.getID())).iterator();
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
		return c.getClass().isAssignableFrom(this.getClass()) ? hasAll((Collection<Item>) c) : false;
	}

	@Override
	public boolean addAll(Collection c) {
		return false;
	}

	public boolean removeAll(Collection<?> c) {
		if (!c.getClass().isAssignableFrom(this.getClass())) return false;

		ArrayList<Item> toRemove = new ArrayList<Item>((Collection<Item>) c);

		for (int i = 0; i < toRemove.size(); ++i) {
			Item current = toRemove.get(i);
			// find everything in this slot that is the same item (ID) as the currently
			// checked item
			Iterator<Item> matching = this.stream().filter(item -> item.getID().equals(current.getID())).iterator();
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

	public boolean retainAll(Collection c) {
		return false;
	}

	// Returners
	public Iterator<Item> iterator() {
		return this.elements.iterator();
	}

	public Object[] toArray() {
		return this.elements.toArray();
	}

	public Item[] toArray(Object[] a) {
		return (Item[]) this.toArray();
	}
}
