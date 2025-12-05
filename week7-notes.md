# CMPT 225 - Week 7 Study Notes
## Data Structures and Programming

---

## Table of Contents
1. [AVL Trees](#avl-trees)
2. [AVL Tree Operations](#avl-tree-operations)
3. [B-Trees and 2-3 Trees](#b-trees-and-2-3-trees)
4. [Priority Queues](#priority-queues)
5. [Heaps](#heaps)
6. [Heap Sort](#heap-sort)

---

## AVL Trees

### Definition

**AVL Trees**: Named after inventors Georgy Adelson-Velsky and Evgenii Landis (1962 paper)

An AVL tree is a Binary Search Tree with the following property:

**Balance Property**: For every vertex v in the tree:
```
|height(v.leftSubtree) - height(v.rightSubtree)| ≤ 1
```

This property ensures the tree remains balanced at all times.

---

### Why AVL Trees?

**Balanced BST**: If a BST is balanced, operations cost O(log n) time.

**Examples of Balanced Trees**:
- Height difference between left and right subtrees is at most 1
- Tree is roughly symmetrical

**Examples of Unbalanced Trees**:
- All nodes form a chain (essentially a linked list)
- One subtree is significantly deeper than the other

---

### Smallest AVL Trees

For a given height h, what's the smallest AVL tree?

```
h = 0:  Single node (A)
        A
        
h = 1:  Two nodes
        A
       /
      B
      
h = 2:  Four nodes
        A
       / \
      B   C
     /
    D
    
h = 3:  Seven nodes
        A
       / \
      B   C
     / \  /
    D  E F
   /
  G
```

**Construction Rule**: To obtain the smallest AVL tree of height h:
- Take the minimum AVL tree of height h-1
- Take the minimum AVL tree of height h-2
- Make them children of a new root

---

### Height Analysis

#### Theorem 1: Exponential Lower Bound

**Claim**: If every vertex v satisfies the AVL balance property, then:
```
size ≥ 2^(height/2)
```

Equivalently: `height ≤ 2 log₂(size)`

**Proof by Induction**:

**Base case** (height = 0):
- size = 1
- 1 ≥ 2^0 = 1 ✓

**Base case** (height = 1):
- size ≥ 2
- height = 1 < 2 = 2 log₂(2) ✓

**Induction step** (height = h ≥ 2):
```
size ≥ 1 + min-size(h-1) + min-size(h-2)
     ≥ 1 + 2^((h-1)/2) + 2^((h-2)/2)
     > 2^(h/2)
```

---

#### Theorem 2: Fibonacci Lower Bound (Tighter)

**Claim**: If every vertex v satisfies the AVL balance property, then:
```
size ≥ Fib(height + 1) > 1.6^h
```

Where Fibonacci numbers are defined as:
- Fib(0) = 1
- Fib(1) = 1
- Fib(2) = 2
- Fib(3) = 3
- Fib(h) = Fib(h-1) + Fib(h-2)

**Proof by Induction**:

**Base case** (height = 0):
- size = 1 = Fib(1) ✓

**Base case** (height = 1):
- size ≥ 2 = Fib(2) ✓

**Induction step** (height = h ≥ 2):
```
size ≥ 1 + min-size(h-1) + min-size(h-2)
     ≥ 1 + Fib(h) + Fib(h-1)
     > Fib(h+1)
```

---

### Conclusion on Height

**Main Result**: If an AVL tree has N nodes, then:
```
height(tree) < log₁.₆(N)

Since N > 1.6^h:
    log₁.₆(N) > h
    h < log₁.₆(N) = log₁.₆(2) × log₂(N) < 1.5 log₂(N)
```

**Key Takeaway**: Height is O(log n), guaranteeing O(log n) operations!

---

### AVL Tree Implementation

```java
public class AVLNode<T extends Comparable<T>> {
    private T data;
    private AVLNode<T> leftChild;
    private AVLNode<T> rightChild;
    private AVLNode<T> parent;
    private int height;
    // Optional: private byte balance; // = leftChild.height - rightChild.height
}
```


---

## AVL Tree Operations

### Insertion Algorithm

**High-Level Algorithm**:

```java
add(element) {
    1. Insert new node as usual (as a leaf in BST)
    2. Go up the tree, update heights of ancestors
    3. If a node becomes unbalanced (balance becomes +2 or -2):
        - Apply "rotation" to fix the balance
        - Rotation is done on the grandchild with greatest height
}
```

---

### Insertion Example 1: Right-Right Case

**Initial Tree**:
```
        9 (height=2)
       /
      8 (height=1)
     /
    4 (height=0)
```

**Insert 10**:
```
Step 1: Insert 10 as leaf
        9 (height=?)
       /
      8 (height=?)
     / \
    4   10
    
Step 2: Update heights
        9 (height=3)  ← UNBALANCED! |left-right| = |2-0| = 2
       /
      8 (height=2)
     / \
    4   10
    
Step 3: Apply rotation (left rotation on 9)
        8 (height=2)
       / \
      4   9
           \
            10
```

**That's better!** All nodes are now balanced.

---

### Insertion Example 2: Right-Left Case

**Initial Tree**:
```
        8 (height=2)
       /
      10 (height=1)
     /
    4 (height=0)
```

**Insert 9**:
```
Step 1: Insert 9
        8 (height=3)  ← UNBALANCED!
       /
      10 (height=2)
     /
    4
     \
      9
      
Step 2: Apply double rotation
        First: Right rotation on 10
        Then: Left rotation on 8
        
Result:
        9 (height=2)
       / \
      8   10
     /
    4
```

---

### General Rotation Cases

There are **4 rotation cases**:

1. **Left-Left (LL)**: Single right rotation
2. **Right-Right (RR)**: Single left rotation
3. **Left-Right (LR)**: Double rotation (left then right)
4. **Right-Left (RL)**: Double rotation (right then left)

**Rotation Selection**: Rotate on the grandchild with the greatest height.

**Key Property**: After insertion, at most **ONE rotation** is needed!

---

### Deletion Algorithm

**High-Level Algorithm**:

```java
remove(element) {
    1. Remove node as in regular BST
       (depending on number of children)
    2. Update heights of all ancestors
    3. If a node becomes unbalanced (balance becomes +2 or -2):
        - Apply "rotation" to fix the balance
        - Rotation on grandchild with greatest height
        - If two grandchildren have same height,
          prefer rotating in the same direction as the child
}
```

---

### Deletion Examples

**Example 1 - Remove 1**:
```
Initial:
        4
       / \
      2   6
     / \
    1   3

After removal and rotation on deepest grandchild:
        4
       / \
      3   6
     /
    2
```

**Example 2 - Tie-breaking (Remove 2)**:
```
When two grandchildren have same height,
prefer going in the same direction as the child.
```

**Key Difference from Insertion**: Deletion may require **multiple rotations** up the tree!

---

### AVL Trees: Pros and Cons

**Pros**:
- Tree is always balanced
- All operations run in O(log n) time guaranteed
- Rebalancing costs a constant factor per operation

**Cons**:
- Rebalancing is not free
- For some applications, occasional O(n) operations might be acceptable if average is O(log n)
- Inefficient for database systems on disk (writing to disk is costly, especially across different blocks)
- Difficult to program (though good coders are highly valued!)

---

### AVL Trees: Practice Problems

**True or False** (prove your answer):

1. When a new element is added as a leaf using normal BST insertion, it is possible that more than one node becomes unbalanced.
   - **True**: Multiple ancestors can violate the balance property

2. Adding a new element to an AVL tree may require rebalancing at most one vertex.
   - **True**: One rotation fixes all imbalances for insertion

3. If adding a vertex to an AVL tree requires rebalancing, then the height of the tree does not change.
   - **False**: Height can increase even with rebalancing

4. When removing a node from an AVL tree, we may require rebalancing at most one vertex.
   - **False**: Deletion can require multiple rotations up to the root

---

## B-Trees and 2-3 Trees

### 2-3 Trees

**Definition**: A 2-3 tree has the following properties:

1. Every non-leaf vertex has **2 or 3 children**
2. Every vertex stores **1 or 2 values** (not just 1 like standard BSTs)
3. Values satisfy the ordering property:
   ```
   For node with value A, B:
   T1 ≤ A ≤ T2 ≤ B ≤ T3
   ```
4. **All leaves have the same depth**

**Visual Example**:
```
Node with 2 children:
       [A]
       / \
      R1  R2
      
Where: R1 ≤ A ≤ R2

Node with 3 children:
       [A, B]
       /  |  \
      T1  T2  T3
      
Where: T1 ≤ A ≤ T2 ≤ B ≤ T3
```

---

### 2-3 Trees: Height Analysis

**Properties**:
- Every non-leaf vertex has 2 or 3 children
- All leaves have the same depth

**Height Bounds**: For a tree of height h with N nodes:

**Lower bound** (minimum nodes):
```
N ≥ 1 + 2 + 2² + ... + 2^h ≥ 2^h
```

**Upper bound** (maximum nodes):
```
N ≤ 1 + 3 + 3² + ... + 3^h ≤ 3^(h+1)
```

**Therefore**:
```
log₃(size) - 1 ≤ h ≤ log₂(size)

Height = Θ(log(size))
```

**Key Takeaway**: Like AVL trees, 2-3 trees guarantee O(log n) height!

---

### B-Trees (Generalization)

**B-tree of order m**:

1. **Root** has between 2 and m children
2. **Every non-leaf non-root** has between ⌈m/2⌉ and m children
3. Every vertex stores a value between every two children
4. Values satisfy ordering property
5. **All leaves have the same depth**

**Visual Example** (order 4):
```
       [A, B, C]
       /  |  |  \
      T1  T2 T3  T4
      
Where: T1 ≤ A ≤ T2 ≤ B ≤ T3 ≤ C ≤ T4
```

**Note**: A 2-3 tree is a B-tree of order 3.

---

### B-Trees: History

**Invented by**: Rudolf Bayer and Ed McCreight at Boeing Research Labs

**Goal**: Efficiently manage index pages for large random access files

**Key Assumption**: Trees are huge, so only small chunks fit in main memory

**What does "B" stand for?**
- Boeing? Balanced? Broad? Bushy? Bayer?
- McCreight: "The more you think about what the B in B-trees means, the better you understand B-trees."

---

### B-Tree Insertion

**Algorithm**:

```
add(value):
    1. Add new value in a leaf
    
    2. If leaf has ≤ 2 values:
        STOP
        
    3. If leaf has 3 values ("overfilled"):
        a. Split into two nodes
        b. Take the median to be the parent
        c. Values < median → left node
        d. Values > median → right node
        e. Push median up to parent
        f. Fix parent if needed (recursively)
```

**Example**: Insert into [A, B, C] (overfilled)
```
Before:        [A, B, C]

After split:      [B]
                 /   \
               [A]   [C]
```

The median B is pushed up to the parent!

---

### B-Tree Deletion

**Algorithm**:

```
remove(value):
    1. Find the value in the tree
    
    2. If value is not in a leaf:
        a. Find its predecessor (in a leaf)
        b. Move predecessor to the deleted value's position
    
    3. Now we only need to handle deletions from leaves
    
    4. If leaf becomes empty ("underflow"):
        Apply rebalancing
```

---

### B-Tree Deletion: Rebalancing

**When a leaf becomes empty**:

```
Rebalancing options (in order of preference):
    1. If possible, borrow from right sibling
    2. Otherwise, borrow from left sibling
    3. Else, all siblings have only one value:
        - Merge parent with right sibling, if possible
        - Otherwise merge parent with left sibling
        - Fix parent recursively if needed
```

---

### B-Trees: Applications

**Commonly used in**:
- Databases
- File systems:
  - Apple's HFS+
  - Microsoft's NTFS
  - Some Linux file systems

**Why B-trees for file systems?**
- Records (nodes) are grouped in the same block on disk
- Minimizes disk I/O operations
- On average, rebalancing time is O(1)
- Much better than AVL trees where we update the entire path from modified node to root

**Practical Reference**: https://www.cs.usfca.edu/~galles/visualization/BTree.html


---

## Priority Queues

### Motivation

**Regular Queue**: Elements removed in the same order they were added (FIFO)

**Priority Queue**: Elements removed based on priority

**Real-World Examples**:
1. **Printer queue**: Prefer shorter jobs first
2. **CPU job scheduling**: Some processes are more important
3. **Customer service line**: Some customers are more valued
4. **Stock market order book**: Highest bids matched with lowest asks

---

### Priority Queue ADT

**A priority queue** is an ordered collection with the following operations:

```java
addItem(item)       // Add item to the queue
getTop()            // Return highest priority item (without removing)
removeTop()         // Remove and return highest priority item
getSize()           // Return number of items
isEmpty()           // Check if empty
```

**No bound** on the number of elements.

**Question**: How can we use a priority queue to sort an array?

**Answer**: 
1. Add all elements to priority queue
2. Remove them one by one
3. Result is sorted!

---

## Heaps

### Min-Heap Definition

**Heap**: An implementation of a Priority Queue

**Common confusion**: Priority queues are often called "heaps" because heaps are their most common implementation.

**We use min-heap**: Minimal element is removed first

**Public Methods**:
```java
addElement(element)
getMin()
removeMin()
getSize()
```

---

### Heap Properties

**A heap is a complete binary tree with**:

1. **Complete tree**: All levels are full except possibly the last
2. **Last row**: Vertices added left to right
3. **Min-heap property**: Value in each node is ≤ values of its children
4. **No requirements** about other relatives (siblings, cousins, etc.)

**Visual Example**:
```
        1
       / \
      4   2
     / \ / \
    9  6 3  10
   / \
  8  10
```

This is a valid min-heap:
- 1 < 4 and 1 < 2 ✓
- 4 < 9 and 4 < 6 ✓
- 2 < 3 and 2 < 10 ✓
- 9 < 8 and 9 < 10 ✓

---

### Heap Operations

#### Get Min

**Algorithm**:
```java
getMin() {
    return root.value;
}
```

**Time Complexity**: O(1)

**Reason**: Minimum is always at the root!

---

#### Add Element

**Algorithm**:
```java
addElement(item) {
    1. Add item to next available position (maintain complete tree)
    2. Propagate it up to satisfy min-heap condition
       (this is called "heapifyUp" or "bubbleUp")
}
```

**Visual Example** - Add 3:
```
Before:
        1
       / \
      4   7
     / \ / \
    9  6 9  10
   / \
  18 10

Step 1: Add 3 at next available position
        1
       / \
      4   7
     / \ / \
    9  6 9  10
   / \ /
  18 10 3

Step 2: HeapifyUp (3 < 7, swap)
        1
       / \
      4   3
     / \ / \
    9  6 9  10
   / \ /
  18 10 7

3 > 1, so stop. Done!
```

**Time Complexity**: O(log n) - Height of tree

---

#### Remove Min

**Algorithm**:
```java
removeMin() {
    1. min = root.value
    2. Move last element to root
    3. Propagate root down to satisfy min-heap condition
       (this is called "heapifyDown" or "bubbleDown")
       - Swap with SMALLER child if parent violates heap property
    4. Return min
}
```

**Visual Example**:
```
Before:
        1
       / \
      4   8
     / \ / \
    6  5 9  10
   / \
  8   7

Step 1: Save min = 1
Step 2: Move last element (7) to root
        7
       / \
      4   8
     / \ / \
    6  5 9  10
   /
  8

Step 3: HeapifyDown
   7 > 4 (left child), swap
        4
       / \
      7   8
     / \ / \
    6  5 9  10
   /
  8
  
   7 > 5 (right child), swap
        4
       / \
      5   8
     / \ / \
    6  7 9  10
   /
  8

Step 4: Return 1
```

**Time Complexity**: O(log n)

---

### Array Representation of Heaps

Since heaps are complete binary trees, we can represent them efficiently using arrays!

**Store**:
1. Size of the tree
2. BFS (level-order) traversal of the heap

**Example**:
```
Tree:
        1
       / \
      4   2
     / \ / \
    9  6 3  10
   / \ /
  8 10 7

Array: [1, 4, 2, 9, 6, 3, 10, 8, 10, 7]
Index:  0  1  2  3  4  5   6  7   8  9
```

---

### Array Navigation Formulas

**Critical formulas for array representation**:

```java
Root:                array[0]
Left child of i:     array[2*i + 1]
Right child of i:    array[2*i + 2]
Parent of j:         array[(j-1)/2]  // Integer division
```

**Example**:
```
Array: [1, 4, 2, 9, 6, 3, 10, 8, 10, 7]
       
Element 4 is at index 1:
- Left child:  array[2*1 + 1] = array[3] = 9 ✓
- Right child: array[2*1 + 2] = array[4] = 6 ✓

Element 9 is at index 3:
- Parent: array[(3-1)/2] = array[1] = 4 ✓
```

---

### Heap Operations Summary

**With array representation**:

```java
addElement(element)  // O(log n) time
getMin()            // O(1) time
removeMin()         // O(log n) time
```

**Space**: O(n) for the array

---

## Heap Sort

### Naive Approach

**Algorithm**:
```
Given an array:
    1. Add all elements to min-heap one by one
    2. Remove them one by one, insert into array in order
```

**Time Complexity**:
- Step 1: O(n log n) - n insertions, each O(log n)
- Step 2: O(n log n) - n removals, each O(log n)
- **Total**: O(n log n)

**Pros**: O(n log n) time

**Cons**: Requires O(n) extra space

**Question**: Can we do it in-place?

---

### In-Place Heap Sort

**Better Algorithm**:
```
Given an array:
    1. buildMaxHeap(array) - in place
    2. For i = 0 to n-1:
        Remove max, add it to end of array in right place
```

**Time Complexity**:
- Step 1: O(n) time (!!!)
- Step 2: O(n log n) time
- **Total**: O(n log n)

**Advantage**: In-place, no extra space!

**Key Insight**: Building a heap can be done in O(n), not O(n log n)!

---

### BuildHeap in O(n) Time

**Goal**: Given an array, turn it into a heap in O(n) time.

**Algorithm**:
```
buildHeap(array):
    1. Treat array as a complete binary tree
    2. For each vertex v, starting from bottom:
        heapifyDown(v)
```

**Key**: Start from the bottom and work up!

---

### BuildHeap Example

**Input**: `[9, 4, 2, 1, 7, 8, 5, 8, 10, 6]`

**Initial tree**:
```
        9
       / \
      4   2
     / \ / \
    1  7 8  5
   / \ /
  8 10 6
```

**Step 1**: Heapify node 7 (leaf, no change)

**Step 2**: Heapify node 1 → swap with 10
```
        9
       / \
      4   2
     / \ / \
   10  7 8  5
   / \ /
  8  1 6
```

**Step 3**: Heapify node 2 → swap with 8
```
        9
       / \
      4   8
     / \ / \
   10  7 2  5
   / \ /
  8  1 6
```

**Step 4**: Heapify node 4 → swap with 10
```
        9
       / \
     10   8
     / \ / \
    8  7 2  5
   / \ /
  4  1 6
```

**Step 5**: Heapify node 9 → swap with 10
```
       10
       / \
      9   8
     / \ / \
    8  7 2  5
   / \ /
  4  1 6
```

**Final array**: `[10, 9, 8, 8, 7, 2, 5, 4, 1, 6]`

This is now a valid max-heap!

---

### Why BuildHeap is O(n)

**Analysis**:

For a tree with n nodes:
- n/2 vertices at bottom level → heapify runs 1 step each
- n/4 vertices at next level → heapify runs 2 steps each
- n/8 vertices → heapify runs 3 steps each
- n/16 vertices → heapify runs 4 steps each
- ...
- 2 vertices → heapify runs log(n)-1 steps
- 1 vertex (root) → heapify runs log(n) steps

**Total time**:
```
T = 1·(n/2) + 2·(n/4) + 3·(n/8) + 4·(n/16) + ... + log(n)·(1)
  = n·(1/2 + 2/4 + 3/8 + 4/16 + ...)
  = n·∑(i/2^i) for i=1 to log(n)
```

**Mathematical fact**: ∑(i/2^i) = 2 for i=1 to ∞

Therefore: **T ≤ 2n = O(n)**

---

### Detailed Proof

**Sum evaluation**:
```
S = 1/2 + 2/4 + 3/8 + 4/16 + 5/32 + ...

Multiply by 2:
2S = 1 + 2/2 + 3/4 + 4/8 + 5/16 + ...

Subtract:
2S - S = 1 + 1/2 + 1/4 + 1/8 + 1/16 + ...
S = 1 + 1/2 + 1/4 + 1/8 + ... = 2

Therefore, the sum converges to 2!
```

---

### Complete HeapSort Algorithm

```java
heapSort(array) {
    // Step 1: Build max-heap in O(n)
    buildMaxHeap(array);
    
    // Step 2: Extract max n times in O(n log n)
    for (i = n-1; i >= 1; i--) {
        // Swap max (at index 0) with last element
        swap(array[0], array[i]);
        
        // Reduce heap size
        heapSize--;
        
        // Restore heap property
        heapifyDown(0);
    }
}
```

**Time Complexity**: O(n) + O(n log n) = **O(n log n)**

**Space Complexity**: O(1) - in-place!


---

## Practice Problems

### Heap Practice

1. Start with array A = [7, 1, 4, 2, 9, 3, 4, 10, 5, 8]
   - a. Apply buildHeap(A)
   - b. Add 6 to the heap
   - c. Remove min from the heap
   - d. Remove min again

2. Apply buildHeap on array [n, n-1, n-2, ..., 2, 1]

3. Apply buildHeap on array [1, 2, 3, 4, ..., n-1, n]

4. Modify min-heap to support `getAverage()` in O(1) time

5. Modify min-heap to support `getMax()` in O(1) time

---

### Advanced Practice

1. Write a data structure supporting:
   - `addElement(int)` in O(log n)
   - `getMin()` / `getMax()` in O(1)
   - `removeMin()` / `removeMax()` in O(log n)
   - `getSize()` / `isEmpty()`

2. Given inOrder traversal [4, 3, 5, 2, 9, 1, 5, 4, 7] of a min-heap, can we recover it uniquely?

3. Given preOrder traversal [1, 2, 3, 4, 5] of a min-heap, can we recover it uniquely?

4. If BuildMaxHeap iterates from top down, will we get a heap in the end?

---

## Summary of Key Concepts

### AVL Trees
- **Balance condition**: |height(left) - height(right)| ≤ 1
- **Height**: O(log n) guaranteed
- **Operations**: All O(log n)
- **Insertion**: At most 1 rotation
- **Deletion**: May need multiple rotations
- **4 rotation types**: LL, RR, LR, RL

### B-Trees / 2-3 Trees
- **Properties**: All leaves at same depth, nodes have 2-3 children
- **Height**: Θ(log n)
- **Used in**: Databases, file systems
- **Insertion**: Split overfilled nodes, push median up
- **Deletion**: Borrow from siblings or merge

### Priority Queues & Heaps
- **ADT**: addItem, getTop, removeTop
- **Min-heap property**: Parent ≤ children
- **Complete binary tree**: Efficient array representation
- **Array formulas**:
  - Left child: 2i + 1
  - Right child: 2i + 2
  - Parent: (j-1)/2

### Heap Operations
- **getMin**: O(1)
- **add**: O(log n) - heapifyUp
- **removeMin**: O(log n) - heapifyDown
- **buildHeap**: O(n) - bottom-up approach

### HeapSort
- **Time**: O(n log n)
- **Space**: O(1) - in-place
- **Steps**: buildMaxHeap + n extractions
- **Key insight**: buildHeap in O(n) time!

---

## Important Formulas and Theorems

### AVL Tree Height
```
size ≥ Fib(height + 1) > 1.6^h
height < 1.5 log₂(N)
```

### 2-3 Tree Height
```
log₃(N) - 1 ≤ height ≤ log₂(N)
```

### Array Navigation (Heaps)
```
root = array[0]
leftChild(i) = array[2i + 1]
rightChild(i) = array[2i + 2]
parent(j) = array[(j-1)/2]
```

### BuildHeap Time Complexity
```
T(n) = ∑(i · n/2^i) for i=1 to log(n)
     ≤ n · ∑(i/2^i) for i=1 to ∞
     = n · 2
     = O(n)
```

---

## Code Templates for Exams

### AVL Tree Node
```java
public class AVLNode<T extends Comparable<T>> {
    private T data;
    private AVLNode<T> leftChild;
    private AVLNode<T> rightChild;
    private AVLNode<T> parent;
    private int height;
    
    public int getBalance() {
        int leftHeight = (leftChild == null) ? -1 : leftChild.height;
        int rightHeight = (rightChild == null) ? -1 : rightChild.height;
        return leftHeight - rightHeight;
    }
    
    public void updateHeight() {
        int leftHeight = (leftChild == null) ? -1 : leftChild.height;
        int rightHeight = (rightChild == null) ? -1 : rightChild.height;
        height = 1 + Math.max(leftHeight, rightHeight);
    }
}
```

### Min-Heap Array Implementation
```java
public class MinHeap {
    private int[] array;
    private int size;
    private int capacity;
    
    public void add(int element) {
        if (size == capacity) resize();
        array[size] = element;
        heapifyUp(size);
        size++;
    }
    
    public int getMin() {
        if (size == 0) throw new Exception("Empty heap");
        return array[0];
    }
    
    public int removeMin() {
        if (size == 0) throw new Exception("Empty heap");
        int min = array[0];
        array[0] = array[size - 1];
        size--;
        heapifyDown(0);
        return min;
    }
    
    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (array[index] >= array[parent]) break;
            swap(index, parent);
            index = parent;
        }
    }
    
    private void heapifyDown(int index) {
        while (2 * index + 1 < size) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;
            
            if (left < size && array[left] < array[smallest])
                smallest = left;
            if (right < size && array[right] < array[smallest])
                smallest = right;
                
            if (smallest == index) break;
            swap(index, smallest);
            index = smallest;
        }
    }
}
```

### BuildHeap
```java
public void buildHeap(int[] array) {
    // Start from last non-leaf node and go backwards
    for (int i = (array.length / 2) - 1; i >= 0; i--) {
        heapifyDown(array, i, array.length);
    }
}
```

### HeapSort
```java
public void heapSort(int[] array) {
    // Step 1: Build max-heap
    buildMaxHeap(array);
    
    // Step 2: Extract elements one by one
    for (int i = array.length - 1; i > 0; i--) {
        // Move current max to end
        swap(array, 0, i);
        
        // Heapify reduced heap
        heapifyDown(array, 0, i);
    }
}
```

---

## Exam Tips

1. **AVL rotations**: Practice identifying which rotation to use (LL, RR, LR, RL)
   - Draw the tree after each operation
   - Update heights as you go

2. **Draw tree diagrams**: Visual representation helps immensely
   - Label heights on each node
   - Check balance factors

3. **Heap array representation**: Memorize the parent/child formulas
   - Practice converting between tree and array
   - Remember: array starts at index 0!

4. **BuildHeap vs repeated insert**: 
   - buildHeap is O(n), not O(n log n)!
   - Understand why (geometric series sum)

5. **B-tree operations**: Understand split/merge conditions
   - Split when node is overfilled (3 values in 2-3 tree)
   - Merge when node is underfilled (0 values)

6. **Time complexities**: Know them cold for all operations
   - AVL: All ops O(log n)
   - Heap: add/remove O(log n), getMin O(1)
   - HeapSort: O(n log n)

7. **Practice tracing**: Work through insertions/deletions step by step
   - Use practice problems from slides
   - Try online visualizers

8. **Proofs**: Understand induction proofs for height bounds
   - AVL: size ≥ Fib(h+1)
   - 2-3 trees: height = Θ(log n)

9. **Common mistakes**:
   - Forgetting to update heights after rotation
   - Wrong child formulas in array heap
   - Thinking buildHeap is O(n log n)
   - Forgetting AVL deletion may need multiple rotations

10. **Visual learning**: Use https://www.cs.usfca.edu/~galles/visualization/
    - AVL tree visualization
    - B-tree visualization
    - Heap visualization

Good luck with your studies!