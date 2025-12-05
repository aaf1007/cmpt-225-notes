
import java.util.Iterator;

import binarytree.BTNode;
import binarytree.BinaryTree;

/**
 * Combined Test Runner - Runs all tests in sequence
 * Run this to verify your implementation is complete
 */
public class RunAllTests {
    
    private static int totalTests = 0;
    private static int passedTests = 0;
    
    public static void main(String[] args) {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   BINARY TREE ASSIGNMENT - COMPREHENSIVE TEST SUITE        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        System.out.println("Part 1: Running Professor's Original Tests");
        System.out.println("─────────────────────────────────────────────────────────────");
        runProfessorTests();
        
        System.out.println("\n\nPart 2: Running Additional Edge Case Tests");
        System.out.println("─────────────────────────────────────────────────────────────");
        runAdditionalTests();
        
        System.out.println("\n\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                      TEST SUMMARY                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("Total Tests Run: " + totalTests);
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + (totalTests - passedTests));
        
        if (passedTests == totalTests) {
            System.out.println("\n✅ ALL TESTS PASSED! Your code is ready for submission! 🎉");
        } else {
            System.out.println("\n⚠️  Some tests failed. Please review the errors above.");
        }
        System.out.println("\n");
    }
    
    // ========== PROFESSOR'S TESTS ==========
    
    public static void runProfessorTests() {
        testNumberOfLeaves_Prof();
        testEquals_Prof();
        testCountDepthK_Prof();
        testPreOrderIterator_Prof();
    }
    
    public static BinaryTree<Integer> createTree() {
        BTNode<Integer> node8 = new BTNode<Integer>(8);
        BTNode<Integer> nodeNegative2 = new BTNode<Integer>(-2);
        BTNode<Integer> node4 = new BTNode<Integer>(4, node8, nodeNegative2, null);
        BTNode<Integer> node1 = new BTNode<Integer>(1);
        BTNode<Integer> node2 = new BTNode<Integer>(2, node1, node4, null);
        BTNode<Integer> node6 = new BTNode<Integer>(6);
        BTNode<Integer> node5 = new BTNode<Integer>(5, node2, node6, null);
        return new BinaryTree<Integer>(node5);
    }
    
    public static void testNumberOfLeaves_Prof() {
        totalTests++;
        BinaryTree<Integer> tree = createTree();
        if (tree.numberOfLeaves() == 4) {
            System.out.println("✓ numberOfLeaves (Professor's test)");
            passedTests++;
        } else {
            System.out.println("✗ numberOfLeaves (Professor's test) - Expected 4, got " + tree.numberOfLeaves());
        }
    }
    
    public static void testEquals_Prof() {
        totalTests++;
        BinaryTree<Integer> tree1 = createTree();
        BinaryTree<Integer> tree2 = createTree();
        BinaryTree<Integer> tree3 = new BinaryTree<Integer>(new BTNode<Integer>(0));
        
        if (tree1.equals(tree2) && !tree1.equals(tree3)) {
            System.out.println("✓ equals (Professor's test)");
            passedTests++;
        } else {
            System.out.println("✗ equals (Professor's test)");
        }
    }
    
    public static void testCountDepthK_Prof() {
        totalTests++;
        BinaryTree<Integer> tree = createTree();
        if (tree.countDepthK(0) == 1 && tree.countDepthK(2) == 2 && tree.countDepthK(5) == 0) {
            System.out.println("✓ countDepthK (Professor's test)");
            passedTests++;
        } else {
            System.out.println("✗ countDepthK (Professor's test)");
            System.out.println("  k=0: expected 1, got " + tree.countDepthK(0));
            System.out.println("  k=2: expected 2, got " + tree.countDepthK(2));
            System.out.println("  k=5: expected 0, got " + tree.countDepthK(5));
        }
    }
    
