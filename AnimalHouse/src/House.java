import java.util.ArrayList;

public class House {
    private final ArrayList<Animal> animals = new ArrayList<>();

    public void printAnimals() {
        animals.forEach(System.out::println);
    }

    public void add(Animal animal) {
        animals.add(animal);
    }

    public void cleanHouse() {
        for (Animal animal : animals) {
            ArrayList<Toy> toys = animal.getToys();
            for (int i = toys.size() - 1; i >= 0; i--) {
                Toy curToy = toys.get(i);
                int lastIndex = toys.lastIndexOf(curToy);
                if (toys.indexOf(curToy) != lastIndex) {
                    toys.remove(lastIndex);
                }
            }
        }
    }
}
