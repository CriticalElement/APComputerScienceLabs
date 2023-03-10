@SuppressWarnings("unused")
public class Truck extends Vehicle
{
    private final int towing;

    public Truck(String type, int year, double price, int towing)
    {
        super(type, year, price);
        this.towing = towing;
    }

    public boolean canTowBoat()
    {
        return towing >= 2000;
    }

    @Override
    public String getInfo()
    {
        return super.getInfo() + ", " + towing + " lbs. towing, $" + getPrice();
    }
}
