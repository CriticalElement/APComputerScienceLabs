import java.util.*;

public class CombinationLockTest
{
    public static void main(String[] args)
    {
        CombinationLock myLock = new CombinationLock("11-22-33");
        System.out.println("The current combination is " + myLock.getCombination());
        System.out.println();
        myLock.open();
        System.out.println(myLock);
        myLock.close();

        System.out.println();
        System.out.println("Change combination to 55-66-77");
        System.out.print("Type new combination -->");
        Scanner console = new Scanner(System.in);
        String combo = console.nextLine();
        myLock.setCombination(combo);
        System.out.println("Combination has been changed.");
        System.out.println();

        myLock.open();
        System.out.println(myLock);
        myLock.close();

        System.out.println();
        System.out.println("Type an incorrect combination.");

        myLock.open();
        System.out.println(myLock);
        myLock.close();
    }
}
