package basicdatastructures.stack;

/**
 * Implementing a stack using ArrayList
 * It's basically an adapter from LinkedList to Stack
 * 
 * @author Igor
 *
 */

import java.util.ArrayList;

public class StackArrayBased<T> implements Stack<T> {

	private ArrayList<T> list;
	
	public StackArrayBased() {
		list = new ArrayList<T>();
	}
	
	// sometimes inefficient
	public void push(T item) {
		list.add(item);
	}

	public T pop() {
		return list.remove(list.size()-1);
	}
	
	public boolean isEmpty() {
		return list.size() == 0;
	}
	
}


	