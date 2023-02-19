package ua.rudniev.taxi.exception;

public class PasswordDoesNotMatchException extends RuntimeException {
    public PasswordDoesNotMatchException() {
        super("Old password is incorrect.");
    }
}
