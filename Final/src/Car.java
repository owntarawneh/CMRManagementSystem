public class Car {
    private String make;
    private String model;
    private String plateNumber;
    private Customer owner;

    public Car(String make, String model, String plateNumber, Customer owner) {
        this.make = make;
        this.model = model;
        this.plateNumber = plateNumber;
        this.owner = owner;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getMakeModel() {
        return make + " " + model;
    }

    public Customer getOwner() {
        return owner;
    }
}
