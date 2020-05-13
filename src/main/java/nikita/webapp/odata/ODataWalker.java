package nikita.webapp.odata;

import nikita.common.util.CommonUtils;
import nikita.webapp.odata.base.ODataParser;
import nikita.webapp.odata.base.ODataParserBaseListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.lang.Integer.parseInt;
import static nikita.common.util.CommonUtils.WebUtils.getEnglishNameObject;
import static nikita.webapp.odata.base.ODataParser.*;

/**
 * Extending ODataBaseListener to capture the required events for processing
 * OData syntax so that it can be converted to SQL or HQL.
 * <p>
 * The code here might be a little ugly in terms of interfaces, perhaps an
 * abstract class should be used, throws on method calls that should only be
 * called by sub-classes etc. But this code is a compromise of sorts as I learn
 * about parsing and how to walk through parse trees. Right now I don't know
 * what the best practice is, but I have to solve a particular a problem and
 * will discover a best practice as I work on that problem.
 * <p>
 * This class captures the enter/exit*Command and calls a
 * 'process enter/exit*Command' that is implemented in a sub class. The reason
 * the sub-class itself does not just implement the enter/exit*Command is that
 * there may be instances where we have to do some checks on siblings or
 * parents and then it's handy to have the ability to do that common for all
 * before calling all sub-classes. If we find that we don't need this, then
 * we will change the code to ignore this extra level of complexity.
 * <p>
 * We have to support OData2HQL, but perhaps someone out there might find an
 * implementation of OData2SQL useful. We will also support OData2
 * ElasticSearch query.
 * <p>
 * Note, as we start this, we do not know how deep the rabbit hole is or how
 * much OData we will support. So DO NOT RELY on copying this and using it in
 * a production environment!
 */

