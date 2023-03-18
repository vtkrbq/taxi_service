package ua.rudniev.taxi.model.trip;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.user.User;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * This class has fields and methods that describes TripOrder object
 */
@Data
@NoArgsConstructor
public class TripOrder {
    private int id;
    private AddressPoint departure;
    private AddressPoint destination;
    private Category category;
    private int capacity;
    private User user;
    private Instant timestampCreated;
    private Instant timestampEnd;

    private Car car;

    private BigDecimal price;

    public TripOrder(AddressPoint departure, AddressPoint destination, Category category, int capacity, User user, Instant timestampCreated) {
        this.departure = departure;
        this.destination = destination;
        this.category = category;
        this.capacity = capacity;
        this.user = user;
        this.timestampCreated = timestampCreated;
    }

    public TripOrder(AddressPoint departure,
                     AddressPoint destination,
                     Category category,
                     int capacity,
                     User user,
                     Car car,
                     BigDecimal price,
                     Instant timestampCreated,
                     Instant timestampEnd) {
        this.departure = departure;
        this.destination = destination;
        this.category = category;
        this.capacity = capacity;
        this.user = user;
        this.car = car;
        this.price = price;
        this.timestampCreated = timestampCreated;
        this.timestampEnd = timestampEnd;
    }

    public String toString() {
        return "[from: " + departure + " to: " + destination + " client: " + user.getLogin() + "]";
    }
}
