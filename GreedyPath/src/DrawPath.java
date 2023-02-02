import java.awt.Color;
import java.io.IOException;

/*
 * Uncomment when all classes are complete, won't compile otherwise
 */
public class DrawPath
{
	public static void main(String [] args) throws IOException
	{
		final String file = "mona-20k.txt"; //change to try other files

		Path p = new GreedyPath(file);

		final int delayMs = 1; // how long to wait, for animating the sequence of lines

		// First line should be the (x, y) of the lower-left coordinate of the bounding box
		double minX = p.getMinX();
		double minY = p.getMinY();

		// Next comes the (x, y) of the upper-right coordinate of the bounding box
		double maxX = p.getMaxX();
		double maxY = p.getMaxY();

		// Setup StdDraw's coordinate system, so it matches the bounding box
		StdDraw.setXscale(minX, maxX);
		StdDraw.setYscale(minY, maxY);

		// Read in the number of points in the path
		final int N = p.getNumPoints();

		double lastX = p.getPoint(0).getX(); //first points
		double lastY = p.getPoint(0).getY();

		// Used to interpolate from red to blue color as we draw lines
		double t = 0.0;
		double deltaT = 1.0 / (N - 2);

		StdDraw.show(delayMs);
		for (int i = 1; i < N; i++)
		{
			double x = p.getPoint(i).getX();
			double y = p.getPoint(i).getY();

			double r = (1 - t);
			double b = (t);
			StdDraw.setPenColor(new Color((float) r, 0.0f, (float) b));

			StdDraw.line(lastX, lastY, x, y);

			// noinspection ConstantConditions
			if (delayMs > 0)
				StdDraw.show(delayMs);

			lastX = x;
			lastY = y;

			t += deltaT;
		}

		StdDraw.setPenColor(StdDraw.BLACK);
		double totalDistance = p.getDistance();
		StdDraw.textLeft(minX, maxY * 1.02, "Distance: " + totalDistance);

		StdDraw.show(0);
	}
}
