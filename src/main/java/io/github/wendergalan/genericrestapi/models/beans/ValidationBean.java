package io.github.wendergalan.genericrestapi.models.beans;

/**
 * generic-rest-api
 * All rights reserved ©
 * *********************************************
 * File name: ValidationBean.java
 * Created by : Wender Galan
 * Date created : 12/01/2019
 * *********************************************
 */
public class ValidationBean {

    private String field;

    private String message;

    /**
     * Instantiates a new Validation bean.
     *
     * @param field   the field
     * @param message the message
     */
    public ValidationBean(String field, String message) {
        this.field = field;
        this.message = message;
    }

    /**
     * Gets field.
     *
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * Sets field.
     *
     * @param field the field
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
