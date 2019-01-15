package io.github.wendergalan.genericrestapi.exceptions;

/**
 * generic-rest-api
 * All rights reserved ©
 * *********************************************
 * File name: GenericControllerException.java
 * Created by : Wender Galan
 * Date created : 12/01/2019
 * *********************************************
 */
public class GenericControllerException extends RuntimeException {

    /**
     * Instantiates a new Generic controller exception.
     */
    public GenericControllerException() {
    }

    /**
     * Instantiates a new Generic controller exception.
     *
     * @param message the message
     */
    public GenericControllerException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Generic controller exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public GenericControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Generic controller exception.
     *
     * @param cause the cause
     */
    public GenericControllerException(Throwable cause) {
        super(cause);
    }
}
