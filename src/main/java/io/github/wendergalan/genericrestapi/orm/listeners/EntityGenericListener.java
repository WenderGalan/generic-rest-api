package io.github.wendergalan.genericrestapi.orm.listeners;

import io.github.wendergalan.genericrestapi.orm.annotations.FieldName;
import io.github.wendergalan.genericrestapi.orm.exceptions.PreRemoveEntityException;
import io.github.wendergalan.genericrestapi.orm.model.Dependency;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * generic-rest-api
 * All rights reserved ©
 * *********************************************
 * File name: EntityGenericListener.java
 * Created by : Wender Galan
 * Date created : 26/01/2019
 * *********************************************
 */
public class EntityGenericListener {

    @PreRemove
    public void validatePreRemove(Object entity) throws PreRemoveEntityException {
        Class clazz = entity.getClass();
        List<Field> fields = new ArrayList<>();
        /**
         * Retrieves all fields in the class
         */
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                fields.add(field);
            }
            clazz = clazz.getSuperclass();
        }

        if (!fields.isEmpty()) {
            Dependency dependency = new Dependency();
            for (Field field : fields) {
                boolean fieldValid = false;

                if (field.isAnnotationPresent(OneToMany.class) && !field.getAnnotation(OneToMany.class).orphanRemoval()) {
                    fieldValid = true;
                } else if (field.isAnnotationPresent(OneToOne.class) && !field.getAnnotation(OneToOne.class).orphanRemoval()) {
                    fieldValid = true;
                } else if (field.isAnnotationPresent(ManyToOne.class) && (!field.isAnnotationPresent(JoinColumn.class)
                        && (!field.isAnnotationPresent(JoinColumns.class) && !field.isAnnotationPresent(JoinTable.class)))) {
                    fieldValid = true;
                } else if (field.isAnnotationPresent(ManyToMany.class) && (!field.isAnnotationPresent(JoinColumn.class)
                        && !field.isAnnotationPresent(JoinColumns.class) && !field.isAnnotationPresent(JoinTable.class))) {
                    fieldValid = true;
                }

                 if (fieldValid) {
                     boolean existsDependency = false;
                     try {
                         String methodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                         Method method = entity.getClass().getMethod(methodName);
                         if (method != null) {
                             Object result = method.invoke(entity);
                             if (result != null) {
                                 if (result instanceof List) {
                                     existsDependency = !((List) result).isEmpty();
                                 } else {
                                     existsDependency = true;
                                 }
                             }
                         }
                     } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                         e.printStackTrace();
                     }

                     if (existsDependency) {
                         io.github.wendergalan.genericrestapi.orm.model.Field fieldReturn = new io.github.wendergalan.genericrestapi.orm.model.Field();
                         if (field.isAnnotationPresent(FieldName.class)) {
                             fieldReturn.setName(field.getAnnotation(FieldName.class).value());
                         } else {
                             fieldReturn.setName(field.getName());
                         }
                         throw new PreRemoveEntityException("First remove entity dependency(ies)", dependency);
                     }
                 }
            }
        }
    }
}
