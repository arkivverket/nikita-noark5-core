package nikita.common.model.noark5.v5.interfaces.entities.secondary;

public interface IGenericPersonEntity
        extends IPostalAddress, IContactInformation, IResidingAddress {

    String getSocialSecurityNumber();

    void setSocialSecurityNumber(String socialSecurityNumber);

    String getdNumber();

    void setdNumber(String dNumber);

    String getName();

    void setName(String name);
}
