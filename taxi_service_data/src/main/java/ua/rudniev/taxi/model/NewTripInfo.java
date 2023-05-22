package ua.rudniev.taxi.model;

import ua.rudniev.taxi.model.car.Car;

import java.math.BigDecimal;

/**
 * This class has fields and methods that building information about trip order including car, price and estimated time arrival
 */
public class NewTripInfo {
    private final Car car;

    private final BigDecimal priceWithoutDiscount;

    private BigDecimal priceWithDiscount;

    /**
    * eta - Estimated time arrival
     */
    private final int eta;

    public NewTripInfo(Car car, BigDecimal priceWithoutDiscount, int eta) {
        this.car = car;
        this.priceWithoutDiscount = priceWithoutDiscount;
        this.eta = eta;
    }

    public Car getCar() {
        return car;
    }

    public int getEta() {
        return eta;
    }

    public BigDecimal getPriceWithoutDiscount() {
        return priceWithoutDiscount;
    }

    public BigDecimal getPriceWithDiscount() {
        return priceWithDiscount;
    }

    public void setPriceWithDiscount(BigDecimal priceWithDiscount) {
        this.priceWithDiscount = priceWithDiscount;
    }
}
