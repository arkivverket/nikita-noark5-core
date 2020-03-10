package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkGeneralEntity;
import nikita.common.model.noark5.v5.metadata.ClassificationType;

import java.util.List;

/**
 * Created by tsodring
 */
public interface IClassificationSystemEntity
        extends INoarkGeneralEntity
{

    ClassificationType getClassificationType();

    void setClassificationType(ClassificationType classificationType);

    List<Series> getReferenceSeries();

    void setReferenceSeries(List<Series> referenceSeries);

    List<Class> getReferenceClass();

    void setReferenceClass(List<Class> referenceClass);

}
