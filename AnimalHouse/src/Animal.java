import java.util.ArrayList;
import java.util.Objects;

public class Animal {
    private final String name;
    private final int birthyear;
    private final ArrayList<Toy> toys = new ArrayList<>();
    private Animal friend = null;
    static int currentYear = 2023;

    Animal(String name, int birthyear) {
        this.name = name;
        this.birthyear = birthyear;
    }

    public void addToy(Toy toy) {
        toys.add(toy);
    }

    public void setFriend(Animal friend) {
        this.friend = friend;
    }

    public int getAge() {
        return currentYear - birthyear;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Toy> getToys() {
        return toys;
    }

    @Override
    public String toString() {
        String string = "Hello, I am " + name + ". I am " + getAge() + " years old.\n";
        if (!Objects.isNull(friend)) {
            string += "I have a friend named " + friend.getName() + ".\n";
        }
        else {
            string += "I have no friends :(\n";
        }
        string += "I have the following toys: " + toys + "\n";
        return string;
    }
}
