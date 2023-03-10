@SuppressWarnings("unused")
public class Motorcycle extends Vehicle
{
    private final int cc;

    public Motorcycle(String type, int year, double price, int cc)
    {
        super(type, year, price);
        this.cc = cc;
    }

    public int getCC()
    {
        return cc;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + ", " + cc + " cc, $" + getPrice();
    }
}
