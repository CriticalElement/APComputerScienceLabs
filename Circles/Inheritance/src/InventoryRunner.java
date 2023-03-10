public class InventoryRunner
{
    public static void main(String[] args)
    {
        Car car1 = new Car("Honda Civic", 2007, 6000, 34);
        Car car2 = new Car("Dodge Caravan", 2002, 5500, 28);

        Truck truck1 = new Truck("Ford F-150", 2023, 50000, 5000);
        Truck truck2 = new Truck("Toyota Tundra", 2023, 45000, 4000);

        Motorcycle motorcycle1 = new Motorcycle("Yamaha R7", 2022, 10000, 689);

        Inventory inventory = new Inventory();
        inventory.addVehicle(car1);
        inventory.addVehicle(car2);
        inventory.addVehicle(truck1);
        inventory.addVehicle(truck2);
        inventory.addVehicle(motorcycle1);
        inventory.listInventory();
    }
}
