package nikita.common.model.noark5.v5.interfaces.entities;

import java.io.Serializable;

/**
 * Created by tsodring on 1/16/17.
 */
public interface IPartEntity extends Serializable {

    String getPartId();

    void setPartId(String partId);

    void setPartName(String partName);

    String getPartRole();

    void setPartRole(String partRole);

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
