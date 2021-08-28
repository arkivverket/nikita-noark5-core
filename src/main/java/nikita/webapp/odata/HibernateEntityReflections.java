package nikita.webapp.odata;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

import static nikita.common.util.CommonUtils.entityMap;
import static nikita.common.util.CommonUtils.natIdentMap;

public class HibernateEntityReflections {

    private static final Logger logger =
            LoggerFactory.getLogger(HibernateEntityReflections.class);

    protected String getForeignKey(String fromClassName, String toClassName) {

        // This is a specific case that we have to deal with and perhaps is
        // applicable to others. The relationship is actually between File and
        // Record rather than CaseFile and RegistryEntry
        if (toClassName.equalsIgnoreCase("CaseFile") &&
                fromClassName.equalsIgnoreCase("RegistryEntry")) {
            fromClassName = "RecordEntity";
            toClassName = "File";
        }
        if (toClassName.equalsIgnoreCase("CaseFile") &&
                fromClassName.equalsIgnoreCase("RecordNote")) {
            fromClassName = "RecordEntity";
            toClassName = "File";
        }

        String finalFromClassName = fromClassName;
        Class<?> klass = Optional.ofNullable(entityMap.get(fromClassName))
                .orElseThrow(() -> new BadRequestException(
                        "Unsupported Entity class: " + finalFromClassName));

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
        if (foreignKeyName.isEmpty() && null != natIdentMap.get(toClassName)) {
            foreignKeyName = "referenceNationalIdentifier";
        }
        return foreignKeyName;
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
}
