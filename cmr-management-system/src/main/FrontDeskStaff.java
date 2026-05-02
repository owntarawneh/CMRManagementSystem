import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Front‐desk UI class.  Now prints daily appointments via Iterator.
 */
public class FrontDeskStaff extends Staff {
    private final CMRManagementSystem system;

    public FrontDeskStaff(String staffId, String fullName, CMRManagementSystem system) {
        super(staffId, fullName, "FrontDesk");
        this.system = system;
    }

    public void registerCustomer(String fullName,
                                 String address,
                                 String nationalId,
                                 String contactNumber,
                                 String category) {
        system.registerCustomer(fullName, address, nationalId, contactNumber, category);
        System.out.println("Customer registered successfully.");
    }

    public void scheduleAppointment(String nationalId,
                                    String plateNumber,
                                    LocalDateTime startTime) {
        boolean success = system.scheduleAppointment(nationalId, plateNumber, startTime);
        if (success) {
            System.out.println("Appointment scheduled successfully at " + startTime);
        } else {
            System.out.println("Failed to schedule appointment. Check constraints.");
        }
    }

    public void generateAppointmentBill(String nationalId, LocalDateTime startTime) {
        Bill bill = system.printBillByNationalId(nationalId, startTime);
        if (bill != null) {
            System.out.println("Generated Bill:\n" + bill.getItemizedBreakdown());
        } else {
            System.out.println("Unable to generate bill (appointment not found).");
        }
    }

    public void printBill(String nationalId, LocalDateTime startTime) {
        Bill bill = system.printBillByNationalId(nationalId, startTime);
        if (bill != null) {
            System.out.println("Bill Details:\n" + bill.getItemizedBreakdown());
        } else {
            System.out.println("Bill not found for given appointment.");
        }
    }

    /**
     * Now uses the Iterator Pattern to traverse daily appointments.
     */
    public void printDailyAppointments(LocalDate date) {
        Iterator<Appointment> iter = system.getDailyAppointmentsIterator(date);
        if (!iter.hasNext()) {
            System.out.println("No appointments found for " + date);
            return;
        }
        System.out.println("Appointments on " + date + ":");
        while (iter.hasNext()) {
            Appointment a = iter.next();
            System.out.println(a.getAppointmentDetails());
        }
    }

    public void showResponsibilities() {
        System.out.println("As Front Desk Staff, you can:");
        System.out.println("  1) Register a new customer");
        System.out.println("  2) Schedule a same-day appointment");
        System.out.println("  3) Generate/print a bill for an appointment");
        System.out.println("  4) Print daily appointments");
        System.out.println("  0) Logout");
    }
}
