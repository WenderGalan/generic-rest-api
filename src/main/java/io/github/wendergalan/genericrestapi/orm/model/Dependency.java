package io.github.wendergalan.genericrestapi.orm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * generic-rest-api
 * All rights reserved ©
 * *********************************************
 * File name: Dependency.java
 * Created by : Wender Galan
 * Date created : 26/01/2019
 * *********************************************
 */
public class Dependency {

    private String message = "This attribute(s) have dependency(ies)";

    private List<Field> fields = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dependency that = (Dependency) o;
        return Objects.equals(message, that.message) &&
                Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, fields);
    }
}
