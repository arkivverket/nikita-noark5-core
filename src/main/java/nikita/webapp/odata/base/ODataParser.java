// Generated from /home/tsodring/git/nikita-noark5-core/src/main/antlr4/ODataParser.g4 by ANTLR 4.8

package nikita.webapp.odata.base;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ODataParser extends Parser {
	public static final int
			SPACE = 1, FILTER = 2, TOP = 3, SKIPRULE = 4, ORDERBY = 5, REF = 6, EXPAND = 7, COUNT = 8,
			SELECT = 9, DOLLARID = 10, CONTAINS = 11, STARTSWITH = 12, ENDSWITH = 13, SUBSTRINGOF = 14,
			LENGTH = 15, INDEXOF = 16, REPLACE = 17, SUBSTRING = 18, TOLOWER = 19, TOUPPER = 20,
			TRIM = 21, CONCAT = 22, DAY = 23, MONTH = 24, YEAR = 25, HOUR = 26, MINUTE = 27, SECOND = 28,
			NOW = 29, MAXDATETIME = 30, MINDATETIME = 31, TOTALOFFSETMINUTES = 32, ROUND = 33,
			FLOOR = 34, CEILING = 35, EQ = 36, GT = 37, LT = 38, GE = 39, LE = 40, NE = 41, ORDER = 42,
			BY = 43, DESC = 44, ASC = 45, OR = 46, AND = 47, NOT = 48, SEPERATOR = 49, HTTP = 50,
			HTTPS = 51, LR_BRACKET = 52, RR_BRACKET = 53, COMMA = 54, QUESTION = 55, SEMI = 56,
			AT_SIGN = 57, SINGLE_QUOTE_SYMB = 58, DOUBLE_QUOTE_SYMB = 59, REVERSE_QUOTE_SYMB = 60,
			COLON_SYMB = 61, AMPERSAND = 62, NULL_LITERAL = 63, NULL_SPEC_LITERAL = 64, DOT = 65,
			SLASH = 66, UUID = 67, NOARK_ENTITY = 68, INTEGER = 69, FLOAT = 70, ID = 71, QUOTED_STRING = 72,
			STRING_LITERAL = 73, DECIMAL_LITERAL = 74, HEXADECIMAL_LITERAL = 75, ODATA_ARK = 76,
			HEX = 77, STRING = 78, ERROR_RECONGNIGION = 79;
	public static final int
			RULE_referenceStatement = 0, RULE_odataQuery = 1, RULE_entityBase = 2,
			RULE_odataCommand = 3, RULE_predicate = 4, RULE_filterStatement = 5, RULE_countStatement = 6,
			RULE_topStatement = 7, RULE_skipStatement = 8, RULE_filterExpression = 9,
			RULE_boolExpressionLeft = 10, RULE_boolExpressionRight = 11, RULE_stringCompareExpression = 12,
			RULE_inComparisonExpression = 13, RULE_joinEntities = 14, RULE_stringCompareCommand = 15,
			RULE_substringExpression = 16, RULE_indexOfExpression = 17, RULE_lengthExpression = 18,
			RULE_timeExpression = 19, RULE_stringModifierExpression = 20, RULE_concatExpression = 21,
			RULE_comparisonExpression = 22, RULE_integerComparatorExpression = 23,
			RULE_integerCompareCommand = 24, RULE_floatComparatorExpression = 25,
			RULE_floatCommand = 26, RULE_floatOrIntegerValue = 27, RULE_orderByClause = 28,
			RULE_orderByExpression = 29, RULE_orderAscDesc = 30, RULE_comparisonOperator = 31,
			RULE_logicalOperator = 32, RULE_columnName = 33, RULE_entityName = 34,
			RULE_attributeName = 35, RULE_packageName = 36, RULE_value = 37, RULE_singleQuotedString = 38,
			RULE_integerValue = 39, RULE_floatValue = 40, RULE_systemIdValue = 41;
	public static final String[] ruleNames = makeRuleNames();
	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	public static final String _serializedATN =
			"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3Q\u0151\4\2\t\2\4" +
					"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" +
					"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22" +
					"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31" +
					"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!" +
					"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\3" +
					"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3" +
					"\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4u\n\4\3\4\3\4\3\5" +
					"\3\5\3\5\5\5|\n\5\3\5\3\5\3\5\3\5\3\5\5\5\u0083\n\5\5\5\u0085\n\5\3\6" +
					"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\7\6\u0090\n\6\f\6\16\6\u0093\13\6\3\7" +
					"\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13" +
					"\3\13\3\13\5\13\u00a8\n\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13" +
					"\3\13\3\13\5\13\u00b5\n\13\3\13\3\13\3\13\5\13\u00ba\n\13\5\13\u00bc\n" +
					"\13\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\5\16\u00c6\n\16\3\16\3\16\3\16" +
					"\3\16\3\17\3\17\3\17\3\17\3\20\3\20\3\20\6\20\u00d3\n\20\r\20\16\20\u00d4" +
					"\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u00e2\n\22" +
					"\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\24" +
					"\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26" +
					"\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\5\30" +
					"\u010d\n\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\5\31\u0116\n\31\3\31\3" +
					"\31\3\31\3\31\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3" +
					"\35\3\35\5\35\u0129\n\35\3\36\3\36\3\36\3\36\3\36\7\36\u0130\n\36\f\36" +
					"\16\36\u0133\13\36\3\37\3\37\5\37\u0137\n\37\3 \3 \3!\3!\3\"\3\"\3#\3" +
					"#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3+\3+\2\3\n,\2\4\6\b" +
					"\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRT\2" +
					"\13\3\2\r\17\3\2\37\"\3\2\25\27\3\2\31\36\3\2#%\3\2./\3\2&+\3\2\60\61" +
					"\3\2IJ\2\u0143\2V\3\2\2\2\4e\3\2\2\2\6t\3\2\2\2\b\u0084\3\2\2\2\n\u0086" +
					"\3\2\2\2\f\u0094\3\2\2\2\16\u0097\3\2\2\2\20\u009a\3\2\2\2\22\u009d\3" +
					"\2\2\2\24\u00bb\3\2\2\2\26\u00bd\3\2\2\2\30\u00bf\3\2\2\2\32\u00c1\3\2" +
					"\2\2\34\u00cb\3\2\2\2\36\u00d2\3\2\2\2 \u00d8\3\2\2\2\"\u00da\3\2\2\2" +
					"$\u00e7\3\2\2\2&\u00f0\3\2\2\2(\u00f7\3\2\2\2*\u00fc\3\2\2\2,\u0103\3" +
					"\2\2\2.\u010c\3\2\2\2\60\u0111\3\2\2\2\62\u011b\3\2\2\2\64\u011d\3\2\2" +
					"\2\66\u0124\3\2\2\28\u0128\3\2\2\2:\u012a\3\2\2\2<\u0134\3\2\2\2>\u0138" +
					"\3\2\2\2@\u013a\3\2\2\2B\u013c\3\2\2\2D\u013e\3\2\2\2F\u0140\3\2\2\2H" +
					"\u0142\3\2\2\2J\u0144\3\2\2\2L\u0146\3\2\2\2N\u0148\3\2\2\2P\u014a\3\2" +
					"\2\2R\u014c\3\2\2\2T\u014e\3\2\2\2VW\7N\2\2WX\7D\2\2XY\5F$\2YZ\7D\2\2" +
					"Z[\5T+\2[\\\7D\2\2\\]\5F$\2]^\7D\2\2^_\7\b\2\2_`\7N\2\2`a\7D\2\2ab\5F" +
					"$\2bc\7D\2\2cd\5T+\2d\3\3\2\2\2ef\5\6\4\2fg\5\b\5\2g\5\3\2\2\2hi\5J&\2" +
					"ij\7D\2\2jk\5F$\2ku\3\2\2\2lm\5J&\2mn\7D\2\2no\5F$\2op\7D\2\2pq\5T+\2" +
					"qr\7D\2\2rs\5F$\2su\3\2\2\2th\3\2\2\2tl\3\2\2\2uv\3\2\2\2vw\79\2\2w\7" +
					"\3\2\2\2x{\5\f\7\2yz\7@\2\2z|\5\16\b\2{y\3\2\2\2{|\3\2\2\2|\u0085\3\2" +
					"\2\2}\u0085\5\20\t\2~\u0085\5\22\n\2\177\u0082\5:\36\2\u0080\u0081\7@" +
					"\2\2\u0081\u0083\5\16\b\2\u0082\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083" +
					"\u0085\3\2\2\2\u0084x\3\2\2\2\u0084}\3\2\2\2\u0084~\3\2\2\2\u0084\177" +
					"\3\2\2\2\u0085\t\3\2\2\2\u0086\u0087\b\6\1\2\u0087\u0088\5\f\7\2\u0088" +
					"\u0091\3\2\2\2\u0089\u008a\f\4\2\2\u008a\u008b\5@!\2\u008b\u008c\5\n\6" +
					"\5\u008c\u0090\3\2\2\2\u008d\u008e\f\3\2\2\u008e\u0090\5@!\2\u008f\u0089" +
					"\3\2\2\2\u008f\u008d\3\2\2\2\u0090\u0093\3\2\2\2\u0091\u008f\3\2\2\2\u0091" +
					"\u0092\3\2\2\2\u0092\13\3\2\2\2\u0093\u0091\3\2\2\2\u0094\u0095\7\4\2" +
					"\2\u0095\u0096\5\24\13\2\u0096\r\3\2\2\2\u0097\u0098\7\n\2\2\u0098\u0099" +
					"\5P)\2\u0099\17\3\2\2\2\u009a\u009b\7\5\2\2\u009b\u009c\5P)\2\u009c\21" +
					"\3\2\2\2\u009d\u009e\7\6\2\2\u009e\u009f\5P)\2\u009f\23\3\2\2\2\u00a0" +
					"\u00a1\5\26\f\2\u00a1\u00a2\5\24\13\2\u00a2\u00a3\5\30\r\2\u00a3\u00a7" +
					"\3\2\2\2\u00a4\u00a5\5B\"\2\u00a5\u00a6\5\24\13\2\u00a6\u00a8\3\2\2\2" +
					"\u00a7\u00a4\3\2\2\2\u00a7\u00a8\3\2\2\2\u00a8\u00bc\3\2\2\2\u00a9\u00b5" +
					"\5\32\16\2\u00aa\u00b5\5\34\17\2\u00ab\u00b5\5.\30\2\u00ac\u00b5\5\60" +
					"\31\2\u00ad\u00b5\5\64\33\2\u00ae\u00b5\5\"\22\2\u00af\u00b5\5$\23\2\u00b0" +
					"\u00b5\5&\24\2\u00b1\u00b5\5*\26\2\u00b2\u00b5\5(\25\2\u00b3\u00b5\5," +
					"\27\2\u00b4\u00a9\3\2\2\2\u00b4\u00aa\3\2\2\2\u00b4\u00ab\3\2\2\2\u00b4" +
					"\u00ac\3\2\2\2\u00b4\u00ad\3\2\2\2\u00b4\u00ae\3\2\2\2\u00b4\u00af\3\2" +
					"\2\2\u00b4\u00b0\3\2\2\2\u00b4\u00b1\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b4" +
					"\u00b3\3\2\2\2\u00b5\u00b9\3\2\2\2\u00b6\u00b7\5B\"\2\u00b7\u00b8\5\24" +
					"\13\2\u00b8\u00ba\3\2\2\2\u00b9\u00b6\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba" +
					"\u00bc\3\2\2\2\u00bb\u00a0\3\2\2\2\u00bb\u00b4\3\2\2\2\u00bc\25\3\2\2" +
					"\2\u00bd\u00be\7\66\2\2\u00be\27\3\2\2\2\u00bf\u00c0\7\67\2\2\u00c0\31" +
					"\3\2\2\2\u00c1\u00c2\5 \21\2\u00c2\u00c5\7\66\2\2\u00c3\u00c6\5H%\2\u00c4" +
					"\u00c6\5\36\20\2\u00c5\u00c3\3\2\2\2\u00c5\u00c4\3\2\2\2\u00c6\u00c7\3" +
					"\2\2\2\u00c7\u00c8\78\2\2\u00c8\u00c9\5N(\2\u00c9\u00ca\7\67\2\2\u00ca" +
					"\33\3\2\2\2\u00cb\u00cc\5\36\20\2\u00cc\u00cd\5@!\2\u00cd\u00ce\5L\'\2" +
					"\u00ce\35\3\2\2\2\u00cf\u00d0\5F$\2\u00d0\u00d1\7D\2\2\u00d1\u00d3\3\2" +
					"\2\2\u00d2\u00cf\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d2\3\2\2\2\u00d4" +
					"\u00d5\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6\u00d7\5H%\2\u00d7\37\3\2\2\2" +
					"\u00d8\u00d9\t\2\2\2\u00d9!\3\2\2\2\u00da\u00db\7\24\2\2\u00db\u00dc\7" +
					"\66\2\2\u00dc\u00dd\5F$\2\u00dd\u00de\78\2\2\u00de\u00e1\5P)\2\u00df\u00e0" +
					"\78\2\2\u00e0\u00e2\5P)\2\u00e1\u00df\3\2\2\2\u00e1\u00e2\3\2\2\2\u00e2" +
					"\u00e3\3\2\2\2\u00e3\u00e4\7\67\2\2\u00e4\u00e5\5@!\2\u00e5\u00e6\5N(" +
					"\2\u00e6#\3\2\2\2\u00e7\u00e8\7\22\2\2\u00e8\u00e9\7\66\2\2\u00e9\u00ea" +
					"\5F$\2\u00ea\u00eb\78\2\2\u00eb\u00ec\5N(\2\u00ec\u00ed\7\67\2\2\u00ed" +
					"\u00ee\5@!\2\u00ee\u00ef\5N(\2\u00ef%\3\2\2\2\u00f0\u00f1\7\21\2\2\u00f1" +
					"\u00f2\7\66\2\2\u00f2\u00f3\5F$\2\u00f3\u00f4\7\67\2\2\u00f4\u00f5\5@" +
					"!\2\u00f5\u00f6\5P)\2\u00f6\'\3\2\2\2\u00f7\u00f8\t\3\2\2\u00f8\u00f9" +
					"\7\66\2\2\u00f9\u00fa\5F$\2\u00fa\u00fb\7\67\2\2\u00fb)\3\2\2\2\u00fc" +
					"\u00fd\t\4\2\2\u00fd\u00fe\7\66\2\2\u00fe\u00ff\5F$\2\u00ff\u0100\78\2" +
					"\2\u0100\u0101\5L\'\2\u0101\u0102\7\67\2\2\u0102+\3\2\2\2\u0103\u0104" +
					"\7\30\2\2\u0104\u0105\7\66\2\2\u0105\u0106\5F$\2\u0106\u0107\78\2\2\u0107" +
					"\u0108\5L\'\2\u0108\u0109\7\67\2\2\u0109-\3\2\2\2\u010a\u010d\5\36\20" +
					"\2\u010b\u010d\5H%\2\u010c\u010a\3\2\2\2\u010c\u010b\3\2\2\2\u010d\u010e" +
					"\3\2\2\2\u010e\u010f\5@!\2\u010f\u0110\5L\'\2\u0110/\3\2\2\2\u0111\u0112" +
					"\5\62\32\2\u0112\u0115\7\66\2\2\u0113\u0116\5H%\2\u0114\u0116\5\36\20" +
					"\2\u0115\u0113\3\2\2\2\u0115\u0114\3\2\2\2\u0116\u0117\3\2\2\2\u0117\u0118" +
					"\7\67\2\2\u0118\u0119\5@!\2\u0119\u011a\5P)\2\u011a\61\3\2\2\2\u011b\u011c" +
					"\t\5\2\2\u011c\63\3\2\2\2\u011d\u011e\5\66\34\2\u011e\u011f\7\66\2\2\u011f" +
					"\u0120\5H%\2\u0120\u0121\7\67\2\2\u0121\u0122\5@!\2\u0122\u0123\58\35" +
					"\2\u0123\65\3\2\2\2\u0124\u0125\t\6\2\2\u0125\67\3\2\2\2\u0126\u0129\5" +
					"R*\2\u0127\u0129\5P)\2\u0128\u0126\3\2\2\2\u0128\u0127\3\2\2\2\u01299" +
					"\3\2\2\2\u012a\u012b\7,\2\2\u012b\u012c\7-\2\2\u012c\u0131\5<\37\2\u012d" +
					"\u012e\78\2\2\u012e\u0130\5<\37\2\u012f\u012d\3\2\2\2\u0130\u0133\3\2" +
					"\2\2\u0131\u012f\3\2\2\2\u0131\u0132\3\2\2\2\u0132;\3\2\2\2\u0133\u0131" +
					"\3\2\2\2\u0134\u0136\5H%\2\u0135\u0137\5> \2\u0136\u0135\3\2\2\2\u0136" +
					"\u0137\3\2\2\2\u0137=\3\2\2\2\u0138\u0139\t\7\2\2\u0139?\3\2\2\2\u013a" +
					"\u013b\t\b\2\2\u013bA\3\2\2\2\u013c\u013d\t\t\2\2\u013dC\3\2\2\2\u013e" +
					"\u013f\7I\2\2\u013fE\3\2\2\2\u0140\u0141\7F\2\2\u0141G\3\2\2\2\u0142\u0143" +
					"\7F\2\2\u0143I\3\2\2\2\u0144\u0145\7F\2\2\u0145K\3\2\2\2\u0146\u0147\t" +
					"\n\2\2\u0147M\3\2\2\2\u0148\u0149\7J\2\2\u0149O\3\2\2\2\u014a\u014b\7" +
					"G\2\2\u014bQ\3\2\2\2\u014c\u014d\7H\2\2\u014dS\3\2\2\2\u014e\u014f\7E" +
					"\2\2\u014fU\3\2\2\2\24t{\u0082\u0084\u008f\u0091\u00a7\u00b4\u00b9\u00bb" +
					"\u00c5\u00d4\u00e1\u010c\u0115\u0128\u0131\u0136";
	public static final ATN _ATN =
			new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
			new PredictionContextCache();
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	static {
		RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION);
	}

	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}

	public ODataParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
	}

	private static String[] makeRuleNames() {
		return new String[]{
				"referenceStatement", "odataQuery", "entityBase", "odataCommand", "predicate",
				"filterStatement", "countStatement", "topStatement", "skipStatement",
				"filterExpression", "boolExpressionLeft", "boolExpressionRight", "stringCompareExpression",
				"inComparisonExpression", "joinEntities", "stringCompareCommand", "substringExpression",
				"indexOfExpression", "lengthExpression", "timeExpression", "stringModifierExpression",
				"concatExpression", "comparisonExpression", "integerComparatorExpression",
				"integerCompareCommand", "floatComparatorExpression", "floatCommand",
				"floatOrIntegerValue", "orderByClause", "orderByExpression", "orderAscDesc",
				"comparisonOperator", "logicalOperator", "columnName", "entityName",
				"attributeName", "packageName", "value", "singleQuotedString", "integerValue",
				"floatValue", "systemIdValue"
		};
	}

	private static String[] makeLiteralNames() {
		return new String[]{
				null, null, "'$filter='", "'$top='", "'$skip='", "'$orderby='", "'$ref?$id='",
				"'$expand='", "'$count='", "'$select='", "'$id='", "'contains'", "'startswith'",
				"'endswith'", "'substringof'", "'length'", "'indexof'", "'replace'",
				"'substring'", "'tolower'", "'toupper'", "'trim'", "'concat'", "'day'",
				"'month'", "'year'", "'hour'", "'minute'", "'second'", "'now'", "'maxdatetime'",
				"'mindatetime'", "'totaloffsetminutes'", "'round'", "'floor'", "'ceiling'",
				"'eq'", "'gt'", "'lt'", "'ge'", "'le'", "'ne'", "'order'", "'by'", "'desc'",
				"'asc'", "'or'", "'and'", "'not'", "'://'", "'http'", "'https'", "'('",
				"')'", "','", "'?'", "';'", "'@'", "'''", "'\"'", "'`'", "':'", "'&'",
				"'NULL'", null, "'.'", "'/'", null, null, null, null, null, null, null,
				null, null, "'/odata/arkivstruktur'"
		};
	}

	private static String[] makeSymbolicNames() {
		return new String[]{
				null, "SPACE", "FILTER", "TOP", "SKIPRULE", "ORDERBY", "REF", "EXPAND",
				"COUNT", "SELECT", "DOLLARID", "CONTAINS", "STARTSWITH", "ENDSWITH",
				"SUBSTRINGOF", "LENGTH", "INDEXOF", "REPLACE", "SUBSTRING", "TOLOWER",
				"TOUPPER", "TRIM", "CONCAT", "DAY", "MONTH", "YEAR", "HOUR", "MINUTE",
				"SECOND", "NOW", "MAXDATETIME", "MINDATETIME", "TOTALOFFSETMINUTES",
				"ROUND", "FLOOR", "CEILING", "EQ", "GT", "LT", "GE", "LE", "NE", "ORDER",
				"BY", "DESC", "ASC", "OR", "AND", "NOT", "SEPERATOR", "HTTP", "HTTPS",
				"LR_BRACKET", "RR_BRACKET", "COMMA", "QUESTION", "SEMI", "AT_SIGN", "SINGLE_QUOTE_SYMB",
				"DOUBLE_QUOTE_SYMB", "REVERSE_QUOTE_SYMB", "COLON_SYMB", "AMPERSAND",
				"NULL_LITERAL", "NULL_SPEC_LITERAL", "DOT", "SLASH", "UUID", "NOARK_ENTITY",
				"INTEGER", "FLOAT", "ID", "QUOTED_STRING", "STRING_LITERAL", "DECIMAL_LITERAL",
				"HEXADECIMAL_LITERAL", "ODATA_ARK", "HEX", "STRING", "ERROR_RECONGNIGION"
		};
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() {
		return "ODataParser.g4";
	}

	@Override
	public String[] getRuleNames() {
		return ruleNames;
	}

	@Override
	public String getSerializedATN() {
		return _serializedATN;
	}

	@Override
	public ATN getATN() {
		return _ATN;
	}

	public final ReferenceStatementContext referenceStatement() throws RecognitionException {
		ReferenceStatementContext _localctx = new ReferenceStatementContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_referenceStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(84);
				match(ODATA_ARK);
				setState(85);
				match(SLASH);
				setState(86);
				entityName();
				setState(87);
				match(SLASH);
				setState(88);
				systemIdValue();
				setState(89);
				match(SLASH);
				setState(90);
				entityName();
				setState(91);
				match(SLASH);
				setState(92);
				match(REF);
				setState(93);
				match(ODATA_ARK);
				setState(94);
				match(SLASH);
				setState(95);
				entityName();
				setState(96);
				match(SLASH);
				setState(97);
				systemIdValue();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final OdataQueryContext odataQuery() throws RecognitionException {
		OdataQueryContext _localctx = new OdataQueryContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_odataQuery);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(99);
				entityBase();
				setState(100);
				odataCommand();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final EntityBaseContext entityBase() throws RecognitionException {
		EntityBaseContext _localctx = new EntityBaseContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_entityBase);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(114);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 0, _ctx)) {
					case 1: {
						{
							setState(102);
							packageName();
							setState(103);
							match(SLASH);
							setState(104);
							entityName();
						}
					}
					break;
					case 2: {
						{
							setState(106);
							packageName();
							setState(107);
							match(SLASH);
							setState(108);
							entityName();
							setState(109);
							match(SLASH);
							setState(110);
							systemIdValue();
							setState(111);
							match(SLASH);
							setState(112);
							entityName();
						}
					}
					break;
				}
				setState(116);
				match(QUESTION);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final OdataCommandContext odataCommand() throws RecognitionException {
		OdataCommandContext _localctx = new OdataCommandContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_odataCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(130);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
					case FILTER: {
						{
							setState(118);
							filterStatement();
							setState(121);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la == AMPERSAND) {
								{
									setState(119);
									match(AMPERSAND);
									setState(120);
									countStatement();
								}
							}

						}
					}
					break;
					case TOP: {
						setState(123);
						topStatement();
					}
					break;
					case SKIPRULE: {
						setState(124);
						skipStatement();
					}
					break;
					case ORDER: {
						{
							setState(125);
							orderByClause();
							setState(128);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la == AMPERSAND) {
								{
									setState(126);
									match(AMPERSAND);
									setState(127);
									countStatement();
								}
							}

						}
					}
					break;
					default:
						throw new NoViableAltException(this);
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final PredicateContext predicate() throws RecognitionException {
		return predicate(0);
	}

	private PredicateContext predicate(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PredicateContext _localctx = new PredicateContext(_ctx, _parentState);
		PredicateContext _prevctx = _localctx;
		int _startState = 8;
		enterRecursionRule(_localctx, 8, RULE_predicate, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				{
					setState(133);
					filterStatement();
				}
				_ctx.stop = _input.LT(-1);
				setState(143);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
				while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						if (_parseListeners != null) triggerExitRuleEvent();
						_prevctx = _localctx;
						{
							setState(141);
							_errHandler.sync(this);
							switch (getInterpreter().adaptivePredict(_input, 4, _ctx)) {
								case 1: {
									_localctx = new PredicateContext(_parentctx, _parentState);
									_localctx.left = _prevctx;
									_localctx.left = _prevctx;
									pushNewRecursionContext(_localctx, _startState, RULE_predicate);
									setState(135);
									if (!(precpred(_ctx, 2)))
										throw new FailedPredicateException(this, "precpred(_ctx, 2)");
									setState(136);
									comparisonOperator();
									setState(137);
									((PredicateContext) _localctx).right = predicate(3);
								}
								break;
								case 2: {
									_localctx = new PredicateContext(_parentctx, _parentState);
									pushNewRecursionContext(_localctx, _startState, RULE_predicate);
									setState(139);
									if (!(precpred(_ctx, 1)))
										throw new FailedPredicateException(this, "precpred(_ctx, 1)");
									setState(140);
									comparisonOperator();
								}
								break;
							}
						}
					}
					setState(145);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public final FilterStatementContext filterStatement() throws RecognitionException {
		FilterStatementContext _localctx = new FilterStatementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_filterStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(146);
				match(FILTER);
				setState(147);
				filterExpression();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final CountStatementContext countStatement() throws RecognitionException {
		CountStatementContext _localctx = new CountStatementContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_countStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(149);
				match(COUNT);
				setState(150);
				integerValue();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final TopStatementContext topStatement() throws RecognitionException {
		TopStatementContext _localctx = new TopStatementContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_topStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(152);
				match(TOP);
				setState(153);
				integerValue();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final SkipStatementContext skipStatement() throws RecognitionException {
		SkipStatementContext _localctx = new SkipStatementContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_skipStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(155);
				match(SKIPRULE);
				setState(156);
				integerValue();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final FilterExpressionContext filterExpression() throws RecognitionException {
		FilterExpressionContext _localctx = new FilterExpressionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_filterExpression);
		try {
			setState(185);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
				case LR_BRACKET:
					enterOuterAlt(_localctx, 1);
				{
					{
						setState(158);
						boolExpressionLeft();
						setState(159);
						filterExpression();
						setState(160);
						boolExpressionRight();
					}
					setState(165);
					_errHandler.sync(this);
					switch (getInterpreter().adaptivePredict(_input, 6, _ctx)) {
						case 1: {
							setState(162);
							logicalOperator();
							setState(163);
							filterExpression();
						}
						break;
					}
				}
				break;
				case CONTAINS:
				case STARTSWITH:
				case ENDSWITH:
				case LENGTH:
				case INDEXOF:
				case SUBSTRING:
				case TOLOWER:
				case TOUPPER:
				case TRIM:
				case CONCAT:
				case DAY:
				case MONTH:
				case YEAR:
				case HOUR:
				case MINUTE:
				case SECOND:
				case NOW:
				case MAXDATETIME:
				case MINDATETIME:
				case TOTALOFFSETMINUTES:
				case ROUND:
				case FLOOR:
				case CEILING:
				case NOARK_ENTITY:
					enterOuterAlt(_localctx, 2);
				{
					setState(178);
					_errHandler.sync(this);
					switch (getInterpreter().adaptivePredict(_input, 7, _ctx)) {
						case 1: {
							setState(167);
							stringCompareExpression();
						}
						break;
						case 2: {
							setState(168);
							inComparisonExpression();
						}
						break;
						case 3: {
							setState(169);
							comparisonExpression();
						}
						break;
						case 4: {
							setState(170);
							integerComparatorExpression();
						}
						break;
						case 5: {
							setState(171);
							floatComparatorExpression();
						}
						break;
						case 6: {
							setState(172);
							substringExpression();
						}
						break;
						case 7: {
							setState(173);
							indexOfExpression();
						}
						break;
						case 8: {
							setState(174);
							lengthExpression();
						}
						break;
						case 9: {
							setState(175);
							stringModifierExpression();
						}
						break;
						case 10: {
							setState(176);
							timeExpression();
						}
						break;
						case 11: {
							setState(177);
							concatExpression();
						}
						break;
					}
					setState(183);
					_errHandler.sync(this);
					switch (getInterpreter().adaptivePredict(_input, 8, _ctx)) {
						case 1: {
							setState(180);
							logicalOperator();
							setState(181);
							filterExpression();
						}
						break;
					}
				}
				break;
				default:
					throw new NoViableAltException(this);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final BoolExpressionLeftContext boolExpressionLeft() throws RecognitionException {
		BoolExpressionLeftContext _localctx = new BoolExpressionLeftContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_boolExpressionLeft);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(187);
				match(LR_BRACKET);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final BoolExpressionRightContext boolExpressionRight() throws RecognitionException {
		BoolExpressionRightContext _localctx = new BoolExpressionRightContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_boolExpressionRight);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(189);
				match(RR_BRACKET);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final StringCompareExpressionContext stringCompareExpression() throws RecognitionException {
		StringCompareExpressionContext _localctx = new StringCompareExpressionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_stringCompareExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(191);
				stringCompareCommand();
				setState(192);
				match(LR_BRACKET);
				setState(195);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 10, _ctx)) {
					case 1: {
						setState(193);
						attributeName();
					}
					break;
					case 2: {
						setState(194);
						joinEntities();
					}
					break;
				}
				setState(197);
				match(COMMA);
				setState(198);
				singleQuotedString();
				setState(199);
				match(RR_BRACKET);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final InComparisonExpressionContext inComparisonExpression() throws RecognitionException {
		InComparisonExpressionContext _localctx = new InComparisonExpressionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_inComparisonExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(201);
				joinEntities();
				setState(202);
				comparisonOperator();
				setState(203);
				value();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final JoinEntitiesContext joinEntities() throws RecognitionException {
		JoinEntitiesContext _localctx = new JoinEntitiesContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_joinEntities);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(208);
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
						case 1: {
							{
								setState(205);
								entityName();
								setState(206);
								match(SLASH);
							}
						}
						break;
						default:
							throw new NoViableAltException(this);
					}
					setState(210);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 11, _ctx);
				} while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
				setState(212);
				attributeName();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final StringCompareCommandContext stringCompareCommand() throws RecognitionException {
		StringCompareCommandContext _localctx = new StringCompareCommandContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_stringCompareCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(214);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CONTAINS) | (1L << STARTSWITH) | (1L << ENDSWITH))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final SubstringExpressionContext substringExpression() throws RecognitionException {
		SubstringExpressionContext _localctx = new SubstringExpressionContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_substringExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(216);
				match(SUBSTRING);
				setState(217);
				match(LR_BRACKET);
				setState(218);
				entityName();
				setState(219);
				match(COMMA);
				setState(220);
				integerValue();
				setState(223);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == COMMA) {
					{
						setState(221);
						match(COMMA);
						setState(222);
						integerValue();
					}
				}

				setState(225);
				match(RR_BRACKET);
				setState(226);
				comparisonOperator();
				setState(227);
				singleQuotedString();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final IndexOfExpressionContext indexOfExpression() throws RecognitionException {
		IndexOfExpressionContext _localctx = new IndexOfExpressionContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_indexOfExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(229);
				match(INDEXOF);
				setState(230);
				match(LR_BRACKET);
				setState(231);
				entityName();
				setState(232);
				match(COMMA);
				setState(233);
				singleQuotedString();
				setState(234);
				match(RR_BRACKET);
				setState(235);
				comparisonOperator();
				setState(236);
				singleQuotedString();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final LengthExpressionContext lengthExpression() throws RecognitionException {
		LengthExpressionContext _localctx = new LengthExpressionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_lengthExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(238);
				match(LENGTH);
				setState(239);
				match(LR_BRACKET);
				setState(240);
				entityName();
				setState(241);
				match(RR_BRACKET);
				setState(242);
				comparisonOperator();
				setState(243);
				integerValue();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final TimeExpressionContext timeExpression() throws RecognitionException {
		TimeExpressionContext _localctx = new TimeExpressionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_timeExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(245);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NOW) | (1L << MAXDATETIME) | (1L << MINDATETIME) | (1L << TOTALOFFSETMINUTES))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(246);
				match(LR_BRACKET);
				setState(247);
				entityName();
				setState(248);
				match(RR_BRACKET);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final StringModifierExpressionContext stringModifierExpression() throws RecognitionException {
		StringModifierExpressionContext _localctx = new StringModifierExpressionContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_stringModifierExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(250);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TOLOWER) | (1L << TOUPPER) | (1L << TRIM))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(251);
				match(LR_BRACKET);
				setState(252);
				entityName();
				setState(253);
				match(COMMA);
				setState(254);
				value();
				setState(255);
				match(RR_BRACKET);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final ConcatExpressionContext concatExpression() throws RecognitionException {
		ConcatExpressionContext _localctx = new ConcatExpressionContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_concatExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(257);
				match(CONCAT);
				setState(258);
				match(LR_BRACKET);
				setState(259);
				entityName();
				setState(260);
				match(COMMA);
				setState(261);
				value();
				setState(262);
				match(RR_BRACKET);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final ComparisonExpressionContext comparisonExpression() throws RecognitionException {
		ComparisonExpressionContext _localctx = new ComparisonExpressionContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_comparisonExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(266);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 13, _ctx)) {
					case 1: {
						setState(264);
						joinEntities();
					}
					break;
					case 2: {
						setState(265);
						attributeName();
					}
					break;
				}
				setState(268);
				comparisonOperator();
				setState(269);
				value();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final IntegerComparatorExpressionContext integerComparatorExpression() throws RecognitionException {
		IntegerComparatorExpressionContext _localctx = new IntegerComparatorExpressionContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_integerComparatorExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(271);
				integerCompareCommand();
				setState(272);
				match(LR_BRACKET);
				setState(275);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 14, _ctx)) {
					case 1: {
						setState(273);
						attributeName();
					}
					break;
					case 2: {
						setState(274);
						joinEntities();
					}
					break;
				}
				setState(277);
				match(RR_BRACKET);
				setState(278);
				comparisonOperator();
				setState(279);
				integerValue();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final IntegerCompareCommandContext integerCompareCommand() throws RecognitionException {
		IntegerCompareCommandContext _localctx = new IntegerCompareCommandContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_integerCompareCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(281);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DAY) | (1L << MONTH) | (1L << YEAR) | (1L << HOUR) | (1L << MINUTE) | (1L << SECOND))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final FloatComparatorExpressionContext floatComparatorExpression() throws RecognitionException {
		FloatComparatorExpressionContext _localctx = new FloatComparatorExpressionContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_floatComparatorExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(283);
				floatCommand();
				setState(284);
				match(LR_BRACKET);
				setState(285);
				attributeName();
				setState(286);
				match(RR_BRACKET);
				setState(287);
				comparisonOperator();
				setState(288);
				floatOrIntegerValue();
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final FloatCommandContext floatCommand() throws RecognitionException {
		FloatCommandContext _localctx = new FloatCommandContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_floatCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(290);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ROUND) | (1L << FLOOR) | (1L << CEILING))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final FloatOrIntegerValueContext floatOrIntegerValue() throws RecognitionException {
		FloatOrIntegerValueContext _localctx = new FloatOrIntegerValueContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_floatOrIntegerValue);
		try {
			setState(294);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
				case FLOAT:
					enterOuterAlt(_localctx, 1);
				{
					setState(292);
					floatValue();
				}
				break;
				case INTEGER:
					enterOuterAlt(_localctx, 2);
				{
					setState(293);
					integerValue();
				}
				break;
				default:
					throw new NoViableAltException(this);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final OrderByClauseContext orderByClause() throws RecognitionException {
		OrderByClauseContext _localctx = new OrderByClauseContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_orderByClause);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(296);
				match(ORDER);
				setState(297);
				match(BY);
				setState(298);
				orderByExpression();
				setState(303);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == COMMA) {
					{
						{
							setState(299);
							match(COMMA);
							setState(300);
							orderByExpression();
						}
					}
					setState(305);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final OrderByExpressionContext orderByExpression() throws RecognitionException {
		OrderByExpressionContext _localctx = new OrderByExpressionContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_orderByExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(306);
				attributeName();
				setState(308);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == DESC || _la == ASC) {
					{
						setState(307);
						orderAscDesc();
					}
				}

			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final OrderAscDescContext orderAscDesc() throws RecognitionException {
		OrderAscDescContext _localctx = new OrderAscDescContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_orderAscDesc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(310);
				_la = _input.LA(1);
				if (!(_la == DESC || _la == ASC)) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final ComparisonOperatorContext comparisonOperator() throws RecognitionException {
		ComparisonOperatorContext _localctx = new ComparisonOperatorContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_comparisonOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(312);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQ) | (1L << GT) | (1L << LT) | (1L << GE) | (1L << LE) | (1L << NE))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final LogicalOperatorContext logicalOperator() throws RecognitionException {
		LogicalOperatorContext _localctx = new LogicalOperatorContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_logicalOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(314);
				_la = _input.LA(1);
				if (!(_la == OR || _la == AND)) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final ColumnNameContext columnName() throws RecognitionException {
		ColumnNameContext _localctx = new ColumnNameContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_columnName);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(316);
				match(ID);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final EntityNameContext entityName() throws RecognitionException {
		EntityNameContext _localctx = new EntityNameContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_entityName);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(318);
				match(NOARK_ENTITY);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final AttributeNameContext attributeName() throws RecognitionException {
		AttributeNameContext _localctx = new AttributeNameContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_attributeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(320);
				match(NOARK_ENTITY);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final PackageNameContext packageName() throws RecognitionException {
		PackageNameContext _localctx = new PackageNameContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_packageName);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(322);
				match(NOARK_ENTITY);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_value);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(324);
				_la = _input.LA(1);
				if (!(_la == ID || _la == QUOTED_STRING)) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final SingleQuotedStringContext singleQuotedString() throws RecognitionException {
		SingleQuotedStringContext _localctx = new SingleQuotedStringContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_singleQuotedString);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(326);
				match(QUOTED_STRING);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final IntegerValueContext integerValue() throws RecognitionException {
		IntegerValueContext _localctx = new IntegerValueContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_integerValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(328);
				match(INTEGER);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final FloatValueContext floatValue() throws RecognitionException {
		FloatValueContext _localctx = new FloatValueContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_floatValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(330);
				match(FLOAT);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final SystemIdValueContext systemIdValue() throws RecognitionException {
		SystemIdValueContext _localctx = new SystemIdValueContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_systemIdValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(332);
				match(UUID);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
			case 4:
				return predicate_sempred((PredicateContext) _localctx, predIndex);
		}
		return true;
	}

	private boolean predicate_sempred(PredicateContext _localctx, int predIndex) {
		switch (predIndex) {
			case 0:
				return precpred(_ctx, 2);
			case 1:
				return precpred(_ctx, 1);
		}
		return true;
	}

	public static class ReferenceStatementContext extends ParserRuleContext {
		public ReferenceStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public List<TerminalNode> ODATA_ARK() {
			return getTokens(ODataParser.ODATA_ARK);
		}

		public TerminalNode ODATA_ARK(int i) {
			return getToken(ODataParser.ODATA_ARK, i);
		}

		public List<TerminalNode> SLASH() {
			return getTokens(ODataParser.SLASH);
		}

		public TerminalNode SLASH(int i) {
			return getToken(ODataParser.SLASH, i);
		}

		public List<EntityNameContext> entityName() {
			return getRuleContexts(EntityNameContext.class);
		}

		public EntityNameContext entityName(int i) {
			return getRuleContext(EntityNameContext.class, i);
		}

		public List<SystemIdValueContext> systemIdValue() {
			return getRuleContexts(SystemIdValueContext.class);
		}

		public SystemIdValueContext systemIdValue(int i) {
			return getRuleContext(SystemIdValueContext.class, i);
		}

		public TerminalNode REF() {
			return getToken(ODataParser.REF, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_referenceStatement;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterReferenceStatement(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitReferenceStatement(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitReferenceStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class OdataQueryContext extends ParserRuleContext {
		public OdataQueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public EntityBaseContext entityBase() {
			return getRuleContext(EntityBaseContext.class, 0);
		}

		public OdataCommandContext odataCommand() {
			return getRuleContext(OdataCommandContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_odataQuery;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterOdataQuery(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitOdataQuery(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitOdataQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class EntityBaseContext extends ParserRuleContext {
		public EntityBaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode QUESTION() {
			return getToken(ODataParser.QUESTION, 0);
		}

		public PackageNameContext packageName() {
			return getRuleContext(PackageNameContext.class, 0);
		}

		public List<TerminalNode> SLASH() {
			return getTokens(ODataParser.SLASH);
		}

		public TerminalNode SLASH(int i) {
			return getToken(ODataParser.SLASH, i);
		}

		public List<EntityNameContext> entityName() {
			return getRuleContexts(EntityNameContext.class);
		}

		public EntityNameContext entityName(int i) {
			return getRuleContext(EntityNameContext.class, i);
		}

		public SystemIdValueContext systemIdValue() {
			return getRuleContext(SystemIdValueContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_entityBase;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterEntityBase(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitEntityBase(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitEntityBase(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class OdataCommandContext extends ParserRuleContext {
		public OdataCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TopStatementContext topStatement() {
			return getRuleContext(TopStatementContext.class, 0);
		}

		public SkipStatementContext skipStatement() {
			return getRuleContext(SkipStatementContext.class, 0);
		}

		public FilterStatementContext filterStatement() {
			return getRuleContext(FilterStatementContext.class, 0);
		}

		public OrderByClauseContext orderByClause() {
			return getRuleContext(OrderByClauseContext.class, 0);
		}

		public TerminalNode AMPERSAND() {
			return getToken(ODataParser.AMPERSAND, 0);
		}

		public CountStatementContext countStatement() {
			return getRuleContext(CountStatementContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_odataCommand;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterOdataCommand(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitOdataCommand(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitOdataCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class PredicateContext extends ParserRuleContext {
		public PredicateContext left;
		public PredicateContext right;

		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public FilterStatementContext filterStatement() {
			return getRuleContext(FilterStatementContext.class, 0);
		}

		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class, 0);
		}

		public List<PredicateContext> predicate() {
			return getRuleContexts(PredicateContext.class);
		}

		public PredicateContext predicate(int i) {
			return getRuleContext(PredicateContext.class, i);
		}

		@Override
		public int getRuleIndex() {
			return RULE_predicate;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterPredicate(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitPredicate(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class FilterStatementContext extends ParserRuleContext {
		public FilterStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode FILTER() {
			return getToken(ODataParser.FILTER, 0);
		}

		public FilterExpressionContext filterExpression() {
			return getRuleContext(FilterExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_filterStatement;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterFilterStatement(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitFilterStatement(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitFilterStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class CountStatementContext extends ParserRuleContext {
		public CountStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode COUNT() {
			return getToken(ODataParser.COUNT, 0);
		}

		public IntegerValueContext integerValue() {
			return getRuleContext(IntegerValueContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_countStatement;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterCountStatement(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitCountStatement(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitCountStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class TopStatementContext extends ParserRuleContext {
		public TopStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode TOP() {
			return getToken(ODataParser.TOP, 0);
		}

		public IntegerValueContext integerValue() {
			return getRuleContext(IntegerValueContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_topStatement;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterTopStatement(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitTopStatement(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitTopStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class SkipStatementContext extends ParserRuleContext {
		public SkipStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode SKIPRULE() {
			return getToken(ODataParser.SKIPRULE, 0);
		}

		public IntegerValueContext integerValue() {
			return getRuleContext(IntegerValueContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_skipStatement;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterSkipStatement(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitSkipStatement(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitSkipStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class FilterExpressionContext extends ParserRuleContext {
		public FilterExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public BoolExpressionLeftContext boolExpressionLeft() {
			return getRuleContext(BoolExpressionLeftContext.class, 0);
		}

		public List<FilterExpressionContext> filterExpression() {
			return getRuleContexts(FilterExpressionContext.class);
		}

		public FilterExpressionContext filterExpression(int i) {
			return getRuleContext(FilterExpressionContext.class, i);
		}

		public BoolExpressionRightContext boolExpressionRight() {
			return getRuleContext(BoolExpressionRightContext.class, 0);
		}

		public LogicalOperatorContext logicalOperator() {
			return getRuleContext(LogicalOperatorContext.class, 0);
		}

		public StringCompareExpressionContext stringCompareExpression() {
			return getRuleContext(StringCompareExpressionContext.class, 0);
		}

		public InComparisonExpressionContext inComparisonExpression() {
			return getRuleContext(InComparisonExpressionContext.class, 0);
		}

		public ComparisonExpressionContext comparisonExpression() {
			return getRuleContext(ComparisonExpressionContext.class, 0);
		}

		public IntegerComparatorExpressionContext integerComparatorExpression() {
			return getRuleContext(IntegerComparatorExpressionContext.class, 0);
		}

		public FloatComparatorExpressionContext floatComparatorExpression() {
			return getRuleContext(FloatComparatorExpressionContext.class, 0);
		}

		public SubstringExpressionContext substringExpression() {
			return getRuleContext(SubstringExpressionContext.class, 0);
		}

		public IndexOfExpressionContext indexOfExpression() {
			return getRuleContext(IndexOfExpressionContext.class, 0);
		}

		public LengthExpressionContext lengthExpression() {
			return getRuleContext(LengthExpressionContext.class, 0);
		}

		public StringModifierExpressionContext stringModifierExpression() {
			return getRuleContext(StringModifierExpressionContext.class, 0);
		}

		public TimeExpressionContext timeExpression() {
			return getRuleContext(TimeExpressionContext.class, 0);
		}

		public ConcatExpressionContext concatExpression() {
			return getRuleContext(ConcatExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_filterExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterFilterExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitFilterExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitFilterExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class BoolExpressionLeftContext extends ParserRuleContext {
		public BoolExpressionLeftContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode LR_BRACKET() {
			return getToken(ODataParser.LR_BRACKET, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_boolExpressionLeft;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterBoolExpressionLeft(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitBoolExpressionLeft(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitBoolExpressionLeft(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class BoolExpressionRightContext extends ParserRuleContext {
		public BoolExpressionRightContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode RR_BRACKET() {
			return getToken(ODataParser.RR_BRACKET, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_boolExpressionRight;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterBoolExpressionRight(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitBoolExpressionRight(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitBoolExpressionRight(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class StringCompareExpressionContext extends ParserRuleContext {
		public StringCompareExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public StringCompareCommandContext stringCompareCommand() {
			return getRuleContext(StringCompareCommandContext.class, 0);
		}

		public TerminalNode LR_BRACKET() {
			return getToken(ODataParser.LR_BRACKET, 0);
		}

		public TerminalNode COMMA() {
			return getToken(ODataParser.COMMA, 0);
		}

		public SingleQuotedStringContext singleQuotedString() {
			return getRuleContext(SingleQuotedStringContext.class, 0);
		}

		public TerminalNode RR_BRACKET() {
			return getToken(ODataParser.RR_BRACKET, 0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class, 0);
		}

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_stringCompareExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterStringCompareExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitStringCompareExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitStringCompareExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class InComparisonExpressionContext extends ParserRuleContext {
		public InComparisonExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class, 0);
		}

		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class, 0);
		}

		public ValueContext value() {
			return getRuleContext(ValueContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_inComparisonExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterInComparisonExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitInComparisonExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitInComparisonExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class JoinEntitiesContext extends ParserRuleContext {
		public JoinEntitiesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class, 0);
		}

		public List<EntityNameContext> entityName() {
			return getRuleContexts(EntityNameContext.class);
		}

		public EntityNameContext entityName(int i) {
			return getRuleContext(EntityNameContext.class, i);
		}

		public List<TerminalNode> SLASH() {
			return getTokens(ODataParser.SLASH);
		}

		public TerminalNode SLASH(int i) {
			return getToken(ODataParser.SLASH, i);
		}

		@Override
		public int getRuleIndex() {
			return RULE_joinEntities;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterJoinEntities(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitJoinEntities(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitJoinEntities(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class StringCompareCommandContext extends ParserRuleContext {
		public StringCompareCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode CONTAINS() {
			return getToken(ODataParser.CONTAINS, 0);
		}

		public TerminalNode STARTSWITH() {
			return getToken(ODataParser.STARTSWITH, 0);
		}

		public TerminalNode ENDSWITH() {
			return getToken(ODataParser.ENDSWITH, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_stringCompareCommand;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterStringCompareCommand(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitStringCompareCommand(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitStringCompareCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class SubstringExpressionContext extends ParserRuleContext {
		public SubstringExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode SUBSTRING() {
			return getToken(ODataParser.SUBSTRING, 0);
		}

		public TerminalNode LR_BRACKET() {
			return getToken(ODataParser.LR_BRACKET, 0);
		}

		public EntityNameContext entityName() {
			return getRuleContext(EntityNameContext.class, 0);
		}

		public List<TerminalNode> COMMA() {
			return getTokens(ODataParser.COMMA);
		}

		public TerminalNode COMMA(int i) {
			return getToken(ODataParser.COMMA, i);
		}

		public List<IntegerValueContext> integerValue() {
			return getRuleContexts(IntegerValueContext.class);
		}

		public IntegerValueContext integerValue(int i) {
			return getRuleContext(IntegerValueContext.class, i);
		}

		public TerminalNode RR_BRACKET() {
			return getToken(ODataParser.RR_BRACKET, 0);
		}

		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class, 0);
		}

		public SingleQuotedStringContext singleQuotedString() {
			return getRuleContext(SingleQuotedStringContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_substringExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterSubstringExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitSubstringExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitSubstringExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class IndexOfExpressionContext extends ParserRuleContext {
		public IndexOfExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode INDEXOF() {
			return getToken(ODataParser.INDEXOF, 0);
		}

		public TerminalNode LR_BRACKET() {
			return getToken(ODataParser.LR_BRACKET, 0);
		}

		public EntityNameContext entityName() {
			return getRuleContext(EntityNameContext.class, 0);
		}

		public TerminalNode COMMA() {
			return getToken(ODataParser.COMMA, 0);
		}

		public List<SingleQuotedStringContext> singleQuotedString() {
			return getRuleContexts(SingleQuotedStringContext.class);
		}

		public SingleQuotedStringContext singleQuotedString(int i) {
			return getRuleContext(SingleQuotedStringContext.class, i);
		}

		public TerminalNode RR_BRACKET() {
			return getToken(ODataParser.RR_BRACKET, 0);
		}

		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_indexOfExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterIndexOfExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitIndexOfExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitIndexOfExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class LengthExpressionContext extends ParserRuleContext {
		public LengthExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode LENGTH() {
			return getToken(ODataParser.LENGTH, 0);
		}

		public TerminalNode LR_BRACKET() {
			return getToken(ODataParser.LR_BRACKET, 0);
		}

		public EntityNameContext entityName() {
			return getRuleContext(EntityNameContext.class, 0);
		}

		public TerminalNode RR_BRACKET() {
			return getToken(ODataParser.RR_BRACKET, 0);
		}

		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class, 0);
		}

		public IntegerValueContext integerValue() {
			return getRuleContext(IntegerValueContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_lengthExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterLengthExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitLengthExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitLengthExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class TimeExpressionContext extends ParserRuleContext {
		public TimeExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode LR_BRACKET() {
			return getToken(ODataParser.LR_BRACKET, 0);
		}

		public EntityNameContext entityName() {
			return getRuleContext(EntityNameContext.class, 0);
		}

		public TerminalNode RR_BRACKET() {
			return getToken(ODataParser.RR_BRACKET, 0);
		}

		public TerminalNode NOW() {
			return getToken(ODataParser.NOW, 0);
		}

		public TerminalNode MAXDATETIME() {
			return getToken(ODataParser.MAXDATETIME, 0);
		}

		public TerminalNode MINDATETIME() {
			return getToken(ODataParser.MINDATETIME, 0);
		}

		public TerminalNode TOTALOFFSETMINUTES() {
			return getToken(ODataParser.TOTALOFFSETMINUTES, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_timeExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterTimeExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitTimeExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitTimeExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class StringModifierExpressionContext extends ParserRuleContext {
		public StringModifierExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode LR_BRACKET() {
			return getToken(ODataParser.LR_BRACKET, 0);
		}

		public EntityNameContext entityName() {
			return getRuleContext(EntityNameContext.class, 0);
		}

		public TerminalNode COMMA() {
			return getToken(ODataParser.COMMA, 0);
		}

		public ValueContext value() {
			return getRuleContext(ValueContext.class, 0);
		}

		public TerminalNode RR_BRACKET() {
			return getToken(ODataParser.RR_BRACKET, 0);
		}

		public TerminalNode TOLOWER() {
			return getToken(ODataParser.TOLOWER, 0);
		}

		public TerminalNode TOUPPER() {
			return getToken(ODataParser.TOUPPER, 0);
		}

		public TerminalNode TRIM() {
			return getToken(ODataParser.TRIM, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_stringModifierExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterStringModifierExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitStringModifierExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitStringModifierExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class ConcatExpressionContext extends ParserRuleContext {
		public ConcatExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode CONCAT() {
			return getToken(ODataParser.CONCAT, 0);
		}

		public TerminalNode LR_BRACKET() {
			return getToken(ODataParser.LR_BRACKET, 0);
		}

		public EntityNameContext entityName() {
			return getRuleContext(EntityNameContext.class, 0);
		}

		public TerminalNode COMMA() {
			return getToken(ODataParser.COMMA, 0);
		}

		public ValueContext value() {
			return getRuleContext(ValueContext.class, 0);
		}

		public TerminalNode RR_BRACKET() {
			return getToken(ODataParser.RR_BRACKET, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_concatExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterConcatExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitConcatExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitConcatExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class ComparisonExpressionContext extends ParserRuleContext {
		public ComparisonExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class, 0);
		}

		public ValueContext value() {
			return getRuleContext(ValueContext.class, 0);
		}

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class, 0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_comparisonExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterComparisonExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitComparisonExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitComparisonExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class IntegerComparatorExpressionContext extends ParserRuleContext {
		public IntegerComparatorExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public IntegerCompareCommandContext integerCompareCommand() {
			return getRuleContext(IntegerCompareCommandContext.class, 0);
		}

		public TerminalNode LR_BRACKET() {
			return getToken(ODataParser.LR_BRACKET, 0);
		}

		public TerminalNode RR_BRACKET() {
			return getToken(ODataParser.RR_BRACKET, 0);
		}

		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class, 0);
		}

		public IntegerValueContext integerValue() {
			return getRuleContext(IntegerValueContext.class, 0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class, 0);
		}

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_integerComparatorExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterIntegerComparatorExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitIntegerComparatorExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitIntegerComparatorExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class IntegerCompareCommandContext extends ParserRuleContext {
		public IntegerCompareCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode YEAR() {
			return getToken(ODataParser.YEAR, 0);
		}

		public TerminalNode MONTH() {
			return getToken(ODataParser.MONTH, 0);
		}

		public TerminalNode DAY() {
			return getToken(ODataParser.DAY, 0);
		}

		public TerminalNode HOUR() {
			return getToken(ODataParser.HOUR, 0);
		}

		public TerminalNode MINUTE() {
			return getToken(ODataParser.MINUTE, 0);
		}

		public TerminalNode SECOND() {
			return getToken(ODataParser.SECOND, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_integerCompareCommand;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterIntegerCompareCommand(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitIntegerCompareCommand(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitIntegerCompareCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class FloatComparatorExpressionContext extends ParserRuleContext {
		public FloatComparatorExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public FloatCommandContext floatCommand() {
			return getRuleContext(FloatCommandContext.class, 0);
		}

		public TerminalNode LR_BRACKET() {
			return getToken(ODataParser.LR_BRACKET, 0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class, 0);
		}

		public TerminalNode RR_BRACKET() {
			return getToken(ODataParser.RR_BRACKET, 0);
		}

		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class, 0);
		}

		public FloatOrIntegerValueContext floatOrIntegerValue() {
			return getRuleContext(FloatOrIntegerValueContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_floatComparatorExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterFloatComparatorExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitFloatComparatorExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitFloatComparatorExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class FloatCommandContext extends ParserRuleContext {
		public FloatCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode ROUND() {
			return getToken(ODataParser.ROUND, 0);
		}

		public TerminalNode FLOOR() {
			return getToken(ODataParser.FLOOR, 0);
		}

		public TerminalNode CEILING() {
			return getToken(ODataParser.CEILING, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_floatCommand;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterFloatCommand(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitFloatCommand(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitFloatCommand(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class FloatOrIntegerValueContext extends ParserRuleContext {
		public FloatOrIntegerValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public FloatValueContext floatValue() {
			return getRuleContext(FloatValueContext.class, 0);
		}

		public IntegerValueContext integerValue() {
			return getRuleContext(IntegerValueContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_floatOrIntegerValue;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterFloatOrIntegerValue(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitFloatOrIntegerValue(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitFloatOrIntegerValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class OrderByClauseContext extends ParserRuleContext {
		public OrderByClauseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode ORDER() {
			return getToken(ODataParser.ORDER, 0);
		}

		public TerminalNode BY() {
			return getToken(ODataParser.BY, 0);
		}

		public List<OrderByExpressionContext> orderByExpression() {
			return getRuleContexts(OrderByExpressionContext.class);
		}

		public OrderByExpressionContext orderByExpression(int i) {
			return getRuleContext(OrderByExpressionContext.class, i);
		}

		public List<TerminalNode> COMMA() {
			return getTokens(ODataParser.COMMA);
		}

		public TerminalNode COMMA(int i) {
			return getToken(ODataParser.COMMA, i);
		}

		@Override
		public int getRuleIndex() {
			return RULE_orderByClause;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterOrderByClause(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitOrderByClause(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitOrderByClause(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class OrderByExpressionContext extends ParserRuleContext {
		public OrderByExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class, 0);
		}

		public OrderAscDescContext orderAscDesc() {
			return getRuleContext(OrderAscDescContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_orderByExpression;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterOrderByExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitOrderByExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitOrderByExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class OrderAscDescContext extends ParserRuleContext {
		public OrderAscDescContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode ASC() {
			return getToken(ODataParser.ASC, 0);
		}

		public TerminalNode DESC() {
			return getToken(ODataParser.DESC, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_orderAscDesc;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterOrderAscDesc(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitOrderAscDesc(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitOrderAscDesc(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class ComparisonOperatorContext extends ParserRuleContext {
		public ComparisonOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode EQ() {
			return getToken(ODataParser.EQ, 0);
		}

		public TerminalNode GT() {
			return getToken(ODataParser.GT, 0);
		}

		public TerminalNode LT() {
			return getToken(ODataParser.LT, 0);
		}

		public TerminalNode LE() {
			return getToken(ODataParser.LE, 0);
		}

		public TerminalNode GE() {
			return getToken(ODataParser.GE, 0);
		}

		public TerminalNode NE() {
			return getToken(ODataParser.NE, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_comparisonOperator;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterComparisonOperator(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitComparisonOperator(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitComparisonOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class LogicalOperatorContext extends ParserRuleContext {
		public LogicalOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode AND() {
			return getToken(ODataParser.AND, 0);
		}

		public TerminalNode OR() {
			return getToken(ODataParser.OR, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_logicalOperator;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterLogicalOperator(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitLogicalOperator(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitLogicalOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class ColumnNameContext extends ParserRuleContext {
		public ColumnNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode ID() {
			return getToken(ODataParser.ID, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_columnName;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterColumnName(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitColumnName(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitColumnName(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class EntityNameContext extends ParserRuleContext {
		public EntityNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode NOARK_ENTITY() {
			return getToken(ODataParser.NOARK_ENTITY, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_entityName;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterEntityName(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitEntityName(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitEntityName(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class AttributeNameContext extends ParserRuleContext {
		public AttributeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode NOARK_ENTITY() {
			return getToken(ODataParser.NOARK_ENTITY, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_attributeName;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterAttributeName(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitAttributeName(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitAttributeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class PackageNameContext extends ParserRuleContext {
		public PackageNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode NOARK_ENTITY() {
			return getToken(ODataParser.NOARK_ENTITY, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_packageName;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterPackageName(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitPackageName(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitPackageName(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class ValueContext extends ParserRuleContext {
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode ID() {
			return getToken(ODataParser.ID, 0);
		}

		public TerminalNode QUOTED_STRING() {
			return getToken(ODataParser.QUOTED_STRING, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_value;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterValue(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitValue(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class SingleQuotedStringContext extends ParserRuleContext {
		public SingleQuotedStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode QUOTED_STRING() {
			return getToken(ODataParser.QUOTED_STRING, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_singleQuotedString;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterSingleQuotedString(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitSingleQuotedString(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitSingleQuotedString(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class IntegerValueContext extends ParserRuleContext {
		public IntegerValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode INTEGER() {
			return getToken(ODataParser.INTEGER, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_integerValue;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterIntegerValue(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitIntegerValue(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitIntegerValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class FloatValueContext extends ParserRuleContext {
		public FloatValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode FLOAT() {
			return getToken(ODataParser.FLOAT, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_floatValue;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterFloatValue(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitFloatValue(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitFloatValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class SystemIdValueContext extends ParserRuleContext {
		public SystemIdValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode UUID() {
			return getToken(ODataParser.UUID, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_systemIdValue;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterSystemIdValue(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitSystemIdValue(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitSystemIdValue(this);
			else return visitor.visitChildren(this);
		}
	}
}