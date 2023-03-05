package ua.rudniev.taxi.dao.common.filter;

import java.math.BigDecimal;
import java.time.Instant;

public class Value {

    private String string;

    private int integer;

    private Instant instant;

    private BigDecimal bigDecimal;

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

    public String getString() {
        return string;
    }

    public int getInteger() {
        return integer;
    }

    public Instant getInstant() {
        return instant;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }
}
