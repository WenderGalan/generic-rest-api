package io.github.wendergalan.genericrestapi.models.beans;

import io.github.wendergalan.genericrestapi.models.enums.SearchOperator;
import io.github.wendergalan.genericrestapi.utils.Util;

/**
 * generic-rest-api
 * All rights reserved ©
 * *********************************************
 * File name: SearchCriteria.java
 * Created by : Wender Galan
 * Date created : 12/01/2019
 * *********************************************
 */
public class SearchCriteria {

    private String key;
    private String operation;
    private Object value;

    /**
     * Instantiates a new Search criteria.
     */
    public SearchCriteria() {}

    /**
     * Instantiates a new Search criteria.
     *
     * @param key       the key
     * @param operation the operation
     * @param value     the value
     */
    public SearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = convertValueType(value);
    }

    private Object convertValueType(Object value) {
        if (value != null) {
            if (value.equals(SearchOperator.IS_NULL.toString())) {
                operation = SearchOperator.IS_NULL.toString();
                value = null;
            } else if (value.equals(SearchOperator.IS_NOT_NULL.toString())) {
                operation = SearchOperator.IS_NOT_NULL.toString();
                value = null;
            } else value = Util.converterDataOfParameters(value);
        }
        return value;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets key.
     *
     * @param key the key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Gets operation.
     *
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Sets operation.
     *
     * @param operation the operation
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
