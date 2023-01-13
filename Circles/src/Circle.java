import java.awt.*;

public class Circle {
    private int x, y;
    private int dx = 0, dy = 0;
    private final int radius;
    private final Color color;

    public Circle(int x, int y, int radius, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
    }

    @SuppressWarnings("unused")
    public Circle(int x, int y, int dx, int dy, int radius, Color color) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.radius = radius;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void draw() {
        StdDraw.setPenColor(color);
        StdDraw.filledCircle(x, y, radius);
    }

    public void update(int size) {
        this.x += dx;
        this.y += dy;

        bounce(size);
    }

    public void bounce(int size) {
        if (x <= radius) {
            dx *= -1;
        }
        if (y <= radius) {
            dy *= -1;
        }
        if (x >= size - radius) {
            dx *= -1;
        }
        if (y >= size - radius) {
            dy *= -1;
        }
    }

    public boolean overlaps(Circle other) {
        int xDist = other.getX() - x;
        int yDist = other.getY() - y;
        double dist = Math.sqrt(xDist * xDist + yDist * yDist);
        return dist < other.getRadius() + radius;
    }

    public static void main(String[] args) {
        new Circle(0, 0, 1, new Color(255, 0, 0)).draw();
    }
}
