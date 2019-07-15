package nikita.common.model.noark5.v5.interfaces.entities.secondary;


import nikita.common.model.noark5.v5.Record;

import java.util.List;

public interface IGenericPersonEntity
        extends IPostalAddress, IContactInformation, IResidingAddress {

    String getSocialSecurityNumber();

    void setSocialSecurityNumber(String socialSecurityNumber);

    String getdNumber();

    void setdNumber(String dNumber);

    String getName();

    void setName(String name);

    List<Record> getReferenceRecord();

    void setReferenceRecord(List<Record> referenceRecord);

    void addRecord(Record record);

}
