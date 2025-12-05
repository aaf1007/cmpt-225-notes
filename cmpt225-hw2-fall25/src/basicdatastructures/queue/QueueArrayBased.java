package basicdatastructures.queue;

/**
 * Implementing a Stack using LinkedList - this is a doubly linked list
 *   
 * @author Igor
 *
 */

import java.util.ArrayList;

public class QueueArrayBased<T> implements Queue<T> {

	ArrayList<T> list;
	
	public QueueArrayBased() {
		list = new ArrayList<T>();
	}

	/**
	 * when need to resize the running time is O(size of queue)
	 */
	public void enqueue(T item) {
		list.add(item); // adds to the end of the array
	}
	
	/**
	 * running time is  O(size of queue)
	 */
	public T dequeue() {
		return list.remove(0); // remove from position 0
	}
	
	public boolean isEmpty() {
		return list.size()==0;
	}
	
}


	