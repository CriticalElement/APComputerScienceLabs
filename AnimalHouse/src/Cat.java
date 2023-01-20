public class Cat extends Animal {
    private final int numLives;

    Cat(String name, int birthyear, int numLives) {
        super(name, birthyear);
        this.numLives = numLives;
    }

    Cat(String name, int birthyear) {
        this(name, birthyear, 9);
    }

    @Override
    public String toString() {
        return super.toString() + "I have " + numLives + " lives.\n";
    }
}
