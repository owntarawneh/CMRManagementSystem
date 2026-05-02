public class CashPayment implements PaymentMethod {
	@Override
	public boolean pay(double amount) {
		System.out.println("Processing cash payment of " + amount);
		return true; // assume always successful
	}

	@Override
	public String toString() {
		return "Cash";
	}
}
