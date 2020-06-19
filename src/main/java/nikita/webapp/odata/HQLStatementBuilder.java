package nikita.webapp.odata;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Some consideration of note:
 * Avoid problem when alias of entity is a reserved HQL keyword e.g "class" or
 * "order".  To achieve this a "_1" is added to the alias of an an entity
 * entity.toLowerCase()+"_1".
 */
public class HQLStatementBuilder {

    private final String PARAMETER = "parameter_";
    private final Map<String, Object> parameters = new HashMap<>();
    private final List<String> orderByList = new ArrayList<>();
    private final StringBuilder select = new StringBuilder();
    private final StringBuilder from = new StringBuilder();
    private final StringBuilder where = new StringBuilder();
    private final StringBuilder orderBy = new StringBuilder();
    private final Set<String> entityList = new TreeSet<>();

    // Setting a hard limit of 1000 unless overridden
    private AtomicInteger limitHowMany = new AtomicInteger(1000);
    // Always start at offset 0 unless overridden
    private AtomicInteger limitOffset = new AtomicInteger(0);
    private String fromEntity = "";
    private String fromEntityAlias = "";
    private Boolean selectCount = false;
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
            from.insert(0, join.toString() + " ");
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

        String query = select.toString() + " FROM " + this.fromEntity + " AS " +
                this.fromEntityAlias + " " + from.toString();

        if (where.length() > 0) {
            query += "WHERE " + where.toString();
        }
        if (orderBy.length() > 0) {
            query += orderBy.toString();
        }

        return query.stripTrailing().stripLeading();
    }

    public String getQuery() {
        return processQuery().stripTrailing();
    }

    public Query getQuery(Session session) {
        Query query = session.createQuery(processQuery());

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
}
