package nikita.webapp.odata;

import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.webapp.odata.model.Comparison;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;

import static nikita.common.config.ODataConstants.*;

/**
 * Extending ODataWalker to handle events to convert OData filter
 * command to SQL.
 */
public class ODataToHQL
        extends ODataWalker
        implements IODataWalker {

    private final HQLStatementBuilder statement;
    private final HibernateEntityReflections reflections;
    private Comparison comparison = new Comparison();
    private boolean right = false;

    public ODataToHQL(String dmlStatementType) {
        statement = new HQLStatementBuilder(dmlStatementType);
        reflections = new HibernateEntityReflections();
    }

    public ODataToHQL() {
        statement = new HQLStatementBuilder();
        reflections = new HibernateEntityReflections();
    }

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

    @Override
    public void processCountAsResource(Boolean includeResults) {
        statement.addCountAsResource(includeResults);
    }

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
        String foreignKey = reflections.getForeignKey(fromEntity, toEntity);
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
}
