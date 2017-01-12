import javax.swing.*;
import java.awt.*;

/**
 * Created by philipp on 20.07.16.
 */
public class Tetris extends JFrame implements Runnable
{
    public static final long FPS = 60L;
    public static final double FRAME_TIME_NANO = 1000000000L / FPS;

    private TetrisRenderer renderer;
    private Board board;

    private boolean running;

    public static KeyboardInputHandler keyboardInputHandler;

    public Tetris()
    {
        super( "Simple Path Finder" );

        board = new Board( 30, 60 );

        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        setLayout( new BorderLayout() );
        renderer = new TetrisRenderer( board, 12 );
        add( renderer, BorderLayout.CENTER );

        keyboardInputHandler = new KeyboardInputHandler();
        addKeyListener( keyboardInputHandler );

        pack();

        setResizable( false );
        setLocationRelativeTo( null );
        setVisible( true );
    }

    public synchronized void start()
    {
        running = true;
        new Thread( this ).start();
    }

    @Override
    public void run()
    {
        long currTime = System.nanoTime();
        long lastTime;
        double updateRatio = 0;
        long milliSecCounter = System.currentTimeMillis();
        int fps = 0;

        while( running )
        {
            lastTime = currTime;
            currTime = System.nanoTime();

            updateRatio += ( ( double ) currTime - ( double ) lastTime ) / FRAME_TIME_NANO;

            while( updateRatio >= 1 )
            {
                update( ( float ) updateRatio );

                updateRatio -= 1;
            }

            fps++;

            if( System.currentTimeMillis() - milliSecCounter >= 1000 )
            {
                milliSecCounter += 1000;
                //System.out.println( "FPS: " + fps );
                fps = 0;
            }

            try
            {
                Thread.sleep( 2 );
            }
            catch( InterruptedException e )
            {
                e.printStackTrace();
            }
        }
    }

    public void update( float updateRatio )
    {
        board.update( updateRatio );
        renderer.render();
        keyboardInputHandler.update();
    }

    public void stop()
    {
        running = false;
    }

    public static void main( String args[] )
    {
        new Tetris().start();
    }
}
