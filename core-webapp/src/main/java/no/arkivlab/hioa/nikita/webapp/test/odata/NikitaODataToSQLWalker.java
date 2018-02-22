package no.arkivlab.hioa.nikita.webapp.test.odata;

import static nikita.config.Constants.DM_OWNED_BY;

/**
 * Extending NikitaODataWalker to handle events so we can convert OData filter
 * command to SQL.
 */

public class NikitaODataToSQLWalker
        extends NikitaODataWalker {

    private String sqlStatement;

    /**
     * processResource
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
     * currently just dealing with getting OData2SQL to work.
     *
     * @param entity       The entity/table you wish to search
     * @param loggedInUser The name of the user whose tupples you want to
     *                     retrieve
     */
    @Override
    public void processResource(String entity, String loggedInUser) {
        sqlStatement = "SQL Statement : ";
        sqlStatement += "select * from " + getNameDatabase(entity) + " where ";
        sqlStatement += DM_OWNED_BY + " ='" + loggedInUser + "' and ";
    }


    @Override
    public void processEnterAttribute(ODataParser.AttributeContext ctx) {
        super.processEnterAttribute(ctx);
    }

    @Override
    public void processEnterValue(ODataParser.ValueContext ctx) {
        super.processEnterValue(ctx);
    }

    /**
     * processContains
     * <p>
     * Convert 'contains' to a LIKE in SQL. In the following example:
     * <p>
     * [contextPath][api]/arkivstruktur/arkiv?$filter=contains(tittel, 'hello')
     * <p>
     * contains(tittel, 'hello') becomes the following SQL:
     * tittel LIKE %hello%
     *
     * @param attribute The attribute/column you wish to filter on
     * @param value     The value you wish to filter on
     */
    @Override
    public void processContains(String attribute, String value) {
        sqlStatement += getNameDatabase(attribute) + " LIKE '%" + value + "%'";
    }

    /**
     * processStartsWith
     * <p>
     * Convert 'startsWith' to a LIKE in SQL. In the following example:
     * <p>
     * [contextPath][api]/arkivstruktur/arkiv?$filter=contains(tittel, 'hello')
     * <p>
     * startsWith(tittel, 'hello') becomes the following SQL:
     * tittel LIKE hello%
     * <p>
     * This is achieved by looking at the children and picking out the
     * attribute (in this case 'tittel') and value (in this case) 'hello'.
     *
     * @param attribute The attribute/column you wish to filter on
     * @param value     The value you wish to filter on
     */
    @Override
    public void processStartsWith(String attribute, String value) {
        sqlStatement += getNameDatabase(attribute) + " LIKE '" + value + "%'";
    }

    public String getSqlStatement() {
        return sqlStatement;
    }
}
