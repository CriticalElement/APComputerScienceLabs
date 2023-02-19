import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class World
{
	private final List<Sprite> sprites;
	private final int width;
	private final int height;

	/** construct a world 600x600 */
	public World() {
		this(600, 600);
	}

	public World(int h, int w)
	{
		height = h;
		width  = w;
		
		sprites = new ArrayList<>();

		StdDraw.setCanvasSize(width, height);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
		StdDraw.clear(Color.BLACK);

		addSprites();
	}

	private void addSprites()
	{
		StationarySprite sprite = new StationarySprite(300, 300, 50, 50, "square.png");
		sprites.add(sprite);

		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			double x = random.nextInt(500) + 50;
			double y = random.nextInt(500) + 50;
			double vx = random.nextDouble() * 6 - 3;
			double vy = random.nextDouble() * 6 - 3;
			sprites.add(new BouncingSprite(x, y, 50, 50, "circle.png", vx, vy));
		}

		HeavySprite heavySprite = new HeavySprite(400, 400, 50, 50, "triangle.png", 2, 0);
		sprites.add(heavySprite);
		ControllableSprite player = new ControllableSprite(300, 200, 50, 50, "star.png", 0, 0);
		sprites.add(player);
	}

	/** add a sprite to the simulation */
	public void add(Sprite sprite)
	{
		this.sprites.add(sprite);
	}

	/** ask all sprites in simulation to update themselves */
	public void stepAll()
	{
		for (Sprite sprite : sprites)
			sprite.step(this);
	}

	/** get the width of the world */
	public int getWidth()
	{
		return width;
	}

	/** get the height of the world */
	public int getHeight()
	{
		return height;
	}

	/** get the number of sprites in the simulation currently */
	public int getNumSprites()
	{
		return sprites.size();
	}

	/** get the sprite at the given index */
	public Sprite getSprite(int index)
	{
		return sprites.get(index);
	}

	/** run the simulation, i.e. the main game loop */
	public void run()
	{
		//noinspection InfiniteLoopStatement
		while (true)
		{
			this.stepAll();
			this.drawAll();

			//noinspection deprecation
			StdDraw.show(10); //don't worry about warning if using Eclipse
			StdDraw.clear(Color.BLACK);
		}
	}

	/** draw all sprites in the simulation at their current positions */
	public void drawAll() {
		for (Sprite sprite : this.sprites)
			sprite.draw();
	}

	public static void main(String[] args)
	{
		World world = new World(600, 600);
		world.run();
	}
}
