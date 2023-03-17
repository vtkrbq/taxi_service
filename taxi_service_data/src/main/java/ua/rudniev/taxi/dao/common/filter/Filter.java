package ua.rudniev.taxi.dao.common.filter;

import ua.rudniev.taxi.dao.common.field.Field;

/**
 * This class has fields and methods that helps building sql queries
 *
 * @param <T>
 */
public class Filter<T extends Field> {

    private final T field;

    private final Value value;

    private final FilterType filterType;

    public Filter(T field, Value value) {
        this.field = field;
        this.value = value;
        this.filterType = FilterType.EQUALS;
    }

    public Filter(T field, Value value, FilterType filterType) {
        this.field = field;
        this.value = value;
        this.filterType = filterType;
    }

    public T getField() {
        return field;
    }

    public Value getValue() {
        return value;
    }

    public FilterType getFilterType() {
        return filterType;
    }
}
