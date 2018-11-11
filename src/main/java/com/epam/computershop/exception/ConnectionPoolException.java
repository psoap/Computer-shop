package com.epam.computershop.exception;

public class ConnectionPoolException extends Exception {
    public ConnectionPoolException() { }

    public ConnectionPoolException(String message) {
        super(message);
    }

    public ConnectionPoolException(String message, Exception ex) {
        super(message, ex);
    }
}