public abstract class ODataWalker
        extends ODataParserBaseListener
        implements IODataWalker {

    private final Logger logger =
            LoggerFactory.getLogger(ODataWalker.class);
    protected String entity = "";

    /**
     * This is the part that picks up the start of the OData command of a
     * OData URL. Given the following example:
     * <p>
     * [contextPath][api]/arkivstruktur/arkiv?$filter=contains(tittel, 'hello')
     * <p>
     * the ODataCommand part is $filter=contains(tittel, 'hello')
     * <p>
     * It can also be $top, $skip etc.
     * <p>
     * We don't do any processing on this set of tokens. This method can
     * probably be removed. TODO: Take a look at this!
     *
     * @param ctx ODataParser.OdataCommandContext
     */

    @Override
    public void enterOdataCommand(ODataParser.OdataCommandContext ctx) {
        super.enterOdataCommand(ctx);
        logger.debug("Entering enterOdataCommand. Found [" +
                ctx.getText() + "]");
        // processEnterOdataCommand(ctx);
    }

    /**
     * enterStringCompareExpression
     * <p>*
     * Used by sub-classes to convert the following OData queries to relevant
     * queries:
     * - contains
     * - startswith
     * - endswith
     * <p>
     * arkiv?$filter=contains(tittel, 'hello')
     * arkiv?$filter=startsWith(tittel, 'hello')
     * arkiv?$filter=endsWith(tittel, 'hello')
     * <p>
     * The string comparison expression contains(tittel, 'hello') is parsed and
     * the command e.g. startsWith and the attribute (in this case  'tittel')
     * and value (in this case) 'hello' are identified and passed
     * to the calling sub-class that will decide how it should be handled
     * according to their syntax.
     *
     * @param ctx The StringCompareExpressionContext
     */
    @Override
    public void enterStringCompareExpression(
            StringCompareExpressionContext ctx) {
        super.enterStringCompareExpression(ctx);
        logger.debug("Entering enterStringCompareExpression. Found [" +
                ctx.getText() + "]");
        if (null != ctx.getChild(AttributeNameContext.class, 0)) {
            processStringCompare(
                    this.entity.toLowerCase() + "_1",
                    getValue(ctx, StringCompareCommandContext.class),
                    getValue(ctx, AttributeNameContext.class),
                    getValue(ctx, SingleQuotedStringContext.class));
        } else if (null != ctx.getChild(JoinEntitiesContext.class, 0)) {
            JoinEntitiesContext joinEntitiesContext =
                    ctx.getChild(JoinEntitiesContext.class, 0);
            processJoinEntitiesContext(joinEntitiesContext);
            String entity = getFinalEntityFromJoin(joinEntitiesContext);
            processStringCompare(entity.toLowerCase() + "_1",
                    getValue(ctx, StringCompareCommandContext.class),
                    getValue(joinEntitiesContext, AttributeNameContext.class),
                    getValue(ctx, SingleQuotedStringContext.class));
        }
    }

    /**
     * enterIntegerComparatorExpression
     * <p>
     * Used by sub-classes to convert the following OData queries to relevant
     * queries:
     * <p>
     * $filter=year(DateTime) eq 2010
     * $filter=month(DateTime) eq 2 // February (month of year)
     * $filter=day(DateTime) eq 21 // 21st day of the month
     * $filter=hour(DateTime) eq 1 // start at 0 or 1? Assuming 0
     * $filter=minute(DateTime) eq 42 // start at 0 or 1? Assuming 0
     * $filter=second(DateTime) eq 55 // what is after 59? 60 or 0?
     *
     * @param ctx
     */
    @Override
    public void enterIntegerComparatorExpression(
            IntegerComparatorExpressionContext ctx) {
        super.enterIntegerComparatorExpression(ctx);
        logger.debug("Entering enterIntegerComparatorExpression. Found [" +
                ctx.getText() + "]");

        if (null != ctx.getChild(AttributeNameContext.class, 0)) {
            processIntegerCompare(
                    getValue(ctx, IntegerCompareCommandContext.class),
                    getAliasAndAttribute(this.entity,
                            getInternalNameObject(getValue(ctx,
                                    AttributeNameContext.class))),
                    getValue(ctx, ComparisonOperatorContext.class),
                    getValue(ctx, IntegerValueContext.class));
        } else if (null != ctx.getChild(JoinEntitiesContext.class, 0)) {
            JoinEntitiesContext joinEntitiesContext =
                    ctx.getChild(JoinEntitiesContext.class, 0);
            processJoinEntitiesContext(joinEntitiesContext);
            processIntegerCompare(
                    getValue(ctx, IntegerCompareCommandContext.class),
                    getAliasAndAttribute(
                            getFinalEntityFromJoin(joinEntitiesContext),
                            getInternalNameObject(getValue(joinEntitiesContext,
                                    AttributeNameContext.class))),
                    getValue(ctx,
                            ComparisonOperatorContext.class),
                    getValue(ctx, IntegerValueContext.class));
        }
    }

    /**
     * enterFloatComparatorExpression
     * <p>
     * Used by sub-classes to convert the following OData queries to relevant
     * queries:
     * <p>
     * $filter=round(Decimal) eq 100
     * $filter=floor(Decimal) eq 0
     * $filter=ceiling(Decimal) eq 1
     *
     * @param ctx
     */
    @Override
    public void enterFloatComparatorExpression(
            FloatComparatorExpressionContext ctx) {
        super.enterFloatComparatorExpression(ctx);
        logger.debug("Entering exitIntegerComparatorExpression. Found [" +
                ctx.getText() + "]");
        processIntegerCompare(
                getValue(ctx, FloatCommandContext.class),
                getValue(ctx, AttributeNameContext.class),
                getValue(ctx, ComparisonOperatorContext.class),
                getValue(ctx, FloatOrIntegerValueContext.class));
    }

    @Override
    public void enterComparisonExpression(ComparisonExpressionContext ctx) {
        super.enterComparisonExpression(ctx);
        logger.debug("Entering enterComparisonExpression. Found [" +
                ctx.getText() + "]");

        JoinEntitiesContext joinEntitiesContext =
                ctx.getChild(JoinEntitiesContext.class, 0);
        if (null != joinEntitiesContext) {
            processJoinEntitiesContext(joinEntitiesContext);
            processComparatorCommand(
                    getAliasAndAttribute(this.entity,
                            getEnglishNameObject(
                                    getValue(joinEntitiesContext,
                                            AttributeNameContext.class))),
                    getValue(ctx, ComparisonOperatorContext.class),
                    getValue(ctx, ValueContext.class));
        } else {
            processComparatorCommand(
                    getAliasAndAttribute(this.entity,
                            getEnglishNameObject(
                                    getValue(ctx, AttributeNameContext.class))),
                    getValue(ctx, ComparisonOperatorContext.class),
                    getValue(ctx, ValueContext.class));
        }
    }

    @Override
    public void enterTopStatement(TopStatementContext ctx) {
        super.enterTopStatement(ctx);
        logger.debug("Entering enterTopStatement. Found [" +
                ctx.getText() + "]");
        processTopCommand(parseInt(getValue(ctx, IntegerValueContext.class)));
    }

    @Override
    public void enterSkipStatement(SkipStatementContext ctx) {
        super.enterSkipStatement(ctx);
        logger.debug("Entering enterSkipStatement. Found [" +
                ctx.getText() + "]");
        processSkipCommand(parseInt(getValue(ctx, IntegerValueContext.class)));
    }

    @Override
    public void enterOrderByClause(OrderByClauseContext ctx) {
        super.enterOrderByClause(ctx);
        logger.debug("Entering enterOrderByClause. Found [" +
                ctx.getText() + "]");
        processOrderByCommand(
                getValue(ctx, AttributeNameContext.class),
                getValue(ctx, OrderAscDescContext.class));
    }

    @Override
    public void enterEntityBase(EntityBaseContext ctx) {
        super.enterEntityBase(ctx);
        logger.debug("Entering enterEntityBase. Found [" +
                ctx.getText() + "]");

        this.entity = getInternalNameObject(getValue(
                ctx, EntityNameContext.class));
        // Process a join filter example e.g.
        // arkivstruktur/dokumentbeskrivelse/cf8e1d0d-e94d-4d07-b5ed
        // -46ba2df0465e/dokumentobjekt?$filter=contains(filnavn, 'fubar')
        if (null != ctx.getChild(SystemIdValueContext.class, 0)) {
            processEntityBase(
                    this.entity,
                    getInternalNameObject(
                            getValue(ctx, EntityNameContext.class, 1)),
                    getInternalNameObject(
                            getValue(ctx, SystemIdValueContext.class)));
        }
        // Process a basic filter example e.g.
        // arkivstruktur/dokumentobjekt?$filter=contains(filnavn, 'fubar')
        else {
            processEntityBase(this.entity);
        }
    }

    @Override
    public void enterReferenceStatement(ReferenceStatementContext ctx) {
        super.enterReferenceStatement(ctx);
        processReferenceStatement(
                getValue(ctx, EntityNameContext.class),
                getValue(ctx, SystemIdValueContext.class),
                getValue(ctx, EntityNameContext.class, 1),
                getValue(ctx, EntityNameContext.class, 2),
                getValue(ctx, SystemIdValueContext.class, 1));
    }

    /**
     * handle a IN clause:
     * <p>
     * arkivstruktur/mappe?$filter=klasse/klasseID eq '12/2'
     * arkivstruktur/mappe?$filter=klasse/klassifikasjonssystem/tittel eq 'GBNR'
     * <p>
     * The first one is straight forward to handle, while the second is handled
     * by retrieving the list of EntityNameContext and resolving as many joins
     * as required. Currently the approach is based on list but perhaps this
     * should be pushed back to the parser to be handled recursively.
     *
     * @param ctx the context (InComparisonExpressionContext)
     */
    @Override
    public void enterInComparisonExpression(
            InComparisonExpressionContext ctx) {
        super.enterInComparisonExpression(ctx);
        JoinEntitiesContext joinEntitiesContext =
                ctx.getChild(JoinEntitiesContext.class, 0);
        if (null != joinEntitiesContext) {
            processJoinEntitiesContext(joinEntitiesContext);
            AttributeNameContext attributeNameContext =
                    joinEntitiesContext.getChild(AttributeNameContext.class, 0);
            String attributeName = attributeNameContext.getText();
            processINCompare(getFinalEntityFromJoin(joinEntitiesContext),
                    getInternalNameObject(attributeName),
                    getValue(ctx, ComparisonOperatorContext.class),
                    getValue(ctx, ValueContext.class));
        } else {
            processINCompare(this.entity,
                    getInternalNameObject(
                            getValue(ctx, AttributeNameContext.class)),
                    getValue(ctx, ComparisonOperatorContext.class),
                    getValue(ctx, ValueContext.class));

        }
    }

    // joinEntities
    //   :
    //   (entityName '/')+ attributeName
    //   ;

    protected String getFinalEntityFromJoin(JoinEntitiesContext ctx) {
        List<EntityNameContext> entityNameContexts =
                ctx.getRuleContexts(EntityNameContext.class);
        return getInternalNameObject(
                getValue(ctx, EntityNameContext.class,
                        entityNameContexts.size() - 1));
    }

    protected void processJoinEntitiesContext(JoinEntitiesContext ctx) {
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
                    String fromEntity = getInternalNameObject(getValue(ctx,
                            EntityNameContext.class, i));
                    toEntity = getInternalNameObject(getValue(ctx,
                            EntityNameContext.class, i + 1));
                    addEntityToEntityJoin(fromEntity, toEntity);
                }
            }
        }
    }

    @Override
    public void enterLogicalOperator(LogicalOperatorContext ctx) {
        super.enterLogicalOperator(ctx);
        processLogicalOperator(ctx.getText());
    }

    @Override
    public void processEntityBase(String entity) {
    }

    @Override
    public void processEntityBase(String parentEntity, String entity,
                                  String systemId) {
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
        String englishName = CommonUtils.WebUtils
                .getEnglishNameDatabase(norwegianName);

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

    private String getValue(ParserRuleContext context, Class klass) {
        return context.getChild(klass, 0).getText();
    }

    private String getValue(ParserRuleContext context, Class klass, int count) {
        return context.getChild(klass, count).getText();
    }

    private String getAliasAndAttribute(String entity, String attribute) {
        return entity.toLowerCase() + "_1." + attribute;
    }
}
