# CMPT 225 — Week 06 Trees & BSTs (Cheat Sheet with Extra Diagrams + Time/Space)

---

## Binary Trees — core facts
- **Binary tree**: each node has ≤ 2 children (left/right).  
- **Level bound**: level `k` has ≤ `2^k` nodes ⇒ depth `d` has ≤ `2^(d+1) − 1` total nodes.  
- **Typical node type (minimal)**
  ```java
  class BTNode<T> {
    T data; BTNode<T> left, right, parent;
    BTNode(T d){ data=d; }
    void setLeft(BTNode<T> c){ left=c; if(c!=null) c.parent=this; }
    void setRight(BTNode<T> c){ right=c; if(c!=null) c.parent=this; }
  }
  ```

### Shape intuition
```
Balanced (height ~ log N)          Path-like (height ~ N-1)
        o                                   o
      /   \                                   \
     o     o                                   o
    / \   / \                                   \
   o  o  o  o                                   o  ...
```

---

## Traversals (definitions, examples, code patterns)

### What each traversal does
| Traversal | Visit order | Output on BST `(10,(5,(1,7)),(21,(16,25)))` |
|---|---|---|
| **Pre-order** | Root → Left → Right | `10, 5, 1, 7, 21, 16, 25` |
| **In-order** | Left → Root → Right | `1, 5, 7, 10, 16, 21, 25` |
| **Post-order** | Left → Right → Root | `1, 7, 5, 16, 25, 21, 10` |
| **BFS (Level-order)** | Level by level | `10, 5, 21, 1, 7, 16, 25` |

**Reference BST**
```
        10
       /  \
      5    21
     / \   / \
    1   7 16 25
```

#### Visual walk-throughs
_Pre-order (Root → L → R)_
```
Visit: 10 → 5 → 1 → 7 → 21 → 16 → 25
```
_In-order (L → Root → R)_
```
Left(10)=[1,5,7]  Root=10  Right(10)=[16,21,25] → sorted
```
_Post-order (L → R → Root)_
```
Children first, then node → 1,7,5,16,25,21,10
```
_BFS / Level-order_
```
L0: [10]
L1: [5, 21]
L2: [1, 7, 16, 25]
```

### Minimal recursive pattern (move one line)
```java
void pre(BTNode<Integer> r){ if(r==null) return;
  System.out.println(r.data); pre(r.left); pre(r.right);
}
void in(BTNode<Integer> r){ if(r==null) return;
  in(r.left); System.out.println(r.data); in(r.right);
}
void post(BTNode<Integer> r){ if(r==null) return;
  post(r.left); post(r.right); System.out.println(r.data);
}
```

### Iterative Pre-order (stack)
```java
void preIter(BTNode<Integer> root){
  if(root==null) return;
  java.util.Deque<BTNode<Integer>> s=new java.util.ArrayDeque<>();
  s.push(root);
  while(!s.isEmpty()){
    BTNode<Integer> n=s.pop();
    System.out.println(n.data);
    if(n.right!=null) s.push(n.right);   // push right first
    if(n.left!=null)  s.push(n.left);    // so left is processed next
  }
}
```

### BFS / Level-order (queue)
```java
void bfs(BTNode<Integer> root){
  if(root==null) return;
  java.util.Queue<BTNode<Integer>> q=new java.util.ArrayDeque<>();
  q.add(root);
  while(!q.isEmpty()){
    BTNode<Integer> n=q.remove();
    System.out.println(n.data);
    if(n.left!=null)  q.add(n.left);
    if(n.right!=null) q.add(n.right);
  }
}
```

**Iterative mental models**
```
Pre stack (push R then L):      In stack (left spine):        BFS queue:
Top → [R,L,...]                  push lefts to null            pop ← [ .. root .. ] ← push children
```

---

## Size & Height (rec/iter)
```java
int size(BTNode<?> r){
  return (r==null)?0:1+size(r.left)+size(r.right);
}
int height(BTNode<?> r){       // edges on longest path
  return (r==null)?-1:1+Math.max(height(r.left),height(r.right));
}
```
```java
int sizeIter(BTNode<?> root){
  if(root==null) return 0;
  java.util.Queue<BTNode<?>> q=new java.util.ArrayDeque<>();
  q.add(root); int cnt=0;
  while(!q.isEmpty()){
    BTNode<?> n=q.remove(); cnt++;
    if(n.left!=null)  q.add(n.left);
    if(n.right!=null) q.add(n.right);
  }
  return cnt;
}
int heightIter(BTNode<?> root){
  if(root==null) return -1;
  java.util.Queue<BTNode<?>> q=new java.util.ArrayDeque<>();
  q.add(root); int h=-1;
  while(!q.isEmpty()){
    for(int i=q.size(); i>0; --i){
      BTNode<?> n=q.remove();
      if(n.left!=null)  q.add(n.left);
      if(n.right!=null) q.add(n.right);
    }
    h++;
  }
  return h;
}
```

