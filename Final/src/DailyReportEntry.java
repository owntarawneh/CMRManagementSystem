import java.time.LocalDateTime;

public class DailyReportEntry {
    private String customerNationalId;
    private String carPlateNumber;
    private String repairsSummary;
    private String diagnosticSummary;
    private LocalDateTime appointmentTime;

    public DailyReportEntry(String customerNationalId, String carPlateNumber,
                            String repairsSummary, String diagnosticSummary,
                            LocalDateTime appointmentTime) {
        this.customerNationalId = customerNationalId;
        this.carPlateNumber = carPlateNumber;
        this.repairsSummary = repairsSummary;
        this.diagnosticSummary = diagnosticSummary;
        this.appointmentTime = appointmentTime;
    }

    public String getEntryLine() {
        return "Time: " + appointmentTime +
               " | Customer ID: " + customerNationalId +
               " | Car Plate: " + carPlateNumber +
               " | Repairs: " + repairsSummary +
               " | Diagnostics: " + diagnosticSummary;
    }
}
