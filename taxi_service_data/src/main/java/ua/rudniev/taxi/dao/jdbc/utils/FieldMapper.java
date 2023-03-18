package ua.rudniev.taxi.dao.jdbc.utils;

import ua.rudniev.taxi.dao.common.field.Field;

/**
 * Interface that has method for simplification of building sql queries
 */
public interface FieldMapper <T extends Field> {
    /**
     * This method returns name of a field from database
     * @param field indicates which field is needed
     * @return string name of a field
     */
    String mapToSqlField(T field);
}
