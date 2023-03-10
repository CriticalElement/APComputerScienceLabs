public class Scheduler
{
    public Scheduler()
    {
        //an Appointment is-a Date and can therefore be stored in a Date variable
        //more info on this in future labs
        Date app1 = new Appointment(12,22,2013, "Christmas Party - 7:00pm");
        System.out.println(app1);
        System.out.println();
        Date app2 = new Appointment(1,27,2014, "Dentist - 9:00am");
        System.out.println(app2);
        System.out.println();
        Date app3 = new Appointment(3,27,2014, "Job Interview - 8:30am");
        System.out.println(app3);
        System.out.println();
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    public static void main(String[] args)
    {
        new Scheduler();
    }
}