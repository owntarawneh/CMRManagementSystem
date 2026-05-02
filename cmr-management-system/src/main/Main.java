import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
	private static final Scanner scanner = new Scanner(System.in);
	private static final CMRManagementSystem system = new CMRManagementSystem();

	// Single instances of each role
	private static final FrontDeskStaff frontDesk = new FrontDeskStaff("F01", "Front Desk", system);
	private static final MechanicStaff mechanic = new MechanicStaff("M01", "Mechanic", system);
	private static final TechnicianStaff technician = new TechnicianStaff("T01", "Technician", system);

	public static void main(String[] args) {
		System.out.println("=== Welcome to CMR Management System ===");

		while (true) {
			System.out.println("\nChoose your role (or 0 to exit):");
			System.out.println(" 1) Front Desk Staff");
			System.out.println(" 2) Mechanic Staff");
			System.out.println(" 3) Technician Staff");
			System.out.println(" 0) Exit");
			String choice = promptNonEmptyString("Enter choice:");

			switch (choice) {
			case "1":
				runFrontDeskMenu();
				break;
			case "2":
				runMechanicMenu();
				break;
			case "3":
				runTechnicianMenu();
				break;
			case "0":
				System.out.println("Goodbye!");
				return;
			default:
				System.out.println("Invalid option. Please enter 0, 1, 2, or 3.");
			}
		}
	}

	// --------------- Front Desk Menu ---------------
	private static void runFrontDeskMenu() {
		while (true) {
			System.out.println("\n--- Front Desk Staff ---");
			System.out.println(" 1) Register Customer + Car");
			System.out.println(" 2) Schedule Appointment");
			System.out.println(" 3) Print Bill for Appointment");
			System.out.println(" 4) List Today's Appointments");
			System.out.println(" 0) Back to Role Selection");

			String opt = promptNonEmptyString("Enter choice:");
			if (opt.equals("0")) {
				return;
			}
			switch (opt) {
			case "1":
				registerCustomerAndCar();
				break;
			case "2":
				scheduleAppointmentFlow();
				break;
			case "3":
				printBillFlow();
				break;
			case "4":
				listDailyAppointmentsFlow();
				break;
			default:
				System.out.println("Invalid choice. Please enter 0–4.");
			}
		}
	}

	private static void registerCustomerAndCar() {
		String name = promptNonEmptyString(" Full Name:");
		String address = promptNonEmptyString(" Address:");
		String nid = promptNonEmptyString(" National ID:");
		String contact = promptNonEmptyString(" Contact Number:");

		String category;
		while (true) {
			category = promptNonEmptyString(" Category (private/fleet/staff):").toLowerCase();
			if (category.equals("private") || category.equals("fleet") || category.equals("staff")) {
				break;
			}
			System.out.println("  → Invalid category. Please enter exactly “private”, “fleet”, or “staff”.");
		}

		try {
			frontDesk.registerCustomer(name, address, nid, contact, category);
		} catch (IllegalArgumentException ex) {
			System.out.println(" Error: " + ex.getMessage());
			return;
		}

		// Now ask how many cars
		int count = promptPositiveInt(" How many cars for this customer?");
		Customer cust = system.getCustomerById(nid);
		for (int i = 1; i <= count; i++) {
			System.out.println("  Car " + i + " details:");
			String make = promptNonEmptyString("   Make:");
			String model = promptNonEmptyString("   Model:");
			String plate = promptNonEmptyString("   Plate Number:");
			Car car = new Car(make, model, plate, cust);
			cust.registerCar(car);
		}
		String pmChoice = promptNonEmptyString(" Preferred payment method (cash/credit):").toLowerCase();
		PaymentMethod pm;
		if (pmChoice.equals("credit")) {
			pm = new CreditCardPayment();
		} else {
			pm = new CashPayment();
		}
		cust.setPreferredPaymentMethod(pm);

		System.out.println(" Customer, cars, and payment preference registered successfully.");
	}

	private static void scheduleAppointmentFlow() {
		String nid = promptNonEmptyString(" Customer National ID:");
		Customer cust = system.getCustomerById(nid);
		if (cust == null) {
			System.out.println("  → Customer not found. Please register first.");
			return;
		}
		if (cust.getCars().isEmpty()) {
			System.out.println("  → This customer has no registered cars. Please register a car first.");
			return;
		}

		System.out.println("  Cars for this customer:");
		for (Car c : cust.getCars()) {
			System.out.println("   • " + c.getPlateNumber() + " (" + c.getMakeModel() + ")");
		}
		String plate = promptNonEmptyString(" Enter Plate Number:");

		LocalDateTime dt = promptDateTime(" Appointment (yyyy-MM-dd HH:mm):");

		frontDesk.scheduleAppointment(nid, plate, dt);
	}

	private static void printBillFlow() {
		String nid = promptNonEmptyString(" Customer National ID:");
		LocalDateTime dt = promptDateTime(" Appointment time (yyyy-MM-dd HH:mm):");
		frontDesk.generateAppointmentBill(nid, dt);
	}

	private static void listDailyAppointmentsFlow() {
		LocalDate date = promptDate(" Date (yyyy-MM-dd):");
		frontDesk.printDailyAppointments(date);
	}

	// --------------- Mechanic Menu ---------------
	private static void runMechanicMenu() {
		while (true) {
			System.out.println("\n--- Mechanic Staff ---");
			System.out.println(" 1) View Appointments for a Date");
			System.out.println(" 2) Add Service Report");
			System.out.println(" 0) Back to Role Selection");

			String opt = promptNonEmptyString("Enter choice:");
			if (opt.equals("0")) {
				return;
			}
			switch (opt) {
			case "1":
				viewMechanicAppointments();
				break;
			case "2":
				addServiceReportFlow();
				break;
			default:
				System.out.println("Invalid choice. Please enter 0, 1, or 2.");
			}
		}
	}

	private static void viewMechanicAppointments() {
		LocalDate date = promptDate(" Date (yyyy-MM-dd):");
		mechanic.viewScheduledAppointments(date);
	}

	private static void addServiceReportFlow() {
		String nid = promptNonEmptyString(" Customer National ID:");
		LocalDate date = promptDate(" Appointment Date (yyyy-MM-dd):");

		int rCount = promptPositiveInt(" How many repairs?");
		String[] repairs = new String[rCount];
		for (int i = 0; i < rCount; i++) {
			repairs[i] = promptNonEmptyString("  Repair " + (i + 1) + ":");
		}

		int pCount = promptPositiveInt(" How many parts used?");
		String[] parts = new String[pCount];
		for (int i = 0; i < pCount; i++) {
			parts[i] = promptNonEmptyString("  Part " + (i + 1) + ":");
		}

		double cost = promptPositiveDouble(" Total parts cost:");

		mechanic.addServiceReport(nid, date, repairs, parts, cost);
	}

	// --------------- Technician Menu ---------------
	private static void runTechnicianMenu() {
		while (true) {
			System.out.println("\n--- Technician Staff ---");
			System.out.println(" 1) Add Diagnostic Report");
			System.out.println(" 2) Generate Daily Report");
			System.out.println(" 0) Back to Role Selection");

			String opt = promptNonEmptyString("Enter choice:");
			if (opt.equals("0")) {
				return;
			}
			switch (opt) {
			case "1":
				addDiagnosticReportFlow();
				break;
			case "2":
				generateDailyReportFlow();
				break;
			default:
				System.out.println("Invalid choice. Please enter 0, 1, or 2.");
			}
		}
	}

	private static void addDiagnosticReportFlow() {
		String nid = promptNonEmptyString(" Customer National ID:");
		LocalDate date = promptDate(" Appointment Date (yyyy-MM-dd):");

		int iCount = promptPositiveInt(" How many issues found?");
		String[] issues = new String[iCount];
		for (int i = 0; i < iCount; i++) {
			issues[i] = promptNonEmptyString("  Issue " + (i + 1) + ":");
		}

		int aCount = promptPositiveInt(" How many recommended actions?");
		String[] recs = new String[aCount];
		for (int i = 0; i < aCount; i++) {
			recs[i] = promptNonEmptyString("  Action " + (i + 1) + ":");
		}

		double fee = promptPositiveDouble(" Diagnostic fee:");

		technician.addDiagnosticReport(nid, date, issues, recs, fee);
	}

	private static void generateDailyReportFlow() {
		LocalDate date = promptDate(" Date (yyyy-MM-dd):");
		technician.generateDailyReport(date);
	}

	// ============== Helper Methods (Input Validation) ==============

	/**
	 * Prompts the user with the given message until a non-empty string is entered.
	 */
	private static String promptNonEmptyString(String prompt) {
		while (true) {
			System.out.print(prompt + " ");
			String input = scanner.nextLine().trim();
			if (!input.isEmpty()) {
				return input;
			}
			System.out.println("  → Input cannot be empty. Please try again.");
		}
	}

	/**
	 * Prompts until user enters an integer ≥ 1.
	 */
	private static int promptPositiveInt(String prompt) {
		while (true) {
			System.out.print(prompt + " ");
			String line = scanner.nextLine().trim();
			try {
				int value = Integer.parseInt(line);
				if (value >= 1) {
					return value;
				}
				System.out.println("  → Please enter a positive integer (≥ 1).");
			} catch (NumberFormatException e) {
				System.out.println("  → Invalid number. Please enter a positive integer.");
			}
		}
	}

	/**
	 * Prompts until user enters a double ≥ 0.
	 */
	private static double promptPositiveDouble(String prompt) {
		while (true) {
			System.out.print(prompt + " ");
			String line = scanner.nextLine().trim();
			try {
				double value = Double.parseDouble(line);
				if (value >= 0) {
					return value;
				}
				System.out.println("  → Please enter a non-negative number.");
			} catch (NumberFormatException e) {
				System.out.println("  → Invalid number. Please enter a valid decimal.");
			}
		}
	}

	/**
	 * Prompts until user enters a date of the form yyyy-MM-dd.
	 */
	private static LocalDate promptDate(String prompt) {
		DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		while (true) {
			System.out.print(prompt + " ");
			String line = scanner.nextLine().trim();
			try {
				return LocalDate.parse(line, dateFmt);
			} catch (DateTimeParseException e) {
				System.out.println("  → Invalid date format. Use yyyy-MM-dd (e.g. 2025-06-06).");
			}
		}
	}

	/**
	 * Prompts until user enters a date-time of the form yyyy-MM-dd HH:mm.
	 */
	private static LocalDateTime promptDateTime(String prompt) {
		DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		while (true) {
			System.out.print(prompt + " ");
			String line = scanner.nextLine().trim();
			try {
				return LocalDateTime.parse(line, dtFmt);
			} catch (DateTimeParseException e) {
				System.out.println("  → Invalid format. Use yyyy-MM-dd HH:mm (e.g. 2025-06-06 10:00).");
			}
		}
	}
}
