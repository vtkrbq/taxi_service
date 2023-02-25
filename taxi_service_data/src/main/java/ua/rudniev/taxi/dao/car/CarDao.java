package ua.rudniev.taxi.dao.car;

import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.user.User;

import java.util.List;
import java.util.Optional;

public interface CarDao {
    List<Car> findAvailableCars(int capacity, Category category);
    Optional<Car> findCarById(int id);
    void update(Car car);
    void createCar(Car car, User user);
}
