import java.util.ArrayList;
import java.util.List;

public abstract class Board {

    /**
     * The ranks of the cards for this game to be sent to the deck.
     */
    private static final String[] RANKS =
            {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};

    /**
     * The suits of the cards for this game to be sent to the deck.
     */
    private static final String[] SUITS =
            {"spades", "hearts", "diamonds", "clubs"};

    /**
     * The cards on this board.
     */
    private final Card[] cards;

    /**
     * The deck of cards being used to play the current game.
     */
    private final Deck deck;

    /**
     * Flag used to control debugging print statements.
     */
    private static final boolean I_AM_DEBUGGING = false;

    /**
     * Creates a new <code>Board</code> instance.
     */
    public Board(int BOARD_SIZE, int[] POINT_VALUES) {
        cards = new Card[BOARD_SIZE];
        deck = new Deck(RANKS, SUITS, POINT_VALUES);
        if (I_AM_DEBUGGING) {
            System.out.println(deck);
            System.out.println("----------");
        }
        dealMyCards();
    }

    /**
     * Start a new game by shuffling the deck and
     * dealing some cards to this board.
     */
    public void newGame() {
        deck.shuffle();
        dealMyCards();
    }

    /**
     * Accesses the size of the board.
     * Note that this is not the number of cards it contains,
     * which will be smaller near the end of a winning game.
     * @return the size of the board
     */
    public int size() {
        return cards.length;
    }

    /**
     * Determines if the board is empty (has no cards).
     * @return true if this board is empty; false otherwise.
     */
    public boolean isEmpty() {
        for (Card card : cards) {
            if (card != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Deal a card to the kth position in this board.
     * If the deck is empty, the kth card is set to null.
     * @param k the index of the card to be dealt.
     */
    public void deal(int k) {
        cards[k] = deck.deal();
    }

    /**
     * Accesses the deck's size.
     * @return the number of undealt cards left in the deck.
     */
    public int deckSize() {
        return deck.size();
    }

    /**
     * Accesses a card on the board.
     * @return the card at position k on the board.
     * @param k is the board position of the card to return.
     */
    public Card cardAt(int k) {
        return cards[k];
    }

    /**
     * Replaces selected cards on the board by dealing new cards.
     * @param selectedCards is a list of the indices of the
     *        cards to be replaced.
     */
    public void replaceSelectedCards(List<Integer> selectedCards) {
        for (Integer k : selectedCards) {
            deal(k);
        }
    }

    /**
     * Gets the indexes of the actual (non-null) cards on the board.
     *
     * @return a List that contains the locations (indexes)
     *         of the non-null entries on the board.
     */
    public List<Integer> cardIndexes() {
        List<Integer> cardIndexes = new ArrayList<>();
        for (int i = 0; i < cards.length; i++) {
            if (cards[i] != null) {
                cardIndexes.add(i);
            }
        }
        return cardIndexes;
    }

    /**
     * Generates and returns a string representation of this board.
     * @return the string version of this board.
     */
    public String toString() {
        String s = "";
        for (int k = 0; k < cards.length; k++) {
            //noinspection StringConcatenationInLoop
            s = s + k + ": " + cards[k] + "\n";
        }
        return s;
    }

    /**
     * Determine whether the game has been won,
     * i.e. neither the board nor the deck has any more cards.
     * @return true when the current game has been won;
     *         false otherwise.
     */
    @SuppressWarnings("unused")
    public boolean gameIsWon() {
        if (deck.isEmpty()) {
            for (Card c : cards) {
                if (c != null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Deal cards to this board to start the game.
     */
    private void dealMyCards() {
        for (int k = 0; k < cards.length; k++) {
            cards[k] = deck.deal();
        }
    }

    public abstract boolean isLegal(List<Integer> selectedCards);

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public abstract boolean anotherPlayIsPossible();
}
