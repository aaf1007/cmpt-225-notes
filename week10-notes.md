# CMPT 225 - Week 10

---

## Table of Contents
1. [Hash Tables - Open Addressing](#hash-tables---open-addressing)
2. [Linear Probing](#linear-probing)
3. [Open Addressing Variants](#open-addressing-variants)
4. [3-SUM Problem](#3-sum-problem)
5. [Quick Sort](#quick-sort)
6. [Finding Median in Linear Time](#finding-median-in-linear-time)

---

## Hash Tables - Open Addressing

### Overview
Open addressing (also called probing) is a collision resolution technique where all elements are stored directly in the hash table array. When a collision occurs, we probe for the next available slot.

---

## Linear Probing

### Concept
**Linear probing**: When insertion causes a collision, check if the next entry is empty, and put the element there. If not, continue to the right (with wraparound using modulo).

### Add Operation (Basic Version)

```java
add(element) {
    index = element.hashCode()
    if (table[index] == null) {
        table[index] = element
    } else {
        // try adding at index+1, index+2...
    }
}
```

### Add Operation (Complete Version)

```java
add(element) {
    index = element.hashCode()
    while (table[index] != null) {
        index = (index + 1) % table.length  // wraparound
    }
    table[index] = element
}
```

### Visual Example - Adding Elements

**Add S, then Add R:**

Initial state:
```
| _ | A | D | L | Q | _ | _ | R | O | _ | _ |
  0   1   2   3   4   5   6   7   8   9   10
```

After adding S (suppose S hashes to index 4):
```
| _ | A | D | L | Q | S | _ | R | O | _ | _ |
  0   1   2   3   4   5   6   7   8   9   10
```

After removing Q and adding at different positions:
```
| _ | A | D | L | Q | _ | _ | _ | O | _ | _ |
  0   1   2   3   4   5   6   7   8   9   10
```

---

### Contains Operation

```java
contains(element) {
    index = element.hashCode()
    while (table[index] != null && !table[index].equals(element)) {
        index = (index + 1) % table.length
    }
    return (table[index].equals(element))
}
```

### Visual Example - Contains Operation

Given table:
```
| _ | A | D | L | Q | S | _ | R | O | _ | _ |
  0   1   2   3   4   5   6   7   8   9   10
```

**Operation Examples:**
- `Contains(R)`: Found at index 7
- `Contains(M)`: Not found (stops at null)
- `Contains(S)`: Found at index 5
- `Contains(C)`: Not found (stops at null)

---

### The Deletion Problem

**Problem**: If we simply set `table[i] = null` when deleting an element, we break the probing chain for elements that were inserted after it.

**Example Issue**:
- Element X hashes to index i
- Index i was occupied, so X was placed at i+1
- If we delete the element at index i and set it to null
- Now `contains(X)` will stop at the null and fail to find X at i+1

---

### Solution: Deleted Marker

When removing an element, mark that location as "deleted" instead of null.

```java
private static final Object deleted = new Object();
```

### Remove Operation

```java
remove(element) {
    index = element.hashCode()
    while (table[index] != null && !element.equals(table[index])) {
        index = (index + 1) % table.length
    }
    if (table[index] != null) {
        table[index] = deleted
    }
}
```

---

### Updated Add with Deleted Markers

```java
add(element) {
    index = element.hashCode()
    while (table[index] != null && table[index] != deleted) {
        index = (index + 1) % table.length
    }
    table[index] = element
}
```

**Important**: Before adding an element, check that it's not already in the table!

### Visual Example with Deleted Markers

After deleting S:
```
| _ | A | D | L | del | _ | _ | R | O | _ | _ |
  0   1   2   3   4    5   6   7   8   9   10
```

After adding S back (it can go into the deleted slot):
```
| _ | A | D | L | S | _ | _ | R | O | _ | _ |
  0   1   2   3   4   5   6   7   8   9   10
```

After deleting R and Q:
```
| _ | A | D | L | del | _ | _ | _ | O | _ | _ |
  0   1   2   3   4    5   6   7   8   9   10
```

Updated contains with deleted markers:
```
| _ | A | D | L | del | S | _ | R | O | del | _ |
  0   1   2   3   4    5   6   7   8   9     10
```

After delete(S):
```
| _ | A | D | L | del | del | _ | R | O | del | _ |
  0   1   2   3   4    5     6   7   8   9     10
```

Then add(S) again:
```
| _ | A | D | del | del | S | _ | R | O | del | _ |
  0   1   2   3    4     5   6   7   8   9     10
```

---

### Updated Contains with Deleted Markers

```java
contains(element) {
    index = element.hashCode()
    while (table[index] != null && !element.equals(table[index])) {
        index = (index + 1) % table.length
    }
    return (element.equals(table[index]))
}
// If we see a cell that was deleted, we keep searching
```

---

### Clustering Problem

**Cluster**: A consecutive block of keys

**Problem**: New keys are disproportionately likely to hash into big clusters, causing long insertion times.

Visual example of clustering:
```
W | _ | _ | _ | F | N | X | _ | _ | _ | A | D | L | del | S | _ | R | O | del | Z |
```

**Solution**: Resize the table to reduce the load factor.

**Resizing Strategy**:
- If load factor > 1/2, double the table length
- If load factor < 1/4, halve the table length
- **Important**: When resizing, we must rehash all keys (this takes time)

---

### Linear Hashing Variant (Without Deleted Markers)

Alternative approach: When removing elements, set to null BUT look forward in the block and move elements that need to be repositioned.

Visual example:
```
Before: | _ | A | D | L | M | _ | _ | R | O | T | _ |
Add S:  | _ | A | D | L | M | S | _ | R | O | T | _ |
Delete L:| _ | A | D | S | M | _ | _ | R | O | T | _ |
```

Notice S moved to fill the gap properly.

---

## Open Addressing Variants

### 1. Quadratic Probing

Instead of probing linearly, probe quadratically: index + 1², index + 2², index + 3², etc.

```java
add(element) {
    index = element.hashCode()
    i = 0
    while (table[index + i*i] != null) {
        i++
    }
    table[index + i*i] = element
}
```

```java
contains(element) {
    index = element.hashCode()
    i = 0
    while (table[index] != null && !element.equals(table[index])) {
        i++
        index = index + i*i
    }
    return (table[index] != null)
}
```

**Advantage**: Reduces clustering compared to linear probing.

---

### 2. Double Hashing

Use two hash functions. When collision occurs, probe using the second hash function.

```java
contains(element) {
    index = element.hashCode()  // hash1
    while (table[index] != null && !element.equals(table[index])) {
        index = index + hash2(element)  // Use second hash function
    }
    return (table[index] != null)
}
```

**Advantage**: Even better distribution than quadratic probing.

---

## 3-SUM Problem

### Problem Definition

**Input**: An array A[0...n-1] of integers

**Goal**: Find indices i, j, k such that A[i] + A[j] = A[k]

**Note**: The indices don't have to be all different

---

### Solution 1: Naive O(n³)

```java
for (i = 0; i < n; i++) {
    for (j = 0; j < n; j++) {
        for (k = 0; k < n; k++) {
            if (A[i] + A[j] == A[k]) {
                return (i, j, k);
            }
        }
    }
}
```

**Time Complexity**: O(n³) - Too slow!

---

### Solution 2: Using Sorting O(n² log n)

```java
// Sort the array A and remember each value's index
sort(A);  // Keep mapping of value -> original index

for (i = 0; i < n; i++) {
    for (j = 0; j < n; j++) {
        // Use binary search to find A[i] + A[j] in sorted array
        int target = A[i] + A[j];
        int k = binarySearch(A, target);
        if (k != -1) {
            return (i, j, k);  // Return original indices
        }
    }
}
```

**Time Complexity**: O(n² log n)

---

### Solution 3: Using Hash Table O(n²) Average

```java
// Store all elements of A in a hash table of size O(n)
HashTable table = new HashTable();
for (int x : A) {
    table.add(x);  // Also store the index
}

for (i = 0; i < n; i++) {
    for (j = 0; j < n; j++) {
        int target = A[i] + A[j];
        if (table.contains(target)) {
            return (i, j, table.getIndex(target));
        }
    }
}
```

**Time Complexity**: O(n²) on average

---

### Designing a Good Hash Table for 3-SUM

**Requirements**:
- Adding/searching in O(1) on average
- Table size is O(n)

#### Idea 0: Direct Array (BAD)
```
M = max(A[i])
Create array of length M
Store each A[i] at HashTable[A[i]]
```
**Problem**: If values in A are huge compared to n, most of the table will be empty. Not space-efficient!

---

#### Idea 1: Simple Modulo (VULNERABLE)
```
Choose prime p ∈ [10n, 20n]
Create hash table of size p
hash(x) = x mod p
```
**Problem**: If an adversary knows p, they could craft input where all A[i] are equal modulo p, causing all collisions!

---

#### Idea 2: Random Prime (BETTER)
```
Choose RANDOM prime p ∈ [10n, 20n]
hash(x) = x mod p
```
**Better**: Adversary doesn't know which prime we chose.

---

#### Idea 3: Universal Hashing (BEST)
```
Choose random prime p ∈ [10n, 20n]
Choose random a, b ∈ {0, 1, ..., p-1}
hash(x) = (ax + b) mod p
```

**Key Insight**: Using number theory, one can prove this gives O(1) expected time with high probability.

**Takeaway**: Learn serious math - it will make you a better programmer!

---

## Quick Sort

### Algorithm Overview

Given an array:
1. **Choose** an element in the array, call it the **pivot**
2. **Rearrange** elements so that:
   - All elements < pivot are to the left of pivot
   - All elements ≥ pivot are to the right of pivot
3. **Recursively** sort the left part
4. **Recursively** sort the right part

---

### Example

```
Input: [1, 8, 7, 10, 6, 12, 3]
Pivot = 7

After rearranging: [3, 1, 6 | 7 | 8, 10, 12]

Then recursively sort [3, 1, 6] and [8, 10, 7, 12]
```

**Important Note**: We don't have guarantees about the size of each half. For example, if pivot = 12:
```
[1, 8, 7, 10, 6, 3 | 12]
```
Left part has n-1 elements, right part has 0 elements!

---

### Running Time Analysis

**Best Case - Good Pivot**: O(n log n)

**Q**: What is a good pivot?

**A**: One that splits the array into approximately equal halves.

**Recurrence**:
```
T(n) = O(1)           // Choose pivot
     + O(n)           // Rearranging
     + 2*T(n/2)       // Recursion on two halves

By Master Theorem: T(n) = O(n log n)
```

**Fun Fact**: If we choose a random pivot at each step, we get O(n log n) with high probability (covered in CMPT 210/307).

---

### Q1: How to Choose the Pivot?

Options:
1. **At random** (gives good expected performance)
2. **Middle element**: `pivot = A[n/2]`
3. **Use domain knowledge** about the array

---

### Q2: How to Rearrange Elements?

#### Naive Approach (Uses Extra Memory)

```java
// Input: [1, 8, 7, 10, 6, 12, 3], pivot = 7

List<Integer> less = new ArrayList<>();
List<Integer> greater = new ArrayList<>();

for (int x : array) {
    if (x < pivot) {
        less.add(x);      // [1, 6, 3]
    } else {
        greater.add(x);   // [8, 7, 10, 12]
    }
}

// Move them back: [1, 6, 3, 8, 7, 10, 12]
```

**Problem**: Requires O(n) extra memory.

**Question**: Can we do it in-place?

---

### In-Place Rearrangement (Two Pointers)

**Strategy**:
- Use two pointers: left and right
- Move left pointer to the right, ensuring all elements to its left are < pivot
- Move right pointer to the left, ensuring all elements to its right are ≥ pivot
- Swap elements when both pointers stop

---

### Detailed Example

```
Input: [1, 8, 7, 5, 6, 12, 3], pivot = 7

Step 1: Initialize pointers
        L                   R
        [1, 8, 7, 5, 6, 12, 3]

Step 2: Move left pointer (1 < pivot, continue)
           L                R
        [1, 8, 7, 5, 6, 12, 3]

Step 3: Stop at 8 (8 ≥ pivot)
           L                R
        [1, 8, 7, 5, 6, 12, 3]

Step 4: Move right pointer from right
           L             R
        [1, 8, 7, 5, 6, 12, 3]
        
        (12 ≥ pivot, continue)
           L          R
        [1, 8, 7, 5, 6, 12, 3]
        
        (6 < pivot, stop)

Step 5: Swap 8 and 3
           L          R
        [1, 3, 7, 5, 6, 12, 8]

Step 6: Move left pointer
              L       R
        [1, 3, 7, 5, 6, 12, 8]
        (3 < pivot, continue to 7)
                 L    R
        [1, 3, 7, 5, 6, 12, 8]
        (7 ≥ pivot, stop)

Step 7: Move right pointer
                 L R
        [1, 3, 7, 5, 6, 12, 8]
        (6 < pivot, stop)

Step 8: Swap 7 and 6
                 LR
        [1, 3, 6, 5, 7, 12, 8]

Step 9: Move left pointer to 7
                    L
        [1, 3, 6, 5, 7, 12, 8]
        Pointers have met - DONE!
```

---

### Rearrange Algorithm (Pseudocode)

```java
rearrange(array, pivot) {
    left = 0;
    right = array.length - 1;
    
    while (left < right) {
        // Move left pointer
        while (array[left] < pivot) {
            left++;
        }
        
        // Move right pointer
        while (array[right] >= pivot) {
            right--;
        }
        
        // Swap if pointers haven't crossed
        if (left < right) {
            swap(array[left], array[right]);
        }
    }
}
```

**Time Complexity**: O(n) - Each element is examined once

---

### Deterministic Pivot Selection

**Problem**: Random pivots could still give bad performance in worst case.

**Solution**: Use the median-finding algorithm!

**Idea**: 
- Find the median element in O(n) time
- Use it as pivot (guarantees splitting array into two halves)
- This makes QuickSort deterministic O(n log n)

---

## Finding Median in Linear Time

### Problem Definition

**Input**: An array A of length n

**Output**: The median element in the sorted order

**Example**:
```
A = [1, 4, 3, 7, 2, 9, 10]
Output: 4 (the middle element if sorted)
```

**Naive Solution**: Sort A, return A[n/2] → O(n log n)

**Question**: Can we do better than O(n log n)?

**Answer**: YES! We can do it in O(n) time.

---

### Generalization: Finding k-th Smallest Element

Instead of just median, find the k-th smallest element.

---

### Algorithm

**Input**: Array A of length n, integer k

**Steps**:
1. Divide A into ⌈n/5⌉ groups, each of size 5 (or less)
2. Sort each 5-tuple
3. Call the medians of each 5-tuple the "representatives"
4. **Recursively** find the median M of these n/5 representatives
5. Let L = all elements < M
6. Let H = all elements > M
7. Rearrange A so that A = [L, M, H]
8. **Decision step**:
   - If |L| = k-1, return M (found it!)
   - If |L| ≥ k, recursively search in L for k-th element
   - If |L| ≤ k-2, recursively search in H for (k - |L| - 1)-th element

---

### Visual Example

#### Step 1-2: Partition into 5-tuples and sort each

```
Group 1: [10, 22, 31, 70, 81]  → median: 31
Group 2: [5, 6, 9, 10, 11]     → median: 9
Group 3: [12, 32, 36, 51, 55]  → median: 36
Group 4: [14, 23, 24, 25, 28]  → median: 24
Group 5: [11, 14, 18, 20, 41]  → median: 18
Group 6: [5, 7, 8]             → median: 7
Group 7: [1, 2, 3, 6, 7]       → median: 3
```

#### Step 3-4: Find median of representatives

```
Representatives: [31, 9, 36, 24, 18, 7, 3]
Median of representatives: M = 18
```

#### Step 5-6: Partition by M

```
L (< 18): [10, 5, 6, 9, 10, 11, 12, 14, 14, 11, 5, 7, 8, 1, 2, 3, 6, 7]
M: 18
H (> 18): [22, 31, 70, 81, 32, 36, 51, 55, 23, 24, 25, 28, 20, 41]
```

#### Step 8: Recursion decision
```
If |L| = k-1: return M
If |L| ≥ k: recursively find k-th element in L
If |L| ≤ k-2: recursively find (k-|L|-1)-th element in H
```

---

### Key Observation (Why This Works)

After partitioning by M (median of representatives):

**|L| ≥ 0.3n** because:
- There are n/10 representatives ≤ M
- For each representative, at least 3 elements are ≤ M
- Total: (n/10) × 3 = 0.3n elements

**|H| ≥ 0.3n** because:
- There are n/10 representatives ≥ M  
- For each representative, at least 3 elements are ≥ M
- Total: (n/10) × 3 = 0.3n elements

**Therefore**: The recursive call is on at most 0.7n elements!

---

### Running Time Analysis

```
T(n) = T(n/5)        // Finding median of representatives
     + T(0.7n)       // Recursive call on smaller subarray
     + O(n)          // Partitioning and sorting 5-tuples
```

**Components**:
- Dividing into 5-tuples: O(n)
- Sorting each 5-tuple: O(1) per tuple, O(n) total
- Partitioning by M: O(n)
- Recursion on representatives: T(n/5)
- Recursion on L or H: T(0.7n) at most

---

### Proof that T(n) = O(n)

**Claim**: If T(n) = T(0.2n) + T(0.7n) + Cn and T(1) = C, then T(n) < 10Cn

**Note**: We're using 0.2n instead of n/5 for simpler calculation (n/5 = 0.2n)

**Proof by Induction**:

**Base case** (n = 1):
```
T(1) = C < 10C ✓
```

**Induction step**: Assume true for all integers < n. Prove for n:
```
T(n) = T(0.2n) + T(0.7n) + Cn
     < 10C(0.2n) + 10C(0.7n) + Cn    // By induction hypothesis
     = 2Cn + 7Cn + Cn
     = 10Cn ✓
```

**Moral of the story**: 0.2n + 0.7n = 0.9n < n

The overhead is O(n), but we're reducing the problem size faster than we're adding overhead!

---

### Alternative Visualization (Recursion Tree)

```
Level 0:  Cn                    = Cn
Level 1:  C(0.2n) + C(0.7n)    = C(0.9n)
Level 2:  C(0.2²n) + C(0.7²n)  = C(0.81n)
Level 3:  C(0.3³n) + C(0.7³n)  = C(0.729n)
...

Total: T(n) = C(n + 0.9n + 0.81n + 0.729n + ...)
            = Cn(1 + 0.9 + 0.9² + 0.9³ + ...)
            = Cn × (1/(1-0.9))
            = Cn × 10
            = 10Cn
```

Geometric series with ratio r = 0.9 < 1, so it converges!

---

## Summary of Key Concepts for Testing

### Hash Tables - Linear Probing
- **Add**: Loop until finding null or deleted, insert element
- **Contains**: Loop until finding element or null
- **Remove**: Mark as deleted, don't set to null
- **Clustering**: Consecutive blocks grow over time, slow down operations
- **Load factor**: Resize when > 1/2 or < 1/4

### Open Addressing Variants
- **Linear probing**: index + 1, index + 2, ...
- **Quadratic probing**: index + 1², index + 2², ...
- **Double hashing**: index + hash2(x), index + 2×hash2(x), ...

### 3-SUM Problem
- **O(n³)**: Brute force triple loop
- **O(n² log n)**: Sort + binary search
- **O(n²)**: Hash table with universal hashing

### QuickSort
- **Pivot selection**: Random, middle, or median
- **Partitioning**: Two-pointer technique in O(n)
- **Best case**: O(n log n) with good pivots
- **Worst case**: O(n²) with bad pivots
- **Deterministic O(n log n)**: Use median-finding for pivot

### Finding Median (k-th Element)
- **Groups of 5**: Ensures good partitioning
- **Recursion**: T(n) = T(n/5) + T(0.7n) + O(n)
- **Time complexity**: O(n) linear time
- **Key property**: Always partition into ≥ 30% and ≤ 70%
- **Proof technique**: Induction showing T(n) < 10Cn

---

## Practice Problems

1. **Trace linear probing** operations on a hash table
2. **Calculate load factors** and when to resize
3. **Implement quicksort** with different pivot strategies
4. **Analyze 3-SUM** solutions and their time complexities
5. **Prove median-finding** algorithm correctness
6. **Design hash functions** for specific applications

---

## Code Templates for Exams

### Linear Probing Add (with deleted markers)
```java
add(element) {
    // Check if already in table first!
    if (contains(element)) return;
    
    index = element.hashCode() % table.length;
    while (table[index] != null && table[index] != deleted) {
        index = (index + 1) % table.length;
    }
    table[index] = element;
    size++;
}
```

### QuickSort Partition
```java
int partition(int[] arr, int low, int high) {
    int pivot = arr[high];
    int i = low - 1;
    
    for (int j = low; j < high; j++) {
        if (arr[j] < pivot) {
            i++;
            swap(arr, i, j);
        }
    }
    swap(arr, i + 1, high);
    return i + 1;
}
```

### Finding k-th Element (Conceptual)
```java
int findKth(int[] A, int k) {
    // 1. Divide into groups of 5
    // 2. Find median of each group
    // 3. Recursively find median of medians (M)
    // 4. Partition A by M into L and H
    // 5. If |L| == k-1, return M
    // 6. Else recurse on L or H
}
```

---

## Important Formulas

- **Load factor**: α = n / table.length
- **Expected probe length (linear)**: 1 / (1 - α)
- **QuickSort best case**: T(n) = 2T(n/2) + O(n) = O(n log n)
- **Median algorithm**: T(n) = T(n/5) + T(0.7n) + O(n) = O(n)
- **Geometric series**: 1 + r + r² + r³ + ... = 1/(1-r) when |r| < 1
