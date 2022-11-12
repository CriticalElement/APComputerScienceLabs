import java.io.FileNotFoundException;

public class TestCemetery {
    public static void main(String[] args) throws FileNotFoundException {
        Cemetery cemetery = new Cemetery("cemetery.txt");
        System.out.println("Average lifespan of everyone who lived on Little Carter Lane: " + cemetery.averageAge());
    }
}
