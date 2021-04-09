package utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import nikita.common.model.noark5.v5.DocumentObject;
import nikita.common.model.noark5.v5.metadata.Format;
import nikita.common.model.noark5.v5.metadata.VariantFormat;

import java.io.IOException;
import java.io.StringWriter;

import static nikita.common.config.N5ResourceMappings.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printNullable;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.printNullableMetadata;

public final class DocumentObjectCreator {

    /**
     * Create a default DocumentObject for testing purposes
     *
     * @return a DocumentObject with all values set
     */
    public static DocumentObject createDocumentObject() {
        DocumentObject documentObject = new DocumentObject();
        documentObject.setFormat(new Format("fmt/291",
                "OpenDocument Text (odt) Version 1.2"));
        documentObject.setVariantFormat(new VariantFormat("P",
                "Produksjonsformat"));
        documentObject.setFileSize(8324L);
        documentObject.setChecksum("9f56a664fe0cc3ddddc42ec29c6246902c1396ed9e32563e0cd9802728eba0f0");
        documentObject.setChecksumAlgorithm("SHA-256");
        documentObject.setMimeType("application/vnd.oasis.opendocument.text");
        documentObject.setOriginalFilename("test_document.odt");
        return documentObject;
    }

    public static String createDocumentObjectAsJSON() throws IOException {
        DocumentObject documentObject = createDocumentObject();
        JsonFactory factory = new JsonFactory();
        StringWriter jsonWriter = new StringWriter();
        JsonGenerator jgen = factory.createGenerator(jsonWriter);
        jgen.writeStartObject();
        printNullableMetadata(jgen, DOCUMENT_OBJECT_VARIANT_FORMAT,
                documentObject.getVariantFormat());
        printNullableMetadata(jgen, DOCUMENT_OBJECT_FORMAT,
                documentObject.getFormat());
        printNullable(jgen, DOCUMENT_OBJECT_CHECKSUM,
                documentObject.getChecksum());
        printNullable(jgen, DOCUMENT_OBJECT_CHECKSUM_ALGORITHM,
                documentObject.getChecksumAlgorithm());
        printNullable(jgen, DOCUMENT_OBJECT_FILE_SIZE,
                documentObject.getFileSize());
        printNullable(jgen, DOCUMENT_OBJECT_FILE_NAME,
                documentObject.getOriginalFilename());
        printNullable(jgen, DOCUMENT_OBJECT_MIME_TYPE,
                documentObject.getMimeType());
        jgen.writeEndObject();
        jgen.close();
        return jsonWriter.toString();
    }
}
