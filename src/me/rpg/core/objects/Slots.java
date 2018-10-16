package me.rpg.core.objects;

import java.lang.reflect.Array;
import java.util.*;

@SuppressWarnings("rawtypes")
public class Slots<T extends Item> implements Collection {
	public int length() { return this.elements.length;}
	private Item[] elements;

	public Slots(T[] elmts) {
		this.elements = elmts.clone();
	}
	
	public Slots(Collection<? extends Item> elmts) {
		ArrayList<Item> generator = new ArrayList<Item>(elmts.size());
		elmts.forEach(e -> generator.add(e));
		this.elements = generator.toArray(this.elements);
	}
	
	public Slots(Class<? extends Item> c, int slots) {
		this.elements = new Item[slots];
	}

	@Override
	public int size() {
		return this.elements.length;
	}

	@Override
	public boolean isEmpty() {
		return this.elements.length == 0;
	}

	@Override
	public boolean contains(Object o) {
		if (o==null || this == null) return false;
		for (Item elmt : this.elements) {
			if(elmt.equals(o)) return true;
		}
		return false;
	}

	@Override
	public boolean add(Object e) {
		if (this.isFull()) {
			//No replacement found
			throw new RuntimeException("Slots are full, no element can be added without replacement");
		}
		
		for (Item elmt : this.elements) {
			if (elmt == null) {
				elmt = (Item) e;
				return true;
			} else if (elmt.equals(e)) {
				//already contains new element
				return false;
			}
		}
		//not allowed to happen normally
		return false;
	}
	
	/**
	 * Changes the slots to have a new size.
	 * It is not allowed to resize the slots if one or more contained items wouldn't fit into the new Slots.
	 * @return true if the resizement was sucessful.
	 */
	public boolean resize(int newSize) {
		ArrayList<Item> elmts = new ArrayList<Item>();
		Arrays.stream(this.elements).forEach(e -> {
			if(e != null) elmts.add(e);
		});
		
		//More elements than new array size
		if (elmts.size() > newSize) return false;
		
		//"else"
		this.elements = (Item[]) Array.newInstance(Item.class, newSize);
		for (int i = 0; i < elmts.size(); i++) {
			this.elements[i] = elmts.get(i);
		}
		return true;
	}
	
	/**
	 * Finds out whether the slots are completely full
	 * @return
	 */
	public boolean isFull() {
		for(Item elmt : this.elements) {
			if(elmt == null) {
				//at least one null -> not full
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Adds an element and, if necessary, replaces the element with the specified index.
	 * @return the replaced element, if any had to be replaced. If there wasn't an element replaced, returns {@code null}.
	 */
	public Item addReplace(Object o, int idx) {
		//Replacement required
		if (this.isFull()) {
			Item old = this.elements[idx];
			this.elements[idx] = (Item) o;
			return old;
		}
		//Insert at first free position
		else for (Item elmt : this.elements) {
			if (elmt == null) elmt = (Item) o;
		}
		
		return null;
	}
	
	/**
	 * Empties the Slots as by invocating the {@code clear} method and returns all elements as an ArrayList.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<? extends Item> empty() {
		ArrayList<T> returner = new ArrayList<T>(this.elements.length);
		returner.addAll((Collection<? extends T>) Arrays.asList(this.elements.clone()));
		this.clear();
		return returner;
	}

	@Override
	public boolean remove(Object o) {
		if (o == null) return false;
		for (Item elmt : this.elements) {
			if (elmt != null ? o.equals(elmt) : false) {
				elmt = null;
				return true;
			}
		}
		return false;
	}

	@Override
	@SuppressWarnings("unused")
	public void clear() {
		for (Item elmt : this.elements) {
			elmt = null;
		}
	}
	
	//Necessary, but unused
	public boolean containsAll(Collection c) { return false; }
	public boolean addAll(Collection c) { return false; }
	public boolean removeAll(Collection c) { return false; }
	public boolean retainAll(Collection c) { return false; }
	
	//Returners
	public Iterator iterator() { return Arrays.stream(this.elements).iterator(); }
	public Object[] toArray() { return this.elements.clone(); }
	public Object[] toArray(Object[] a) { return (Object[]) this.toArray(); }
}
