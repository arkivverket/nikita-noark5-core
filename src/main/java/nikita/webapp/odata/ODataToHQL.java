package nikita.webapp.odata;

import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.webapp.odata.model.Comparison;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static nikita.common.config.ODataConstants.*;

/**
 * Extending ODataWalker to handle events to convert OData filter
 * command to SQL.
 */
public class ODataToHQL
        extends ODataWalker
        implements IODataWalker {

    private static final Logger logger =
            LoggerFactory.getLogger(ODataToHQL.class);

    private final HQLStatementBuilder statement;
    private Map<String, Class<?>> entityMap = new HashMap<>();
    private Comparison comparison = new Comparison();
    private boolean right = false;

    public ODataToHQL(String dmlStatementType) {
        statement = new HQLStatementBuilder(dmlStatementType);
        constructEntityList();
    }

    public ODataToHQL() {
        statement = new HQLStatementBuilder();
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
    public void processQueryEntity(String entity) {
        statement.addQueryEntity(entity);
    }

    @Override
    public void processAttribute(String attribute) {
        if (!right) {
            comparison.setLeft(attribute);
        } else {
            comparison.setRight(attribute);
        }
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
     * @param comparator        The type of comparison you want to undertake e.g eq,
     *                          lt etc
     * @param value             The value you wish to filter on
     */
    @Override
    public void processComparatorCommand(String aliasAndAttribute,
                                         String comparator, Object value) {
        statement.addCompareValue(aliasAndAttribute,
                translateComparator(comparator), value);
    }

    @Override
    public void processCompareMethod(String methodName, Object value) {
        if (methodName.equals("contains")) {
            comparison.setRight("%" + value + "%");
            comparison.setComparator("like");
        } else if (methodName.equals("startswith")) {
            comparison.setRight(value + "%");
            comparison.setComparator("like");
        } else if (methodName.equals("endswith")) {
            comparison.setRight("%" + value);
            comparison.setComparator("like");
        } else {
            comparison.setRight(value);
        }
    }

    @Override
    public void processCompare(String aliasAndAttribute,
                               String comparisonOperator, Object value) {
        statement.addCompareValue(aliasAndAttribute,
                translateComparator(comparisonOperator), value);
    }

    @Override
    public void addEntityToEntityJoin(String fromEntity, String toEntity) {
        String foreignKey = getForeignKey(fromEntity, toEntity);
        statement.addEntityToEntityJoin(fromEntity, foreignKey, toEntity);
    }

    @Override
    public void processParenthesis(String bracket) {
        statement.addBracket(bracket);
    }

    @Override
    public void processLogicalOperator(String logicalOperator) {
        statement.addLogicalOperator(logicalOperator);
    }

    @Override
    public void processComparator(String comparator) {
        comparison.setComparator(translateComparator(comparator));
    }

    @Override
    public void processStartRight() {
        right = true;
    }

    @Override
    public void processMethodExpression(String methodName) {
        if (methodName.equalsIgnoreCase("tolower")) {
            methodName = "lower";
        } else if (methodName.equalsIgnoreCase("toupper")) {
            methodName = "upper";
        }

        if (!right) {
            comparison.addLeftMethod(methodName);
        } else {
            comparison.addRightMethod(methodName);
        }
    }


    @Override
    public void processSkip(Integer skip) {
        statement.setLimitOffset(new AtomicInteger(skip));
    }

    @Override
    public void processTop(Integer top) {
        statement.setLimitHowMany(new AtomicInteger(top));
    }

    @Override
    public void startBoolComparison() {
        comparison = new Comparison();
        right = false;
    }

    @Override
    public void endBoolComparison() {
        String leftFunctionNames = "", leftFunctionClose = "";
        for (Object function : comparison.getLeftMethods()) {
            leftFunctionNames += function + "(";
            leftFunctionClose += ")";
        }
        String rightFunctionNames = "", rightFunctionClose = "";
        for (Object function : comparison.getRightMethods()) {
            rightFunctionNames += function + "(";
            rightFunctionClose += ")";
        }
        StringJoiner concatValues = comparison.getConcatValues();
        // 8 is the number of characters in concat()
        if (concatValues.length() > 8) {
            statement.addCompareValueFunction(concatValues.toString(), "",
                    comparison.getLeft(), comparison.getComparator(),
                    comparison.getRight(), rightFunctionNames, rightFunctionClose);
        } else {
            statement.addCompareValueFunction(leftFunctionNames, leftFunctionClose,
                    comparison.getLeft(), comparison.getComparator(),
                    comparison.getRight(), rightFunctionNames, rightFunctionClose);
        }
        processEndConcat();
    }

    @Override
    public void processStartConcat() {
        comparison.startConcat();
    }

    @Override
    public void processEndConcat() {
        comparison.endConcat();
    }

    @Override
    public void processOrderBy(String attribute, String sortOrder) {
        statement.addOrderBy(attribute, sortOrder);
    }

    public Query getHqlStatement(Session session) {
        return statement.getQuery(session);
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

    public void processPrimitive(Object value) {
        if (comparison.getProcessConcat()) {
            value = "'" + value + "'";
        }
        if (!right) {
            comparison.setLeft(value);
        } else {
            comparison.setRight(value);
        }
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
