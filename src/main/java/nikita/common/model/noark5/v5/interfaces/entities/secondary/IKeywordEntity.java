package nikita.common.model.noark5.v5.interfaces.entities.secondary;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.RecordEntity;
import nikita.common.model.noark5.v5.interfaces.entities.ISystemId;

import java.util.Set;

public interface IKeywordEntity
        extends ISystemId {

    String getKeyword();

    void setKeyword(String keyword);

    Set<RecordEntity> getReferenceRecordEntity();

    void addReferenceRecord(RecordEntity referenceRecordEntity);

    void removeReferenceRecord(RecordEntity referenceRecordEntity);

    Set<File> getReferenceFile();

    void addReferenceFile(File referenceFile);

    void removeReferenceFile(File referenceFile);

    Set<Class> getReferenceClass();

    void addReferenceClass(Class referenceClass);

    void removeReferenceClass(Class referenceClass);
}
