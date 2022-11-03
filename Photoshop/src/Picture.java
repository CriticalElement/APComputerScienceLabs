import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.UIManager;

/**
 * A class that represents a picture made up of a rectangle of {@link Pixel}s
 */
public class Picture {

    /** The 2D array of pixels that comprise this picture */
	private Pixel[][] pixels;

    private PictureViewer viewer = null;

    /**
     * Creates a Picture from an image file in the "images" directory
     * @param picture The name of the file to load
     */
    public Picture(String picture) {
        File file = new File("images/" +picture);
        BufferedImage image;
        if (!file.exists()) throw new RuntimeException("No picture at the location "+file.getPath()+"!");
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        pixels = new Pixel[image.getHeight()][image.getWidth()];
        for (int y = 0; y<pixels.length; y++) {
            for (int x = 0; x<pixels[y].length; x++) {
                int rgb = image.getRGB(x, y);
                /*
                 * For the curious - BufferedImage saves an image's RGB info into a hexadecimal integer
                 * The below extracts the individual values using bit-shifting and bit-wise ANDing with all 1's
                 */
                pixels[y][x] = new Pixel((rgb>>16)&0xff, (rgb>>8)&0xff, rgb&0xff);
            }
        }
    }

    /**
     * Creates a solid-color Picture of a given color, width, and height
     * @param red The red value of the color
     * @param green The green value of the color
     * @param blue The blue value of the color
     * @param height The height of the Picture
     * @param width The width of the Picture
     */
    public Picture(int red, int green, int blue, int height, int width) {
        pixels = new Pixel[height][width];
        for (int y = 0; y<pixels.length; y++) {
            for (int x = 0; x<pixels[y].length; x++) {
                pixels[y][x] = new Pixel(red, green, blue);
            }
        }
    }

    /**
     * Creates a solid white Picture of a given width and height
     * @param height The height of the Picture
     * @param width The width of the Picture
     */
    public Picture(int height, int width) {
        this(Color.WHITE, height, width);
    }

    /**
     * Creates a solid-color Picture of a given color, width, and height
     * @param color The {@link Color} of the Picture
     * @param width The width of the Picture
     * @param height The height of the Picture
     */
    public Picture(Color color, int height, int width) {
        this(color.getRed(), color.getGreen(), color.getBlue(), height, width);
    }

    /**
     * Creates a Picture based off of an existing {@link Pixel} 2D array
     * @param pixels A rectangular 2D array of {@link Pixel}s. Must be at least 1x1
     */
    public Picture(Pixel[][] pixels) {
        if (pixels.length==0 || pixels[0].length==0) throw new RuntimeException("Can't have an empty image!");
        int width = pixels[0].length;
        for (int i = 0; i<pixels.length; i++) if (pixels[i].length!=width)
            throw new RuntimeException("Pictures must be rectangles. pixels[0].length!=pixels["+i+"].length!");
        this.pixels = new Pixel[pixels.length][width];
        for (int i = 0; i<pixels.length; i++) {
            for (int j = 0; j<pixels[i].length; j++) {
                this.pixels[i][j] = new Pixel(pixels[i][j].getColor());
            }
        }
    }

    public Picture(Pixel[][] pixels, PictureViewer viewer) {
        if (pixels.length==0 || pixels[0].length==0) throw new RuntimeException("Can't have an empty image!");
        int width = pixels[0].length;
        for (int i = 0; i<pixels.length; i++) if (pixels[i].length!=width)
            throw new RuntimeException("Pictures must be rectangles. pixels[0].length!=pixels["+i+"].length!");
        this.pixels = new Pixel[pixels.length][width];
        for (int i = 0; i<pixels.length; i++) {
            for (int j = 0; j<pixels[i].length; j++) {
                this.pixels[i][j] = new Pixel(pixels[i][j].getColor());
            }
        }
        this.viewer = viewer;
    }

    /**
     * Creates a Picture based off of an existing Picture
     * @param picture The Picture to copy
     */
    public Picture(Picture picture)
    {
        this(picture.getPixels(), picture.getViewer());
    }

    public PictureViewer getViewer() {
        return viewer;
    }

    /**
     * Gets the width of the Picture
     * @return The width of the Picture
     */
    public int getWidth() {
        return pixels[0].length;
    }

    /**
     * Gets the height of the Picture
     * @return The height of the Picture
     */
    public int getHeight() {
        return pixels.length;
    }

    /**
     * Gets the {@link Pixel} at a given coordinate
     * @param x The x location of the {@link Pixel}
     * @param y The y location of the {@link Pixel}
     * @return The {@link Pixel} at the given location
     */
    public Pixel getPixel(int x, int y) {
        if (x>=getWidth() || y>=getHeight() || x<0 || y<0) throw new RuntimeException("No pixel at ("+x+", "+y+")");
        return pixels[y][x];
    }

