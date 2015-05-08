package de.fh_muenster.xbank.exceptions;

/**
 * Created by FalkoHoefte on 29.03.15.
 */
public class InvalidLoginException extends Exception {

    public InvalidLoginException(String message) {
        super(message);
    }

    public InvalidLoginException(String message, Throwable throwable) {
        super(message, throwable);
    }

}