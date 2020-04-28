package nikita.webapp.odata.model;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Methods like addValue, addAttribute, addComparator (that call addWithSpace)
 * are there for readability. We may decide to replace them with addWithSpace
 * later, but currently use them to make things a little clearer during
 * development.
 */

public class HQLStatement {

    private final String CONTAINS_PARAMETER = "containsParameter_";
    private final String STARTS_WITH_PARAMETER = "startsWithParameter_";
    private final String ENDS_WITH_PARAMETER = "endsWithParameter_";
    // Parameter values
    private final Map<String, String> startsWithParameters = new HashMap<>();
    private final Map<String, String> endsWithParameters = new HashMap<>();
    private final Map<String, String> containsParameters = new HashMap<>();
    // query options
    // List of order by options
    private final Map<String, String> orderByMap = new HashMap<>();
    private StringBuilder hqlStatement = new StringBuilder();
    // Foreign key reference
    private String parentSystemId = "";
    // Setting a hard limit of 1000 unless overridden
    private AtomicInteger limitHowMany = new AtomicInteger(1000);
    // Always start at offset 0 unless overridden
    private AtomicInteger limitOffset = new AtomicInteger(0);

    public HQLStatement() {
    }

    /**
     * Construct HQLStatement object identifying the statement type
     *
     * @param statementType Can be delete. insert is not required
     */
    public HQLStatement(String statementType) {
        hqlStatement.append(statementType);
        if (!statementType.isEmpty()) {
            hqlStatement.append(" ");
        }
    }

    public void addFrom(String entity) {
        hqlStatement.append("from ");
        hqlStatement.append(entity);
        hqlStatement.append("where ");
    }

    public void addFromWithForeignKey(String parentEntity, String entity,
                                      String systemId) {
        hqlStatement.append("from ");
        hqlStatement.append(entity);
        hqlStatement.append("where reference");
        hqlStatement.append(parentEntity);
        hqlStatement.append(" = :parentSystemId)");
        hqlStatement.append("'");
        parentSystemId = systemId;
    }

    public void addStartsWith(String attribute, String value) {
        String parameter = STARTS_WITH_PARAMETER + startsWithParameters.size();
        value = desanitiseValue(value);
        startsWithParameters.put(parameter, value);
        addAttribute(attribute);
        addComparator("like");
        addValue(":" + parameter);
    }

    public void addEndsWith(String attribute, String value) {
        String parameter = ENDS_WITH_PARAMETER + endsWithParameters.size();
        value = desanitiseValue(value);
        endsWithParameters.put(parameter, value);
        addAttribute(attribute);
        addComparator("like");
        addValue(":" + parameter);
    }

    public void addContains(String attribute, String value) {
        String parameter = CONTAINS_PARAMETER + containsParameters.size();
        value = desanitiseValue(value);
        containsParameters.put(parameter, value);
        addAttribute(attribute);
        addComparator("like");
        addValue(":" + parameter);
    }

    public void addLeftBracket() {
        hqlStatement.append("( ");
    }

    public void addRightBracket() {
        hqlStatement.append(" ) ");
    }

    public void addOr() {
        hqlStatement.append("or ");
    }

    public void addAnd() {
        hqlStatement.append("or ");
    }

    public void addIsNull() {
        hqlStatement.append("is null ");
    }

    public void addIsNotNull() {
        hqlStatement.append("is not null ");
    }

    public void addValue(String value) {
        addWithSpace(value);
    }

    public void addAttribute(String attribute) {
        addWithSpace(attribute);
    }

    public void addComparator(String comparator) {
        addWithSpace(comparator);
    }

    private void addWithSpace(String value) {
        hqlStatement.append(value);
        hqlStatement.append(" ");
    }

    // Methods relating to query options top, skip, orderby
    public void setLimitHowMany(AtomicInteger limitHowMany) {
        this.limitHowMany = limitHowMany;
    }

    public void setLimitOffset(AtomicInteger limitOffset) {
        this.limitOffset = limitOffset;
    }

    public void addOrderBy(String attribute, String sortOrder) {
        orderByMap.put(attribute, sortOrder);
    }

    // Methods relating to generating and returning the query
    // Remove the trailing space if it is there
    private String processQuery() {
        return hqlStatement.toString().stripTrailing();
    }

    public String getQuery() {
        return processQuery();
    }

    public Query getQuery(Session session) {
        Query query = session.createQuery(processQuery());

        if (!parentSystemId.isEmpty()) {
            query.setParameter("parentId", parentSystemId);
        }

        // Resolve all startsWith parameters
        for (Map.Entry entry : startsWithParameters.entrySet()) {
            query.setParameter(entry.getKey().toString(),
                    entry.getValue() + "%");
        }

        // Resolve all contains parameters
        //set :containsParameter_0 should be containsParameter_0
        for (Map.Entry entry : containsParameters.entrySet()) {
            query.setParameter(entry.getKey().toString(),
                    "%" + entry.getValue() + "%");
        }

        // Resolve all endsWith parameters
        for (Map.Entry entry : endsWithParameters.entrySet()) {
            query.setParameter(entry.getKey().toString(),
                    "%" + entry.getValue());
        }

        if (orderByMap.size() > 0) {
            hqlStatement.append(" order by ");
            StringJoiner join = new StringJoiner(",");
            for (Map.Entry entry : orderByMap.entrySet()) {
                join.add(entry.getKey() + " " + entry.getValue());
            }
            hqlStatement.append(join.toString());
        }

        query.setFirstResult(limitOffset.get());
        query.setMaxResults(limitHowMany.get());
        return query;
    }

    protected String desanitiseValue(String value) {
        if (quotedString(value)) {
            value = dequote(value);
        }
        return unescape(value);
    }

    private String unescape(String original) {
        String unescaped = original.replaceAll("\'\'", "'");
        return unescaped;
    }

    private boolean quotedString(String value) {
        if ((value.charAt(0) == '\'' &&
                value.charAt(value.length() - 1) == '\'')) {
            return true;
        }
        return false;
    }

    private String dequote(String original) {
        if (original != null && original.length() > 1) {
            if (quotedString(original)) {
                return original.substring(1, original.length() - 1);
            }
        }
        return original;
    }
}
