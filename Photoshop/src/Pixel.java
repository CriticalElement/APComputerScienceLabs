import java.awt.*;

/**
 * A class that contains a red, green, and blue color value for a pixel.
 */
public class Pixel {

	private int red;
	private int green;
	private int blue;

	/**
	 * Creates a Pixel with color values red, green, and blue.
	 * @param red The red value of the Pixel, from 0-255
	 * @param green The green value of the Pixel, from 0-255
	 * @param blue The blue value of the Pixel, from 0-255
	 */
	public Pixel(int red, int green, int blue) {
		this.red   = red;
		this.green = green;
		this.blue  = blue;
	}

	/**
	 * Creates a Pixel with the specified {@link Color}
	 * @param color {@link Color} of the Pixel
	 */
	public Pixel(Color color) {
		this.red   = color.getRed();
		this.green = color.getGreen();
		this.blue  = color.getBlue();
	}

	/**
	 * Gets the red value of the Pixel
	 * @return The red value of the Pixel
	 */
	public int getRed() {
		return red;
	}

	/**
	 * Sets the red value of the Pixel
	 * @param red The new red value of the Pixel, from 0-255
	 */
	public void setRed(int red) {
		this.red = red;
	}

	/**
	 * Gets the green value of the Pixel
	 * @return The green value of the Pixel
	 */
	public int getGreen() {
		return green;
	}

	/**
	 * Sets the green value of the Pixel
	 * @param green The new green value of the Pixel, from 0-255
	 */
	public void setGreen(int green) {
		this.green = green;
	}

	/**
	 * Gets the blue value of the Pixel
	 * @return The blue value of the Pixel
	 */
	public int getBlue() {
		return blue;
	}

	/**
	 * Sets the blue value of the Pixel
	 * @param blue The new blue value of the Pixel, from 0-255
	 */
	public void setBlue(int blue) {
		this.blue = blue;
	}

	/**
	 * Return a {@link Color} with the current color of the Pixel
	 * @return The {@link Color} of the Pixel
	 */
	public Color getColor() {
		return new Color(red, green, blue);
	}

	/**
	 * Sets the color of the Pixel
	 * @param red The new red value of the Pixel, from 0-255
	 * @param green The new green value of the Pixel, from 0-255
	 * @param blue The new blue value of the Pixel, from 0-255
	 */
	public void setColor(int red, int green, int blue) {
		this.red   = red;
		this.green = green;
		this.blue  = blue;
	}

	/**
	 * Sets the {@link Color} of the Pixel
	 * @param color The new {@link Color} of the Pixel
	 */
	public void setColor(Color color) {
		this.red   = color.getRed();
		this.green = color.getGreen();
		this.blue  = color.getBlue();
	}

	/**
	 * Returns a {@link String} representation of the Pixel
	 * @return {@link String} representation of the Pixel
	 */
	@Override
	public String toString() {
		return "{Red: "+red+", Green: "+green+", Blue: "+blue+"}";
	}

	/**
	 * Method to check if this Pixel is equal to another {@link Object}
	 * @param other The {@link Object} to check equality with
	 * @return True if the two objects are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Pixel)) return false;
		Pixel p = (Pixel)other;
		return p.red==red && p.green==green && p.blue==blue;
	}

	/**
	 * Method to get the distance between this pixel's color and the passed color
	 * @param testColor the color to compare to
	 * @return the distance between this pixel's color and the passed color
	 */
	public double colorDistance(Color testColor)
	{
		double redDistance   = this.getRed()   - testColor.getRed();
		double greenDistance = this.getGreen() - testColor.getGreen();
		double blueDistance  = this.getBlue()  - testColor.getBlue();
		
		return Math.sqrt(redDistance * redDistance + greenDistance * greenDistance + blueDistance * blueDistance);
	}
}
