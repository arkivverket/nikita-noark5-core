package nikita.webapp.odata;

import nikita.common.util.exceptions.NikitaMalformedInputDataException;
import nikita.webapp.odata.base.ODataParserBaseListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.fromString;
import static nikita.common.config.N5ResourceMappings.SYSTEM_ID;
import static nikita.common.util.CommonUtils.WebUtils.getEnglishNameDatabase;
import static nikita.common.util.CommonUtils.WebUtils.getEnglishNameObject;
import static nikita.webapp.odata.base.ODataParser.*;

public abstract class ODataWalker
        extends ODataParserBaseListener
        implements IODataWalker {

    private final Logger logger =
            LoggerFactory.getLogger(ODataWalker.class);
    protected String entity = "";
    protected String joinEntity = "";

    @Override
    public void enterMethodCallExpr(MethodCallExprContext ctx) {
        super.enterMethodCallExpr(ctx);
        processMethodExpression(ctx.getChild(0).getText());
    }

    @Override
    public void enterCompareMethodExpr(CompareMethodExprContext ctx) {
        super.enterCompareMethodExpr(ctx);
        processCompareMethod(ctx.getChild(0).getText(),
                getPrimitiveTypeObject((PrimitiveLiteralContext)
                        ctx.getChild(4).getChild(0)));
    }

    @Override
    public void enterCalenderMethodExp(CalenderMethodExpContext ctx) {
        super.enterCalenderMethodExp(ctx);
        processMethodExpression(ctx.getChild(0).getText());
    }

    @Override
    public void enterJoinEntities(JoinEntitiesContext ctx) {
        super.enterJoinEntities(ctx);
        this.joinEntity = processJoinEntitiesContext(ctx);
    }

    @Override
    public void enterAttributeName(AttributeNameContext ctx) {
        super.enterAttributeName(ctx);
        processAttribute(getAliasAndAttribute(
                this.joinEntity.isEmpty() == true ?
                        this.entity : this.joinEntity,
                getInternalNameObject(ctx.getText())));
    }

    @Override
    public void enterRightComparatorExpr(RightComparatorExprContext ctx) {
        super.enterRightComparatorExpr(ctx);
        processStartRight();
    }

    @Override
    public void enterComparisonOperator(ComparisonOperatorContext ctx) {
        super.enterComparisonOperator(ctx);
        processComparator(ctx.getChild(0).getText());
    }

    @Override
    public void enterConcatMethodExpr(ConcatMethodExprContext ctx) {
        super.enterConcatMethodExpr(ctx);
        processStartConcat();

    }

    @Override
    public void enterBoolExpression(BoolExpressionContext ctx) {
        super.enterBoolExpression(ctx);
        startBoolComparison();
    }

    @Override
    public void enterOrderByItem(OrderByItemContext ctx) {
        super.enterOrderByItem(ctx);
        String aliasAndAttribute =
                getAliasAndAttribute(this.entity,
                        getInternalNameObject(ctx.getChild(0).getText()));
        if (ctx.getChild(1) instanceof SortOrderContext) {
            processOrderBy(aliasAndAttribute,
                    ctx.getChild(1).getChild(0).getText());
        } else {
            processOrderBy(aliasAndAttribute, "");
        }
    }

    @Override
    public void enterPrimitiveLiteral(PrimitiveLiteralContext ctx) {
        super.enterPrimitiveLiteral(ctx);
        processPrimitive(getPrimitiveTypeObject(ctx));
    }

    @Override
    public void exitBoolExpression(BoolExpressionContext ctx) {
        super.exitBoolExpression(ctx);
        endBoolComparison();
        this.joinEntity = "";
    }

    protected String processJoinEntitiesContext(JoinEntitiesContext ctx) {
        List<EntityNameContext> entityNameContexts =
                ctx.getRuleContexts(EntityNameContext.class);
        // Join the from the entity applying the filter on e.g
        // mappe?$filter=klasse/klassifikasjonssystem ....
        // You have to first join mappe (File) to klasse (Class)
        String toEntity = getInternalNameObject(getValue(ctx,
                EntityNameContext.class));
        addEntityToEntityJoin(this.entity, toEntity);
        if (entityNameContexts.size() > 1) {
            for (int i = 0; i < entityNameContexts.size(); i++) {
                if (i < entityNameContexts.size() - 1) {
                    String fromEntity = getInternalNameObject
                            (getValue(ctx, EntityNameContext.class, i));
                    toEntity = getInternalNameObject(
                            getValue(ctx, EntityNameContext.class, i + 1));
                    addEntityToEntityJoin(fromEntity, toEntity);
                }
            }
        }
        return toEntity;
    }

    @Override
    public void enterBinaryExpression(BinaryExpressionContext ctx) {
        super.enterBinaryExpression(ctx);
    }

    @Override
    public void enterLogicalOperator(LogicalOperatorContext ctx) {
        super.enterLogicalOperator(ctx);
        processLogicalOperator(ctx.getText());
    }

    @Override
    public void enterOpenPar(OpenParContext ctx) {
        super.enterOpenPar(ctx);
        processParenthesis(ctx.getText());
    }

    @Override
    public void enterClosePar(CloseParContext ctx) {
        super.enterClosePar(ctx);
        processParenthesis(ctx.getText());
    }

    @Override
    public void enterResourcePath(ResourcePathContext ctx) {
        super.enterResourcePath(ctx);
        logger.debug("Entering ResourcePathContext. Found [" +
                ctx.getText() + "]");

        if (null != ctx.getChild(EntityContext.class, 0)) {
            this.entity = getInternalNameObject(getValue(
                    ctx.getChild(EntityContext.class, 0),
                    EntityNameContext.class));
        } else if (null != ctx.getChild(EntityCastContext.class, 0)) {
            this.entity = getInternalNameObject(getValue(
                    ctx, EntityCastContext.class));
        } else if (null != ctx.getChild(EntityUUIDContext.class, 0)) {
            this.entity = getInternalNameObject(getValue(
                    ctx, EntityNameContext.class));
            processEntityUUID(ctx.getChild(EntityUUIDContext.class, 0));
        } else if (null != ctx.getChild(EmbeddedEntitySetContext.class, 0)) {
            this.entity = processResourcePathJoin(
                    ctx.getChild(EmbeddedEntitySetContext.class, 0));
            processLogicalOperator("and");
        }
        processQueryEntity(this.entity);
    }

    private void processEntityUUID(EntityUUIDContext ctx) {
        UUID systemID = fromString(
                getValue(ctx, UuidIdValueContext.class));
        String aliasAndAttribute = getAliasAndAttribute(
                this.entity, getInternalNameObject(SYSTEM_ID));
        processComparatorCommand(aliasAndAttribute, "eq", systemID);
    }

    private String processResourcePathJoin(EmbeddedEntitySetContext ctx) {
        // Identify the last entity so we can use it as the starting point for
        // JOINs HERE!!!
        String entity = getFinalEntity(
                ctx.getRuleContexts(EntityContext.class));

        List<EntityUUIDContext> uuidContexts =
                ctx.getRuleContexts(EntityUUIDContext.class);

        if (uuidContexts.size() > 0) {
            String toEntity = getInternalNameObject(
                    uuidContexts.get(uuidContexts.size() - 1)
                            .getChild(0).getText());
            addEntityToEntityJoin(entity, toEntity);
            String toEntityUUID = uuidContexts.get(uuidContexts.size() - 1)
                    .getChild(2).getText();
            processCompare(getAliasAndAttribute(toEntity,
                    getInternalNameObject(SYSTEM_ID)),
                    "eq", fromString(toEntityUUID));
            for (int i = uuidContexts.size() - 1; i > 0; i--) {
                String fromEntity = getInternalNameObject(
                        uuidContexts.get(i).getChild(0).getText());
                toEntity = getInternalNameObject(
                        uuidContexts.get(i - 1).getChild(0).getText());
                toEntityUUID = uuidContexts.get(i - 1)
                        .getChild(2).getText();
                addEntityToEntityJoin(fromEntity, toEntity);
                processLogicalOperator("and");
                processCompare(getAliasAndAttribute(toEntity,
                        getInternalNameObject(SYSTEM_ID)),
                        "eq", fromString(toEntityUUID));
            }
        }
        return entity;
    }

    protected String getFinalEntity(
            List<EntityContext> entityContexts) {
        if (entityContexts.size() > 0) {
            return getInternalNameObject(entityContexts.get(
                    entityContexts.size() - 1).getText());
        }
        // Consider making this throw a 500 internal or 400 bad request
        return "";
    }

    @Override
    public void enterCountStatement(CountStatementContext ctx) {
        super.enterCountStatement(ctx);
        processCountAsResource(true);
    }

    /**
     * getInternalNameAttribute
     * <p>
     * Helper mechanism to convert Norwegian entity / attribute names to
     * English as English is used in classes and tables. Interacting with the
     * core is done in Norwegian but things have then to be translated to
     * English naming conventions.
     * <p>
     * Note, this will return the name of the database column
     * <p>
     * If we don't have a corresponding object, we choose just to return the
     * original object. This should force a database query error and expose
     * any missing objects. This strategy is OK in development, but later we
     * need a better way of handling it.
     *
     * @param norwegianName The name in Norwegian
     * @return the English version of the Norwegian name if it exists, otherwise
     * the original Norwegian name.
     */
    protected String getInternalNameAttribute(String norwegianName) {
        String englishName = getEnglishNameDatabase(norwegianName);
        if (englishName == null)
            return norwegianName;
        else
            return englishName;
    }

    /**
     * getInternalNameObject
     * <p>
     * Helper mechanism to convert Norwegian entity / attribute names to
     * English as English is used in classes and tables. Interacting with the
     * core is done in Norwegian but things have then to be translated to
     * English naming conventions.
     * <p>
     * Note, this will return the name of the class variable
     * <p>
     * If we don't have a corresponding object, we choose just to return the
     * original object. This should force a database query error and expose
     * any missing objects. This strategy is OK in development, but later we
     * need a better way of handling it.
     *
     * @param norwegianName The name in Norwegian
     * @return the English version of the Norwegian name if it exists, otherwise
     * the original Norwegian name.
     */
    protected String getInternalNameObject(String norwegianName) {
        String englishName = getEnglishNameObject(norwegianName);
        if (englishName == null)
            return norwegianName;
        else
            return englishName;
    }

    @Override
    public void enterTopStatement(TopStatementContext ctx) {
        super.enterTopStatement(ctx);
        processTop(Integer.valueOf(ctx.getChild(1).getText()));
    }

    @Override
    public void enterSkipStatement(SkipStatementContext ctx) {
        super.enterSkipStatement(ctx);
        processSkip(Integer.valueOf(ctx.getChild(1).getText()));
    }

    String getValue(ParserRuleContext context, Class klass) {
        ParseTree pContext = context.getChild(klass, 0);
        return pContext.getText();
    }

    String getValue(ParserRuleContext context, Class klass, int count) {
        ParserRuleContext context1 = (ParserRuleContext) context.getChild(klass, count);
        String value = context1.getText();
        return value;
    }

    String getAliasAndAttribute(String entity, String attribute) {
        return entity.toLowerCase() + "_1." + attribute;
    }

    private Object getPrimitiveTypeObject(PrimitiveLiteralContext context) {
        if (null != context.getChild(IntegerValueContext.class, 0)) {
            return Integer.valueOf(context
                    .getChild(IntegerValueContext.class, 0).getText());
        } else if (null != context.getChild(FloatValueContext.class, 0)) {
            return Float.valueOf(context
                    .getChild(FloatValueContext.class, 0).getText());
        } else if (null != context.getChild(DecimalLiteralContext.class, 0)) {
            return Double.valueOf(context
                    .getChild(DecimalLiteralContext.class, 0).getText());
        } else if (null != context.getChild(BooleanValueContext.class, 0)) {
            return Boolean.valueOf(context
                    .getChild(BooleanValueContext.class, 0).getText());
        } else if (null != context.getChild(QuotedStringContext.class, 0)) {
            String quotedString =
                    context.getChild(QuotedStringContext.class, 0).getText();
            return quotedString.substring(1, quotedString.length() - 1);
        } else if (null != context.getChild(NullTokenContext.class, 0) ||
                null != context.getChild(NullSpecLiteralContext.class, 0)) {
            return null;
        } else {
            String error = "Unknown primitive literal for context "
                    + context.getText();
            logger.error(error);
            throw new NikitaMalformedInputDataException(error);
        }
    }
}