    /**
     * Sets the {@link Pixel} at a given coordinate
     * @param x The x location of the {@link Pixel}
     * @param y The y location of the {@link Pixel}
     * @param pixel The new {@link Pixel}
     */
    public void setPixel(int x, int y, Pixel pixel) {
        if (x>=getWidth() || y>=getHeight() || x<0 || y<0) throw new RuntimeException("No pixel at ("+x+", "+y+")");
        if (pixel==null) throw new NullPointerException("Pixel is null"); //guard is required because pixel's value isn't used in this method
        pixels[y][x] = pixel;
    }

    /**
     * Opens a {@link PictureViewer} to view this Picture
     * @return the {@link PictureViewer} viewing the Picture
     */
    public PictureViewer view()
    {
        this.viewer = new PictureViewer(this);
        return this.viewer;
    }

    public boolean isVisible()
    {
        return this.viewer.isVisible();
    }

    public void hangUntilClosed()
    {
        try {
            while (isVisible()) {
                // noinspection BusyWait lol
                Thread.sleep(1000); // bad practice but who cares- I can't be bothered to implement it properly
            }
        }
        catch (InterruptedException e) {
            System.out.println("Interrupted, continuing normally.");
        }
    }

	/**
	 * Save the image on disk as a JPEG
	 * Call programmatically on a Picture object, it will prompt you to choose a save location
	 * In the save dialogue window, specify the file AND extension (e.g. "lilies.jpg")
	 * Extension must be .jpg as ImageIO is expecting to write a jpeg
	 */
    @SuppressWarnings("unused")
	public void save()
	{
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
		catch(Exception e) {
	        e.printStackTrace();
	    }
		
		BufferedImage image = new BufferedImage(this.pixels[0].length, this.pixels.length, BufferedImage.TYPE_INT_RGB);

		for (int r = 0; r < this.pixels.length; r++)
			for (int c = 0; c < this.pixels[0].length; c++)
				image.setRGB(c, r, this.pixels[r][c].getColor().getRGB());

		//user's Desktop will be default directory location
		JFileChooser chooser = new JFileChooser(System.getProperty("user.home") + "/Desktop");

		chooser.setDialogTitle("Select picture save location / file name");

		File file = null;

		int choice = chooser.showSaveDialog(null);

		if (choice == JFileChooser.APPROVE_OPTION)
			file = chooser.getSelectedFile();

		//append extension if user didn't read save instructions
        assert file != null;
        if (!file.getName().endsWith(".jpg") && !file.getName().endsWith(".JPG") && !file.getName().endsWith(".jpeg") && !file.getName().endsWith(".JPEG"))
			file = new File(file.getAbsolutePath() + ".jpg");
		
		try {
			ImageIO.write(image, "jpg", file);
			System.out.println("File created at " + file.getAbsolutePath());
		}
		catch (IOException e) {
			System.out.println("Can't write to location: " + file);
		}
		catch (NullPointerException|IllegalArgumentException e) {
			System.out.println("Invalid directory choice");
		}
	}
	
	/** return a copy of the reference to the 2D array of pixels that comprise this picture */
	public Pixel[][] getPixels() {
		return pixels;
	}


    /** remove all blue tint from a picture */
    @SuppressWarnings("unused")
    public void zeroBlue()
    {
    	for (Pixel[] plist : pixels) {
            for (Pixel p : plist) {
                p.setBlue(0);
            }
        }
    }

    /** remove everything BUT blue tint from a picture */
    @SuppressWarnings("unused")
    public void keepOnlyBlue()
    {
        for (Pixel[] plist : pixels) {
            for (Pixel p : plist) {
                p.setRed(0);
                p.setGreen(0);
            }
        }
    }

    /** invert a picture's colors */
    @SuppressWarnings("unused")
    public void negate()
    {
        for (Pixel[] plist : pixels) {
            for (Pixel p : plist) {
                p.setBlue(255 - p.getBlue());
                p.setGreen(255 - p.getGreen());
                p.setRed(255 - p.getRed());
            }
        }
    }

    /** simulate the over-exposure of a picture in film processing */
    @SuppressWarnings("unused")
    public void solarize(int threshold)
    {
        for (Pixel[] plist : pixels) {
            for (Pixel p : plist) {
                int blue = p.getBlue();
                int green = p.getGreen();
                int red = p.getRed();
                if (blue < threshold) {
                    p.setBlue(255 - blue);
                }
                if (green < threshold) {
                    p.setGreen(255 - green);
                }
                if (red < threshold) {
                    p.setRed(255 - red);
                }
            }
        }
    }

