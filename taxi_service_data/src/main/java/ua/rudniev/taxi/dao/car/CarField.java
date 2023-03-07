package ua.rudniev.taxi.dao.car;

import ua.rudniev.taxi.dao.common.field.Field;
import ua.rudniev.taxi.dao.common.field.FieldType;

public enum CarField implements Field {
    NAME(FieldType.STRING),
    CATEGORY(FieldType.STRING),
    CAPACITY(FieldType.INTEGER),
    LICENSE_PLATE(FieldType.STRING),
    DRIVER_LOGIN(FieldType.STRING),
    STATUS(FieldType.STRING),
    CURRENT_ADDRESS(FieldType.STRING),
    CURRENT_POINT_X(FieldType.DOUBLE),
    CURRENT_POINT_Y(FieldType.DOUBLE),
    DISCOUNT(FieldType.INTEGER);
    private final FieldType fieldType;

    CarField(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public FieldType getFieldType() {
        return fieldType;
    }
}
