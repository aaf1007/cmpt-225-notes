package assignment2;
import java.util.*;

/**
 *
 * This is a generic class representing a list of objects.
 * The operations on the list are as follows:
 * - adding and removing elements from the left and from the right.
 * - reversing the list
 * - getting the size of the list
 * 
 * **All operations must run in O(1) time.**
 */
public class MyLinkedList<T> {

	public class MyLinkedListNode {
		T data;
		MyLinkedListNode next;
		MyLinkedListNode prev;
		// one param
		MyLinkedListNode(T data) {
			this.data = data;
			this.next = null;
			this.prev = null;
		}
		// two param
		MyLinkedListNode(T data, MyLinkedListNode next) {
			this.data = data;
			this.next = next;
			this.prev = null;
		}
	}

	private MyLinkedListNode head;
	private MyLinkedListNode tail;
	private int length;
	private int direction;

	/**
	 * The constructor creates an empty list
	 */
	public MyLinkedList() {
		head = null;
		tail = null;
		length = 0;
		direction = 1;
	}

	/**
	 * Adds the new item to the left of the list. 
	 */
	public void addLeft(T item) {
		if (length == 0) {
			MyLinkedListNode newNode = new MyLinkedListNode(item);
			this.head = newNode;
			this.tail = newNode;
		} else {
			if (direction == 1) {
				MyLinkedListNode newNode = new MyLinkedListNode(item, this.head);
				this.head.prev = newNode;
				this.head = newNode;
			} else {
				MyLinkedListNode newNode = new MyLinkedListNode(item, null);
				newNode.prev = this.tail;
				this.tail.next = newNode;
				this.tail = newNode;
			}
		}
		length++;
	}

	/**
	 * Adds the new item to the right of the list. 
	 */
	public void addRight(T item) {
		if (length == 0) {
			MyLinkedListNode newNode = new MyLinkedListNode(item);
			this.head = newNode;
			this.tail = newNode;
		} else {
			if (direction == 1) {
				MyLinkedListNode newNode = new MyLinkedListNode(item, null);
				newNode.prev = this.tail;
				this.tail.next = newNode;
				this.tail = newNode;
			} else {
				MyLinkedListNode newNode = new MyLinkedListNode(item, this.head);
				this.head.prev = newNode;
				this.head = newNode;
			}
		}
		length++;
	}

	/**
	 * Removes the leftmost item from the list and returns it.
	 * If the list is empty, throws NoSuchElementException.
	 */
	public T removeLeft() {
		// when nothing to remove -> length == 0
		if (this.length == 0) {
			throw new NoSuchElementException("List is empty");
		}
		T temp;
		if (this.length == 1) {
			temp = this.head.data;
			this.head = null;
			this.tail = null;
			length--;
			return temp;
		}
		if (direction == 1) {
			temp = this.head.data;
			this.head = this.head.next;
			this.head.prev = null;
		} else {
			temp = this.tail.data;
			this.tail = this.tail.prev;
			this.tail.next = null;
		}
		length--;
		return temp;
	}

	/**
	 * Removes the rightmost item from the list and returns it.
	 * If the list is empty, throws NoSuchElementException.
	 */
	public T removeRight() {
		if (this.length == 0) {
			throw new NoSuchElementException("List is empty");
		}
		T temp;
		if (this.length == 1) {
			temp = this.tail.data;
			this.head = null;
			this.tail = null;
			length--;
			return temp;
		}
		if (direction == 1) {
			temp = this.tail.data;
			this.tail = this.tail.prev;
			this.tail.next = null;
		} else {
			temp = this.head.data;
			this.head = this.head.next;
			this.head.prev = null;
		}
		length--;
		return temp;
	}


	/**
	 * Reverses the list
	 */
	public void reverse() {
		// first change direction
		direction = -direction;
	}

	/**
	 * Returns the size of the list.
	 */
	public int size() {
		return length;
	}

	/**
	 * Returns true if list is empty, and returns false otherwise.
	 */
	public boolean isEmpty() {
		return length==0;
	}

}
