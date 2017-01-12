import java.awt.*;

/**
 * Created by philipp on 27.07.16.
 */
public class ColorPoint extends Point
{
    public Color color;

    public ColorPoint( int x, int y, Color color )
    {
        super( x, y );
        this.color = color;
    }
}
