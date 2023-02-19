package ua.rudniev.taxi.service.price;


import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.trip.Point;

import java.math.BigDecimal;

public interface PriceStrategy {
    BigDecimal calculatePrice(Point departure, Point destination, Car car);
}
