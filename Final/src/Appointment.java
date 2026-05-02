import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Appointment {
	private UUID appointmentId;
	private LocalDateTime startTime;
	private int durationInHours = 1;
	private Customer customer;
	private Car car;
	private ServiceReport serviceReport;
	private DiagnosticReport diagnosticReport;
	private Bill bill;

	public Appointment(Customer customer, Car car, LocalDateTime startTime) {
		this.appointmentId = UUID.randomUUID();
		this.customer = customer;
		this.car = car;
		this.startTime = startTime;
	}

	public UUID getAppointmentId() {
		return appointmentId;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public LocalDate getDate() {
		return startTime.toLocalDate();
	}

	public Customer getCustomer() {
		return customer;
	}

	public Car getCar() {
		return car;
	}

	public ServiceReport getServiceReport() {
		return serviceReport;
	}

	public DiagnosticReport getDiagnosticReport() {
		return diagnosticReport;
	}

	public Bill getBill() {
		return bill;
	}

	public boolean isServiced() {
		return this.serviceReport != null;
	}

	/** Always returns 50.0 as base service fee */
	public double calculateBaseFee() {
		return 50.0;
	}

	public void addServiceReport(ServiceReport report) {
		this.serviceReport = report;
	}

	public void addDiagnosticReport(DiagnosticReport report) {
		this.diagnosticReport = report;
	}

	/** Generate a Bill instance, linking in any existing reports */
	public Bill generateBill() {
		this.bill = new Bill(this);
		return this.bill;
	}

	public double calculateFinalFee() {
		if (bill == null) {
			generateBill();
		}
		return bill.calculateTotalAmount();
	}

	public String getAppointmentDetails() {
		StringBuilder sb = new StringBuilder();
		sb.append("Appointment ID: ").append(appointmentId).append("\n");
		sb.append("Date/Time: ").append(startTime).append("\n");
		sb.append("Customer: ").append(customer.getFullName()).append(" (").append(customer.getNationalId())
				.append(")\n");
		sb.append("Car: ").append(car.getMakeModel()).append(" [Plate: ").append(car.getPlateNumber()).append("]\n");
		sb.append("preferredPaymentMethod: ").append(customer.getPreferredPaymentMethod()).append("\n");
		if (serviceReport != null) {
			sb.append("Service Report: ").append(serviceReport.getSummary()).append("\n");
		}
		if (diagnosticReport != null) {
			sb.append("Diagnostic Report: ").append(diagnosticReport.getSummary()).append("\n");
		}
		if (bill != null) {
			sb.append("Bill Total: ").append(bill.calculateTotalAmount()).append("\n");
		}
		return sb.toString();
	}
}
