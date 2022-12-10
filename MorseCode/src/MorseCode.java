import java.util.HashMap;

public class MorseCode {
    private final HashMap<String, String> toText;
    private final HashMap<String, String> toCode;

    public MorseCode() {
        final String[] morse = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-",
            ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..",
            ".----", "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.", "-----", "|"};
        final String alphabet = "abcdefghijklmnopqrstuvwxyz1234567890 ";

        toText = new HashMap<>();
        toCode = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            toText.put(morse[i], alphabet.substring(i, i + 1));
            toCode.put(alphabet.substring(i, i + 1), morse[i]);
        }
    }

    public String encode(String s) {
        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            encoded.append(toCode.get(s.substring(i, i + 1))).append(" ");
        }
        return encoded.toString();
    }

    public String decode(String s) {
        StringBuilder decoded = new StringBuilder();
        String[] parsed = s.split(" ");
        for (String value : parsed) {
            decoded.append(toText.get(value));
        }
        return decoded.toString();
    }
}
