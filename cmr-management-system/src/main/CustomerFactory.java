public interface CustomerFactory {
    Customer createCustomer(String fullName, String address, String nationalId, String contactNumber, String category);
}
