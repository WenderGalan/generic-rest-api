package io.github.wendergalan.genericrestapi.utils;

import io.github.wendergalan.genericrestapi.models.beans.ValidationBean;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

/**
 * generic-rest-api
 * All rights reserved ©
 * *********************************************
 * File name: Util.java
 * Created by : Wender Galan
 * Date created : 12/01/2019
 * *********************************************
 */
public class Util {

    /**
     * Converter data of parameters object.
     *
     * @param value the value
     * @return the object
     */
    public static Object converterDataOfParameters(Object value) {
        if (value == null) {
            return null;
        }

        Object converterValue = null;
        try {
            converterValue = Integer.parseInt(value.toString());
        } catch (NumberFormatException ex) {
        }

        if (converterValue == null && (value.toString().toUpperCase().equals("TRUE") || value.toString().toUpperCase().equals("FALSE"))) {
            converterValue = Boolean.valueOf(value.toString());
        }

        if (converterValue == null) {
            converterValue = DateUtil.convertData(value.toString(), DateUtil.MASCARA_DD_MM_YYY_HH_MM_SS);
        }
        if (converterValue == null) {
            converterValue = DateUtil.convertData(value.toString(), DateUtil.MASCARA_DD_MM_YYY_HH_MM_SS_A);
        }
        if (converterValue == null) {
            converterValue = DateUtil.convertData(value.toString(), DateUtil.MASCARA_DD_MM_YYY);
        }
        if (converterValue == null) {
            converterValue = DateUtil.convertData(value.toString(), DateUtil.MASCARA_HH_MM_SS);
        }
        if (converterValue == null) {
            converterValue = DateUtil.convertData(value.toString(), DateUtil.MASCARA_HH_MM_SS_A);
        }

        return converterValue != null ? converterValue : value;
    }

    /**
     * Create list of validation errors list.
     *
     * @param errors the errors
     * @return the list
     */
    public static List<ValidationBean> createListOfValidationErrors(List<ObjectError> errors) {
        List<ValidationBean> lista = new ArrayList<>();
        if (errors != null && !errors.isEmpty()) {
            for (ObjectError error : errors) {
                if(error instanceof FieldError){
                    lista.add(new ValidationBean(((FieldError) error).getField(), error.getDefaultMessage()));
                }else{
                    lista.add(new ValidationBean(error.getObjectName(), error.getDefaultMessage()));
                }
            }
        }
        return lista;
    }
}
