@SuppressWarnings("unused")
public class Appointment extends Date
{
    private final String text;

    public Appointment()
    {
        super();
        text = "";
    }

    public Appointment(int month, int day, int year, String text)
    {
        super(month, day, year);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return super.toString() + " " + text;
    }
}
