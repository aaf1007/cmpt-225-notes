# Week 9 Lecture 2

## 1) Variations on the Search Algorithms

### Bidirectional BFS
- **Idea:** Run BFS *from the start* and *from the target* simultaneously; stop when the frontiers meet.
- **Why it helps:** Each search explores roughly half the distance; the **search space shrinks dramatically** compared to single-source BFS.
- **Memory:** Typically **more memory-efficient** overall since each frontier stays smaller.
- **When to use:** When you know (or can detect) a **unique target** and can test whether a node appears in the opposite frontier.

### IDA\* (Iterative Deepening A\*)
- **Hybrid:** Combines **A\***’s heuristic guidance with **DFS**’s low memory footprint.
- **Mechanism:** Choose a threshold **T** and run DFS but **prune** any node where `g(v) + h(v) > T`. If not found, **increase `T`** and repeat.
- **Trade-offs:** Much lower memory than classic A\*, but can **revisit** nodes across iterations.

---

## 2) Project Tips (Rubik’s Cube Solver Context)

- **Scoring functions:**
  - `g(v)`: cost from **start** to node `v` (number of moves so far).
  - `h(v)`: **heuristic/estimate** from `v` to the **target** (must be fast; admissible if `h ≤ true_distance`).
  - `f(v) = g(v) + h(v)` (the priority key).

- **Core data structures:**
  - **Open set:** a **min-priority queue** (priority by **`f(v)`**).
  - **Closed set:** a **`HashSet`** for fast membership (visited/processed nodes).

- **Implementation notes:**
  - Implement **`equals`** and **`hashCode`** correctly on your **cube state**; consider **precomputing** and caching the hash in the constructor to speed lookups.
  - Expect **`OutOfMemoryError`** if states are large; **compress** state representation.
  - Consider **intermediate subgoals** (e.g., solve first row) to reduce branching.
  - Start simple; ensure correctness before optimizing heuristics or pruning.

---

## 3) Hashing & Hash Tables – Core Ideas

### Goals
- Support **Insert**, **Remove**, **Contains?** in **O(1) average time**.
- **Worst case:** O(n), but good design keeps this rare.

### Hash Functions & `hashCode()`
- A **hash function** maps any key/object → **int**, then to a table index (typically `index = hash % M`).
- In **Java**, override **`hashCode()`** (declared in `java.lang.Object`) in tandem with **`equals()`**.

**`hashCode` contract (essentials):**
1. **Consistency per execution:** Multiple calls on the same object should return the **same** value (unless fields used by the hash change).
2. **Equal objects ⇒ equal hashes:** If `a.equals(b)` is **true**, then `a.hashCode() == b.hashCode()` **must** hold.
3. **Unequal objects ⇒ (ideally) different hashes:** Not required, but **reduces collisions** and improves performance.

### Designing Good Hashes
- Aim for **even distribution** over table slots; avoid “obvious” collisions.
- Prefer modulus by a **prime** table size (e.g., 1019) rather than round numbers like 100.
- **Strings:** Java’s typical pattern is a **polynomial rolling hash** with base ~31  
  (e.g., `hash = s[0]*31^(n-1) + s[1]*31^(n-2) + … + s[n-1]`).
- **Speed matters:** Hashing should be **fast** since it’s called frequently.
- **Cryptographic hashes ≠ general hashing:** Crypto hashes aim to be **hard to invert**—overkill for hash tables.

---

## 4) Hash Tables – From Hash to Data Structure

### Basic idea
- Use an **array** of size `M` (often prime).
- Map a key to `index = hash(key) % M`.
- But **collisions are inevitable** ⇒ need a **collision strategy**.

### Two families of collision handling
1. **Separate Chaining (Closed Addressing)**  
   Each array slot stores a **bucket** (commonly a **linked list**) of entries that map to that index.
2. **Open Addressing (Probing)**  
   Store **one** entry per slot; upon collision, **probe** for another free slot (e.g., linear/quadratic probing, double hashing).

> The slides emphasize **Separate Chaining**.

---

## 5) Separate Chaining (Details)

- **Structure:** Table is an array; each entry holds a **LinkedList** (or another small structure) of elements with the same index.

**Operations (sketch):**
- **Contains?(x)**  
  `i = hash(x) % M` → scan bucket `table[i]` for `x`.
- **Add(x)**  
  `i = hash(x) % M` → if `x` **not** in `table[i]`, **insert** (often at front).
- **Remove(x)**  
  `i = hash(x) % M` → if present in `table[i]`, **delete** it.

**Notes & variants:**
- Buckets are usually **small** (aim \< 10 items).
- Buckets can be **LinkedLists**, but could also be:
  - **Balanced BSTs** (ordering by a key) to get **O(log k)** per bucket.
  - **Secondary hash tables** (two-level hashing) for even faster lookups.

**Pros**
- **Simple** and robust; performance depends mostly on **load factor**.
- Easy to implement deletion.
  
**Cons**
- Extra **pointer overhead** and potential **cache misses** compared to open addressing.
