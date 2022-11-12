public class Tombstone {
    private final String name;
    private final String burialDate;
    private final int age;
    private final String address;

    public Tombstone(String name, String burialDate, int age, String address) {
        this.name = name;
        this.burialDate = burialDate;
        this.age = age;
        this.address = address;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public String getBurialDate() {
        return burialDate;
    }

    public int getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }
}
