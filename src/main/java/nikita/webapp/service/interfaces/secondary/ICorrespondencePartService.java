package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v4.casehandling.secondary.CorrespondencePartInternal;
import nikita.common.model.noark5.v4.casehandling.secondary.CorrespondencePartPerson;
import nikita.common.model.noark5.v4.casehandling.secondary.CorrespondencePartUnit;

import javax.validation.constraints.NotNull;

public interface ICorrespondencePartService {

    CorrespondencePartPerson updateCorrespondencePartPerson(
            String systemId, Long version,
            CorrespondencePartPerson incomingCorrespondencePart);

    CorrespondencePartUnit updateCorrespondencePartUnit(
            @NotNull String systemId, @NotNull Long version,
            @NotNull CorrespondencePartUnit incomingCorrespondencePart);

    CorrespondencePartInternal updateCorrespondencePartInternal(
            @NotNull String systemId, @NotNull Long version,
            @NotNull CorrespondencePartInternal incomingCorrespondencePart);

    CorrespondencePartUnit createNewCorrespondencePartUnit(
            @NotNull CorrespondencePartUnit correspondencePartUnit);

    CorrespondencePartInternal createNewCorrespondencePartInternal(
            @NotNull CorrespondencePartInternal correspondencePartUnit);

    CorrespondencePartPerson createNewCorrespondencePartPerson(
            @NotNull CorrespondencePartPerson correspondencePartPerson);

    void deleteCorrespondencePartUnit(@NotNull String code);

    void deleteCorrespondencePartPerson(@NotNull String code);

    void deleteCorrespondencePartInternal(@NotNull String code);

}
