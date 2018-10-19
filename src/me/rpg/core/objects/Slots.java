package me.rpg.core.objects;

import java.util.*;

/**
 * Slots are used to hold items in designated spots. Slots support limitations
 * on item types
 * @author kleinesfilmroellchen
 * @param <T> limiting item type, must be subclass of Item
 * @see me.rpg.core.objects.Item
 */
@SuppressWarnings("rawtypes")
public class Slots<T extends Item> implements Collection {

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
	public boolean add(Object e) {
		if (this.isFull()) {
			// No replacement found
			throw new RuntimeException("Slots are full, no element can be added without replacement");
		}

		for (Item elmt : this.elements) {
			if (elmt == null) {
				elmt = (Item) e;
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
	public Item addReplace(Object o, int idx) {
		// Replacement required
		if (this.isFull())
			return this.elements.set(idx, (Item) o);
		// Insert at first free position
		else
			for (Item elmt : this.elements) {
				if (elmt == null) elmt = (Item) o;
			}

		return null;
	}

	/**
	 * Empties the Slots as by invocating the {@code clear} method and returns all
	 * elements as an ArrayList.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Item> empty() {
		ArrayList formerElmts = new ArrayList<Item>(this.elements);
		this.elements.removeAll(elements);
		return formerElmts;
	}

	@Override
	public boolean remove(Object o) {
		return elements.remove(o);
	}

	@Override
	@SuppressWarnings("unused")
	public void clear() {
		this.elements.clear();
	}

	// Necessary, but unused
	public boolean containsAll(Collection c) {
		return false;
	}

	public boolean addAll(Collection c) {
		return false;
	}

	public boolean removeAll(Collection c) {
		return false;
	}

	public boolean retainAll(Collection c) {
		return false;
	}

	// Returners
	public Iterator iterator() {
		return this.elements.iterator();
	}

	public Object[] toArray() {
		return this.elements.toArray();
	}

	public Object[] toArray(Object[] a) {
		return (Object[]) this.toArray();
	}
}
