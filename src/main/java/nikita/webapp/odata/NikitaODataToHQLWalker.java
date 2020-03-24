package nikita.webapp.odata;


import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.webapp.odata.model.Ref;
import nikita.webapp.odata.model.RefBuilder;
import org.hibernate.Session;
import org.hibernate.query.Query;

import static nikita.common.config.ODataConstants.*;

/**
 * Extending NikitaODataWalker to handle events so we can convert OData filter
 * command to SQL.
 */

public class NikitaODataToHQLWalker
        extends NikitaODataWalker
        implements IODataWalker {

    private HQLStatementBuilder hqlStatement;
    private Ref ref;

    public NikitaODataToHQLWalker(String dmlStatementType) {
        this.hqlStatement = new HQLStatementBuilder(dmlStatementType);
    }

    public NikitaODataToHQLWalker() {
        this.hqlStatement = new HQLStatementBuilder();
    }

    public void setParentIdPrimaryKey(String primaryKey) {
        hqlStatement.setParentIdPrimaryKey(primaryKey);
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
        hqlStatement.addFrom(getNameObject(entity) + " x ");
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
        hqlStatement.setParentIdPrimaryKey(systemId);
        hqlStatement.addFromWithForeignKey(parentEntity, entity);
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
        hqlStatement.addEqualsWhere("x." + getNameObject(attribute), comparator,
                value);
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
            hqlStatement.addStartsWith(attribute, value);
        } else if (type.equalsIgnoreCase(ENDS_WITH)) {
            hqlStatement.addEndsWith(attribute, value);
        } else if (type.equalsIgnoreCase(CONTAINS)) {
            hqlStatement.addContains(attribute, value);
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

    }

    @Override
    public void processFloatCompare(String type, String attribute,
                                    String comparisonOperator, String value) {

    }

    @Override
    public void processSkipCommand(Integer skip) {
        hqlStatement.addLimitby_skip(skip);
    }

    @Override
    public void processTopCommand(Integer top) {
        hqlStatement.addLimitby_top(top);
    }

    @Override
    public void processOrderByCommand(String attribute, String sortOrder) {
        hqlStatement.addOrderby(attribute, sortOrder);
    }

    public Query getHqlStatment(Session session) {
        return hqlStatement.buildHQLStatement(session);
    }

    /**
     * /arkivstruktur/mappe/1234/ny-kryssreferanse/$ref?$id=
     * arkivstruktur/basisregistrering/4321
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
                setFromEntity(fromEntity).
                setFromSystemId(fromSystemId).
                setEntity(entity).
                setToEntity(toEntity).
                setToSystemId(toSystemId).createRef();
    }

    public Ref getRef() {
        return ref;
    }
}
