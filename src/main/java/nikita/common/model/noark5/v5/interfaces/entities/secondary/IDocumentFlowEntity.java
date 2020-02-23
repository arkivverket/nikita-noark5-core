package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.admin.User;
import nikita.common.model.noark5.v5.casehandling.RecordNote;
import nikita.common.model.noark5.v5.casehandling.RegistryEntry;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkEntity;
import nikita.common.model.noark5.v5.metadata.FlowStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface IDocumentFlowEntity
        extends INoarkEntity {

    String getFlowTo();
    void setFlowTo(String flowTo);

    UUID getReferenceFlowToSystemID();
    void setReferenceFlowToSystemID(UUID referenceFlowToSystemID);
    User getReferenceFlowTo();
    void setReferenceFlowTo(User referenceFlowTo);

    String getFlowFrom();
    void setFlowFrom(String flowFrom);

    UUID getReferenceFlowFromSystemID();
    void setReferenceFlowFromSystemID(UUID referenceFlowFromSystemID);
    User getReferenceFlowFrom();
    void setReferenceFlowFrom(User referenceFlowFrom);

    OffsetDateTime getFlowReceivedDate();
    void setFlowReceivedDate(OffsetDateTime flowReceivedDate);

    OffsetDateTime getFlowSentDate();
    void setFlowSentDate(OffsetDateTime flowSentDate);

    FlowStatus getFlowStatus();
    void setFlowStatus(FlowStatus flowStatus);

    String getFlowComment();
    void setFlowComment(String flowComment);

    RegistryEntry getReferenceRegistryEntry();
    void setReferenceRegistryEntry(RegistryEntry referenceRegistryEntry);

    RecordNote getReferenceRecordNote();
    void setReferenceRecordNote(RecordNote referenceRecordNote);
}
