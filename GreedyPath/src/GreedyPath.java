import java.io.FileNotFoundException;

public class GreedyPath extends Path
{
    private final Point[] greedyPoints;

    public GreedyPath(String fileName) throws FileNotFoundException
    {
        super(fileName);
        greedyPoints = new Point[getNumPoints()];

        findPath();
    }

    private void findPath()
    {
        Point start = super.getPoint(0);
        greedyPoints[0] = start;
        start.setVisited(true);

        for (int i = 1; i < getNumPoints(); i++)
        {
            double minDist = Double.MAX_VALUE;
            Point chosenPoint = null;

            for (int j = 0; j < getNumPoints(); j++)
            {
                Point point = super.getPoint(j);
                double dist = start.getDistance(point);
                if (dist < minDist && !point.isVisited())
                {
                    minDist = dist;
                    chosenPoint = point;
                }
            }

            assert chosenPoint != null;
            greedyPoints[i] = chosenPoint;
            start = chosenPoint;
            chosenPoint.setVisited(true);
        }
    }

    @Override
    public double getDistance()
    {
        double dist = 0;
        Point start = greedyPoints[0];

        for (int i = 1; i < greedyPoints.length; i++)
        {
            dist += start.getDistance(greedyPoints[i]);
            start = greedyPoints[i];
        }

        return dist;
    }

    @Override
    public Point getPoint(int i)
    {
        return greedyPoints[i];
    }

    @Override
    public String toString()
    {
        return "Distance: " + getDistance();
    }
}
