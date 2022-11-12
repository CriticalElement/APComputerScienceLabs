import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.OptionalDouble;
import java.util.Scanner;

public class Cemetery {
    private final ArrayList<Tombstone> tombstones;

    Cemetery(String fileName) throws FileNotFoundException {
        tombstones = new ArrayList<>();
        Scanner scan = new Scanner(new File(fileName));
        while (scan.hasNextLine()) {
            Scanner line = new Scanner(scan.nextLine());
            StringBuilder name = new StringBuilder();
            while (line.hasNext("[a-zA-Z.]+")) {
                name.append(line.next()).append(" ");
            }
            name.deleteCharAt(name.length() - 1);
            String burialDate = line.nextInt() + " " + line.next() + " " + line.nextInt();
            int age = parseAge(line.next());
            String address = line.nextLine();
            tombstones.add(new Tombstone(name.toString(), burialDate, age, address));
        }
    }

    public double averageAge() {
        ArrayList<Integer> ages = new ArrayList<>();
        tombstones.stream().filter(tomb -> tomb.getAddress().contains("Little Carter Lane")).forEach(tomb -> ages.add(tomb.getAge()));
        OptionalDouble avg = ages.stream().mapToInt(i -> i).average();
        assert avg.isPresent();
        double result = avg.getAsDouble() / 360;
        result = Math.round(result * 10) / 10.0;
        return result;
    }

    /**
     * convert the ageString to a number of days; age can
     * take a variety of forms in the data file
     */
    public static int parseAge(String ageString)
    {
        if (ageString.contains("d")) { //age supplied in days
            ageString = ageString.replaceAll("d", "");
            return Integer.parseInt(ageString);
        }

        int result = 0;

        boolean done = true;

        try { result = Integer.parseInt(ageString); } //is the String a whole number of years?

        catch (NumberFormatException n) { done = false; }

        if (done) //successfully parsed as an int, return value
            return 365 * result; //ignoring leap years

        double ageDouble = 0;

        done = true;

        try { ageDouble = Double.parseDouble(ageString); } //is the String a floating point number of years?

        catch (NumberFormatException n) { done = false; }

        if (done) { //successfully parse as a double, String doesn't contain any text
            return (int)(ageDouble * 365); //ignoring leap years, using 30 for days in a month
        }

        if (ageString.contains("w")) { //age is supplied in weeks, return appropriately
            ageString = ageString.replaceAll("w", "");
            return Integer.parseInt(ageString) * 7;
        }

        return 0;
    }
}