    public static void testPreOrderIterator_Prof() {
        totalTests++;
        BinaryTree<Integer> tree = createTree();
        Iterator<Integer> it = tree.preOrderIterator();
        int firstFour[] = {5, 2, 1, 4};
        boolean flag = true;
        
        for (int i = 0; i < 4; i++)
            if (!it.hasNext() || it.next() != firstFour[i])
                flag = false;
        
        BTNode<Integer> six = tree.getRoot().getRightChild();
        six.setLeftChild(new BTNode<Integer>(100));
        six.setRightChild(new BTNode<Integer>(-50));
        
        int last5[] = {8, -2, 6, 100, -50};
        for (int i = 0; i < 5; i++)
            if (!it.hasNext() || it.next() != last5[i])
                flag = false;
        if (it.hasNext())
            flag = false;
        
        if (flag) {
            System.out.println("✓ preOrderIterator (Professor's test - DYNAMIC)");
            passedTests++;
        } else {
            System.out.println("✗ preOrderIterator (Professor's test - DYNAMIC)");
        }
    }
    
    // ========== ADDITIONAL CRITICAL TESTS ==========
    
    public static void runAdditionalTests() {
        // Single node tests
        testSingleNodeTree();
        
        // countDepthK edge cases
        testCountDepthK_AllLevels();
        testCountDepthK_Negative();
        testCountDepthK_LargeK();
        
        // equals edge cases
        testEquals_Null();
        testEquals_SameReference();
        testEquals_DifferentStructure();
        
        // Iterator edge cases
        testIterator_SingleNode();
        testIterator_NoSuchElement();
        testIterator_LinearTree();
        
        // Additional dynamic test
        testIterator_DynamicSimple();
    }
    
    public static void testSingleNodeTree() {
        totalTests++;
        BTNode<Integer> root = new BTNode<Integer>(42);
        BinaryTree<Integer> tree = new BinaryTree<Integer>(root);
        
        boolean pass = true;
        if (tree.numberOfLeaves() != 1) pass = false;
        if (tree.countDepthK(0) != 1) pass = false;
        if (tree.countDepthK(1) != 0) pass = false;
        
        if (pass) {
            System.out.println("✓ Single node tree (all methods)");
            passedTests++;
        } else {
            System.out.println("✗ Single node tree");
        }
    }
    
    public static void testCountDepthK_AllLevels() {
        totalTests++;
        BinaryTree<Integer> tree = createTree();
        //     5          <- depth 0: 1 node
        //    / \
        //   2   6        <- depth 1: 2 nodes
        //  / \
        // 1   4          <- depth 2: 2 nodes
        //    / \
        //   8   -2       <- depth 3: 2 nodes
        
        boolean pass = true;
        if (tree.countDepthK(1) != 2) pass = false;
        if (tree.countDepthK(3) != 2) pass = false;
        
        if (pass) {
            System.out.println("✓ countDepthK (all levels: k=1 and k=3)");
            passedTests++;
        } else {
            System.out.println("✗ countDepthK (all levels)");
            System.out.println("  k=1: expected 2, got " + tree.countDepthK(1));
            System.out.println("  k=3: expected 2, got " + tree.countDepthK(3));
        }
    }
    
