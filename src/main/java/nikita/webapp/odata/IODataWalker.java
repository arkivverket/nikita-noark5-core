package nikita.webapp.odata;

public interface IODataWalker {

    void processSkip(Integer skip);

    void processTop(Integer top);

    void processOrderBy(String attribute, String sortOrder);

    void processCountAsResource(Boolean includeResults);

    void processQueryEntity(String entity);

    void processCompare(String aliasAndAttribute,
                        String comparisonOperator, Object value);

    void processComparatorCommand(String aliasAndAttribute,
                                  String comparator, Object value);

    void addEntityToEntityJoin(String fromEntity, String toEntity);

    void addClassNameForInheritanceClarification(
            String entityName, String originalEntityName);

    void processParenthesis(String bracket);

    void processLogicalOperator(String logicalOperator);

    void processMethodExpression(String methodName);

    void processAttribute(String attribute);

    void processComparator(String comparator);

    void processStartConcat();

    void processEndConcat();

    void startBoolComparison();

    void processStartRight();

    void endBoolComparison();

    void processPrimitive(Object value);

    void processCompareMethod(String methodName, Object value);
}
