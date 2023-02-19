public class BouncingSprite extends MobileSprite {
    public BouncingSprite(double x, double y, int width, int height, String image, double vx, double vy) {
        super(x, y, width, height, image, vx, vy);
    }

    @Override
    public void step(World world) {
        bounce(world);
        super.step(world);
    }

    private void bounce(World world) {
        if (getX() - (double) getWidth() / 2 <= 0)
            setVx(-getVx());
        if (getX() + (double) getWidth() / 2 >= world.getWidth())
            setVx(-getVx());
        if (getY() - (double) getHeight() / 2 <= 0)
            setVy(-getVy());
        if (getY() + (double) getHeight() / 2 >= world.getHeight())
            setVy(-getVy());
    }
}
