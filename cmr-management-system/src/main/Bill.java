import java.util.UUID;

public class Bill {
    private UUID billId;
    private double baseServiceFee = 50.0;
    private double partsTotal = 0.0;
    private double diagnosticTotal = 0.0;
    private double discount = 0.0;
    private Payment payment;
    private Appointment appointment;

    public Bill(Appointment appointment) {
        this.billId = UUID.randomUUID();
        this.appointment = appointment;

        // Gather parts cost and diagnostic fee from reports if available
        ServiceReport sr = appointment.getServiceReport();
        if (sr != null) {
            this.partsTotal = sr.getPartsCost();
        }

        DiagnosticReport dr = appointment.getDiagnosticReport();
        if (dr != null) {
            this.diagnosticTotal = dr.getDiagnosticFee();
        }

        // Calculate discount based on customer type
        Customer c = appointment.getCustomer();
        double totalBeforeDiscount = baseServiceFee + partsTotal + diagnosticTotal;
        this.discount = totalBeforeDiscount * c.getDiscountRate();
    }

    public double calculateTotalAmount() {
        double total = baseServiceFee + partsTotal + diagnosticTotal - discount;
        return total;
    }

    public String getItemizedBreakdown() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bill ID: ").append(billId).append("\n");
        sb.append("Base Service Fee: ").append(baseServiceFee).append("\n");
        sb.append("Parts Total: ").append(partsTotal).append("\n");
        sb.append("Diagnostic Total: ").append(diagnosticTotal).append("\n");
        sb.append("Discount: ").append(discount).append("\n");
        sb.append("Total Amount Due: ").append(calculateTotalAmount()).append("\n");
        return sb.toString();
    }

    public boolean payBill() {
        // For simplicity, choose a payment method at runtime
        // In a real system you'd ask the user which method, here we default to cash
        if (payment == null) {
            payment = new Payment(new CashPayment());
        }
        return payment.processPayment(calculateTotalAmount());
    }
}
