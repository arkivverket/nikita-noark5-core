package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.casehandling.secondary.ContactInformation;

/**
 * Created by tsodring on 5/22/17.
 */
public interface IGenericUnitEntity
        extends IPostalAddress, IBusinessAddress, IContactInformation {

    String getOrganisationNumber();

    void setOrganisationNumber(String organisationNumber);

    String getName();

    void setName(String name);

    ContactInformation getContactInformation();

    void setContactInformation(ContactInformation contactInformation);

    String getContactPerson();

    void setContactPerson(String contactPerson);

}
