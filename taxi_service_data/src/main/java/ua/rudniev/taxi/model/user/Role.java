package ua.rudniev.taxi.model.user;

/**
 * This enum has fields and methods that are indicating roles of a user
 */
public enum Role {
    USER("User"),
    DRIVER("Driver"),
    ADMIN("Admin");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Role getRoleByValue (String value) {
        if (value == null || value.isEmpty()) return null;
        return Role.valueOf(value.toUpperCase());
    }
}
