import java.util.List;
import java.util.UUID;

public class ServiceReport {
    private UUID reportId;
    private List<String> repairsPerformed;
    private List<String> partsUsed;
    private double partsCost;

    public ServiceReport(Appointment appointment, List<String> repairs, List<String> parts, double partsCost) {
        this.reportId = UUID.randomUUID();
        this.repairsPerformed = repairs;
        this.partsUsed = parts;
        this.partsCost = partsCost;
        appointment.addServiceReport(this);
    }

    public String getSummary() {
        return "ServiceReport ID: " + reportId +
               ", Repairs: " + repairsPerformed +
               ", Parts Used: " + partsUsed +
               ", Parts Cost: " + partsCost;
    }

    public double getPartsCost() {
        return partsCost;
    }
}
