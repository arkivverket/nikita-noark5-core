package nikita.webapp.odata;

import nikita.common.model.nikita.Pair;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Some consideration of note:
 * Avoid problem when alias of entity is a reserved HQL keyword e.g "class" or
 * "order".  To achieve this a "_1" is added to the alias of an an entity
 * entity.toLowerCase()+"_1".
 */
public class HQLStatementBuilder {

    private final static String PARAMETER = "parameter_";
    private final Map<String, Object> parameters = new HashMap<>();
    protected final Map<String, String> typeMappings = new HashMap<>();
    protected final List<Pair> inheritanceList = new ArrayList<>();
    private final List<String> orderByList = new ArrayList<>();
    protected final StringBuilder select = new StringBuilder();
    private final StringBuilder from = new StringBuilder();
    private final StringBuilder where = new StringBuilder();
    private final StringBuilder orderBy = new StringBuilder();
    private final Set<String> entityList = new TreeSet<>();

    public final Map<String, StringBuilder> bsmParameters = new HashMap<>();
    // Setting a hard limit of 1000 unless overridden
    private AtomicInteger limitHowMany = new AtomicInteger(Integer.MAX_VALUE);
    // Always start at offset 0 unless overridden
    private AtomicInteger limitOffset = new AtomicInteger(0);
    private String fromEntity = "";
    private String fromEntityAlias = "";
    private Boolean selectCount = false;

    private final Pattern uuidPattern = Pattern.compile(
            "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");

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

    public void addCompareValueFunction(
            String leftFunctionNames, String leftFunctionClose,
            Object aliasAndAttribute, String comparator, Object value,
            String rightFunctionNames, String rightFunctionClose) {
        String parameter = PARAMETER + parameters.size();
        if (value instanceof String) {
            value = desanitiseValue((String) value);
            if (uuidPattern.matcher(value.toString()).matches()) {
                value = UUID.fromString(value.toString());
            }
        }
        if (value != null) {
            parameters.put(parameter, value);
        }
        where.append(leftFunctionNames);
        if (aliasAndAttribute != null) {
            where.append(aliasAndAttribute);
        }
        where.append(leftFunctionClose);
        where.append(" ");
        addComparator(comparator, value);
        if (null == value) {
            where.append("null");
        }
        where.append(rightFunctionNames);
        if (value != null) {
            where.append(":");
            where.append(parameter);
        }
        where.append(rightFunctionClose);
        where.append(" ");
    }

    public void addCompareValue(String aliasAndAttribute, String comparator,
                                Object value) {
        if (uuidPattern.matcher(value.toString()).matches()) {
            value = UUID.fromString(value.toString());
        }
        String parameter = PARAMETER + parameters.size();
        if (value instanceof String) {
            parameters.put(parameter, desanitiseValue((String) value));
        } else {
            parameters.put(parameter, value);
        }
        addAttribute(aliasAndAttribute);
        addComparator(comparator);
        addValue(":" + parameter);
    }

    private void addComparator(String comparator, Object value) {
        if (null == value) {
            if (comparator.equals("=")) {
                addComparator("is");
            } else if (comparator.equals("!=") || comparator.equals("<>")) {
                addComparator("is not");
            }
        } else {
            addComparator(comparator);
        }
    }

    public void addBracket(String bracket) {
        // For cosmetic reasons to deal with too many spaces. The HQL
        // syntax tests are a little too rigid on spaces. Later when we test on
        // database rather than syntax, we can remove this code that deletes
        // a redundant space
        if (bracket.equals(")") && where.charAt(where.length() - 1) == ' ') {
            where.delete(where.length() - 1, where.length());
        }
        where.append(bracket);
    }

    public void addLogicalOperator(String logicalOperator) {
        if (where.charAt(where.length() - 1) != ' ') {
            where.append(" ");
        }
        where.append(logicalOperator);
        where.append(" ");
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
        where.append(value);
        where.append(" ");
    }

