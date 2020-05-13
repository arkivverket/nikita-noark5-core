package nikita.webapp.odata;


import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.webapp.odata.model.HQLStatement;
import nikita.webapp.odata.model.Ref;
import nikita.webapp.odata.model.RefBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static nikita.common.config.ODataConstants.*;

/**
 * Extending NikitaODataWalker to handle events so we can convert OData filter
 * command to SQL.
 */
public class NikitaODataToHQLWalker
        extends NikitaODataWalker
        implements IODataWalker {

    private static final Logger logger =
            LoggerFactory.getLogger(NikitaODataToHQLWalker.class);

    private final HQLStatement statement;
    private Ref ref;
    private Map<String, Class<?>> entityMap = new HashMap<>();

    public NikitaODataToHQLWalker(String dmlStatementType) {
        statement = new HQLStatement(dmlStatementType);
        constructEntityList();
    }

    public NikitaODataToHQLWalker() {
        statement = new HQLStatement();
    }

    /**
     * processResource
     * <p>
     * When dealing with the following example URL:
     * <p>
     * [contextPath][api]/arkivstruktur/arkiv?$filter=startsWith(tittel,'hello')
     * <p>
     * The 'arkiv' entity is identified as a entity and picked out and is
     * identified as the 'from' part. We always add to the where clause to
     * filter out rows that actually belong to the user first and then can add
     * extra filtering as the walker progresses.
     * <p>
     * Note this will cause some problems when dealing with ownership of objects
     * via groups. Probably have to some lookup or something. But we are
     * currently just dealing with getting OData2HQL to work.
     *
     * @param entity The entity/table you wish to search
     */

    @Override
    public void processEntityBase(String entity) {
        super.processEntityBase(entity);
        statement.addQueryEntity(entity);
    }

    /**
     * processNikitaObjects
     * <p>
     * When dealing with the following example URL:
     * <p>
     * [contextPath][api]/arkivstruktur/arkiv?$filter=startsWith(tittel,'hello')
     * <p>
     * The 'arkiv' entity is identified as a resource and picked out and is
     * identified as the 'from' part. We always add to the where clause to
     * filter out rows that actually belong to the user first and then can add
     * extra filtering as the walker progresses.
     * <p>
     * Note this will cause some problems when dealing with ownership of objects
     * via groups. Probably have to some lookup or something. But we are
     * currently just dealing with getting OData2HQL to work.
     *
     * @param parentEntity the parent entity e.g. series->casefile query
     *                     then series is the parent entity.
     * @param entity       the entity to retreve e.g. series->casefile query
     *                     then casefile is the entity.
     * @param systemId     systemId of the parent entity
     */
    @Override
    public void processEntityBase(String parentEntity, String entity,
                                  String systemId) {
        this.entity = entity;
        statement.addQueryEntity(parentEntity);
        statement.addFromWithForeignKey(getInternalNameObject(parentEntity),
                getInternalNameObject(entity), systemId);
    }

    /**
     * processComparatorCommand
     * <p>
     * Convert a general Odata attribute comparator value command to HQL. In
     * the following example:
     * <p>
     * [contextPath][api]/arkivstruktur/arkiv?$filter=tittel eq 'hello'
     * <p>
     * tittel eq 'hello' becomes the following HQL:
     * <p>
     * <p>
     * This is achieved by looking at the children and picking out the
     * attribute (in this case 'tittel'), the comparator (in this case eq) and
     * value (in this case 'hello').
     *
     * @param aliasAndAttribute The alias and attribute you wish to
     *                          filter on
     * @param comparator The type of comparison you want to undertake e.g eq,
     *                   lt etc
     * @param value      The value you wish to filter on
     */
    @Override
    public void processComparatorCommand(String aliasAndAttribute,
                                         String comparator, String value) {
        statement.addCompareValue(aliasAndAttribute,
                translateComparator(comparator), value);
    }

    /**
     * $filter=startsWith(tittel,'hello')
     * $filter=endsWith(tittel,'hello')
     * $filter=contains(tittel,'hello')
     * <p>
     * The attribute is tittel
     * The value is hello
     * Type is startsWith | endsWith | contains
     *
     * @param type      the command
     * @param attribute The name of the column / attribute
     * @param value     The value to compare against
     */
    @Override
    public void processStringCompare(String entityAlias, String type,
                                     String attribute, String value) {
        if (type.equalsIgnoreCase(STARTS_WITH)) {
            statement.addStartsWith(entityAlias,
                    getInternalNameObject(attribute), value);
        } else if (type.equalsIgnoreCase(ENDS_WITH)) {
            statement.addEndsWith(entityAlias,
                    getInternalNameObject(attribute), value);
        } else if (type.equalsIgnoreCase(CONTAINS)) {
            statement.addContains(entityAlias,
                    getInternalNameObject(attribute), value);
        } else {
            throw new NikitaMalformedInputDataException(
                    "OData string contains content that can't be processed."
                            + " values are type [" + type + "], attributte [" +
                            attribute + "], value [" + value + "]");
        }
    }

    @Override
    public void processIntegerCompare(String function, String aliasAndAttribute,
                                      String comparisonOperator, String value) {
        statement.addCompareValue(function, aliasAndAttribute,
                translateComparator(comparisonOperator), value);
    }

    // from File file where
    @Override
    public void addEntityToEntityJoin(String fromEntity, String toEntity) {
        String foreignKey = getForeignKey(fromEntity, toEntity);
        statement.addEntityToEntityJoin(fromEntity, foreignKey, toEntity);
    }

    @Override
    public void processINCompare(String entity, String attribute,
                                 String comparisonOperator, String value) {
        statement.addCompare(entity.toLowerCase() + "_1." + attribute,
                translateComparator(comparisonOperator), value);
    }

    @Override
    public void processINCompareForeignKey(
            String entity, String attribute,
            String comparisonOperator, String value) {
        statement.addCompare(entity + "." + attribute,
                translateComparator(comparisonOperator), value);
    }

    @Override
    public void processLogicalOperator(String logicalOperator) {
        statement.addAnd();
    }

    @Override
    public void processFloatCompare(String type, String attribute,
                                    String comparisonOperator, String value) {

    }

    @Override
    public void processSkipCommand(Integer skip) {
        statement.setLimitOffset(new AtomicInteger(skip));
    }

    @Override
    public void processTopCommand(Integer top) {
        statement.setLimitHowMany(new AtomicInteger(top));
    }

    @Override
    public void processOrderByCommand(String attribute, String sortOrder) {
        statement.addOrderBy(attribute, sortOrder);
    }

    public Query getHqlStatment(Session session) {
        return statement.getQuery(session);
    }

    /**
     * /arkivstruktur/mappe/1234/ny-kryssreferanse/$ref?$id=
     * arkivstruktur/basisregistrering/4321
     * <p>
     * This will likely be taken out!
     *
     * @param fromEntity   mappe
     * @param fromSystemId 1234
     * @param entity       ny-kryssreferanse
     * @param toEntity     basisregistrering
     * @param toSystemId   4321
     */
    @Override
    public void processReferenceStatement(
            String fromEntity, String fromSystemId, String entity,
            String toEntity, String toSystemId) {
        ref = new RefBuilder().
                setFromEntity(getInternalNameObject(fromEntity)).
                setFromSystemId(fromSystemId).
                setEntity(getInternalNameObject(entity)).
                setToEntity(getInternalNameObject(toEntity)).
                setToSystemId(toSystemId)
                .createRef();
    }


    public Ref getRef() {
        return ref;
    }

    /**
     * Convert a OData comparator to a HQL comparator
     * - "eq" -> "="
     * - "gt" -> ">"
     * - "ge" -> ">="
     * - "lt" -> "<"
     * - "le" -> "<="
     * - "ne" -> "!="
     *
     * @param comparator The OData comparator
     * @return comparator used in HQL
     */
    private String translateComparator(String comparator)
            throws NikitaMalformedInputDataException {

        switch (comparator) {
            case ODATA_EQ:
                return HQL_EQ;
            case ODATA_GT:
                return HQL_GT;
            case ODATA_GE:
                return HQL_GE;
            case ODATA_LT:
                return HQL_LT;
            case ODATA_LE:
                return HQL_LE;
            case ODATA_NE:
                return HQL_NE;
        }
        throw new NikitaMalformedInputDataException(
                "Unrecognised comparator used in OData query (" +
                        comparator + ")");
    }

    private String getForeignKey(String fromClassName, String toClassName) {

        Class klass = Optional.ofNullable(entityMap.get(fromClassName))
                .orElseThrow(() -> new BadRequestException(
                        "Unsupported Noark class: " + fromClassName));

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

            if (field.getAnnotation(OneToMany.class) != null) {
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
                System.out.println("Id class is: " + field.getName());
                return field.getName();
            }
        }
        return "";
    }

    private String getAlias(String entity) {
        return entity.toLowerCase() + "_1";
    }

    private String getEntityAndAlias(String entity) {
        return entity + "." + entity.toLowerCase() + "_1";
    }

    private void constructEntityList() {
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
