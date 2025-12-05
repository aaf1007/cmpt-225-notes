# Week 9

---

## 1. Graph Model and Terminology

- We assume an **unweighted graph**:
  - Every edge costs the same (cost = 1 move).
  - So **distance** = number of moves.

- A graph is written as:  
  **G = (V, E)**  
  - **V** = set of vertices (nodes / states).
  - **E** = set of edges (possible moves between states).

- We’re usually given:
  - A **start** node.
  - A **target / goal** node.

- Typical data we store during search:
  - **visited[ ]**
    - Boolean flag: have we already discovered this node?
    - Prevents infinite loops and redundant work.
  - **parent[ ]**
    - `parent[x]` = which node first discovered `x`.
    - Lets us reconstruct a path from start to any reachable node.

- How `parent[ ]` gives you the path:
  - Start from the goal node.
  - Repeatedly follow `parent[node]` backward until you reach the start.
  - Reverse that chain: that’s your path from start → goal.
  - Works because these parent pointers form a tree rooted at the start. 


---

## 2. Breadth First Search (BFS)

### 2.1 What BFS Does

- **BFS = explore the graph in layers of increasing distance from the start.**

- Layer 0: the start node  
- Layer 1: all nodes that are 1 move away  
- Layer 2: all nodes that are 2 moves away  
- Layer 3: all nodes that are 3 moves away  
- etc.

- BFS computes:
  - The **shortest distance** (fewest edges) from `start` to every reachable node.
  - A **shortest path** from `start` to a specific node.

- Mental model:  
  - BFS is like a ripple spreading outward equally in all directions.

### 2.2 BFS Algorithm (Queue-Based)

    BreadthFirstSearch(start):
        create an empty Queue q
        mark all nodes as unvisited
        mark start as visited
        parent[start]   = null        // start has no parent
        distance[start] = 0           // optional
        q.enqueue(start)

        while q is not empty:
            v = q.dequeue()
            // PROCESS v if needed (print, log, etc.)

            for each neighbor u of v:
                if u is unvisited:
                    mark u as visited
                    parent[u]   = v
                    distance[u] = distance[v] + 1
                    q.enqueue(u)

Key details:
- We **mark a node visited as soon as we enqueue it**, NOT when we later dequeue it.
  - This prevents enqueuing the same node multiple times.
- We set:
  - `parent[u] = v` the first time we ever discover `u`.
  - This forms what we call the **BFS tree**.

### 2.3 The BFS Tree

- While BFS runs, each new node `u` that we discover from `v` gets `parent[u] = v`.

- All these parent pointers together form a **tree rooted at `start`**:
  - This is called the **BFS tree**.

- IMPORTANT PROPERTY:
  - For any node `u`,  
    `distance(start, u)` in the original graph  
    = `depth of u in the BFS tree`.

- So:
  - The BFS tree doesn’t just give a path.
  - It gives the **shortest path(s)** from `start` to all reachable nodes.

- You can reconstruct a shortest path to any node by following `parent[ ]` backward from that node to `start`, then reversing.

### 2.4 Why BFS Guarantees Shortest Paths (Unweighted Graph)

- BFS uses a **queue (FIFO)**:
  - You first enqueue the start node.
  - Then you enqueue all neighbors (distance 1).
  - Then you enqueue all nodes at distance 2.
  - Then all nodes at distance 3.
  - etc.

- Because of that:
  - When BFS **first visits** a node `u`, it has already found the **minimum number of edges** needed to reach `u`.
  - No later discovery can beat that, since later discoveries are from farther layers.

- Therefore:  
  BFS = shortest path finder for unweighted graphs.  
  (This is one of its biggest uses.)

### 2.5 BFS Complexity

Let:
- `|V|` = number of vertices (states).
- `|E|` = number of edges (moves).

**Time Complexity:**
- With an adjacency **list** representation:
  - We visit each vertex once.
  - We inspect each edge a constant number of times.
  - Runtime is **O(|V| + |E|)**.
- With an adjacency **matrix** representation:
  - For each of the `|V|` vertices, we scan all `|V|` possible neighbors.
  - Runtime is **O(|V|²)**.

**Space Complexity:**
- The queue can hold up to **O(|V|)** nodes in the worst case.
- We store:
  - `visited[ ]`  → O(|V|)
  - `parent[ ]`   → O(|V|)
  - `distance[ ]` → O(|V|) if we track it
- Total auxiliary space: **O(|V|)**

### 2.6 BFS Exploration Shape (Layered View)

    start
     ├─ dist 1 nodes
     │    ├─ dist 2 nodes
     │    │    └─ dist 3 nodes
     │    └─ dist 2 nodes
     └─ dist 1 nodes
          └─ dist 2 nodes

- BFS expands level by level: distance 0, then distance 1, then distance 2, etc.
- Summary:
  - BFS is the right tool when you want the **fewest number of moves** from start to goal in an unweighted graph.

