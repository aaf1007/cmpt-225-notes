package basicdatastructures.queue;

/**
 * Implementing a Stack using LinkedList - this is a doubly linked list
 *   
 * @author Igor
 *
 */

import java.util.LinkedList;

public class QueueLinkedListBased<T> implements Queue<T> {

	LinkedList<T> list;
	
	public QueueLinkedListBased() {
		list = new LinkedList<T>();
	}
	
	public void enqueue(T item) {
		list.addLast(item);
	}
	
	public T dequeue() {
		return list.removeFirst();
	}
	
	public boolean isEmpty() {
		return list.size()==0;
	}
	
}


	