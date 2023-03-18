package ua.rudniev.taxi.service.price;

import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.trip.Point;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class is implementation of PriceStrategy interface
 */
public class PythagorasPriceStrategy implements PriceStrategy {

    public static final double PRICE_FACTOR = 13;

    @Override
    public BigDecimal calculatePrice(double distance, Car car) {
        double price = distance * PRICE_FACTOR * car.getCarCategory().getFactor();
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.DOWN);
    }
}
