// Generated from /home/tsodring/git/nikita-noark5-core/src/main/antlr4/ODataParser.g4 by ANTLR 4.8

package nikita.webapp.odata.base;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ODataParser}.
 */
public interface ODataParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ODataParser#referenceStatement}.
	 * @param ctx the parse tree
	 */
	void enterReferenceStatement(ODataParser.ReferenceStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#referenceStatement}.
	 * @param ctx the parse tree
	 */
	void exitReferenceStatement(ODataParser.ReferenceStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#odataQuery}.
	 * @param ctx the parse tree
	 */
	void enterOdataQuery(ODataParser.OdataQueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#odataQuery}.
	 * @param ctx the parse tree
	 */
	void exitOdataQuery(ODataParser.OdataQueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#entityBase}.
	 * @param ctx the parse tree
	 */
	void enterEntityBase(ODataParser.EntityBaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#entityBase}.
	 * @param ctx the parse tree
	 */
	void exitEntityBase(ODataParser.EntityBaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#odataCommand}.
	 * @param ctx the parse tree
	 */
	void enterOdataCommand(ODataParser.OdataCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#odataCommand}.
	 * @param ctx the parse tree
	 */
	void exitOdataCommand(ODataParser.OdataCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(ODataParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(ODataParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#filterStatement}.
	 * @param ctx the parse tree
	 */
	void enterFilterStatement(ODataParser.FilterStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#filterStatement}.
	 * @param ctx the parse tree
	 */
	void exitFilterStatement(ODataParser.FilterStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#countStatement}.
	 * @param ctx the parse tree
	 */
	void enterCountStatement(ODataParser.CountStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#countStatement}.
	 * @param ctx the parse tree
	 */
	void exitCountStatement(ODataParser.CountStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#topStatement}.
	 * @param ctx the parse tree
	 */
	void enterTopStatement(ODataParser.TopStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#topStatement}.
	 * @param ctx the parse tree
	 */
	void exitTopStatement(ODataParser.TopStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#skipStatement}.
	 * @param ctx the parse tree
	 */
	void enterSkipStatement(ODataParser.SkipStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#skipStatement}.
	 * @param ctx the parse tree
	 */
	void exitSkipStatement(ODataParser.SkipStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#filterExpression}.
	 * @param ctx the parse tree
	 */
	void enterFilterExpression(ODataParser.FilterExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#filterExpression}.
	 * @param ctx the parse tree
	 */
	void exitFilterExpression(ODataParser.FilterExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#boolExpressionLeft}.
	 * @param ctx the parse tree
	 */
	void enterBoolExpressionLeft(ODataParser.BoolExpressionLeftContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#boolExpressionLeft}.
	 * @param ctx the parse tree
	 */
	void exitBoolExpressionLeft(ODataParser.BoolExpressionLeftContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#boolExpressionRight}.
	 * @param ctx the parse tree
	 */
	void enterBoolExpressionRight(ODataParser.BoolExpressionRightContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#boolExpressionRight}.
	 * @param ctx the parse tree
	 */
	void exitBoolExpressionRight(ODataParser.BoolExpressionRightContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#stringCompareExpression}.
     *
     * @param ctx the parse tree
     */
    void enterStringCompareExpression(ODataParser.StringCompareExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#stringCompareExpression}.
     *
     * @param ctx the parse tree
     */
    void exitStringCompareExpression(ODataParser.StringCompareExpressionContext ctx);

    /**
     * Enter a parse tree produced by {@link ODataParser#inComparisonExpression}.
     *
     * @param ctx the parse tree
     */
    void enterInComparisonExpression(ODataParser.InComparisonExpressionContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#inComparisonExpression}.
     *
     * @param ctx the parse tree
     */
    void exitInComparisonExpression(ODataParser.InComparisonExpressionContext ctx);

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
     * Enter a parse tree produced by {@link ODataParser#stringCompareCommand}.
     *
     * @param ctx the parse tree
     */
    void enterStringCompareCommand(ODataParser.StringCompareCommandContext ctx);

    /**
     * Exit a parse tree produced by {@link ODataParser#stringCompareCommand}.
     *
     * @param ctx the parse tree
     */
    void exitStringCompareCommand(ODataParser.StringCompareCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#substringExpression}.
	 * @param ctx the parse tree
	 */
	void enterSubstringExpression(ODataParser.SubstringExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#substringExpression}.
	 * @param ctx the parse tree
	 */
	void exitSubstringExpression(ODataParser.SubstringExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#indexOfExpression}.
	 * @param ctx the parse tree
	 */
	void enterIndexOfExpression(ODataParser.IndexOfExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#indexOfExpression}.
	 * @param ctx the parse tree
	 */
	void exitIndexOfExpression(ODataParser.IndexOfExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#lengthExpression}.
	 * @param ctx the parse tree
	 */
	void enterLengthExpression(ODataParser.LengthExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#lengthExpression}.
	 * @param ctx the parse tree
	 */
	void exitLengthExpression(ODataParser.LengthExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#timeExpression}.
	 * @param ctx the parse tree
	 */
	void enterTimeExpression(ODataParser.TimeExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#timeExpression}.
	 * @param ctx the parse tree
	 */
	void exitTimeExpression(ODataParser.TimeExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#stringModifierExpression}.
	 * @param ctx the parse tree
	 */
	void enterStringModifierExpression(ODataParser.StringModifierExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#stringModifierExpression}.
	 * @param ctx the parse tree
	 */
	void exitStringModifierExpression(ODataParser.StringModifierExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#concatExpression}.
	 * @param ctx the parse tree
	 */
	void enterConcatExpression(ODataParser.ConcatExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#concatExpression}.
	 * @param ctx the parse tree
	 */
	void exitConcatExpression(ODataParser.ConcatExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void enterComparisonExpression(ODataParser.ComparisonExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#comparisonExpression}.
	 * @param ctx the parse tree
	 */
	void exitComparisonExpression(ODataParser.ComparisonExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#integerComparatorExpression}.
	 * @param ctx the parse tree
	 */
	void enterIntegerComparatorExpression(ODataParser.IntegerComparatorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#integerComparatorExpression}.
	 * @param ctx the parse tree
	 */
	void exitIntegerComparatorExpression(ODataParser.IntegerComparatorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#integerCompareCommand}.
	 * @param ctx the parse tree
	 */
	void enterIntegerCompareCommand(ODataParser.IntegerCompareCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#integerCompareCommand}.
	 * @param ctx the parse tree
	 */
	void exitIntegerCompareCommand(ODataParser.IntegerCompareCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#floatComparatorExpression}.
	 * @param ctx the parse tree
	 */
	void enterFloatComparatorExpression(ODataParser.FloatComparatorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#floatComparatorExpression}.
	 * @param ctx the parse tree
	 */
	void exitFloatComparatorExpression(ODataParser.FloatComparatorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#floatCommand}.
	 * @param ctx the parse tree
	 */
	void enterFloatCommand(ODataParser.FloatCommandContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#floatCommand}.
	 * @param ctx the parse tree
	 */
	void exitFloatCommand(ODataParser.FloatCommandContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#floatOrIntegerValue}.
	 * @param ctx the parse tree
	 */
	void enterFloatOrIntegerValue(ODataParser.FloatOrIntegerValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#floatOrIntegerValue}.
	 * @param ctx the parse tree
	 */
	void exitFloatOrIntegerValue(ODataParser.FloatOrIntegerValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void enterOrderByClause(ODataParser.OrderByClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#orderByClause}.
	 * @param ctx the parse tree
	 */
	void exitOrderByClause(ODataParser.OrderByClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#orderByExpression}.
	 * @param ctx the parse tree
	 */
	void enterOrderByExpression(ODataParser.OrderByExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#orderByExpression}.
	 * @param ctx the parse tree
	 */
	void exitOrderByExpression(ODataParser.OrderByExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#orderAscDesc}.
	 * @param ctx the parse tree
	 */
	void enterOrderAscDesc(ODataParser.OrderAscDescContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#orderAscDesc}.
	 * @param ctx the parse tree
	 */
	void exitOrderAscDesc(ODataParser.OrderAscDescContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void enterComparisonOperator(ODataParser.ComparisonOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#comparisonOperator}.
	 * @param ctx the parse tree
	 */
	void exitComparisonOperator(ODataParser.ComparisonOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void enterLogicalOperator(ODataParser.LogicalOperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#logicalOperator}.
	 * @param ctx the parse tree
	 */
	void exitLogicalOperator(ODataParser.LogicalOperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#columnName}.
	 * @param ctx the parse tree
	 */
	void enterColumnName(ODataParser.ColumnNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#columnName}.
	 * @param ctx the parse tree
	 */
	void exitColumnName(ODataParser.ColumnNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#entityName}.
	 * @param ctx the parse tree
	 */
	void enterEntityName(ODataParser.EntityNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#entityName}.
	 * @param ctx the parse tree
	 */
	void exitEntityName(ODataParser.EntityNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void enterAttributeName(ODataParser.AttributeNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#attributeName}.
	 * @param ctx the parse tree
	 */
	void exitAttributeName(ODataParser.AttributeNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#packageName}.
	 * @param ctx the parse tree
	 */
	void enterPackageName(ODataParser.PackageNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#packageName}.
	 * @param ctx the parse tree
	 */
	void exitPackageName(ODataParser.PackageNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(ODataParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(ODataParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#singleQuotedString}.
	 * @param ctx the parse tree
	 */
	void enterSingleQuotedString(ODataParser.SingleQuotedStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#singleQuotedString}.
	 * @param ctx the parse tree
	 */
	void exitSingleQuotedString(ODataParser.SingleQuotedStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#integerValue}.
	 * @param ctx the parse tree
	 */
	void enterIntegerValue(ODataParser.IntegerValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#integerValue}.
	 * @param ctx the parse tree
	 */
	void exitIntegerValue(ODataParser.IntegerValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#floatValue}.
	 * @param ctx the parse tree
	 */
	void enterFloatValue(ODataParser.FloatValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#floatValue}.
	 * @param ctx the parse tree
	 */
	void exitFloatValue(ODataParser.FloatValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ODataParser#systemIdValue}.
	 * @param ctx the parse tree
	 */
	void enterSystemIdValue(ODataParser.SystemIdValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ODataParser#systemIdValue}.
	 * @param ctx the parse tree
	 */
	void exitSystemIdValue(ODataParser.SystemIdValueContext ctx);
}