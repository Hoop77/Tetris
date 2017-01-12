import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by philipp on 20.07.16.
 */
public class Board
{
    private int tilesX, tilesY;
    private Tile[] tiles;
    private Piece player;

    private Random random;

    private State spawnState, fallingState, loosingState;
    private State currentState;

    private Timer fallingTimer;

    public Board( int tilesX, int tilesY )
    {
        this.tilesX = tilesX;
        this.tilesY = tilesY;
        tiles = new Tile[ tilesX * tilesY ];

        // setup timer who fires every second the falling event
        fallingTimer = new Timer( 500.0, Tetris.FRAME_TIME_NANO );

        random = new Random();

        spawnState = new State()
        {
            @Override
            public void update()
            {
                // get random piece for player
                int randomPiece = random.nextInt( Assets.pieces.length );
                player = Assets.pieces[ randomPiece ];

                // set random rotation
                int randomRoation = random.nextInt( player.getCoordCount() );
                for( int i = 0; i < randomRoation; i++ )
                {
                    player.rotateLeft();
                }

                // try to spawn at random position
                boolean gameOver = !generateSpawnPosition();

                if( gameOver )
                    currentState = loosingState;
                else
                    currentState = fallingState;
            }
        };

        fallingState = new State()
        {
            @Override
            public void update()
            {
                // check for collision
                if( isColliding( 0, 1 ))
                {
                    // if it would collide add the player's tiles to the board to make them persistent
                    addTiles();
                    removeRows();
                    // change to spawning state
                    currentState = spawnState;
                    currentState.update();
                }
                else
                {
                    // move player down 1 tile
                    movePlayerDown( 1 );
                }
            }
        };

        loosingState = new State()
        {
            @Override
            public void update()
            {
                // TODO
            }
        };

        currentState = spawnState;
    }

    private void movePlayerDown( int yOffset )
    {
        player.setY( player.getY() + yOffset );
    }

    private void dropPlayer()
    {
        int yOffset = 0;
        while( !isColliding( 0, yOffset + 1 ))
        {
            yOffset++;
        }

        movePlayerDown( yOffset );
        currentState.update();
    }

    private boolean generateSpawnPosition()
    {
        player.setX( 0 );
        player.setY( 0 );

        List<Integer> possiblePositions = new LinkedList<>();

        for( int x = 0; x < tilesX; x++ )
        {
            // test for offset x
            if( !isColliding( x, 0 ))
                possiblePositions.add( x );
        }

        if( possiblePositions.isEmpty() )
            return false;

        int randomIndex = random.nextInt( possiblePositions.size());
        int newX = possiblePositions.get( randomIndex );
        player.setX( newX );

        return true;
    }

    private boolean isColliding( int offsetX, int offsetY )
    {
        Point position = player.getPosition();

        for( int i = 0; i < player.getCoordCount(); i++ )
        {
            Point coord = player.getCoord( i );
            Point positionOnBoard = new Point(
                    position.x + coord.x + offsetX,
                    position.y + coord.y + offsetY );

            // check bounds and for existing tile
            if( positionOnBoard.x < 0 ||
                positionOnBoard.x >= tilesX ||
                positionOnBoard.y >= tilesY ||
                isTileAt( positionOnBoard ) )
                return true;
        }

        return false;
    }

    private void addTiles()
    {
        Point position = player.getPosition();

        for( ColorPoint coord : player.getCoords() )
        {
            Point positionOnBoard = new Point(
                    position.x + coord.x,
                    position.y + coord.y );

            setTile( positionOnBoard, new Tile( coord.color ));
        }
    }

    private void removeRows()
    {
        int minRow = player.getPosition().y + player.getMinXY().y;
        int maxRow = player.getPosition().y + player.getMaxXY().y;

        // remove rows
        int rowsToRemove = 0;
        boolean shouldRemove = true;
        int row;
        for( row = minRow; row < maxRow + 1; row++ )
        {
            // check if the row is complete
            shouldRemove = isRowComplete( row );

            // collect a sequence of rows that can be removed
            // once a row is not complete, remove the collected rows
            if( !shouldRemove && rowsToRemove > 0 )
            {
                moveTilesDown( row - 1, rowsToRemove );
                rowsToRemove = 0;
            }
            else if( shouldRemove )
            {
                rowsToRemove++;
            }
        }

        // final check
        if( shouldRemove && rowsToRemove > 0 )
        {
            moveTilesDown( row - 1, rowsToRemove );
        }
    }

    private boolean isRowComplete( int row )
    {
        for( int x = 0; x < tilesX; x++ )
        {
            if( !isTileAt( new Point( x, row )))
                return false;
        }

        return true;
    }

    private void moveTilesDown( int startRow, int count )
    {
        // move tiles down
        for( int y = startRow - count; y >= 0; y-- )
        {
            for( int x = 0; x < tilesX; x++ )
            {
                setTile( new Point( x, y + count ), getTile( new Point( x, y )));
            }
        }

        // add new empty tiles to the top
        for( int y = 0; y < count; y++ )
        {
            for( int x = 0; x < tilesX; x++ )
            {
                setTile( new Point( x, y ), null );
            }
        }
    }

    private void rotate()
    {
        // rotate left
        player.rotateLeft();

        // if the rotation would result in a collision -> rotate back!
        if( isColliding( 0, 0 ))
            player.rotateRight();
    }

    public Tile getTile( Point position )
    {
        return tiles[ position.x + position.y * tilesX ];
    }

    public void setTile( Point position, Tile tile )
    {
        tiles[ position.x + position.y * tilesX ] = tile;
    }

    public int getTilesX() { return tilesX; }
    public int getTilesY() { return tilesY; }

    public Piece getPlayer() { return player; }

    private boolean isTileAt( Point position )
    {
        return ( getTile( position ) != null );
    }

    public void update( double updateRatio )
    {
        // handle input
        // move left
        if( Tetris.keyboardInputHandler.isKeyPressed( KeyEvent.VK_LEFT ) )
        {
            if( !isColliding( -1, 0 ) )
                player.setX( player.getX() - 1 );
        }
        // move right
        else if( Tetris.keyboardInputHandler.isKeyPressed( KeyEvent.VK_RIGHT ) )
        {
            if( !isColliding( 1, 0 ) )
                player.setX( player.getX() + 1 );
        }

        // rotate with space
        if( Tetris.keyboardInputHandler.isKeyPressed( KeyEvent.VK_SPACE ) )
        {
            rotate();
        }

        // drop player if enter is pressed
        if( Tetris.keyboardInputHandler.isKeyPressed( KeyEvent.VK_ENTER ) )
        {
            dropPlayer();
        }

        // update logic
        if( fallingTimer.update( updateRatio) )
            currentState.update();
    }
}