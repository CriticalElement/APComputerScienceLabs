public class Toy {
    private final String name;

    Toy(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            final Toy other = (Toy) obj;
            return name.equals(other.getName());
        }
        return false;
    }

    @Override
    public String toString() {
        return "A " + name;
    }
}
