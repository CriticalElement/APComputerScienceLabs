import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;

public class MapTester
{
    public static void main(String[] args) throws FileNotFoundException {
        MapIllustrator map = new MapIllustrator("Colorado_480x480.txt");
        
        // construct DrawingPanel (the GUI that displays the map) - don't mess with this
        DrawingPanel panel = new DrawingPanel(map.getRows(), map.getCols());
        Graphics     g     = panel.getGraphics();

		/*
		 * add test code below
		 */

        map.drawMap(g);
        testBestPath(map, g);
    }
    
    /** test the drawPath method, find path starting at row 200 */
    @SuppressWarnings("unused")
    public static void testDrawPath(MapIllustrator map, Graphics g) {
		map.drawMap(g); //draw the map
		
        g.setColor(Color.GREEN); //can set the color of the 'brush' before drawing, the method doesn't need to worry about it
        
        int totalChange = map.drawPath(g, 200); //find greedy path for row 200
        
        System.out.println("Lowest-elevation-change path starting at row 200: " + totalChange); //should be ~18,000-19,000 
    }
    
    /** test the getIndexOfLowest method, which finds the overall best path / starting row */
    public static void testBestPath(MapIllustrator map, Graphics g) {
        g.setColor(Color.RED); //set back to red to draw all possible greedy paths

        int bestRow = map.getIndexOfLowestPath(g); //run a greedy walk for all rows, return BEST row
        
        g.setColor(Color.GREEN); //set brush to green for drawing best path
        
        int totalChange = map.drawPath(g, bestRow); //draw and get total for just the overall best row
        
        System.out.println("Overall best lowest-elevation-change path at row: " + bestRow + ", total change of: " + totalChange);
    }
}