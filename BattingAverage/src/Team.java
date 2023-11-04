public class Team {
    private final Player[] players;

    public Team() {
        players = new Player[12];
    }

    public Team(int numPlayers) {
        players = new Player[numPlayers];
    }

    public void printTeamStats() {
        for (Player player : players) {
            System.out.printf("%-15s#%-6d average >>> %s\n", player.getName(), player.getNumber(), player.getBattingAverageString());
        }
    }

    public Player getPlayer(int index) {
        return players[index];
    }

    public void addPlayer(Player p, int index) {
        players[index] = p;
    }

    public int getNumPlayers() {
        return players.length;
    }
}
