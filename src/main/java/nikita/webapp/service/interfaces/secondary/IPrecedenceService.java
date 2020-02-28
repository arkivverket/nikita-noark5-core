package nikita.webapp.service.interfaces.secondary;

import nikita.common.model.noark5.v5.hateoas.secondary.PrecedenceHateoas;

import nikita.common.model.noark5.v5.secondary.Precedence;

public interface IPrecedenceService {

    PrecedenceHateoas updatePrecedenceBySystemId
        (String systemId, Long version, Precedence incomingPrecedence);

    PrecedenceHateoas createNewPrecedence(Precedence entity);

    void deletePrecedenceBySystemId(String systemID);

    PrecedenceHateoas findAllByOwner();

    PrecedenceHateoas findBySystemId(String precedenceSystemId);

    PrecedenceHateoas generateDefaultPrecedence();
}
