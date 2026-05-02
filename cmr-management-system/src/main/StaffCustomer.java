public class StaffCustomer extends Customer {

    public StaffCustomer(String fullName, String address, String nationalId, String contactNumber) {
        super(fullName, address, nationalId, contactNumber);
    }

    @Override
    public double getDiscountRate() {
        return 0.50;
    }
}
