package nikita.webapp.odata;

import nikita.common.model.nikita.Pair;

import java.util.HashMap;
import java.util.Map;

import static nikita.common.config.N5ResourceMappings.*;

/**
 * A Class to inherit from HQLStatementBuilderto provide the ability to
 * implement Noark specific extensions for handling the building of the HQL
 * statements.
 * An example of use is to handle the part:partPerson part:Unit referencing
 * when dealing with inheritance.
 * The importance of this class is to ensure we can keep the OData stuff
 * clean of Noark stuff and to have a place to add in the Noark specific stuff
 */
public class NoarkHQLStatementBuilder
        extends HQLStatementBuilder {

    protected final Map<String, String> inheritanceMappings = new HashMap<>();

    public NoarkHQLStatementBuilder() {
        super();
        buildInheritanceMappings();
    }

    /**
     * Construct HQLStatementBuilder object identifying the statement type
     *
     * @param statementType Can be delete. insert is not required
     */
    public NoarkHQLStatementBuilder(String statementType) {
        super(statementType);
        buildInheritanceMappings();
    }

    /**
     * Make a note of the entityName e.g., Part and the original
     * originalEntityName e.g. PartPerson, so that we can do an appropriate
     * TYPE() call in the HQL
     * <p>
     * In this subclassed method, we ensure we only add a pair if there is an
     * actual need for it.
     *
     * @param entityName         The name of the entity e.g., Part
     * @param originalEntityName The original name of the entity e.g.,
     *                           PartPerson
     */
    public void addPotentialTypeMapping(
            String entityName, String originalEntityName) {
        if (null != inheritanceMappings.get(originalEntityName)) {
            inheritanceList.add(new Pair(entityName,
                    inheritanceMappings.get(originalEntityName)));
        }
    }

    /**
     * Build a list of inheritance mappings.
     * <p>
     * Perhaps it might be a good idea to do this based on reflection, but
     * with the number of cases in Noark where this is relevant, it may be OK to
     * just have this as the chosen approach.
     */
    private void buildInheritanceMappings() {
        inheritanceMappings.put(PART_UNIT_FIELD, PART_UNIT_ENG_OBJECT);
        inheritanceMappings.put(PART_PERSON_FIELD, PART_PERSON_ENG_OBJECT);
    }
}
