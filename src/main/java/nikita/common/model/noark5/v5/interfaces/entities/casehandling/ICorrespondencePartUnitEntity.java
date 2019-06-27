package nikita.common.model.noark5.v5.interfaces.entities.casehandling;

import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.casehandling.secondary.ContactInformation;

import java.util.List;

/**
 * Created by tsodring on 5/22/17.
 */
public interface ICorrespondencePartUnitEntity
        extends IPostalAddress, IBusinessAddress,
        IContactInformation, ICorrespondencePartEntity {

    String getOrganisationNumber();

    void setOrganisationNumber(String organisationNumber);

    String getName();

    void setName(String name);

    ContactInformation getContactInformation();

    void setContactInformation(ContactInformation contactInformation);

    String getContactPerson();

    void setContactPerson(String contactPerson);

    List<RegistryEntry> getReferenceRegistryEntry();

    void setReferenceRegistryEntry(List<RegistryEntry> referenceRegistryEntry);

    void addRegistryEntry(RegistryEntry registryEntry);
}
