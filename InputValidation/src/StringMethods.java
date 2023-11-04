import java.util.Scanner;

public class StringMethods
{
    public StringMethods() {}

    public void unusualHello(String name)
    {
        System.out.println("Hello " + name + ", you rock!");
    }

    public String stringRipper(String str)
    {
        return str.substring(0, 1) + str.charAt(str.length() - 1);
    }

    public boolean evenFooBar(String s)
    {
        int numFoo = 0;
        int numBar = 0;

        for (int i = 0; i < s.length() - 2; i++)
        {
            String subStr = s.substring(i, i + 3);
            if (subStr.equals("foo"))
            {
                numFoo++;
            } else if (subStr.equals("bar"))
            {
                numBar++;
            }
        }

        return numBar == numFoo;
    }

    public int sumString(String str)
    {
        Scanner scan = new Scanner(str);
        int sum = 0;

        while (scan.hasNext())
        {
            String next = scan.next();
            if (next.length() == 1 && Character.isDigit(next.charAt(0)))
            {
                sum += Integer.parseInt(next.substring(0, 1));
            }
        }

        return sum;
    }

    public String decode(String key, String code)
    {
        Scanner scan = new Scanner(code);
        String finalStr = "";

        while (scan.hasNext())
        {
            finalStr += key.charAt(scan.nextInt());
        }

        return finalStr;
    }
}
