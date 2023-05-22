package ua.rudniev.taxi.service.time;

import ua.rudniev.taxi.model.trip.AddressPoint;

/**
 * This class used to calculate estimated time arrival of a car
 */
public class TimeStrategy {

    /**
     * This method calculate estimated time arrival of a car
     * @param client This parameter provides AddressPoint of user
     * @param driver This parameter provides AddressPoint of driver
     * @return int estimated time arrival in minutes
     */
    public int calculateEta(AddressPoint client, AddressPoint driver) {
        double t = ((Math.sqrt(
                Math.pow(driver.getX() - client.getX(), 2)
                        + Math.pow(driver.getY() - client.getY(), 2)
        ) * 100) / 60) * 15;
        return (int) (t * (Math.random() * (5 - 2) + 2));
    }
}
