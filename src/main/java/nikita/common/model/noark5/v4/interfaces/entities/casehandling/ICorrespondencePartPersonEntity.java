package nikita.common.model.noark5.v4.interfaces.entities.casehandling;

import nikita.common.model.noark5.v4.casehandling.RegistryEntry;

import java.util.List;

/**
 * Created by tsodring on 5/22/17.
 */
public interface ICorrespondencePartPersonEntity
        extends IPostalAddress, IContactInformation, IResidingAddress,
        ICorrespondencePartEntity {

    String getSocialSecurityNumber();

    void setSocialSecurityNumber(String socialSecurityNumber);

    String getdNumber();

    void setdNumber(String dNumber);

    String getName();

    void setName(String name);

    List<RegistryEntry> getReferenceRegistryEntry();

    void setReferenceRegistryEntry(List<RegistryEntry> referenceRegistryEntry);

    void addRegistryEntry(RegistryEntry registryEntry);
}
