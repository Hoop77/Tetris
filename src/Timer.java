/**
 * Created by philipp on 25.07.16.
 */
public class Timer
{
    private double span;
    private double counter;
    private double frameTime;

    /**
     * @param span period of firing in milliseconds
     * @param frameTime in nanoseconds
     */
    public Timer( double span, double frameTime )
    {
        this.span = span;
        this.frameTime = frameTime;
    }

    public boolean update( double updateRatio )
    {
        counter += frameTime * updateRatio;
        if( counter >= span / 1000000 )
        {
            counter -= span * 1000000;
            return true;
        }
        else
        {
            return false;
        }
    }
}
