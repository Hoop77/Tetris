import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created by philipp on 20.07.16.
 */
public class TetrisRenderer extends Canvas
{
    private Board board;
    private int tileSize;

    public TetrisRenderer( Board board, int tileSize )
    {
        this.board = board;
        this.tileSize = tileSize;

        System.setProperty( "sun.java2d.transaccel", "True" );
        System.setProperty( "sun.java2d.d3d", "True" );
        System.setProperty( "sun.java2d.ddforcevram", "True" );
        System.setProperty( "sun.java2d.opengl", "True" );

        setMinimumSize( new Dimension( tileSize * board.getTilesX(), tileSize * board.getTilesY() ));
        setMaximumSize( new Dimension( tileSize * board.getTilesX(), tileSize * board.getTilesY() ));
        setPreferredSize( new Dimension( tileSize * board.getTilesX(), tileSize * board.getTilesY() ));
    }

    public void render()
    {
        BufferStrategy bufferStrategy = getBufferStrategy();

        if( bufferStrategy == null )
        {
            createBufferStrategy( 3 );
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();

        graphics.setColor( Color.black );
        graphics.fillRect( 0, 0, getWidth(), getHeight() );

        // render methods
        renderBoard( graphics );
        renderPlayer( graphics );

        graphics.dispose();
        bufferStrategy.show();
    }

    private void renderPlayer( Graphics g )
    {
        Piece player = board.getPlayer();

        Point position = player.getPosition();

        for( ColorPoint coord : player.getCoords() )
        {
            Point positionOnBoard = new Point(
                    position.x + coord.x,
                    position.y + coord.y );

            g.setColor( coord.color );
            g.fillRect( positionOnBoard.x * tileSize, positionOnBoard.y * tileSize, tileSize, tileSize );
        }
    }

    private void renderBoard( Graphics g )
    {
        for( int y = 0; y < board.getTilesY(); y++ )
        {
            for( int x = 0; x < board.getTilesX(); x++ )
            {
                Tile tile = board.getTile( new Point( x, y ));

                if( tile != null )
                    g.setColor( tile.color );
                else
                    g.setColor( Color.black );

                g.fillRect( x * tileSize, y * tileSize, tileSize, tileSize );
            }
        }
    }
}
