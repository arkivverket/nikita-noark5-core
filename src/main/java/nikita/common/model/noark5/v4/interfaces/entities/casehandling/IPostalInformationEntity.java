package nikita.common.model.noark5.v4.interfaces.entities.casehandling;


import nikita.common.model.noark5.v4.casehandling.secondary.ContactInformation;

/**
 * Created by tsodring on 5/22/17.
 */
public interface IPostalInformationEntity {

    String getEmailAddress();

    void setEmailAddress(String emailAddress);

    String getMobileTelephoneNumber();

    void setMobileTelephoneNumber(String mobileTelephoneNumber);

    String getTelephoneNumber();

    void setTelephoneNumber(String telephoneNumber);

    ContactInformation getContactInformation();

    void setContactInformation(ContactInformation contactInformation);
}
