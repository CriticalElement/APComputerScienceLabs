public class Book {
    private final String title;
    private final String author;
    private final double price;

    Book(String title, String author, double price) {
        this.title = title;
        this.author = author;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    @SuppressWarnings("unused")
    public String getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return title + ", by " + author + ". Cost: $" + price;
    }
}
