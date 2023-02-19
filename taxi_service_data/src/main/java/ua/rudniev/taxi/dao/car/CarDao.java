package ua.rudniev.taxi.dao.car;

import ua.rudniev.taxi.model.car.Car;
import ua.rudniev.taxi.model.car.Category;
import ua.rudniev.taxi.model.user.User;

import java.util.List;

public interface CarDao {
    List<Car> findAvailableCars(int capacity, Category category);
    void update(Car car);
    void createCar(Car car, User user);
}