    /** convert an image to grayscale */
    @SuppressWarnings("unused")
    public void grayscale()
    {
        for (Pixel[] plist : pixels) {
            for (Pixel p : plist) {
                int average = p.getBlue() + p.getGreen() + p.getRed();
                average /= 3;
                p.setBlue(average);
                p.setGreen(average);
                p.setRed(average);
            }
        }
    }

	/** change the tint of the picture by the supplied coefficients */
    @SuppressWarnings("unused")
	public void tint(double red, double blue, double green)
	{
        for (Pixel[] plist : pixels) {
            for (Pixel p : plist) {
                p.setRed((int) Math.min(p.getRed() * red, 255));
                p.setGreen((int) Math.min(p.getGreen() * green, 255));
                p.setBlue((int) Math.min(p.getBlue() * blue, 255));
            }
        }
	}
	
	/** reduces the number of colors in an image to create a "graphic poster" effect */
    @SuppressWarnings("unused")
	public void posterize(int span)
	{
        for (Pixel[] plist : pixels) {
            for (Pixel p : plist) {
                p.setBlue(p.getBlue() / span * span);
                p.setGreen(p.getGreen() / span * span);
                p.setRed(p.getRed() / span * span);
            }
        }
	}

    /** mirror an image about a vertical midline, left to right */
    @SuppressWarnings("unused")
    public void mirrorVertical()
    {
		Pixel leftPixel;
		Pixel rightPixel;

		int width = pixels[0].length;

        for (Pixel[] pixel : pixels) {
            for (int c = 0; c < width / 2; c++) {
                leftPixel = pixel[c];
                rightPixel = pixel[(width - 1) - c];
                rightPixel.setColor(leftPixel.getColor());
            }
        }
    }

    /** mirror about a vertical midline, right to left */
    @SuppressWarnings("unused")
    public void mirrorRightToLeft()
    {
        Pixel leftPixel;
        Pixel rightPixel;

        int width = pixels[0].length;

        for (Pixel[] pixel : pixels) {
            for (int c = 0; c < width / 2; c++) {
                leftPixel = pixel[c];
                rightPixel = pixel[(width - 1) - c];
                leftPixel.setColor(rightPixel.getColor());
            }
        }
    }

    /** mirror about a horizontal midline, top to bottom */
    @SuppressWarnings("unused")
    public void mirrorHorizontal()
    {
        Pixel topPixel;
        Pixel bottomPixel;

        int width = pixels[0].length;

        for (int r = 0; r < pixels.length / 2; r++)
        {
            for (int c = 0; c < width; c++)
            {
                topPixel  = pixels[r][c];
                bottomPixel = pixels[(pixels.length - 1) - r][c];
                bottomPixel.setColor(topPixel.getColor());
            }
        }
    }

    /** flip an image upside down about its bottom edge */
    @SuppressWarnings("unused")
    public void verticalFlip()
    {
        List<Pixel[]> reversed = Arrays.asList(pixels);
        Collections.reverse(reversed);
        pixels = reversed.stream().filter(Objects::nonNull).toArray(Pixel[][]::new);
    }

    /** fix roof on greek temple */
    @SuppressWarnings("unused")
    public void fixRoof()
    {
        Pixel leftPixel;
        Pixel rightPixel;

        int width = pixels[0].length;

        for (int r = 24; r < 100; r++)
        {
            for (int c = 0; c < 277; c++)
            {
                leftPixel  = pixels[r][c];
                rightPixel = pixels[r][(width - 17) - c];
                rightPixel.setColor(leftPixel.getColor());
            }
        }
    }

    /** detect and mark edges in an image */
    @SuppressWarnings("unused")
    public void edgeDetection(int dist)
    {
        Pixel leftPixel;
        Pixel rightPixel;
        Pixel bottomPixel;

        int width = pixels[0].length;

        for (int r = 0; r < pixels.length - 1; r++)
        {
            for (int c = 0; c < width - 1; c++)
            {
                leftPixel  = pixels[r][c];
                rightPixel = pixels[r][c + 1];
                bottomPixel = pixels[r + 1][c];
                double xDistance = leftPixel.colorDistance(rightPixel.getColor());
                double yDistance = leftPixel.colorDistance(bottomPixel.getColor());
                if (xDistance < dist && yDistance < dist) {
                    leftPixel.setColor(255, 255, 255);
                }
                else {
                    leftPixel.setColor(0, 0, 0);
                }
            }
        }
    }


	/** copy another picture's pixels into this picture, if a color is within dist of param Color */
	public void chromakey(Picture other, Color color, int dist)
	{
        for (int r = 0; r < pixels.length; r++) {
            for (int c = 0; c < pixels[0].length; c++) {
                Pixel thisPixel = pixels[r][c];
                double distance = thisPixel.colorDistance(color);
                if (distance < dist) {
                    thisPixel.setColor(other.getPixels()[r][c].getColor());
                }
            }
        }
	}

