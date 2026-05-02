import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Facade for the CMR Center: customer registry, scheduling, report‐generation,
 * billing, and (now) iterator access.
 */
public class CMRManagementSystem {
	private final Map<String, Customer> customersByNationalId = new HashMap<>();
	private final AppointmentScheduler appointmentScheduler = new AppointmentScheduler();
	private final List<Appointment> allAppointments = new ArrayList<>();

	private final CustomerFactory customerFactory = new ConcreteCustomerFactory();
	private final ReportGenerator reportGenerator = new ReportGenerator();

	/** Registers a new customer of the given category. */
	public void registerCustomer(String fullName, String address, String nationalId, String contactNumber,
			String category) {
		if (nationalId == null || nationalId.trim().isEmpty()) {
			throw new IllegalArgumentException("National ID cannot be empty.");
		}
		if (customersByNationalId.containsKey(nationalId)) {
			throw new IllegalArgumentException("Customer with National ID " + nationalId + " already exists.");
		}
		Customer c = customerFactory.createCustomer(fullName, address, nationalId, contactNumber, category);
		customersByNationalId.put(nationalId, c);
	}

	/** Schedules an appointment if possible. */
	public boolean scheduleAppointment(String nationalId, String plateNumber, LocalDateTime startTime) {
		Customer c = customersByNationalId.get(nationalId);
		if (c == null)
			return false;
		// find the car
		Car chosenCar = null;
		for (Car car : c.getCars()) {
			if (car.getPlateNumber().equalsIgnoreCase(plateNumber)) {
				chosenCar = car;
				break;
			}
		}
		if (chosenCar == null)
			return false;

		Appointment appt = new Appointment(c, chosenCar, startTime);
		boolean ok = appointmentScheduler.scheduleAppointment(appt);
		if (ok)
			allAppointments.add(appt);
		return ok;
	}

	/** Adds a service report to a matching appointment. */
	public boolean addServiceReport(String nationalId, LocalDate date, String[] repairs, String[] parts,
			double partsCost) {
		for (Appointment a : allAppointments) {
			if (a.getCustomer().getNationalId().equals(nationalId) && a.getDate().equals(date)) {
				new ServiceReport(a, Arrays.asList(repairs), Arrays.asList(parts), partsCost);
				return true;
			}
		}
		return false;
	}

	/** Adds a diagnostic report to a matching appointment. */
	public boolean addDiagnosticReport(String nationalId, LocalDate date, String[] issues, String[] recommendations,
			double diagnosticFee) {
		for (Appointment a : allAppointments) {
			if (a.getCustomer().getNationalId().equals(nationalId) && a.getDate().equals(date)) {
				new DiagnosticReport(a, Arrays.asList(issues), Arrays.asList(recommendations), diagnosticFee);
				return true;
			}
		}
		return false;
	}

	/** Prints or returns a Bill for a given appointment. */
	public Bill printBillByNationalId(String nationalId, LocalDateTime startTime) {
		for (Appointment a : allAppointments) {
			if (a.getCustomer().getNationalId().equals(nationalId) && a.getStartTime().equals(startTime)) {
				if (a.getBill() == null)
					a.generateBill();
				return a.getBill();
			}
		}
		return null;
	}

	/** Legacy: returns a List of daily appointments. */
	public List<Appointment> printDailyAppointments(LocalDate date) {
		return appointmentScheduler.getDailySchedule(date);
	}

	/** New: returns an Iterator over daily appointments. */
	public Iterator<Appointment> getDailyAppointmentsIterator(LocalDate date) {
		return appointmentScheduler.getDailyScheduleIterator(date);
	}

	/**
	 * Generates a DailyReport (serviced only) and writes it to "daily_report.txt".
	 */
	public DailyReport generateDailyReport(LocalDate date) {
		List<Appointment> daily = appointmentScheduler.getDailySchedule(date);
		if (daily.isEmpty())
			return null;

		DailyReport report = reportGenerator.generateDailyReport(date, daily);

		// write out to daily_report.txt
		try (FileWriter writer = new FileWriter("daily_report.txt")) {
			writer.write(report.getFormattedText());
		} catch (IOException e) {
			// you may want to log this properly in a real system
			e.printStackTrace();
		}

		return report;
	}

	/** Utility to look up a customer by their national ID. */
	public Customer getCustomerById(String nationalId) {
		return customersByNationalId.get(nationalId);
	}
}
