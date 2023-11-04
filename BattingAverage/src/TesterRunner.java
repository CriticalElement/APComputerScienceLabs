import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.*;
import javax.tools.*;
public class TesterRunner extends TestGUI.TestData {
   public static void main(String[] args) {
      new TestGUI();
   }

   public static void runTests() {
      try {
         Object runner = makeObject("TesterRunner", null);
         testMethod(runner, "test", null, null);
      } catch (Exception e) { System.out.println(e); }         
   }
   
   // you may change the method calls as noted below, but don't change anything else to ensure it works as it is supposed to
   public static void test() throws FileNotFoundException {
		Team test = new Team();
		for (int i = 0; i < test.getNumPlayers(); i++) { // replace getNumPlayers() with your method that returns the number of players
			test.addPlayer(new Player("player" + i, i), i);
		}
		System.out.println("Printing team stats...");
		test.printTeamStats();
		System.out.println();
		System.out.println("Player 7 name test -> " + test.getPlayer(7).getName() + "\n"); // replace getPlayer() with your method that gets a Player at an index
      Scanner fileReader = new Scanner(new File("players.txt"));

      Team knights = new Team(fileReader.nextInt());
      
      for (int i = 0; i < knights.getNumPlayers(); i++) {
         Player p = new Player(fileReader.next(), fileReader.nextInt(), 
                           fileReader.nextInt(), fileReader.nextInt());
         knights.addPlayer(p, i);
      }
      
      knights.printTeamStats();

   }
}