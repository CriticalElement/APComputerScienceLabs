public class Place
{
    final String name;
    final String address;
    final GeoLocation location;

    public Place(String name, String address, double latitude, double longitude)
    {
        this.name = name;
        this.address = address;
        this.location = new GeoLocation(latitude, longitude);
    }

    public Place(String name, String address, GeoLocation location)
    {
        this.name = name;
        this.address = address;
        this.location = location;
    }

    public double distanceTo(Place other)
    {
        return location.distanceTo(other.location);
    }

    @Override
    public String toString() {
        return name + "\n" + address + "\n" + location.toString();
    }
}
