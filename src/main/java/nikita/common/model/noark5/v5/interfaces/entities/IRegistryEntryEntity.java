package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.interfaces.IElectronicSignature;
import nikita.common.model.noark5.v5.interfaces.IPrecedence;
import nikita.common.model.noark5.v5.interfaces.ISignOff;
import nikita.common.model.noark5.v5.metadata.RegistryEntryStatus;
import nikita.common.model.noark5.v5.metadata.RegistryEntryType;

import java.time.OffsetDateTime;

public interface IRegistryEntryEntity extends IRecordEntity, IRecordNoteEntity,
        IElectronicSignature, IPrecedence, ISignOff {

    Integer getRecordYear();

    void setRecordYear(Integer recordYear);

    Integer getRecordSequenceNumber();

    void setRecordSequenceNumber(Integer recordSequenceNumber);

    Integer getRegistryEntryNumber();

    void setRegistryEntryNumber(Integer registryEntryNumber);

    RegistryEntryType getRegistryEntryType();

    void setRegistryEntryType(RegistryEntryType registryEntryType);

    RegistryEntryStatus getRegistryEntryStatus();

    void setRegistryEntryStatus(RegistryEntryStatus registryEntryStatus);

    OffsetDateTime getRecordDate();

    void setRecordDate(OffsetDateTime recordDate);

    String getRecordsManagementUnit();

    void setRecordsManagementUnit(String recordsManagementUnit);

}
