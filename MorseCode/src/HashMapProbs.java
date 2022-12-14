import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class HashMapProbs {
    public static void main(String[] args) throws FileNotFoundException {
        runProbs();
    }

    public static void runProbs() throws FileNotFoundException {
        Scanner scan = new Scanner(System.in);

        // 1:
        HashMap<String, String> animalSounds = new HashMap<>();
        animalSounds.put("Cat", "Meow");
        animalSounds.put("Cow", "Moo");
        animalSounds.put("Pig", "Oink");
        animalSounds.put("Dog", "Woof");
        System.out.println(animalSounds + "\n");

        // 2:
        System.out.print("Enter an animal name >>> ");
        String str = scan.next();
        System.out.println(animalSounds.get(str) + "\n");

        // 3:
        System.out.println(animalSounds.size() + "\n");

        // 4:
        System.out.print("Enter an animal followed by the sound it makes (on one line) >>> ");
        animalSounds.put(scan.next(), scan.next());
        System.out.println(animalSounds + "\n");

        // 5:
        System.out.println(takeBefore("str", "bye") + "\n");

        // 6:
        System.out.println(multiple("hello") + "\n");

        // 7:
        System.out.println(charWord(new String[] {"tea", "salt", "soda", "taco"}) + "\n");

        // 8:
        HashMap<String, Integer> map = new HashMap<>();
        Scanner file = new Scanner(new File("dream.txt"));
        while (file.hasNext()) {
            String token = file.next();
            map.computeIfPresent(token, (key, val) -> ++val);
            map.putIfAbsent(token, 1);
        }
        int max = Collections.max(map.values());
        map.forEach((key, val) -> {
            if (val == max) System.out.println("Highest frequency word: " + key + ", " + val);
        });
    }

    public static HashMap<String, String> takeBefore(String a, String b) {
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < a.length(); i++) {
            char first = a.charAt(i);
            char second = b.charAt(i);
            if (first <= second)
                map.put(Character.toString(first), Character.toString(second));
            else
                map.put(Character.toString(second), Character.toString(first));
        }
        return map;
    }

    public static HashMap<String, Boolean> multiple(String s) {
        HashMap<String, Boolean> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.lastIndexOf(Character.toString(s.charAt(i))) != i) {
                map.put(Character.toString(s.charAt(i)), true);
            }
            else {
                map.putIfAbsent(Character.toString(s.charAt(i)), false);
            }
        }
        return map;
    }

    public static HashMap<String, String> charWord(String[] words) {
        HashMap<String, String> map = new HashMap<>();
        for (String word : words) {
            map.computeIfPresent(word.substring(0, 1), (key, val) -> val + word);
            map.putIfAbsent(word.substring(0, 1), word);
        }
        return map;
    }
}
