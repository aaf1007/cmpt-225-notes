import binarytree.BTNode;
import binarytree.BinaryTree;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Additional comprehensive test cases for BinaryTree assignment
 * These tests cover edge cases and scenarios likely to be tested by professor
 */
public class AdditionalTests {

    // ==================== HELPER METHODS ====================

    public static BinaryTree<Integer> createTree() {
        // Same tree as professor's test
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

    public static BinaryTree<Integer> createSingleNode() {
        return new BinaryTree<Integer>(new BTNode<Integer>(42));
    }

    public static BinaryTree<Integer> createLinearLeftTree() {
        // Linear tree: 1 -> 2 -> 3 (all left children)
        //   1
        //  /
        // 2
        //  /
        // 3
        BTNode<Integer> node3 = new BTNode<Integer>(3);
        BTNode<Integer> node2 = new BTNode<Integer>(2, node3, null, null);
        BTNode<Integer> node1 = new BTNode<Integer>(1, node2, null, null);
        return new BinaryTree<Integer>(node1);
    }

    public static BinaryTree<Integer> createLinearRightTree() {
        // Linear tree: 1 -> 2 -> 3 (all right children)
        //   1
        //    \
        //     2
        //      \
        //       3
        BTNode<Integer> node3 = new BTNode<Integer>(3);
        BTNode<Integer> node2 = new BTNode<Integer>(2, null, node3, null);
        BTNode<Integer> node1 = new BTNode<Integer>(1, null, node2, null);
        return new BinaryTree<Integer>(node1);
    }

    public static BinaryTree<Integer> createCompleteTree() {
        // Complete binary tree
        //       1
        //      / \
        //     2   3
        //    / \ / \
        //   4  5 6  7
        BTNode<Integer> node4 = new BTNode<Integer>(4);
        BTNode<Integer> node5 = new BTNode<Integer>(5);
        BTNode<Integer> node6 = new BTNode<Integer>(6);
        BTNode<Integer> node7 = new BTNode<Integer>(7);
        BTNode<Integer> node2 = new BTNode<Integer>(2, node4, node5, null);
        BTNode<Integer> node3 = new BTNode<Integer>(3, node6, node7, null);
        BTNode<Integer> node1 = new BTNode<Integer>(1, node2, node3, null);
        return new BinaryTree<Integer>(node1);
    }

    // ==================== numberOfLeaves() TESTS ====================

    public static void testNumberOfLeaves_SingleNode() {
        BinaryTree<Integer> tree = createSingleNode();
        if (tree.numberOfLeaves() == 1)
            System.out.println("numberOfLeaves (single node) OK");
        else
            System.out.println("numberOfLeaves (single node) ERROR - Expected 1, got " + tree.numberOfLeaves());
    }

    public static void testNumberOfLeaves_LinearLeft() {
        BinaryTree<Integer> tree = createLinearLeftTree();
        if (tree.numberOfLeaves() == 1) // Only the deepest node is a leaf
            System.out.println("numberOfLeaves (linear left) OK");
        else
            System.out.println("numberOfLeaves (linear left) ERROR - Expected 1, got " + tree.numberOfLeaves());
    }

    public static void testNumberOfLeaves_LinearRight() {
        BinaryTree<Integer> tree = createLinearRightTree();
        if (tree.numberOfLeaves() == 1) // Only the deepest node is a leaf
            System.out.println("numberOfLeaves (linear right) OK");
        else
            System.out.println("numberOfLeaves (linear right) ERROR - Expected 1, got " + tree.numberOfLeaves());
    }

    public static void testNumberOfLeaves_CompleteTree() {
        BinaryTree<Integer> tree = createCompleteTree();
        if (tree.numberOfLeaves() == 4) // Nodes 4,5,6,7 are leaves
            System.out.println("numberOfLeaves (complete tree) OK");
        else
            System.out.println("numberOfLeaves (complete tree) ERROR - Expected 4, got " + tree.numberOfLeaves());
    }

    // ==================== equals() TESTS ====================

    public static void testEquals_SameReference() {
        BinaryTree<Integer> tree = createTree();
        if (tree.equals(tree))
            System.out.println("equals (same reference) OK");
        else
            System.out.println("equals (same reference) ERROR");
    }

    public static void testEquals_Null() {
        BinaryTree<Integer> tree = createTree();
        if (!tree.equals(null))
            System.out.println("equals (null) OK");
        else
            System.out.println("equals (null) ERROR");
    }

    public static void testEquals_DifferentClass() {
        BinaryTree<Integer> tree = createTree();
        String notATree = "not a tree";
        if (!tree.equals(notATree))
            System.out.println("equals (different class) OK");
        else
            System.out.println("equals (different class) ERROR");
    }

    public static void testEquals_DifferentStructure() {
        BinaryTree<Integer> tree1 = createTree();
        BinaryTree<Integer> tree2 = createCompleteTree();
        if (!tree1.equals(tree2))
            System.out.println("equals (different structure) OK");
        else
            System.out.println("equals (different structure) ERROR");
    }

    public static void testEquals_SameStructureDifferentData() {
        // Tree 1: root=5
        BTNode<Integer> tree1Root = new BTNode<Integer>(5, 
            new BTNode<Integer>(2), 
            new BTNode<Integer>(3), null);
        BinaryTree<Integer> tree1 = new BinaryTree<Integer>(tree1Root);
        
        // Tree 2: root=5, but different child values
        BTNode<Integer> tree2Root = new BTNode<Integer>(5, 
            new BTNode<Integer>(2), 
            new BTNode<Integer>(999), null); // Different right child
        BinaryTree<Integer> tree2 = new BinaryTree<Integer>(tree2Root);
        
        if (!tree1.equals(tree2))
            System.out.println("equals (different data) OK");
        else
            System.out.println("equals (different data) ERROR");
    }

    public static void testEquals_LeftVsRightSubtree() {
        // Tree with only left child
        BTNode<Integer> tree1Root = new BTNode<Integer>(1, 
            new BTNode<Integer>(2), null, null);
        BinaryTree<Integer> tree1 = new BinaryTree<Integer>(tree1Root);
        
        // Tree with only right child
        BTNode<Integer> tree2Root = new BTNode<Integer>(1, 
            null, new BTNode<Integer>(2), null);
        BinaryTree<Integer> tree2 = new BinaryTree<Integer>(tree2Root);
        
        if (!tree1.equals(tree2))
            System.out.println("equals (left vs right subtree) OK");
        else
            System.out.println("equals (left vs right subtree) ERROR");
    }

    // ==================== countDepthK() TESTS ====================

    public static void testCountDepthK_SingleNode() {
        BinaryTree<Integer> tree = createSingleNode();
        boolean pass = true;
        
        if (tree.countDepthK(0) != 1) {
            System.out.println("countDepthK (single node, k=0) ERROR - Expected 1, got " + tree.countDepthK(0));
            pass = false;
        }
        if (tree.countDepthK(1) != 0) {
            System.out.println("countDepthK (single node, k=1) ERROR - Expected 0, got " + tree.countDepthK(1));
            pass = false;
        }
        if (pass)
            System.out.println("countDepthK (single node) OK");
    }

    public static void testCountDepthK_CompleteTree() {
        BinaryTree<Integer> tree = createCompleteTree();
        //       1        <- depth 0: 1 node
        //      / \
        //     2   3      <- depth 1: 2 nodes
        //    / \ / \
        //   4  5 6  7    <- depth 2: 4 nodes
        
        boolean pass = true;
        if (tree.countDepthK(0) != 1) {
            System.out.println("countDepthK (complete, k=0) ERROR - Expected 1, got " + tree.countDepthK(0));
            pass = false;
        }
        if (tree.countDepthK(1) != 2) {
            System.out.println("countDepthK (complete, k=1) ERROR - Expected 2, got " + tree.countDepthK(1));
            pass = false;
        }
        if (tree.countDepthK(2) != 4) {
            System.out.println("countDepthK (complete, k=2) ERROR - Expected 4, got " + tree.countDepthK(2));
            pass = false;
        }
        if (tree.countDepthK(3) != 0) {
            System.out.println("countDepthK (complete, k=3) ERROR - Expected 0, got " + tree.countDepthK(3));
            pass = false;
        }
        if (pass)
            System.out.println("countDepthK (complete tree) OK");
    }

    public static void testCountDepthK_LinearTree() {
        BinaryTree<Integer> tree = createLinearLeftTree();
        // 1 -> 2 -> 3 (each depth has 1 node)
        
        boolean pass = true;
        if (tree.countDepthK(0) != 1) {
            System.out.println("countDepthK (linear, k=0) ERROR");
            pass = false;
        }
        if (tree.countDepthK(1) != 1) {
            System.out.println("countDepthK (linear, k=1) ERROR");
            pass = false;
        }
        if (tree.countDepthK(2) != 1) {
            System.out.println("countDepthK (linear, k=2) ERROR");
            pass = false;
        }
        if (tree.countDepthK(3) != 0) {
            System.out.println("countDepthK (linear, k=3) ERROR");
            pass = false;
        }
        if (pass)
            System.out.println("countDepthK (linear tree) OK");
    }

    public static void testCountDepthK_LargeDepth() {
        BinaryTree<Integer> tree = createTree();
        // Test large k values (beyond tree height)
        if (tree.countDepthK(100) == 0 && tree.countDepthK(1000) == 0)
            System.out.println("countDepthK (large depth) OK");
        else
            System.out.println("countDepthK (large depth) ERROR");
    }

    public static void testCountDepthK_NegativeThrowsException() {
        BinaryTree<Integer> tree = createTree();
        try {
            tree.countDepthK(-1);
            System.out.println("countDepthK (negative) ERROR - Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("countDepthK (negative exception) OK");
        }
    }

    public static void testCountDepthK_ProfTree() {
        BinaryTree<Integer> tree = createTree();
        //     5          <- depth 0: 1 node
        //    / \
        //   2   6        <- depth 1: 2 nodes
        //  / \
        // 1   4          <- depth 2: 2 nodes
        //    / \
        //   8   -2       <- depth 3: 2 nodes
        
        boolean pass = true;
        if (tree.countDepthK(1) != 2) {
            System.out.println("countDepthK (prof tree, k=1) ERROR - Expected 2, got " + tree.countDepthK(1));
            pass = false;
        }
        if (tree.countDepthK(3) != 2) {
            System.out.println("countDepthK (prof tree, k=3) ERROR - Expected 2, got " + tree.countDepthK(3));
            pass = false;
        }
        if (tree.countDepthK(4) != 0) {
            System.out.println("countDepthK (prof tree, k=4) ERROR - Expected 0, got " + tree.countDepthK(4));
            pass = false;
        }
        if (pass)
            System.out.println("countDepthK (prof tree additional cases) OK");
    }

    // ==================== preOrderIterator() TESTS ====================

    public static void testPreOrderIterator_SingleNode() {
        BinaryTree<Integer> tree = createSingleNode();
        Iterator<Integer> it = tree.preOrderIterator();
        
        if (it.hasNext() && it.next() == 42 && !it.hasNext())
            System.out.println("preOrderIterator (single node) OK");
        else
            System.out.println("preOrderIterator (single node) ERROR");
    }

    public static void testPreOrderIterator_LinearLeft() {
        BinaryTree<Integer> tree = createLinearLeftTree();
        //   1
        //  /
        // 2
        //  /
        // 3
        // Pre-order: 1, 2, 3
        Iterator<Integer> it = tree.preOrderIterator();
        int[] expected = {1, 2, 3};
        boolean pass = true;
        
        for (int i = 0; i < expected.length; i++) {
            if (!it.hasNext() || it.next() != expected[i]) {
                pass = false;
                break;
            }
        }
        if (it.hasNext())
            pass = false;
        
        if (pass)
            System.out.println("preOrderIterator (linear left) OK");
        else
            System.out.println("preOrderIterator (linear left) ERROR");
    }

    public static void testPreOrderIterator_LinearRight() {
        BinaryTree<Integer> tree = createLinearRightTree();
        //   1
        //    \
        //     2
        //      \
        //       3
        // Pre-order: 1, 2, 3
        Iterator<Integer> it = tree.preOrderIterator();
        int[] expected = {1, 2, 3};
        boolean pass = true;
        
        for (int i = 0; i < expected.length; i++) {
            if (!it.hasNext() || it.next() != expected[i]) {
                pass = false;
                break;
            }
        }
        if (it.hasNext())
            pass = false;
        
        if (pass)
            System.out.println("preOrderIterator (linear right) OK");
        else
            System.out.println("preOrderIterator (linear right) ERROR");
    }

    public static void testPreOrderIterator_CompleteTree() {
        BinaryTree<Integer> tree = createCompleteTree();
        //       1
        //      / \
        //     2   3
        //    / \ / \
        //   4  5 6  7
        // Pre-order: 1, 2, 4, 5, 3, 6, 7
        Iterator<Integer> it = tree.preOrderIterator();
        int[] expected = {1, 2, 4, 5, 3, 6, 7};
        boolean pass = true;
        
        for (int i = 0; i < expected.length; i++) {
            if (!it.hasNext() || it.next() != expected[i]) {
                System.out.println("preOrderIterator (complete) ERROR at position " + i + 
                    " - Expected " + expected[i]);
                pass = false;
                break;
            }
        }
        if (it.hasNext())
            pass = false;
        
        if (pass)
            System.out.println("preOrderIterator (complete tree) OK");
        else
            System.out.println("preOrderIterator (complete tree) ERROR");
    }

    public static void testPreOrderIterator_NoSuchElementException() {
        BinaryTree<Integer> tree = createSingleNode();
        Iterator<Integer> it = tree.preOrderIterator();
        
        it.next(); // Consume the only element
        
        try {
            it.next(); // Should throw NoSuchElementException
            System.out.println("preOrderIterator (NoSuchElementException) ERROR - Should throw exception");
        } catch (NoSuchElementException e) {
            System.out.println("preOrderIterator (NoSuchElementException) OK");
        }
    }

    public static void testPreOrderIterator_DynamicModification_AddToLeft() {
        // Test adding nodes to the LEFT subtree during iteration
        BTNode<Integer> node2 = new BTNode<Integer>(2);
        BTNode<Integer> node3 = new BTNode<Integer>(3);
        BTNode<Integer> root = new BTNode<Integer>(1, node2, node3, null);
        BinaryTree<Integer> tree = new BinaryTree<Integer>(root);
        
        Iterator<Integer> it = tree.preOrderIterator();
        
        // Process: 1, 2
        if (it.next() != 1) {
            System.out.println("preOrderIterator (dynamic left) ERROR - first element");
            return;
        }
        if (it.next() != 2) {
            System.out.println("preOrderIterator (dynamic left) ERROR - second element");
            return;
        }
        
        // Now modify node 3 (not yet visited) by adding children
        node3.setLeftChild(new BTNode<Integer>(4));
        node3.setRightChild(new BTNode<Integer>(5));
        
        // Continue iteration - should see: 3, 4, 5
        int[] expected = {3, 4, 5};
        boolean pass = true;
        for (int val : expected) {
            if (!it.hasNext() || it.next() != val) {
                pass = false;
                break;
            }
        }
        
        if (it.hasNext())
            pass = false;
        
        if (pass)
            System.out.println("preOrderIterator (dynamic - add to right subtree) OK");
        else
            System.out.println("preOrderIterator (dynamic - add to right subtree) ERROR");
    }

    public static void testPreOrderIterator_DynamicModification_DeepTree() {
        // More complex dynamic test
        BTNode<Integer> left = new BTNode<Integer>(2);
        BTNode<Integer> right = new BTNode<Integer>(3);
        BTNode<Integer> root = new BTNode<Integer>(1, left, right, null);
        BinaryTree<Integer> tree = new BinaryTree<Integer>(root);
        
        Iterator<Integer> it = tree.preOrderIterator();
        
        // Get first 2 elements
        int first = it.next();  // Should be 1
        int second = it.next(); // Should be 2
        
        // Add children to left node (already processed - shouldn't see these)
        left.setLeftChild(new BTNode<Integer>(10));
        left.setRightChild(new BTNode<Integer>(11));
        
        // Add children to right node (not yet processed - SHOULD see these)
        right.setLeftChild(new BTNode<Integer>(20));
        right.setRightChild(new BTNode<Integer>(21));
        
        // Expected: 3, 20, 21
        boolean pass = (first == 1 && second == 2);
        int[] expected = {3, 20, 21};
        
        for (int val : expected) {
            if (!it.hasNext() || it.next() != val) {
                pass = false;
                break;
            }
        }
        
        if (it.hasNext())
            pass = false;
        
        if (pass)
            System.out.println("preOrderIterator (dynamic - deep modification) OK");
        else
            System.out.println("preOrderIterator (dynamic - deep modification) ERROR");
    }

    public static void testPreOrderIterator_MultipleIterators() {
        // Test that multiple iterators work independently
        BinaryTree<Integer> tree = createCompleteTree();
        
        Iterator<Integer> it1 = tree.preOrderIterator();
        Iterator<Integer> it2 = tree.preOrderIterator();
        
        // Advance it1 by 3 steps
        it1.next(); it1.next(); it1.next();
        
        // it2 should still be at the start
        if (it2.next() == 1 && it1.hasNext())
            System.out.println("preOrderIterator (multiple iterators) OK");
        else
            System.out.println("preOrderIterator (multiple iterators) ERROR");
    }

    // ==================== MAIN ====================

    public static void main(String[] args) {
        System.out.println("\n========== RUNNING ADDITIONAL TEST SUITE ==========\n");
        
        System.out.println("--- numberOfLeaves() Tests ---");
        testNumberOfLeaves_SingleNode();
        testNumberOfLeaves_LinearLeft();
        testNumberOfLeaves_LinearRight();
        testNumberOfLeaves_CompleteTree();
        
        System.out.println("\n--- equals() Tests ---");
        testEquals_SameReference();
        testEquals_Null();
        testEquals_DifferentClass();
        testEquals_DifferentStructure();
        testEquals_SameStructureDifferentData();
        testEquals_LeftVsRightSubtree();
        
        System.out.println("\n--- countDepthK() Tests ---");
        testCountDepthK_SingleNode();
        testCountDepthK_CompleteTree();
        testCountDepthK_LinearTree();
        testCountDepthK_LargeDepth();
        testCountDepthK_NegativeThrowsException();
        testCountDepthK_ProfTree();
        
        System.out.println("\n--- preOrderIterator() Tests ---");
        testPreOrderIterator_SingleNode();
        testPreOrderIterator_LinearLeft();
        testPreOrderIterator_LinearRight();
        testPreOrderIterator_CompleteTree();
        testPreOrderIterator_NoSuchElementException();
        testPreOrderIterator_DynamicModification_AddToLeft();
        testPreOrderIterator_DynamicModification_DeepTree();
        testPreOrderIterator_MultipleIterators();
        
        System.out.println("\n========== TEST SUITE COMPLETE ==========\n");
    }
}