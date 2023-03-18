package ua.rudniev.taxi.model.trip;

/**
 * This class has fields and methods that contains String address and two objects of a class Point (x, y) - coordinates of that address
 */
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