---

## Binary Search Trees (BST)

### Definition & invariant
```
For every node x:
   keys(left(x)) ≤ x.key ≤ keys(right(x))   (for all descendants)
```
_In-order_ yields sorted keys.  
**Duplicates policy used:** equal keys go **to the right**.

### Find / Insert / Remove (generic shapes)
```java
// FIND (iterative)
<T extends Comparable<T>> BTNode<T> find(BTNode<T> r, T x){
  while(r!=null){
    int c = x.compareTo(r.data);
    if(c==0) return r;
    r = (c<0) ? r.left : r.right;
  }
  return null;
}

// INSERT (duplicates → right)
<T extends Comparable<T>> BTNode<T> insert(BTNode<T> r, T x){
  if(r==null) return new BTNode<>(x);
  if(x.compareTo(r.data)<0) r.setLeft(insert(r.left,x));
  else                      r.setRight(insert(r.right,x));
  return r;
}

// REMOVE (3 cases)
<T extends Comparable<T>> BTNode<T> min(BTNode<T> n){
  while(n.left!=null) n=n.left; return n;
}
<T extends Comparable<T>> BTNode<T> remove(BTNode<T> r, T x){
  if(r==null) return null;
  int c = x.compareTo(r.data);
  if(c<0) r.setLeft(remove(r.left,x));
  else if(c>0) r.setRight(remove(r.right,x));
  else{
    if(r.left==null)  return r.right;
    if(r.right==null) return r.left;
    BTNode<T> s=min(r.right);   // successor
    r.data = s.data;
    r.setRight(remove(r.right, s.data));
  }
  return r;
}
```

#### Remove cases — diagrams
_Leaf (0 children)_
```
Parent ──> [X]        ⇒  Parent ──> null
```
_One child_
```
Parent ──> [X]
           /
         [C]        ⇒  Parent ──> [C]
```
_Two children (copy successor, then delete it)_
```
     [X]                       [S]
    /   \         copy S       /  \
  ...   [R]      ─────────>   ...  [R*] (S removed from R)
       /
     [S=min(R)]
```

#### Successor / Predecessor — diagrams
```
Successor(x): if right!=null → min(right)
Else go up: first ancestor where x is in its LEFT subtree
```
```
Predecessor(x): if left!=null → max(left)
Else go up: first ancestor where x is in its RIGHT subtree
```

---

## Build a balanced tree from a sorted array (median-first)
```java
<T extends Comparable<T>> BTNode<T> fromSorted(java.util.List<T> a,int lo,int hi){
  if(lo>hi) return null;
  int mid=(lo+hi)/2;
  BTNode<T> r=new BTNode<>(a.get(mid));
  r.setLeft (fromSorted(a,lo,mid-1));
  r.setRight(fromSorted(a,mid+1,hi));
  return r;
}
```
_Build shape_
```
Array: [1, 5, 7, 10, 16, 21, 25]
             ^
            (10)
           /    \
        (5)      (21)
       /  \      /  \
     (1)  (7) (16)  (25)
```

---

## AVL Trees (self-balancing BST)

### Balance rule & height
```
∀ node v: |height(v.left) − height(v.right)| ≤ 1
Height = O(log N)  (via Fibonacci lower bound on size)
```

### Detect & fix (rebalance up the path)
```
Let z be first unbalanced node on the path up (±2)
Pick the child y = taller child of z
Pick the grandchild x = taller child of y (tie → same-side)
Case by pattern of (z,y,x): LL, RR, LR, RL → apply rotations
```

### Rotation diagrams
_LL (Right rotate)_
```
      z                 y
     /                 / \
    y       ---->     x   z
   /
  x
```

_RR (Left rotate)_
```
  z                     y
   \                   / \
    y      ---->      z   x
     \
      x
```

_LR (Left on child, Right on z)_
```
    z                  z                  x
   /                  /                  / \
  y       ---->     (x)     ---->       y   z
   \                /                  (subtrees reattached)
    x              y
```

_RL (Right on child, Left on z)_
```
  z                    z                   x
   \                  /                   / \
    y     ---->     (x)       ---->      z   y
   /                                       
  x
```

**Minimal AVL insert (shape)**
```java
// Insert like BST, then rebalance up:
AVLNode insert(AVLNode n, K k):
  if n==null: return new AVLNode(k)
  if k < n.key:  n.left  = insert(n.left, k)
  else:          n.right = insert(n.right,k)
  updateHeight(n)
  return rebalance(n)   // apply LL/RR/LR/RL as needed
```

---

