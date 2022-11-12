public class Person {
    private final String name;
    private final int IQ;


    Person(String name, int IQ) {
        this.name = name;
        this.IQ = IQ;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    public int getIQ() {
        return IQ;
    }
}
