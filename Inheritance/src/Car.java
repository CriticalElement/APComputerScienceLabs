@SuppressWarnings("unused")
public class Car extends Vehicle
{
    private final double mpg;

    public Car(String type, int year, double price, double mpg)
    {
        super(type, year, price);
        this.mpg = mpg;
    }

    public boolean greatMPG()
    {
        return mpg >= 36;
    }

    @Override
    public String getInfo()
    {
        return super.getInfo() + ", " + mpg + " mpg, $" + getPrice();
    }
}
