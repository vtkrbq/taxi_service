package ua.rudniev.taxi.service.price;

import ua.rudniev.taxi.model.NewTripInfo;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.user.User;

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

    @Override
    public BigDecimal calculateDiscountVolume(User user, NewTripInfo newTripInfo) {
        return newTripInfo.getPriceWithoutDiscount()
                .multiply(BigDecimal.valueOf(user.getDiscount()))
                .divide(BigDecimal.valueOf(100), RoundingMode.DOWN);
    }
}
