public class MobileSprite extends Sprite {
    private double vx, vy;

    public MobileSprite(double x, double y, int width, int height, String image, double vx, double vy) {
        super(x, y, width, height, image);
        this.vx = vx;
        this.vy = vy;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    @Override
    public void step(World world) {
        setX(getX() + vx);
        setY(getY() + vy);
    }
}
