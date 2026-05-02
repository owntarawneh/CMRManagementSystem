import java.time.LocalDate;
import java.util.List;

public class MechanicStaff extends Staff {
    private CMRManagementSystem system;

    public MechanicStaff(String staffId, String fullName, CMRManagementSystem system) {
        super(staffId, fullName, "Mechanic");
        this.system = system;
    }

    public void viewScheduledAppointments(LocalDate date) {
        List<Appointment> list = system.printDailyAppointments(date);
        if (list.isEmpty()) {
            System.out.println("No appointments found for " + date);
        } else {
            System.out.println("Appointments on " + date + ":");
            for (Appointment a : list) {
                System.out.println(a.getAppointmentDetails());
            }
        }
    }

    public void addServiceReport(String nationalId, LocalDate date, String[] repairs, String[] parts, double partsCost) {
        boolean success = system.addServiceReport(nationalId, date, repairs, parts, partsCost);
        if (success) {
            System.out.println("Service report added successfully.");
        } else {
            System.out.println("Failed to add service report (appointment not found).");
        }
    }

    public void showResponsibilities() {
        System.out.println("As Mechanic Staff, you can:");
        System.out.println("  1) View scheduled appointments for a date");
        System.out.println("  2) Add service report to an appointment");
        System.out.println("  0) Logout");
    }
}
