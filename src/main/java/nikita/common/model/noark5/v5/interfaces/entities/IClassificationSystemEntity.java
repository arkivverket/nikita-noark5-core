package nikita.common.model.noark5.v5.interfaces.entities;

import nikita.common.model.noark5.v5.Class;
import nikita.common.model.noark5.v5.Series;
import nikita.common.model.noark5.v5.metadata.ClassificationType;

import java.util.Set;

public interface IClassificationSystemEntity
        extends INoarkGeneralEntity {
    ClassificationType getClassificationType();

    void setClassificationType(ClassificationType classificationType);

    Set<Series> getReferenceSeries();

    void addSeries(Series series);

    void removeSeries(Series series);

    Set<Class> getReferenceClass();

    void addClass(Class klass);

    void removeClass(Class klass);
}
