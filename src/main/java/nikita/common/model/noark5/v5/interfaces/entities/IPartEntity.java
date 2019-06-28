package nikita.common.model.noark5.v5.interfaces.entities;

import java.io.Serializable;

/**
 * Created by tsodring on 1/16/17.
 */
public interface IPartEntity extends Serializable {

    String getPartyId();

    void setPartyId(String partyId);

    void setPartyName(String partyName);

    String getPartyRole();

    void setPartyRole(String partyRole);

    String getPostalAddress();

    void setPostalAddress(String postalAddress);

    String getPostCode();

    void setPostCode(String postCode);

    String getPostalTown();

    void setPostalTown(String postalTown);

    String getForeignAddress();

    void setForeignAddress(String foreignAddress);

    String getEmailAddress();

    void setEmailAddress(String emailAddress);

    String getTelephoneNumber();

    void setTelephoneNumber(String telephoneNumber);

    String getContactPerson();

    void setContactPerson(String contactPerson);

}
