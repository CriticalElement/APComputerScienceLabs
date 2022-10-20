import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NBodySimulation
{
	private final Body[] bodies; // stores all the bodies in the simulation
	private final double uRadius; // radius of the universe

	public NBodySimulation(String nameOfFile) throws FileNotFoundException {
		// file providing the input
		Scanner scan = new Scanner(new File(nameOfFile));
		// number of bodies in this simulation
		int numBodies = scan.nextInt();
		bodies = new Body[numBodies];
		uRadius = scan.nextDouble();
		int i = 0;
		while (scan.hasNextLine()) {
			bodies[i] = new Body(scan.nextDouble(), scan.nextDouble(), scan.nextDouble(),
								 scan.nextDouble(), scan.nextDouble(), scan.next());
			i++;
		}

		initCanvas(); //don't move, should be the last line of the constructor
	}

	/** initialize the drawing canvas */
	private void initCanvas()
	{
		StdDraw.setScale(-uRadius, uRadius); //set canvas size / scale
		StdDraw.picture(0, 0, "starfield.jpg"); //paint background

		//below is a for-each loop, which we will cover in the next lab
		//more info is available in the powerpoints, for the curious
		for (Body body : bodies)
			body.draw();
	}

	/**
	 * run the n-bodies simulation
	 * @param T total time to run the simulation, in seconds
	 * @param dt time step, in seconds
	 */
	public void run(double T, double dt)
	{
		for (double t = 0; t <= T; t += dt) {
			// calculate physics
			for (Body body: bodies) {
				body.setNetForce(bodies);
				body.update(dt);
			}
			StdDraw.picture(0, 0, "starfield.jpg");
			for (Body body: bodies) {
				body.draw();
			}
			StdDraw.show(10);
		}
	}
}
