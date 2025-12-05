# CMPT 225 – Trees Cheat Sheet (Developer-Focused, Java)

---

## Trees – Core Concepts

**Definition & Purpose**
- A **tree** is a connected, acyclic hierarchy with a distinguished **root**. Each non-root node has exactly one **parent**; nodes with no children are **leaves**. Great for hierarchical data (filesystems, DOM, ASTs).

**Key Theories / Principles**
- **Depth(node)**: edges from root to node  
- **Height(tree)**: max depth among nodes  
- **Size(tree)**: number of nodes  
- **Subtree**: node plus all descendants  
- Unique simple path from root to any node

**ASCII Diagrams**

- General rooted tree
```
            root
        /     |     \
     n1       n2     n3
   / | \            /   \
  a  b  c          d     e
```

- Full vs Perfect vs Complete (binary illustrations)
```
Full (0 or 2 children)          Perfect (all leaves same depth)
         •                                •
       /   \                            /   \
      •     •                          •     •
     / \   / \                        / \   / \
    •  •  •  •                      •  •  •  •

Complete (filled by levels, left packed)
         •
       /   \
      •     •
     / \   /
    •  •  •
```

- Balanced vs Skewed
```
Balanced                         Right-skewed (height ~ N-1)
     •                                   •
    / \                                   \
   •   •                                   •
  / \   \                                   \
 •  •    •                                   •
```

**Use Cases / When to Use**
- Hierarchies, compilers (AST), UI scenes, indexes and priority structures (heaps), search structures (BST family)

**Common Pitfalls**
- Creating cycles
- Confusing height vs depth
- Mutations that break parent-child invariants

**Pro Tips**
- Track `parent` only if needed
- Choose and document one height convention for your codebase

**Minimal Node & Tree Types (general arity)**
```java
import java.util.*;

class Node<T> {
    T data;
    List<Node<T>> children;
    Node<T> parent;  // optional

    Node(T d) {
        data = d;
        children = new ArrayList<>();
    }
}

class Tree<T> {
    Node<T> root;
}
```

---

## Binary Trees (BT)

**Definition & Purpose**
- A **binary tree** restricts children to `left` and `right`. Foundation for BST, AVL, Red-Black, segment trees, expression trees.

**Key Principles**
- Level k has at most 2^k nodes
- Max nodes in height h (edges-based) is 2^(h+1) - 1
- Height ranges from log N (balanced) to N-1 (skewed)

**Reference Binary Node**
```java
class BTNode<T> {
    T data;
    BTNode<T> left;
    BTNode<T> right;
    BTNode<T> parent; // optional

    BTNode(T d) { data = d; }
}

class BinaryTree<T> {
    BTNode<T> root;
}
```

---

## Size, Height, Depth

**Conventions (choose one and stay consistent)**
- Edges-based: height(null) = -1, height(leaf) = 0  
- Nodes-based: height(null) = 0, height(leaf) = 1

**Recursive Implementations (edges-based)**
```java
class BTUtils {
    static int height(BTNode<Integer> n) {
        if (n == null) return -1;
        int hl = height(n.left);
        int hr = height(n.right);
        return 1 + Math.max(hl, hr);
    }

    static int size(BTNode<Integer> n) {
        if (n == null) return 0;
        return 1 + size(n.left) + size(n.right);
    }

    static int depth(BTNode<Integer> root, BTNode<Integer> x) {
        int d = 0;
        BTNode<Integer> cur = x;
        while (cur != null && cur != root) {
            cur = cur.parent;
            d++;
        }
        return (cur == null) ? -1 : d;
    }
}
```

