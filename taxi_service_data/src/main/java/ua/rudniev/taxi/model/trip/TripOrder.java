package ua.rudniev.taxi.model.trip;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.user.User;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
public class TripOrder {
    private AddressPoint departure;
    private AddressPoint destination;
    private Category category;
    private int capacity;
    private User user;
    private Instant timestamp;

    private Car car;

    private BigDecimal price;

    public TripOrder(AddressPoint departure, AddressPoint destination, Category category, int capacity, User user, Instant timestamp) {
        this.departure = departure;
        this.destination = destination;
        this.category = category;
        this.capacity = capacity;
        this.user = user;
        this.timestamp = timestamp;
    }

    public TripOrder(AddressPoint departure,
                     AddressPoint destination,
                     Category category,
                     int capacity,
                     User user,
                     Car car,
                     BigDecimal price,
                     Instant timestamp) {
        this.departure = departure;
        this.destination = destination;
        this.category = category;
        this.capacity = capacity;
        this.user = user;
        this.car = car;
        this.price = price;
        this.timestamp = timestamp;
    }
}
