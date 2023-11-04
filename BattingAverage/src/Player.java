public class Player {
    private final String name;
    private final int number;
    private int atBats;
    private int hits;

    public Player(String name, int number) {
        this.name = name;
        this.number = number;
        atBats = 0;
        hits = 0;
    }

    public Player(String name, int number, int atBats, int hits) {
        this.name = name;
        this.number = number;
        this.atBats = atBats;
        this.hits = hits;
    }

    public double getBattingAverage() {
        return (double) hits / atBats;
    }

    public String getBattingAverageString() {
        return (int) Math.round(getBattingAverage() * 1000) + "";
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }
}
