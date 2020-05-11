package nikita.webapp.odata.model;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Methods like addValue, addAttribute, addComparator (that call addWithSpace)
 * are there for readability. We may decide to replace them with addWithSpace
 * later, but currently use them to make things a little clearer during
 * development.
 * <p>
 * <p>
 * Some consideration of note:
 * Avoid problem when alias of entity is a reserved HQL keyword e.g "class" or
 * "order".  To achieve this a "_1" is added to the alias of an an entity
 * entity.toLowerCase()+"_1".
 */
public class HQLStatement {

    private final String CONTAINS_PARAMETER = "containsParameter_";
    private final String COMPARISON_PARAMETER = "comparisonParameter_";
    private final String STARTS_WITH_PARAMETER = "startsWithParameter_";
    private final String ENDS_WITH_PARAMETER = "endsWithParameter_";
    // Parameter values
    private final Map<String, String> startsWithParameters = new HashMap<>();
    private final Map<String, String> endsWithParameters = new HashMap<>();
    private final Map<String, String> containsParameters = new HashMap<>();
    private final Map<String, Object> comparisonParameters = new HashMap<>();
    // query options
    // List of order by options
    private final Map<String, String> orderByMap = new HashMap<>();

    private StringBuilder select = new StringBuilder();
    private StringBuilder from = new StringBuilder();
    private StringBuilder where = new StringBuilder();
    private StringBuilder orderby = new StringBuilder();

    // Foreign key reference
    private String parentSystemId = "";
    // Setting a hard limit of 1000 unless overridden
    private AtomicInteger limitHowMany = new AtomicInteger(1000);
    // Always start at offset 0 unless overridden
    private AtomicInteger limitOffset = new AtomicInteger(0);

    private StringBuilder ingress = new StringBuilder();
    private Set<String> entityList = new TreeSet<>();

    private String entity = "";
    private String entityAlias = "";

    private boolean usesJoin = false;
    private boolean usesWhere = false;

    public HQLStatement() {
    }

    /**
     * Construct HQLStatement object identifying the statement type
     *
     * @param statementType Can be delete. insert is not required
     */
    public HQLStatement(String statementType) {
        if (!statementType.isEmpty()) {
            select.append(statementType);
            select.append(" ");
        }
    }

    public void addQueryEntity(String entity) {
        this.entity = entity;
        this.entityAlias = entity.toLowerCase() + "_1";
    }

    public void addFromEntity(String entityAndAlias) {
        entityList.add(entityAndAlias);
    }

    public void addWhat(String what) {
        ingress.append(what);
    }

    public void addFrom(String entity) {
        this.entity = entity;
        entityList.add(entity + " AS " + entity.toLowerCase() + "_1 ");
    }

    public void addWhere() {
        where.append("WHERE ");
    }

    public void addFromWithForeignKey(String parentEntity, String entity,
                                      String systemId) {
        where.append(" where reference");
        where.append(parentEntity);
        where.append(" = :parentSystemId)");
        where.append("'");
        parentSystemId = systemId;
        this.entity = entity;
    }

    public void addStartsWith(String entityAlias, String attribute,
                              String value) {
        String parameter = STARTS_WITH_PARAMETER + startsWithParameters.size();
        value = desanitiseValue(value);
        startsWithParameters.put(parameter, value);
        addAttribute(entityAlias + "." + attribute);
        addComparator("like");
        addValue(":" + parameter);
    }

    public void addEndsWith(String entityAlias, String attribute,
                            String value) {
        String parameter = ENDS_WITH_PARAMETER + endsWithParameters.size();
        value = desanitiseValue(value);
        endsWithParameters.put(parameter, value);
        addAttribute(entityAlias + "." + attribute);
        addComparator("like");
        addValue(":" + parameter);
    }

    public void addContains(String entityAlias, String attribute,
                            String value) {
        String parameter = CONTAINS_PARAMETER + containsParameters.size();
        value = desanitiseValue(value);
        containsParameters.put(parameter, value);
        addAttribute(entityAlias + "." + attribute);
        addComparator("like");
        addValue(":" + parameter);
    }

