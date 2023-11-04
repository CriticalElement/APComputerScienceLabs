public class GeoLocationRunner
{
    public static void main(String[] args)
    {
        GeoLocation fisdAdmin = new GeoLocation(33.123961, -96.798735);
        System.out.println(fisdAdmin);

        Place fisdAdminPlace = new Place("Frisco ISD Admin Building", "5515 Ohio Dr, Frisco, TX 75035",
                fisdAdmin);
        System.out.println(fisdAdminPlace);
        Place school = new Place("Independence High School", "10555 Independence Pkwy, Frisco, TX 75035",
                33.1645638, -96.7512269);
        System.out.println(school);
        System.out.println("Distance: " + school.location.distanceTo(fisdAdminPlace.location));
    }
}
