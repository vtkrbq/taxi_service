package ua.rudniev.taxi.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String login) {
        super("User with login" + login + "is already exists");
    }
}
