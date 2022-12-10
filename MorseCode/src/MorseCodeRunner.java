import java.io.FileNotFoundException;

public class MorseCodeRunner {
    public static void main(String[] args) throws FileNotFoundException {
        try {
            HashMapProbs.runProbs();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Could not find the file, maybe try fixing the run configuration? " +
                    "Or, verify that the \"dream.txt\" file is located in the right place (usually in the parent " +
                    "directory of the src folder).");
        }
        System.out.println("\n\n\n");
        MorseCode m = new MorseCode();
        System.out.println(m.encode("hello world"));
        System.out.println(m.decode("--- -- --. | .. - | .-- --- .-. -.- . -.. "));
    }
}
