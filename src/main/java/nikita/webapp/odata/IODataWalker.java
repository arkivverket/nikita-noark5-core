package nikita.webapp.odata;

public interface IODataWalker {

    void processEntityBase(String entity);

    void processEntityBase(String parentEntity, String entity, String systemId);

    void processStringCompare(String type, String attribute, String value);

    void processIntegerCompare(String type, String attribute,
                               String comparisonOperator, String value);

    void processFloatCompare(String type, String attribute,
                             String comparisonOperator, String value);

    void processSkipCommand(Integer skip);

    void processTopCommand(Integer top);

    void processOrderByCommand(String attribute, String sortOrder);

    void processComparatorCommand(String attribute, String comparator,
                                  String value);

    void processReferenceStatement(
            String fromEntity, String fromSystemId,
            String entity, String toEntity,
            String toSystemId);
}
