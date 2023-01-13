import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class CircleAnimations
{
	private final ArrayList<Circle> circles; //the circles to animate
	private final int size;    //canvas width and height (will be square)
	private final Random rng;     //use to make random numbers

	/** create a drawing pane of a particular size */
	public CircleAnimations(int s) {
		circles = new ArrayList<>();
		size = s;
		rng = new Random();

		//don't mess with this
		StdDraw.setCanvasSize(size, size); //set up drawing canvas
		StdDraw.setXscale(0, size);        //<0, 0> is bottom left.  <size-1, size-1> is top right
		StdDraw.setYscale(0, size);
	}

	public void drawCircles() {
		for (Circle circle : circles) {
			circle.draw();
		}
	}

	@SuppressWarnings("unused")
	public void addCircles() {
		addCircles(3);
	}

	public void addCircles(int num) {
		for (int i = 0; i < num; i++) {
			int newRadius = rng.nextInt(75) + 1;
			int newX = rng.nextInt(size - newRadius * 2) + newRadius;
			int newY = rng.nextInt(size - newRadius * 2) + newRadius;
			Color newColor = new Color(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
			circles.add(new Circle(newX, newY, newRadius, newColor));
		}
		drawCircles();
	}

	public boolean hasOverlaps(Circle circle) {
		for (Circle other : circles) {
			if (circle.overlaps(other)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unused")
	public void noOverlapping(int number) {
		for (int i = 0; i < number; i++) {
			Circle newCircle;
			do {
				int newRadius = rng.nextInt(75) + 1;
				int newX = rng.nextInt(size - newRadius * 2) + newRadius;
				int newY = rng.nextInt(size - newRadius * 2) + newRadius;
				Color newColor = new Color(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
				newCircle = new Circle(newX, newY, newRadius, newColor);
			} while (hasOverlaps(newCircle));
			circles.add(newCircle);
		}
		drawCircles();
	}

	@SuppressWarnings({"InfiniteLoopStatement", "deprecation"})
	public void movingCircles() {
		if (circles.isEmpty()) {
			addCircles(10);
		}

		for (Circle circle : circles) {
			circle.setDx(rng.nextInt(11) - 5);
			circle.setDy(rng.nextInt(11) - 5);
		}

		while (true) {
			drawCircles();
			for (Circle circle : circles) {
				circle.update(size);
			}
			if (StdDraw.isMousePressed()) {
				for (int i = circles.size() - 1; i >= 0; i--) {
					int distX = (int) (StdDraw.mouseX() - circles.get(i).getX());
					int distY = (int) (StdDraw.mouseY() - circles.get(i).getY());
					double dist = Math.sqrt(distX * distX + distY * distY);
					if (dist < circles.get(i).getRadius()) {
						circles.remove(i);
					}
				}
			}
			StdDraw.show(10);
			StdDraw.clear();
		}
	}
}
