public class Runner
{
    public static void main(String[] args)
    {
        Toy t1 = new Toy("Ball");
        Toy t2 = new Toy("Ball");
        Toy t3 = new Toy("Bone");
        Toy t4 = new Toy("Busy Bee");
        Toy t5 = new Toy("Bone");
        Toy t6 = new Toy("Ball");
        Toy t7 = new Toy("Mouse");


        //noinspection NewObjectEquality
        System.out.println("Same toy? " + (t1 == t2)); // tests if they're the same object
        System.out.println("Equal toys? " + t1.equals(t2) + "\n"); // tests if they're equal (equivalent)

        House house = new House();

        Animal a = new Dog("Sparky", 2005, true);
        Animal b = new Cat("Fluffy", 2008);

        a.addToy(t1); a.addToy(t2); a.addToy(t3); a.addToy(t4); a.addToy(t5); a.addToy(t6);

        b.addToy(t7);

        a.setFriend(b); b.setFriend(a); // awww they're best friends

        house.add(a);
        house.add(b);
        house.cleanHouse();
        house.printAnimals();
    }
}