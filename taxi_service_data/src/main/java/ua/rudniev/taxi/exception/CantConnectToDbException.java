package ua.rudniev.taxi.exception;

public class CantConnectToDbException extends RuntimeException {
    public CantConnectToDbException(String login) {
        super("Can't connect to Database");
    }
}
