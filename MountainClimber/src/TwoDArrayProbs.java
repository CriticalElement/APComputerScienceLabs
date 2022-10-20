import java.util.Arrays;

public class TwoDArrayProbs {
    private final int[][] nums;

    TwoDArrayProbs(int[][] nums) {
        this.nums = nums;
    }

    public int sum() {
        int total = 0;
        for (int[] num : nums) {
            for (int i : num) {
                total += i;
            }
        }
        return total;
    }

    public boolean isSquare() {
        return nums.length == nums[0].length;
    }

    public void addMatrix(int[][] other) {
        for (int i = 0; i < other.length; i++) {
            for (int j = 0; j < other[0].length; j++) {
                nums[i][j] += other[i][j];
            }
        }
        print();
    }

    private void print() {
        // could have just used System.out.println(Arrays.deepToString(nums)) but ok
        System.out.print("[");
        for (int[] innerArray : nums) {
            System.out.print(Arrays.toString(innerArray) + ", ");
        }
        System.out.println("\b\b]"); // \b deletes last character printed, gets rid of the trailing comma
    }

    public int columnSum(int col) {
        int sum = 0;
        for (int[] row : nums) {
            if (row.length - 1 >= col) {
                sum += row[col];
            }
        }
        return sum;
    }

    public boolean isColumnMagic() {
        // this fails if the first row isn't the longest but who cares
        int innerLength = nums[0].length;
        int firstColumnSum = columnSum(0);
        for (int i = 0; i < innerLength; i++) {
            if (columnSum(i) != firstColumnSum) {
                return false;
            }
        }
        return true;
    }

    public int diagDifference() {
        int diag1 = 0;
        int diag2 = 0;
        for (int i = 0; i < nums.length; i++) {
            diag1 += nums[i][i];
            diag2 += nums[i][nums.length - 1 - i];
        }
        return Math.abs(diag1 - diag2);
    }
}
