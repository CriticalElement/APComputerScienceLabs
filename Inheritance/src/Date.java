@SuppressWarnings("unused")
public class Date
{
    private final int month;
    private final int day;
    private final int year;

    public Date() {
        month = 0;
        day   = 0;
        year  = 0;
    }

    public Date(int m, int d, int y) {
        month = m;
        day   = d;
        year  = y;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }
}
