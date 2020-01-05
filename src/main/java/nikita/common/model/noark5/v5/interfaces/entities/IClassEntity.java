package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.ClassificationSystem;
import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.File;
import nikita.common.model.noark5.v5.Record;
import nikita.common.model.noark5.v5.interfaces.*;

import java.util.List;

public interface IClassEntity
        extends INoarkGeneralEntity, IKeyword, IDisposal, IScreening,
        IClassified, ICrossReference {

    String getClassId();

    void setClassId(String fileId);

    ClassificationSystem getReferenceClassificationSystem();

    void setReferenceClassificationSystem(
            ClassificationSystem referenceClassificationSystem);

    Class getReferenceParentClass();

    void setReferenceParentClass(Class referenceParentClass);

    List<Class> getReferenceChildClass();

    void setReferenceChildClass(List<Class> referenceChildClass);

    List<File> getReferenceFile();

    void setReferenceFile(List<File> referenceFile);

    List<Record> getReferenceRecord();

    void setReferenceRecord(List<Record> referenceRecord);
}
