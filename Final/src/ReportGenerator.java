import java.time.LocalDate;
import java.util.List;

public class ReportGenerator {

	public DailyReport generateDailyReport(LocalDate date, List<Appointment> appointments) {
		DailyReport dr = new DailyReport(date);
		for (Appointment a : appointments) {
			// only include serviced appointments
			if (!a.isServiced())
				continue;

			String repairsSummary = a.getServiceReport().getSummary();
			String diagnosticSummary = (a.getDiagnosticReport() != null) ? a.getDiagnosticReport().getSummary()
					: "No diagnostic report";

			DailyReportEntry entry = new DailyReportEntry(a.getCustomer().getNationalId(), a.getCar().getPlateNumber(),
					repairsSummary, diagnosticSummary, a.getStartTime());
			dr.addEntry(entry);
		}
		return dr;
	}
}
