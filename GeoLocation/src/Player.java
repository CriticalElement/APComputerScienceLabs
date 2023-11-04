public class Player
{
    private final String name;
    private final int number;

    public Player(String name, int number)
    {
        this.name = name;
        this.number = number;
    }

    public Player()
    {
        this("Default", -1);
    }

    String playerInfo()
    {
        return "Player: " + name + ", #" + number;
    }
}
