package ua.rudniev.taxi.dao.trip;

public enum TableFields {
    DEPARTURE_ADDRESS ("departure_address"),
    DESTINATION_ADDRESS ("destination_address"),
    CATEGORY ("trip_category"),
    CAPACITY ("trip_capacity"),
    USER_NAME ("client.lastname"),
    CAR_NAME ("car.car_name"),
    PRICE ("trip_order.price"),
    CREATED ("trip_order.created"),
    DRIVER_LOGIN ("car.driver_login"),
    USER_LOGIN ("trip_order.user_login");

    private final String fieldName;
    TableFields(String fieldName) {
        this.fieldName = fieldName;
    }
    public String getValue() {
        return fieldName;
    }
}
