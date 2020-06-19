package nikita.webapp.odata;

import nikita.webapp.odata.base.ODataParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
