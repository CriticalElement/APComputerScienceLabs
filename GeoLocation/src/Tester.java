import java.util.Scanner;

public class Tester
{
    public static void main(String[] args)
    {
        System.out.println("Hello again, world!");
        // this is a comment!

        int numApples = 20;

        final int PRICE_OF_APPLES = 199;

        System.out.println("The total for " + numApples + " apples: " + numApples * PRICE_OF_APPLES + " cents");

        if (numApples * PRICE_OF_APPLES >= 2000)
            System.out.println("Thank you valued customer!");

        for (int i = 10; i > 0; i--) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 150; i <= 300; i += 3) {
            System.out.print(i + " ");
        }
        System.out.println();

        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += i;
        }
        System.out.println(sum);

        // 13:
        // no

        Scanner console = new Scanner(System.in);

        System.out.print("Enter a double: ");
        double num = console.nextDouble();
        System.out.println(Math.sqrt(num));

        System.out.println(num + "^3 = " + Math.pow(num, 3));

        System.out.print("Enter an int: ");
        int num1 = console.nextInt();
        System.out.print("Enter an int: ");
        int num2 = console.nextInt();

        if (Math.abs(num2 - num1) <= 10)
            System.out.println("Within 10");
        else
            System.out.println("Not within 10");

        sum = 0;
        double numNums = 0;
        System.out.println("Enter numbers to add / average: ");

        while (true) {
            int entered = console.nextInt();
            if (entered == 0)
                break;
            sum += entered;
            numNums++;
        }

        System.out.println("Sum of entered values: " + sum);
        System.out.println("Average of entered values: " + (sum / numNums));

        double[] areas = new double[20];
        areas[0] = 4.56;
        int length = areas.length;
        boolean[] arr = new boolean[] {true, true, false, false, true};

        // 24
        // no

        simpleMethod();
        System.out.println(sum(7, 5));
        System.out.println(sumToN(14));
        triangle(7);
        System.out.println(altCaps("aaaaa"));

        Player player1 = new Player();
        Player player2 = new Player("Bob", 5);
        System.out.println(player1.playerInfo());
        System.out.println(player2.playerInfo());

    }

    public static void simpleMethod()
    {
        System.out.println("This is a method!");
    }

    public static int sum(int a, int b)
    {
        return a + b;
    }

    public static int sumToN(int n)
    {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            if (i % 3 == 0 || i % 5 == 0)
                sum += i;
        }
        return sum;
    }

    public static void triangle(int n)
    {
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                System.out.print(i);
            }
            System.out.println();
        }
    }

    public static String altCaps(String s)
    {
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLetter(c)) {
                if (i % 2 == 0)
                    result += Character.toString(c).toUpperCase();
                else
                    result += Character.toString(c).toLowerCase();
            }

        }
        return result;
    }
}
