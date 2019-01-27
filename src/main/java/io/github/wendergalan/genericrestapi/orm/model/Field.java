package io.github.wendergalan.genericrestapi.orm.model;

import java.util.Objects;

/**
 * generic-rest-api
 * All rights reserved ©
 * *********************************************
 * File name: Field.java
 * Created by : Wender Galan
 * Date created : 26/01/2019
 * *********************************************
 */
public class Field {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(name, field.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