	/** steganography encode (hide the message in msg in this picture) */
	public void encode(Picture msg)
	{
        for (Pixel[] plist : pixels) {
            for (Pixel p : plist) {
                if (p.getRed() % 2 != 0) {
                    p.setRed(p.getRed() - 1);
                }
            }
        }
        for (int r = 0; r < pixels.length; r++)
        {
            for (int c = 0; c < pixels[0].length; c++)
            {
                if (msg.getPixels()[r][c].colorDistance(Color.black) < 50)
                {
                    pixels[r][c].setRed(pixels[r][c].getRed() + 1);
                }
            }
        }
	}

	/** steganography decode (return a new Picture containing the message hidden in this picture) */
	public Picture decode()
	{
		Picture decoded = new Picture(getHeight(), getWidth());

        for (int r = 0; r < pixels.length; r++)
        {
            for (int c = 0; c < pixels[0].length; c++)
            {
                if (pixels[r][c].getRed() % 2 != 0)
                {
                    decoded.getPixels()[r][c].setColor(Color.BLACK);
                }
            }
        }

        return decoded;
	}

	/** perform a simple blur using the colors of neighboring pixels */
	public Picture simpleBlur()
	{
        Picture result = new Picture(getHeight(), getWidth());

        for (int r = 1; r < pixels.length - 1; r++)
        {
            for (int c = 1; c < pixels[0].length - 1; c++)
            {
                Pixel topPixel = pixels[r - 1][c];
                Pixel rightPixel = pixels[r][c + 1];
                Pixel bottomPixel = pixels[r + 1][c];
                Pixel leftPixel = pixels[r][c - 1];
                int redAverage = topPixel.getRed() + rightPixel.getRed() + bottomPixel.getRed() +
                        leftPixel.getRed() + pixels[r][c].getRed();
                redAverage /= 5;
                int greenAverage = topPixel.getGreen() + rightPixel.getGreen() + bottomPixel.getGreen() +
                        leftPixel.getGreen() + pixels[r][c].getGreen();
                greenAverage /= 5;
                int blueAverage = topPixel.getBlue() + rightPixel.getBlue() + bottomPixel.getBlue() +
                        leftPixel.getBlue() + pixels[r][c].getBlue();
                blueAverage /= 5;
                result.setPixel(c, r, new Pixel(redAverage, greenAverage, blueAverage));
            }
        }

        return result;
	}

	/** perform a blur using the colors of pixels within radius of current pixel */
	public Picture blur(int radius)
	{
        Picture result = new Picture(getHeight(), getWidth());

        for (int r = 0; r < pixels.length - 1; r++)
        {
            for (int c = 0; c < pixels[0].length - 1; c++)
            {
                ArrayList<Pixel> blurredPixels = new ArrayList<>();
                for (int i = -radius; i <= radius; i++) {
                    for (int j = -radius; j <= radius; j++) {
                        if (r + i >= 0 && r + i < pixels.length - 1) {
                            if (c + j >= 0 && c + j < pixels[0].length - 1) {
                                blurredPixels.add(pixels[r + i][c + j]);
                            }
                        }
                    }
                }
                OptionalDouble redAverage = blurredPixels.stream().mapToInt(Pixel::getRed).average();
                assert redAverage.isPresent();
                OptionalDouble greenAverage = blurredPixels.stream().mapToInt(Pixel::getGreen).average();
                assert greenAverage.isPresent();
                OptionalDouble blueAverage = blurredPixels.stream().mapToInt(Pixel::getBlue).average();
                assert blueAverage.isPresent();
                result.setPixel(c, r, new Pixel((int) redAverage.getAsDouble(),
                        (int) greenAverage.getAsDouble(), (int) blueAverage.getAsDouble()));
            }
        }

        return result;
	}
	
	/**
	 * Simulate looking at an image through a pane of glass
	 * @param dist the "radius" of the neighboring pixels to use
	 * @return a new Picture with the glass filter applied
	 */
	public Picture glassFilter(int dist) 
	{
        Picture result = new Picture(getHeight(), getWidth());

        for (int r = 0; r < pixels.length - 1; r++)
        {
            for (int c = 0; c < pixels[0].length - 1; c++)
            {
                ArrayList<Pixel> blurredPixels = new ArrayList<>();
                for (int i = -dist; i <= dist; i++) {
                    for (int j = -dist; j <= dist; j++) {
                        if (r + i >= 0 && r + i < pixels.length - 1) {
                            if (c + j >= 0 && c + j < pixels[0].length - 1) {
                                blurredPixels.add(pixels[r + i][c + j]);
                            }
                        }
                    }
                }
                Pixel picked = blurredPixels.get(new Random().nextInt(blurredPixels.size()));
                result.setPixel(c, r, picked);
            }
        }

        return result;
	}
}
