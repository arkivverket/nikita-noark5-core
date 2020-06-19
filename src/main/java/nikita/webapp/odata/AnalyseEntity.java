package nikita.webapp.odata;

import org.apache.commons.lang3.reflect.FieldUtils;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public class AnalyseEntity {

    public String getPrimaryKey(java.lang.Class klass) {
        Field[] allFields = FieldUtils.getAllFields(klass);
        for (Field field : allFields) {
            if (field.getAnnotation(Id.class) != null) {
                System.out.println("Id class is: " + field.getName());
                return field.getName();
            }
        }
        return "";
    }

    public String getForeignKeyObjectName(String className, java.lang.Class klass)
            throws NoSuchMethodException {
        // FieldUtils.getAllFields, to get all superclass fields
        Field[] allFields = FieldUtils.getAllFields(klass);
        for (Field field : allFields) {
            String variableName = field.getName();
            // If the field is not a potential match for what we are looking
            // for simply continue
            if (!variableName.contains(className)) {
                continue;
            }

            if (field.getAnnotation(OneToMany.class) != null) {
                return getForeignKeyFromOneToMany(
                        className, field, klass, variableName);
            }
            if (field.getAnnotation(ManyToMany.class) != null) {
                return getForeignKeyFromManyToMany(
                        className, field, klass, variableName);
            }
            if (field.getAnnotation(ManyToOne.class) != null) {
                return getForeignKeyFromManyToOne(
                        className, field, klass, variableName);
            }
            if (field.getAnnotation(OneToOne.class) != null) {
                return getForeignKeyFromOneToOne(
                        className, field, klass, variableName);
            }

        }
        return "";
    }

    protected String getForeignKeyFromOneToOne(
            String className, Field field, Class klass, String variableName)
            throws NoSuchMethodException {
        return "";
    }

    protected String getForeignKeyFromManyToOne(
            String className, Field field, Class klass, String variableName)
            throws NoSuchMethodException {
        return "";
    }

    protected String getForeignKeyFromOneToMany(
            String className, Field field, Class klass, String variableName)
            throws NoSuchMethodException {
        for (java.lang.Class iface : field.getType().getInterfaces()) {
            if (iface.isAssignableFrom(Collection.class)) {
                Method method = klass.getMethod("get" +
                        variableName.substring(0, 1).toUpperCase() +
                        variableName.substring(1), null);
                if (null == method) {
                    method = klass.getMethod("getReference" +
                            variableName.substring(0, 1)
                                    .toUpperCase() +
                            variableName.substring(1), null);
                }
                // There is no foreign key associated with variable
                if (null == method) {
                    return "";
                }

                Type genericReturnType = method.getGenericReturnType();
                if (genericReturnType instanceof ParameterizedType) {
                    for (Type type :
                            ((ParameterizedType) genericReturnType)
                                    .getActualTypeArguments()) {
                        java.lang.Class returnType = (java.lang.Class) type;
                        if (returnType.getSimpleName()
                                .equals(className)) {
                            return variableName;
                        }
                    }
                }
            }
        }
        return "";
    }

    protected String getForeignKeyFromManyToMany(
            String className, Field field, Class klass, String variableName)
            throws NoSuchMethodException {
        for (java.lang.Class iface : field.getType().getInterfaces()) {
            if (iface.isAssignableFrom(Collection.class)) {
                Method method = klass.getMethod("get" +
                        variableName.substring(0, 1).toUpperCase() +
                        variableName.substring(1), null);
                if (null == method) {
                    method = klass.getMethod("getReference" +
                            variableName.substring(0, 1)
                                    .toUpperCase() +
                            variableName.substring(1), null);
                }
                // There is no foreign key associated with variable
                if (null == method) {
                    return "";
                }

                Type genericReturnType = method.getGenericReturnType();
                if (genericReturnType instanceof ParameterizedType) {
                    for (Type type :
                            ((ParameterizedType) genericReturnType)
                                    .getActualTypeArguments()) {
                        java.lang.Class returnType = (java.lang.Class) type;
                        if (returnType.getSimpleName()
                                .equals(className)) {
                            return variableName;
                        }
                    }
                }
            }
        }
        return "";
    }
}
