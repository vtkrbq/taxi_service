package ua.rudniev.taxi.dao.common.order;

import ua.rudniev.taxi.dao.common.field.Field;

/**
 * This class has fields and methods that helps building sql queries
 *
 * @param <T>
 */
public class OrderBy<T extends Field> {
    private final T field;

    private final OrderByType orderByType;

    public OrderBy(T field, OrderByType orderByType) {
        this.field = field;
        this.orderByType = orderByType;
    }

    public T getField() {
        return field;
    }

    public OrderByType getOrderByType() {
        return orderByType;
    }
}
