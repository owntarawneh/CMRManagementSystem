import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;

public class CMRManagementSystemTest {

    private CMRManagementSystem system;

    @BeforeEach
    void setUp() {
        system = new CMRManagementSystem();
    }

    // --- Functionality 1: Customer Registration ---

    @Test
    void TC1_1_registerPrivateCustomer() {
        system.registerCustomer("Alice", "123 Main St", "ID123", "555-0100", "private");
        Customer c = system.getCustomerById("ID123");
        assertNotNull(c);
        assertTrue(c instanceof PrivateCustomer);
        assertEquals("Alice", c.getFullName());
    }

    @Test
    void TC1_2_registerFleetCustomer() {
        system.registerCustomer("BobCo", "456 Park Ave", "FLEET1", "555-0200", "fleet");
        Customer c = system.getCustomerById("FLEET1");
        assertNotNull(c);
        assertTrue(c instanceof FleetCustomer);
        assertEquals(0.30, c.getDiscountRate());
    }

    @Test
    void TC1_3_missingIdFieldFails() {
        // we expect an exception if nationalId is blank
        assertThrows(IllegalArgumentException.class, () ->
            system.registerCustomer("NoID", "Nowhere", "", "555-0000", "private")
        );
    }

    // --- Functionality 2: Appointment Scheduling ---

    @Test
    void TC2_1_validSameDayAppointment() {
        // prepare customer + car
        system.registerCustomer("Carol", "Addr", "ID456", "555-0300", "private");
        Customer carol = system.getCustomerById("ID456");
        carol.registerCar(new Car("Toyota", "Corolla", "PLATE1", carol));
        carol.setPreferredPaymentMethod(new CashPayment());

        LocalDateTime slot = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
        boolean ok = system.scheduleAppointment("ID456", "PLATE1", slot);
        assertTrue(ok);
    }

    @Test
    void TC2_2_doubleBookingRejected() {
        system.registerCustomer("Dan", "Addr", "ID789", "555-0400", "private");
        Customer dan = system.getCustomerById("ID789");
        dan.registerCar(new Car("Honda", "Civic", "PLATE2", dan));
        dan.setPreferredPaymentMethod(new CashPayment());
        LocalDateTime slot = LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0));
        assertTrue(system.scheduleAppointment("ID789", "PLATE2", slot));
        // try again at the same time
        assertFalse(system.scheduleAppointment("ID789", "PLATE2", slot));
    }

    @Test
    void TC2_3_invalidTimeAfterBusinessHours() {
        system.registerCustomer("Eve", "Addr", "ID321", "555-0500", "private");
        Customer eve = system.getCustomerById("ID321");
        eve.registerCar(new Car("Ford", "Focus", "PLATE3", eve));
        eve.setPreferredPaymentMethod(new CashPayment());
        // 18:00 is after the 17:00 cutoff
        LocalDateTime late = LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0));
        assertFalse(system.scheduleAppointment("ID321", "PLATE3", late));
    }

    // --- Functionality 3: Billing and Discount Calculation ---

    @Test
    void TC3_1_privateCustomerNoParts() {
        system.registerCustomer("Frank", "Addr", "ID555", "555-0600", "private");
        Customer frank = system.getCustomerById("ID555");
        frank.registerCar(new Car("Mazda", "3", "PLATE4", frank));
        frank.setPreferredPaymentMethod(new CashPayment());

        LocalDateTime slot = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 30));
        system.scheduleAppointment("ID555", "PLATE4", slot);
        // no reports added
        Bill bill = system.printBillByNationalId("ID555", slot);
        assertEquals(50.0, bill.calculateTotalAmount());
    }

    @Test
    void TC3_2_fleetCustomerWithParts() {
        system.registerCustomer("FleetX", "Addr", "F100", "555-0700", "fleet");
        Customer fx = system.getCustomerById("F100");
        fx.registerCar(new Car("Volvo", "V60", "PLATE5", fx));
        fx.setPreferredPaymentMethod(new CashPayment());

        LocalDateTime slot = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
        system.scheduleAppointment("F100", "PLATE5", slot);
        system.addServiceReport("F100", LocalDate.now(), new String[]{"R1"}, new String[]{"P1"}, 30.0);

        Bill bill = system.printBillByNationalId("F100", slot);
        // (50 + 30) - (0.30 * 80) = 80 - 24 = 56
        assertEquals(56.0, bill.calculateTotalAmount());
    }

    @Test
    void TC3_3_staffCustomerWithAllServices() {
        system.registerCustomer("Staffer", "Addr", "S200", "555-0800", "staff");
        Customer staff = system.getCustomerById("S200");
        staff.registerCar(new Car("BMW", "X1", "PLATE6", staff));
        staff.setPreferredPaymentMethod(new CashPayment());

        LocalDateTime slot = LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0));
        system.scheduleAppointment("S200", "PLATE6", slot);
        system.addServiceReport("S200", LocalDate.now(), new String[]{"R1"}, new String[]{"P1"}, 20.0);
        system.addDiagnosticReport("S200", LocalDate.now(), new String[]{"I1"}, new String[]{"A1"}, 10.0);

        Bill bill = system.printBillByNationalId("S200", slot);
        // (50 + 20 + 10) - 50% = 80 - 40 = 40
        assertEquals(40.0, bill.calculateTotalAmount());
    }

    // --- Functionality 4: Integration Testing ---

    @Test
    void TC4_1_fullWorkflowProducesCorrectBill() {
        // staff has 50% discount
        system.registerCustomer("Sarah", "Addr", "STF1", "555-0900", "staff");
        Customer sarah = system.getCustomerById("STF1");
        sarah.registerCar(new Car("Audi", "A3", "PLATE7", sarah));
        sarah.setPreferredPaymentMethod(new CashPayment());

        LocalDateTime slot = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
        assertTrue(system.scheduleAppointment("STF1", "PLATE7", slot));

        system.addServiceReport("STF1", LocalDate.now(), new String[]{}, new String[]{}, 20.0);
        system.addDiagnosticReport("STF1", LocalDate.now(), new String[]{}, new String[]{}, 10.0);

        Bill bill = system.printBillByNationalId("STF1", slot);
        assertEquals(40.0, bill.calculateTotalAmount());
    }

    @Test
    void TC4_2_dailyReportContainsBothSummaries() {
        // reuse the TC4_1 setup
        TC4_1_fullWorkflowProducesCorrectBill();

        DailyReport dr = system.generateDailyReport(LocalDate.now());
        String text = dr.getFormattedText();

        assertTrue(text.contains("ServiceReport ID:"));
        assertTrue(text.contains("DiagnosticReport ID:"));
        // exactly one entry line
        long count = text.lines().filter(l -> l.startsWith("Time:")).count();
        assertEquals(1, count);
    }

    @Test
    void TC4_3_preferredPaymentMethodIsHonored() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        // --- Alice: private → cash at 11:00 ---
        system.registerCustomer("Alice", "Addr", "P001", "555-1000", "private");
        Customer alice = system.getCustomerById("P001");
        alice.registerCar(new Car("Kia", "Rio", "PLATE8", alice));
        alice.setPreferredPaymentMethod(new CashPayment());
        LocalDateTime slotA = LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0));
        assertTrue(system.scheduleAppointment("P001", "PLATE8", slotA));
        Bill billA = system.printBillByNationalId("P001", slotA);
        assertNotNull(billA);
        assertTrue(billA.payBill());
        assertTrue(out.toString().contains("Processing cash payment"));

        out.reset();

    }
}
