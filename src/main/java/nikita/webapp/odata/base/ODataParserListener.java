// Generated from /home/tsodring/git/nikita-noark5-core/src/main/antlr4/ODataParser.g4 by ANTLR 4.8

package nikita.webapp.odata.base;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ODataParser}.
 */
public interface ODataParserListener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link ODataParser#odataRelativeUri}.
     *
     * @param ctx the parse tree
     */
    void enterOdataRelativeUri(ODataParser.OdataRelativeUriContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#odataRelativeUri}.
     *
     * @param ctx the parse tree
     */
    void exitOdataRelativeUri(ODataParser.OdataRelativeUriContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#resourcePath}.
     *
     * @param ctx the parse tree
     */
    void enterResourcePath(ODataParser.ResourcePathContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#resourcePath}.
     *
     * @param ctx the parse tree
     */
    void exitResourcePath(ODataParser.ResourcePathContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#entity}.
     *
     * @param ctx the parse tree
     */
    void enterEntity(ODataParser.EntityContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#entity}.
     *
     * @param ctx the parse tree
     */
    void exitEntity(ODataParser.EntityContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#entityCast}.
     *
     * @param ctx the parse tree
     */
    void enterEntityCast(ODataParser.EntityCastContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#entityCast}.
     *
     * @param ctx the parse tree
     */
    void exitEntityCast(ODataParser.EntityCastContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#entityUUID}.
     *
     * @param ctx the parse tree
     */
    void enterEntityUUID(ODataParser.EntityUUIDContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#entityUUID}.
     *
     * @param ctx the parse tree
     */
    void exitEntityUUID(ODataParser.EntityUUIDContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#embeddedEntitySet}.
     *
     * @param ctx the parse tree
     */
    void enterEmbeddedEntitySet(ODataParser.EmbeddedEntitySetContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#embeddedEntitySet}.
     *
     * @param ctx the parse tree
     */
    void exitEmbeddedEntitySet(ODataParser.EmbeddedEntitySetContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#queryOptions}.
     *
     * @param ctx the parse tree
     */
    void enterQueryOptions(ODataParser.QueryOptionsContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#queryOptions}.
     *
     * @param ctx the parse tree
     */
    void exitQueryOptions(ODataParser.QueryOptionsContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#queryOption}.
     *
     * @param ctx the parse tree
     */
    void enterQueryOption(ODataParser.QueryOptionContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#queryOption}.
     *
     * @param ctx the parse tree
     */
    void exitQueryOption(ODataParser.QueryOptionContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#filter}.
     *
     * @param ctx the parse tree
     */
    void enterFilter(ODataParser.FilterContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#filter}.
     *
     * @param ctx the parse tree
     */
    void exitFilter(ODataParser.FilterContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#expand}.
     *
     * @param ctx the parse tree
     */
    void enterExpand(ODataParser.ExpandContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#expand}.
     *
     * @param ctx the parse tree
     */
    void exitExpand(ODataParser.ExpandContext ctx);

    /**
     * Enter a parse tree produced by the {@code binaryExpression}
     * labeled alternative in {@link ODataParser#filterExpression}.
     *
     * @param ctx the parse tree
     */
    void enterBinaryExpression(ODataParser.BinaryExpressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code binaryExpression}
     * labeled alternative in {@link ODataParser#filterExpression}.
     *
     * @param ctx the parse tree
     */
    void exitBinaryExpression(ODataParser.BinaryExpressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code boolExpression}
     * labeled alternative in {@link ODataParser#filterExpression}.
     *
     * @param ctx the parse tree
     */
    void enterBoolExpression(ODataParser.BoolExpressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code boolExpression}
     * labeled alternative in {@link ODataParser#filterExpression}.
     *
     * @param ctx the parse tree
     */
    void exitBoolExpression(ODataParser.BoolExpressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code parenExpression}
     * labeled alternative in {@link ODataParser#filterExpression}.
     *
     * @param ctx the parse tree
     */
    void enterParenExpression(ODataParser.ParenExpressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code parenExpression}
     * labeled alternative in {@link ODataParser#filterExpression}.
     *
     * @param ctx the parse tree
     */
    void exitParenExpression(ODataParser.ParenExpressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code comparatorExpression}
     * labeled alternative in {@link ODataParser#boolCommonExpr}.
     *
     * @param ctx the parse tree
     */
    void enterComparatorExpression(ODataParser.ComparatorExpressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code comparatorExpression}
     * labeled alternative in {@link ODataParser#boolCommonExpr}.
     *
     * @param ctx the parse tree
     */
    void exitComparatorExpression(ODataParser.ComparatorExpressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code countComparatorExpression}
     * labeled alternative in {@link ODataParser#boolCommonExpr}.
     *
     * @param ctx the parse tree
     */
    void enterCountComparatorExpression(ODataParser.CountComparatorExpressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code countComparatorExpression}
     * labeled alternative in {@link ODataParser#boolCommonExpr}.
     *
     * @param ctx the parse tree
     */
    void exitCountComparatorExpression(ODataParser.CountComparatorExpressionContext ctx);

    /**
     * Enter a parse tree produced by the {@code compareMethodExpression}
     * labeled alternative in {@link ODataParser#boolCommonExpr}.
     *
     * @param ctx the parse tree
     */
    void enterCompareMethodExpression(ODataParser.CompareMethodExpressionContext ctx);

    /**
     * Exit a parse tree produced by the {@code compareMethodExpression}
     * labeled alternative in {@link ODataParser#boolCommonExpr}.
     *
     * @param ctx the parse tree
     */
    void exitCompareMethodExpression(ODataParser.CompareMethodExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#leftComparatorExpr}.
     *
     * @param ctx the parse tree
     */
    void enterLeftComparatorExpr(ODataParser.LeftComparatorExprContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#leftComparatorExpr}.
     *
     * @param ctx the parse tree
     */
    void exitLeftComparatorExpr(ODataParser.LeftComparatorExprContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#rightComparatorExpr}.
     *
     * @param ctx the parse tree
     */
    void enterRightComparatorExpr(ODataParser.RightComparatorExprContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#rightComparatorExpr}.
     *
     * @param ctx the parse tree
     */
    void exitRightComparatorExpr(ODataParser.RightComparatorExprContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#orderBy}.
     *
     * @param ctx the parse tree
     */
    void enterOrderBy(ODataParser.OrderByContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#orderBy}.
     *
     * @param ctx the parse tree
     */
    void exitOrderBy(ODataParser.OrderByContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#orderByItem}.
     *
     * @param ctx the parse tree
     */
    void enterOrderByItem(ODataParser.OrderByItemContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#orderByItem}.
     *
     * @param ctx the parse tree
     */
    void exitOrderByItem(ODataParser.OrderByItemContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#topStatement}.
     *
     * @param ctx the parse tree
     */
    void enterTopStatement(ODataParser.TopStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#topStatement}.
     *
     * @param ctx the parse tree
     */
    void exitTopStatement(ODataParser.TopStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#skipStatement}.
     *
     * @param ctx the parse tree
     */
    void enterSkipStatement(ODataParser.SkipStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#skipStatement}.
     *
     * @param ctx the parse tree
     */
    void exitSkipStatement(ODataParser.SkipStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#joinEntities}.
     *
     * @param ctx the parse tree
     */
    void enterJoinEntities(ODataParser.JoinEntitiesContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#joinEntities}.
     *
     * @param ctx the parse tree
     */
    void exitJoinEntities(ODataParser.JoinEntitiesContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#commonExpr}.
     *
     * @param ctx the parse tree
     */
    void enterCommonExpr(ODataParser.CommonExprContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#commonExpr}.
     *
     * @param ctx the parse tree
     */
    void exitCommonExpr(ODataParser.CommonExprContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#mathExpr}.
     *
     * @param ctx the parse tree
     */
    void enterMathExpr(ODataParser.MathExprContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#mathExpr}.
     *
     * @param ctx the parse tree
     */
    void exitMathExpr(ODataParser.MathExprContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#methodExpr}.
     *
     * @param ctx the parse tree
     */
    void enterMethodExpr(ODataParser.MethodExprContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#methodExpr}.
     *
     * @param ctx the parse tree
     */
    void exitMethodExpr(ODataParser.MethodExprContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#compareMethodExpr}.
     *
     * @param ctx the parse tree
     */
    void enterCompareMethodExpr(ODataParser.CompareMethodExprContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#compareMethodExpr}.
     *
     * @param ctx the parse tree
     */
    void exitCompareMethodExpr(ODataParser.CompareMethodExprContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#methodCallExpr}.
     *
     * @param ctx the parse tree
     */
    void enterMethodCallExpr(ODataParser.MethodCallExprContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#methodCallExpr}.
     *
     * @param ctx the parse tree
     */
    void exitMethodCallExpr(ODataParser.MethodCallExprContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#calenderMethodExp}.
     *
     * @param ctx the parse tree
     */
    void enterCalenderMethodExp(ODataParser.CalenderMethodExpContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#calenderMethodExp}.
     *
     * @param ctx the parse tree
     */
    void exitCalenderMethodExp(ODataParser.CalenderMethodExpContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#concatMethodExpr}.
     *
     * @param ctx the parse tree
     */
    void enterConcatMethodExpr(ODataParser.ConcatMethodExprContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#concatMethodExpr}.
     *
     * @param ctx the parse tree
     */
    void exitConcatMethodExpr(ODataParser.ConcatMethodExprContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#singleMethodCallExpr}.
     *
     * @param ctx the parse tree
     */
    void enterSingleMethodCallExpr(ODataParser.SingleMethodCallExprContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#singleMethodCallExpr}.
     *
     * @param ctx the parse tree
     */
    void exitSingleMethodCallExpr(ODataParser.SingleMethodCallExprContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#substringMethodCallExpr}.
     *
     * @param ctx the parse tree
     */
    void enterSubstringMethodCallExpr(ODataParser.SubstringMethodCallExprContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#substringMethodCallExpr}.
     *
     * @param ctx the parse tree
     */
    void exitSubstringMethodCallExpr(ODataParser.SubstringMethodCallExprContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#comparisonOperator}.
     *
     * @param ctx the parse tree
     */
    void enterComparisonOperator(ODataParser.ComparisonOperatorContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#comparisonOperator}.
     *
     * @param ctx the parse tree
     */
    void exitComparisonOperator(ODataParser.ComparisonOperatorContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#compareMethodName}.
     *
     * @param ctx the parse tree
     */
    void enterCompareMethodName(ODataParser.CompareMethodNameContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#compareMethodName}.
     *
     * @param ctx the parse tree
     */
    void exitCompareMethodName(ODataParser.CompareMethodNameContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#methodName}.
     *
     * @param ctx the parse tree
     */
    void enterMethodName(ODataParser.MethodNameContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#methodName}.
     *
     * @param ctx the parse tree
     */
    void exitMethodName(ODataParser.MethodNameContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#calenderMethodName}.
     *
     * @param ctx the parse tree
     */
    void enterCalenderMethodName(ODataParser.CalenderMethodNameContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#calenderMethodName}.
     *
     * @param ctx the parse tree
     */
    void exitCalenderMethodName(ODataParser.CalenderMethodNameContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#number}.
     *
     * @param ctx the parse tree
     */
    void enterNumber(ODataParser.NumberContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#number}.
     *
     * @param ctx the parse tree
     */
    void exitNumber(ODataParser.NumberContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#primitiveLiteral}.
     *
     * @param ctx the parse tree
     */
    void enterPrimitiveLiteral(ODataParser.PrimitiveLiteralContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#primitiveLiteral}.
     *
     * @param ctx the parse tree
     */
    void exitPrimitiveLiteral(ODataParser.PrimitiveLiteralContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#countStatement}.
     *
     * @param ctx the parse tree
     */
    void enterCountStatement(ODataParser.CountStatementContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#countStatement}.
     *
     * @param ctx the parse tree
     */
    void exitCountStatement(ODataParser.CountStatementContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#openPar}.
     *
     * @param ctx the parse tree
     */
    void enterOpenPar(ODataParser.OpenParContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#openPar}.
     *
     * @param ctx the parse tree
     */
    void exitOpenPar(ODataParser.OpenParContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#closePar}.
     *
     * @param ctx the parse tree
     */
    void enterClosePar(ODataParser.CloseParContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#closePar}.
     *
     * @param ctx the parse tree
     */
    void exitClosePar(ODataParser.CloseParContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#logicalOperator}.
     *
     * @param ctx the parse tree
     */
    void enterLogicalOperator(ODataParser.LogicalOperatorContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#logicalOperator}.
     *
     * @param ctx the parse tree
     */
    void exitLogicalOperator(ODataParser.LogicalOperatorContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#sortOrder}.
     *
     * @param ctx the parse tree
     */
    void enterSortOrder(ODataParser.SortOrderContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#sortOrder}.
     *
     * @param ctx the parse tree
     */
    void exitSortOrder(ODataParser.SortOrderContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#entityName}.
     *
     * @param ctx the parse tree
     */
    void enterEntityName(ODataParser.EntityNameContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#entityName}.
     *
     * @param ctx the parse tree
     */
    void exitEntityName(ODataParser.EntityNameContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#attributeName}.
     *
     * @param ctx the parse tree
     */
    void enterAttributeName(ODataParser.AttributeNameContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#attributeName}.
     *
     * @param ctx the parse tree
     */
    void exitAttributeName(ODataParser.AttributeNameContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#uuidIdValue}.
     *
     * @param ctx the parse tree
     */
    void enterUuidIdValue(ODataParser.UuidIdValueContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#uuidIdValue}.
     *
     * @param ctx the parse tree
     */
    void exitUuidIdValue(ODataParser.UuidIdValueContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#quotedString}.
     *
     * @param ctx the parse tree
     */
    void enterQuotedString(ODataParser.QuotedStringContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#quotedString}.
     *
     * @param ctx the parse tree
     */
    void exitQuotedString(ODataParser.QuotedStringContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#nullSpecLiteral}.
     *
     * @param ctx the parse tree
     */
    void enterNullSpecLiteral(ODataParser.NullSpecLiteralContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#nullSpecLiteral}.
     *
     * @param ctx the parse tree
     */
    void exitNullSpecLiteral(ODataParser.NullSpecLiteralContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#nullToken}.
     *
     * @param ctx the parse tree
     */
    void enterNullToken(ODataParser.NullTokenContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#nullToken}.
     *
     * @param ctx the parse tree
     */
    void exitNullToken(ODataParser.NullTokenContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#booleanValue}.
     *
     * @param ctx the parse tree
     */
    void enterBooleanValue(ODataParser.BooleanValueContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#booleanValue}.
     *
     * @param ctx the parse tree
     */
    void exitBooleanValue(ODataParser.BooleanValueContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#durationValue}.
     *
     * @param ctx the parse tree
     */
    void enterDurationValue(ODataParser.DurationValueContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#durationValue}.
     *
     * @param ctx the parse tree
     */
    void exitDurationValue(ODataParser.DurationValueContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#dateValue}.
     *
     * @param ctx the parse tree
     */
    void enterDateValue(ODataParser.DateValueContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#dateValue}.
     *
     * @param ctx the parse tree
     */
    void exitDateValue(ODataParser.DateValueContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#dateTimeOffsetValue}.
     *
     * @param ctx the parse tree
     */
    void enterDateTimeOffsetValue(ODataParser.DateTimeOffsetValueContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#dateTimeOffsetValue}.
     *
     * @param ctx the parse tree
     */
    void exitDateTimeOffsetValue(ODataParser.DateTimeOffsetValueContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#timeOfDayValue}.
     *
     * @param ctx the parse tree
     */
    void enterTimeOfDayValue(ODataParser.TimeOfDayValueContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#timeOfDayValue}.
     *
     * @param ctx the parse tree
     */
    void exitTimeOfDayValue(ODataParser.TimeOfDayValueContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#decimalLiteral}.
     *
     * @param ctx the parse tree
     */
    void enterDecimalLiteral(ODataParser.DecimalLiteralContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#decimalLiteral}.
     *
     * @param ctx the parse tree
     */
    void exitDecimalLiteral(ODataParser.DecimalLiteralContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#floatValue}.
     *
     * @param ctx the parse tree
     */
    void enterFloatValue(ODataParser.FloatValueContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#floatValue}.
     *
     * @param ctx the parse tree
     */
    void exitFloatValue(ODataParser.FloatValueContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#integerValue}.
     *
     * @param ctx the parse tree
     */
    void enterIntegerValue(ODataParser.IntegerValueContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#integerValue}.
     *
     * @param ctx the parse tree
     */
    void exitIntegerValue(ODataParser.IntegerValueContext ctx);
}