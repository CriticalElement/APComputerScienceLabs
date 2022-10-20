public class Body
{
	public static final double G = 6.67E-11;  //Newtons' gravitational constant
	private double x, y; //planet's x and y coordinate position
	private double xVelocity, yVelocity;
	private final double mass;
	private double xNetForce, yNetForce; //net forces action on this planet
	private final String image; //image of this planet (for drawing)

	public Body(double x, double y, double xVelocity, double yVelocity, double mass, String image)
	{
		this.x = x;
		this.y = y;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		this.mass = mass;
		this.image = image;
	}

	public static void main(String[] args) {
		Body body = new Body(0.5, 0.5, 0, 0, 0, "earth.gif");
		Body body2 = new Body(1, 1, 0, 0, 0, "venus.gif");
		System.out.println(body.getDistance(body2));
		Body body3 = new Body(0, 0, 0, 0, 6e24, "");
		Body body4 = new Body(1, 1, 0, 0, 7e24, "");
		System.out.println(body3.getPairwiseForce(body4));
		System.out.println(body3.getPairwiseForceX(body4));
		// body.draw();
	}



	/*
	 * TODO - student written methods here
	 */

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getMass() {
		return mass;
	}

	private double getDistance(Body other) {
		double dx = other.getX() - x;
		double dy = other.getY() - y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	private double getPairwiseForce(Body other) {
		double product = mass * other.getMass() * G;
		product /= Math.pow(getDistance(other), 2);
		return product;
	}

	private double getPairwiseForceX(Body other) {
		double force = getPairwiseForce(other);
		double dx = other.getX() - x;
		double r = getDistance(other);
		return force * Math.cos(Math.acos(dx / r));
	}

	private double getPairwiseForceY(Body other) {
		double force = getPairwiseForce(other);
		double dy = other.getY() - y;
		double r = getDistance(other);
		return force * Math.sin(Math.asin(dy / r));
	}

	/** calculates / sets the net force exerted on this body by all the (input) bodies */
	public void setNetForce(Body[] bodies)
	{
		xNetForce = 0;
		yNetForce = 0;

		for (Body body: bodies) {
			if (body != this) {
				xNetForce += getPairwiseForceX(body);
				yNetForce += getPairwiseForceY(body);
			}
		}
	}

	/** updates this body's accel, vel, and position, given the time step */
	public void update(double dt)
	{
		double xAccel = xNetForce / mass;
		double yAccel = yNetForce / mass;

		xVelocity += xAccel * dt;
		yVelocity += yAccel * dt;

		x += xVelocity * dt;
		y += yVelocity * dt;
	}

	/** Draws the body using the StdDraw library file's drawing method */
	public void draw()
	{
		StdDraw.picture(x, y, image);
	}
}