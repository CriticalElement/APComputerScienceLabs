// run this file!!
// WARNING: extra edits were made to the PictureViewer and Picture classes, to implement the hangUntilClosed feature.

import java.awt.*;

public class PictureTester 
{
    public static void main(String[] args) 
    {
		testZeroBlue();
		testKeepOnlyBlue();
		testNegate();
		testSolarize();
		testGrayscale();
		testTint();
		testPosterize();
		testMirrorRightToLeft();
		testMirrorHorizontal();
		testVerticalFlip();
		testFixRoof();
		testEdgeDetection();
		testChromakey();
		testSteganography();
		testSimpleBlur();
		testBlur();
		testGlassFilter();
    }

	public static void testZeroBlue()
	{
		Picture beach = new Picture("beach.jpg");
		beach.view();
		beach.zeroBlue();
		beach.view();
		beach.hangUntilClosed();
	}

	public static void testKeepOnlyBlue()
	{
		Picture beach = new Picture("beach.jpg");
		beach.view();
		beach.keepOnlyBlue();
		beach.view();
		beach.hangUntilClosed();
	}

	public static void testNegate()
	{
		Picture koala = new Picture("koala.jpg");
		koala.view();
		koala.negate();
		koala.view();
		koala.hangUntilClosed();
	}

	public static void testSolarize()
	{
		Picture lilies = new Picture("waterlilies.jpg");
		lilies.view();
		lilies.solarize(127);
		lilies.view();
		lilies.hangUntilClosed();
	}

	public static void testGrayscale()
	{
		Picture gorge = new Picture("gorge.jpg");
		gorge.view();
		gorge.grayscale();
		gorge.view();
		gorge.hangUntilClosed();
	}

	public static void testTint()
	{
		Picture beach = new Picture("beach.jpg");
		beach.view();
		beach.tint(1.25, 0.75, 1);
		beach.view();
		beach.hangUntilClosed();
	}

	public static void testPosterize()
	{
		Picture beach = new Picture("beach.jpg");
		beach.view();
		beach.posterize(63);
		beach.view();
		beach.hangUntilClosed();
	}

	public static void testMirrorRightToLeft()
	{
		Picture motorcycle = new Picture("redMotorcycle.jpg");
		motorcycle.view();
		motorcycle.mirrorRightToLeft();
		motorcycle.view();
		motorcycle.hangUntilClosed();
	}

	public static void testMirrorHorizontal()
	{
		Picture motorcycle = new Picture("redMotorcycle.jpg");
		motorcycle.view();
		motorcycle.mirrorHorizontal();
		motorcycle.view();
		motorcycle.hangUntilClosed();
	}

	public static void testVerticalFlip()
	{
		Picture lilies = new Picture("waterlilies.jpg");
		lilies.view();
		lilies.verticalFlip();
		lilies.view();
		lilies.hangUntilClosed();
	}

	public static void testFixRoof()
	{
		Picture temple = new Picture("temple.jpg");
		temple.view();
		temple.fixRoof();
		temple.view();
		temple.hangUntilClosed();
	}

	public static void testEdgeDetection()
	{
		Picture swan = new Picture("swan.jpg");
		swan.view();
		swan.edgeDetection(25);
		swan.view();
		swan.hangUntilClosed();
	}

    /** this method is static, you don't need to call it on an object (just "testChromakey()") */
	@SuppressWarnings("unused")
	public static void testChromakey()
	{
		Picture one = new Picture("blue-mark.jpg");
		Picture two = new Picture("moon-surface.jpg");
		
		one.view(); //show original mustache guy picture
		two.view(); //show the untouched moon's surface pic
		
		one.chromakey(two, new Color(10, 40, 75), 60); //replace this color if within 60
		
		one.view();
		one.hangUntilClosed();
	}
	
    /** this method is static, you don't need to call it on an object (just "testSteganography()") */
	@SuppressWarnings("unused")
	public static void testSteganography()
	{
		Picture msg   = new Picture("msg.jpg");
		Picture beach = new Picture("beach.jpg");
		
		beach.encode(msg); //hide message in beach picture
		beach.view();      //beach w/ hidden message inside, shouldn't look different
		
		beach.decode().view(); //see the hidden message in the beach picture
		beach.hangUntilClosed();
	}

	@SuppressWarnings("unused")
	public static void testSimpleBlur()
	{
		Picture koala = new Picture("koala.jpg");
		koala.view();
		koala.simpleBlur().view();
		koala.hangUntilClosed();
	}

	@SuppressWarnings("unused")
	public static void testBlur()
	{
		Picture lilies = new Picture("waterlilies.jpg");
		lilies.view();
		lilies.blur(5).view();
		lilies.hangUntilClosed();
	}

	@SuppressWarnings("unused")
	public static void testGlassFilter()
	{
		Picture lilies = new Picture("waterlilies.jpg");
		lilies.view();
		lilies.glassFilter(5).view();
		lilies.hangUntilClosed();
	}
}
