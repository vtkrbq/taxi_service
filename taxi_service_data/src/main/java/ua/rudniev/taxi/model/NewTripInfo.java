package ua.rudniev.taxi.model;

import ua.rudniev.taxi.model.car.Car;

import java.math.BigDecimal;

public class NewTripInfo {
    private final Car car;

    private final BigDecimal price;

    public NewTripInfo(Car car, BigDecimal price) {
        this.car = car;
        this.price = price;
    }

    public Car getCar() {
        return car;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
