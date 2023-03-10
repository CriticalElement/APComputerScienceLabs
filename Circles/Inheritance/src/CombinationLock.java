import java.util.Scanner;

@SuppressWarnings("unused")
public class CombinationLock extends Lock
{
    private String combination;

    public CombinationLock()
    {
        super();
        combination = "";
    }

    public CombinationLock(String combo)
    {
        super();
        combination = combo;
    }

    public void open()
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the combination: ");
        String combo = scan.nextLine();

        if (combo.equals(combination))
        {
            super.open();
        }
    }

    public void setCombination(String combination)
    {
        this.combination = combination;
    }

    public String getCombination()
    {
        return combination;
    }

    @Override
    public String toString()
    {
        return super.toString() + "\n" + "Combination = " + combination + "\n";
    }
}
