/**
 * Created by philipp on 28.07.16.
 */
public class Bitmap
{
    private int width, height;
    private int[] pixels;

    public Bitmap( int width, int height )
    {
        this.width = width;
        this.height = height;

        pixels = new int[ width * height ];
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public static int getAlpha( int color )
    {
        return (color & 0xff000000) >> 24;
    }

    public static int getRed( int color )
    {
        return (color & 0x000000ff);
    }

    public static int getGreen( int color )
    {
        return (color & 0x0000ff00) >> 8;
    }

    public static int getBlue( int color )
    {
        return (color & 0x00ff0000) >> 16;
    }

    public int getPixel( int x, int y )
    {
        return pixels[ x + y * width ];
    }

    public int[] getPixels()
    {
        return pixels;
    }
}
