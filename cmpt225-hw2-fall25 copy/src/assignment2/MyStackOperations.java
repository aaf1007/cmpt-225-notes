package assignment2;

import basicdatastructures.stack.*;
import java.util.NoSuchElementException;

public class MyStackOperations {
	/**
	 * Returns the number of elements in s.
	 */
	public static <T> int size(Stack<T> s) {
		// TODO implement me
		int size = 0;
		if (s.isEmpty())
			return size;
		
		Stack<T> temp = new StackLinkedListBased<>();
		while (!s.isEmpty()) {
			temp.push(s.pop());
			size++;
		}
		while (!temp.isEmpty())
			s.push(temp.pop());
		
		return size;
	}

	/**
	 * Removes the bottom element from the stack, and returns it.
     * The remaining elements are kept in the same order.
     * If s is empty, the method throws NoSuchElementException.
	 */
    public static <T> T removeBottom(Stack<T> s) {
		// TODO implement me
		if (s.isEmpty())
			throw new NoSuchElementException("Stack is empty");

		T bottom;
		Stack<T> temp = new StackLinkedListBased<>();

		for (int i = 0; i < size(s); i++)
			temp.push(s.pop());

		bottom = s.pop();
		while (!temp.isEmpty())
			s.push(temp.pop());
			
		return bottom;
	}

	/**
	 * Reverses the order of the elements in s.
	 */
	public static <T> void reverse(Stack<T> s) {
		// TODO implement me
		StackLinkedListBased<T> temp1 = new StackLinkedListBased<>();
		StackLinkedListBased<T> temp2 = new StackLinkedListBased<>();
		
		while (!s.isEmpty())
			temp1.push(s.pop());

		while (!temp1.isEmpty())
			temp2.push(temp1.pop());

		while (!temp2.isEmpty())
			s.push(temp2.pop());
	}

	/**
	 * Checks if the two stacks have the same items in the same order. The items in
	 * the queues are compared using == operator.
	 */
	public static <T> boolean areEqual(Stack<T> s1, Stack<T> s2) {
		// TODO implement me
		StackLinkedListBased<T> temp1 = new StackLinkedListBased<>();
		StackLinkedListBased<T> temp2 = new StackLinkedListBased<>();

		boolean result = true;
		if (size(s1) != size(s2))
			return !result;
		
		if (size(s1) == 0 && size(s2) == 0)
			return result;

		while (!s1.isEmpty()) {
			T t1 = s1.pop(), t2 = s2.pop();
			temp1.push(t1);
			temp2.push(t2);
			if (t1 != t2)
				result = !result;
		}
		while (!temp1.isEmpty()) {
			s1.push(temp1.pop());
			s2.push(temp2.pop());
		}

		return result;
	}
}