---

## 3. Depth First Search (DFS)

### 3.1 What DFS Does

- **DFS = explore one path as deep as possible before backtracking.**

- Process:
  - Pick a neighbor.
  - Go to that neighbor.
  - Then go to *its* neighbor.
  - Keep going until you get stuck.
  - Then backtrack and try a different branch.

- So:
  - BFS spreads “wide.”
  - DFS dives “deep.”

- Use cases for DFS:
  - Check if **any** path exists from start to goal.
  - Explore structure like connected components, detect cycles, etc.

- DFS **does not guarantee shortest path** in terms of number of steps.

### 3.2 DFS Algorithm (Iterative, Using a Stack)

    DepthFirstSearch(start):
        create an empty Stack s
        mark all nodes as unvisited
        mark start as visited
        parent[start] = null
        s.push(start)

        while s is not empty:
            v = s.pop()
            // PROCESS v here if needed (print, etc.)

            for each neighbor u of v:
                if u is unvisited:
                    mark u as visited
                    parent[u] = v
                    s.push(u)

Key contrast with BFS:
- BFS uses a **Queue** (FIFO).
- DFS uses a **Stack** (LIFO).

But other than that difference, the high-level loop looks very similar.

### 3.3 Recursive DFS (Same Logic, Call Stack = Stack)

    DFS(v):
        mark v as visited
        // PROCESS v here if needed

        for each neighbor u of v:
            if u is unvisited:
                parent[u] = v
                DFS(u)

- The recursion gives you the same behavior as manually managing a stack.
- `parent[ ]` can still be recorded.

### 3.4 DFS Tree

- Just like BFS, we can track `parent[u] = v` when we discover `u` from `v`.
- This creates a **DFS spanning tree** rooted at the start node.
- BUT:
  - The depth of a node in the DFS tree is **not** guaranteed to equal its shortest-path distance from the start.
  - DFS may wander down a long detour even if there is a much shorter route somewhere else.

### 3.5 DFS Complexity

Let `|V|` = number of vertices, `|E|` = number of edges.

**Time Complexity:**
- With adjacency **list**:
  - We visit each node once and scan through its adjacency list.
  - Time: **O(|V| + |E|)**.
- With adjacency **matrix**:
  - For each of `|V|` nodes, we scan all `|V|` possible neighbors.
  - Time: **O(|V|²)**.

**Space Complexity:**
- We store:
  - `visited[ ]`  → O(|V|)
  - `parent[ ]`   → O(|V|)
- The stack (or recursion depth) can be as deep as O(|V|) in the worst case (for example, in a long path-like graph).
- Total: **O(|V|)** space.

### 3.6 BFS vs DFS (Intuition)

**BFS expansion (layered / shortest distance):**

    start
     ├─ all neighbors (distance 1)
         ├─ their neighbors (distance 2)
             ├─ ...

**DFS expansion (deep dive):**

    start
     └─ first neighbor
          └─ that neighbor’s neighbor
               └─ that neighbor’s neighbor
                    (only backtrack when stuck)

**Key differences:**
- **BFS**
  - Explores in rings, increasing distance.
  - Guarantees the shortest path in an unweighted graph.
- **DFS**
  - Explores by depth first.
  - Does NOT guarantee shortest path.
  - Useful when you just need to know “is there ANY path?” instead of “what is the shortest path?”


---

## 4. A\* Search

### 4.1 Why A\*?

- **BFS** and **DFS** are **uninformed**:
  - BFS explores in all directions equally, without knowing which direction leads toward the goal.
  - DFS commits to a deep path, even if it’s a terrible direction.

- This is a problem in huge search spaces (like all cube states).
  - BFS might expand millions of states in every direction.
  - DFS might go super deep in a useless direction before backtracking.

- **A\*** is an **informed search algorithm**:
  - It uses a guess/estimate of how close each node is to the goal.
  - It tries to explore the **most promising** states first.
  - Result: you often reach the goal much sooner in practice.

### 4.2 Core Quantities in A\*

For each node `v` we track:

- **g(v)**  
  - Cost so far from the start node to `v`.  
  - In an unweighted graph, this is literally “number of moves taken so far”.

- **h(v)**  
  - A heuristic estimate of how many moves remain from `v` to the goal.  
  - This is just a GUESS. It is not guaranteed to be exact.  
  - Slides: “h stands for heuristic.” :contentReference[oaicite:3]{index=3}

- **f(v)**  
  - `f(v) = g(v) + h(v)`  
  - Our priority score.
  - Intuition:  
    - `g(v)` = cost we've paid so far.  
    - `h(v)` = estimated cost left.  
    - So `f(v)` = estimated total cost of a path that goes through `v`.

### 4.3 Data Structures Used by A\*

