public class Payment {
    private PaymentMethod paymentMethod;

    public Payment(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean processPayment(double amount) {
        return paymentMethod.pay(amount);
    }
}
