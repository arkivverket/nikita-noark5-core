package nikita.common.model.noark5.v5.casehandling;

import nikita.common.model.noark5.v5.interfaces.entities.ICaseFileEntity;

/**
 * A lot of methods are implemented, just to be in compliance with the
 * ICaseFileEntity interfaces. They will never have a meaningful
 * implementation in this class as the class is just a dummy place holder so
 * the serialiser can create a proper CaseFile like payload.
 * <p>
 * The need for this class in the codebase is to be able to provide only a
 * subset of the CaseFile attributes. The attributes belonging to File should
 * not be included when serialising.
 */
public class CaseFileExpansion
        extends CaseFile
        implements ICaseFileEntity {

    public CaseFileExpansion() {
    }
}
