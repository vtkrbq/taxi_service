package ua.rudniev.taxi.model.trip;

public class AddressPoint extends Point {
    private final String address;

    public AddressPoint(double x, double y, String address) {
        super(x, y);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
