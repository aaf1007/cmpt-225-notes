import assignment2.MyStackOperations;
import basicdatastructures.stack.*;
import geomerticshapes.*;

public class TestMyStackOperations {

    public static void testSize() {
        Stack<Integer> s = new StackLinkedListBased<Integer>();
        s.push(5);
        s.push(6);
        s.push(7);

        if (MyStackOperations.size(s) != 3) {
            System.out.println("testSize ERROR");
            return;
        }

        s.push(7);
        if (MyStackOperations.size(s) != 4) {
            System.out.println("testSize ERROR");
            return;
        }

        s.pop();
        if (MyStackOperations.size(s) != 3) {
            System.out.println("testSize ERROR");
            return;
        }

        Stack<GeometricShape> s2 = new StackLinkedListBased<GeometricShape>();
        Circle c = new Circle(5, 10, 15);
        for (int i = 0; i < 50; i++)
            s2.push(c);
        for (int i = 0; i < 50; i++)
            s2.push(new Circle(1, 2, 3));

        if (MyStackOperations.size(s2) != 100) {
            System.out.println("testSize ERROR");
            return;
        }

        System.out.println("testSize OK");
    }


    public static void testRemoveBottom() {
        int a[] = {5, 50, 500};
        Stack<Integer> s = new StackLinkedListBased<Integer>();
        for (int i = 0; i < a.length; i++) {
            s.push(a[i]);
        }

        if (s.isEmpty()) {
            System.out.println("testRemoveBottom ERROR");
            return;
        }

        int ret = MyStackOperations.removeBottom(s);
        if (ret != a[0]) {
            System.out.println("testRemoveBottom ERROR");
            return;
        }

        int top = s.pop();
        if (top != a[2]) {
            System.out.println("testRemoveBottom ERROR");
            return;
        }

        int mid = s.pop();
        if (mid != a[1]) {
            System.out.println("testRemoveBottom ERROR");
            return;
        }

        if (!s.isEmpty()) {
            System.out.println("testRemoveBottom ERROR");
            return;
        }

        System.out.println("testRemoveBottom OK");
    }

    public static void testReverse() {
        int a[] = {1, 2, 3, 4, 5, 5, 5, 5, 5};
        Stack<Integer> s = new StackLinkedListBased<Integer>();
        for (int i = 0; i < a.length; i++)
            s.push(a[i]);

        MyStackOperations.reverse(s);

        for (int i = 0; i < a.length; i++) {
            if (s.isEmpty() || s.pop() != a[i]) {
                System.out.println("testReverse ERROR");
                return;
            }
        }
        if (!s.isEmpty()) {
            System.out.println("testReverse ERROR");
            return;
        }

        System.out.println("testReverse OK");
    }


    public static void testAreEqual() {
        Stack<GeometricShape> s1 = new StackLinkedListBased<GeometricShape>();
        Stack<GeometricShape> s2 = new StackArrayBased<GeometricShape>();
        if (!MyStackOperations.areEqual(s1, s2)) {
            System.out.println("testAreEqual ERROR 1");
            return;
        }

        GeometricShape a[] = {
                new Square(5, 6, 10),
                new Square(1, 2, 10),
                new Circle(10, 20, 1),
                new Rectangle(0, 0, 2, 1),
        };
        for (int i = 0; i < a.length; i++) {
            s1.push(a[i]);
            s2.push(a[i]);
        }

        if (!MyStackOperations.areEqual(s1, s2)) {
            System.out.println("testAreEqual ERROR 2");
            return;
        }

        s2.push(new Rectangle(1, 1, 1, 1));
        if (MyStackOperations.areEqual(s1, s2)) {
            System.out.println("testAreEqual ERROR 3");
            return;
        }


        GeometricShape a4[] = {a[0], new Square(1, 2, 10), a[2], a[3]}; // a4[1] != a[1]

        Stack<GeometricShape> s3 = new StackLinkedListBased<GeometricShape>();
        for (int i = 0; i < a4.length; i++)
            s3.push(a4[i]);
        if (MyStackOperations.areEqual(s1, s3)) {
            System.out.println("testAreEqual ERROR 4");
            return;
        }

        System.out.println("testAreEqual OK");
    }

    public static void main(String[] args) {
        testSize();
        testRemoveBottom();
        testReverse();
        testAreEqual();

        // Stack<Integer> s = new StackLinkedListBased<>();
        // s.push(1);
        // s.push(2);
        // s.push(3);
        // s.push(4);

        // System.out.println(MyStackOperations.removeBottom(s));
    }

}
