package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkGeneralEntity;
import nikita.common.model.noark5.v5.metadata.PrecedenceStatus;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

public interface IPrecedenceEntity
    extends  INoarkGeneralEntity {
    OffsetDateTime getPrecedenceDate();

    void setPrecedenceDate(OffsetDateTime precedenceDate);

    String getPrecedenceAuthority();

    void setPrecedenceAuthority(String precedenceAuthority);

    String getSourceOfLaw();

    void setSourceOfLaw(String sourceOfLaw);

    OffsetDateTime getPrecedenceApprovedDate();

    void setPrecedenceApprovedDate(OffsetDateTime precedenceApprovedDate);

    String getPrecedenceApprovedBy();

    void setPrecedenceApprovedBy(String precedenceApprovedBy);

    UUID getReferencePrecedenceApprovedBySystemID();

    void setReferencePrecedenceApprovedBySystemID(UUID referenceFlowToSystemID);

    User getReferencePrecedenceApprovedBy();

    void setReferencePrecedenceApprovedBy(User referencePrecedenceApprovedBy);

    PrecedenceStatus getPrecedenceStatus();

    void setPrecedenceStatus(PrecedenceStatus precedenceStatus);

    Set<RegistryEntry> getReferenceRegistryEntry();

    void addRegistryEntry(RegistryEntry registryEntry);

    Set<CaseFile> getReferenceCaseFile();

    void addCaseFile(CaseFile caseFile);
}
