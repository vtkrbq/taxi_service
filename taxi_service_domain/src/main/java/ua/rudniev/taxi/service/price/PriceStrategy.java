package ua.rudniev.taxi.service.price;


import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.trip.Point;

import java.math.BigDecimal;

/**
 * This interface has method that will calculate price of trip order
 */
public interface PriceStrategy {
    /**
     * This method calculate price
     * @param distance Given distance
     * @param car Given car
     * @return BigDecimal price of trip
     */
    BigDecimal calculatePrice(double distance, Car car);
}
