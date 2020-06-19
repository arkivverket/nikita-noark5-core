package nikita.webapp.odata;

import nikita.webapp.odata.base.ODataParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nikita.common.config.N5ResourceMappings.*;

/**
 * Class to handle special adaptions to ODataToHQL so that
 * particular interpretations e.g. complexTypes that are not
 * database tables are dealt with
 * <p>
 * Code value queries e.g. arkiv/arkivstatus/kode has to be translated.
 * arkiv/arkivstatus/kode -> fonds/fondsStatusCode
 * arkiv/arkivstatus/kodenavn -> fonds/fondsStatusCodeName
 * <p>
 * arkiv/arkivstatus?$filter=kode eq 'O'
 * arkiv/arkivstatus?$filter=kodenavn eq 'Opprettet'
 * <p>
 * arkiv?$filter=arkivstatus/kode eq 'O'
 * arkiv?$filter=arkivstatus/kodenavn eq 'Opprettet'
 */

public class NikitaODataToHQL
        extends ODataToHQL {

    private static final Map<String, String> codeValues = new HashMap<>();

    public NikitaODataToHQL(String dmlStatementType) {
        super(dmlStatementType);
        initCodeValues();
    }

    public void initCodeValues() {
        codeValues.put("arkivdelstatus", SERIES_STATUS_ENG_OBJECT);
        codeValues.put("arkivstatus", FONDS_STATUS_ENG_OBJECT);
        codeValues.put("avskrivningsmaate", SIGN_OFF_METHOD_ENG_OBJECT);
        codeValues.put("dokumentmedium", DOCUMENT_MEDIUM_ENG_OBJECT);
        codeValues.put("dokumentstatus", DOCUMENT_STATUS_ENG_OBJECT);
        codeValues.put("dokumenttype", DOCUMENT_TYPE_ENG_OBJECT);
        codeValues.put("elektronisksignatursikkerhetsnivaa",
                ELECTRONIC_SIGNATURE_SECURITY_LEVEL_FIELD_ENG_OBJECT);
        codeValues.put("elektronisksignaturverifisert",
                ELECTRONIC_SIGNATURE_VERIFIED_FIELD_ENG_OBJECT);
        codeValues.put("flytstatus", FLOW_STATUS_ENG_OBJECT);
        codeValues.put("format", FORMAT_ENG_OBJECT);
        codeValues.put("graderingskode", CLASSIFICATION_ENG_OBJECT);
        codeValues.put("hendelsetype", EVENT_TYPE_ENG_OBJECT);
        codeValues.put("journalposttype", REGISTRY_ENTRY_TYPE_ENG_OBJECT);
        codeValues.put("journalstatus", REGISTRY_ENTRY_STATUS_ENG_OBJECT);
        codeValues.put("kassasjonsvedtak", DISPOSAL_DECISION_ENG_OBJECT);
        codeValues.put("klassifikasjonstype", CLASSIFICATION_ENG_OBJECT);
        codeValues.put("koordinatsystem", COORDINATE_SYSTEM);
        codeValues.put("korrespondanseparttype", CORRESPONDENCE_PART_TYPE_ENG_OBJECT);
        codeValues.put("land", COUNTRY_CODE_ENG_OBJECT);
        codeValues.put("merknadstype", COMMENT_TEXT_ENG_OBJECT);
        codeValues.put("partrolle", PART_ROLE_FIELD_ENG_OBJECT);
        codeValues.put("presedensstatus", PRECEDENCE_PRECEDENCE_STATUS_ENG_OBJECT);
        codeValues.put("saksstatus", CASE_STATUS_ENG_OBJECT);
        codeValues.put("skjermingdokument", SCREENING_SCREENING_DOCUMENT_ENG_OBJECT);
        codeValues.put("skjermingmetadata", SCREENING_SCREENING_METADATA_ENG_OBJECT);
        codeValues.put("slettingstype", DELETION_TYPE_ENG_OBJECT);
        codeValues.put("tilgangskategori", ACCESS_CATEGORY_ENG_OBJECT);
        codeValues.put("tilgangsrestriksjon", ACCESS_RESTRICTION_ENG_OBJECT);
        codeValues.put("tilknyttetregistreringsom",
                ASSOCIATED_WITH_RECORD_AS_ENG_OBJECT);
        codeValues.put("variantformat", VARIANT_FORMAT_ENG);
    }

    protected String processJoinEntitiesContext(ODataParser.JoinEntitiesContext ctx) {
        List<ODataParser.EntityNameContext> entityNameContexts =
                ctx.getRuleContexts(ODataParser.EntityNameContext.class);
        String lastValue = ctx.getChild(ctx.getChildCount() - 1).getText();
        if (lastValue.equalsIgnoreCase("kode") ||
                lastValue.equalsIgnoreCase("kodenavn")) {
            String entity = entityNameContexts
                    .get(entityNameContexts.size() - 1).getText();
            // Because codeValues are both entities and attributes e.g
            // arkivstatus then we need to make sure to distinguish them as
            // attribute/entity. So make sure to lowercase the first letter.
            // The java code to use lowercase is only necessary when using
            // non-metadata entity queries
            // e.g. dokumentobjekt?$filter=dokumentbeskrivelse/dokumentstatus/kode eq 'B'
            // but does no harm when run on non-metadata queries
            // e.g arkivstatus?$filter=kode eq 'O'
            String codeValue = getInternalNameObject(entity);
            String code = codeValue.substring(0, 1).toLowerCase() +
                    codeValue.substring(1);
            if (lastValue.equalsIgnoreCase("kode")) {
                code += "Code";
            } else if (lastValue.equalsIgnoreCase("kodenavn")) {
                code += "CodeName";
            }
            if (entityNameContexts.size() <= 1) {
                processAttribute(getAliasAndAttribute(this.entity, code));
                return "";
            } else {
                // Join the from the entity applying the filter on e.g
                // mappe?$filter=klasse/klassifikasjonssystem ....
                // You have to first join mappe (File) to klasse (Class)
                String toEntity = getInternalNameObject(getValue(ctx,
                        ODataParser.EntityNameContext.class));
                addEntityToEntityJoin(this.entity, toEntity);
                if (entityNameContexts.size() > 2) {
                    for (int i = 0; i < entityNameContexts.size() - 2; i++) {
                        if (i < entityNameContexts.size() - 1) {
                            String fromEntity = getInternalNameObject
                                    (getValue(ctx, ODataParser.EntityNameContext.class, i));
                            toEntity = getInternalNameObject(
                                    getValue(ctx, ODataParser.EntityNameContext.class, i + 1));
                            addEntityToEntityJoin(fromEntity, toEntity);
                        }
                    }
                }
                processAttribute(getAliasAndAttribute(toEntity, code));
                return toEntity;
            }
        }
        return super.processJoinEntitiesContext(ctx);
    }

    @Override
    public void enterAttributeName(ODataParser.AttributeNameContext ctx) {
        if (ctx.getText().equalsIgnoreCase("kode") ||
                ctx.getText().equalsIgnoreCase("kodenavn")) {
            if (ctx.getParent() instanceof ODataParser.JoinEntitiesContext) {
                return;
            }
        }
        processAttribute(getAliasAndAttribute(
                this.joinEntity.isEmpty() == true ?
                        this.entity : this.joinEntity,
                getInternalNameObject(ctx.getText())));
    }
}
