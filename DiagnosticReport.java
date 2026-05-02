import java.util.List;
import java.util.UUID;

public class DiagnosticReport {
    private UUID reportId;
    private List<String> issuesFound;
    private List<String> recommendedActions;
    private double diagnosticFee;

    public DiagnosticReport(Appointment appointment, List<String> issues, List<String> recommendations, double diagnosticFee) {
        this.reportId = UUID.randomUUID();
        this.issuesFound = issues;
        this.recommendedActions = recommendations;
        this.diagnosticFee = diagnosticFee;
        appointment.addDiagnosticReport(this);
    }

    public String getSummary() {
        return "DiagnosticReport ID: " + reportId +
               ", Issues: " + issuesFound +
               ", Recommendations: " + recommendedActions +
               ", Diagnostic Fee: " + diagnosticFee;
    }

    public double getDiagnosticFee() {
        return diagnosticFee;
    }
}
