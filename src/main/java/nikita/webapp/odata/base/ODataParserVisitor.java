// Generated from /home/tsodring/git/nikita-noark5-core/src/main/antlr4/ODataParser.g4 by ANTLR 4.8

package nikita.webapp.odata.base;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ODataParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public interface ODataParserVisitor<T> extends ParseTreeVisitor<T> {
    /**
     * Visit a parse tree produced by {@link ODataParser#odataRelativeUri}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOdataRelativeUri(ODataParser.OdataRelativeUriContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#resourcePath}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitResourcePath(ODataParser.ResourcePathContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#entity}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEntity(ODataParser.EntityContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#entityCast}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEntityCast(ODataParser.EntityCastContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#entityUUID}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEntityUUID(ODataParser.EntityUUIDContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#embeddedEntitySet}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEmbeddedEntitySet(ODataParser.EmbeddedEntitySetContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#queryOptions}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitQueryOptions(ODataParser.QueryOptionsContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#queryOption}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitQueryOption(ODataParser.QueryOptionContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#filter}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFilter(ODataParser.FilterContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#expand}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitExpand(ODataParser.ExpandContext ctx);

    /**
     * Visit a parse tree produced by the {@code binaryExpression}
     * labeled alternative in {@link ODataParser#filterExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBinaryExpression(ODataParser.BinaryExpressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code boolExpression}
     * labeled alternative in {@link ODataParser#filterExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBoolExpression(ODataParser.BoolExpressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code parenExpression}
     * labeled alternative in {@link ODataParser#filterExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitParenExpression(ODataParser.ParenExpressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code comparatorExpression}
     * labeled alternative in {@link ODataParser#boolCommonExpr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitComparatorExpression(ODataParser.ComparatorExpressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code countComparatorExpression}
     * labeled alternative in {@link ODataParser#boolCommonExpr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCountComparatorExpression(ODataParser.CountComparatorExpressionContext ctx);

    /**
     * Visit a parse tree produced by the {@code compareMethodExpression}
     * labeled alternative in {@link ODataParser#boolCommonExpr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCompareMethodExpression(ODataParser.CompareMethodExpressionContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#leftComparatorExpr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLeftComparatorExpr(ODataParser.LeftComparatorExprContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#rightComparatorExpr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitRightComparatorExpr(ODataParser.RightComparatorExprContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#orderBy}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOrderBy(ODataParser.OrderByContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#orderByItem}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOrderByItem(ODataParser.OrderByItemContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#topStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTopStatement(ODataParser.TopStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#skipStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSkipStatement(ODataParser.SkipStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#joinEntities}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitJoinEntities(ODataParser.JoinEntitiesContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#commonExpr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCommonExpr(ODataParser.CommonExprContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#mathExpr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMathExpr(ODataParser.MathExprContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#methodExpr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMethodExpr(ODataParser.MethodExprContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#compareMethodExpr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCompareMethodExpr(ODataParser.CompareMethodExprContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#methodCallExpr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMethodCallExpr(ODataParser.MethodCallExprContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#calenderMethodExp}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCalenderMethodExp(ODataParser.CalenderMethodExpContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#concatMethodExpr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConcatMethodExpr(ODataParser.ConcatMethodExprContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#singleMethodCallExpr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSingleMethodCallExpr(ODataParser.SingleMethodCallExprContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#substringMethodCallExpr}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSubstringMethodCallExpr(ODataParser.SubstringMethodCallExprContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#comparisonOperator}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitComparisonOperator(ODataParser.ComparisonOperatorContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#compareMethodName}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCompareMethodName(ODataParser.CompareMethodNameContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#methodName}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitMethodName(ODataParser.MethodNameContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#calenderMethodName}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCalenderMethodName(ODataParser.CalenderMethodNameContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#number}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNumber(ODataParser.NumberContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#primitiveLiteral}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPrimitiveLiteral(ODataParser.PrimitiveLiteralContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#countStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCountStatement(ODataParser.CountStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#openPar}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOpenPar(ODataParser.OpenParContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#closePar}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitClosePar(ODataParser.CloseParContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#logicalOperator}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLogicalOperator(ODataParser.LogicalOperatorContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#sortOrder}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSortOrder(ODataParser.SortOrderContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#entityName}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEntityName(ODataParser.EntityNameContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#attributeName}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAttributeName(ODataParser.AttributeNameContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#uuidIdValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitUuidIdValue(ODataParser.UuidIdValueContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#quotedString}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitQuotedString(ODataParser.QuotedStringContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#nullSpecLiteral}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNullSpecLiteral(ODataParser.NullSpecLiteralContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#nullToken}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitNullToken(ODataParser.NullTokenContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#booleanValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBooleanValue(ODataParser.BooleanValueContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#durationValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDurationValue(ODataParser.DurationValueContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#dateValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDateValue(ODataParser.DateValueContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#dateTimeOffsetValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDateTimeOffsetValue(ODataParser.DateTimeOffsetValueContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#timeOfDayValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTimeOfDayValue(ODataParser.TimeOfDayValueContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#decimalLiteral}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitDecimalLiteral(ODataParser.DecimalLiteralContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#floatValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFloatValue(ODataParser.FloatValueContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#integerValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIntegerValue(ODataParser.IntegerValueContext ctx);
}