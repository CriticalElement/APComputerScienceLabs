import java.io.FileNotFoundException;

public class Runner 
{
	public static void main(String[] args)
	{
		final double T = 1_000_000_000; //simulation time in seconds
		
		final double dt = 25_000; //time step in seconds, one day
		
		final String fileName = "planets.txt";

		try {
			NBodySimulation sim = new NBodySimulation(fileName);
			sim.run(T, dt);
		}
		catch (FileNotFoundException e) {
			System.out.println("u suck :(");
		}
	}
}
