package ua.rudniev.taxi.model;

import ua.rudniev.taxi.model.car.Car;

import java.math.BigDecimal;

/**
 * This class has fields and methods that building information about trip order including car, price and estimated time arrival
 */
public class NewTripInfo {
    private final Car car;

    private final BigDecimal price;

    private final int eta; //TODO: Пирог: интуитивно не понятно что это за параметр https://dictionary.cambridge.org/dictionary/english/eta
    //В нембридже пишут что это "№"the seventh letter of the Greek alphabet"

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

    public int getEta() { //TODO: Пирог: проверь где используется
        return eta;
    }
}
