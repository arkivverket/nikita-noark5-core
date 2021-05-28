package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v5.admin.AdministrativeUnit;
import nikita.common.model.noark5.v5.casehandling.SequenceNumberGenerator;

import javax.validation.constraints.NotNull;

public interface ISequenceNumberGeneratorService {
    Integer getNextCaseFileSequenceNumber(
            @NotNull final AdministrativeUnit administrativeUnit);

    Integer getNextRecordSequenceNumber(
            @NotNull final AdministrativeUnit administrativeUnit);

    SequenceNumberGenerator createSequenceNumberGenerator(
            @NotNull final AdministrativeUnit administrativeUnit);
}