## 2–3 Trees (perfectly balanced)
**Properties**
- Internal nodes: **2 or 3** children; **1 or 2** keys.  
- **All leaves at same depth** ⇒ strictly balanced ⇒ `Θ(log N)` height.

_Split on insert_
```
Leaf [A,B,C]  → promote B
        B
      /   \
    [A]   [C]
```

_Borrow / Merge on delete_
```
Borrow:          Merge:
 [K|   ] ←→ [L|M]        [K]  [L]   →   [K|L]
   ↑ take L or M         (pull key down from parent if needed)
```

---

## B-Trees (order m)
**Node capacity**
```
Root: 2..m children
Internal (non-root): ceil(m/2)..m children
Leaves: same depth
```
_Why_: external-memory friendly (one node ≈ one disk block).

_Insert split (node with too many keys)_
```
[ a | b | c | d ]  (too many)
          ↓ promote median (b or c)
     parent gets key; children split around it
```

_Delete underflow_
```
Borrow from rich sibling if possible; else merge with sibling and pull parent key down
(recursive up as needed)
```

---

## **Time & Space Complexity (Cheat Table)**

### Traversals & Basic Utilities
| Operation | Time | Extra Space |
|---|---|---|
| **Pre/In/Post (recursive)** | `Θ(N)` | recursion stack `O(H)` |
| **Pre (iterative, stack)** | `Θ(N)` | `O(H)` stack (worst `O(N)`) |
| **In (iterative, stack)** | `Θ(N)` | `O(H)` stack |
| **Post (two stacks)** | `Θ(N)` | `O(N)` in worst-case (2 stacks total `O(N)`) |
| **BFS / Level-order** | `Θ(N)` | queue up to width `O(W)` (worst `O(N)`) |
| **Size/Height (rec)** | `Θ(N)` | recursion `O(H)` |
| **Size/Height (iter BFS)** | `Θ(N)` | queue `O(W)` |

`N` = nodes, `H` = height, `W` = max width.

### BST (unbalanced)
| Operation | Best | Average (random) | Worst | Extra Space |
|---|---|---|---|---|
| **find / insert / remove** | `Ω(log N)` | `Θ(log N)` | `Θ(N)` | recursive: `O(H)`; iterative: `O(1)` |

> Height `H` can degrade to `N−1` if keys arrive sorted.

### Build / Conversions
| Task | Time | Extra Space |
|---|---|---|
| **Build from Pre+In (distinct keys)** | `Θ(N)` (with hashmap of inorder indices) | recursion `O(H)` + hashmap `O(N)` |
| **Build balanced from sorted array** | `Θ(N)` | recursion `O(H)` |
| **Copy / Serialize tree (pre-order)** | `Θ(N)` | `O(H)` |

### AVL (balanced BST)
| Operation | Time | Extra Space | Notes |
|---|---|---|---|
| **find** | `Θ(log N)` | `O(1)` iterative or `O(log N)` recursion | height bounded |
| **insert** | `Θ(log N)` | `O(1)` extra (rotations constant) | ≤ 2 rotations |
| **remove** | `Θ(log N)` | `O(1)` extra | ≤ 2 rotations per unbalance on path |
| **height bound** | `≤ c·log N` | — | from Fibonacci lower bound |

### 2–3 Trees
| Operation | Time | Space (extra) | Notes |
|---|---|---|---|
| **search / insert / delete** | `Θ(log N)` | `O(1)` amortized | perfect balance; split/merge may cascade |

### B-Trees (order `m`)
| Operation | Time | Node I/O | Notes |
|---|---|---|---|
| **search / insert / delete** | `Θ(logₘ N)` comparisons | `O(logₘ N)` blocks | split/borrow/merge amortized `O(1)` per update |

### Representation Space
| Structure | Space |
|---|---|
| **Pointer-based binary tree** | `Θ(N)` nodes; each node `O(1)` pointers & key |
| **With parent pointers** | still `Θ(N)` (constant factor ↑) |
| **Arrays for complete BT (heap)** | `O(N)` contiguous array; parent/child via indices |

---

## Practice cues
- Build BSTs from insertion sequences; **draw shape** + **in-order** to verify.  
- Reconstruct BST from **pre-order** (unique for BST).  
- Track **duplicate policy** (here: equal → right).  
- Augment nodes (e.g., `subtreeSize`) if you need `rank/select` in `Θ(log N)`.

---

## Complexity summary (at a glance)
```
Traversals: Θ(N) time, O(H) or O(W) space
BST ops:   Θ(height) → Θ(log N) average, Θ(N) worst
AVL ops:   Θ(log N) time, O(1) extra, rotations constant
2–3/B-Tree: Θ(log N) (or logₘN) with strict balance / block I/O efficiency
```