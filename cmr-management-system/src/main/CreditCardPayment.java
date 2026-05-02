public class CreditCardPayment implements PaymentMethod {
	@Override
	public boolean pay(double amount) {
		// In a real system, integrate with credit card gateway here
		System.out.println("Processing credit card payment of " + amount);
		return true; // assume always successful

	}

	@Override
	public String toString() {
		return "Credit Card";
	}
}
