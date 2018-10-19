package utils;

import java.util.*;

//<>|

/**
 * Max heap that contains integers.<br>
 * This collection always contains the elements in order; such that the largest contained value is at the very top of the "heap"
 * and retrieved first by the caller. Adding an object to this heap will always
 */
public class IntMaxHeap implements Iterable<Integer> {
	
	private static final int DEFAULT_CAPACITY = 10;
	
	private List<Integer> elements;
	
	public IntMaxHeap(int capacity) {
		this.elements = new ArrayList<Integer>(capacity);
	}
	
	public IntMaxHeap() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * Add a value to the heap.
	 */
	public boolean add(int n) {
		
		//in case of empty heap
		if (elements.isEmpty()) {
			elements.add(n);
			return true;
		}
		
		for (int i = 0; i < elements.size(); ++i) {
			//first position to be smaller: add there
			if (elements.get(i).intValue() < n) {
				elements.add(i, n);
				return true;
			}
			
			//last place: not bigger than any elements, so we add it at the end
			if (i == elements.size()-1) {
				elements.add(n);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes the largest element from this heap and returns it. If there are no elements, zero (0) is returned.
	 */
	public Integer retrieve() {
		if(this.elements.isEmpty()) return 0;
		int max = elements.get(0);
		elements.remove(0);
		return max;
	}
	
	/**
	 * Same as {@code retrieve()}, but does not remove the element returned.
	 * @return The largest element of this heap.
	 */
	public Integer peek() {
		if(this.elements.isEmpty()) return 0;
		return elements.get(0);
	}

	public void clear() {
		this.elements.clear();
	}

	public boolean isEmpty() {
		return this.elements.isEmpty();
	}

	/**
	 * Standard iterator that does not remove any elements from this heap.
	 */
	@Override
	public Iterator<Integer> iterator() {
		return this.elements.iterator();
	}

	public int size() {
		return this.elements.size();
	}

	public Object[] toArray() {
		return this.elements.toArray();
	}

	public Object[] toArray(Object[] a) {
		return this.elements.toArray(a);
	}
	
	@Override
	public String toString() {
		return "Integer Max Heap " + this.elements.toString();
	}

}
