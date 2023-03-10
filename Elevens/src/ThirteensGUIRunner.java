public class ThirteensGUIRunner {
    public static void main(String[] args) {
        ThirteensBoard board = new ThirteensBoard();
        CardGameGUI gui = new CardGameGUI(board);
        gui.displayGame();
    }
}
