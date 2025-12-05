package sorting;
import java.util.Arrays;

public class MySortingAlgs {

    public static void sortStrings(String[] a) {
        // TODO implement me
        if (a.length == 0 || a.length == 1)
            return;
        Arrays.sort(a, (o1,o2) -> {
            if (o1.length() != o2.length())
                return o1.length() - o2.length();
            return o1.compareTo(o2);
        });
    }

	/**
	 * Assumption arr[start...mid-1] is sorted and arr[mid...end] is sorted.
     * The function merges the two parts into a sorted subarray arr[start...end]
     * All elements outside arr[start...end] remain unchanged
	 */
	public static <T extends Comparable<T>> void merge(T[] arr, int start, int mid, int end) {
        // TODO implement me
        T[] left = Arrays.copyOfRange(arr, start, mid);
        T[] right = Arrays.copyOfRange(arr, mid, end+1);
        int i = 0; // index for left
        int j = 0; // index for right
        int k = start; // index for arr
        while (i < left.length && j < right.length) {
            if (left[i].compareTo(right[j]) <= 0 )
                arr[k++] = left[i++];
            else
                arr[k++] = right[j++];
        }
        // Add remaining elements to arr
        while (i < left.length) 
            arr[k++] = left[i++];
        while (j < right.length)
            arr[k++] = right[j++];
	}


	/**
	 * sorts the subarray arr[start...end] using the Merge Sort algorithm
	 */
    public static <T extends Comparable<T>> void mergeSort(T[] arr, int start, int end) {
        if (start == end)
            return;

        int mid = (start + end) / 2;
        mergeSort(arr, start, mid-1);
        mergeSort(arr, mid, end);
        merge(arr, start, mid, end);
    }

    /**
     * sorts the subarray arr using the Merge Sort algorithm
     */
    public static <T extends Comparable<T>> void mergeSort(T[] arr) {
        mergeSort(arr,0,arr.length-1);
    }

}
