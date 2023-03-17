package ua.rudniev.taxi.dao.common.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * This class has fields and methods that helps building sql queries
 */
@Data
public class Value {

    private String string;

    private int integer;

    private Instant instant;

    private BigDecimal bigDecimal;

    private Double doubleValue;

    public Value(String string) {
        this.string = string;
    }

    public Value(int integer) {
        this.integer = integer;
    }

    public Value(Instant instant) {
        this.instant = instant;
    }

    public Value(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public Value(Double doubleValue) {
        this.doubleValue = doubleValue;
    }
}
