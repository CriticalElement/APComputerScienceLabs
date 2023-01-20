public class Dog extends Animal {
    private final boolean goodBoy;

    Dog(String name, int birthyear, boolean goodBoy) {
        super(name, birthyear);
        this.goodBoy = goodBoy;
    }

    @Override
    public String toString() {
        return super.toString() + "I am a " + (goodBoy ? "good" : "bad") + " boy.\n";
    }
}
