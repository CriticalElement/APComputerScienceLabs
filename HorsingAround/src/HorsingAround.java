import java.util.*;

public class HorsingAround
{
    public static int totalWidth(ArrayList<Animal> animals)
    {
        return animals.stream().mapToInt(Animal::getWidth).sum();
    }

    public static Animal tallestAnimal(ArrayList<Animal> animals)
    {
        int index = 0;
        for (int i = 0; i < animals.size(); i++) {
            if (animals.get(i).getHeight() > animals.get(index).getHeight())
                index = i;
        }

        return animals.get(index);
    }

    public static int countYourChickens(ArrayList<Animal> animals)
    {
        return (int) animals.stream().filter(animal -> animal.getName().equals("chicken")).count();
    }

    public static ArrayList<String> inventory(ArrayList<Animal> animals)
    {
        ArrayList<String> names = new ArrayList<>();
        animals.forEach(animal -> names.add(animal.getName()));

        return names;
    }

    public static void pestControl(ArrayList<Animal> animals)
    {
    	animals.removeIf(animal -> animal.getName().equals("mouse"));
    }

    public static void horsingAround(ArrayList<Animal> animals)
    {
        int i = 0;
        while (i <= animals.size()) {
            animals.add(i, new Animal("horse"));
            i += 2;
        }
    }

    public static void feelingSheepish(ArrayList<Animal> animals)
    {
        ArrayList<Animal> duplicate = new ArrayList<>(animals);

        for (int i = 0; i < animals.size(); i++) {
            String before = "";
            if (i > 0) {
                before = animals.get(i - 1).getName();
            }
            String after = "";
            if (i < animals.size() - 1) {
                after = animals.get(i + 1).getName();
            }
            if (before.equals("sheep") || after.equals("sheep")) {
                duplicate.set(i, new Animal("sheep"));
            }
        }

        animals.clear();
        animals.addAll(duplicate);
    }

    public static void selectionSort(ArrayList<Animal> animals)
    {
        for (int i = 0; i < animals.size() - 1; i++) {
            int maxIndex = i;

            for (int j = i + 1; j < animals.size(); j++) {
                if (animals.get(j).getHeight() > animals.get(maxIndex).getHeight()) {
                    maxIndex = j;
                }
            }

            Animal temp = new Animal(animals.get(maxIndex).getName());
            animals.set(maxIndex, animals.get(i));
            animals.set(i, temp);
        }
    }
}