package nikita.webapp.odata;

import java.util.ArrayList;

/**
 * SQLStatementBuilder
 * <p>
 * Handle the process of building an SQL statement. The potential statement
 * is derived for a number of parts. The select part is straight forward. The
 * where part is an ArrayList of various clauses that are currently joined
 * together with an 'and'. We need a better way to handle this. But this is
 * experimental, prototyping the solution as we go along.
 * <p>
 * Note. When implementing paging, make sure there is a sort order. Remember
 * the fetch order is unpredictable.
 */

public class SQLStatementBuilder {

    // private ArrayList <String> selectList;
    private String select;
    private ArrayList<String> whereList;
    private ArrayList<String> orderByList;
    private String limitHowMany;
    private String limitOffset;

    public SQLStatementBuilder() {
        select = "";
        whereList = new ArrayList<>();
        orderByList = new ArrayList<>();
    }

    public void addFrom(String entity, String ownerColumn, String
            loggedInUser) {
        select = "select * from " + entity + " where " + ownerColumn + " ='" +
                loggedInUser + "'";
    }

    public void addWhere(String where) {
        whereList.add(where);
    }

    public void addOrderBy(String attribute, String sortOrder) {
        orderByList.add(attribute + " " + sortOrder);
    }

    public void addLimitby_skip(Integer skip) {
        limitOffset = skip.toString();
    }

    public void addLimitby_top(Integer top) {
        limitHowMany = top.toString();
    }

    public String buildSQLStatement() {

        // take care of the select part
        StringBuffer sqlStatement = new StringBuffer(select);

        // take care of the where part
        // Coding with 'and'. Will figure out how to handle this properly later
        // We always start filtering based on logged in person, so we have to
        // add an 'and' here.
        for (String where : whereList) {
            sqlStatement.append(" and ");
            sqlStatement.append(where);
        }

        sqlStatement.append(" ");

        // take care of the orderBy part
        boolean firstOrderBy = false;
        for (String orderBy : orderByList) {
            if (firstOrderBy) {
                sqlStatement.append(", ");
            } else {
                sqlStatement.append(" order by ");
                firstOrderBy = true;
            }
            sqlStatement.append(orderBy);
        }

        sqlStatement.append(" ");

        // take care of the limit part
        if (limitOffset != null) {
            sqlStatement.append(" LIMIT " + limitOffset);
            if (limitHowMany != null) {
                sqlStatement.append(", " + limitHowMany);
            }
        } else if (limitHowMany != null) {
            sqlStatement.append(" LIMIT " + limitHowMany);
        }

        return sqlStatement.toString();
    }
}
