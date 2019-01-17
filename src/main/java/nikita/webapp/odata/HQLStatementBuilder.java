package nikita.webapp.odata;

import nikita.common.model.noark5.v4.casehandling.CaseFile;
import nikita.common.util.CommonUtils;
import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.out;
import static nikita.common.config.ODataConstants.*;

/**
 * HQLStatementBuilder
 * <p>
 * Handle the process of building an HQL statement. The potential statement
 * is derived for a number of parts. The select part is straight forward. The
 * where part is an ArrayList of various clauses that are currently joined
 * together with an 'and'. We need a better way to handle this. But this is
 * experimental, prototyping the solution as we go along.
 * <p>
 * Note. When implementing paging, make sure there is a sort order. Remember
 * the fetch order is unpredictable.
 */

public class HQLStatementBuilder {

    private String select;
    private ArrayList<String> whereList;
    private Map<String, String> orderByMap;
    private Integer limitHowMany;
    private Integer limitOffset;

    public HQLStatementBuilder() {
        select = "";
        whereList = new ArrayList<>();
        orderByMap = new HashMap<>();
    }

    public void addSelect(String entity, String ownerColumn, String
            loggedInUser) {
        select = "FROM " + entity + " where " + ownerColumn + " ='" +
                loggedInUser + "'";
    }

    public void addSelectWithForeignKey
            (String parentResource, String resource, String ownerColumn,
             String loggedInUser) {
        select = "FROM CaseFile where ";
        if (getNameObject(resource).equalsIgnoreCase("caseFile")) {
            select += CaseFile.getForeignKeyIdentifier(
                    getNameObject(parentResource)) + "= 'PARENT_ID' and ";
        }
        select += ownerColumn + " = '" + loggedInUser + "'";
    }

    /**
     * Currently the parser is passing in a value with a single quote.
     * <p>
     * .... $filter=tittel eq 'Oppføring av bygg på eksempelvei 1' ....
     * <p>
     * attribute = tittel
     * comparator = eq
     * value = 'Oppførring av bygg på eksempelvei 1'
     *
     * @param attribute  Noark attribute in english e.g. title (must be
     *                   translated before passing in)
     * @param comparator A comparator e.g. eq, nq, lt
     * @param value      the search query
     */

    public void addEqualsWhere(String attribute, String comparator,
                               String value) {
        whereList.add(attribute + translateComparator(comparator) + value);
    }

    public void addWhere(String where) {
        whereList.add(where);
    }

    public void addOrderby(String attribute, String sortOrder) {
        orderByMap.put(attribute, sortOrder);
    }

    public void addLimitby_skip(Integer skip) {
        limitOffset = skip;
    }

    public void addLimitby_top(Integer top) {
        limitHowMany = top;
    }

    public Query buildHQLStatement(Session session) {
        // take care of the select part
        StringBuffer hqlStatement = new StringBuffer(select);

        // take care of the where part
        // Coding with 'and'. Will figure out how to handle this properly later
        // We always start limiting based on logged in person, so we have to
        // add an 'and' here.
        for (String where : whereList) {
            hqlStatement.append(" and ");
            hqlStatement.append(where);
        }

        hqlStatement.append(" ");

        Query query = session.createQuery(hqlStatement.toString());

        // take care of the orderBy part
        boolean firstOrderBy = false;
        for (Map.Entry entry : orderByMap.entrySet()) {
            if (firstOrderBy) {
                hqlStatement.append(", ");
            } else {
                hqlStatement.append(" order by ");
                firstOrderBy = true;
            }
            hqlStatement.append(entry.getKey() + " " + entry.getValue());
        }

        query.setFirstResult(limitOffset);
        query.setMaxResults(limitHowMany);

        String queryString = query.getQueryString();
        out.println("HQL Query string is " + queryString);
        return query;
    }

    public void replaceParentIdWithPrimaryKey(String primaryKey) {
        select = select.replace("PARENT_ID", primaryKey);
    }


    /**
     * getNameDatabase
     * <p>
     * Helper mechanism to convert Norwegian entity / attribute names to
     * English as English is used in classes and tables. Interacting with the
     * core is done in Norwegian but things have then to be translated to
     * English naming conventions.
     * <p>
     * Note, this will return the name of the database column
     * <p>
     * If we don't have a corresponding object, we choose just to return the
     * original object. This should force a database query error and expose
     * any missing objects. This strategy is OK in development, but later we
     * need a better way of handling it.
     *
     * @param norwegianName The name in Norwegian
     * @return the English version of the Norwegian name if it exists, otherwise
     * the original Norwegian name.
     */
    protected String getNameDatabase(String norwegianName) {
        String englishName = CommonUtils.WebUtils
                .getEnglishNameDatabase(norwegianName);

        if (englishName == null)
            return norwegianName;
        else
            return englishName;
    }

    /**
     * getNameObject
     * <p>
     * Helper mechanism to convert Norwegian entity / attribute names to
     * English as English is used in classes and tables. Interacting with the
     * core is done in Norwegian but things have then to be translated to
     * English naming conventions.
     * <p>
     * Note, this will return the name of the class variable
     * <p>
     * If we don't have a corresponding object, we choose just to return the
     * original object. This should force a database query error and expose
     * any missing objects. This strategy is OK in development, but later we
     * need a better way of handling it.
     *
     * @param norwegianName The name in Norwegian
     * @return the English version of the Norwegian name if it exists, otherwise
     * the original Norwegian name.
     */
    protected String getNameObject(String norwegianName) {
        String englishName = CommonUtils.WebUtils
                .getEnglishNameObject(norwegianName);
        if (englishName == null)
            return norwegianName;
        else
            return englishName;
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
                return HQL_LE;
            case ODATA_LE:
                return HQL_LE;
            case ODATA_NE:
                return HQL_NE;
        }
        throw new NikitaMalformedInputDataException(
                "Unrecognised comparator used in OData query (" +
                        comparator +")");
    }
}
