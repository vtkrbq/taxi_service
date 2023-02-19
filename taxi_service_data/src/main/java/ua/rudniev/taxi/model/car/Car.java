package ua.rudniev.taxi.model.car;

import ua.rudniev.taxi.model.user.User;

public class Car {
    private int id;

    private User driver;

    private String carName;

    private Category carCategory;

    private Status status;

    private int carCapacity;

    private String licensePlate;

    public Car() {

    }

    public Car(User driver, String carName, Category carCategory, Status status, int carCapacity, String licensePlate) {
        this.driver = driver;
        this.carName = carName;
        this.carCategory = carCategory;
        this.status = status;
        this.carCapacity = carCapacity;
        this.licensePlate = licensePlate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public Category getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(Category carCategory) {
        this.carCategory = carCategory;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getCarCapacity() {
        return carCapacity;
    }

    public void setCarCapacity(int carCapacity) {
        this.carCapacity = carCapacity;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
