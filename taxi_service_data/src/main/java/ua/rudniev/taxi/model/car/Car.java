package ua.rudniev.taxi.model.car;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.rudniev.taxi.model.trip.AddressPoint;
import ua.rudniev.taxi.model.user.User;

@Data
@NoArgsConstructor
public class Car {
    private int id;
    private User driver;
    private String carName;
    private Category carCategory;
    private Status status;
    private int carCapacity;
    private String licensePlate;
    private AddressPoint currentAddress;

    public Car(User driver, String carName, Category carCategory, Status status, int carCapacity, String licensePlate) {
        this.driver = driver;
        this.carName = carName;
        this.carCategory = carCategory;
        this.status = status;
        this.carCapacity = carCapacity;
        this.licensePlate = licensePlate;
        this.currentAddress = new AddressPoint(
                49.991981,
                36.328126,
                "Харків, вул. Гвардійців-Широнінців, 5");
    }
    public Car(int id, User driver, String carName, Category carCategory, int carCapacity, String licensePlate) {
        this.id = id;
        this.driver = driver;
        this.carName = carName;
        this.carCategory = carCategory;
        this.carCapacity = carCapacity;
        this.licensePlate = licensePlate;
        this.currentAddress = new AddressPoint(
                49.991981,
                36.328126,
                "Харків, вул. Гвардійців-Широнінців, 5");
    }
}
