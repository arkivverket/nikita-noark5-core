package nikita.webapp.structure;

import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.Series;
import nikita.webapp.odata.AnalyseEntity;
import org.junit.jupiter.api.Test;

public class TestReflection {

    @Test
    public void testReflection() throws NoSuchMethodException {
        TestReflection ref = new TestReflection();
        Fonds fonds = new Fonds();
        AnalyseEntity analyseEntity = new AnalyseEntity();

        System.out.println("fonds: " + analyseEntity.
                getForeignKeyObjectName("Series", Fonds.class));
        System.out.println("fonds: " + analyseEntity.
                getForeignKeyObjectName("Record", Fonds.class));
        System.out.println("fonds PK: " + analyseEntity.getPrimaryKey(Fonds.class));

        Series series = new Series();
        System.out.println("series: " + series.getForeignKeyObjectName(
                "ClassificationSystem"));
        System.out.println("series: " + series.getForeignKeyObjectName("Fonds"));
        System.out.println("series: " + series.getForeignKeyObjectName("File"));
        System.out.println("series: " + series.getForeignKeyObjectName("Record"));
        System.out.println("series: " + series.getForeignKeyObjectName("Classified"));
        System.out.println("series PK: " + fonds.getPrimaryKey());
    }
}