- **Open set / OpenQueue**:
  - A **min-priority queue (min-heap)** ordered by `f(v)`.
  - Contains nodes we’ve discovered but not fully processed yet.
  - We always expand the node with the **smallest f(v)** first.

- **Closed set / ClosedSet**:
  - A set (often a hash set) of nodes we’ve already fully expanded.
  - Prevents reprocessing nodes unless we find a strictly better path to them.

- **parent[ ]**:
  - Just like in BFS and DFS.
  - Used to reconstruct the path once we reach the goal.
  - `parent[u] = v` means: best path to `u` currently known goes through `v`.

- We also store `g(v)` and recompute/set `f(v)` as needed.

### 4.4 A\* Algorithm (High-Level Pseudocode)

    AStar(start, target):
        g(start) = 0
        parent[start] = null

        OpenQueue  = priority queue (min-heap) ordered by f(v) = g(v) + h(v)
        ClosedSet  = empty set

        insert start into OpenQueue

        while OpenQueue is not empty:
            v = OpenQueue.extractMin()   // node with the lowest f(v)

            if v == target:
                // goal reached
                return path reconstructed by following parent[] from v back to start

            move v to ClosedSet

            for each neighbor u of v:
                tentative_g = g(v) + 1   // +1 because graph edges cost 1

                if (u not in ClosedSet) AND
                   (u not in OpenQueue OR tentative_g < g(u)):

                    parent[u] = v
                    g(u) = tentative_g
                    f(u) = g(u) + h(u)

                    // if u is new, add it to OpenQueue
                    // if u was already there with worse g, update its priority
                    OpenQueue.insert_or_decrease_key(u, f(u))

Explanation:
- We always expand the node with **best estimated total cost** (`f` lowest).
- If we discover a cheaper path to a node we already knew, we update its `g`, `f`, and `parent`.
- As soon as we pop the **target** from the priority queue, we can reconstruct the solution path.

### 4.5 Relationship to BFS and DFS

- If we set **h(v) = 0 for all v**, then `f(v) = g(v)`.
  - With a **min-priority queue**, this behaves like BFS:
    - We always expand the node with the smallest distance-from-start (g).
    - So A\* with h = 0 ≈ BFS.

- If we set **h(v) = 0 for all v** BUT we always expand the node with the **largest** priority (i.e. max-heap instead of min-heap):
  - This behaves more like DFS:
    - We tend to chase “deeper” nodes.

- If **h(v)** was magically PERFECT (it exactly equals the true distance from `v` to the goal):
  - Then A\* would essentially go straight toward the goal.
  - It would always know the best next state to expand.
  - In real life, we don't have perfect h(v), but even a decent guess helps a lot.

- Takeaway:
  - BFS and DFS can be seen as special/extreme cases of picking what to expand next.
  - A\* is a flexible version that uses a heuristic to focus search.

### 4.6 Admissible Heuristic

- A heuristic `h(v)` is called **admissible** if:
  - `h(v)` **never overestimates** the true cost from `v` to the goal.
  - Formally:  
    `h(v) <= actual_distance(v, goal)` for all `v`.

- Why admissibility matters:
  - With an admissible heuristic (never too high) and a proper min-priority queue:
    - A\* is guaranteed to eventually reach a goal.
  - A "tighter" heuristic (closer to the real distance, but still never too high) will reduce how many states we explore.
  - A weak heuristic (like always `0`) makes A\* behave more like plain BFS.

- Practical runtime also depends on:
  - how fast we can compute `h(v)` for each state,
  - using good data structures (priority queues, hash sets),
  - and overall search space size. :contentReference[oaicite:4]{index=4}

### 4.7 A\* Complexity

Worst case:
- A\* can still end up exploring a massive portion of the search space
  (sometimes basically exponential in the depth of the optimal solution path).

But we can analyze cost per operation:

**Time Complexity:**
- We use a **min-priority queue / min-heap** for `OpenQueue`.
- Each extract-min (get best `f(v)`) takes `O(log |V|)`.
- Each insert / decrease-key also takes `O(log |V|)`.

- Over the whole run:
  - We may touch many vertices and edges.
  - Overall worst-case runtime is often given as  
    **O((|V| + |E|) log |V|)**.

**Space Complexity:**
- We maintain:
  - `OpenQueue` (frontier),
  - `ClosedSet` (already expanded nodes),
  - `parent[ ]`,
  - `g(v)` (cost-so-far),
  - sometimes `f(v)` explicitly.
- In the worst case, these can all be size `O(|V|)`.

Comparison:
- **BFS**
  - Time: `O(|V| + |E|)` with adjacency list; `O(|V|²)` with adjacency matrix.
  - Space: `O(|V|)` (queue + arrays).
  - Behavior: explores evenly; guarantees the shortest path in an unweighted graph.
