package ua.rudniev.taxi.dao.common.filter;

/**
 * Enum class that has fields for simplification of work with filter types when building sql queries
 */
public enum FilterType {
    EQUALS,
    NOT_EQUALS,
    MORE,
    LESS,
    LESS_OR_EQUALS,
    MORE_OR_EQUALS
}
