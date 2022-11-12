import java.util.ArrayList;
import java.util.Arrays;

public class ArrayListProbsRunner {
    public static void main(String[] args) {
        ArrayListProbs arr = new ArrayListProbs();

        arr.makeListAndPrint(12, 50);
        System.out.println(arr.addOne(new ArrayList<>(Arrays.asList(1, 2, 3, 4))));
        System.out.println(arr.minToFront(new ArrayList<>(Arrays.asList(2, 4, -5, 3))));
        System.out.println(arr.removeDupes(new ArrayList<>(Arrays.asList("to", "to", "be", "be", "be", "be", "or"))));
        System.out.println(arr.swapPairs(new ArrayList<>(Arrays.asList(1, 2, 3, 4))));
        System.out.println(arr.removeLenN(new ArrayList<>(Arrays.asList("wow", "hello", "world", "omg")),3));
        System.out.println(arr.dumbestPerson(new ArrayList<>(Arrays.asList(new Person("Rita", 150), new Person("Ron", 100), new Person("Ronda", 120)))));
        System.out.println(arr.highestPricedBook(new ArrayList<>(Arrays.asList(new Book("Book 1", "Author 1", 12.99), new Book("Book 2", "Author 2", 15.99), new Book("Book 3", "Author 3", 9.99)))).toString());
        System.out.println(arr.banBook(new ArrayList<>(Arrays.asList(new Book("Book 1", "Author 1", 12.99), new Book("Book 2", "Author 2", 15.99), new Book("Book 3", "Author 3", 9.99))), new Book("Book 1", "Author 1", 12.99)));
        // joke about java devs having super ultrawide monitors here
        Bookstore bookstore = new Bookstore();
        bookstore.addBook(new Book("Book 1", "Author 1", 12.99));
        bookstore.addBook(new Book("Book 2", "Author 2", 15.99));
        bookstore.addBook(new Book("Book 3", "Author 3", 9.99));
        System.out.println(arr.bookstoreValue(bookstore));
    }
}
