public class HeavySprite extends BouncingSprite {
    @SuppressWarnings("FieldCanBeLocal")
    private final double ay = -0.05; // bouncing on the floor will not work properly unless vertical acceleration is
    //                                  implemented
    public HeavySprite(double x, double y, int width, int height, String image, double vx, double vy) {
        super(x, y, width, height, image, vx, vy);
    }

    @Override
    public void step(World world) {
        super.step(world);
        setVy(getVy() + ay);
    }
}
