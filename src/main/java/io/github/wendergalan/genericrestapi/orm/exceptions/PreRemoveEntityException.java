package io.github.wendergalan.genericrestapi.orm.exceptions;

import io.github.wendergalan.genericrestapi.orm.model.Dependency;

/**
 * generic-rest-api
 * All rights reserved ©
 * *********************************************
 * File name: PreRemoveEntityException.java
 * Created by : Wender Galan
 * Date created : 26/01/2019
 * *********************************************
 */
public class PreRemoveEntityException extends Exception {

    private Dependency dependency;

    public PreRemoveEntityException(Dependency dependency) {
        this.dependency = dependency;
    }

    public PreRemoveEntityException(String message, Dependency dependency) {
        super(message);
        this.dependency = dependency;
    }

    public PreRemoveEntityException(String message, Throwable cause, Dependency dependency) {
        super(message, cause);
        this.dependency = dependency;
    }

    public PreRemoveEntityException(Throwable cause, Dependency dependency) {
        super(cause);
        this.dependency = dependency;
    }

    public PreRemoveEntityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Dependency dependency) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.dependency = dependency;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public void setDependency(Dependency dependency) {
        this.dependency = dependency;
    }
}