**Iterative Versions**
```java
import java.util.*;

class BTIter {
    static int heightIter(BTNode<Integer> root) {
        if (root == null) return -1;
        Queue<BTNode<Integer>> q = new ArrayDeque<>();
        q.add(root);
        int h = -1;
        while (!q.isEmpty()) {
            int levelSize = q.size();
            while (levelSize > 0) {
                BTNode<Integer> n = q.remove();
                if (n.left  != null) q.add(n.left);
                if (n.right != null) q.add(n.right);
                levelSize--;
            }
            h++;
        }
        return h;
    }

    static int sizeIter(BTNode<Integer> root) {
        if (root == null) return 0;
        Deque<BTNode<Integer>> st = new ArrayDeque<>();
        st.push(root);
        int cnt = 0;
        while (!st.isEmpty()) {
            BTNode<Integer> n = st.pop();
            cnt++;
            if (n.left  != null) st.push(n.left);
            if (n.right != null) st.push(n.right);
        }
        return cnt;
    }
}
```

---

## Traversals (DFS)

**Definition & Purpose**
- Systematic visits  
  - **Preorder**: root, left, right  
  - **Inorder**: left, root, right  
  - **Postorder**: left, right, root  
- Inorder on a BST yields sorted order

**ASCII Order Example**
```
       10
      /  \
     5    21
    / \  /  \
   1  7 16  25

Preorder  : 10, 5, 1, 7, 21, 16, 25
Inorder   : 1, 5, 7, 10, 16, 21, 25
Postorder : 1, 7, 5, 16, 25, 21, 10
```

**Recursive Templates**
```java
class TraversalsRec {
    static void preorder(BTNode<Integer> n) {
        if (n == null) return;
        visit(n);
        preorder(n.left);
        preorder(n.right);
    }

    static void inorder(BTNode<Integer> n) {
        if (n == null) return;
        inorder(n.left);
        visit(n);
        inorder(n.right);
    }

    static void postorder(BTNode<Integer> n) {
        if (n == null) return;
        postorder(n.left);
        postorder(n.right);
        visit(n);
    }

    static void visit(BTNode<Integer> n) {
        // print or process n.data
        System.out.print(n.data + " ");
    }
}
```

**Iterative Templates**
```java
import java.util.*;

class TraversalsIter {
    static void inorderIter(BTNode<Integer> root) {
        Deque<BTNode<Integer>> st = new ArrayDeque<>();
        BTNode<Integer> cur = root;
        while (cur != null || !st.isEmpty()) {
            while (cur != null) {
                st.push(cur);
                cur = cur.left;
            }
            cur = st.pop();
            visit(cur);
            cur = cur.right;
        }
    }

    static void preorderIter(BTNode<Integer> root) {
        if (root == null) return;
        Deque<BTNode<Integer>> st = new ArrayDeque<>();
        st.push(root);
        while (!st.isEmpty()) {
            BTNode<Integer> n = st.pop();
            visit(n);
            if (n.right != null) st.push(n.right);
            if (n.left  != null) st.push(n.left);
        }
    }

    static void postorderIter(BTNode<Integer> root) {
        if (root == null) return;
        Deque<BTNode<Integer>> s1 = new ArrayDeque<>();
        Deque<BTNode<Integer>> s2 = new ArrayDeque<>();
        s1.push(root);
        while (!s1.isEmpty()) {
            BTNode<Integer> n = s1.pop();
            s2.push(n);
            if (n.left  != null) s1.push(n.left);
            if (n.right != null) s1.push(n.right);
        }
        while (!s2.isEmpty()) {
            visit(s2.pop());
        }
    }

    static void visit(BTNode<Integer> n) {
        System.out.print(n.data + " ");
    }
}
```

---

## BFS (Level Order)

**Purpose**
- Visit nodes by depth. Useful for shortest path in unweighted trees, computing height, and width.

```java
import java.util.*;

class LevelOrder {
    static void levelOrder(BTNode<Integer> root) {
        if (root == null) return;
        Queue<BTNode<Integer>> q = new ArrayDeque<>();
        q.add(root);
        while (!q.isEmpty()) {
            BTNode<Integer> n = q.remove();
            visit(n);
            if (n.left  != null) q.add(n.left);
            if (n.right != null) q.add(n.right);
        }
    }

    static void visit(BTNode<Integer> n) {
        System.out.print(n.data + " ");
    }
}
```

