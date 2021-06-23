package nikita.common.util.analysis;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import javax.sql.rowset.serial.SerialClob;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Clob;
import java.sql.SQLException;

public class DocumentAnalysis {

    private final Tika tika;

    public DocumentAnalysis() {
        tika = new Tika();
    }

    /**
     * Convert contents of a document to a string of tokens
     *
     * @param stream InputStream of document to retrieve tokens from
     * @return The string of tokens
     * @throws IOException   if one occurs
     * @throws TikaException problem processing file
     */
    public String getDocumentTokens(InputStream stream)
            throws IOException, TikaException {
        return tika.parseToString(stream);
    }

    /**
     * Convert contents of a document to a CLOB type
     *
     * @param stream InputStream of document to retrieve tokens from
     * @return The string of tokens as a CLOB
     * @throws IOException   if one occurs
     * @throws TikaException problem processing file
     */
    public Clob getDocumentTokensAsClob(InputStream stream)
            throws IOException, TikaException, SQLException {
        return new SerialClob(tika.parseToString(stream).toCharArray());
    }

    /**
     * Get mimeType of the incoming document
     *
     * @param stream InputStream of document to check
     * @return the mimeType
     * @throws IOException if one occurs
     */
    public String getMimeType(InputStream stream)
            throws IOException {
        return tika.detect(stream);
    }
}
