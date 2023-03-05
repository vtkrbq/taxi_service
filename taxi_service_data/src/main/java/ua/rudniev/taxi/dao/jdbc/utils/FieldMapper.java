package ua.rudniev.taxi.dao.jdbc.utils;

import ua.rudniev.taxi.dao.common.field.Field;

public interface FieldMapper <T extends Field> {

    String mapToSqlField(T field);
}
