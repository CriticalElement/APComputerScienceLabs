import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * The Deck class represents a shuffled deck of cards.
 * It provides several operations including
 *      initialize, shuffle, deal, and check if empty.
 */
public class Deck {

    /**
     * cards contains all the cards in the deck.
     */
    private final List<Card> cards;

    /**
     * size is the number of not-yet-dealt cards.
     * Cards are dealt from the top (the highest index) down.
     * The next card to be dealt is at size - 1.
     */
    private int size;

    /**
     * Creates a new <code>Deck</code> instance.<BR>
     * It pairs each element of ranks with each element of suits,
     * and produces one of the corresponding card.
     * @param ranks is an array containing all the card ranks.
     * @param suits is an array containing all the card suits.
     * @param values is an array containing all the card point values.
     */
    public Deck(String[] ranks, String[] suits, int[] values) {
        cards = new ArrayList<>();
        for (int j = 0; j < ranks.length; j++) {
            for (String suitString : suits) {
                cards.add(new Card(ranks[j], suitString, values[j]));
            }
        }
        size = cards.size();
        shuffle();
    }

    /**
     * Determines if this deck is empty (no undealt cards).
     * @return true if this deck is empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Accesses the number of undealt cards in this deck.
     * @return the number of undealt cards in this deck.
     */
    public int size() {
        return size;
    }

    /**
     * Randomly permute the given collection of cards
     * and reset the size to represent the entire deck.
     */
    public void shuffle() {
        Random random = new Random();
        for (int i = 1; i <= 51; i++) {
            int r = random.nextInt(i) + 1;
            Card atR = cards.get(r);
            cards.set(r, cards.get(i));
            cards.set(i, atR);
        }
        size = 52;
    }

    /**
     * Deals a card from this deck.
     * @return the card just dealt, or null if all the cards have been
     *         previously dealt.
     */
    public Card deal() {
        if (isEmpty()) {
            return null;
        }
        size--;
        return cards.get(size);
    }

    /**
     * Generates and returns a string representation of this deck.
     * @return a string representation of this deck.
     */
    @SuppressWarnings("StringConcatenationInLoop")
    @Override
    public String toString() {
        String rtn = "size = " + size + "\nUndealt cards: \n";

        for (int k = size - 1; k >= 0; k--) {
            rtn = rtn + cards.get(k);
            if (k != 0) {
                rtn = rtn + ", ";
            }
            if ((size - k) % 2 == 0) {
                // Insert carriage returns so entire deck is visible on console.
                rtn = rtn + "\n";
            }
        }

        rtn = rtn + "\nDealt cards: \n";
        for (int k = cards.size() - 1; k >= size; k--) {
            rtn = rtn + cards.get(k);
            if (k != size) {
                rtn = rtn + ", ";
            }
            if ((k - cards.size()) % 2 == 0) {
                // Insert carriage returns so entire deck is visible on console.
                rtn = rtn + "\n";
            }
        }

        rtn = rtn + "\n";
        return rtn;
    }
}
