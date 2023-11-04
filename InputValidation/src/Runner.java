public class Runner
{
    public static void main(String[] args)
    {
        StringMethods test = new StringMethods();

        System.out.print("unusualHello(\"Bob\") >>> ");
        test.unusualHello("Bob");

        System.out.println("\nstringRipper(\"something\") >>> " + test.stringRipper("something") + "\n");

        System.out.println("evenFooBar(\"foobarfoobarbaz\") >>> " + test.evenFooBar("foobarfoobarbaz") + "\n");

        System.out.println("sumString(\"Hi 5 there are 2 or 3 numbers in this String\") >>> " +
                test.sumString("Hi 5 there are 2 or 3 numbers in this String") + "\n");

        System.out.println("decoded message >>> " + test.decode("six perfect quality black jewels amazed the governor",
                "35 10 10 33 9 24 3 17 41 8 3 20 51 16 38 44 47 32 33 10 19 38 35 28 49"));
    }
}
