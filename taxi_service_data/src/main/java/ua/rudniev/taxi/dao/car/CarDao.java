package ua.rudniev.taxi.dao.car;

import ua.rudniev.taxi.dao.common.filter.Filter;
import ua.rudniev.taxi.model.car.Car;

import java.util.List;
import java.util.Optional;

/**
 * Interface class that has methods for working with repositories
 */
public interface CarDao {
    /**
     * This method searching all cars in repository with filters
     *
     * @param filters This parameter filters cars in repository
     * @return List of objects Car that are fit to filters parameter from database
     */
    List<Car> findCars(List<Filter<CarField>> filters);

    /**
     * This method searching for car in repository with indicated id
     *
     * @param id This parameter is indicating which car will be returned
     * @return Optional of Car with indicated id
     */
    Optional<Car> findCarById(int id);

    /**
     * This method updates car in repository
     *
     * @param car This parameter contains data about updated car
     */
    void update(Car car);

    /**
     * This method creates car in repository
     *
     * @param car This parameter contains data about created car
     */
    void createCar(Car car);
}
