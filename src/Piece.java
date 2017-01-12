import java.awt.*;

/**
 * Created by philipp on 20.07.16.
 */
public class Piece
{
    private Point position;
    private ColorPoint[] coords;

    public Piece( ColorPoint[] coords )
    {
        this.coords = coords;

        position = new Point( 0, 0 );

        align();
    }

    public void rotateLeft()
    {
        Vector2 origin = calculateOrigin();

        for( Point coord : coords )
        {
            // move to origin
            float x = coord.x - origin.x;
            float y = coord.y - origin.y;

            // rotate
            float rx = y;
            float ry = -x;

            // move away from origin and convert to int
            coord.x = (int) Math.floor( rx );
            coord.y = (int) Math.floor( ry );
        }

        align();
    }

    public void rotateRight()
    {
        Vector2 origin = calculateOrigin();

        for( Point coord : coords )
        {
            // move to origin
            float x = coord.x - origin.x;
            float y = coord.y - origin.y;

            // rotate
            float rx = -y;
            float ry = x;

            // move away from origin and convert to int
            coord.x = (int) Math.floor( rx );
            coord.y = (int) Math.floor( ry );
        }

        align();
    }

    /**
     * warning: The piece must be aligned!
     */
    private Vector2 calculateOrigin()
    {
        Point max = getMaxXY();

        return new Vector2( (float) max.x / 2, (float) max.y / 2 );
    }

    /**
     * Aligns the position of the piece so its directly surrounded by the axis of the coordinate system.
     */
    private void align()
    {
        Point min = getMinXY();

        // execute alignment
        for( int i = 0; i < coords.length; i++ )
        {
            Point coord = getCoord( i );
            coord.x -= min.x;
            coord.y -= min.y;
        }
    }

    public Point getMinXY()
    {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;

        for( int i = 0; i < coords.length; i++ )
        {
            Point coord = getCoord( i );

            if( coord.x < minX )
                minX = coord.x;
            if( coord.y < minY )
                minY = coord.y;
        }

        return new Point( minX, minY );
    }

    public Point getMaxXY()
    {
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for( int i = 0; i < coords.length; i++ )
        {
            Point coord = getCoord( i );

            if( coord.x > maxX )
                maxX = coord.x;
            if( coord.y > maxY )
                maxY = coord.y;
        }

        return new Point( maxX, maxY );
    }

    public ColorPoint getCoord( int i )
    {
        return coords[ i ];
    }

    public ColorPoint[] getCoords() { return coords; }

    public int getCoordCount() { return coords.length; }

    public void setX( int x )
    {
        position.x = x;
    }

    public void setY( int y )
    {
        position.y = y;
    }

    public int getX() { return position.x; }
    public int getY() { return position.y; }

    public Point getPosition() { return position; }
    public void setPosition( Point position ) { this.position = position; }
}
