package nikita.webapp.structure;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nikita.N5CoreApp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import static nikita.common.util.CommonUtils.Hateoas.Deserialize.*;
import static nikita.common.util.CommonUtils.Hateoas.Serialize.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import nikita.common.model.noark5.v5.Fonds;
import nikita.common.model.noark5.v5.Record;
import nikita.common.util.deserialisers.FondsDeserializer;
import nikita.common.util.deserialisers.RecordDeserializer;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest(classes = N5CoreApp.class)
//@AutoConfigureRestDocs(outputDir = "target/snippets")
public class TestParsing {

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
    }

    @Test
    public void validdates() throws Exception {
	System.out.println("info: testing date and datetime parsing");
	String[] datetimemust = {
            "1997-07-16T19:20+01:00",
            "1997-07-16T19:20:30+01:00",
            "1997-07-16T19:20:30.45+01:00",
            "2012-10-10T15:00:00Z",
            "2014-11-22T15:15:02.956+01:00",

            // These could be handled too
            "1997-07-16Z",
            "1865-02-13T00:00:00Z",
            "1865-02-13T00:00:00+00:00",
            "1865-02-13T00:00:00+01:00",
            "1865-02-13T00:00:00-01:00",
            "1864-10-09T00:00:00Z",
            "1864-10-09T00:00:00+00:00",
        };
        String[] datetimereject = {
            "1997-07-16T19:20+0100",
            "1997-07-16T19:20-0100",
            "1997-07-16T19:20+01",
            "1997-07-16T19:20-01",
            "1997",
            "1997-07",
            "1997-07-16",
        };
	for (String teststr : datetimemust) {
	    StringBuilder errors = new StringBuilder();
	    String json = "{ \"d\": \"" + teststr+ "\"}";
	    ObjectNode objectNode =
		(ObjectNode) new ObjectMapper().readTree(json);
	    OffsetDateTime d =
		deserializeDateTime("d", objectNode, errors);
	    if (null != d) {
		System.out.println("success: datetime '" + teststr + "' parsed to " + d);
		String str = formatDateTime(d);
		if (str.equals(teststr)) {
		    System.out.println("success: datetime serialized back to '" + str + "'");
		} else {
		    // Some datetime are not expected to stay
		    // unchanged, like 19:30 -> 19:30:00, so only flag
		    // warning, not error.
		    System.out.println("warning: serialized datetime changed to '" + str + "'");
		}
	    } else {
		System.out.println("error: unable to parse datetime '" + teststr + "'");
	    }
	}
	for (String teststr : datetimereject) {
	    StringBuilder errors = new StringBuilder();
	    String json = "{ \"d\": \"" + teststr+ "\"}";
	    ObjectNode objectNode =
		(ObjectNode) new ObjectMapper().readTree(json);
	    OffsetDateTime d =
		deserializeDateTime("d", objectNode, errors);
	    if (null != d) {
		System.out.println("error: datetime '" + teststr + "' parsed to " + d);
	    } else {
		System.out.println("success: unable to parse datetime '" + teststr + "'");
	    }
	}

	String[] datemust = {
            "1997-07-16+01:00",
            "1997-07-16-01:00",
            "1997-07-16Z",
            "1865-02-13Z",
	};
	String[] datereject = {
            "1997-07-16+0100",
            "1997-07-16-0100",
            "1997-07-16+01",
            "1997-07-16-01",
            "1997-07-16",
	};
	for (String teststr : datemust) {
	    StringBuilder errors = new StringBuilder();
	    String json = "{ \"d\": \"" + teststr+ "\"}";
	    ObjectNode objectNode =
		(ObjectNode) new ObjectMapper().readTree(json);
	    OffsetDateTime d =
		deserializeDate("d", objectNode, errors);
	    if (null != d) {
		System.out.println("success: date '" + teststr + "' parsed to " + d);
		String str = formatDate(d);
		if (str.equals(teststr)) {
		    System.out.println("success: date serialized back to '" + str + "'");
		} else {
		    System.out.println("error: serialized date changed to '" + str + "'");
		}
	    } else {
		System.out.println("error: unable to parse date '" + teststr + "'");
	    }
	}
	for (String teststr : datereject) {
	    StringBuilder errors = new StringBuilder();
	    String json = "{ \"d\": \"" + teststr+ "\"}";
	    ObjectNode objectNode =
		(ObjectNode) new ObjectMapper().readTree(json);
	    OffsetDateTime d =
		deserializeDate("d", objectNode, errors);
	    if (null != d) {
		System.out.println("error: date '" + teststr + "' parsed to " + d);
	    } else {
		System.out.println("success: unable to parse date '" + teststr + "'");
	    }
	}
    }

    @Test
    public void parseFonds() throws Exception {
	System.out.println("info: testing fonds parsing");
	String json = "{ \"tittel\": \"A fonds title\" }";
	ObjectMapper objectMapper = new ObjectMapper();
	JsonParser jsonParser =
	    objectMapper.getJsonFactory().createJsonParser(json);
	FondsDeserializer fondsDeserializer = new FondsDeserializer();
	Fonds fonds =
	    fondsDeserializer.deserialize(jsonParser,
					  null /* DeserializationContext */);
	assert(null != fonds);
	assert("A fonds title".equals(fonds.getTitle()));
	/*
	assertNotNull("able to create default fonds from json", fonds);
	assertEquals("fonds title matches input",
		     "En tittel", fonds.getTitle());
	*/
    }

    @Test
    public void parseRecord() throws Exception {
	System.out.println("info: testing record parsing");
	String json = "{ "
	    +"\"tittel\": \"A record title\", "
	    + "\"forfatter\": [\"Isac Asimov\"] "+
	    "}";
	ObjectMapper objectMapper = new ObjectMapper();
	JsonParser jsonParser =
	    objectMapper.getJsonFactory().createJsonParser(json);
	RecordDeserializer recordDeserializer = new RecordDeserializer();
	Record record =
	    recordDeserializer.deserialize(jsonParser,
					   null /* DeserializationContext */);
	assert(null != record);
	assert("A record title".equals(record.getTitle()));
    }
}
