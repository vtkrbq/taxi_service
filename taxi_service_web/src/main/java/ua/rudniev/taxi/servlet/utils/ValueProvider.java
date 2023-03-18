package ua.rudniev.taxi.servlet.utils;

import ua.rudniev.taxi.dao.common.field.Field;
import ua.rudniev.taxi.dao.common.field.FieldType;
import ua.rudniev.taxi.dao.common.filter.Value;

import java.math.BigDecimal;
import java.time.Instant;

import static ua.rudniev.taxi.dao.common.field.FieldType.*;
import static ua.rudniev.taxi.dao.common.field.FieldType.INSTANT;

/**
 * This class has method that implementing wrapping of simple type objects
 */
public class ValueProvider {

    public static Value getValueByField(String value, Field field) {
        FieldType fieldType = field.getFieldType();
        if(fieldType == INTEGER) {
            return new Value(Integer.parseInt(value));
        }
        if(fieldType == STRING) {
            return new Value(value);
        }
        if(fieldType == BIG_DECIMAL) {
            return new Value(new BigDecimal(value));
        }
        if(fieldType == INSTANT) {
            return new Value(Instant.parse(value + "T00:00:00Z"));
        }
        if(fieldType == DOUBLE) {
            return new Value(Double.parseDouble(value));
        }
        throw new IllegalArgumentException("Unknown fieldType: " + fieldType);
    }
}
