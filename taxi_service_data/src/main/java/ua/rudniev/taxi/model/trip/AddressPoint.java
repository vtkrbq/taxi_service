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

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AddressPoint)) {
            return false;
        }
        AddressPoint c = (AddressPoint) o;
        return Double.compare(c.getX(), ((AddressPoint) o).getX()) == 0
                && Double.compare(c.getY(), ((AddressPoint) o).getY()) == 0 && c.getAddress().equals(((AddressPoint) o).getAddress());
    }
}
