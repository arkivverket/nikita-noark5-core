package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.casehandling.CaseFile;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkGeneralEntity;
import nikita.common.model.noark5.v5.metadata.PrecedenceStatus;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Created by tsodring on 1/16/17.
 */
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

    List<RegistryEntry> getReferenceRegistryEntry();

    void setReferenceRegistryEntry(List<RegistryEntry> referenceRegistryEntry);

    List<CaseFile> getReferenceCaseFile();

    void setReferenceCaseFile(List<CaseFile> referenceCaseFile);
}
