import java.time.LocalDate;
import java.util.List;

public class TechnicianStaff extends Staff {
	private CMRManagementSystem system;

	public TechnicianStaff(String staffId, String fullName, CMRManagementSystem system) {
		super(staffId, fullName, "Technician");
		this.system = system;
	}

	public void addDiagnosticReport(String nationalId, LocalDate date, String[] issues, String[] recommendations,
			double diagnosticFee) {
		boolean success = system.addDiagnosticReport(nationalId, date, issues, recommendations, diagnosticFee);
		if (success) {
			System.out.println("Diagnostic report added successfully.");
		} else {
			System.out.println("Failed to add diagnostic report (appointment not found).");
		}
	}

	public void generateDailyReport(LocalDate date) {
		DailyReport report = system.generateDailyReport(date);
		if (report != null) {
			System.out.println("Daily Report for " + date + ":\n");
			System.out.println(report.getFormattedText());
			System.out.println("✅ Saved to daily_report.txt");
		} else {
			System.out.println("No serviced appointments found for " + date);
		}
	}

	public void showResponsibilities() {
		System.out.println("As Technician Staff, you can:");
		System.out.println("  1) Add diagnostic report to an appointment");
		System.out.println("  2) Generate daily report");
		System.out.println("  0) Logout");
	}
}