    public static void testCountDepthK_Negative() {
        totalTests++;
        BinaryTree<Integer> tree = createTree();
        try {
            tree.countDepthK(-1);
            System.out.println("✗ countDepthK (negative) - Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("✓ countDepthK (negative throws exception)");
            passedTests++;
        } catch (Exception e) {
            System.out.println("✗ countDepthK (negative) - Wrong exception type: " + e.getClass().getName());
        }
    }
    
    public static void testCountDepthK_LargeK() {
        totalTests++;
        BinaryTree<Integer> tree = createTree();
        if (tree.countDepthK(100) == 0) {
            System.out.println("✓ countDepthK (large k beyond tree height)");
            passedTests++;
        } else {
            System.out.println("✗ countDepthK (large k) - Expected 0, got " + tree.countDepthK(100));
        }
    }
    
    public static void testEquals_Null() {
        totalTests++;
        BinaryTree<Integer> tree = createTree();
        if (!tree.equals(null)) {
            System.out.println("✓ equals (null comparison)");
            passedTests++;
        } else {
            System.out.println("✗ equals (null comparison) - Should return false");
        }
    }
    
    public static void testEquals_SameReference() {
        totalTests++;
        BinaryTree<Integer> tree = createTree();
        if (tree.equals(tree)) {
            System.out.println("✓ equals (same reference)");
            passedTests++;
        } else {
            System.out.println("✗ equals (same reference) - Should return true");
        }
    }
    
    public static void testEquals_DifferentStructure() {
        totalTests++;
        BinaryTree<Integer> tree1 = createTree();
        BTNode<Integer> simpleRoot = new BTNode<Integer>(5, new BTNode<Integer>(2), null, null);
        BinaryTree<Integer> tree2 = new BinaryTree<Integer>(simpleRoot);
        
        if (!tree1.equals(tree2)) {
            System.out.println("✓ equals (different structure)");
            passedTests++;
        } else {
            System.out.println("✗ equals (different structure) - Should return false");
        }
    }
    
    public static void testIterator_SingleNode() {
        totalTests++;
        BTNode<Integer> root = new BTNode<Integer>(42);
        BinaryTree<Integer> tree = new BinaryTree<Integer>(root);
        Iterator<Integer> it = tree.preOrderIterator();
        
        if (it.hasNext() && it.next() == 42 && !it.hasNext()) {
            System.out.println("✓ preOrderIterator (single node)");
            passedTests++;
        } else {
            System.out.println("✗ preOrderIterator (single node)");
        }
    }
    
    public static void testIterator_NoSuchElement() {
        totalTests++;
        BTNode<Integer> root = new BTNode<Integer>(1);
        BinaryTree<Integer> tree = new BinaryTree<Integer>(root);
        Iterator<Integer> it = tree.preOrderIterator();
        
        it.next(); // Consume the only element
        
        try {
            it.next(); // Should throw
            System.out.println("✗ preOrderIterator (NoSuchElementException) - Should throw exception");
        } catch (java.util.NoSuchElementException e) {
            System.out.println("✓ preOrderIterator (NoSuchElementException)");
            passedTests++;
        } catch (Exception e) {
            System.out.println("✗ preOrderIterator (NoSuchElementException) - Wrong exception: " + e.getClass().getName());
        }
    }
    
    public static void testIterator_LinearTree() {
        totalTests++;
        // Create: 1 -> 2 -> 3 (all left)
        BTNode<Integer> node3 = new BTNode<Integer>(3);
        BTNode<Integer> node2 = new BTNode<Integer>(2, node3, null, null);
        BTNode<Integer> node1 = new BTNode<Integer>(1, node2, null, null);
        BinaryTree<Integer> tree = new BinaryTree<Integer>(node1);
        
        Iterator<Integer> it = tree.preOrderIterator();
        int[] expected = {1, 2, 3};
        boolean pass = true;
        
        for (int val : expected) {
            if (!it.hasNext() || it.next() != val) {
                pass = false;
                break;
            }
        }
        if (it.hasNext()) pass = false;
        
        if (pass) {
            System.out.println("✓ preOrderIterator (linear tree)");
            passedTests++;
        } else {
            System.out.println("✗ preOrderIterator (linear tree)");
        }
    }
    
    public static void testIterator_DynamicSimple() {
        totalTests++;
        BTNode<Integer> left = new BTNode<Integer>(2);
        BTNode<Integer> right = new BTNode<Integer>(3);
        BTNode<Integer> root = new BTNode<Integer>(1, left, right, null);
        BinaryTree<Integer> tree = new BinaryTree<Integer>(root);
        
        Iterator<Integer> it = tree.preOrderIterator();
        
        it.next(); // 1
        it.next(); // 2
        
        // Modify unvisited right subtree
        right.setLeftChild(new BTNode<Integer>(4));
        right.setRightChild(new BTNode<Integer>(5));
        
        // Should see: 3, 4, 5
        boolean pass = true;
        int[] expected = {3, 4, 5};
        for (int val : expected) {
            if (!it.hasNext() || it.next() != val) {
                pass = false;
                break;
            }
        }
        if (it.hasNext()) pass = false;
        
        if (pass) {
            System.out.println("✓ preOrderIterator (dynamic - simple case)");
            passedTests++;
        } else {
            System.out.println("✗ preOrderIterator (dynamic - simple case)");
        }
    }
}