- **DFS**
  - Time: `O(|V| + |E|)` with adjacency list; `O(|V|²)` with adjacency matrix.
  - Space: `O(|V|)` (stack depth + arrays).
  - Behavior: dives deep; does not guarantee shortest path.
- **A\***
  - Time: `O((|V| + |E|) log |V|)` worst case.
  - Space: `O(|V|)`.
  - Behavior: guided by `h(v)`; in practice can reach the goal much faster than BFS/DFS if `h(v)` is decent.


---

## 5. BFS vs DFS vs A* (Side-by-Side Summary)

### BFS
- **Data structure:** Queue (FIFO).
- **Exploration style:** Expands equally in all directions, by increasing distance from the start.
- **Guarantee:** Finds a **shortest path** (fewest edges) in an unweighted graph.
- **Path info:** Builds a **shortest-path tree** using `parent[ ]`.
- **Time:**
  - `O(|V| + |E|)` with adjacency list.
  - `O(|V|²)` with adjacency matrix.
- **Space:**
  - Queue can hold `O(|V|)` frontier nodes.
  - `visited[ ]`, `parent[ ]`, `distance[ ]` are `O(|V|)`.
  - Overall `O(|V|)`.
- **Use BFS when:**
  - You need the **minimum number of moves** from start to goal.

### DFS
- **Data structure:** Stack (LIFO) or recursion.
- **Exploration style:** Goes deep along one path, only backtracks when stuck.
- **Guarantee:** Will find *a* path if one exists, but NOT necessarily the shortest path.
- **Time:**
  - `O(|V| + |E|)` with adjacency list.
  - `O(|V|²)` with adjacency matrix.
- **Space:**
  - Recursion depth / stack can be `O(|V|)` in the worst case.
  - Plus `visited[ ]`, `parent[ ]`: overall `O(|V|)`.
- **Use DFS when:**
  - You only care whether **any** solution exists.
  - You don’t care about optimality of the path length.

### A\*
- **Data structure:** Min-priority queue ordered by `f(v) = g(v) + h(v)`.
- **Exploration style:** Expands the node that currently looks most promising, based on an estimate of how close it is to the goal.
- **Guarantee:**  
  - With an **admissible heuristic** (`h(v)` never overestimates), A\* will eventually reach a goal.
  - With a good heuristic, it often explores far fewer states than BFS/DFS.
- **Time:**
  - Worst-case `O((|V| + |E|) log |V|)` due to priority queue operations.
- **Space:**
  - Needs to remember frontier (OpenQueue), explored nodes (ClosedSet), and cost info.
  - Worst-case `O(|V|)`.
- **Use A\* when:**
  - You have a specific goal state.
  - You have a heuristic that can guess "how close am I to that goal?"


---

## 6. Visual Intuition

### 6.1 BFS (“wavefront”)

    start
    ├── dist=1 nodes
    │     ├── dist=2 nodes
    │     │      ├── dist=3 nodes
    │     │      └── dist=3 nodes
    │     └── dist=2 nodes
    └── dist=1 nodes

- BFS spreads outward in rings.
- It explores all nodes at distance 1 before distance 2, distance 2 before distance 3, etc.
- Because of this, BFS naturally finds the shortest path in an unweighted graph.

### 6.2 DFS (“deep tunnel”)

    start
     └── child A
           └── child B
                 └── child C
                      (backtrack only when stuck)

- DFS chooses one branch and follows it down as far as it can.
- Only after it hits a dead end does it backtrack and try alternative branches.
- Good for existence of *some* solution, but not necessarily the best one.

### 6.3 A* (“guided toward target”)

    start
       ↘        (expand node with best f = g + h)
         ↘
           ↘   target ?

- A\* prefers to expand nodes that:
  - have a low cost-so-far (`g(v)`),
  - AND look close to the goal (`h(v)` small).
- This makes the search feel like a "laser" pointing at the goal, instead of fog spreading everywhere like BFS.


---

## 7. TLDR

- **BFS = Queue**
  - Layered / level-order expansion.
  - First time you see a node = shortest path to it.
  - Time: `O(|V| + |E|)` with adjacency list.
  - Space: `O(|V|)`.
  - Use when you need the FEWEST MOVES.

- **DFS = Stack (or recursion)**
  - Dive deep first, backtrack later.
  - Does not guarantee shortest path.
  - Time: `O(|V| + |E|)` with adjacency list.
  - Space: `O(|V|)` worst case due to recursion depth.
  - Use when you just need to know IF A PATH EXISTS.

- **A\* = Priority Queue (min-heap on f = g + h)**
  - Guided search using a heuristic.
  - With admissible `h(v)` (never overestimates), A\* will eventually find a path.
  - Often WAY faster in practice than BFS for giant search spaces.
  - Worst-case time: `O((|V| + |E|) log |V|)`.
  - Space: `O(|V|)`.