**Level Boundaries (example)**
```
Level 0: 10
Level 1: 5, 21
Level 2: 1, 7, 16, 25
```

---

## Expression Trees

**Definition & Purpose**
- Leaves are operands, internal nodes are operators. Evaluate with postorder. Pretty print with inorder plus parentheses.

**ASCII**
```
         (*)
        /   \
      (/)   (+)
      / \   / \
    10  2  6  3
```

**Code**
```java
class ExprTree {
    static double eval(BTNode<String> n) {
        if (n.left == null && n.right == null) return Double.parseDouble(n.data);
        double L = eval(n.left);
        double R = eval(n.right);
        if (n.data.equals("+")) return L + R;
        if (n.data.equals("-")) return L - R;
        if (n.data.equals("*")) return L * R;
        if (n.data.equals("/")) return L / R;
        throw new IllegalArgumentException("op " + n.data);
    }

    static String infix(BTNode<String> n) {
        if (n.left == null && n.right == null) return n.data;
        return "(" + infix(n.left) + " " + n.data + " " + infix(n.right) + ")";
    }
}
```

---

## Build Patterns & Utilities

**Build complete binary tree from array (heap-style indices)**
```java
class Builders {
    // left = 2*i + 1, right = 2*i + 2
    static BTNode<Integer> buildTreeFromArray(Integer[] a, int i) {
        if (i >= a.length || a[i] == null) return null;
        BTNode<Integer> n = new BTNode<>(a[i]);
        n.left  = buildTreeFromArray(a, 2*i + 1);
        n.right = buildTreeFromArray(a, 2*i + 2);
        return n;
    }
}
```

**Visitor hook**
```java
static void visit(BTNode<Integer> n) {
    // print or process n.data
    System.out.print(n.data + " ");
}
```

---

## Space and Time Complexity Quick Table

| Operation / Structure                 | Best Time | Avg Time | Worst Time | Space |
|--------------------------------------|-----------|----------|------------|-------|
| Traverse entire tree (DFS or BFS)    | O(N)      | O(N)     | O(N)       | O(H) stack for DFS, up to O(N) queue for BFS |
| Height (recursive)                   | O(N)      | O(N)     | O(N)       | O(H) |
| Height via BFS                       | O(N)      | O(N)     | O(N)       | O(N) |
| Size                                 | O(N)      | O(N)     | O(N)       | O(H) |
| BST search (balanced)                | O(log N)  | O(log N) | O(N)       | O(1) |
| BST insert/delete (balanced)         | O(log N)  | O(log N) | O(N)       | O(1) |
| Expression eval                      | O(N)      | O(N)     | O(N)       | O(H) |
| Build from array (complete tree)     | O(N)      | O(N)     | O(N)       | O(N) |

Notes
- `H` is tree height  
- Balanced trees aim for `H` in `Theta(log N)`; skewed trees have `H` near `N-1`

---

## Extra ASCII Visuals (quick recall)

- Parent and subtrees
```
      P
    /   \
   L     R
  / \   / \
 LL LR RL RR

Subtree at L includes L, LL, LR
```

- Memory layout intuition
```
Stack (call frames)      Heap (nodes)
+------------------+     [node A] -> left -> [node B]
| postorder(n=A)   |     [node A] -> right -> [node C]
|  ...             |
+------------------+
```

- Inorder threaded idea (for Morris traversal mental model)
```
Make temporary link from rightmost of left subtree back to current
Visit when returning along that link
Remove link after use
```

---

## Common Pitfalls (Recap)
- Mismatched height conventions across helpers  
- Forgetting to null-check before pushing to stack or queue  
- Modifying pointers during traversal without a strategy  
- Not handling duplicate keys or comparator consistency in BST-like structures

## Pro Tips (Recap)
- Prefer iterative traversals in production hot paths  
- For BST range queries use inorder and prune subtrees by range bounds  
- Encapsulate traversal with a visitor or callback for reuse  
- Assert invariants after structural updates in debug builds

---
