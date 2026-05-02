public class ConcreteCustomerFactory implements CustomerFactory {

	@Override
	public Customer createCustomer(String fullName, String address, String nationalId, String contactNumber,
			String category) {
		switch (category.toLowerCase()) {
		case "private":
			return new PrivateCustomer(fullName, address, nationalId, contactNumber);
		case "fleet":
			return new FleetCustomer(fullName, address, nationalId, contactNumber);
		case "staff":
			return new StaffCustomer(fullName, address, nationalId, contactNumber);
		default:
			throw new IllegalArgumentException("Unknown category: " + category);
		}
	}
}
