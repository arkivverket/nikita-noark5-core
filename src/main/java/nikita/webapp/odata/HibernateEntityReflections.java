package nikita.webapp.odata;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class HibernateEntityReflections {

    private static final Logger logger =
            LoggerFactory.getLogger(HibernateEntityReflections.class);

    private final Map<String, Class<?>> entityMap = new HashMap<>();

    public HibernateEntityReflections() {
        constructEntityList();
    }

    protected String getForeignKey(String fromClassName, String toClassName) {

        Class klass = Optional.ofNullable(entityMap.get(fromClassName))
                .orElseThrow(() -> new BadRequestException(
                        "Unsupported Entity class: " + fromClassName));

        String foreignKeyName = "";
        Field[] allFields = FieldUtils.getAllFields(klass);
        for (Field field : allFields) {
            String variableName = field.getName();
            // If the field is not a potential match for what we are looking
            // for simply continue
            if (!variableName.contains(toClassName)) {
                continue;
            }

            if (field.getAnnotation(ManyToOne.class) != null) {
                String type = field.getType().getSimpleName();
                if (toClassName.equals(type)) {
                    foreignKeyName = field.getName();
                }
            }

            if (field.getAnnotation(OneToMany.class) != null ||
                    field.getAnnotation(ManyToMany.class) != null) {
                for (java.lang.Class iface : field.getType().getInterfaces()) {
                    if (iface.isAssignableFrom(Collection.class)) {
                        Method method;
                        try {
                            method = klass.getMethod("get" +
                                    variableName.substring(0, 1).toUpperCase() +
                                    variableName.substring(1), null);
                            if (null == method) {
                                method = klass.getMethod("getReference" +
                                        variableName.substring(0, 1)
                                                .toUpperCase() +
                                        variableName.substring(1), null);
                            }
                        } catch (NoSuchMethodException e) {
                            String error = klass.getName() + "has no foreign" +
                                    " key for " + toClassName;
                            logger.error(error);
                            throw new InternalServerErrorException(error);
                        }

                        Type genericReturnType = method.getGenericReturnType();
                        if (genericReturnType instanceof ParameterizedType) {
                            for (Type type :
                                    ((ParameterizedType) genericReturnType)
                                            .getActualTypeArguments()) {
                                java.lang.Class returnType = (java.lang.Class) type;
                                if (returnType.getSimpleName()
                                        .equals(toClassName)) {
                                    foreignKeyName = variableName;
                                }
                            }
                        }
                    }
                }
            }
        }
        return foreignKeyName;
    }

    public Class getClass(String className) {
        return Optional.ofNullable(entityMap.get(className))
                .orElseThrow(() -> new BadRequestException(
                        "Unsupported Noark class: " + className));
    }

    public String getPrimaryKey(String className) {

        Class klass = Optional.ofNullable(entityMap.get(className))
                .orElseThrow(() -> new BadRequestException(
                        "Unsupported Noark class: " + className));

        Field[] allFields = FieldUtils.getAllFields(klass);
        for (Field field : allFields) {
            if (field.getAnnotation(Id.class) != null) {
                return field.getName();
            }
        }
        return "";
    }

    protected void constructEntityList() {
        Reflections ref = new Reflections("nikita.common.model.noark5.v5");
        Set<String> entities1 =
                ref.getTypesAnnotatedWith(
                        javax.persistence.Entity.class)
                        .stream().map(Class::getName).collect(Collectors.toSet());

        Iterator<Class<?>> itr =
                ref.getTypesAnnotatedWith(Entity.class).iterator();
        while (itr.hasNext()) {
            Class klass = itr.next();
            String simpleName = klass.getSimpleName();
            entityMap.put(simpleName, klass);
        }
    }
}
