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
			SPACE = 1, FILTER = 2, TOP = 3, SKIPRULE = 4, SKIPTOKEN = 5, ORDERBY = 6, REF = 7, EXPAND = 8,
			COUNT = 9, SELECT = 10, DOLLARID = 11, CONTAINS = 12, STARTSWITH = 13, ENDSWITH = 14,
			SUBSTRINGOF = 15, LENGTH = 16, INDEXOF = 17, REPLACE = 18, SUBSTRING = 19, TOLOWER = 20,
			TOUPPER = 21, TRIM = 22, CONCAT = 23, DAY = 24, MONTH = 25, YEAR = 26, HOUR = 27, MINUTE = 28,
			SECOND = 29, NOW = 30, TIME = 31, MAX_DATE_TIME = 32, MIN_DATE_TIME = 33, TOTAL_OFFSET_MINUTES = 34,
			FRACTIONAL_SECONDS = 35, TOTAL_SECONDS = 36, GEO_INTERSECTS = 37, GEO_DISTANCE = 38,
			GEO_LENGTH = 39, ROUND = 40, FLOOR = 41, CEILING = 42, CAST = 43, ISOF = 44, EQUAL = 45,
			EQ = 46, GT = 47, LT = 48, GE = 49, LE = 50, NE = 51, ADD = 52, SUB = 53, MUL = 54, DIV = 55,
			MOD = 56, ORDER = 57, BY = 58, DESC = 59, ASC = 60, OR = 61, AND = 62, NOT = 63, SEPERATOR = 64,
			HTTP = 65, HTTPS = 66, OPEN = 67, CLOSE = 68, COMMA = 69, QUESTION = 70, DOLLAR = 71,
			SEMI = 72, AT_SIGN = 73, BAR = 74, SINGLE_QUOTE_SYMB = 75, DOUBLE_QUOTE_SYMB = 76,
			REVERSE_QUOTE_SYMB = 77, COLON_SYMB = 78, AMPERSAND = 79, NULL_TOKEN = 80, NULL_SPEC_LITERAL = 81,
			DOT = 82, SLASH = 83, UNDERSCORE = 84, EDM = 85, COLLECTION = 86, GEOGRAPHY = 87,
			GEOMETRY = 88, BINARY = 89, BOOLEAN = 90, BYTE = 91, DATE = 92, DATETIMEOFFSET = 93,
			DECIMAL = 94, DOUBLE = 95, DURATION = 96, GUID = 97, INT16 = 98, INT32 = 99, INT64 = 100,
			SBYTE = 101, SINGLE = 102, STREAM = 103, STRING = 104, TIMEOFDAY = 105, LINESTRING = 106,
			MULTILINESTRING = 107, MULTIPOINT = 108, MULTIPOLYGON = 109, POINT = 110, POLYGON = 111,
			BOOLEAN_VALUE = 112, UUID = 113, INTEGER = 114, FLOAT = 115, BSM_ID = 116, ID = 117,
			QUOTED_UUID = 118, QUOTED_STRING = 119, STRING_LITERAL = 120, DECIMAL_LITERAL = 121,
			TIME_OF_DAY_VALUE = 122, DURATION_VALUE = 123, DATE_VALUE = 124, DATE_TIME_OFFSET_VALUE = 125,
			HOUR_DEF = 126, MINUTE_DEF = 127, SECOND_DEF = 128, ZERO_TO_FIFTY_NINE = 129,
			ONE_TO_NINE = 130, YEAR_DEF = 131, MONTH_DEF = 132, DAY_DEF = 133, HEX = 134, SINGLE_CHAR_SMALL = 135,
			SINGLE_CHAR = 136, NEGATIVE = 137, DEC_OCTECT = 138, HEX_NUMBER = 139, BIN_NUMBER = 140,
			ERROR_RECONGNIGION = 141;
	public static final int
			RULE_odataRelativeUri = 0, RULE_resourcePath = 1, RULE_entity = 2, RULE_entityCast = 3,
			RULE_entityUUID = 4, RULE_embeddedEntitySet = 5, RULE_queryOptions = 6,
			RULE_queryOption = 7, RULE_filter = 8, RULE_expand = 9, RULE_filterExpression = 10,
			RULE_boolCommonExpr = 11, RULE_leftComparatorExpr = 12, RULE_rightComparatorExpr = 13,
			RULE_orderBy = 14, RULE_orderByItem = 15, RULE_topStatement = 16, RULE_skipStatement = 17,
			RULE_joinEntities = 18, RULE_commonExpr = 19, RULE_mathExpr = 20, RULE_methodExpr = 21,
			RULE_compareMethodExpr = 22, RULE_methodCallExpr = 23, RULE_calenderMethodExp = 24,
			RULE_concatMethodExpr = 25, RULE_singleMethodCallExpr = 26, RULE_substringMethodCallExpr = 27,
			RULE_comparisonOperator = 28, RULE_compareMethodName = 29, RULE_methodName = 30,
			RULE_calenderMethodName = 31, RULE_number = 32, RULE_primitiveLiteral = 33,
			RULE_countStatement = 34, RULE_attributeName = 35, RULE_openPar = 36,
			RULE_closePar = 37, RULE_logicalOperator = 38, RULE_sortOrder = 39, RULE_entityName = 40,
			RULE_orderAttributeName = 41, RULE_uuidIdValue = 42, RULE_quotedUUID = 43,
			RULE_quotedString = 44, RULE_nullSpecLiteral = 45, RULE_nullToken = 46,
			RULE_booleanValue = 47, RULE_durationValue = 48, RULE_dateValue = 49,
			RULE_dateTimeOffsetValue = 50, RULE_timeOfDayValue = 51, RULE_decimalLiteral = 52,
			RULE_floatValue = 53, RULE_integerValue = 54;
	public static final String[] ruleNames = makeRuleNames();
	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	public static final String _serializedATN =
			"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u008f\u0195\4\2\t" +
					"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13" +
					"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22" +
					"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31" +
					"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!" +
					"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4" +
					",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t" +
					"\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\3\2\3\2\3\2\5\2t\n\2\3\3\3\3\3" +
					"\3\5\3y\n\3\3\3\5\3|\n\3\3\4\3\4\5\4\u0080\n\4\3\5\3\5\3\5\3\5\3\6\3\6" +
					"\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\5\7\u0090\n\7\7\7\u0092\n\7\f\7\16\7" +
					"\u0095\13\7\3\b\3\b\3\b\7\b\u009a\n\b\f\b\16\b\u009d\13\b\3\t\3\t\3\t" +
					"\3\t\3\t\3\t\3\t\3\t\3\t\7\t\u00a8\n\t\f\t\16\t\u00ab\13\t\3\n\3\n\3\n" +
					"\3\n\3\13\3\13\3\13\3\13\3\13\5\13\u00b6\n\13\3\13\3\13\5\13\u00ba\n\13" +
					"\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00c2\n\f\3\f\3\f\3\f\3\f\7\f\u00c8\n\f\f" +
					"\f\16\f\u00cb\13\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u00d6\n\r\3" +
					"\16\3\16\3\16\3\16\3\16\3\16\5\16\u00de\n\16\3\17\3\17\3\17\3\17\3\17" +
					"\3\17\5\17\u00e6\n\17\3\20\3\20\3\20\3\20\3\20\7\20\u00ed\n\20\f\20\16" +
					"\20\u00f0\13\20\3\21\3\21\5\21\u00f4\n\21\3\22\3\22\3\22\3\23\3\23\3\23" +
					"\3\24\3\24\3\24\6\24\u00ff\n\24\r\24\16\24\u0100\3\24\3\24\3\25\3\25\3" +
					"\25\5\25\u0108\n\25\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\5\27" +
					"\u0113\n\27\3\30\3\30\3\30\3\30\3\30\5\30\u011a\n\30\3\30\3\30\3\30\3" +
					"\30\3\31\3\31\3\31\3\31\3\31\5\31\u0125\n\31\3\31\3\31\3\32\3\32\3\32" +
					"\3\32\3\32\5\32\u012e\n\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\5\33\u0137" +
					"\n\33\3\33\3\33\3\33\5\33\u013c\n\33\3\33\3\33\3\34\3\34\3\34\3\34\3\35" +
					"\3\35\3\35\3\35\3\35\5\35\u0149\n\35\3\35\3\35\3\35\3\35\3\36\3\36\3\37" +
					"\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\5#\u0165" +
					"\n#\3$\3$\3$\3$\5$\u016b\n$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3" +
					"+\3,\3,\3-\3-\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63\3\64" +
					"\3\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\38\2\3\269\2\4\6\b\n\f\16\20" +
					"\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\^`bdfhj" +
					"ln\2\f\3\2\66:\4\2  \"#\3\2\60\65\5\2\16\20\23\23\'(\5\2\22\22\26\30)" +
					",\6\2\32\37!!$&^^\3\2tu\3\2vw\3\2?@\3\2=>\2\u019c\2p\3\2\2\2\4x\3\2\2" +
					"\2\6\177\3\2\2\2\b\u0081\3\2\2\2\n\u0085\3\2\2\2\f\u0089\3\2\2\2\16\u0096" +
					"\3\2\2\2\20\u009e\3\2\2\2\22\u00ac\3\2\2\2\24\u00b0\3\2\2\2\26\u00c1\3" +
					"\2\2\2\30\u00d5\3\2\2\2\32\u00dd\3\2\2\2\34\u00e5\3\2\2\2\36\u00e7\3\2" +
					"\2\2 \u00f1\3\2\2\2\"\u00f5\3\2\2\2$\u00f8\3\2\2\2&\u00fe\3\2\2\2(\u0107" +
					"\3\2\2\2*\u0109\3\2\2\2,\u0112\3\2\2\2.\u0114\3\2\2\2\60\u011f\3\2\2\2" +
					"\62\u0128\3\2\2\2\64\u0131\3\2\2\2\66\u013f\3\2\2\28\u0143\3\2\2\2:\u014e" +
					"\3\2\2\2<\u0150\3\2\2\2>\u0152\3\2\2\2@\u0154\3\2\2\2B\u0156\3\2\2\2D" +
					"\u0164\3\2\2\2F\u0166\3\2\2\2H\u016c\3\2\2\2J\u016e\3\2\2\2L\u0170\3\2" +
					"\2\2N\u0172\3\2\2\2P\u0174\3\2\2\2R\u0176\3\2\2\2T\u0178\3\2\2\2V\u017a" +
					"\3\2\2\2X\u017c\3\2\2\2Z\u017e\3\2\2\2\\\u0180\3\2\2\2^\u0182\3\2\2\2" +
					"`\u0184\3\2\2\2b\u0186\3\2\2\2d\u0188\3\2\2\2f\u018a\3\2\2\2h\u018c\3" +
					"\2\2\2j\u018e\3\2\2\2l\u0190\3\2\2\2n\u0192\3\2\2\2ps\5\4\3\2qr\7H\2\2" +
					"rt\5\16\b\2sq\3\2\2\2st\3\2\2\2t\3\3\2\2\2uy\5\f\7\2vy\5\n\6\2wy\5\6\4" +
					"\2xu\3\2\2\2xv\3\2\2\2xw\3\2\2\2y{\3\2\2\2z|\5F$\2{z\3\2\2\2{|\3\2\2\2" +
					"|\5\3\2\2\2}\u0080\5R*\2~\u0080\5\b\5\2\177}\3\2\2\2\177~\3\2\2\2\u0080" +
					"\7\3\2\2\2\u0081\u0082\5R*\2\u0082\u0083\7U\2\2\u0083\u0084\5R*\2\u0084" +
					"\t\3\2\2\2\u0085\u0086\5R*\2\u0086\u0087\7U\2\2\u0087\u0088\5V,\2\u0088" +
					"\13\3\2\2\2\u0089\u0093\5\n\6\2\u008a\u008b\7U\2\2\u008b\u0092\5\n\6\2" +
					"\u008c\u008f\7U\2\2\u008d\u0090\5\b\5\2\u008e\u0090\5\6\4\2\u008f\u008d" +
					"\3\2\2\2\u008f\u008e\3\2\2\2\u0090\u0092\3\2\2\2\u0091\u008a\3\2\2\2\u0091" +
					"\u008c\3\2\2\2\u0092\u0095\3\2\2\2\u0093\u0091\3\2\2\2\u0093\u0094\3\2" +
					"\2\2\u0094\r\3\2\2\2\u0095\u0093\3\2\2\2\u0096\u009b\5\20\t\2\u0097\u0098" +
					"\7Q\2\2\u0098\u009a\5\20\t\2\u0099\u0097\3\2\2\2\u009a\u009d\3\2\2\2\u009b" +
					"\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c\17\3\2\2\2\u009d\u009b\3\2\2" +
					"\2\u009e\u00a9\5\22\n\2\u009f\u00a0\7Q\2\2\u00a0\u00a8\5\24\13\2\u00a1" +
					"\u00a2\7Q\2\2\u00a2\u00a8\5\36\20\2\u00a3\u00a4\7Q\2\2\u00a4\u00a8\5$" +
					"\23\2\u00a5\u00a6\7Q\2\2\u00a6\u00a8\5\"\22\2\u00a7\u009f\3\2\2\2\u00a7" +
					"\u00a1\3\2\2\2\u00a7\u00a3\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a8\u00ab\3\2" +
					"\2\2\u00a9\u00a7\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\21\3\2\2\2\u00ab\u00a9" +
					"\3\2\2\2\u00ac\u00ad\7\4\2\2\u00ad\u00ae\7/\2\2\u00ae\u00af\5\26\f\2\u00af" +
					"\23\3\2\2\2\u00b0\u00b1\7\n\2\2\u00b1\u00b5\7/\2\2\u00b2\u00b6\5&\24\2" +
					"\u00b3\u00b6\5H%\2\u00b4\u00b6\5\30\r\2\u00b5\u00b2\3\2\2\2\u00b5\u00b3" +
					"\3\2\2\2\u00b5\u00b4\3\2\2\2\u00b6\u00b9\3\2\2\2\u00b7\u00b8\7U\2\2\u00b8" +
					"\u00ba\5\22\n\2\u00b9\u00b7\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba\25\3\2\2" +
					"\2\u00bb\u00bc\b\f\1\2\u00bc\u00bd\5J&\2\u00bd\u00be\5\26\f\2\u00be\u00bf" +
					"\5L\'\2\u00bf\u00c2\3\2\2\2\u00c0\u00c2\5\30\r\2\u00c1\u00bb\3\2\2\2\u00c1" +
					"\u00c0\3\2\2\2\u00c2\u00c9\3\2\2\2\u00c3\u00c4\f\4\2\2\u00c4\u00c5\5N" +
					"(\2\u00c5\u00c6\5\26\f\5\u00c6\u00c8\3\2\2\2\u00c7\u00c3\3\2\2\2\u00c8" +
					"\u00cb\3\2\2\2\u00c9\u00c7\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca\27\3\2\2" +
					"\2\u00cb\u00c9\3\2\2\2\u00cc\u00cd\5\32\16\2\u00cd\u00ce\5:\36\2\u00ce" +
					"\u00cf\5\34\17\2\u00cf\u00d6\3\2\2\2\u00d0\u00d1\7\13\2\2\u00d1\u00d2" +
					"\5:\36\2\u00d2\u00d3\5D#\2\u00d3\u00d6\3\2\2\2\u00d4\u00d6\5.\30\2\u00d5" +
					"\u00cc\3\2\2\2\u00d5\u00d0\3\2\2\2\u00d5\u00d4\3\2\2\2\u00d6\31\3\2\2" +
					"\2\u00d7\u00de\5\60\31\2\u00d8\u00de\5\62\32\2\u00d9\u00de\5\64\33\2\u00da" +
					"\u00de\5&\24\2\u00db\u00de\5H%\2\u00dc\u00de\5(\25\2\u00dd\u00d7\3\2\2" +
					"\2\u00dd\u00d8\3\2\2\2\u00dd\u00d9\3\2\2\2\u00dd\u00da\3\2\2\2\u00dd\u00db" +
					"\3\2\2\2\u00dd\u00dc\3\2\2\2\u00de\33\3\2\2\2\u00df\u00e6\5\60\31\2\u00e0" +
					"\u00e6\5\62\32\2\u00e1\u00e6\5\64\33\2\u00e2\u00e6\5&\24\2\u00e3\u00e6" +
					"\5H%\2\u00e4\u00e6\5(\25\2\u00e5\u00df\3\2\2\2\u00e5\u00e0\3\2\2\2\u00e5" +
					"\u00e1\3\2\2\2\u00e5\u00e2\3\2\2\2\u00e5\u00e3\3\2\2\2\u00e5\u00e4\3\2" +
					"\2\2\u00e6\35\3\2\2\2\u00e7\u00e8\7\b\2\2\u00e8\u00e9\7/\2\2\u00e9\u00ee" +
					"\5 \21\2\u00ea\u00eb\7G\2\2\u00eb\u00ed\5 \21\2\u00ec\u00ea\3\2\2\2\u00ed" +
					"\u00f0\3\2\2\2\u00ee\u00ec\3\2\2\2\u00ee\u00ef\3\2\2\2\u00ef\37\3\2\2" +
					"\2\u00f0\u00ee\3\2\2\2\u00f1\u00f3\5T+\2\u00f2\u00f4\5P)\2\u00f3\u00f2" +
					"\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4!\3\2\2\2\u00f5\u00f6\7\5\2\2\u00f6" +
					"\u00f7\5n8\2\u00f7#\3\2\2\2\u00f8\u00f9\7\6\2\2\u00f9\u00fa\5n8\2\u00fa" +
					"%\3\2\2\2\u00fb\u00fc\5R*\2\u00fc\u00fd\7U\2\2\u00fd\u00ff\3\2\2\2\u00fe" +
					"\u00fb\3\2\2\2\u00ff\u0100\3\2\2\2\u0100\u00fe\3\2\2\2\u0100\u0101\3\2" +
					"\2\2\u0101\u0102\3\2\2\2\u0102\u0103\5H%\2\u0103\'\3\2\2\2\u0104\u0108" +
					"\5D#\2\u0105\u0108\5,\27\2\u0106\u0108\5*\26\2\u0107\u0104\3\2\2\2\u0107" +
					"\u0105\3\2\2\2\u0107\u0106\3\2\2\2\u0108)\3\2\2\2\u0109\u010a\t\2\2\2" +
					"\u010a\u010b\5B\"\2\u010b+\3\2\2\2\u010c\u0113\5\60\31\2\u010d\u0113\5" +
					"\62\32\2\u010e\u0113\5\66\34\2\u010f\u0113\5\64\33\2\u0110\u0113\58\35" +
					"\2\u0111\u0113\5.\30\2\u0112\u010c\3\2\2\2\u0112\u010d\3\2\2\2\u0112\u010e" +
					"\3\2\2\2\u0112\u010f\3\2\2\2\u0112\u0110\3\2\2\2\u0112\u0111\3\2\2\2\u0113" +
					"-\3\2\2\2\u0114\u0115\5<\37\2\u0115\u0119\7E\2\2\u0116\u011a\5&\24\2\u0117" +
					"\u011a\5H%\2\u0118\u011a\5(\25\2\u0119\u0116\3\2\2\2\u0119\u0117\3\2\2" +
					"\2\u0119\u0118\3\2\2\2\u011a\u011b\3\2\2\2\u011b\u011c\7G\2\2\u011c\u011d" +
					"\5(\25\2\u011d\u011e\7F\2\2\u011e/\3\2\2\2\u011f\u0120\5> \2\u0120\u0124" +
					"\7E\2\2\u0121\u0125\5\60\31\2\u0122\u0125\5&\24\2\u0123\u0125\5H%\2\u0124" +
					"\u0121\3\2\2\2\u0124\u0122\3\2\2\2\u0124\u0123\3\2\2\2\u0125\u0126\3\2" +
					"\2\2\u0126\u0127\7F\2\2\u0127\61\3\2\2\2\u0128\u0129\5@!\2\u0129\u012d" +
					"\7E\2\2\u012a\u012e\5&\24\2\u012b\u012e\5H%\2\u012c\u012e\5D#\2\u012d" +
					"\u012a\3\2\2\2\u012d\u012b\3\2\2\2\u012d\u012c\3\2\2\2\u012e\u012f\3\2" +
					"\2\2\u012f\u0130\7F\2\2\u0130\63\3\2\2\2\u0131\u0132\7\31\2\2\u0132\u0136" +
					"\7E\2\2\u0133\u0137\5\64\33\2\u0134\u0137\5D#\2\u0135\u0137\5H%\2\u0136" +
					"\u0133\3\2\2\2\u0136\u0134\3\2\2\2\u0136\u0135\3\2\2\2\u0137\u0138\3\2" +
					"\2\2\u0138\u013b\7G\2\2\u0139\u013c\5D#\2\u013a\u013c\5H%\2\u013b\u0139" +
					"\3\2\2\2\u013b\u013a\3\2\2\2\u013c\u013d\3\2\2\2\u013d\u013e\7F\2\2\u013e" +
					"\65\3\2\2\2\u013f\u0140\t\3\2\2\u0140\u0141\7E\2\2\u0141\u0142\7F\2\2" +
					"\u0142\67\3\2\2\2\u0143\u0144\7\25\2\2\u0144\u0148\7E\2\2\u0145\u0149" +
					"\5&\24\2\u0146\u0149\5H%\2\u0147\u0149\5(\25\2\u0148\u0145\3\2\2\2\u0148" +
					"\u0146\3\2\2\2\u0148\u0147\3\2\2\2\u0149\u014a\3\2\2\2\u014a\u014b\7G" +
					"\2\2\u014b\u014c\5(\25\2\u014c\u014d\7F\2\2\u014d9\3\2\2\2\u014e\u014f" +
					"\t\4\2\2\u014f;\3\2\2\2\u0150\u0151\t\5\2\2\u0151=\3\2\2\2\u0152\u0153" +
					"\t\6\2\2\u0153?\3\2\2\2\u0154\u0155\t\7\2\2\u0155A\3\2\2\2\u0156\u0157" +
					"\t\b\2\2\u0157C\3\2\2\2\u0158\u0165\5X-\2\u0159\u0165\5Z.\2\u015a\u0165" +
					"\5\\/\2\u015b\u0165\5^\60\2\u015c\u0165\5`\61\2\u015d\u0165\5b\62\2\u015e" +
					"\u0165\5d\63\2\u015f\u0165\5f\64\2\u0160\u0165\5h\65\2\u0161\u0165\5j" +
					"\66\2\u0162\u0165\5l\67\2\u0163\u0165\5n8\2\u0164\u0158\3\2\2\2\u0164" +
					"\u0159\3\2\2\2\u0164\u015a\3\2\2\2\u0164\u015b\3\2\2\2\u0164\u015c\3\2" +
					"\2\2\u0164\u015d\3\2\2\2\u0164\u015e\3\2\2\2\u0164\u015f\3\2\2\2\u0164" +
					"\u0160\3\2\2\2\u0164\u0161\3\2\2\2\u0164\u0162\3\2\2\2\u0164\u0163\3\2" +
					"\2\2\u0165E\3\2\2\2\u0166\u0167\7U\2\2\u0167\u016a\7\13\2\2\u0168\u0169" +
					"\7/\2\2\u0169\u016b\5`\61\2\u016a\u0168\3\2\2\2\u016a\u016b\3\2\2\2\u016b" +
					"G\3\2\2\2\u016c\u016d\t\t\2\2\u016dI\3\2\2\2\u016e\u016f\7E\2\2\u016f" +
					"K\3\2\2\2\u0170\u0171\7F\2\2\u0171M\3\2\2\2\u0172\u0173\t\n\2\2\u0173" +
					"O\3\2\2\2\u0174\u0175\t\13\2\2\u0175Q\3\2\2\2\u0176\u0177\7w\2\2\u0177" +
					"S\3\2\2\2\u0178\u0179\7w\2\2\u0179U\3\2\2\2\u017a\u017b\7s\2\2\u017bW" +
					"\3\2\2\2\u017c\u017d\7x\2\2\u017dY\3\2\2\2\u017e\u017f\7y\2\2\u017f[\3" +
					"\2\2\2\u0180\u0181\7S\2\2\u0181]\3\2\2\2\u0182\u0183\7R\2\2\u0183_\3\2" +
					"\2\2\u0184\u0185\7r\2\2\u0185a\3\2\2\2\u0186\u0187\7}\2\2\u0187c\3\2\2" +
					"\2\u0188\u0189\7~\2\2\u0189e\3\2\2\2\u018a\u018b\7\177\2\2\u018bg\3\2" +
					"\2\2\u018c\u018d\7|\2\2\u018di\3\2\2\2\u018e\u018f\7{\2\2\u018fk\3\2\2" +
					"\2\u0190\u0191\7u\2\2\u0191m\3\2\2\2\u0192\u0193\7t\2\2\u0193o\3\2\2\2" +
					" sx{\177\u008f\u0091\u0093\u009b\u00a7\u00a9\u00b5\u00b9\u00c1\u00c9\u00d5" +
					"\u00dd\u00e5\u00ee\u00f3\u0100\u0107\u0112\u0119\u0124\u012d\u0136\u013b" +
					"\u0148\u0164\u016a";
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

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	public ODataParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
	}

	private static String[] makeRuleNames() {
		return new String[]{
				"odataRelativeUri", "resourcePath", "entity", "entityCast", "entityUUID",
				"embeddedEntitySet", "queryOptions", "queryOption", "filter", "expand",
				"filterExpression", "boolCommonExpr", "leftComparatorExpr", "rightComparatorExpr",
				"orderBy", "orderByItem", "topStatement", "skipStatement", "joinEntities",
				"commonExpr", "mathExpr", "methodExpr", "compareMethodExpr", "methodCallExpr",
				"calenderMethodExp", "concatMethodExpr", "singleMethodCallExpr", "substringMethodCallExpr",
				"comparisonOperator", "compareMethodName", "methodName", "calenderMethodName",
				"number", "primitiveLiteral", "countStatement", "attributeName", "openPar",
				"closePar", "logicalOperator", "sortOrder", "entityName", "orderAttributeName",
				"uuidIdValue", "quotedUUID", "quotedString", "nullSpecLiteral", "nullToken",
				"booleanValue", "durationValue", "dateValue", "dateTimeOffsetValue",
				"timeOfDayValue", "decimalLiteral", "floatValue", "integerValue"
		};
	}

	private static String[] makeLiteralNames() {
		return new String[]{
				null, null, null, null, null, "'$skiptoken'", "'$orderby'", "'$ref?$id='",
				"'$expand'", "'$count'", "'$select='", "'$id='", null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, "'='", null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null,
				"'://'", null, null, "'('", "')'", "','", "'?'", "'$'", "';'", "'@'",
				"'|'", "'''", "'\"'", "'`'", "':'", "'&'", null, null, "'.'", "'/'",
				"'_'", "'Edm'", "'Collection'", "'Geography'", "'Geometry'", "'Binary'",
				"'Boolean'", "'Byte'", "'Date'", "'DateTimeOffset'", "'Decimal'", "'Double'",
				"'Duration'", "'Guid'", "'Int16'", "'Int32'", "'Int64'", "'SByte'", "'Single'",
				"'Stream'", "'String'", "'TimeOfDay'", null, "'MultiLineString'", "'MultiPoint'",
				"'MultiPolygon'", "'Point'", "'Polygon'", null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, "'-'"
		};
	}

	private static String[] makeSymbolicNames() {
		return new String[]{
				null, "SPACE", "FILTER", "TOP", "SKIPRULE", "SKIPTOKEN", "ORDERBY", "REF",
				"EXPAND", "COUNT", "SELECT", "DOLLARID", "CONTAINS", "STARTSWITH", "ENDSWITH",
				"SUBSTRINGOF", "LENGTH", "INDEXOF", "REPLACE", "SUBSTRING", "TOLOWER",
				"TOUPPER", "TRIM", "CONCAT", "DAY", "MONTH", "YEAR", "HOUR", "MINUTE",
				"SECOND", "NOW", "TIME", "MAX_DATE_TIME", "MIN_DATE_TIME", "TOTAL_OFFSET_MINUTES",
				"FRACTIONAL_SECONDS", "TOTAL_SECONDS", "GEO_INTERSECTS", "GEO_DISTANCE",
				"GEO_LENGTH", "ROUND", "FLOOR", "CEILING", "CAST", "ISOF", "EQUAL", "EQ",
				"GT", "LT", "GE", "LE", "NE", "ADD", "SUB", "MUL", "DIV", "MOD", "ORDER",
				"BY", "DESC", "ASC", "OR", "AND", "NOT", "SEPERATOR", "HTTP", "HTTPS",
				"OPEN", "CLOSE", "COMMA", "QUESTION", "DOLLAR", "SEMI", "AT_SIGN", "BAR",
				"SINGLE_QUOTE_SYMB", "DOUBLE_QUOTE_SYMB", "REVERSE_QUOTE_SYMB", "COLON_SYMB",
				"AMPERSAND", "NULL_TOKEN", "NULL_SPEC_LITERAL", "DOT", "SLASH", "UNDERSCORE",
				"EDM", "COLLECTION", "GEOGRAPHY", "GEOMETRY", "BINARY", "BOOLEAN", "BYTE",
				"DATE", "DATETIMEOFFSET", "DECIMAL", "DOUBLE", "DURATION", "GUID", "INT16",
				"INT32", "INT64", "SBYTE", "SINGLE", "STREAM", "STRING", "TIMEOFDAY",
				"LINESTRING", "MULTILINESTRING", "MULTIPOINT", "MULTIPOLYGON", "POINT",
				"POLYGON", "BOOLEAN_VALUE", "UUID", "INTEGER", "FLOAT", "BSM_ID", "ID",
				"QUOTED_UUID", "QUOTED_STRING", "STRING_LITERAL", "DECIMAL_LITERAL",
				"TIME_OF_DAY_VALUE", "DURATION_VALUE", "DATE_VALUE", "DATE_TIME_OFFSET_VALUE",
				"HOUR_DEF", "MINUTE_DEF", "SECOND_DEF", "ZERO_TO_FIFTY_NINE", "ONE_TO_NINE",
				"YEAR_DEF", "MONTH_DEF", "DAY_DEF", "HEX", "SINGLE_CHAR_SMALL", "SINGLE_CHAR",
				"NEGATIVE", "DEC_OCTECT", "HEX_NUMBER", "BIN_NUMBER", "ERROR_RECONGNIGION"
		};
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
		return _ATN; }

	public final OdataRelativeUriContext odataRelativeUri() throws RecognitionException {
		OdataRelativeUriContext _localctx = new OdataRelativeUriContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_odataRelativeUri);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(110);
				resourcePath();
				setState(113);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == QUESTION) {
					{
						setState(111);
						match(QUESTION);
						setState(112);
						queryOptions();
					}
				}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final ResourcePathContext resourcePath() throws RecognitionException {
		ResourcePathContext _localctx = new ResourcePathContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_resourcePath);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(118);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 1, _ctx)) {
					case 1: {
						setState(115);
						embeddedEntitySet();
					}
					break;
					case 2: {
						setState(116);
						entityUUID();
					}
					break;
					case 3: {
						setState(117);
						entity();
					}
					break;
				}
				setState(121);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == SLASH) {
					{
						setState(120);
						countStatement();
					}
				}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final EntityContext entity() throws RecognitionException {
		EntityContext _localctx = new EntityContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_entity);
		try {
			setState(125);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 3, _ctx)) {
				case 1:
					enterOuterAlt(_localctx, 1);
				{
					setState(123);
					entityName();
				}
				break;
				case 2:
					enterOuterAlt(_localctx, 2);
				{
					setState(124);
					entityCast();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final EntityCastContext entityCast() throws RecognitionException {
		EntityCastContext _localctx = new EntityCastContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_entityCast);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(127);
				entityName();
				setState(128);
				match(SLASH);
				setState(129);
				entityName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final EntityUUIDContext entityUUID() throws RecognitionException {
		EntityUUIDContext _localctx = new EntityUUIDContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_entityUUID);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(131);
				entityName();
				setState(132);
				match(SLASH);
				setState(133);
				uuidIdValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final EmbeddedEntitySetContext embeddedEntitySet() throws RecognitionException {
		EmbeddedEntitySetContext _localctx = new EmbeddedEntitySetContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_embeddedEntitySet);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(135);
				entityUUID();
				setState(145);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input, 6, _ctx);
				while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						{
							setState(143);
							_errHandler.sync(this);
							switch (getInterpreter().adaptivePredict(_input, 5, _ctx)) {
								case 1: {
									setState(136);
									match(SLASH);
									setState(137);
									entityUUID();
								}
								break;
								case 2: {
									{
										setState(138);
										match(SLASH);
										setState(141);
										_errHandler.sync(this);
										switch (getInterpreter().adaptivePredict(_input, 4, _ctx)) {
											case 1: {
												setState(139);
												entityCast();
											}
											break;
											case 2: {
												setState(140);
												entity();
											}
											break;
										}
									}
								}
								break;
							}
						}
					}
					setState(147);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 6, _ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final QueryOptionsContext queryOptions() throws RecognitionException {
		QueryOptionsContext _localctx = new QueryOptionsContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_queryOptions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(148);
				queryOption();
				setState(153);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == AMPERSAND) {
					{
						{
							setState(149);
							match(AMPERSAND);
							setState(150);
							queryOption();
						}
					}
					setState(155);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final QueryOptionContext queryOption() throws RecognitionException {
		QueryOptionContext _localctx = new QueryOptionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_queryOption);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(156);
				filter();
				setState(167);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input, 9, _ctx);
				while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						{
							setState(165);
							_errHandler.sync(this);
							switch (getInterpreter().adaptivePredict(_input, 8, _ctx)) {
								case 1: {
									setState(157);
									match(AMPERSAND);
									setState(158);
									expand();
								}
								break;
								case 2: {
									setState(159);
									match(AMPERSAND);
									setState(160);
									orderBy();
								}
								break;
								case 3: {
									setState(161);
									match(AMPERSAND);
									setState(162);
									skipStatement();
								}
								break;
								case 4: {
									setState(163);
									match(AMPERSAND);
									setState(164);
									topStatement();
								}
								break;
							}
						}
					}
					setState(169);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 9, _ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final FilterContext filter() throws RecognitionException {
		FilterContext _localctx = new FilterContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_filter);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(170);
				match(FILTER);
				setState(171);
				match(EQUAL);
				setState(172);
				filterExpression(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final ExpandContext expand() throws RecognitionException {
		ExpandContext _localctx = new ExpandContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_expand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(174);
				match(EXPAND);
				setState(175);
				match(EQUAL);
				setState(179);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 10, _ctx)) {
					case 1: {
						setState(176);
						joinEntities();
					}
					break;
					case 2: {
						setState(177);
						attributeName();
					}
					break;
					case 3: {
						setState(178);
						boolCommonExpr();
					}
					break;
				}
				setState(183);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == SLASH) {
					{
						setState(181);
						match(SLASH);
						setState(182);
						filter();
					}
				}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final FilterExpressionContext filterExpression() throws RecognitionException {
		return filterExpression(0);
	}

	private FilterExpressionContext filterExpression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		FilterExpressionContext _localctx = new FilterExpressionContext(_ctx, _parentState);
		FilterExpressionContext _prevctx = _localctx;
		int _startState = 20;
		enterRecursionRule(_localctx, 20, RULE_filterExpression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(191);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
					case OPEN: {
						_localctx = new ParenExpressionContext(_localctx);
						_ctx = _localctx;
						_prevctx = _localctx;

						setState(186);
						openPar();
						setState(187);
						filterExpression(0);
						setState(188);
						closePar();
					}
					break;
					case COUNT:
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
			case TIME:
			case MAX_DATE_TIME:
			case MIN_DATE_TIME:
			case TOTAL_OFFSET_MINUTES:
					case FRACTIONAL_SECONDS:
					case TOTAL_SECONDS:
					case GEO_INTERSECTS:
					case GEO_DISTANCE:
					case GEO_LENGTH:
					case ROUND:
					case FLOOR:
					case CEILING:
					case ADD:
					case SUB:
					case MUL:
					case DIV:
					case MOD:
					case NULL_TOKEN:
					case NULL_SPEC_LITERAL:
					case DATE:
					case BOOLEAN_VALUE:
					case INTEGER:
					case FLOAT:
					case BSM_ID:
					case ID:
					case QUOTED_UUID:
					case QUOTED_STRING:
					case DECIMAL_LITERAL:
					case TIME_OF_DAY_VALUE:
					case DURATION_VALUE:
					case DATE_VALUE:
					case DATE_TIME_OFFSET_VALUE: {
						_localctx = new BoolExpressionContext(_localctx);
						_ctx = _localctx;
						_prevctx = _localctx;
						setState(190);
						boolCommonExpr();
					}
					break;
					default:
						throw new NoViableAltException(this);
				}
				_ctx.stop = _input.LT(-1);
				setState(199);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input, 13, _ctx);
				while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						if (_parseListeners != null) triggerExitRuleEvent();
						_prevctx = _localctx;
						{
							{
								_localctx = new BinaryExpressionContext(new FilterExpressionContext(_parentctx, _parentState));
								((BinaryExpressionContext) _localctx).left = _prevctx;
								pushNewRecursionContext(_localctx, _startState, RULE_filterExpression);
								setState(193);
								if (!(precpred(_ctx, 2)))
									throw new FailedPredicateException(this, "precpred(_ctx, 2)");
								setState(194);
								((BinaryExpressionContext) _localctx).op = logicalOperator();
								setState(195);
								((BinaryExpressionContext) _localctx).right = filterExpression(3);
							}
						}
					}
					setState(201);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 13, _ctx);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public final BoolCommonExprContext boolCommonExpr() throws RecognitionException {
		BoolCommonExprContext _localctx = new BoolCommonExprContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_boolCommonExpr);
		try {
			setState(211);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 14, _ctx)) {
				case 1:
					_localctx = new ComparatorExpressionContext(_localctx);
					enterOuterAlt(_localctx, 1);
				{
					setState(202);
					((ComparatorExpressionContext) _localctx).left = leftComparatorExpr();
					setState(203);
					((ComparatorExpressionContext) _localctx).op = comparisonOperator();
					setState(204);
					((ComparatorExpressionContext) _localctx).right = rightComparatorExpr();
				}
				break;
				case 2:
					_localctx = new CountComparatorExpressionContext(_localctx);
					enterOuterAlt(_localctx, 2);
				{
					setState(206);
					((CountComparatorExpressionContext) _localctx).left = match(COUNT);
					setState(207);
					((CountComparatorExpressionContext) _localctx).op = comparisonOperator();
					setState(208);
					((CountComparatorExpressionContext) _localctx).right = primitiveLiteral();
				}
				break;
				case 3:
					_localctx = new CompareMethodExpressionContext(_localctx);
					enterOuterAlt(_localctx, 3);
				{
					setState(210);
					compareMethodExpr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final LeftComparatorExprContext leftComparatorExpr() throws RecognitionException {
		LeftComparatorExprContext _localctx = new LeftComparatorExprContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_leftComparatorExpr);
		try {
			setState(219);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 15, _ctx)) {
				case 1:
					enterOuterAlt(_localctx, 1);
				{
					setState(213);
					methodCallExpr();
				}
				break;
				case 2:
					enterOuterAlt(_localctx, 2);
				{
					setState(214);
					calenderMethodExp();
				}
				break;
				case 3:
					enterOuterAlt(_localctx, 3);
				{
					setState(215);
					concatMethodExpr();
				}
				break;
				case 4:
					enterOuterAlt(_localctx, 4);
				{
					setState(216);
					joinEntities();
				}
				break;
				case 5:
					enterOuterAlt(_localctx, 5);
				{
					setState(217);
					attributeName();
				}
				break;
				case 6:
					enterOuterAlt(_localctx, 6);
				{
					setState(218);
					commonExpr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final RightComparatorExprContext rightComparatorExpr() throws RecognitionException {
		RightComparatorExprContext _localctx = new RightComparatorExprContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_rightComparatorExpr);
		try {
			setState(227);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 16, _ctx)) {
				case 1:
					enterOuterAlt(_localctx, 1);
				{
					setState(221);
					methodCallExpr();
				}
				break;
				case 2:
					enterOuterAlt(_localctx, 2);
				{
					setState(222);
					calenderMethodExp();
				}
				break;
				case 3:
					enterOuterAlt(_localctx, 3);
				{
					setState(223);
					concatMethodExpr();
				}
				break;
				case 4:
					enterOuterAlt(_localctx, 4);
				{
					setState(224);
					joinEntities();
				}
				break;
				case 5:
					enterOuterAlt(_localctx, 5);
				{
					setState(225);
					attributeName();
				}
				break;
				case 6:
					enterOuterAlt(_localctx, 6);
				{
					setState(226);
					commonExpr();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final OrderByContext orderBy() throws RecognitionException {
		OrderByContext _localctx = new OrderByContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_orderBy);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(229);
				match(ORDERBY);
				setState(230);
				match(EQUAL);
				setState(231);
				orderByItem();
				setState(236);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == COMMA) {
					{
						{
							setState(232);
							match(COMMA);
							setState(233);
							orderByItem();
						}
					}
					setState(238);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final OrderByItemContext orderByItem() throws RecognitionException {
		OrderByItemContext _localctx = new OrderByItemContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_orderByItem);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(239);
				orderAttributeName();
				setState(241);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == DESC || _la == ASC) {
					{
						setState(240);
						sortOrder();
					}
				}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final TopStatementContext topStatement() throws RecognitionException {
		TopStatementContext _localctx = new TopStatementContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_topStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(243);
				match(TOP);
				setState(244);
				integerValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final SkipStatementContext skipStatement() throws RecognitionException {
		SkipStatementContext _localctx = new SkipStatementContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_skipStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(246);
				match(SKIPRULE);
				setState(247);
				integerValue();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final JoinEntitiesContext joinEntities() throws RecognitionException {
		JoinEntitiesContext _localctx = new JoinEntitiesContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_joinEntities);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(252);
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
						case 1: {
							{
								setState(249);
								entityName();
								setState(250);
								match(SLASH);
							}
						}
						break;
						default:
							throw new NoViableAltException(this);
					}
					setState(254);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 19, _ctx);
				} while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
				setState(256);
				attributeName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final CommonExprContext commonExpr() throws RecognitionException {
		CommonExprContext _localctx = new CommonExprContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_commonExpr);
		try {
			setState(261);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
				case NULL_TOKEN:
				case NULL_SPEC_LITERAL:
				case BOOLEAN_VALUE:
				case INTEGER:
				case FLOAT:
				case QUOTED_UUID:
				case QUOTED_STRING:
				case DECIMAL_LITERAL:
				case TIME_OF_DAY_VALUE:
				case DURATION_VALUE:
				case DATE_VALUE:
				case DATE_TIME_OFFSET_VALUE:
					enterOuterAlt(_localctx, 1);
				{
					setState(258);
					primitiveLiteral();
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
			case TIME:
			case MAX_DATE_TIME:
			case MIN_DATE_TIME:
			case TOTAL_OFFSET_MINUTES:
				case FRACTIONAL_SECONDS:
				case TOTAL_SECONDS:
				case GEO_INTERSECTS:
				case GEO_DISTANCE:
				case GEO_LENGTH:
				case ROUND:
				case FLOOR:
				case CEILING:
				case DATE:
					enterOuterAlt(_localctx, 2);
				{
					setState(259);
					methodExpr();
				}
				break;
				case ADD:
				case SUB:
				case MUL:
				case DIV:
				case MOD:
					enterOuterAlt(_localctx, 3);
				{
					setState(260);
					mathExpr();
				}
				break;
				default:
					throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final MathExprContext mathExpr() throws RecognitionException {
		MathExprContext _localctx = new MathExprContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_mathExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(263);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ADD) | (1L << SUB) | (1L << MUL) | (1L << DIV) | (1L << MOD))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(264);
				number();
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

	public final MethodExprContext methodExpr() throws RecognitionException {
		MethodExprContext _localctx = new MethodExprContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_methodExpr);
		try {
			setState(272);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
				case LENGTH:
				case TOLOWER:
				case TOUPPER:
				case TRIM:
				case GEO_LENGTH:
				case ROUND:
				case FLOOR:
				case CEILING:
					enterOuterAlt(_localctx, 1);
				{
					setState(266);
					methodCallExpr();
				}
				break;
				case DAY:
				case MONTH:
				case YEAR:
				case HOUR:
				case MINUTE:
				case SECOND:
				case TIME:
				case TOTAL_OFFSET_MINUTES:
				case FRACTIONAL_SECONDS:
				case TOTAL_SECONDS:
				case DATE:
					enterOuterAlt(_localctx, 2);
				{
					setState(267);
					calenderMethodExp();
				}
				break;
				case NOW:
				case MAX_DATE_TIME:
				case MIN_DATE_TIME:
					enterOuterAlt(_localctx, 3);
				{
					setState(268);
					singleMethodCallExpr();
				}
				break;
				case CONCAT:
					enterOuterAlt(_localctx, 4);
				{
					setState(269);
					concatMethodExpr();
				}
				break;
				case SUBSTRING:
					enterOuterAlt(_localctx, 5);
				{
					setState(270);
					substringMethodCallExpr();
				}
				break;
				case CONTAINS:
				case STARTSWITH:
				case ENDSWITH:
				case INDEXOF:
				case GEO_INTERSECTS:
				case GEO_DISTANCE:
					enterOuterAlt(_localctx, 6);
				{
					setState(271);
					compareMethodExpr();
				}
				break;
				default:
					throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final CompareMethodExprContext compareMethodExpr() throws RecognitionException {
		CompareMethodExprContext _localctx = new CompareMethodExprContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_compareMethodExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(274);
				compareMethodName();
				setState(275);
				match(OPEN);
				setState(279);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 22, _ctx)) {
					case 1: {
						setState(276);
						joinEntities();
					}
					break;
					case 2: {
						setState(277);
						attributeName();
					}
					break;
					case 3: {
						setState(278);
						commonExpr();
					}
					break;
				}
				setState(281);
				match(COMMA);
				setState(282);
				commonExpr();
				setState(283);
				match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final MethodCallExprContext methodCallExpr() throws RecognitionException {
		MethodCallExprContext _localctx = new MethodCallExprContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_methodCallExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(285);
				methodName();
				setState(286);
				match(OPEN);
				setState(290);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 23, _ctx)) {
					case 1: {
						setState(287);
						methodCallExpr();
					}
					break;
					case 2: {
						setState(288);
						joinEntities();
					}
					break;
					case 3: {
						setState(289);
						attributeName();
					}
					break;
				}
				setState(292);
				match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final CalenderMethodExpContext calenderMethodExp() throws RecognitionException {
		CalenderMethodExpContext _localctx = new CalenderMethodExpContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_calenderMethodExp);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(294);
				calenderMethodName();
				setState(295);
				match(OPEN);
				setState(299);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 24, _ctx)) {
					case 1: {
						setState(296);
						joinEntities();
					}
					break;
					case 2: {
						setState(297);
						attributeName();
					}
					break;
					case 3: {
						setState(298);
						primitiveLiteral();
					}
					break;
				}
				setState(301);
				match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final ConcatMethodExprContext concatMethodExpr() throws RecognitionException {
		ConcatMethodExprContext _localctx = new ConcatMethodExprContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_concatMethodExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(303);
				match(CONCAT);
				setState(304);
				match(OPEN);
				setState(308);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
					case CONCAT: {
						setState(305);
						concatMethodExpr();
					}
					break;
					case NULL_TOKEN:
					case NULL_SPEC_LITERAL:
					case BOOLEAN_VALUE:
					case INTEGER:
					case FLOAT:
					case QUOTED_UUID:
					case QUOTED_STRING:
					case DECIMAL_LITERAL:
					case TIME_OF_DAY_VALUE:
					case DURATION_VALUE:
					case DATE_VALUE:
					case DATE_TIME_OFFSET_VALUE: {
						setState(306);
						primitiveLiteral();
					}
					break;
					case BSM_ID:
					case ID: {
						setState(307);
						attributeName();
					}
					break;
					default:
						throw new NoViableAltException(this);
				}
				setState(310);
				match(COMMA);
				setState(313);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
					case NULL_TOKEN:
					case NULL_SPEC_LITERAL:
					case BOOLEAN_VALUE:
					case INTEGER:
					case FLOAT:
					case QUOTED_UUID:
					case QUOTED_STRING:
					case DECIMAL_LITERAL:
					case TIME_OF_DAY_VALUE:
					case DURATION_VALUE:
					case DATE_VALUE:
					case DATE_TIME_OFFSET_VALUE: {
						setState(311);
						primitiveLiteral();
					}
					break;
					case BSM_ID:
					case ID: {
						setState(312);
						attributeName();
					}
					break;
					default:
						throw new NoViableAltException(this);
				}
				setState(315);
				match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final SingleMethodCallExprContext singleMethodCallExpr() throws RecognitionException {
		SingleMethodCallExprContext _localctx = new SingleMethodCallExprContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_singleMethodCallExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(317);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NOW) | (1L << MAX_DATE_TIME) | (1L << MIN_DATE_TIME))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(318);
				match(OPEN);
				setState(319);
				match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final SubstringMethodCallExprContext substringMethodCallExpr() throws RecognitionException {
		SubstringMethodCallExprContext _localctx = new SubstringMethodCallExprContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_substringMethodCallExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(321);
				match(SUBSTRING);
				setState(322);
				match(OPEN);
				setState(326);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 27, _ctx)) {
					case 1: {
						setState(323);
						joinEntities();
					}
					break;
					case 2: {
						setState(324);
						attributeName();
					}
					break;
					case 3: {
						setState(325);
						commonExpr();
					}
					break;
				}
				setState(328);
				match(COMMA);
				setState(329);
				commonExpr();
				setState(330);
				match(CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final ComparisonOperatorContext comparisonOperator() throws RecognitionException {
		ComparisonOperatorContext _localctx = new ComparisonOperatorContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_comparisonOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(332);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQ) | (1L << GT) | (1L << LT) | (1L << GE) | (1L << LE) | (1L << NE))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final CompareMethodNameContext compareMethodName() throws RecognitionException {
		CompareMethodNameContext _localctx = new CompareMethodNameContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_compareMethodName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(334);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CONTAINS) | (1L << STARTSWITH) | (1L << ENDSWITH) | (1L << INDEXOF) | (1L << GEO_INTERSECTS) | (1L << GEO_DISTANCE))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final MethodNameContext methodName() throws RecognitionException {
		MethodNameContext _localctx = new MethodNameContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_methodName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(336);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LENGTH) | (1L << TOLOWER) | (1L << TOUPPER) | (1L << TRIM) | (1L << GEO_LENGTH) | (1L << ROUND) | (1L << FLOOR) | (1L << CEILING))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final CalenderMethodNameContext calenderMethodName() throws RecognitionException {
		CalenderMethodNameContext _localctx = new CalenderMethodNameContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_calenderMethodName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(338);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DAY) | (1L << MONTH) | (1L << YEAR) | (1L << HOUR) | (1L << MINUTE) | (1L << SECOND) | (1L << TIME) | (1L << TOTAL_OFFSET_MINUTES) | (1L << FRACTIONAL_SECONDS) | (1L << TOTAL_SECONDS))) != 0) || _la == DATE)) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_number);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(340);
				_la = _input.LA(1);
				if (!(_la == INTEGER || _la == FLOAT)) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final PrimitiveLiteralContext primitiveLiteral() throws RecognitionException {
		PrimitiveLiteralContext _localctx = new PrimitiveLiteralContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_primitiveLiteral);
		try {
			setState(354);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
				case QUOTED_UUID:
					enterOuterAlt(_localctx, 1);
				{
					setState(342);
					quotedUUID();
				}
				break;
				case QUOTED_STRING:
					enterOuterAlt(_localctx, 2);
				{
					setState(343);
					quotedString();
				}
				break;
				case NULL_SPEC_LITERAL:
					enterOuterAlt(_localctx, 3);
				{
					setState(344);
					nullSpecLiteral();
				}
				break;
				case NULL_TOKEN:
					enterOuterAlt(_localctx, 4);
				{
					setState(345);
					nullToken();
				}
				break;
				case BOOLEAN_VALUE:
					enterOuterAlt(_localctx, 5);
				{
					setState(346);
					booleanValue();
				}
				break;
				case DURATION_VALUE:
					enterOuterAlt(_localctx, 6);
				{
					setState(347);
					durationValue();
				}
				break;
				case DATE_VALUE:
					enterOuterAlt(_localctx, 7);
				{
					setState(348);
					dateValue();
				}
				break;
				case DATE_TIME_OFFSET_VALUE:
					enterOuterAlt(_localctx, 8);
				{
					setState(349);
					dateTimeOffsetValue();
				}
				break;
				case TIME_OF_DAY_VALUE:
					enterOuterAlt(_localctx, 9);
				{
					setState(350);
					timeOfDayValue();
				}
				break;
				case DECIMAL_LITERAL:
					enterOuterAlt(_localctx, 10);
				{
					setState(351);
					decimalLiteral();
				}
				break;
				case FLOAT:
					enterOuterAlt(_localctx, 11);
				{
					setState(352);
					floatValue();
				}
				break;
				case INTEGER:
					enterOuterAlt(_localctx, 12);
				{
					setState(353);
					integerValue();
				}
				break;
				default:
					throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final CountStatementContext countStatement() throws RecognitionException {
		CountStatementContext _localctx = new CountStatementContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_countStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(356);
				match(SLASH);
				setState(357);
				match(COUNT);
				setState(360);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == EQUAL) {
					{
						setState(358);
						match(EQUAL);
						setState(359);
						booleanValue();
					}
				}

			}
		}
		catch (RecognitionException re) {
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(362);
				_la = _input.LA(1);
				if (!(_la == BSM_ID || _la == ID)) {
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

	public final OpenParContext openPar() throws RecognitionException {
		OpenParContext _localctx = new OpenParContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_openPar);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(364);
				match(OPEN);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final CloseParContext closePar() throws RecognitionException {
		CloseParContext _localctx = new CloseParContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_closePar);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(366);
				match(CLOSE);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final LogicalOperatorContext logicalOperator() throws RecognitionException {
		LogicalOperatorContext _localctx = new LogicalOperatorContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_logicalOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(368);
				_la = _input.LA(1);
				if (!(_la == OR || _la == AND)) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final SortOrderContext sortOrder() throws RecognitionException {
		SortOrderContext _localctx = new SortOrderContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_sortOrder);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(370);
				_la = _input.LA(1);
				if (!(_la == DESC || _la == ASC)) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final EntityNameContext entityName() throws RecognitionException {
		EntityNameContext _localctx = new EntityNameContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_entityName);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(372);
				match(ID);
			}
		} catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public final OrderAttributeNameContext orderAttributeName() throws RecognitionException {
		OrderAttributeNameContext _localctx = new OrderAttributeNameContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_orderAttributeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(374);
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

	public final UuidIdValueContext uuidIdValue() throws RecognitionException {
		UuidIdValueContext _localctx = new UuidIdValueContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_uuidIdValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(376);
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

	public final QuotedUUIDContext quotedUUID() throws RecognitionException {
		QuotedUUIDContext _localctx = new QuotedUUIDContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_quotedUUID);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(378);
				match(QUOTED_UUID);
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

	public final QuotedStringContext quotedString() throws RecognitionException {
		QuotedStringContext _localctx = new QuotedStringContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_quotedString);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(380);
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

	public final NullSpecLiteralContext nullSpecLiteral() throws RecognitionException {
		NullSpecLiteralContext _localctx = new NullSpecLiteralContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_nullSpecLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(382);
				match(NULL_SPEC_LITERAL);
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

	public final NullTokenContext nullToken() throws RecognitionException {
		NullTokenContext _localctx = new NullTokenContext(_ctx, getState());
		enterRule(_localctx, 92, RULE_nullToken);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(384);
				match(NULL_TOKEN);
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

	public final BooleanValueContext booleanValue() throws RecognitionException {
		BooleanValueContext _localctx = new BooleanValueContext(_ctx, getState());
		enterRule(_localctx, 94, RULE_booleanValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(386);
				match(BOOLEAN_VALUE);
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

	public final DurationValueContext durationValue() throws RecognitionException {
		DurationValueContext _localctx = new DurationValueContext(_ctx, getState());
		enterRule(_localctx, 96, RULE_durationValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(388);
				match(DURATION_VALUE);
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

	public final DateValueContext dateValue() throws RecognitionException {
		DateValueContext _localctx = new DateValueContext(_ctx, getState());
		enterRule(_localctx, 98, RULE_dateValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(390);
				match(DATE_VALUE);
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

	public final DateTimeOffsetValueContext dateTimeOffsetValue() throws RecognitionException {
		DateTimeOffsetValueContext _localctx = new DateTimeOffsetValueContext(_ctx, getState());
		enterRule(_localctx, 100, RULE_dateTimeOffsetValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(392);
				match(DATE_TIME_OFFSET_VALUE);
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

	public final TimeOfDayValueContext timeOfDayValue() throws RecognitionException {
		TimeOfDayValueContext _localctx = new TimeOfDayValueContext(_ctx, getState());
		enterRule(_localctx, 102, RULE_timeOfDayValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(394);
				match(TIME_OF_DAY_VALUE);
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

	public final DecimalLiteralContext decimalLiteral() throws RecognitionException {
		DecimalLiteralContext _localctx = new DecimalLiteralContext(_ctx, getState());
		enterRule(_localctx, 104, RULE_decimalLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(396);
				match(DECIMAL_LITERAL);
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
		enterRule(_localctx, 106, RULE_floatValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(398);
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

	public final IntegerValueContext integerValue() throws RecognitionException {
		IntegerValueContext _localctx = new IntegerValueContext(_ctx, getState());
		enterRule(_localctx, 108, RULE_integerValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(400);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
			case 10:
				return filterExpression_sempred((FilterExpressionContext) _localctx, predIndex);
		}
		return true;
	}

	public static class OdataRelativeUriContext extends ParserRuleContext {
		public OdataRelativeUriContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public ResourcePathContext resourcePath() {
			return getRuleContext(ResourcePathContext.class,0);
		}

		public TerminalNode QUESTION() { return getToken(ODataParser.QUESTION, 0); }

		public QueryOptionsContext queryOptions() {
			return getRuleContext(QueryOptionsContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_odataRelativeUri; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterOdataRelativeUri(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitOdataRelativeUri(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitOdataRelativeUri(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class ResourcePathContext extends ParserRuleContext {
		public ResourcePathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public EmbeddedEntitySetContext embeddedEntitySet() {
			return getRuleContext(EmbeddedEntitySetContext.class,0);
		}

		public EntityUUIDContext entityUUID() {
			return getRuleContext(EntityUUIDContext.class,0);
		}

		public EntityContext entity() {
			return getRuleContext(EntityContext.class,0);
		}

		public CountStatementContext countStatement() {
			return getRuleContext(CountStatementContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_resourcePath; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterResourcePath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitResourcePath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitResourcePath(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class EntityContext extends ParserRuleContext {
		public EntityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public EntityNameContext entityName() {
			return getRuleContext(EntityNameContext.class,0);
		}

		public EntityCastContext entityCast() {
			return getRuleContext(EntityCastContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_entity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterEntity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitEntity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitEntity(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class EntityCastContext extends ParserRuleContext {
		public EntityCastContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public List<EntityNameContext> entityName() {
			return getRuleContexts(EntityNameContext.class);
		}

		public EntityNameContext entityName(int i) {
			return getRuleContext(EntityNameContext.class,i);
		}

		public TerminalNode SLASH() { return getToken(ODataParser.SLASH, 0); }

		@Override public int getRuleIndex() { return RULE_entityCast; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterEntityCast(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitEntityCast(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitEntityCast(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class EntityUUIDContext extends ParserRuleContext {
		public EntityUUIDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public EntityNameContext entityName() {
			return getRuleContext(EntityNameContext.class,0);
		}

		public TerminalNode SLASH() { return getToken(ODataParser.SLASH, 0); }

		public UuidIdValueContext uuidIdValue() {
			return getRuleContext(UuidIdValueContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_entityUUID; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterEntityUUID(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitEntityUUID(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitEntityUUID(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class EmbeddedEntitySetContext extends ParserRuleContext {
		public EmbeddedEntitySetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public List<EntityUUIDContext> entityUUID() {
			return getRuleContexts(EntityUUIDContext.class);
		}

		public EntityUUIDContext entityUUID(int i) {
			return getRuleContext(EntityUUIDContext.class,i);
		}

		public List<TerminalNode> SLASH() { return getTokens(ODataParser.SLASH); }

		public TerminalNode SLASH(int i) {
			return getToken(ODataParser.SLASH, i);
		}

		public List<EntityCastContext> entityCast() {
			return getRuleContexts(EntityCastContext.class);
		}

		public EntityCastContext entityCast(int i) {
			return getRuleContext(EntityCastContext.class,i);
		}

		public List<EntityContext> entity() {
			return getRuleContexts(EntityContext.class);
		}

		public EntityContext entity(int i) {
			return getRuleContext(EntityContext.class,i);
		}

		@Override public int getRuleIndex() { return RULE_embeddedEntitySet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterEmbeddedEntitySet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitEmbeddedEntitySet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitEmbeddedEntitySet(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class QueryOptionsContext extends ParserRuleContext {
		public QueryOptionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public List<QueryOptionContext> queryOption() {
			return getRuleContexts(QueryOptionContext.class);
		}

		public QueryOptionContext queryOption(int i) {
			return getRuleContext(QueryOptionContext.class,i);
		}

		public List<TerminalNode> AMPERSAND() { return getTokens(ODataParser.AMPERSAND); }

		public TerminalNode AMPERSAND(int i) {
			return getToken(ODataParser.AMPERSAND, i);
		}

		@Override public int getRuleIndex() { return RULE_queryOptions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterQueryOptions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitQueryOptions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitQueryOptions(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class QueryOptionContext extends ParserRuleContext {
		public QueryOptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public FilterContext filter() {
			return getRuleContext(FilterContext.class,0);
		}

		public List<TerminalNode> AMPERSAND() { return getTokens(ODataParser.AMPERSAND); }

		public TerminalNode AMPERSAND(int i) {
			return getToken(ODataParser.AMPERSAND, i);
		}

		public List<ExpandContext> expand() {
			return getRuleContexts(ExpandContext.class);
		}

		public ExpandContext expand(int i) {
			return getRuleContext(ExpandContext.class,i);
		}

		public List<OrderByContext> orderBy() {
			return getRuleContexts(OrderByContext.class);
		}

		public OrderByContext orderBy(int i) {
			return getRuleContext(OrderByContext.class,i);
		}

		public List<SkipStatementContext> skipStatement() {
			return getRuleContexts(SkipStatementContext.class);
		}

		public SkipStatementContext skipStatement(int i) {
			return getRuleContext(SkipStatementContext.class,i);
		}

		public List<TopStatementContext> topStatement() {
			return getRuleContexts(TopStatementContext.class);
		}

		public TopStatementContext topStatement(int i) {
			return getRuleContext(TopStatementContext.class,i);
		}

		@Override public int getRuleIndex() { return RULE_queryOption; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterQueryOption(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitQueryOption(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitQueryOption(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class FilterContext extends ParserRuleContext {
		public FilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode FILTER() { return getToken(ODataParser.FILTER, 0); }

		public TerminalNode EQUAL() { return getToken(ODataParser.EQUAL, 0); }

		public FilterExpressionContext filterExpression() {
			return getRuleContext(FilterExpressionContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_filter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterFilter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitFilter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitFilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class ExpandContext extends ParserRuleContext {
		public ExpandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode EXPAND() { return getToken(ODataParser.EXPAND, 0); }

		public TerminalNode EQUAL() { return getToken(ODataParser.EQUAL, 0); }

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class,0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}

		public BoolCommonExprContext boolCommonExpr() {
			return getRuleContext(BoolCommonExprContext.class,0);
		}

		public TerminalNode SLASH() { return getToken(ODataParser.SLASH, 0); }

		public FilterContext filter() {
			return getRuleContext(FilterContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_expand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterExpand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitExpand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitExpand(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class FilterExpressionContext extends ParserRuleContext {
		public FilterExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		public FilterExpressionContext() { }

		@Override public int getRuleIndex() { return RULE_filterExpression; }

		public void copyFrom(FilterExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}

	public static class BinaryExpressionContext extends FilterExpressionContext {
		public FilterExpressionContext left;
		public LogicalOperatorContext op;
		public FilterExpressionContext right;
		public BinaryExpressionContext(FilterExpressionContext ctx) { copyFrom(ctx); }

		public List<FilterExpressionContext> filterExpression() {
			return getRuleContexts(FilterExpressionContext.class);
		}

		public FilterExpressionContext filterExpression(int i) {
			return getRuleContext(FilterExpressionContext.class,i);
		}

		public LogicalOperatorContext logicalOperator() {
			return getRuleContext(LogicalOperatorContext.class,0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterBinaryExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitBinaryExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitBinaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class BoolExpressionContext extends FilterExpressionContext {
		public BoolExpressionContext(FilterExpressionContext ctx) { copyFrom(ctx); }

		public BoolCommonExprContext boolCommonExpr() {
			return getRuleContext(BoolCommonExprContext.class,0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterBoolExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitBoolExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitBoolExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class ParenExpressionContext extends FilterExpressionContext {
		public ParenExpressionContext(FilterExpressionContext ctx) { copyFrom(ctx); }

		public OpenParContext openPar() {
			return getRuleContext(OpenParContext.class,0);
		}

		public FilterExpressionContext filterExpression() {
			return getRuleContext(FilterExpressionContext.class,0);
		}

		public CloseParContext closePar() {
			return getRuleContext(CloseParContext.class,0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterParenExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitParenExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitParenExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class BoolCommonExprContext extends ParserRuleContext {
		public BoolCommonExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		public BoolCommonExprContext() { }

		@Override public int getRuleIndex() { return RULE_boolCommonExpr; }

		public void copyFrom(BoolCommonExprContext ctx) {
			super.copyFrom(ctx);
		}
	}

	public static class CompareMethodExpressionContext extends BoolCommonExprContext {
		public CompareMethodExpressionContext(BoolCommonExprContext ctx) { copyFrom(ctx); }

		public CompareMethodExprContext compareMethodExpr() {
			return getRuleContext(CompareMethodExprContext.class,0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterCompareMethodExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitCompareMethodExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitCompareMethodExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class ComparatorExpressionContext extends BoolCommonExprContext {
		public LeftComparatorExprContext left;
		public ComparisonOperatorContext op;
		public RightComparatorExprContext right;
		public ComparatorExpressionContext(BoolCommonExprContext ctx) { copyFrom(ctx); }

		public LeftComparatorExprContext leftComparatorExpr() {
			return getRuleContext(LeftComparatorExprContext.class,0);
		}

		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class,0);
		}

		public RightComparatorExprContext rightComparatorExpr() {
			return getRuleContext(RightComparatorExprContext.class,0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterComparatorExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitComparatorExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitComparatorExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class CountComparatorExpressionContext extends BoolCommonExprContext {
		public Token left;
		public ComparisonOperatorContext op;
		public PrimitiveLiteralContext right;
		public CountComparatorExpressionContext(BoolCommonExprContext ctx) { copyFrom(ctx); }

		public TerminalNode COUNT() { return getToken(ODataParser.COUNT, 0); }

		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class,0);
		}

		public PrimitiveLiteralContext primitiveLiteral() {
			return getRuleContext(PrimitiveLiteralContext.class,0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterCountComparatorExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitCountComparatorExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitCountComparatorExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class LeftComparatorExprContext extends ParserRuleContext {
		public LeftComparatorExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public MethodCallExprContext methodCallExpr() {
			return getRuleContext(MethodCallExprContext.class,0);
		}

		public CalenderMethodExpContext calenderMethodExp() {
			return getRuleContext(CalenderMethodExpContext.class,0);
		}

		public ConcatMethodExprContext concatMethodExpr() {
			return getRuleContext(ConcatMethodExprContext.class,0);
		}

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class,0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}

		public CommonExprContext commonExpr() {
			return getRuleContext(CommonExprContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_leftComparatorExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterLeftComparatorExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitLeftComparatorExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitLeftComparatorExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class RightComparatorExprContext extends ParserRuleContext {
		public RightComparatorExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public MethodCallExprContext methodCallExpr() {
			return getRuleContext(MethodCallExprContext.class,0);
		}

		public CalenderMethodExpContext calenderMethodExp() {
			return getRuleContext(CalenderMethodExpContext.class,0);
		}

		public ConcatMethodExprContext concatMethodExpr() {
			return getRuleContext(ConcatMethodExprContext.class,0);
		}

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class,0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}

		public CommonExprContext commonExpr() {
			return getRuleContext(CommonExprContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_rightComparatorExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterRightComparatorExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitRightComparatorExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitRightComparatorExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class OrderByContext extends ParserRuleContext {
		public OrderByContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode ORDERBY() { return getToken(ODataParser.ORDERBY, 0); }

		public TerminalNode EQUAL() { return getToken(ODataParser.EQUAL, 0); }

		public List<OrderByItemContext> orderByItem() {
			return getRuleContexts(OrderByItemContext.class);
		}

		public OrderByItemContext orderByItem(int i) {
			return getRuleContext(OrderByItemContext.class,i);
		}

		public List<TerminalNode> COMMA() { return getTokens(ODataParser.COMMA); }

		public TerminalNode COMMA(int i) {
			return getToken(ODataParser.COMMA, i);
		}

		@Override public int getRuleIndex() { return RULE_orderBy; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterOrderBy(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitOrderBy(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitOrderBy(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class OrderByItemContext extends ParserRuleContext {
		public OrderByItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public OrderAttributeNameContext orderAttributeName() {
			return getRuleContext(OrderAttributeNameContext.class, 0);
		}

		public SortOrderContext sortOrder() {
			return getRuleContext(SortOrderContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_orderByItem;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterOrderByItem(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitOrderByItem(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitOrderByItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class TopStatementContext extends ParserRuleContext {
		public TopStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode TOP() { return getToken(ODataParser.TOP, 0); }

		public IntegerValueContext integerValue() {
			return getRuleContext(IntegerValueContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_topStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterTopStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitTopStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitTopStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class SkipStatementContext extends ParserRuleContext {
		public SkipStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode SKIPRULE() { return getToken(ODataParser.SKIPRULE, 0); }

		public IntegerValueContext integerValue() {
			return getRuleContext(IntegerValueContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_skipStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterSkipStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitSkipStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitSkipStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class JoinEntitiesContext extends ParserRuleContext {
		public JoinEntitiesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}

		public List<EntityNameContext> entityName() {
			return getRuleContexts(EntityNameContext.class);
		}

		public EntityNameContext entityName(int i) {
			return getRuleContext(EntityNameContext.class,i);
		}

		public List<TerminalNode> SLASH() { return getTokens(ODataParser.SLASH); }

		public TerminalNode SLASH(int i) {
			return getToken(ODataParser.SLASH, i);
		}

		@Override public int getRuleIndex() { return RULE_joinEntities; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterJoinEntities(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitJoinEntities(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitJoinEntities(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class CommonExprContext extends ParserRuleContext {
		public CommonExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public PrimitiveLiteralContext primitiveLiteral() {
			return getRuleContext(PrimitiveLiteralContext.class,0);
		}

		public MethodExprContext methodExpr() {
			return getRuleContext(MethodExprContext.class,0);
		}

		public MathExprContext mathExpr() {
			return getRuleContext(MathExprContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_commonExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterCommonExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitCommonExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitCommonExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class MathExprContext extends ParserRuleContext {
		public MathExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}

		public TerminalNode ADD() { return getToken(ODataParser.ADD, 0); }

		public TerminalNode SUB() { return getToken(ODataParser.SUB, 0); }

		public TerminalNode MUL() { return getToken(ODataParser.MUL, 0); }

		public TerminalNode DIV() { return getToken(ODataParser.DIV, 0); }

		public TerminalNode MOD() { return getToken(ODataParser.MOD, 0); }

		@Override public int getRuleIndex() { return RULE_mathExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterMathExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitMathExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitMathExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class MethodExprContext extends ParserRuleContext {
		public MethodExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public MethodCallExprContext methodCallExpr() {
			return getRuleContext(MethodCallExprContext.class,0);
		}

		public CalenderMethodExpContext calenderMethodExp() {
			return getRuleContext(CalenderMethodExpContext.class,0);
		}

		public SingleMethodCallExprContext singleMethodCallExpr() {
			return getRuleContext(SingleMethodCallExprContext.class,0);
		}

		public ConcatMethodExprContext concatMethodExpr() {
			return getRuleContext(ConcatMethodExprContext.class,0);
		}

		public SubstringMethodCallExprContext substringMethodCallExpr() {
			return getRuleContext(SubstringMethodCallExprContext.class,0);
		}

		public CompareMethodExprContext compareMethodExpr() {
			return getRuleContext(CompareMethodExprContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_methodExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterMethodExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitMethodExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitMethodExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class CompareMethodExprContext extends ParserRuleContext {
		public CompareMethodExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public CompareMethodNameContext compareMethodName() {
			return getRuleContext(CompareMethodNameContext.class,0);
		}

		public TerminalNode OPEN() { return getToken(ODataParser.OPEN, 0); }

		public TerminalNode COMMA() { return getToken(ODataParser.COMMA, 0); }

		public List<CommonExprContext> commonExpr() {
			return getRuleContexts(CommonExprContext.class);
		}

		public CommonExprContext commonExpr(int i) {
			return getRuleContext(CommonExprContext.class,i);
		}

		public TerminalNode CLOSE() { return getToken(ODataParser.CLOSE, 0); }

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class,0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_compareMethodExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterCompareMethodExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitCompareMethodExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitCompareMethodExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class MethodCallExprContext extends ParserRuleContext {
		public MethodCallExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public MethodNameContext methodName() {
			return getRuleContext(MethodNameContext.class,0);
		}

		public TerminalNode OPEN() { return getToken(ODataParser.OPEN, 0); }

		public TerminalNode CLOSE() { return getToken(ODataParser.CLOSE, 0); }

		public MethodCallExprContext methodCallExpr() {
			return getRuleContext(MethodCallExprContext.class,0);
		}

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class,0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_methodCallExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterMethodCallExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitMethodCallExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitMethodCallExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class CalenderMethodExpContext extends ParserRuleContext {
		public CalenderMethodExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public CalenderMethodNameContext calenderMethodName() {
			return getRuleContext(CalenderMethodNameContext.class,0);
		}

		public TerminalNode OPEN() { return getToken(ODataParser.OPEN, 0); }

		public TerminalNode CLOSE() { return getToken(ODataParser.CLOSE, 0); }

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class,0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}

		public PrimitiveLiteralContext primitiveLiteral() {
			return getRuleContext(PrimitiveLiteralContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_calenderMethodExp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterCalenderMethodExp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitCalenderMethodExp(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitCalenderMethodExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class ConcatMethodExprContext extends ParserRuleContext {
		public ConcatMethodExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode CONCAT() { return getToken(ODataParser.CONCAT, 0); }

		public TerminalNode OPEN() { return getToken(ODataParser.OPEN, 0); }

		public TerminalNode COMMA() { return getToken(ODataParser.COMMA, 0); }

		public TerminalNode CLOSE() { return getToken(ODataParser.CLOSE, 0); }

		public ConcatMethodExprContext concatMethodExpr() {
			return getRuleContext(ConcatMethodExprContext.class,0);
		}

		public List<PrimitiveLiteralContext> primitiveLiteral() {
			return getRuleContexts(PrimitiveLiteralContext.class);
		}

		public PrimitiveLiteralContext primitiveLiteral(int i) {
			return getRuleContext(PrimitiveLiteralContext.class,i);
		}

		public List<AttributeNameContext> attributeName() {
			return getRuleContexts(AttributeNameContext.class);
		}

		public AttributeNameContext attributeName(int i) {
			return getRuleContext(AttributeNameContext.class,i);
		}

		@Override public int getRuleIndex() { return RULE_concatMethodExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterConcatMethodExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitConcatMethodExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitConcatMethodExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class SingleMethodCallExprContext extends ParserRuleContext {
		public SingleMethodCallExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode OPEN() { return getToken(ODataParser.OPEN, 0); }

		public TerminalNode CLOSE() { return getToken(ODataParser.CLOSE, 0); }

		public TerminalNode MIN_DATE_TIME() { return getToken(ODataParser.MIN_DATE_TIME, 0); }

		public TerminalNode MAX_DATE_TIME() { return getToken(ODataParser.MAX_DATE_TIME, 0); }

		public TerminalNode NOW() { return getToken(ODataParser.NOW, 0); }

		@Override public int getRuleIndex() { return RULE_singleMethodCallExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterSingleMethodCallExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitSingleMethodCallExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitSingleMethodCallExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class SubstringMethodCallExprContext extends ParserRuleContext {
		public SubstringMethodCallExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode SUBSTRING() { return getToken(ODataParser.SUBSTRING, 0); }

		public TerminalNode OPEN() { return getToken(ODataParser.OPEN, 0); }

		public TerminalNode COMMA() { return getToken(ODataParser.COMMA, 0); }

		public List<CommonExprContext> commonExpr() {
			return getRuleContexts(CommonExprContext.class);
		}

		public CommonExprContext commonExpr(int i) {
			return getRuleContext(CommonExprContext.class,i);
		}

		public TerminalNode CLOSE() { return getToken(ODataParser.CLOSE, 0); }

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class,0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class,0);
		}

		@Override public int getRuleIndex() { return RULE_substringMethodCallExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).enterSubstringMethodCallExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitSubstringMethodCallExpr(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitSubstringMethodCallExpr(this);
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

	public static class CompareMethodNameContext extends ParserRuleContext {
		public CompareMethodNameContext(ParserRuleContext parent, int invokingState) {
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

		public TerminalNode INDEXOF() {
			return getToken(ODataParser.INDEXOF, 0);
		}

		public TerminalNode GEO_INTERSECTS() {
			return getToken(ODataParser.GEO_INTERSECTS, 0);
		}

		public TerminalNode GEO_DISTANCE() {
			return getToken(ODataParser.GEO_DISTANCE, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_compareMethodName;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterCompareMethodName(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitCompareMethodName(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitCompareMethodName(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class MethodNameContext extends ParserRuleContext {
		public MethodNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode LENGTH() {
			return getToken(ODataParser.LENGTH, 0);
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

		public TerminalNode ROUND() {
			return getToken(ODataParser.ROUND, 0);
		}

		public TerminalNode FLOOR() {
			return getToken(ODataParser.FLOOR, 0);
		}

		public TerminalNode CEILING() {
			return getToken(ODataParser.CEILING, 0);
		}

		public TerminalNode GEO_LENGTH() {
			return getToken(ODataParser.GEO_LENGTH, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_methodName;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterMethodName(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitMethodName(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitMethodName(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class CalenderMethodNameContext extends ParserRuleContext {
		public CalenderMethodNameContext(ParserRuleContext parent, int invokingState) {
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

		public TerminalNode FRACTIONAL_SECONDS() {
			return getToken(ODataParser.FRACTIONAL_SECONDS, 0);
		}

		public TerminalNode TOTAL_SECONDS() {
			return getToken(ODataParser.TOTAL_SECONDS, 0);
		}

		public TerminalNode DATE() {
			return getToken(ODataParser.DATE, 0);
		}

		public TerminalNode TIME() {
			return getToken(ODataParser.TIME, 0);
		}

		public TerminalNode TOTAL_OFFSET_MINUTES() {
			return getToken(ODataParser.TOTAL_OFFSET_MINUTES, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_calenderMethodName;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterCalenderMethodName(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitCalenderMethodName(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitCalenderMethodName(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class NumberContext extends ParserRuleContext {
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode INTEGER() {
			return getToken(ODataParser.INTEGER, 0);
		}

		public TerminalNode FLOAT() {
			return getToken(ODataParser.FLOAT, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_number;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterNumber(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitNumber(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class PrimitiveLiteralContext extends ParserRuleContext {
		public PrimitiveLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public QuotedUUIDContext quotedUUID() {
			return getRuleContext(QuotedUUIDContext.class, 0);
		}

		public QuotedStringContext quotedString() {
			return getRuleContext(QuotedStringContext.class, 0);
		}

		public NullSpecLiteralContext nullSpecLiteral() {
			return getRuleContext(NullSpecLiteralContext.class, 0);
		}

		public NullTokenContext nullToken() {
			return getRuleContext(NullTokenContext.class, 0);
		}

		public BooleanValueContext booleanValue() {
			return getRuleContext(BooleanValueContext.class, 0);
		}

		public DurationValueContext durationValue() {
			return getRuleContext(DurationValueContext.class, 0);
		}

		public DateValueContext dateValue() {
			return getRuleContext(DateValueContext.class, 0);
		}

		public DateTimeOffsetValueContext dateTimeOffsetValue() {
			return getRuleContext(DateTimeOffsetValueContext.class, 0);
		}

		public TimeOfDayValueContext timeOfDayValue() {
			return getRuleContext(TimeOfDayValueContext.class, 0);
		}

		public DecimalLiteralContext decimalLiteral() {
			return getRuleContext(DecimalLiteralContext.class, 0);
		}

		public FloatValueContext floatValue() {
			return getRuleContext(FloatValueContext.class, 0);
		}

		public IntegerValueContext integerValue() {
			return getRuleContext(IntegerValueContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_primitiveLiteral;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterPrimitiveLiteral(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitPrimitiveLiteral(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitPrimitiveLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class CountStatementContext extends ParserRuleContext {
		public CountStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode SLASH() {
			return getToken(ODataParser.SLASH, 0);
		}

		public TerminalNode COUNT() {
			return getToken(ODataParser.COUNT, 0);
		}

		public TerminalNode EQUAL() {
			return getToken(ODataParser.EQUAL, 0);
		}

		public BooleanValueContext booleanValue() {
			return getRuleContext(BooleanValueContext.class, 0);
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

	public static class AttributeNameContext extends ParserRuleContext {
		public AttributeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode BSM_ID() {
			return getToken(ODataParser.BSM_ID, 0);
		}

		public TerminalNode ID() {
			return getToken(ODataParser.ID, 0);
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

	public static class OpenParContext extends ParserRuleContext {
		public OpenParContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode OPEN() {
			return getToken(ODataParser.OPEN, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_openPar;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterOpenPar(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitOpenPar(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitOpenPar(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class CloseParContext extends ParserRuleContext {
		public CloseParContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode CLOSE() {
			return getToken(ODataParser.CLOSE, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_closePar;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterClosePar(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitClosePar(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitClosePar(this);
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

	public static class SortOrderContext extends ParserRuleContext {
		public SortOrderContext(ParserRuleContext parent, int invokingState) {
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
			return RULE_sortOrder;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterSortOrder(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitSortOrder(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitSortOrder(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class EntityNameContext extends ParserRuleContext {
		public EntityNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode ID() {
			return getToken(ODataParser.ID, 0);
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

	public static class OrderAttributeNameContext extends ParserRuleContext {
		public OrderAttributeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode ID() {
			return getToken(ODataParser.ID, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_orderAttributeName;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterOrderAttributeName(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitOrderAttributeName(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitOrderAttributeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class UuidIdValueContext extends ParserRuleContext {
		public UuidIdValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode UUID() {
			return getToken(ODataParser.UUID, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_uuidIdValue;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterUuidIdValue(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitUuidIdValue(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitUuidIdValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class QuotedUUIDContext extends ParserRuleContext {
		public QuotedUUIDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode QUOTED_UUID() {
			return getToken(ODataParser.QUOTED_UUID, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_quotedUUID;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterQuotedUUID(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitQuotedUUID(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitQuotedUUID(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class QuotedStringContext extends ParserRuleContext {
		public QuotedStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode QUOTED_STRING() {
			return getToken(ODataParser.QUOTED_STRING, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_quotedString;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterQuotedString(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitQuotedString(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitQuotedString(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class NullSpecLiteralContext extends ParserRuleContext {
		public NullSpecLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode NULL_SPEC_LITERAL() {
			return getToken(ODataParser.NULL_SPEC_LITERAL, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_nullSpecLiteral;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterNullSpecLiteral(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitNullSpecLiteral(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitNullSpecLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class NullTokenContext extends ParserRuleContext {
		public NullTokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode NULL_TOKEN() {
			return getToken(ODataParser.NULL_TOKEN, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_nullToken;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterNullToken(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitNullToken(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitNullToken(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class BooleanValueContext extends ParserRuleContext {
		public BooleanValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode BOOLEAN_VALUE() {
			return getToken(ODataParser.BOOLEAN_VALUE, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_booleanValue;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterBooleanValue(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitBooleanValue(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitBooleanValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class DurationValueContext extends ParserRuleContext {
		public DurationValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode DURATION_VALUE() {
			return getToken(ODataParser.DURATION_VALUE, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_durationValue;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterDurationValue(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitDurationValue(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitDurationValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class DateValueContext extends ParserRuleContext {
		public DateValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode DATE_VALUE() {
			return getToken(ODataParser.DATE_VALUE, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_dateValue;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterDateValue(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitDateValue(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitDateValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class DateTimeOffsetValueContext extends ParserRuleContext {
		public DateTimeOffsetValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode DATE_TIME_OFFSET_VALUE() {
			return getToken(ODataParser.DATE_TIME_OFFSET_VALUE, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_dateTimeOffsetValue;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterDateTimeOffsetValue(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitDateTimeOffsetValue(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitDateTimeOffsetValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class TimeOfDayValueContext extends ParserRuleContext {
		public TimeOfDayValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode TIME_OF_DAY_VALUE() {
			return getToken(ODataParser.TIME_OF_DAY_VALUE, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_timeOfDayValue;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterTimeOfDayValue(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitTimeOfDayValue(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitTimeOfDayValue(this);
			else return visitor.visitChildren(this);
		}
	}

	private boolean filterExpression_sempred(FilterExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
			case 0:
				return precpred(_ctx, 2);
		}
		return true;
	}

	public static class DecimalLiteralContext extends ParserRuleContext {
		public DecimalLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode DECIMAL_LITERAL() {
			return getToken(ODataParser.DECIMAL_LITERAL, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_decimalLiteral;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterDecimalLiteral(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitDecimalLiteral(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitDecimalLiteral(this);
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
}
