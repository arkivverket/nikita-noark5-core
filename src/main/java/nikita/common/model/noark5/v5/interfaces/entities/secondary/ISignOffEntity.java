package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import java.time.OffsetDateTime;
import java.util.List;

import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.casehandling.secondary.CorrespondencePart;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;
import nikita.common.model.noark5.v5.metadata.SignOffMethod;

// TODO check if this inheritence is ok.
public interface ISignOffEntity
        extends ISystemId {
    OffsetDateTime getSignOffDate();

    void setSignOffDate(OffsetDateTime signOffDate);

    String getSignOffBy();

    void setSignOffBy(String signOffBy);

    SignOffMethod getSignOffMethod();

    void setSignOffMethod(SignOffMethod signOffMethod);

    RegistryEntry getReferenceSignedOffRecord();

    void setReferenceSignedOffRecord
	(RegistryEntry referenceSignedOffRecord);

    CorrespondencePart getReferenceSignedOffCorrespondencePart();

    void setReferenceSignedOffCorrespondencePart
	(CorrespondencePart referenceSignedOffCorrespondencePart);

    RegistryEntry getReferenceRecord();

    void setReferenceRecord(RegistryEntry referenceRecord);

}
