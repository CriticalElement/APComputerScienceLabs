import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Path
{
	private final Point[] points;
	private final double  minX, minY; //min X and Y values, for setting canvas scale
	private final double  maxX, maxY; //maxes

	/** construct a path from a given file */
	public Path(String fileName) throws FileNotFoundException
	{
		Scanner scan = new Scanner(new File(fileName));
		int numPoints = scan.nextInt();
		points = new Point[numPoints];
		double minX = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;

		for (int i = 0; i < numPoints; i++)
		{
			double x = scan.nextDouble();
			double y = scan.nextDouble();
			minX = Math.min(minX, x);
			maxX = Math.max(maxX, x);
			minY = Math.min(minY, y);
			maxY = Math.max(maxY, y);
			points[i] = new Point(x, y);
		}

		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public double getMinX()
	{
		return minX;
	}

	public double getMaxX()
	{
		return maxX;
	}

	public double getMinY()
	{
		return minY;
	}

	public double getMaxY()
	{
		return maxY;
	}

	public Point getPoint(int i)
	{
		return points[i];
	}

	public int getNumPoints()
	{
		return points.length;
	}

	/** returns the distance traveled going point to point, in order given in file */
	public double getDistance()
	{
		Point start = points[0];
		double dist = 0;

		for (int i = 1; i < points.length; i++)
		{
			dist += start.getDistance(points[i]);
			start = points[i];
		}

		return dist;
	}

	@Override
	public String toString()
	{
		return "Distance: " + getDistance();
	}
}
