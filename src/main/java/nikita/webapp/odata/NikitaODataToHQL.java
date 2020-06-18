package nikita.webapp.odata;

import nikita.webapp.odata.base.ODataParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nikita.common.config.N5ResourceMappings.ELECTRONIC_SIGNATURE_VERIFIED_FIELD_ENG;

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
        codeValues.put("arkivdelstatus", "seriesStatus");
        codeValues.put("arkivstatus", "fondsStatus");
        codeValues.put("avskrivningsmaate", "signOffMethod");
        codeValues.put("dokumentmedium", "documentMedium");
        codeValues.put("dokumentstatus", "documentStatus");
        codeValues.put("dokumenttype", "documentType");
        codeValues.put("elektronisksignatursikkerhetsnivaa", "electronicSignatureSecurityLevel");
        codeValues.put("elektronisksignaturverifisert", ELECTRONIC_SIGNATURE_VERIFIED_FIELD_ENG);
        codeValues.put("flytstatus", "flowStatus");
        codeValues.put("format", "format");
        codeValues.put("graderingskode", "classification");
        codeValues.put("hendelsetype", "eventType");
        codeValues.put("journalposttype", "registryEntryType");
        codeValues.put("journalstatus", "registryEntryStatus");
        codeValues.put("kassasjonsvedtak", "disposalDecision");
        codeValues.put("klassifikasjonstype", "classificationType");
        codeValues.put("koordinatsystem", "coordinateSystem");
        codeValues.put("korrespondanseparttype", "correspondencePartType");
        codeValues.put("land", "country");
        codeValues.put("mappetype", "fileType");
        codeValues.put("merknadstype", "commentType");
        codeValues.put("partrolle", "partRole");
        codeValues.put("presedensstatus", "precedenceStatus");
        codeValues.put("saksstatus", "caseStatus");
        codeValues.put("skjermingdokument", "screeningDocument");
        codeValues.put("skjermingmetadata", "screeningMetadata");
        codeValues.put("slettingstype", "deletionType");
        codeValues.put("tilgangskategori", "accessCategory");
        codeValues.put("tilgangsrestriksjon", "accessRestriction");
        codeValues.put("tilknyttetregistreringsom", "associatedWithRecordAs");
        codeValues.put("variantformat", "variantFormat");
    }

    protected String processJoinEntitiesContext(ODataParser.JoinEntitiesContext ctx) {
        List<ODataParser.EntityNameContext> entityNameContexts =
                ctx.getRuleContexts(ODataParser.EntityNameContext.class);
        String lastValue = ctx.getChild(ctx.getChildCount() - 1).getText();
        if (lastValue.equalsIgnoreCase("kode") ||
                lastValue.equalsIgnoreCase("kodenavn")) {
            String entity = entityNameContexts
                    .get(entityNameContexts.size() - 1).getText();
            String code = codeValues.get(entity);
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
            return;
        }
        processAttribute(getAliasAndAttribute(
                this.joinEntity.isEmpty() == true ?
                        this.entity : this.joinEntity,
                getInternalNameObject(ctx.getText())));
    }
}
