import java.awt.event.KeyEvent;

public class ControllableSprite extends MobileSprite {
    public ControllableSprite(double x, double y, int width, int height, String image, double vx, double vy) {
        super(x, y, width, height, image, vx, vy);
    }

    @Override
    public void step(World world) {
        double newVx = 0;
        double newVy = 0;
        if (StdDraw.isKeyPressed(KeyEvent.VK_UP))
            newVy += 2;
        if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN))
            newVy -= 2;
        if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT))
            newVx += 2;
        if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT))
            newVx -= 2;

        setVx(newVx);
        setVy(newVy);

        super.step(world);

        double halfWidth = (double) getWidth() / 2;
        double halfHeight = (double) getHeight() / 2;
        setX(Math.max(0, getX() - halfWidth));
        setX(Math.min(world.getWidth() - halfWidth, getX() + halfWidth));
        setY(Math.max(0, getY() - halfHeight));
        setY(Math.min(world.getHeight() - halfHeight, getY() + halfHeight));
    }
}
