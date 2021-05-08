package utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.secondary.Keyword;

import java.io.IOException;
import java.io.StringWriter;

import static nikita.common.config.N5ResourceMappings.KEYWORD;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printNullable;
import static utils.TestConstants.KEYWORD_TEST;
import static utils.TestConstants.KEYWORD_TEST_UPDATED;

public final class KeywordCreator {

    /**
     * Create a default Keyword for testing purposes
     *
     * @return a Keyword with all values set
     */
    public static Keyword createKeyword() {
        Keyword keyword = new Keyword();
        keyword.setKeyword(KEYWORD_TEST);
        return keyword;
    }

    public static String createKeywordAsJSON() throws IOException {
        return createKeywordAsJSON(createKeyword());
    }

    public static String createUpdatedKeywordAsJSON() throws IOException {
        Keyword keyword = createKeyword();
        keyword.setKeyword(KEYWORD_TEST_UPDATED);
        return createKeywordAsJSON(keyword);
    }

    public static String createKeywordAsJSON(
            Keyword keyword) throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonWriter = new StringWriter();
        JsonGenerator jgen = factory.createGenerator(jsonWriter);
        jgen.writeStartObject();
        printNullable(jgen, KEYWORD, keyword.getKeyword());
        jgen.writeEndObject();
        jgen.close();
        return jsonWriter.toString();
    }
}
