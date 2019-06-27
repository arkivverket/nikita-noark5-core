package nikita.common.model.noark5.v5.interfaces;

import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePartUnit;
import nikita.common.model.noark5.v5.metadata.CorrespondencePartType;

import java.util.List;

/**
 * Created by tsodring on 12/7/16.
 */

public interface ICorrespondencePart {

    List<CorrespondencePartPerson> getReferenceCorrespondencePartPerson();

    void setReferenceCorrespondencePartPerson(
            List<CorrespondencePartPerson> referenceCorrespondencePartPerson);

    List<CorrespondencePartUnit> getReferenceCorrespondencePartUnit();

    void setReferenceCorrespondencePartUnit(
            List<CorrespondencePartUnit> referenceCorrespondencePartUnit);

    List<CorrespondencePartInternal> getReferenceCorrespondencePartInternal();

    void setReferenceCorrespondencePartInternal(
            List<CorrespondencePartInternal> referenceCorrespondencePartInternal);

    CorrespondencePartType getCorrespondencePartType();

    void setCorrespondencePartType(
            CorrespondencePartType correspondencePartType);
}
