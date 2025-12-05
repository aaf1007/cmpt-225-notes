package basicdatastructures.queue;

public interface Queue<T> {
	
	/**
	 * adds an element to the tail of the queue 
	 * @param item
	 */
	public void enqueue(T item);
	
	/**
	 * removes an element from the head of the queue 
	 * @param item
	 */
	public T dequeue();
	
	public boolean isEmpty();

}


	