package ua.rudniev.taxi.model.trip;

import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.user.User;

import java.math.BigDecimal;
import java.time.Instant;

public class TripOrder {
    private AddressPoint departure;
    private AddressPoint destination;
    private Category category;
    private int capacity;
    private User user;
    private Instant timestamp;

    private Car car;

    private BigDecimal price;

    public TripOrder() {
    }

    public TripOrder(AddressPoint departure, AddressPoint destination, Category category, int capacity, User user, Instant timestamp) {
        this.departure = departure;
        this.destination = destination;
        this.category = category;
        this.capacity = capacity;
        this.user = user;
        this.timestamp = timestamp;
    }

    public AddressPoint getDeparture() {
        return departure;
    }

    public void setDeparture(AddressPoint departure) {
        this.departure = departure;
    }

    public AddressPoint getDestination() {
        return destination;
    }

    public void setDestination(AddressPoint destination) {
        this.destination = destination;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
