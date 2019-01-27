package io.github.wendergalan.genericrestapi.config;

import io.github.wendergalan.genericrestapi.exceptions.GenericControllerException;
import io.github.wendergalan.genericrestapi.orm.exceptions.PreRemoveEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * generic-rest-api
 * All rights reserved ©
 * *********************************************
 * File name: GenericGlobalExceptionConfig.java
 * Created by : Wender Galan
 * Date created : 26/01/2019
 * *********************************************
 */
public class GenericGlobalExceptionConfig {

    @ExceptionHandler({GenericControllerException.class, PreRemoveEntityException.class})
    public ResponseEntity handleErrorGeneric(Exception ex) {
        if (ex instanceof PreRemoveEntityException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(((PreRemoveEntityException) ex).getDependency());
        } else if (ex instanceof GenericControllerException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        } else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
