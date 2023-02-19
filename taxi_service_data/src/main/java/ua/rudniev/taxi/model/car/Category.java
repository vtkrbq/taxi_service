package ua.rudniev.taxi.model.car;

public enum Category {
    ECONOMY(0.7),
    COMFORT(1),
    BUSINESS(1.5);
    private final double factor;
    Category(double factor) {
        this.factor = factor;
    }
    public double getFactor() {
        return factor;
    }
}
