package binarytree;

import java.util.*;

public class BTIterator<T> implements Iterator<T>{

    private Stack<BTNode<T>> stack;

    public BTIterator(BTNode<T> root) {
        stack = new Stack<>();
        if (root != null)
            stack.push(root);
    }

    public T getCur()  {
        return stack.peek().getData();
    }

    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public T next() {
        if (!this.hasNext())
            throw new NoSuchElementException("There are no more elements in the Iterator");
        BTNode<T> cur = stack.pop();
        if (cur.getRightChild() != null)
            stack.push(cur.getRightChild());
        if (cur.getLeftChild() != null)
            stack.push(cur.getLeftChild());
        return cur.getData();
    }
}