    public void setLimitHowMany(AtomicInteger limitHowMany) {
        this.limitHowMany = limitHowMany;
    }

    public void setLimitOffset(AtomicInteger limitOffset) {
        this.limitOffset = limitOffset;
    }

    public void addOrderBy(String attribute, String sortOrder) {
        if (sortOrder.isEmpty()) {
            orderByList.add(attribute);
        } else {
            orderByList.add(attribute + " " + sortOrder);
        }
    }

    // Methods relating to generating and returning the query
    // Remove the trailing space if it is there
    private String processQuery() {

        // Add orderBy values
        if (orderByList.size() > 0) {
            orderBy.append("order by ");
            orderByList.forEach(
                    o -> {
                        orderBy.append(o);
                        orderBy.append(", ");
                    });
            if (orderBy.charAt(orderBy.length() - 1) == ' ') {
                orderBy.deleteCharAt(orderBy.length() - 1);
                orderBy.deleteCharAt(orderBy.length() - 1);
            }
        }

        // Add list of entities in query
        if (entityList.size() > 0) {
            StringJoiner join = new StringJoiner(",");
            for (String s : entityList) {
                join.add(s);
            }
            from.insert(0, join + " ");
        }

        // For JOIN queries it is important to state the entity you want to
        // retrieve, otherwise you get a List of Objects with all entities that
        // are part of the JOIN.
        if (select.length() < 1) {
            select.append("SELECT ");
            if (selectCount) {
                select.append("count(*)");
            } else {
                select.append(this.fromEntityAlias);
            }
        }

        String query = select + " FROM " + this.fromEntity + " AS " +
                this.fromEntityAlias + " " + from;

        if (where.length() > 0) {
            String whereString = where.toString();
            // Check if there is any BSM values that need to be tidied up
            for (Map.Entry<String, StringBuilder> entry : bsmParameters.entrySet()) {
                String colName = entry.getValue().toString() + "Value";
                whereString = whereString.replaceAll(entry.getKey(), colName);
            }
            query += "WHERE " + whereString;
        }

        if (inheritanceList.size() > 0) {
            // "_1) is used because addEntityToEntityJoin uses _1
            for (Pair pair : inheritanceList) {
                query += " and type(" + pair.getKey().toLowerCase()
                        + "_1) = " + pair.getValue();
            }
        }

        if (orderBy.length() > 0) {
            query += orderBy.toString();
        }

        /*
         When refactoring code so that we can use OData for sub-queries
         e.g., arkiv/systemID/arkivdel it was noticed that " and" is
         appearing at the end of the query and causing a problem with the HQL.
         Prior to this, nikita only allowed sub-queries with a filter clause
         and in that case the " and" was required. A simple fix is to remove
         this now, but the ideal solution would be to revisit the code and
         only add an " and" if it is necessary.
         */
        if (query.endsWith(" and ")) {
            query = query.substring(0, query.length() - 5);
        }
        return query.stripTrailing().stripLeading();
    }

    public String getQuery() {
        return processQuery().stripTrailing();
    }

    public Query<INoarkEntity> getQuery(Session session) {
        Query<INoarkEntity> query = (Query<INoarkEntity>)
                session.createQuery(processQuery());

        for (Map.Entry<String, Object> entry :
                parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
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

    // currently not sure how to deal with $count=true so leaving its
    // implementation out for the moment but leaving the signatures intact
    public void addCountAsResource(Boolean includeResults) {
        selectCount = true;
    }

    /**
     * Make a note of the entityName e.g., Part and the original
     * originalEntityName e.g., PartPerson, so that we can allow higher layers
     * that understand the domain model the ability fix the inheritance
     *
     * @param entityName         The name of the entity e.g., Part
     * @param originalEntityName The name of the entity e.g., PartPerson
     */
    public void addPotentialTypeMapping(
            String entityName, String originalEntityName) {
        typeMappings.put(originalEntityName, entityName);
    }

    public String getFromEntity() {
        return fromEntity;
    }
}
