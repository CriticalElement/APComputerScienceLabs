import java.util.ArrayList;

public class Bookstore {
    private final ArrayList<Book> inventory;

    public Bookstore() {
        this.inventory = new ArrayList<>();
    }

    public void addBook(Book b) {
        inventory.add(b);
    }

    public int numBooks() {
        return inventory.size();
    }

    public Book getBook(int index) {
        if (index >= 0 && index < numBooks()) {
            return inventory.get(index);
        }
        return null;
    }
}
