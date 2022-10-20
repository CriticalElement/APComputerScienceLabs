import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class MapIllustrator
{
	/** the 2D array containing the elevations */
	private final int[][] grid;

	/** constructor, parses input from the file into grid */
	public MapIllustrator(String fileName) throws FileNotFoundException
	{
		Scanner scan = new Scanner(new File(fileName));
		grid = new int[scan.nextInt()][scan.nextInt()];
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				grid[i][j] = scan.nextInt();
			}
		}
	}

	/** @return the min value in the entire grid */
	public int findMin()
	{
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < getRows(); i++) {
			int minCols = Integer.MAX_VALUE;
			for (int j = 0; j < getCols(); j++) {
				if (grid[i][j] < minCols) {
					minCols = grid[i][j];
				}
			}
			if (minCols < min) {
				min = minCols;
			}
		}
		return min;
	}

	/** @return the max value in the entire grid */
	public int findMax()
	{
		int max = 0;
		for (int i = 0; i < getRows(); i++) {
			int maxCols = 0;
			for (int j = 0; j < getCols(); j++) {
				if (grid[i][j] > maxCols) {
					maxCols = grid[i][j];
				}
			}
			if (maxCols > max) {
				max = maxCols;
			}
		}
		return max;
	}

	/**
	 * Draws the grid using the given Graphics object.
	 * Colors should be grayscale values 0-255, scaled based on min/max values in grid
	 */
	public void drawMap(Graphics g)
	{
		int max = findMax();
		int min = findMin();
		double scaleValue = max - min;
		scaleValue = 255 / scaleValue;
		for (int r = 0; r < getRows(); r++) {
			for (int c = 0; c < getCols(); c++) {
				int color = (int) ((grid[r][c] - min) * scaleValue);
				g.setColor(new Color(color, color, color));
				g.fillRect(c, r, 1, 1);
			}
		}

	}

	/**
	 * Find a path from West-to-East starting at given row.
	 * Choose a forward step out of 3 possible forward locations, using greedy method described in assignment.
	 * @return the total change in elevation traveled from West-to-East
	 */
	public int drawPath(Graphics g, int row)
	{
		g.fillRect(0, row, 1, 1);
		int rows = getRows();
		Random random = new Random();
		int elevationChange = 0;
		for (int col = 1; col < getCols(); col++) {
			int topCond;
			if (row == 0) {
				topCond = Integer.MAX_VALUE; // value that will never get picked
			}
			else {
				topCond = Math.abs(grid[row - 1][col] - grid[row][col - 1]);
			}
			int fwdCond = Math.abs(grid[row][col] - grid[row][col - 1]);
			int bottomCond;
			if (row == rows - 1) {
				bottomCond = Integer.MAX_VALUE;
			}
			else {
				bottomCond = Math.abs(grid[row + 1][col] - grid[row][col - 1]);
			}

			if (fwdCond <= topCond && fwdCond <= bottomCond) {
				elevationChange += fwdCond;
			}
			else if (topCond == bottomCond) {
				if (random.nextInt(2) == 0) {
					row--;
					elevationChange += topCond;
				}
				else {
					row++;
					elevationChange += bottomCond;
				}
			}
			else if (topCond < bottomCond) {
				row--;
				elevationChange += topCond;
			}
			else {
				row++;
				elevationChange += bottomCond;
			}
			g.fillRect(col, row, 1, 1);
		}
		return elevationChange;
	}

	/** @return the index of the starting row for the lowest-elevation-change path in the entire grid. */
	public int getIndexOfLowestPath(Graphics g)
	{
		int min = Integer.MAX_VALUE;
		int bestRow = 0;
		for (int i = 0; i < getRows(); i++) {
			int test = drawPath(g, i);
			if (test < min) {
				min = test;
				bestRow = i;
			}
		}
		return bestRow;
	}

	/** return the number of rows in grid */
	public int getRows()
	{
		return grid.length;
	}

	/** return the number of columns in grid (assumed rectangular) */
	public int getCols()
	{
		return grid[0].length;
	}
}
