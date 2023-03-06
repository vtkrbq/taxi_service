package ua.rudniev.taxi.model;

import ua.rudniev.taxi.model.car.Car;

import java.math.BigDecimal;

public class NewTripInfo {
    private final Car car;

    private final BigDecimal price;

    private final int eta;

    public NewTripInfo(Car car, BigDecimal price, int eta) {
        this.car = car;
        this.price = price;
        this.eta = eta;
    }

    public Car getCar() {
        return car;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getEta() {
        return eta;
    }
}
