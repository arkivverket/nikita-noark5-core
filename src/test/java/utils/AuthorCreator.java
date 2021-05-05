package utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.secondary.Author;

import java.io.IOException;
import java.io.StringWriter;

import static nikita.common.config.N5ResourceMappings.AUTHOR;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printNullable;
import static utils.TestConstants.AUTHOR_TEST;
import static utils.TestConstants.AUTHOR_TEST_UPDATED;

public final class AuthorCreator {

    /**
     * Create a default Author for testing purposes
     *
     * @return a Author with all values set
     */
    public static Author createAuthor() {
        Author author = new Author();
        author.setAuthor(AUTHOR_TEST);
        return author;
    }

    public static String createAuthorAsJSON() throws IOException {
        return createAuthorAsJSON(createAuthor());
    }

    public static String createUpdatedAuthorAsJSON() throws IOException {
        Author author = createAuthor();
        author.setAuthor(AUTHOR_TEST_UPDATED);
        return createAuthorAsJSON(author);
    }

    public static String createAuthorAsJSON(
            Author author) throws IOException {
        JsonFactory factory = new JsonFactory();
        StringWriter jsonWriter = new StringWriter();
        JsonGenerator jgen = factory.createGenerator(jsonWriter);
        jgen.writeStartObject();
        printNullable(jgen, AUTHOR, author.getAuthor());
        jgen.writeEndObject();
        jgen.close();
        return jsonWriter.toString();
    }
}
