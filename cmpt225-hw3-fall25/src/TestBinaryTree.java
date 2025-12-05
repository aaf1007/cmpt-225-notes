import binarytree.BTNode;
import binarytree.BinaryTree;

import java.util.Iterator;

public class TestBinaryTree<T> {

    public static BinaryTree<Integer> createTree() {
        // creating the tree
        //     5
        //    / \
        //   2   6
        //  / \
        // 1   4
        //    / \
        //   8   -2
        BTNode<Integer> node8 = new BTNode<Integer>(8);
        BTNode<Integer> nodeNegative2 = new BTNode<Integer>(-2);
        BTNode<Integer> node4 = new BTNode<Integer>(4, node8, nodeNegative2, null);

        BTNode<Integer> node1 = new BTNode<Integer>(1);
        BTNode<Integer> node2 = new BTNode<Integer>(2, node1, node4, null);

        BTNode<Integer> node6 = new BTNode<Integer>(6);
        BTNode<Integer> node5 = new BTNode<Integer>(5, node2, node6, null);

        return new BinaryTree<Integer>(node5);
    }


    public static void testNumberOfLeaves() {
        BinaryTree<Integer> tree = createTree();
        if (tree.numberOfLeaves() == 4)
            System.out.println("numberOfLeaves OK");
        else
            System.out.println("numberOfLeaves ERROR");
    }


    public static void testEquals() {
        BinaryTree<Integer> tree1 = createTree();
        BinaryTree<Integer> tree2 = createTree();
        BinaryTree<Integer> tree3 = new BinaryTree<Integer>(new BTNode<Integer>(0)); // tree with one node

        if (tree1.equals(tree2) && !tree1.equals(tree3))
            System.out.println("equals OK");
        else
            System.out.println("equals ERROR");
    }

    public static void testCountDepthK() {
        BinaryTree<Integer> tree = createTree();
        if (tree.countDepthK(0) == 1 && tree.countDepthK(2) == 2 && tree.countDepthK(5) == 0)
            System.out.println("countDepthK OK");
        else
            System.out.println("countDepthK ERROR");
    }


    public static void testPreOrderIterator() {
        BinaryTree<Integer> tree = createTree();
        //     5
        //    / \
        //   2   6
        //  / \
        // 1   4
        //    / \
        //   8   -2
        Iterator<Integer> it = tree.preOrderIterator();
        // [5,2,1,4,8,-2,6]
        int firstFour[] = {5, 2, 1, 4};
        boolean flag = true;

        for (int i = 0; i < 4; i++)
            if (!it.hasNext() || it.next() != firstFour[i])
                flag = false;
        // it has [8,-2,6] left
        // adding more nodes under 6
        BTNode<Integer> six = tree.getRoot().getRightChild();
        six.setLeftChild(new BTNode<Integer>(100));
        six.setRightChild(new BTNode<Integer>(-50));
        //        5
        //      /   \
        //   2        6
        //  / \     /   \
        // 1   4  100   -50
        //    / \
        //   8   -2


        // it has [8,-2,6,100,-50] left
        int last5[] = {8, -2, 6, 100, -50};
        for (int i = 0; i < 5; i++)
            if (!it.hasNext() || it.next() != last5[i])
                flag = false;
        if (it.hasNext())
            flag = false;

        if (flag)
            System.out.println("preOrderIterator OK");
        else
            System.out.println("preOrderIterator ERROR");
    }


    public static void main(String[] args) {
        testNumberOfLeaves();
        testEquals();
        testCountDepthK();
        testPreOrderIterator();
    }

}
