package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.interfaces.entities.INoarkGeneralEntity;

import java.util.List;

/**
 * Created by tsodring
 */
public interface IClassificationSystemEntity
        extends INoarkGeneralEntity
{

    String getClassificationTypeCode();

    void setClassificationTypeCode(String classificationTypeCode);

    String getClassificationTypeCodeName();

    void setClassificationTypeCodeName(String classificationTypeCodeName);

    List<Series> getReferenceSeries();

    void setReferenceSeries(List<Series> referenceSeries);

    List<Class> getReferenceClass();

    void setReferenceClass(List<Class> referenceClass);

}
