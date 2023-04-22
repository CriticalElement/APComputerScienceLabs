import java.util.Arrays;
import java.util.Random;

@SuppressWarnings("unused")
public class SearchSort {
    private int[] nums;

    public SearchSort(int[] nums) {
        this.nums = nums;
    }

    public SearchSort(int size) {
        this.nums = new int[size];
        initArray();
    }

    public void initArray() {
        Random random = new Random();
        for (int i = 0; i < nums.length; i++) {
            nums[i] = random.nextInt(1000) + 1;
        }
    }

    private void swap(int i, int j) {
        int temp = nums[j];
        nums[j] = nums[i];
        nums[i] = temp;
    }

    /* searching algorithms */

    public int linearSearch(int key) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == key) {
                return i;
            }
        }
        return -1;
    }

    public int linearSearchRecur(int key) {
        return linearSearchRecur(key, 0);
    }

    private int linearSearchRecur(int key, int index) {
        if (index >= nums.length) {
            return -1;
        }

        if (nums[index] == key) {
            return index;
        }

        return linearSearchRecur(key, index + 1);
    }

    public int binarySearch(int key) {
        Arrays.sort(nums);
        int start = 0;
        int end = nums.length - 1;

        while (start <= end) {
            int median = (end - start) / 2 + start;

            if (nums[median] == key) {
                return median;
            }
            else if (key < nums[median]) {
                end = median - 1;
            }
            else {
                start = median + 1;
            }
        }
        return -1;
    }

    public int binarySearchRecur(int key) {
        Arrays.sort(nums);
        return binarySearchRecur(key, 0, nums.length - 1);
    }

    private int binarySearchRecur(int key, int start, int end) {
        if (start > end) {
            return -1;
        }

        int median = (end - start) / 2 + start;
        if (nums[median] == key) {
            return median;
        }
        else if (key < nums[median]) {
            return binarySearchRecur(key, start, median - 1);
        }
        else {
            return binarySearchRecur(key, median + 1, end);
        }
    }

    /* sorting algorithms */

    public void bubbleSort() {
        bubbleSort(false);
    }

    public void bubbleSort(boolean print) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = 1; j < nums.length; j++) {
                if (nums[j] < nums[j - 1]) {
                    swap(j, j - 1);
                    if (print)
                        System.out.println(Arrays.toString(nums));
                }
            }
        }
    }

    public void selectionSort() {
        selectionSort(false);
    }

    public void selectionSort(boolean print) {
        for (int i = 0; i < nums.length - 1; i++) {
            int minIndex = i;

            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] < nums[minIndex]) {
                    minIndex = j;
                }
            }

            swap(i, minIndex);
            if (print)
                System.out.println(Arrays.toString(nums));
        }
    }

    public void insertionSort() {
        insertionSort(false);
    }

    public void insertionSort(boolean print) {
        for (int i = 1; i < nums.length; i++) {
            int j = i;
            while (nums[j] < nums[j - 1]) {
                swap(j, j - 1);
                if (print)
                    System.out.println(Arrays.toString(nums));
                j--;
                if (j == 0) {
                    break;
                }
            }
        }
    }

    public void mergeSort() {
        mergeSort(false);
    }

    public void mergeSort(boolean print) {
        nums = mergeSortRecur(nums, print);
    }

    private int[] mergeSortRecur(int[] list, boolean print) {
        if (list.length != 1) {
            int[] left = Arrays.copyOfRange(list, 0, list.length / 2);
            int[] right = Arrays.copyOfRange(list, list.length / 2, list.length);
            if (print) {
                System.out.println(Arrays.toString(left) + " " + Arrays.toString(right));
            }
            int[] splitLeft = mergeSortRecur(left, print);
            int[] splitRight = mergeSortRecur(right, print);
            int[] sorted = new int[splitLeft.length + splitRight.length];
            int i = 0;
            int j = 0;
            while (i < splitLeft.length && j < splitRight.length) {
                if (splitLeft[i] <= splitRight[j]) {
                    sorted[i + j] = splitLeft[i];
                    i++;
                }
                else {
                    sorted[i + j] = splitRight[j];
                    j++;
                }
            }
            if (i == splitLeft.length) {
                System.arraycopy(splitRight, j, sorted, i + j, splitRight.length - j);
            }
            else if (j == splitRight.length) {
                System.arraycopy(splitLeft, i, sorted, i + j, splitLeft.length - i);
            }
            if (print)
                System.out.println(Arrays.toString(sorted));
            return sorted;
        }
        return list;
    }

    public int[] getNums() {
        return nums;
    }

    public static void main(String[] args) {
        /* test all algorithms */

        SearchSort searchSort;
        Random random = new Random();
        int key;

        /* test searching algorithms */

        searchSort = new SearchSort(8);
        System.out.println("Linear Search: " + Arrays.toString(searchSort.getNums()));
        key = searchSort.getNums()[random.nextInt(8)];
        System.out.println("Key: " + key + " -> index " + searchSort.linearSearch(key));
        key = random.nextInt(1000) + 1;
        System.out.println("Key: " + key + " -> index " + searchSort.linearSearch(key));
        searchSort = new SearchSort(8);
        System.out.println("Recursive Linear Search: " + Arrays.toString(searchSort.getNums()));
        key = searchSort.getNums()[random.nextInt(8)];
        System.out.println("Key: " + key + " -> index " + searchSort.linearSearchRecur(key));
        key = random.nextInt(1000) + 1;
        System.out.println("Key: " + key + " -> index " + searchSort.linearSearchRecur(key));
        searchSort = new SearchSort(8);
        searchSort.binarySearch(-1);
        System.out.println("Binary Search: " + Arrays.toString(searchSort.getNums()));
        key = searchSort.getNums()[random.nextInt(8)];
        System.out.println("Key: " + key + " -> index " + searchSort.binarySearch(key));
        key = random.nextInt(1000) + 1;
        System.out.println("Key: " + key + " -> index " + searchSort.binarySearch(key));
        searchSort = new SearchSort(8);
        searchSort.binarySearch(-1);
        System.out.println("Recursive Binary Search: " + Arrays.toString(searchSort.getNums()));
        key = searchSort.getNums()[random.nextInt(8)];
        System.out.println("Key: " + key + " -> index " + searchSort.binarySearchRecur(key));
        key = random.nextInt(1000) + 1;
        System.out.println("Key: " + key + " -> index " + searchSort.binarySearchRecur(key) + "\n");

        /* test sorting algorithms */

        searchSort = new SearchSort(8);
        System.out.println("Bubble Sort:");
        System.out.println(Arrays.toString(searchSort.getNums()));
        searchSort.bubbleSort(true);
        System.out.println();
        searchSort = new SearchSort(8);
        System.out.println("Selection Sort:");
        System.out.println(Arrays.toString(searchSort.getNums()));
        searchSort.selectionSort(true);
        System.out.println();
        searchSort = new SearchSort(8);
        System.out.println("Insertion Sort:");
        System.out.println(Arrays.toString(searchSort.getNums()));
        searchSort.insertionSort(true);
        System.out.println();
        searchSort = new SearchSort(8);
        System.out.println("Merge Sort:");
        System.out.println(Arrays.toString(searchSort.getNums()));
        searchSort.mergeSort(true);
    }
}
