package de.fh_muenster.xbank.exceptions;

/**
 * Created by FalkoHoefte on 29.03.15.
 */
public class NoSessionException extends Exception {

    public NoSessionException(String message) {
        super(message);
    }

    public NoSessionException(String message, Throwable throwable) {
        super(message, throwable);
    }

}