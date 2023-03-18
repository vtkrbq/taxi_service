package ua.rudniev.taxi.dao.trip;

import ua.rudniev.taxi.dao.common.field.Field;
import ua.rudniev.taxi.dao.common.field.FieldType;

/**
 * Enum class that has fields and methods for simplification of work with repository
 */
public enum TripOrderField implements Field {
    DEPARTURE_ADDRESS(FieldType.STRING),
    DESTINATION_ADDRESS(FieldType.STRING),
    CATEGORY(FieldType.STRING),
    CAPACITY(FieldType.INTEGER),
    CLIENT_LAST_NAME(FieldType.STRING),
    CLIENT_LOGIN(FieldType.STRING),
    CAR_NAME(FieldType.STRING),
    PRICE(FieldType.BIG_DECIMAL),
    CREATED(FieldType.INSTANT),
    DRIVER_LOGIN(FieldType.STRING);
    private final FieldType fieldType;

    TripOrderField(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public FieldType getFieldType() {
        return fieldType;
    }
}
