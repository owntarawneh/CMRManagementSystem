import java.util.ArrayList;
import java.util.List;

public abstract class Customer {
	private String fullName;
	private String address;
	private String nationalId;
	private String contactNumber;
	private List<Car> cars;
	private PaymentMethod preferredPaymentMethod;

	public Customer(String fullName, String address, String nationalId, String contactNumber) {
		this.fullName = fullName;
		this.address = address;
		this.nationalId = nationalId;
		this.contactNumber = contactNumber;
		this.cars = new ArrayList<>();
	}

	public String getFullName() {
		return fullName;
	}

	public String getAddress() {
		return address;
	}

	public String getNationalId() {
		return nationalId;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void registerCar(Car car) {
		cars.add(car);
	}

	public List<Car> getCars() {
		return cars;
	}

	public void setPreferredPaymentMethod(PaymentMethod pm) {
		this.preferredPaymentMethod = pm;
	}

	public PaymentMethod getPreferredPaymentMethod() {
		return preferredPaymentMethod;
	}

	/** Each subclass will override this: */
	public abstract double getDiscountRate();

	public double calculateFinalFee(double baseFee) {
		return baseFee * (1.0 - getDiscountRate());
	}
}
