public class PrivateCustomer extends Customer {

    public PrivateCustomer(String fullName, String address, String nationalId, String contactNumber) {
        super(fullName, address, nationalId, contactNumber);
    }

    @Override
    public double getDiscountRate() {
        return 0.0;
    }
}
