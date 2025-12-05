package basicdatastructures.stack;

/**
 * Implementing a Stack using LinkedList
 * All operations run in O(1) time
 *   
 * @author Igor
 *
 */

import java.util.LinkedList;

public class StackLinkedListBased<T> implements Stack<T> {

	private LinkedList<T> list;
	
	public StackLinkedListBased() {
		list = new LinkedList<T>();
	}

	public void push(T item) {
		list.addFirst(item);
	}
	
	public T pop() {
		return list.removeFirst();
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
}


	