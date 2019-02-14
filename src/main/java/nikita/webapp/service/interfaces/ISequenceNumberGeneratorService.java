package nikita.webapp.service.interfaces;

import nikita.common.model.noark5.v4.admin.AdministrativeUnit;
import nikita.common.model.noark5.v4.casehandling.SequenceNumberGenerator;

import javax.validation.constraints.NotNull;

public interface ISequenceNumberGeneratorService {
    Integer getNextSequenceNumber(
            @NotNull AdministrativeUnit administrativeUnit);

    SequenceNumberGenerator createSequenceNumberGenerator(
            @NotNull AdministrativeUnit administrativeUnit);
}
