import java.awt.*;
import java.util.*;

public class SandLab
{
	//add constants for particle types here
	public static final int EMPTY = 0;
	public static final int METAL = 1;
	public static final int SAND = 2;
	public static final int WATER = 3;

	//do not add any more fields!
	private final int[][] grid;
	private final SandDisplay display; //SandDisplay is the GUI class

	public SandLab(int numRows, int numCols)
	{
		String[] names = new String[4];

		names[EMPTY] = "Empty";
		names[METAL] = "Metal";
		names[SAND] = "Sand";
		names[WATER] = "Water";

		display = new SandDisplay("Falling Sand", numRows, numCols, names);
		grid = new int[numRows][numCols];
	}

	/** called when the user clicks on a location using the given tool */
	private void locationClicked(int row, int col, int tool)
	{
		grid[row][col] = tool;
	}

	/** copies each element of grid into the display */
	public void updateDisplay()
	{
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				switch (grid[i][j]) {
					case EMPTY:
						display.setColor(i, j, Color.BLACK);
						break;
					case METAL:
						display.setColor(i, j, new Color(80, 80, 80));
						break;
					case SAND:
						display.setColor(i, j, Color.YELLOW);
						break;
					case WATER:
						display.setColor(i, j, Color.BLUE);
						break;
				}
			}
		}
	}

	/** called repeatedly, causes one random particle to maybe do something */
	public void step()
	{
		Random random = new Random();

		int row = random.nextInt(grid.length - 1);
		int col = random.nextInt(grid[0].length);

		if (grid[row][col] == SAND) {
			int prevType = grid[row + 1][col];
			if (prevType == EMPTY || prevType == WATER) {
				grid[row + 1][col] = SAND;
				grid[row][col] = prevType;
			}
		}

		if (grid[row][col] == WATER) {
			ArrayList<int[]> availablePositions = new ArrayList<>();
			int[][] directions = new int[][] {{1, 0}, {0, -1}, {0, 1}};
			for (int[] dir : directions) {
				int newRow = row + dir[0];
				int newCol = col + dir[1];
				if (newRow >= 0 && newRow < grid.length) {
					if (newCol >= 0 && newCol < grid[0].length) {
						if (grid[newRow][newCol] == EMPTY) {
							availablePositions.add(new int[] {newRow, newCol});
						}
					}
				}
			}
			if (!availablePositions.isEmpty()) {
				int rand = random.nextInt(availablePositions.size());
				grid[row][col] = EMPTY;
				int[] newPos = availablePositions.get(rand);
				grid[newPos[0]][newPos[1]] = WATER;
			}
		}
	}

	//do not modify!
	public void run()
	{
		//noinspection InfiniteLoopStatement
		while (true)
		{
			for (int i = 0; i < display.getSpeed(); i++)
				step();

			updateDisplay();

			display.repaint();

			display.pause(1);  //wait for redrawing and for mouse

			int[] mouseLoc = display.getMouseLocation();

			if (mouseLoc != null)  //test if mouse clicked
				locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
		}
	}
}
