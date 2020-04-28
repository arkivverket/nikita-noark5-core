package nikita.webapp.odata;


import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.webapp.odata.model.HQLStatement;
import nikita.webapp.odata.model.Ref;
import nikita.webapp.odata.model.RefBuilder;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.concurrent.atomic.AtomicInteger;

import static nikita.common.config.ODataConstants.*;

/**
 * Extending NikitaODataWalker to handle events so we can convert OData filter
 * command to SQL.
 */

public class NikitaODataToHQLWalker
        extends NikitaODataWalker
        implements IODataWalker {

    private final HQLStatement statement;
    private Ref ref;

    public NikitaODataToHQLWalker(String dmlStatementType) {
        statement = new HQLStatement(dmlStatementType);
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
        statement.addFrom(getInternalNameObject(entity) + " x ");
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
     *                     *                       then casefile is the entity.
     * @param systemId     systemId of the parent entity
     */
    @Override
    public void processEntityBase(String parentEntity, String entity,
                                  String systemId) {
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
     * @param attribute  The attribute/column you wish to filter on
     * @param comparator The type of comparison you want to undertake e.g eq,
     *                   lt etc
     * @param value      The value you wish to filter on
     */
    @Override
    public void processComparatorCommand(String attribute, String comparator,
                                         String value) {
        // TODO: Why using "x." here???
        statement.addAttribute("x." + getInternalNameAttribute(attribute));
        statement.addComparator(translateComparator(comparator));
        statement.addValue(value);
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
    public void processStringCompare(String type, String attribute,
                                     String value) {
        if (type.equalsIgnoreCase(STARTS_WITH)) {
            statement.addStartsWith("x." +
                    getInternalNameAttribute(attribute), value);
        } else if (type.equalsIgnoreCase(ENDS_WITH)) {
            statement.addEndsWith("x." +
                    getInternalNameAttribute(attribute), value);
        } else if (type.equalsIgnoreCase(CONTAINS)) {
            statement.addContains("x." +
                    getInternalNameAttribute(attribute), value);
        } else {
            throw new NikitaMalformedInputDataException(
                    "OData string contains content that can't be processed."
                            + " values are type [" + type + "], attributte [" +
                            attribute + "], value [" + value + "]");
        }
    }

    @Override
    public void processIntegerCompare(String type, String attribute,
                                      String comparisonOperator, String value) {
        statement.addAttribute(getInternalNameAttribute(attribute));
        statement.addComparator(translateComparator(comparisonOperator));
        statement.addValue(value);
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
}
