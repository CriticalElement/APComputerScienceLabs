import java.util.ArrayList;

public class Inventory
{
    private final ArrayList<Vehicle> vehicles;

    public Inventory()
    {
        vehicles = new ArrayList<>();
    }

    void addVehicle(Vehicle v)
    {
        vehicles.add(v);
    }

    void listInventory()
    {
        vehicles.forEach(vehicle -> System.out.println(vehicle.getInfo()));
    }
}
