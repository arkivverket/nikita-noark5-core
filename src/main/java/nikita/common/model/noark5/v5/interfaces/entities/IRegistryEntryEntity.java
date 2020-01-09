package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.interfaces.IElectronicSignature;
import nikita.common.model.noark5.v5.interfaces.IPrecedence;
import nikita.common.model.noark5.v5.interfaces.ISignOff;

import java.time.OffsetDateTime;

public interface IRegistryEntryEntity extends IRecordEntity, IRecordNoteEntity,
        IElectronicSignature, IPrecedence, ISignOff {

    Integer getRecordYear();

    void setRecordYear(Integer recordYear);

    Integer getRecordSequenceNumber();

    void setRecordSequenceNumber(Integer recordSequenceNumber);

    Integer getRegistryEntryNumber();

    void setRegistryEntryNumber(Integer registryEntryNumber);

    String getRegistryEntryTypeCode();

    void setRegistryEntryTypeCode(String registryEntryTypeCode);

    String getRegistryEntryTypeCodeName();

    void setRegistryEntryTypeCodeName(String registryEntryTypeCodeName);

    String getRecordStatusCode();

    void setRecordStatusCode(String recordStatusCode);

    String getRecordStatusCodeName();

    void setRecordStatusCodeName(String recordStatusCodeName);

    OffsetDateTime getRecordDate();

    void setRecordDate(OffsetDateTime recordDate);

    String getRecordsManagementUnit();

    void setRecordsManagementUnit(String recordsManagementUnit);

}
