package nikita.webapp.odata;


import org.antlr.v4.runtime.RecognitionException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class ODataIT {

    @Test
    public void testNewline() throws IOException, RecognitionException {


        String fileName = "odata/odata_samples.txt";

        InputStream fileStream = getClass().getClassLoader().getResourceAsStream(fileName);
/*
        ODataLexer lexer = new ODataLexer(
                CharStreams.fromStream(fileStream));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ODataParser parser = new ODataParser(tokens);
        ParseTree tree = parser.odataURL();
        ParseTreeWalker walker = new ParseTreeWalker();

        // Make the SQL Statement
        NikitaODataToSQLWalker sqlWalker = new NikitaODataToSQLWalker();
        walker.walk(sqlWalker, tree);
        System.out.println(sqlWalker.getSqlStatement());

 */
    }
}
