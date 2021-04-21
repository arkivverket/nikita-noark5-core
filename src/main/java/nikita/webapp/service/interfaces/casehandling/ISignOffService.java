package nikita.webapp.service.interfaces.casehandling;

import nikita.common.model.noark5.v5.hateoas.secondary.SignOffHateoas;
import nikita.common.model.noark5.v5.secondary.SignOff;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface ISignOffService {

    // All find methods
    ResponseEntity<SignOffHateoas> findBySystemId(
            @NotNull final UUID systemId);

    ResponseEntity<SignOffHateoas> updateSignOff(
            @NotNull UUID signOffSystemId, @NotNull SignOff signOff);

    void deleteSignOff(@NotNull UUID signOffSystemId);
}
