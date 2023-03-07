package ua.rudniev.taxi.dao.common.filter;

import java.math.BigDecimal;
import java.time.Instant;

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

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }
}
