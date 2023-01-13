@SuppressWarnings("unused")
public class Lock
{
    private boolean locked;

    public Lock()
    {
        locked = true;
    }

    public void open()
    {
        locked = false;
    }

    public void close()
    {
        locked = true;
    }

    public boolean isLocked()
    {
        return locked;
    }

    @Override
    public String toString()
    {
        return !locked ? "Lock is open": "Lock is closed";
    }
}
