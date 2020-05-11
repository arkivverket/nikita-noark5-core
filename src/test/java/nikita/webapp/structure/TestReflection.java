package nikita.webapp.structure;

import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.Series;
import org.junit.jupiter.api.Test;

public class TestReflection {

    @Test
    public void testReflection() throws NoSuchMethodException {
        TestReflection ref = new TestReflection();
        Fonds fonds = new Fonds();
        System.out.println("fonds: " + fonds.getForeignKeyObjectName("Series"));
        System.out.println("fonds: " + fonds.getForeignKeyObjectName("Record"));
        System.out.println("fonds PK: " + fonds.getPrimaryKey());

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
