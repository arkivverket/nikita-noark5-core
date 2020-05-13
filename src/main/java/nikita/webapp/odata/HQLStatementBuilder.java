package nikita.webapp.odata;

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
public class HQLStatementBuilder {

    private final String COMPARISON_PARAMETER = "comparisonParameter_";
    // Parameter values
    private final Map<String, String> startsWithParameters = new HashMap<>();
    private final Map<String, String> endsWithParameters = new HashMap<>();
    private final Map<String, String> containsParameters = new HashMap<>();
    private final Map<String, Object> comparisonParameters = new HashMap<>();

    // query options
    // List of order by options
    private final Map<String, String> orderByMap = new HashMap<>();

    private final StringBuilder select = new StringBuilder();
    private final StringBuilder from = new StringBuilder();
    private final StringBuilder where = new StringBuilder();
    private final StringBuilder orderby = new StringBuilder();
    private final Set<String> entityList = new TreeSet<>();
    // Foreign key reference
    private String parentSystemId = "";
    // Setting a hard limit of 1000 unless overridden
    private AtomicInteger limitHowMany = new AtomicInteger(1000);
    // Always start at offset 0 unless overridden
    private AtomicInteger limitOffset = new AtomicInteger(0);
    private String fromEntity = "";
    private String fromEntityAlias = "";

    public HQLStatementBuilder() {
    }

    /**
     * Construct HQLStatementBuilder object identifying the statement type
     *
     * @param statementType Can be delete. insert is not required
     */
    public HQLStatementBuilder(String statementType) {
        if (!statementType.isEmpty()) {
            select.append(statementType);
            select.append(" ");
        }
    }

    public void addQueryEntity(String fromEntity) {
        this.fromEntity = fromEntity;
        this.fromEntityAlias = fromEntity.toLowerCase() + "_1";
    }

    public void addFromWithForeignKey(String parentEntity, String fromEntity,
                                      String systemId) {
        where.append(" where reference");
        where.append(parentEntity);
        where.append(" = :parentSystemId)");
        where.append("'");
        parentSystemId = systemId;
        addQueryEntity(fromEntity);
    }

    public void addStartsWith(String entityAlias, String attribute,
                              String value) {
        String STARTS_WITH_PARAMETER = "startsWithParameter_";
        String parameter = STARTS_WITH_PARAMETER + startsWithParameters.size();
        value = desanitiseValue(value);
        startsWithParameters.put(parameter, value);
        addAttribute(entityAlias + "." + attribute);
        addComparator("like");
        addValue(":" + parameter);
    }

    public void addEndsWith(String entityAlias, String attribute,
                            String value) {
        String ENDS_WITH_PARAMETER = "endsWithParameter_";
        String parameter = ENDS_WITH_PARAMETER + endsWithParameters.size();
        value = desanitiseValue(value);
        endsWithParameters.put(parameter, value);
        addAttribute(entityAlias + "." + attribute);
        addComparator("like");
        addValue(":" + parameter);
    }

    public void addContains(String entityAlias, String attribute,
                            String value) {
        String CONTAINS_PARAMETER = "containsParameter_";
        String parameter = CONTAINS_PARAMETER + containsParameters.size();
        value = desanitiseValue(value);
        containsParameters.put(parameter, value);
        addAttribute(entityAlias + "." + attribute);
        addComparator("like");
        addValue(":" + parameter);
    }

    public void addCompareValue(String aliasAndAttribute, String comparator,
                                String value) {
        String parameter = COMPARISON_PARAMETER + comparisonParameters.size();
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

    public void addComparator(String comparator) {
        addWithSpace(comparator);
    }

    public void addCompare(String aliasAndAttribute, String comparator,
                           String value) {
        String parameter = COMPARISON_PARAMETER + comparisonParameters.size();
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
            for (Map.Entry<String, String> entry :
                    orderByMap.entrySet()) {
                join.add(entry.getKey() + " " + entry.getValue());
            }
            orderby.append(join.toString());
        }

        // Add list of entities in query
        if (entityList.size() > 0) {
            StringJoiner join = new StringJoiner(",");
            for (String s : entityList) {
                join.add(s);
            }
            from.insert(0, join.toString() + " ");
        }

        // For JOIN queries it is important to state the entity you want to
        // retrieve, otherwise you get a List of Objects with all entities that
        // are part of the JOIN.
        if (select.length() < 1) {
            select.append("SELECT ");
            select.append(fromEntityAlias);
        }

        String query = select.toString() + " FROM " + fromEntity + " AS " +
                fromEntityAlias + " " + from.toString();

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
        for (Map.Entry<String, String> entry :
                startsWithParameters.entrySet()) {
            query.setParameter(entry.getKey(),
                    entry.getValue() + "%");
        }

        // Resolve all startsWith parameters
        for (Map.Entry<String, Object> entry :
                comparisonParameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        // Resolve all contains parameters
        for (Map.Entry<String, String> entry :
                containsParameters.entrySet()) {
            query.setParameter(entry.getKey(),
                    "%" + entry.getValue() + "%");
        }

        // Resolve all endsWith parameters
        for (Map.Entry<String, String> entry :
                endsWithParameters.entrySet()) {
            query.setParameter(entry.getKey(),
                    "%" + entry.getValue());
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
        return original.replaceAll("''", "'");
    }

    private boolean quotedString(String value) {
        return value.charAt(0) == '\'' &&
                value.charAt(value.length() - 1) == '\'';
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
        from.append("JOIN ");
        from.append(fromEntity.toLowerCase());
        from.append("_1");
        from.append(".");
        from.append(foreignKey);
        from.append(" AS ");
        from.append(toEntity.toLowerCase());
        from.append("_1");
        from.append(" ");
    }
}
