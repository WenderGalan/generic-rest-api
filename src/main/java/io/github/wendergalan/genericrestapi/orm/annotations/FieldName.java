package io.github.wendergalan.genericrestapi.orm.annotations;

import java.lang.annotation.*;

/**
 * generic-rest-api
 * All rights reserved ©
 * *********************************************
 * File name: FieldName.java
 * Created by : Wender Galan
 * Date created : 26/01/2019
 * *********************************************
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface FieldName {
    String value() default "";
}
