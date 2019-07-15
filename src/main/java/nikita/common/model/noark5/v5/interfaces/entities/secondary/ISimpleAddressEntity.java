package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.casehandling.secondary.PostalNumber;

/**
 * Created by tsodring on 5/22/17.
 */
public interface ISimpleAddressEntity {

    String getAddressType();

    void setAddressType(String addressType);

    String getAddressLine1();

    void setAddressLine1(String addressLine1);

    String getAddressLine2();

    void setAddressLine2(String addressLine2);

    String getAddressLine3();

    void setAddressLine3(String addressLine3);

    PostalNumber getPostalNumber();

    void setPostalNumber(PostalNumber postalNumber);

    String getPostalTown();

    void setPostalTown(String postalTown);

    String getCountryCode();

    void setCountryCode(String countryCode);

}
