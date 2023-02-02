public class Point 
{
	private final double x, y;
	private boolean visited = false;

	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	public boolean isVisited()
	{
		return visited;
	}

	public void setVisited(boolean visited)
	{
		this.visited = visited;
	}

	/** get the Euclidean distance between two points */
	public double getDistance(Point other)
	{
		double xOff = Math.abs(x - other.getX());
		double yOff = Math.abs(y - other.getY());
		return Math.sqrt(Math.pow(xOff, 2) + Math.pow(yOff, 2));
	}
	
	@Override
	public String toString()
	{
		return String.format("(%s, %s)", x, y);
	}
}
