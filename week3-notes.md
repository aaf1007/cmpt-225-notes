## <u>Design Patterns</u>
---
 ### Singleton
 - only allows **one** object from class

```java
public class Singleton {
  // needs to be priv
  private static Singleton theUniqueInstance = new Singleton();
  public static Singleton getInstance() {
    return theUniqueInstance;
  }
  private Singleton() {....}
  // ^ private so cant be accessed and make another instance of obj.
}
```

### Factory
- a collection of classes that implements an **interface** or extends and **Abstract** class
- centralizes object creation that share a common type of

This class implements a **Factory** Design Pattern called **GeoShapeFactory**
```java
public class GeoShapeFactory {
  public GeoShape createShape(String shapeType) {
    if (shapeType.equalsIgnoreCase("CIRCLE")) {
      return new Circle(0,0,10);
    }
    .
    .
    .
    return null;
  }
}
```
This class uses the **Factory** Design Class from above ^^^

``` java
public class FactoryDemo {
  public static void main (String[] args) {
    GeoShapeFactory factory = new GeoShapeFactory();

    LinkedList<GeoShape> list = new LinkedList<GeoShape>;
    .
    .
    .
    // this creates the shape using the shapeType parameter from GeoShapeFactory Factory class
    // and adds to LinkedList Data Structure
    list.add(factory.createShape("circle"));
  }
}
```
### Builder
- used to build complex objects step-by-step and finishes with buiild()/create() methods - that returns an object
- avoids contructors and only initializes what you need
- reusable to create multiple instances of obj

**Builder** design class:
``` java
public class GeometricShapeBuilder {
  // this acts as a default shape to use
	public final static String CIRCLE = "Circle";
	
	private String shapeType;
	private int x;
	private int y;
	private int radius; // used for circle
	private int length;	// used for rectangle/square
	private int width;

	public GeometricShape createShape() {
		if(shapeType == null){
			return null;
			// or maybe throw an exception
		}		

		if(shapeType.equalsIgnoreCase(CIRCLE)){
			return new Circle(x,y,radius);

		} else if(shapeType.equalsIgnoreCase("RECTANGLE")){
			return new Rectangle(x,y,length,width);

		} else if(shapeType.equalsIgnoreCase("SQUARE")){
			return new Square(x,y,length);
		}

		return null;
	}
}
```
This same class has setters for each variable
```java
GeometricShapeBuilder setLength(int length) {
		this.length = length;
		return this;
	}
```
Class that **uses** the builder design
```java
public class BuilderDemo {
	public static void main(String[] args) {
    // just makes an ArrayList Data Structure to hold GeometricShape objects
		ArrayList<GeometricShape> list = new ArrayList<GeometricShape>();
    // creates the builder class instance
		GeometricShapeBuilder builder = new GeometricShapeBuilder();

		GeometricShape geomShape = null;

//		old fashion style
		builder.setShapeType("Circle");
		builder.setX(0);
		builder.setY(0);
		builder.setRadius(100);
    // finishes with createShape() that geomShape to whatever was built
		geomShape = builder.createShape();
		list.add(geomShape);
	}
}
```
Building the object can also be done like this:
```java
// geomShape = builder.setShapeType("circle")
geomShape = builder.setShapeType(GeometricShapeBuilder.CIRCLE) // use constants
    .setX(0).setY(0).setRadius(100)
    .createShape();
list.add(geomShape);
```
In one line:
```java
// adds to ArrayList
list.add(builder.setShapeType("Rectangle")
    .setX(40)
    .setY(50)
    .setLength(50)
    .setWidth(100)
    .createShape());
``` 
**Remember:**
- Always end with the build()/create() methods
- The same "builder" can be used mult. times to create similar objects
---

## <u>Streams</u>
```java
import.util.stream.Stream;
```
- Used for processing data from collections/arrays in a concise way

**Create:**
- list.stream(), Arrays.stream(arr), Stream.of(a,b,c)
Core intermediate ops:
- filter(p), map(f), distinct(), sorted(), limit(n), skip(n)

**Terminals:**
- forEach(action), collect(toList), reduce(id, combiner), count(), anyMatch(p), allMatch(p), findFirst()

