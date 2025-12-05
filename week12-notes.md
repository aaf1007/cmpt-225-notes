# Week 12 Exam Notes

---

## TABLE OF CONTENTS

1. [**EVALUATING ARITHMETIC EXPRESSIONS**](#evaluating-arithmetic-expressions)
   - [The Problem](#the-problem)
   - [Basic Recursive Approach](#basic-recursive-approach)
   - [Running Time Analysis](#running-time-analysis-important)

2. [**LINEAR TIME EVALUATION (Using Stack)**](#linear-time-evaluation-using-stack)
   - [Infix Evaluation Algorithm](#infix-evaluation--linear-time-algorithm)

3. [**PREFIX AND POSTFIX NOTATIONS**](#prefix-and-postfix-notations)
   - [Prefix (Polish) Notation](#prefix-notation-polish-notation)
   - [Postfix (Reverse Polish) Notation](#postfix-notation-reverse-polish-notation)
   - [Finding Operands in Prefix](#finding-operand-in-prefix-notation)

4. [**EVALUATING PREFIX/POSTFIX IN LINEAR TIME**](#evaluating-prefixpostfix-in-linear-time)
   - [Prefix Stack Algorithm](#evaluating-prefix-stack-based)
   - [Postfix Stack Algorithm](#evaluating-postfix-stack-based)

5. [**BINARY TREES FOR EXPRESSIONS**](#binary-trees-for-expressions)
   - [Tree Representation](#tree-representation)
   - [Evaluating Expression Trees](#evaluating-expression-tree-recursive)
   - [Tree Traversals ↔ Notations](#tree-traversals--notations)

6. [**UNION-FIND DATA STRUCTURE**](#union-find-data-structure)
   - [Overview & Operations](#overview)
   - [Tree Representation](#tree-representation-1)
   - [Basic Implementation](#basic-implementation)
   - [Union by Rank](#union-by-rank)
   - [Key Property Proof](#key-property-union-by-rank--important-proof)
   - [Depth Bound Theorem](#depth-bound--critical-theorem)
   - [Valid vs Invalid Structures](#what-structures-are-possible)

7. [**PATH COMPRESSION OPTIMIZATION**](#path-compression-optimization)
   - [The Idea](#the-idea)
   - [Before/After Diagrams](#beforeafter-path-compression)
   - [Why Ranks Don't Change](#important-ranks-dont-change-after-compression)

8. [**UNION-FIND AMORTIZED COMPLEXITY**](#union-find-amortized-complexity)
   - [The log* Function](#the-log-iterated-logarithm-function)
   - [log* Value Table](#log-value-table)
   - [Amortized Complexity Theorem](#amortized-complexity-theorem)

9. [**MINIMUM SPANNING TREE (MST)**](#minimum-spanning-tree-mst)
   - [Problem Definition](#problem-definition)
   - [Kruskal's Algorithm](#kruskals-algorithm)
   - [Why Union-Find Works](#why-union-find-works-here)
   - [Running Time Analysis](#detailed-running-time-analysis)
   - [MST Example Trace](#mst-example-trace)

10. [**PRACTICE PROBLEMS**](#practice-problems-from-slides)
    - [Parentheses Validation](#parentheses-validation)
    - [Notation Conversions](#notation-conversions)
    - [Union-Find Trace Problems](#union-find-trace-problems)

11. [**KEY THEOREMS TO KNOW**](#key-theorems-to-know)

12. [**QUICK REFERENCE – COMPLEXITIES TABLE**](#quick-reference--key-complexities)

13. [**EXAM TIPS & THINGS TO MEMORIZE**](#exam-tips--things-to-memorize)

---

## EVALUATING ARITHMETIC EXPRESSIONS

---

### The Problem

**Goal:** Write a program to evaluate expressions like:
```
( ( ( 16 / ( 8 − ( 2 + 2 ) ) ) × 3) − ( 2 + ( 1 – 1 ) ) )
```

**Assumption:** All operations have parentheses (including outermost).

**Key Insight:** We need to:
1. Parse the expression to find operands
2. Recursively evaluate each operand
3. Apply the operator


### Basic Recursive Approach

**Algorithm:**
1. If first token is a number → return it (base case)
2. Find operand1
3. term1 = Evaluate(operand1) ← recursion
4. Find operand2
5. term2 = Evaluate(operand2) ← recursion
6. Apply operator on term1 and term2

**Q: How do we find where the first operand ends?**
- **Count parentheses:**
  - Skip outermost opening parenthesis
  - If token == '(' → count++
  - If token == ')' → count--
  - If count < 0 at any step → throw exception (invalid)
  - First operand ends when count == 0

**Example:** `( ( (5 + 11) / ( ( 5 + 3 ) - 4 ) ) − ( 2 + ( 1 – 1 ) ) )`
```
Skip outermost '('
Count: 1  2  1  2  3  2  1  0  ← operand1 ends here
       (  (     (  (        )
```


---

## RUNNING TIME ANALYSIS (IMPORTANT!)

---

### Recurrence Relation

**General formula:** If n = total length, k = length of first operand:
```
T(n) = O(k) + T(k) + T(n-k)
       ↑       ↑       ↑
    finding  eval    eval
    operand1 op1     op2
```

### Case 1: Right-Heavy Expression

**Expression:** `(1 + (1 + (1 + ... (1 + 1)...)))` with n summands

**Analysis:**
- First operand = just "1" → k = 1
- Finding it takes O(1)
- Recurrence: T(n) = T(1) + T(n-1) + O(1) = T(n-1) + O(1)
- **Solution: T(n) = O(n)** ✓ Linear!


### Case 2: Left-Heavy Expression

**Expression:** `(((...(1 + 1)...) + 1) + 1)` with n summands

**Analysis:**
- First operand = almost entire expression → k = n-1
- Finding it takes O(n) (must scan whole thing)
- Recurrence: T(n) = T(n-1) + T(1) + O(n) = T(n-1) + O(n)
- Expanding: T(n) = O(n) + O(n-1) + O(n-2) + ... + O(1)
- **Solution: T(n) = O(n²)** ✗ Quadratic!


### Optimization: Search from Both Ends

**Idea:** Search from front AND back simultaneously.
- Stop when either direction finds the boundary
- This guarantees k ≤ n/2 (we always find the shorter operand first)

**New recurrence:** T(n) = O(k) + T(k) + T(n-k), where k ≤ n/2

**Analysis of different k values:**

| k value | Recurrence | Solution |
|---------|------------|----------|
| k = 1 | T(n) = T(n-1) + O(1) | O(n) |
| k = n/2 | T(n) = 2T(n/2) + O(n) | O(n log n) |
| k = n/3 | T(n) = T(n/3) + T(2n/3) + O(n) | O(n log n) |
| k = √n | T(n) = T(√n) + T(n-√n) + O(√n) | O(n log n) |

**Theorem:** With this optimization, **worst case is O(n log n)** for any k ≤ n/2.

**Note:** The k = n/2 case is like merge sort recurrence!


---

## LINEAR TIME EVALUATION (Using Stack)

---

### Infix Evaluation – Linear Time Algorithm

**Algorithm:**
1. If see '(' → push to stack
2. If see operator or number → push to stack
3. If see ')' →
   - Pop last 4 items: `(`, `operand1`, `operator`, `operand2`
   - Evaluate and push result

**Example:** `( 15 / ( 7 - 2 ) )`

```
Read: (  15  /  (  7  -  2  )  )

Stack states:
[(]  →  [( 15]  →  [( 15 /]  →  [( 15 / (]  
→  [( 15 / ( 7]  →  [( 15 / ( 7 -]  →  [( 15 / ( 7 - 2]

See ')': pop (7-2), push 5 → [( 15 / 5]
See ')': pop (15/5), push 3 → [3]

Return 3
```

**Time Complexity:** O(n) – each token pushed/popped exactly once


---

## PREFIX AND POSTFIX NOTATIONS

---

### Prefix Notation (Polish Notation)

**Definition:** Operator comes BEFORE operands. No parentheses needed!

**Examples:**
| Infix | Prefix |
|-------|--------|
| `( 5 + 7 )` | `+ 5 7` |
| `( 8 * ( 2 + 4 ) )` | `* 8 + 2 4` |
| `( ( 3 + 5 ) * 4 )` | `* + 3 5 4` |


### Postfix Notation (Reverse Polish Notation)

**Definition:** Operator comes AFTER operands. No parentheses needed!

**Examples:**
| Infix | Postfix |
|-------|---------|
| `( 5 + 7 )` | `5 7 +` |
| `( 8 * ( 2 + 4 ) )` | `8 2 4 + *` |
| `( ( 3 + 5 ) * 4 )` | `3 5 + 4 *` |

**Key Observation:** If we have k numbers → we have k-1 operators


### Finding Operand in Prefix Notation

```
count = 0
skip first token (operator)
while count < 1 and tokens remain:
    read next token
    if number: count++
    if operator: count--
if count == 1: reached end of first operand
else: invalid expression
```


---

## EVALUATING PREFIX/POSTFIX IN LINEAR TIME

---

### Evaluating Prefix (Stack-Based)

**Algorithm:**
1. Read **right to left**
2. Push numbers onto stack
3. When reaching operator:
   - Pop top two terms
   - Apply operator
   - Push result

**Example:** `/ 15 - 7 + 2 2`  (represents `15 / (7 - (2+2))`)
```
Read R→L: 2, 2, +, 7, -, 15, /

[2] → [2,2] → [4] → [7,4] → [3] → [15,3] → [5]
      (2+2=4)      (7-4=3)          (15/3=5)
```


### Evaluating Postfix (Stack-Based)

**Algorithm:**
1. Read **left to right**
2. Push numbers onto stack
3. When reaching operator:
   - Pop top two terms (term2 first, then term1)
   - Apply: term1 operator term2
   - Push result

**Example:** `8 4 / 3 +`  (represents `(8/4) + 3`)
```
[8] → [8,4] → [2] → [2,3] → [5]
       (8/4=2)       (2+3=5)
```

**Code snippet (from ArithmeticExpressions.java):**
```java
public static double evaluatePostfix(String expression) {
    Stack<Double> stack = new Stack<Double>();
    StringTokenizer tokens = new StringTokenizer(expression);
    
    while (tokens.hasMoreTokens()) {
        String nextToken = tokens.nextToken();
        if (isNumber(nextToken)) {
            stack.push(Double.parseDouble(nextToken));
        }
        else if (isOperation(nextToken)) {
            double term2 = stack.pop();  // pop term2 first!
            double term1 = stack.pop();
            stack.push(applyOperator(term1, term2, nextToken));
        }
    }
    return stack.pop();
}
```


---

## BINARY TREES FOR EXPRESSIONS

---

### Tree Representation

- **Inner nodes** = operators
- **Leaves** = numbers

```
Expression: ( ( 5 * ( ( 9 + 4 ) - 1 ) ) / 6 )

        /
       / \
      *   6
     / \
    5   -
       / \
      +   1
     / \
    9   4
```


### Evaluating Expression Tree (Recursive)

```java
public static double evaluateTree(BTNode<Double> root) {
    if (root == null)
        throw new NullPointerException();
    else if (root.isLeaf())
        return root.getData();      // base case
    else {
        double term1 = evaluateTree(root.getLeftChild());
        double term2 = evaluateTree(root.getRightChild());
        return applyOperator(term1, term2, 
                            opsStr.get(root.getData().intValue()));
    }
}
```


### Tree Traversals ↔ Notations

| Traversal | Notation | Order |
|-----------|----------|-------|
| **Pre-order** | Prefix | Root → Left → Right |
| **In-order** | Infix | Left → Root → Right |
| **Post-order** | Postfix | Left → Right → Root |

**Converting tree to postfix:**
```java
public static String treeToPostfix(BTNode<Double> root) {
    if (root.isLeaf())
        return root.getData().toString();
    else {
        String term1 = treeToPostfix(root.getLeftChild());
        String term2 = treeToPostfix(root.getRightChild());
        return term1 + " " + term2 + " " + operator;
    }
}
```


---

## UNION-FIND DATA STRUCTURE

---

### Overview

**Purpose:** Maintain collection of disjoint sets with efficient operations.

**Operations:**
| Operation | Description |
|-----------|-------------|
| **makeSet(x)** | Create set containing only x |
| **find(x)** | Return name/ID of set containing x |
| **union(i,j)** | Merge set containing i with set containing j |

**Key property:** If i,j in same set → find(i) == find(j)


### Tree Representation

- Each set = a tree
- Each element has pointer to parent
- Root's parent = null
- **find(x)** returns root of tree containing x

```
Sets: {a}  {b,c}  {d,e,f,g,h,i,j}

    a       b         d
           /        / | \
          c        f  g  e
                   |    /|\
                   h   i j
```


### Basic Implementation

**makeSet:**
```java
public static UnionFindTicket makeSet(Object data) {
    Node node = new Node(data);
    node.parent = null;
    node.rank = 0;
    return node;
}
```

**find (basic):**
```java
public static UnionFindTicket find(UnionFindTicket node) {
    Node current = (Node) node;
    while (current.parent != null)
        current = current.parent;
    return current;  // return root
}
```


### Union by Rank

**Idea:** Attach shorter tree under root of taller tree to minimize depth.

```java
public static void union(UnionFindTicket u, UnionFindTicket v) {
    Node ru = (Node) find(u);  // root of u
    Node rv = (Node) find(v);  // root of v
    
    if (ru.getRank() > rv.getRank())
        rv.setParent(ru);           // ru becomes root
    else if (ru.getRank() < rv.getRank())
        ru.setParent(rv);           // rv becomes root
    else {  // ranks equal
        ru.setParent(rv);           // rv becomes root
        rv.setRank(rv.getRank() + 1);  // increase rank
    }
}
```


### Key Property (Union by Rank) – IMPORTANT PROOF

**Theorem:** If rank(v) = k, then v has at least **2^k** vertices under it.

**Proof by induction on k:**

**Base case (k = 0):**
- makeSet() creates node with rank = 0
- Node has 2⁰ = 1 vertex (itself) ✓

**Inductive hypothesis:** Assume true for all ranks < k.

**Inductive step:** Consider a node v with rank = k. How did v get rank k?
- Rank only increases in union when two roots have EQUAL rank
- So v got rank k when two rank-(k-1) trees were merged
- By IH, each had ≥ 2^(k-1) vertices
- Combined: ≥ 2^(k-1) + 2^(k-1) = 2^k vertices ✓

**Alternative case:** If ranks were different during union:
- Ranks don't change, but vertices increase
- Property still holds ✓

**QED**


### Depth Bound – CRITICAL THEOREM

**Theorem:** If there are n vertices total, then max rank (and depth) ≤ log₂(n).

**Proof:**
- If rank(v) = k, then v has ≥ 2^k vertices (by previous theorem)
- Total vertices = n
- So 2^k ≤ n
- Therefore k ≤ log₂(n) ✓

**Corollary:** find(x) and union(u,v) both run in **O(log n)** time.
- find: traverses path of length ≤ log n
- union: calls find twice + O(1) work = O(log n)


### What Structures Are Possible?

**Valid structure:**
```
     d (rank 2)
    /|\
   f g e
   |
   h
```
Built by: union(e,d), union(h,f), union(f,d)

**INVALID structure 1:**
```
     1 (rank 2)
    /|\
   2 3 (only 3 vertices, but rank 2 needs ≥ 4)
```
Why invalid: rank 2 requires ≥ 2² = 4 vertices!

**INVALID structure 2:**
```
     X
     |
     Y
    / \
   Z   W
```
Why invalid: If Z,W are under Y, they were added before X joined. But then how did X get above Y? Contradiction!


---

## PATH COMPRESSION OPTIMIZATION

---

### The Idea

**Principle:** "A little extra effort in routine maintenance pays off in the long run."

**Observation:** During find(x), we traverse the path from x to root. We can "compress" this path by making all nodes point directly to the root.

**find with path compression:**
```java
public static UnionFindTicket find(UnionFindTicket node) {
    Node current = (Node) node;
    
    if (current.parent == null)  // current is root
        return current;
    else {
        Node root = (Node) find(current.getParent());  // recurse to find root
        current.setParent(root);   // PATH COMPRESSION: point directly to root
        return root;
    }
}
```

**Alternative (iterative) approach:**
```
find(x):
    // First pass: find the root
    current = x
    while (current.parent != null)
        current = current.parent
    root = current
    
    // Second pass: compress path
    for each node on path from x to root:
        node.parent = root
    
    return root
```


### Before/After Path Compression

```
Before find(c):              After find(c):
                           
     d                           d
    /|\                       /|\ \ \
   f g e                     f g e b c
   |  /|\                    |  /|\
   h i j b                   h i j
          \
           c
```

**Benefits:**
- Next find(c) = O(1) — direct pointer to root!
- find on any ancestor of c also faster
- Cumulative effect: tree becomes very flat over time


### IMPORTANT: Ranks Don't Change After Compression

**Key observations:**
1. makeSet(x) makes x a root (of itself)
2. Once a node stops being a root (due to union), it will **never be a root again**
3. A non-root node's rank **never changes**

**Why this matters:**
- Ranks are assigned during union operations only
- Path compression only changes parent pointers, not ranks
- So ranks become "historical" — they record what the height WAS, not what it IS

**Example:**
```
Before compression:          After compression:
d.rank = 3, height = 3       d.rank = 3, height = 2
b.rank = 1, height = 1       b.rank = 1, height = 0

Ranks unchanged! Only heights changed.
```

**This is why it's called "rank" not "height"!**


### Why Path Compression is Safe

**Theorem:** Union-by-rank properties still hold after path compression.

**Proof idea:**
- The rank property (rank k → ≥ 2^k descendants) depends on how trees were merged
- Path compression doesn't add/remove nodes, just reorganizes pointers
- The original rank assignments remain valid upper bounds
- Union decisions still use original ranks → still make good choices


---

## UNION-FIND AMORTIZED COMPLEXITY

---

### The log* (Iterated Logarithm) Function

**Definition:** log*(n) = number of times you apply log₂ until result is < 2

**Formal definition:**
```
log*(n) = 0                  if n ≤ 1
log*(n) = 1 + log*(log₂(n))  if n > 1
```

**Step-by-step examples:**

**Example 1: log*(16)**
```
log₂(16) = 4
log₂(4) = 2
log₂(2) = 1 < 2 ✓
Applied 3 times → log*(16) = 3
```

**Example 2: log*(65,536) = log*(2^16)**
```
log₂(65,536) = 16
log₂(16) = 4
log₂(4) = 2
log₂(2) = 1 < 2 ✓
Applied 4 times → log*(65,536) = 4
```

**Example 3: log*(2^65,536)**
```
log₂(2^65,536) = 65,536
... then same as above ...
Applied 5 times → log*(2^65,536) = 5
```

### log* Value Table

| n | log*(n) |
|---|---------|
| 1 | 0 |
| 2 | 1 |
| 3, 4 | 2 |
| 5 to 16 | 3 |
| 17 to 65,536 | 4 |
| 65,537 to 2^65,536 | 5 |

**CRITICAL INSIGHT:** log*(n) ≤ 5 for ANY number that could represent something in the physical universe!
- Number of atoms in universe ≈ 10^80 < 2^265 → log* ≈ 5
- For all practical purposes, log*(n) is essentially constant!


### Amortized Complexity Theorem

**Theorem:** For a sequence of operations:
- makeSet() — n times
- find() — O(n) times  
- union() — at most n-1 times

**Total running time = O(n · log*(n))**

**What this means:**
- Average cost per operation = O(log*(n)) ≈ O(1) in practice!
- This is called "amortized" analysis — average over sequence of operations
- Some operations may be slow, but they speed up future operations

**Comparison:**
| Implementation | Time per operation |
|----------------|-------------------|
| No optimization | O(n) worst case |
| Union by rank only | O(log n) |
| Union by rank + path compression | **O(log*(n))** amortized |

**Note:** The proof is complex (covered in CMPT307/CMPT405). Key idea is that path compression "pays for itself" by making future operations faster.


---

## MINIMUM SPANNING TREE (MST)

---

### Problem Definition

**Input:** 
- Undirected graph G = (V, E)
- Edge costs {cₑ : e ∈ E}

**Output:** 
- Spanning tree T of minimum total cost

**Spanning Tree Definition:**
- T ⊆ E with exactly **|V| - 1 edges**
- Graph (V, T) is **connected**

**Cost of spanning tree:** Σ cₑ for all e ∈ T


### Why |V| - 1 Edges?

**Theorem:** A tree with n vertices has exactly n-1 edges.

**Intuition:** 
- Start with n isolated vertices (0 edges)
- Each edge added connects two components into one
- Need n-1 edges to go from n components to 1 component


### Kruskal's Algorithm

**Greedy approach:** Always pick lightest edge that doesn't create a cycle.

```
Kruskal(G, costs):
    T = empty set
    Sort edges by cost (non-decreasing)
    
    For each vertex v ∈ V:
        makeSet(v)
    
    For each edge e=(u,v) in sorted order:
        if find(u) ≠ find(v):    // different components
            union(u,v)
            add e to T
    
    return T
```

**Key insight:** Edge (u,v) creates cycle ⟺ u and v already in same component


### Why Union-Find Works Here

**Observation:** At any point during Kruskal's:
- Each tree in the forest = one connected component
- Two vertices in same tree ⟺ same connected component

**Using Union-Find:**
- Each component = one set in union-find
- find(u) == find(v) → same component → edge creates cycle → SKIP
- find(u) ≠ find(v) → different components → edge is safe → ADD, then union(u,v)


### Detailed Running Time Analysis

**Let n = |V| (vertices), m = |E| (edges)**

**Naive approach (using BFS to check cycles):**
```
For each of n-1 edges added:
    Scan all m edges to find minimum: O(m)
    Run BFS to check if creates cycle: O(n)
Total: O(n) × O(m) × O(n) = O(n²m)
```

**Improvement 1: Sort edges first**
```
Sort m edges: O(m log m)
For each of m edges:
    Run BFS to check cycle: O(n)
Total: O(m log m) + O(mn) = O(mn)
```

**Improvement 2: Use Union-Find (BEST)**
```
Sort m edges: O(m log m)
n makeSet operations: O(n)
For each of m edges:
    2 find operations: O(log n) each → O(log n)
    possibly 1 union: O(log n)
Total for m edges: O(m log n)

Grand total: O(m log m) + O(m log n)
```

**Final complexity:** Since m ≤ n² for simple graphs:
- log m ≤ log(n²) = 2 log n = O(log n)
- So O(m log m) = O(m log n)
- **Final: O(m log m)** or equivalently **O(m log n)**


### With Path Compression

**Even better (theoretical):**
- Using union by rank + path compression
- m union-find operations take O(m log*(n))
- Sorting still dominates: O(m log m)
- **Total still O(m log m)** but with better constant factors

**Special case:** If edges are pre-sorted (or can be sorted in O(m) time):
- Total becomes O(m log*(m)) ≈ O(m) — essentially linear!


### MST Example Trace

```
Graph:                    Sorted edges:
  a---1---c               (a,c): 1
  |\ 2   /|               (d,g): 1
  2  \  / 1               (b,d): 1
  |   \/  |               (a,b): 2
  b-1-d-7-e               (c,e): 1
      |                   (a,d): 2
      3                   (c,f): 2
      |                   (e,f): 3
      f---5---g           (d,e): 7
      
Process:
1. (a,c): 1  → find(a)≠find(c) → ADD, union(a,c)
2. (d,g): 1  → find(d)≠find(g) → ADD, union(d,g)
3. (b,d): 1  → find(b)≠find(d) → ADD, union(b,d)
4. (a,b): 2  → find(a)≠find(b) → ADD, union(a,b)
5. (c,e): 1  → find(c)≠find(e) → ADD, union(c,e)
6. (a,d): 2  → find(a)==find(d) → SKIP (would create cycle!)
7. (c,f): 2  → find(c)≠find(f) → ADD, union(c,f)

MST has 7 edges (n-1 = 8-1 = 7) ✓
```


---

## PRACTICE PROBLEMS (From Slides)

---

### Parentheses Validation

**Problem:** Check if sequence of '(' and ')' is legal.

**Algorithm:**
```
count = 0
for each token:
    if '(': count++
    if ')': count--
    if count < 0: return ILLEGAL
if count == 0: return LEGAL
else: return ILLEGAL
```

**Examples:**
- `( ( ) )` → counts: 1,2,1,0 → LEGAL
- `( ) ) (` → counts: 1,0,-1 → ILLEGAL (count went negative)


### Notation Conversions

**Practice converting between:**
- Prefix ↔ Postfix (use stack-based approach)
- Infix ↔ Prefix (build tree, then traverse)
- Prefix ↔ Infix (build tree, then traverse)

**Hint:** Build expression tree first, then:
- Pre-order traversal → Prefix
- In-order traversal → Infix  
- Post-order traversal → Postfix


### Union-Find Trace Problems

**Given sequence of operations, trace:**
1. Tree structure after each union
2. Rank of each node
3. Effect of path compression on find

**Example to trace:**
```
makeSet(a), makeSet(b), makeSet(c), makeSet(d), makeSet(e)
union(a,b)   // What happens?
union(c,d)   // What happens?
union(a,c)   // Which root becomes parent?
find(b)      // What path is traversed? Compressed?
```


---

## KEY THEOREMS TO KNOW

---

1. **Expression evaluation recurrence:**
   T(n) = O(k) + T(k) + T(n-k)
   - k=1 → O(n)
   - k=n-1 → O(n²)
   - k≤n/2 (optimized) → O(n log n)

2. **Arithmetic expression observation:**
   In any valid expression: # operators = # numbers - 1

3. **Union-Find rank theorem:**
   rank(v) = k → v has ≥ 2^k descendants
   
4. **Union-Find depth bound:**
   n vertices → max depth ≤ log₂(n)

5. **Path compression invariant:**
   Ranks never change after a node becomes non-root

6. **Amortized complexity:**
   n makeSet + O(n) find/union → O(n log*(n)) total

7. **Spanning tree property:**
   n vertices → exactly n-1 edges


---

## QUICK REFERENCE – KEY COMPLEXITIES

---

| Algorithm/Operation | Time Complexity |
|---------------------|-----------------|
| Infix eval (naive recursive, worst) | O(n²) |
| Infix eval (optimized recursive) | O(n log n) |
| Infix eval (stack-based) | **O(n)** |
| Prefix/Postfix eval (stack) | **O(n)** |
| Union-Find find (no optimization) | O(n) worst |
| Union-Find (union by rank only) | O(log n) |
| Union-Find (rank + path compression) | **O(log*(n))** amortized |
| Kruskal's MST (naive) | O(n²m) |
| Kruskal's MST (sort + BFS) | O(nm) |
| Kruskal's MST (sort + union-find) | **O(m log m)** |


---

## EXAM TIPS & THINGS TO MEMORIZE

---

### Expression Evaluation
- Know how to **trace stack operations** for all three notations
- Prefix: read RIGHT to LEFT, push numbers, pop 2 on operator
- Postfix: read LEFT to RIGHT, push numbers, pop 2 on operator
- Infix: push '(' and operators, pop 4 items on ')'

### Union-Find
- **Rank heuristic:** attach shorter tree under taller tree's root
- **Path compression:** make all nodes on find path point to root
- **Why rank not height:** ranks fixed after node becomes non-root
- **2^k property:** rank k needs at least 2^k nodes (know the proof!)
- **log*(n) ≤ 5** for all practical n (memorize the table!)

### MST
- **Spanning tree:** exactly |V|-1 edges, connected
- **Kruskal's:** sort edges, greedily add if no cycle
- **Cycle detection:** same component = find(u) == find(v)
- Know the **three complexity levels:** O(n²m) → O(nm) → O(m log m)

### Key Proof Ideas
1. **Rank k → 2^k nodes:** Induction. Equal ranks merge → double nodes, increase rank.
2. **Depth ≤ log n:** If rank k requires 2^k nodes, and we have n nodes, then k ≤ log n.
3. **Why path compression is safe:** Only parent pointers change, not ranks. Union decisions still valid.