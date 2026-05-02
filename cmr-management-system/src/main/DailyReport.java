import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DailyReport {
	private LocalDate reportDate;
	private List<DailyReportEntry> entries;

	public DailyReport(LocalDate reportDate) {
		this.reportDate = reportDate;
		this.entries = new ArrayList<>();
	}

	public void addEntry(DailyReportEntry entry) {
		entries.add(entry);
	}

	public String getFormattedText() {
		StringBuilder sb = new StringBuilder();
		sb.append("=== Daily Report for ").append(reportDate).append(" ===\n\n");
		if (entries.isEmpty()) {
			sb.append("No entries.\n");
		} else {
			for (DailyReportEntry e : entries) {
				sb.append(e.getEntryLine()).append("\n");
			}
		}
		return sb.toString();
	}
}