**Examples:**
```java
// print
list.stream().forEach(System.out::println);

// filter + map
list.stream().filter(n -> n % 2 == 1).map(n -> n + 1000).forEach(System.out::println);

// collect to list
List<Integer> odds = list.stream().filter(n -> n % 2 == 1).toList(); // Java 16+
// List<Integer> odds = list.stream().filter(n -> n % 2 == 1).collect(Collectors.toList());

// reduce (sum evens)
int sumEvens = list.stream().filter(n -> n % 2 == 0).reduce(0, Integer::sum);

// distinct + sorted (reverse)
list.stream().distinct().sorted().forEach(System.out::println);
list.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);

// first match or default
int firstOdd = list.stream().filter(n -> n % 2 == 1).findFirst().orElse(-1);

// primitive helpers
int total = list.stream().mapToInt(Integer::intValue).sum();
double avg = list.stream().mapToInt(Integer::intValue).average().orElse(0);
```
---
## <u>Data Structures</u>

### Linked List
```java
import.java.util.LinkedList;
```
- Like a chain of elements - all linked to the following/next one
- Last element points to **NULL**

<img src="Screenshot 2025-10-04 at 5.34.28 PM.png">

**Implementation:**
```java
public class LinkedList<T> {
  // This is for nodes
  class LinkedListNode { // inner class
    T data;
    LinkedListNode next;
    // One param Constructor
    LinkedListNode(T data) {
      this.data = data;
      this.next = null;
    }
    // Two param const.
    LinkedListNode(T data, LinkedListNode next) {
      this.data = data;
      this.next = next;
    }
    public LinkedListNode getNext() {
      return next;
    }
  }
  private LinkedListNode head;
  private int length;
}
```
**Add to Head**
```java
public void addToHead(T element) {
  if (element == null)
    throw new IllegalArgumentException("Trying to add null to the list");

  LinkedListNode newNode = new LinkedListNode(element, this.head);
  this.head = newNode; // update the head of the list to be the new node
  length++;
}
```
<img src="Screenshot 2025-10-04 at 5.52.40 PM.png">

**Remove from Head**
```java
public T removeFromHead() {
  if (head==null) // list is empty
    throw new NoSuchElementException("My linked list is empty");

  T ret = head.data;
  this.head = this.head.next;
  length--;
  return ret;
}
```
<img src="Screenshot 2025-10-04 at 5.56.46 PM.png">

**Add to Tail**
```java
public void addToTail(T element) {
  if (element == null)
    throw new IllegalArgumentException("Trying to add null to the list");

  LinkedListNode newNode = new LinkedListNode(element, null);

  if(head == null)
    head = newNode;
  else {
    LinkedListNode node = head;
    while (node.next != null) // iterate until reaching the last node
      node = node.next;
    // here node.next == null
    node.next = newNode;
  }
  length++;
}
```

**Remove from Tail**
```java
public T removeFromTail() {
  if (length==0)
    throw new NoSuchElementException("My linked list is empty");
  
  T ret;
  if (head.next == null) {
    ret = head.data;
    head = null;
  }
 else {
    LinkedListNode node = head;
    while (node.next.next != null)
      node = node.next;
    // node is the one before last
    ret =  node.next.data;	// remember the last element
    node.next = null; // disconnecting node.next from the list
  }
  length--;
  return ret;
}
```
**Getter**
```java
public T get(int index) {
  if (index < 0 || index >= length)
    throw new IndexOutOfBoundsException("get(" + index + "), length = " + length);

  LinkedListNode node = head;
  for (int j = 0; j < index; j++)
    node = node.next;
  return node.data;
}
```

**Setter** - returns previous value
```java
public T set(int index, T newValue) {
  
  if (newValue == null)
    throw new IllegalArgumentException("Trying to add null to the list");
  
  if (index <0 || index >= length)
    throw new IndexOutOfBoundsException("set(" + index + "), length = " + length);

  LinkedListNode node = head;
  for (int j = 0; j < index; j++)
    node = node.next;
  T prevValue = node.data; // store the previous value
  node.data = newValue; // set the new value
  return prevValue;	// return the previous value
}
```

