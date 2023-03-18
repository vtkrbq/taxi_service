package ua.rudniev.taxi.servlet.validation;

import ua.rudniev.taxi.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class has fields and methods that provides validation of data entered by user
 */
public class ValidationUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(.+)$");

    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+\\d+$");

    public static void validatePhone(List<String> errors, String value) {
        ValidationUtils.validateMandatory(errors, value, "phone");
        if (!StringUtils.isEmptyOrNull(value)) {
            Matcher matcher = PHONE_PATTERN.matcher(value);
            if (!matcher.matches()) {
                errors.add("Phone should follow the format +xxxxxxxxxxxx");
            }
        }
    }

    public static void validateEmail(List<String> errors, String value) {
        ValidationUtils.validateMandatory(errors, value, "email");
        if (!StringUtils.isEmptyOrNull(value)) {
            Matcher matcher = EMAIL_PATTERN.matcher(value);
            if (!matcher.matches()) {
                errors.add("Email is not valid");
            }
        }
    }

    public static void validateLogin(List<String> errors, String value) {
        ValidationUtils.validateMandatory(errors, value, "Login");
    }

    public static void validatePassword(List<String> errors, String password, String confirmedPassword) {
        ValidationUtils.validateMandatory(errors, password, "Password");
        ValidationUtils.validateMandatory(errors, password, "Confirm password");
        if (!StringUtils.isEmptyOrNull(password) && !StringUtils.isEmptyOrNull(confirmedPassword)
                && !password.equals(confirmedPassword)) {
            errors.add("Passwords not match");
        }
    }

    public static void validateName(List<String> errors, String value) {
        ValidationUtils.validateMandatory(errors, value, "Name");
    }

    public static void validateLastName(List<String> errors, String value) {
        ValidationUtils.validateMandatory(errors, value, "Lastname");
    }

    public static void validateMandatory(List<String> errors, String value, String fieldName) {
        if (StringUtils.isEmptyOrNull(value)) {
            errors.add(fieldName + " can't be empty");
        }
    }

    public static void validateCarName(List<String> errors, String value) {
        ValidationUtils.validateMandatory(errors, value, "Car name");
    }

    public static void validateCarCategory(List<String> errors, String value) {
        ValidationUtils.validateMandatory(errors, value, "Car category");
    }

    public static void validateCarCapacity(List<String> errors, String value) {
        ValidationUtils.validateMandatory(errors, value, "Car capacity");
    }

    public static void validateLicensePlate(List<String> errors, String value) {
        ValidationUtils.validateMandatory(errors, value, "License plate");
    }
}
