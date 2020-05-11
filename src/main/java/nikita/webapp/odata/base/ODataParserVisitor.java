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
     * Visit a parse tree produced by {@link ODataParser#referenceStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitReferenceStatement(ODataParser.ReferenceStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#odataQuery}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOdataQuery(ODataParser.OdataQueryContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#entityBase}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitEntityBase(ODataParser.EntityBaseContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#odataCommand}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOdataCommand(ODataParser.OdataCommandContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#predicate}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPredicate(ODataParser.PredicateContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#filterStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFilterStatement(ODataParser.FilterStatementContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#countStatement}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitCountStatement(ODataParser.CountStatementContext ctx);

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
     * Visit a parse tree produced by {@link ODataParser#filterExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFilterExpression(ODataParser.FilterExpressionContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#boolExpressionLeft}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBoolExpressionLeft(ODataParser.BoolExpressionLeftContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#boolExpressionRight}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitBoolExpressionRight(ODataParser.BoolExpressionRightContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#stringCompareExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStringCompareExpression(ODataParser.StringCompareExpressionContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#inComparisonExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitInComparisonExpression(ODataParser.InComparisonExpressionContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#joinEntities}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitJoinEntities(ODataParser.JoinEntitiesContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#stringCompareCommand}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStringCompareCommand(ODataParser.StringCompareCommandContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#substringExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSubstringExpression(ODataParser.SubstringExpressionContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#indexOfExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIndexOfExpression(ODataParser.IndexOfExpressionContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#lengthExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLengthExpression(ODataParser.LengthExpressionContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#timeExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTimeExpression(ODataParser.TimeExpressionContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#stringModifierExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitStringModifierExpression(ODataParser.StringModifierExpressionContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#concatExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitConcatExpression(ODataParser.ConcatExpressionContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#comparisonExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitComparisonExpression(ODataParser.ComparisonExpressionContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#integerComparatorExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIntegerComparatorExpression(ODataParser.IntegerComparatorExpressionContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#integerCompareCommand}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIntegerCompareCommand(ODataParser.IntegerCompareCommandContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#floatComparatorExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFloatComparatorExpression(ODataParser.FloatComparatorExpressionContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#floatCommand}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFloatCommand(ODataParser.FloatCommandContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#floatOrIntegerValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFloatOrIntegerValue(ODataParser.FloatOrIntegerValueContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#orderByClause}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOrderByClause(ODataParser.OrderByClauseContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#orderByExpression}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOrderByExpression(ODataParser.OrderByExpressionContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#orderAscDesc}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOrderAscDesc(ODataParser.OrderAscDescContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#comparisonOperator}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitComparisonOperator(ODataParser.ComparisonOperatorContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#logicalOperator}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitLogicalOperator(ODataParser.LogicalOperatorContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#columnName}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitColumnName(ODataParser.ColumnNameContext ctx);

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
     * Visit a parse tree produced by {@link ODataParser#packageName}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitPackageName(ODataParser.PackageNameContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#value}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitValue(ODataParser.ValueContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#singleQuotedString}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSingleQuotedString(ODataParser.SingleQuotedStringContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#integerValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitIntegerValue(ODataParser.IntegerValueContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#floatValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitFloatValue(ODataParser.FloatValueContext ctx);

    /**
     * Visit a parse tree produced by {@link ODataParser#systemIdValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSystemIdValue(ODataParser.SystemIdValueContext ctx);
}