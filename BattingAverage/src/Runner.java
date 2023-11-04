import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Runner {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("players.txt"));
        int p = scanner.nextInt();
        Team knights = new Team(p);

        for (int i = 0; i < p; i++) {
            String name = scanner.next();
            int number = scanner.nextInt();
            int atBats = scanner.nextInt();
            int hits = scanner.nextInt();
            knights.addPlayer(new Player(name, number, atBats, hits), i);
        }

        knights.printTeamStats();
    }
}
