import sorting.MySortingAlgs;

public class TestSorting<T> {
    public static void testSortStrings1() {
        String[] a = {"hello", "World", "1", "2", "12345678"};
        String[] expected_ans = {"1", "2", "World", "hello", "12345678"};

        MySortingAlgs.sortStrings(a);
        for (int i = 0; i < a.length; i++)
            if (!a[i].equals(expected_ans[i])) {
                System.out.println("testSortStrings1 ERROR");
                return;
            }
        System.out.println("testSortStrings1 OK");
    }

    public static void testSortStrings2() {
        String[] a = {"Wx", "ab", "Zde", "6_@7h", "7", "hij", "_hh", "b"};
        String[] expected_ans = {"7", "b", "Wx", "ab", "Zde", "_hh", "hij", "6_@7h"};

        MySortingAlgs.sortStrings(a);
        for (int i = 0; i < a.length; i++)
            if (!a[i].equals(expected_ans[i])) {
                System.out.println("testSortStrings2 ERROR");
                return;
            }
        System.out.println("testSortStrings2 OK");
    }

    public static void testMerge1() {
        Integer[] a = {3, 5, 6, 8, 1, 2, 4, 7};
        Integer[] expected_ans = {1, 2, 3, 4, 5, 6, 7, 8};

        MySortingAlgs.merge(a, 0, 4, a.length - 1);
        for (int i = 0; i < a.length; i++)
            if (!a[i].equals(expected_ans[i])) {
                System.out.println("testMerge1 ERROR");
                return;
            }
        System.out.println("testMerge1 OK");
    }

    public static void testMerge2() {
        String[] a = {"ZZZ", "JKL", "WXYZ", "DEF", "GHI", "MNO", "PQR", "TUV"};
        String[] expected_ans = {"ZZZ", "DEF", "GHI", "JKL", "MNO", "PQR", "TUV", "WXYZ"};

        MySortingAlgs.merge(a, 1, 3, a.length - 1);
        for (int i = 0; i < a.length; i++)
            if (!a[i].equals(expected_ans[i])) {
                System.out.println("testMerge2 ERROR");
                return;
            }
        System.out.println("testMerge2 OK");
    }

    public static void main(String[] args) {
        testSortStrings1();
        testSortStrings2();

        testMerge1();
        testMerge2();
    }

}
