import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by philipp on 20.07.16.
 */
public class Assets
{
    public static Assets instance = new Assets();

    public static Piece square = new Piece(
        new ColorPoint[] {
            new ColorPoint( 0, 0, Color.BLUE ),
            new ColorPoint( 0, 1, Color.BLUE ),
            new ColorPoint( 1, 0, Color.BLUE ),
            new ColorPoint( 1, 1, Color.BLUE )
        }
    );

    public static Piece line = new Piece(
        new ColorPoint[] {
            new ColorPoint( 0, 0, Color.GREEN ),
            new ColorPoint( 1, 0, Color.GREEN ),
            new ColorPoint( 2, 0, Color.GREEN ),
            new ColorPoint( 3, 0, Color.GREEN )
        }
    );

    public static Piece s = new Piece(
        new ColorPoint[] {
            new ColorPoint( 0, 0, Color.RED ),
            new ColorPoint( 1, 0, Color.RED ),
            new ColorPoint( 1, 1, Color.RED ),
            new ColorPoint( 2, 1, Color.RED )
        }
    );

    public static Piece l = new Piece(
        new ColorPoint[] {
            new ColorPoint( 0, 0, Color.YELLOW ),
            new ColorPoint( 1, 0, Color.YELLOW ),
            new ColorPoint( 2, 0, Color.YELLOW ),
            new ColorPoint( 2, 1, Color.YELLOW )
        }
    );

    public static Piece t = new Piece(
        new ColorPoint[] {
            new ColorPoint( 0, 0, Color.CYAN ),
            new ColorPoint( 1, 0, Color.CYAN ),
            new ColorPoint( 2, 0, Color.CYAN ),
            new ColorPoint( 1, 1, Color.CYAN )
        }
    );

    public static Piece sheep = new Piece(
        instance.bitmapToCoords( instance.loadBitmap( "/sheep2.png" ))
    );

    public static Piece pieces[] = { square, line, s, l, t, sheep };

    public Bitmap loadBitmap( String filename )
    {
        try
        {
            BufferedImage image = ImageIO.read( instance.getClass().getResource( filename ) );

            int width = image.getWidth();
            int height = image.getHeight();

            Bitmap result = new Bitmap( width, height );
            image.getRGB( 0, 0, width, height, result.getPixels(), 0, width );

            return result;
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }

        return null;
    }

    public ColorPoint[] bitmapToCoords( Bitmap bitmap )
    {
        // create a list to collect the coordinates
        List<ColorPoint> coordList = new ArrayList<>();
        for( int y = 0; y < bitmap.getHeight(); y++ )
        {
            for( int x = 0; x < bitmap.getWidth(); x++ )
            {
                int pixel = bitmap.getPixel( x, y );
                if( pixel != 0 )
                {
                    int r = Bitmap.getRed( pixel );
                    int g = Bitmap.getGreen( pixel );
                    int b = Bitmap.getBlue( pixel );
                    Color color = new Color( r, g, b );
                    coordList.add( new ColorPoint( x, y, color ));
                }
            }
        }

        ColorPoint[] result = new ColorPoint[ coordList.size() ];
        coordList.toArray( result );
        return result;
    }
}