**removeAllOccurances**
```java
public void removeAllOccurences(T element) {

  // remove all elements in the beginning of the list
  while (head!=null && head.data.equals(element)) {
          this.head = this.head.next;
          length--;
      }

  if (head==null)
    return;

  // head is not null and head.data is not element
  // look one node forward, and remove it if needed
  LinkedListNode node = head;
  while (node.next != null) {
    if (node.next.data.equals(element)) {
              node.next = node.next.next;
              length--;
          }
    else
      node = node.next;
  }
}
```

### Doubly Linked List
**Implementation**
```java
public class DoublyLinkedList<T> {
  class DLLNode { // inner class
    T data;
    DLLNode next;
    DLLNode prev;
  …
  }
  private DLLNode head;
  private DLLNode tail;
}
```

**Remove**
```java
public T removeNode(LinkedListNode node) {
  node.prev.next = node.next;
  node .next.prev = node.prev;
  return node.data;
}
```
<img src="Screenshot 2025-10-04 at 6.20.47 PM.png">

### Stack
- Last-in-first-out (LIFO)

**Stack Interface**
```java
public interface Stack<T> {
	
	public void push(T item);
	
	public T pop();
	
	public boolean isEmpty();

}
```
**Implementation**
1. Using **ArrayList**
  - push(item) - add to end of list - O(1)
  - pop() - remove from end of list - O(1)
  - isEmpty() - check if size==0 - O(1)
  ```java
  public class StackArrayBased<T> implements Stack<T> {
    private ArrayList<T> list;
    
    public StackArrayBased() {
      list = new ArrayList<T>();
    }
    // sometimes inefficient
    public void push(T item) {
      list.add(item);
    }
    public T pop() {
      return list.remove(list.size()-1);
    }
    public boolean isEmpty() {
      return list.size() == 0;
    }
  }
  ```
2. Using **LinkedList**
  - push(item) - add to **head** - O(1)
  - pop() - remove from **head** - O(1)
  - isEmpty() - size==0 or head==null - O(1)
  ```java
  public class StackLinkedListBased<T> implements Stack<T> {
    private LinkedList<T> list;

    public StackLinkedListBased() {
      list = new LinkedList<T>();
    }
    public void push(T item) {
      list.addFirst(item);
    }
    public T pop() {
      return list.removeFirst();
    }
    public boolean isEmpty() {
      return list.isEmpty();
    }
  }
  ```

### Queue
- First-in-first-out (FIFO)

**Queue Interface**
```java
public interface Queue<T> {
	/**
	 * adds an element to the tail of the queue 
	 * @param item
	 */
	public void enqueue(T item);
	/**
	 * removes an element from the head of the queue 
	 * @param item
	 */
	public T dequeue();
	public boolean isEmpty();
}
```
**Implementation**
1. Using **LinkedList**
  - enqueue(item) - add to **tail** of list - O(1)
  - dequeue() - remove from **head** of list - O(1)
  - isEmpty() - check if size==0 - O(1)
  ```java
  public class QueueLinkedListBased<T> implements Queue<T> {
    LinkedList<T> list;
    public QueueLinkedListBased() {
      list = new LinkedList<T>();
    }
    public void enqueue(T item) {
      list.addLast(item);
    }
    public T dequeue() {
      return list.removeFirst();
    }
    public boolean isEmpty() {
      return list.size()==0;
    }
  }
  ```
2. Using **ArrayList**
  - enqueue(item) - add to **tail** - O(1)
  - dequeue() - remove from *head* - O(n)
  - isEmpty() - size==0 or head==null - O(1)
  ```java
  public class QueueArrayBased<T> implements Queue<T> {
    ArrayList<T> list;
    public QueueArrayBased() {
      list = new ArrayList<T>();
    }
    /**
    * when need to resize the running time is O(size of queue)
    */
    public void enqueue(T item) {
      list.add(item); // adds to the end of the array
    }
    /**
    * running time is  O(size of queue)
    */
    public T dequeue() {
      return list.remove(0); // remove from position 0
    }
    public boolean isEmpty() {
      return list.size()==0;
    }
  }
  ```