    public void addCompareValue(String aliasAndAttribute, String comparator,
                                String value) {
        String parameter = COMPARISON_PARAMETER + containsParameters.size();
        comparisonParameters.put(parameter, desanitiseValue(value));
        addAttribute(aliasAndAttribute);
        addComparator(comparator);
        addValue(":" + parameter);
    }

    /**
     * add a function to the where clause
     * <p>
     * Note the aliasAndAttribute parameter contains both the alias as well
     * as the attribute with a "." between them
     *
     * @param function          function to use e.g. year or month
     * @param aliasAndAttribute e.g file.createdDate
     * @param comparator        The comparator operator
     * @param value             The value to compare against
     */
    public void addCompareValue(String function, String aliasAndAttribute,
                                String comparator, String value) {
        String parameter = COMPARISON_PARAMETER + comparisonParameters.size();
        comparisonParameters.put(parameter, Integer.parseInt(value));
        where.append(function);
        where.append("(");
        where.append(aliasAndAttribute);
        where.append(") ");
        addComparator(comparator);
        addValue(":" + parameter);
    }

    public void addLeftBracket() {
        where.append("( ");
    }

    public void addRightBracket() {
        where.append(" ) ");
    }

    public void addOr() {
        where.append("or ");
    }

    public void addAnd() {
        where.append("and ");
    }

    public void addIsNull() {
        where.append("is null ");
    }


    public void addIsNotNull() {
        where.append("is not null ");
    }

    private void addValue(String value) {
        addWithSpace(value);
    }

    public void addAttribute(String attribute) {
        addWithSpace(attribute);
    }

    public void addAliasAndAttribute(String entityAlias, String attribute) {
        addWithSpace(entityAlias + "." + attribute);
    }

    public void addComparator(String comparator) {
        addWithSpace(comparator);
    }

    public void addCompare(String aliasAndAttribute, String comparator,
                           String value) {
        String parameter = COMPARISON_PARAMETER + containsParameters.size();
        comparisonParameters.put(parameter, desanitiseValue(value));
        addAttribute(aliasAndAttribute);
        addComparator(comparator);
        addValue(":" + parameter);
    }

    private void addWithSpace(String value) {
        where.append(value);
        where.append(" ");
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

        // Add orderBy values
        if (orderByMap.size() > 0) {
            StringJoiner join = new StringJoiner(",");
            for (Map.Entry entry : orderByMap.entrySet()) {
                join.add(entry.getKey() + " " + entry.getValue());
            }
            orderby.append(join.toString());
        }

        // Add list of entities in query
        if (entityList.size() > 0) {
            StringJoiner join = new StringJoiner(",");
            Iterator<String> it = entityList.iterator();
            while (it.hasNext()) {
                join.add(it.next());
            }
            from.insert(0, join.toString() + " ");
        }

        String query = select.toString() + " FROM " +
                this.entity + " AS " + this.entity.toLowerCase() + "_1 "
                + from.toString();

        if (where.length() > 0) {
            query += "WHERE " + where.toString();
        }
        if (orderby.length() > 0) {
            query += orderby.toString();
        }

        return query.stripTrailing().stripLeading();
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

        // Resolve all startsWith parameters
        for (Map.Entry entry : comparisonParameters.entrySet()) {
            query.setParameter(entry.getKey().toString(), entry.getValue());
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


        // BREAKPOINT COMMENT : Check from is actually prepended
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

    public void addEntityToEntityJoin(String fromEntity, String foreignKey,
                                      String toEntity) {

        //"JOIN file_1.referenceClass AS class_1"
        from.append("JOIN ");
        from.append(fromEntity.toLowerCase() + "_1");
        from.append(".");
        from.append(foreignKey);
        from.append(" AS ");
        from.append(toEntity.toLowerCase() + "_1");
        from.append(" ");

        /*
        from.append(fromEntity.toLowerCase() + "_1");
        from.append(".");
        from.append(foreignKey);
        from.append(" = ");
        from.append(toEntity.toLowerCase() + "_1");
        from.append(".");
        from.append(primaryKey);
        from.append(" ");
        */
    }
}
