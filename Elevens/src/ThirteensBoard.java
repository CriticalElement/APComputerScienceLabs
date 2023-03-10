import java.util.List;

public class ThirteensBoard extends Board {
    private static final int BOARD_SIZE = 10;
    private static final int[] POINT_VALUES =
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0};

    public ThirteensBoard() {
        super(BOARD_SIZE, POINT_VALUES);
    }

    @Override
    public boolean isLegal(List<Integer> selectedCards) {
        if (containsPairSum13(selectedCards) && selectedCards.size() == 2)
            return true;
        return containsKing(selectedCards) && selectedCards.size() == 1;
    }

    @Override
    public boolean anotherPlayIsPossible() {
        List<Integer> listOfCards = cardIndexes();
        return containsPairSum13(listOfCards) || containsKing(listOfCards);
    }

    private boolean containsPairSum13(List<Integer> selectedCards) {
        for (int k : selectedCards) {
            for (int j : selectedCards) {
                if (k == j)
                    continue;
                if (cardAt(k).pointValue() + cardAt(j).pointValue() == 13)
                    return true;
            }
        }
        return false;
    }

    private boolean containsKing(List<Integer> selectedCards) {
        for (int k : selectedCards) {
            Card card = cardAt(k);
            if (card.rank().equals("king"))
                return true;
        }
        return false;
    }
}
