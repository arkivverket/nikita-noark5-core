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
		SPACE=1, FILTER=2, TOP=3, SKIPRULE=4, SKIPTOKEN=5, ORDERBY=6, REF=7, EXPAND=8,
		COUNT=9, SELECT=10, DOLLARID=11, CONTAINS=12, STARTSWITH=13, ENDSWITH=14,
		SUBSTRINGOF=15, LENGTH=16, INDEXOF=17, REPLACE=18, SUBSTRING=19, TOLOWER=20,
		TOUPPER=21, TRIM=22, CONCAT=23, DAY=24, MONTH=25, YEAR=26, HOUR=27, MINUTE=28,
		SECOND=29, NOW=30, TIME=31, MAX_DATE_TIME=32, MIN_DATE_TIME=33, TOTAL_OFFSET_MINUTES=34,
		FRACTIONAL_SECONDS=35, TOTAL_SECONDS=36, GEO_INTERSECTS=37, GEO_DISTANCE=38,
		GEO_LENGTH=39, ROUND=40, FLOOR=41, CEILING=42, CAST=43, ISOF=44, EQUAL=45,
		EQ=46, GT=47, LT=48, GE=49, LE=50, NE=51, ADD=52, SUB=53, MUL=54, DIV=55,
		MOD=56, ORDER=57, BY=58, DESC=59, ASC=60, OR=61, AND=62, NOT=63, SEPERATOR=64,
		HTTP=65, HTTPS=66, OPEN=67, CLOSE=68, COMMA=69, QUESTION=70, DOLLAR=71,
		SEMI=72, AT_SIGN=73, BAR=74, SINGLE_QUOTE_SYMB=75, DOUBLE_QUOTE_SYMB=76,
		REVERSE_QUOTE_SYMB=77, COLON_SYMB=78, AMPERSAND=79, NULL_TOKEN=80, NULL_SPEC_LITERAL=81,
		DOT=82, SLASH=83, UNDERSCORE=84, EDM=85, COLLECTION=86, GEOGRAPHY=87,
		GEOMETRY=88, BINARY=89, BOOLEAN=90, BYTE=91, DATE=92, DATETIMEOFFSET=93,
		DECIMAL=94, DOUBLE=95, DURATION=96, GUID=97, INT16=98, INT32=99, INT64=100,
		SBYTE=101, SINGLE=102, STREAM=103, STRING=104, TIMEOFDAY=105, LINESTRING=106,
		MULTILINESTRING=107, MULTIPOINT=108, MULTIPOLYGON=109, POINT=110, POLYGON=111,
		BOOLEAN_VALUE=112, UUID=113, INTEGER=114, FLOAT=115, ID=116, QUOTED_STRING=117,
		STRING_LITERAL=118, DECIMAL_LITERAL=119, TIME_OF_DAY_VALUE=120, DURATION_VALUE=121,
		DATE_VALUE=122, DATE_TIME_OFFSET_VALUE=123, HOUR_DEF=124, MINUTE_DEF=125,
		SECOND_DEF=126, ZERO_TO_FIFTY_NINE=127, ONE_TO_NINE=128, YEAR_DEF=129,
		MONTH_DEF=130, DAY_DEF=131, HEX=132, SINGLE_CHAR_SMALL=133, SINGLE_CHAR=134,
		NEGATIVE=135, DEC_OCTECT=136, HEX_NUMBER=137, BIN_NUMBER=138, ERROR_RECONGNIGION=139;
	public static final int
			RULE_odataRelativeUri = 0, RULE_resourcePath = 1, RULE_entity = 2, RULE_entityCast = 3,
			RULE_entityUUID = 4, RULE_embeddedEntitySet = 5, RULE_queryOptions = 6,
			RULE_queryOption = 7, RULE_filter = 8, RULE_expand = 9, RULE_filterExpression = 10,
			RULE_boolCommonExpr = 11, RULE_leftComparatorExpr = 12, RULE_rightComparatorExpr = 13,
			RULE_orderby = 14, RULE_orderbyItem = 15, RULE_topStatement = 16, RULE_skipStatement = 17,
			RULE_joinEntities = 18, RULE_commonExpr = 19, RULE_mathExpr = 20, RULE_methodExpr = 21,
			RULE_compareMethodExpr = 22, RULE_methodCallExpr = 23, RULE_calenderMethodExp = 24,
			RULE_concatMethodExpr = 25, RULE_singleMethodCallExpr = 26, RULE_substringMethodCallExpr = 27,
			RULE_comparisonOperator = 28, RULE_compareMethodName = 29, RULE_methodName = 30,
			RULE_calenderMethodName = 31, RULE_number = 32, RULE_primitiveLiteral = 33,
			RULE_countStatement = 34, RULE_openPar = 35, RULE_closePar = 36, RULE_logicalOperator = 37,
			RULE_entityName = 38, RULE_attributeName = 39, RULE_uuidIdValue = 40,
			RULE_quotedString = 41, RULE_nullSpecLiteral = 42, RULE_nullToken = 43,
			RULE_booleanValue = 44, RULE_durationValue = 45, RULE_dateValue = 46,
			RULE_dateTimeOffsetValue = 47, RULE_timeOfDayValue = 48, RULE_decimalLiteral = 49,
			RULE_floatValue = 50, RULE_integerValue = 51;
	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	public static final String _serializedATN =
			"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u008d\u018b\4\2\t" +
					"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13" +
					"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22" +
					"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31" +
					"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!" +
					"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4" +
					",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t" +
					"\64\4\65\t\65\3\2\3\2\3\2\5\2n\n\2\3\3\3\3\3\3\5\3s\n\3\3\3\5\3v\n\3\3" +
					"\4\3\4\5\4z\n\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3" +
					"\7\5\7\u008a\n\7\7\7\u008c\n\7\f\7\16\7\u008f\13\7\3\b\3\b\3\b\7\b\u0094" +
					"\n\b\f\b\16\b\u0097\13\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\7" +
					"\t\u00a4\n\t\f\t\16\t\u00a7\13\t\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3" +
					"\13\5\13\u00b2\n\13\3\13\3\13\5\13\u00b6\n\13\3\f\3\f\3\f\3\f\3\f\3\f" +
					"\5\f\u00be\n\f\3\f\3\f\3\f\3\f\7\f\u00c4\n\f\f\f\16\f\u00c7\13\f\3\r\3" +
					"\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\r\u00d2\n\r\3\16\3\16\3\16\3\16\3\16" +
					"\3\16\5\16\u00da\n\16\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u00e2\n\17\3" +
					"\20\3\20\3\20\3\20\7\20\u00e8\n\20\f\20\16\20\u00eb\13\20\5\20\u00ed\n" +
					"\20\3\21\3\21\5\21\u00f1\n\21\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24" +
					"\3\24\6\24\u00fc\n\24\r\24\16\24\u00fd\3\24\3\24\3\25\3\25\3\25\5\25\u0105" +
					"\n\25\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\5\27\u0110\n\27\3\30" +
					"\3\30\3\30\3\30\3\30\5\30\u0117\n\30\3\30\3\30\3\30\3\30\3\31\3\31\3\31" +
					"\3\31\3\31\5\31\u0122\n\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\5\32\u012b" +
					"\n\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\5\33\u0134\n\33\3\33\3\33\3\33" +
					"\5\33\u0139\n\33\3\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35" +
					"\5\35\u0146\n\35\3\35\3\35\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3" +
					"\"\3\"\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\3#\5#\u0161\n#\3$\3$\3$\3$\5$\u0167" +
					"\n$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3+\3,\3,\3-\3-\3.\3.\3/\3" +
					"/\3\60\3\60\3\61\3\61\3\62\3\62\3\63\3\63\3\64\3\64\3\65\3\65\3\65\2\3" +
					"\26\66\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<" +
					">@BDFHJLNPRTVXZ\\^`bdfh\2\13\3\2=>\3\2\66:\4\2  \"#\3\2\60\65\5\2\16\20" +
					"\23\23\'(\5\2\22\22\26\30),\6\2\32\37!!$&^^\3\2tu\3\2?@\2\u0196\2j\3\2" +
					"\2\2\4r\3\2\2\2\6y\3\2\2\2\b{\3\2\2\2\n\177\3\2\2\2\f\u0083\3\2\2\2\16" +
					"\u0090\3\2\2\2\20\u0098\3\2\2\2\22\u00a8\3\2\2\2\24\u00ac\3\2\2\2\26\u00bd" +
					"\3\2\2\2\30\u00d1\3\2\2\2\32\u00d9\3\2\2\2\34\u00e1\3\2\2\2\36\u00e3\3" +
					"\2\2\2 \u00ee\3\2\2\2\"\u00f2\3\2\2\2$\u00f5\3\2\2\2&\u00fb\3\2\2\2(\u0104" +
					"\3\2\2\2*\u0106\3\2\2\2,\u010f\3\2\2\2.\u0111\3\2\2\2\60\u011c\3\2\2\2" +
					"\62\u0125\3\2\2\2\64\u012e\3\2\2\2\66\u013c\3\2\2\28\u0140\3\2\2\2:\u014b" +
					"\3\2\2\2<\u014d\3\2\2\2>\u014f\3\2\2\2@\u0151\3\2\2\2B\u0153\3\2\2\2D" +
					"\u0160\3\2\2\2F\u0162\3\2\2\2H\u0168\3\2\2\2J\u016a\3\2\2\2L\u016c\3\2" +
					"\2\2N\u016e\3\2\2\2P\u0170\3\2\2\2R\u0172\3\2\2\2T\u0174\3\2\2\2V\u0176" +
					"\3\2\2\2X\u0178\3\2\2\2Z\u017a\3\2\2\2\\\u017c\3\2\2\2^\u017e\3\2\2\2" +
					"`\u0180\3\2\2\2b\u0182\3\2\2\2d\u0184\3\2\2\2f\u0186\3\2\2\2h\u0188\3" +
					"\2\2\2jm\5\4\3\2kl\7H\2\2ln\5\16\b\2mk\3\2\2\2mn\3\2\2\2n\3\3\2\2\2os" +
					"\5\f\7\2ps\5\n\6\2qs\5\6\4\2ro\3\2\2\2rp\3\2\2\2rq\3\2\2\2su\3\2\2\2t" +
					"v\5F$\2ut\3\2\2\2uv\3\2\2\2v\5\3\2\2\2wz\5N(\2xz\5\b\5\2yw\3\2\2\2yx\3" +
					"\2\2\2z\7\3\2\2\2{|\5N(\2|}\7U\2\2}~\5N(\2~\t\3\2\2\2\177\u0080\5N(\2" +
					"\u0080\u0081\7U\2\2\u0081\u0082\5R*\2\u0082\13\3\2\2\2\u0083\u008d\5\n" +
					"\6\2\u0084\u0085\7U\2\2\u0085\u008c\5\n\6\2\u0086\u0089\7U\2\2\u0087\u008a" +
					"\5\b\5\2\u0088\u008a\5\6\4\2\u0089\u0087\3\2\2\2\u0089\u0088\3\2\2\2\u008a" +
					"\u008c\3\2\2\2\u008b\u0084\3\2\2\2\u008b\u0086\3\2\2\2\u008c\u008f\3\2" +
					"\2\2\u008d\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\r\3\2\2\2\u008f\u008d" +
					"\3\2\2\2\u0090\u0095\5\20\t\2\u0091\u0092\7Q\2\2\u0092\u0094\5\20\t\2" +
					"\u0093\u0091\3\2\2\2\u0094\u0097\3\2\2\2\u0095\u0093\3\2\2\2\u0095\u0096" +
					"\3\2\2\2\u0096\17\3\2\2\2\u0097\u0095\3\2\2\2\u0098\u00a5\5\22\n\2\u0099" +
					"\u009a\7Q\2\2\u009a\u00a4\5\24\13\2\u009b\u009c\7Q\2\2\u009c\u00a4\5\36" +
					"\20\2\u009d\u009e\7Q\2\2\u009e\u00a4\5$\23\2\u009f\u00a0\7Q\2\2\u00a0" +
					"\u00a4\5\"\22\2\u00a1\u00a2\7Q\2\2\u00a2\u00a4\5\"\22\2\u00a3\u0099\3" +
					"\2\2\2\u00a3\u009b\3\2\2\2\u00a3\u009d\3\2\2\2\u00a3\u009f\3\2\2\2\u00a3" +
					"\u00a1\3\2\2\2\u00a4\u00a7\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a5\u00a6\3\2" +
					"\2\2\u00a6\21\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a8\u00a9\7\4\2\2\u00a9\u00aa" +
					"\7/\2\2\u00aa\u00ab\5\26\f\2\u00ab\23\3\2\2\2\u00ac\u00ad\7\n\2\2\u00ad" +
					"\u00b1\7/\2\2\u00ae\u00b2\5&\24\2\u00af\u00b2\5P)\2\u00b0\u00b2\5\30\r" +
					"\2\u00b1\u00ae\3\2\2\2\u00b1\u00af\3\2\2\2\u00b1\u00b0\3\2\2\2\u00b2\u00b5" +
					"\3\2\2\2\u00b3\u00b4\7U\2\2\u00b4\u00b6\5\22\n\2\u00b5\u00b3\3\2\2\2\u00b5" +
					"\u00b6\3\2\2\2\u00b6\25\3\2\2\2\u00b7\u00b8\b\f\1\2\u00b8\u00b9\5H%\2" +
					"\u00b9\u00ba\5\26\f\2\u00ba\u00bb\5J&\2\u00bb\u00be\3\2\2\2\u00bc\u00be" +
					"\5\30\r\2\u00bd\u00b7\3\2\2\2\u00bd\u00bc\3\2\2\2\u00be\u00c5\3\2\2\2" +
					"\u00bf\u00c0\f\4\2\2\u00c0\u00c1\5L\'\2\u00c1\u00c2\5\26\f\5\u00c2\u00c4" +
					"\3\2\2\2\u00c3\u00bf\3\2\2\2\u00c4\u00c7\3\2\2\2\u00c5\u00c3\3\2\2\2\u00c5" +
					"\u00c6\3\2\2\2\u00c6\27\3\2\2\2\u00c7\u00c5\3\2\2\2\u00c8\u00c9\5\32\16" +
					"\2\u00c9\u00ca\5:\36\2\u00ca\u00cb\5\34\17\2\u00cb\u00d2\3\2\2\2\u00cc" +
					"\u00cd\7\13\2\2\u00cd\u00ce\5:\36\2\u00ce\u00cf\5D#\2\u00cf\u00d2\3\2" +
					"\2\2\u00d0\u00d2\5.\30\2\u00d1\u00c8\3\2\2\2\u00d1\u00cc\3\2\2\2\u00d1" +
					"\u00d0\3\2\2\2\u00d2\31\3\2\2\2\u00d3\u00da\5\60\31\2\u00d4\u00da\5\62" +
					"\32\2\u00d5\u00da\5\64\33\2\u00d6\u00da\5&\24\2\u00d7\u00da\5P)\2\u00d8" +
					"\u00da\5(\25\2\u00d9\u00d3\3\2\2\2\u00d9\u00d4\3\2\2\2\u00d9\u00d5\3\2" +
					"\2\2\u00d9\u00d6\3\2\2\2\u00d9\u00d7\3\2\2\2\u00d9\u00d8\3\2\2\2\u00da" +
					"\33\3\2\2\2\u00db\u00e2\5\60\31\2\u00dc\u00e2\5\62\32\2\u00dd\u00e2\5" +
					"\64\33\2\u00de\u00e2\5&\24\2\u00df\u00e2\5P)\2\u00e0\u00e2\5(\25\2\u00e1" +
					"\u00db\3\2\2\2\u00e1\u00dc\3\2\2\2\u00e1\u00dd\3\2\2\2\u00e1\u00de\3\2" +
					"\2\2\u00e1\u00df\3\2\2\2\u00e1\u00e0\3\2\2\2\u00e2\35\3\2\2\2\u00e3\u00ec" +
					"\7\b\2\2\u00e4\u00e9\5 \21\2\u00e5\u00e6\7G\2\2\u00e6\u00e8\5 \21\2\u00e7" +
					"\u00e5\3\2\2\2\u00e8\u00eb\3\2\2\2\u00e9\u00e7\3\2\2\2\u00e9\u00ea\3\2" +
					"\2\2\u00ea\u00ed\3\2\2\2\u00eb\u00e9\3\2\2\2\u00ec\u00e4\3\2\2\2\u00ec" +
					"\u00ed\3\2\2\2\u00ed\37\3\2\2\2\u00ee\u00f0\5(\25\2\u00ef\u00f1\t\2\2" +
					"\2\u00f0\u00ef\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1!\3\2\2\2\u00f2\u00f3" +
					"\7\5\2\2\u00f3\u00f4\5h\65\2\u00f4#\3\2\2\2\u00f5\u00f6\7\6\2\2\u00f6" +
					"\u00f7\5h\65\2\u00f7%\3\2\2\2\u00f8\u00f9\5N(\2\u00f9\u00fa\7U\2\2\u00fa" +
					"\u00fc\3\2\2\2\u00fb\u00f8\3\2\2\2\u00fc\u00fd\3\2\2\2\u00fd\u00fb\3\2" +
					"\2\2\u00fd\u00fe\3\2\2\2\u00fe\u00ff\3\2\2\2\u00ff\u0100\5P)\2\u0100\'" +
					"\3\2\2\2\u0101\u0105\5D#\2\u0102\u0105\5,\27\2\u0103\u0105\5*\26\2\u0104" +
					"\u0101\3\2\2\2\u0104\u0102\3\2\2\2\u0104\u0103\3\2\2\2\u0105)\3\2\2\2" +
					"\u0106\u0107\t\3\2\2\u0107\u0108\5B\"\2\u0108+\3\2\2\2\u0109\u0110\5\60" +
					"\31\2\u010a\u0110\5\62\32\2\u010b\u0110\5\66\34\2\u010c\u0110\5\64\33" +
					"\2\u010d\u0110\58\35\2\u010e\u0110\5.\30\2\u010f\u0109\3\2\2\2\u010f\u010a" +
					"\3\2\2\2\u010f\u010b\3\2\2\2\u010f\u010c\3\2\2\2\u010f\u010d\3\2\2\2\u010f" +
					"\u010e\3\2\2\2\u0110-\3\2\2\2\u0111\u0112\5<\37\2\u0112\u0116\7E\2\2\u0113" +
					"\u0117\5&\24\2\u0114\u0117\5P)\2\u0115\u0117\5(\25\2\u0116\u0113\3\2\2" +
					"\2\u0116\u0114\3\2\2\2\u0116\u0115\3\2\2\2\u0117\u0118\3\2\2\2\u0118\u0119" +
					"\7G\2\2\u0119\u011a\5(\25\2\u011a\u011b\7F\2\2\u011b/\3\2\2\2\u011c\u011d" +
					"\5> \2\u011d\u0121\7E\2\2\u011e\u0122\5\60\31\2\u011f\u0122\5&\24\2\u0120" +
					"\u0122\5P)\2\u0121\u011e\3\2\2\2\u0121\u011f\3\2\2\2\u0121\u0120\3\2\2" +
					"\2\u0122\u0123\3\2\2\2\u0123\u0124\7F\2\2\u0124\61\3\2\2\2\u0125\u0126" +
					"\5@!\2\u0126\u012a\7E\2\2\u0127\u012b\5&\24\2\u0128\u012b\5P)\2\u0129" +
					"\u012b\5D#\2\u012a\u0127\3\2\2\2\u012a\u0128\3\2\2\2\u012a\u0129\3\2\2" +
					"\2\u012b\u012c\3\2\2\2\u012c\u012d\7F\2\2\u012d\63\3\2\2\2\u012e\u012f" +
					"\7\31\2\2\u012f\u0133\7E\2\2\u0130\u0134\5\64\33\2\u0131\u0134\5D#\2\u0132" +
					"\u0134\5P)\2\u0133\u0130\3\2\2\2\u0133\u0131\3\2\2\2\u0133\u0132\3\2\2" +
					"\2\u0134\u0135\3\2\2\2\u0135\u0138\7G\2\2\u0136\u0139\5D#\2\u0137\u0139" +
					"\5P)\2\u0138\u0136\3\2\2\2\u0138\u0137\3\2\2\2\u0139\u013a\3\2\2\2\u013a" +
					"\u013b\7F\2\2\u013b\65\3\2\2\2\u013c\u013d\t\4\2\2\u013d\u013e\7E\2\2" +
					"\u013e\u013f\7F\2\2\u013f\67\3\2\2\2\u0140\u0141\7\25\2\2\u0141\u0145" +
					"\7E\2\2\u0142\u0146\5&\24\2\u0143\u0146\5P)\2\u0144\u0146\5(\25\2\u0145" +
					"\u0142\3\2\2\2\u0145\u0143\3\2\2\2\u0145\u0144\3\2\2\2\u0146\u0147\3\2" +
					"\2\2\u0147\u0148\7G\2\2\u0148\u0149\5(\25\2\u0149\u014a\7F\2\2\u014a9" +
					"\3\2\2\2\u014b\u014c\t\5\2\2\u014c;\3\2\2\2\u014d\u014e\t\6\2\2\u014e" +
					"=\3\2\2\2\u014f\u0150\t\7\2\2\u0150?\3\2\2\2\u0151\u0152\t\b\2\2\u0152" +
					"A\3\2\2\2\u0153\u0154\t\t\2\2\u0154C\3\2\2\2\u0155\u0161\5T+\2\u0156\u0161" +
					"\5V,\2\u0157\u0161\5X-\2\u0158\u0161\5Z.\2\u0159\u0161\5\\/\2\u015a\u0161" +
					"\5^\60\2\u015b\u0161\5`\61\2\u015c\u0161\5b\62\2\u015d\u0161\5d\63\2\u015e" +
					"\u0161\5f\64\2\u015f\u0161\5h\65\2\u0160\u0155\3\2\2\2\u0160\u0156\3\2" +
					"\2\2\u0160\u0157\3\2\2\2\u0160\u0158\3\2\2\2\u0160\u0159\3\2\2\2\u0160" +
					"\u015a\3\2\2\2\u0160\u015b\3\2\2\2\u0160\u015c\3\2\2\2\u0160\u015d\3\2" +
					"\2\2\u0160\u015e\3\2\2\2\u0160\u015f\3\2\2\2\u0161E\3\2\2\2\u0162\u0163" +
					"\7U\2\2\u0163\u0166\7\13\2\2\u0164\u0165\7/\2\2\u0165\u0167\5Z.\2\u0166" +
					"\u0164\3\2\2\2\u0166\u0167\3\2\2\2\u0167G\3\2\2\2\u0168\u0169\7E\2\2\u0169" +
					"I\3\2\2\2\u016a\u016b\7F\2\2\u016bK\3\2\2\2\u016c\u016d\t\n\2\2\u016d" +
					"M\3\2\2\2\u016e\u016f\7v\2\2\u016fO\3\2\2\2\u0170\u0171\7v\2\2\u0171Q" +
					"\3\2\2\2\u0172\u0173\7s\2\2\u0173S\3\2\2\2\u0174\u0175\7w\2\2\u0175U\3" +
					"\2\2\2\u0176\u0177\7S\2\2\u0177W\3\2\2\2\u0178\u0179\7R\2\2\u0179Y\3\2" +
					"\2\2\u017a\u017b\7r\2\2\u017b[\3\2\2\2\u017c\u017d\7{\2\2\u017d]\3\2\2" +
					"\2\u017e\u017f\7|\2\2\u017f_\3\2\2\2\u0180\u0181\7}\2\2\u0181a\3\2\2\2" +
					"\u0182\u0183\7z\2\2\u0183c\3\2\2\2\u0184\u0185\7y\2\2\u0185e\3\2\2\2\u0186" +
					"\u0187\7u\2\2\u0187g\3\2\2\2\u0188\u0189\7t\2\2\u0189i\3\2\2\2!mruy\u0089" +
					"\u008b\u008d\u0095\u00a3\u00a5\u00b1\u00b5\u00bd\u00c5\u00d1\u00d9\u00e1" +
					"\u00e9\u00ec\u00f0\u00fd\u0104\u010f\u0116\u0121\u012a\u0133\u0138\u0145" +
					"\u0160\u0166";
	public static final ATN _ATN =
			new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	protected static final DFA[] _decisionToDFA;
	public static final String[] ruleNames = makeRuleNames();
	protected static final PredictionContextCache _sharedContextCache =
			new PredictionContextCache();
	private static final String[] _LITERAL_NAMES = makeLiteralNames();

	static {
		RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION);
	}

	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
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
	public String getGrammarFileName() { return "ODataParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ODataParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	private static String[] makeRuleNames() {
		return new String[]{
				"odataRelativeUri", "resourcePath", "entity", "entityCast", "entityUUID",
				"embeddedEntitySet", "queryOptions", "queryOption", "filter", "expand",
				"filterExpression", "boolCommonExpr", "leftComparatorExpr", "rightComparatorExpr",
				"orderby", "orderbyItem", "topStatement", "skipStatement", "joinEntities",
				"commonExpr", "mathExpr", "methodExpr", "compareMethodExpr", "methodCallExpr",
				"calenderMethodExp", "concatMethodExpr", "singleMethodCallExpr", "substringMethodCallExpr",
				"comparisonOperator", "compareMethodName", "methodName", "calenderMethodName",
				"number", "primitiveLiteral", "countStatement", "openPar", "closePar",
				"logicalOperator", "entityName", "attributeName", "uuidIdValue", "quotedString",
				"nullSpecLiteral", "nullToken", "booleanValue", "durationValue", "dateValue",
				"dateTimeOffsetValue", "timeOfDayValue", "decimalLiteral", "floatValue",
				"integerValue"
		};
	}

	public final OdataRelativeUriContext odataRelativeUri() throws RecognitionException {
		OdataRelativeUriContext _localctx = new OdataRelativeUriContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_odataRelativeUri);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			resourcePath();
			setState(107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==QUESTION) {
				{
				setState(105);
				match(QUESTION);
				setState(106);
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

	private static String[] makeLiteralNames() {
		return new String[] {
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
			null, null, null, null, null, null, "'-'"
		};
	}

	public final ResourcePathContext resourcePath() throws RecognitionException {
		ResourcePathContext _localctx = new ResourcePathContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_resourcePath);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(109);
				embeddedEntitySet();
				}
				break;
			case 2:
				{
				setState(110);
				entityUUID();
				}
				break;
			case 3:
				{
				setState(111);
				entity();
				}
				break;
			}
			setState(115);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SLASH) {
				{
				setState(114);
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

	private static String[] makeSymbolicNames() {
		return new String[] {
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
			"POLYGON", "BOOLEAN_VALUE", "UUID", "INTEGER", "FLOAT", "ID", "QUOTED_STRING",
			"STRING_LITERAL", "DECIMAL_LITERAL", "TIME_OF_DAY_VALUE", "DURATION_VALUE",
			"DATE_VALUE", "DATE_TIME_OFFSET_VALUE", "HOUR_DEF", "MINUTE_DEF", "SECOND_DEF",
			"ZERO_TO_FIFTY_NINE", "ONE_TO_NINE", "YEAR_DEF", "MONTH_DEF", "DAY_DEF",
			"HEX", "SINGLE_CHAR_SMALL", "SINGLE_CHAR", "NEGATIVE", "DEC_OCTECT",
			"HEX_NUMBER", "BIN_NUMBER", "ERROR_RECONGNIGION"
		};
	}

	public final EntityContext entity() throws RecognitionException {
		EntityContext _localctx = new EntityContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_entity);
		try {
			setState(119);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(117);
				entityName();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(118);
				entityCast();
				}
				break;
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

	public final EntityCastContext entityCast() throws RecognitionException {
		EntityCastContext _localctx = new EntityCastContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_entityCast);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(121);
				entityName();
				setState(122);
				match(SLASH);
				setState(123);
				entityName();
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

	public final EntityUUIDContext entityUUID() throws RecognitionException {
		EntityUUIDContext _localctx = new EntityUUIDContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_entityUUID);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(125);
				entityName();
				setState(126);
				match(SLASH);
				setState(127);
				uuidIdValue();
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

	public final EmbeddedEntitySetContext embeddedEntitySet() throws RecognitionException {
		EmbeddedEntitySetContext _localctx = new EmbeddedEntitySetContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_embeddedEntitySet);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(129);
				entityUUID();
			setState(139);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(137);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						setState(130);
						match(SLASH);
						setState(131);
						entityUUID();
						}
						break;
					case 2:
						{
						{
						setState(132);
						match(SLASH);
						setState(135);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
						case 1:
							{
							setState(133);
							entityCast();
							}
							break;
						case 2:
							{
							setState(134);
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
				setState(141);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
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

	public final QueryOptionsContext queryOptions() throws RecognitionException {
		QueryOptionsContext _localctx = new QueryOptionsContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_queryOptions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(142);
				queryOption();
				setState(147);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == AMPERSAND) {
					{
						{
							setState(143);
							match(AMPERSAND);
							setState(144);
							queryOption();
						}
					}
					setState(149);
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
				setState(150);
				filter();
				setState(163);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input, 9, _ctx);
				while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
					if (_alt == 1) {
						{
							setState(161);
							_errHandler.sync(this);
							switch (getInterpreter().adaptivePredict(_input, 8, _ctx)) {
								case 1: {
									setState(151);
									match(AMPERSAND);
									setState(152);
									expand();
								}
								break;
					case 2:
						{
						setState(153);
						match(AMPERSAND);
						setState(154);
						orderby();
						}
						break;
					case 3:
						{
						setState(155);
						match(AMPERSAND);
						setState(156);
						skipStatement();
						}
					break;
								case 4: {
									setState(157);
									match(AMPERSAND);
									setState(158);
									topStatement();
								}
								break;
								case 5: {
									setState(159);
									match(AMPERSAND);
									setState(160);
									topStatement();
								}
								break;
							}
						}
					}
					setState(165);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 9, _ctx);
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

	public final FilterContext filter() throws RecognitionException {
		FilterContext _localctx = new FilterContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_filter);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(166);
				match(FILTER);
				setState(167);
				match(EQUAL);
				setState(168);
				filterExpression(0);
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

	public final ExpandContext expand() throws RecognitionException {
		ExpandContext _localctx = new ExpandContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_expand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(170);
				match(EXPAND);
				setState(171);
				match(EQUAL);
				setState(175);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 10, _ctx)) {
					case 1: {
						setState(172);
						joinEntities();
					}
					break;
					case 2: {
						setState(173);
						attributeName();
					}
					break;
					case 3: {
						setState(174);
						boolCommonExpr();
					}
					break;
				}
				setState(179);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == SLASH) {
					{
						setState(177);
						match(SLASH);
						setState(178);
						filter();
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
				setState(187);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
					case OPEN: {
						_localctx = new ParenExpressionContext(_localctx);
						_ctx = _localctx;
						_prevctx = _localctx;

						setState(182);
						openPar();
						setState(183);
						filterExpression(0);
						setState(184);
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
			case ID:
			case QUOTED_STRING:
			case DECIMAL_LITERAL:
			case TIME_OF_DAY_VALUE:
			case DURATION_VALUE:
			case DATE_VALUE:
			case DATE_TIME_OFFSET_VALUE: {
				_localctx = new BoolExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(186);
				boolCommonExpr();
			}
			break;
					default:
						throw new NoViableAltException(this);
				}
				_ctx.stop = _input.LT(-1);
				setState(195);
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
								setState(189);
								if (!(precpred(_ctx, 2)))
									throw new FailedPredicateException(this, "precpred(_ctx, 2)");
								setState(190);
								((BinaryExpressionContext) _localctx).op = logicalOperator();
								setState(191);
								((BinaryExpressionContext) _localctx).right = filterExpression(3);
							}
						}
					}
					setState(197);
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
			setState(207);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 14, _ctx)) {
				case 1:
					_localctx = new ComparatorExpressionContext(_localctx);
					enterOuterAlt(_localctx, 1);
				{
					setState(198);
					((ComparatorExpressionContext) _localctx).left = leftComparatorExpr();
					setState(199);
					((ComparatorExpressionContext) _localctx).op = comparisonOperator();
					setState(200);
					((ComparatorExpressionContext) _localctx).right = rightComparatorExpr();
				}
				break;
			case 2:
				_localctx = new CountComparatorExpressionContext(_localctx);
				enterOuterAlt(_localctx, 2);
			{
				setState(202);
				((CountComparatorExpressionContext) _localctx).left = match(COUNT);
				setState(203);
				((CountComparatorExpressionContext) _localctx).op = comparisonOperator();
				setState(204);
				((CountComparatorExpressionContext) _localctx).right = primitiveLiteral();
				}
				break;
			case 3:
				_localctx = new CompareMethodExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
			{
				setState(206);
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
			setState(215);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 15, _ctx)) {
				case 1:
					enterOuterAlt(_localctx, 1);
				{
					setState(209);
					methodCallExpr();
				}
				break;
				case 2:
				enterOuterAlt(_localctx, 2);
				{
					setState(210);
					calenderMethodExp();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
			{
				setState(211);
				concatMethodExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
			{
				setState(212);
				joinEntities();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
			{
				setState(213);
				attributeName();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
			{
				setState(214);
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
			setState(223);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 16, _ctx)) {
				case 1:
					enterOuterAlt(_localctx, 1);
				{
					setState(217);
					methodCallExpr();
				}
				break;
				case 2:
				enterOuterAlt(_localctx, 2);
				{
					setState(218);
					calenderMethodExp();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
			{
				setState(219);
				concatMethodExpr();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
			{
				setState(220);
				joinEntities();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
			{
				setState(221);
				attributeName();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
			{
				setState(222);
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

	public final OrderbyContext orderby() throws RecognitionException {
		OrderbyContext _localctx = new OrderbyContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_orderby);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(225);
				match(ORDERBY);
				setState(234);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << CONTAINS) | (1L << STARTSWITH) | (1L << ENDSWITH) | (1L << LENGTH) | (1L << INDEXOF) | (1L << SUBSTRING) | (1L << TOLOWER) | (1L << TOUPPER) | (1L << TRIM) | (1L << CONCAT) | (1L << DAY) | (1L << MONTH) | (1L << YEAR) | (1L << HOUR) | (1L << MINUTE) | (1L << SECOND) | (1L << NOW) | (1L << TIME) | (1L << MAX_DATE_TIME) | (1L << MIN_DATE_TIME) | (1L << TOTAL_OFFSET_MINUTES) | (1L << FRACTIONAL_SECONDS) | (1L << TOTAL_SECONDS) | (1L << GEO_INTERSECTS) | (1L << GEO_DISTANCE) | (1L << GEO_LENGTH) | (1L << ROUND) | (1L << FLOOR) | (1L << CEILING) | (1L << ADD) | (1L << SUB) | (1L << MUL) | (1L << DIV) | (1L << MOD))) != 0) || ((((_la - 80)) & ~0x3f) == 0 && ((1L << (_la - 80)) & ((1L << (NULL_TOKEN - 80)) | (1L << (NULL_SPEC_LITERAL - 80)) | (1L << (DATE - 80)) | (1L << (BOOLEAN_VALUE - 80)) | (1L << (INTEGER - 80)) | (1L << (FLOAT - 80)) | (1L << (QUOTED_STRING - 80)) | (1L << (DECIMAL_LITERAL - 80)) | (1L << (TIME_OF_DAY_VALUE - 80)) | (1L << (DURATION_VALUE - 80)) | (1L << (DATE_VALUE - 80)) | (1L << (DATE_TIME_OFFSET_VALUE - 80)))) != 0)) {
					{
						setState(226);
						orderbyItem();
						setState(231);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la == COMMA) {
							{
								{
									setState(227);
									match(COMMA);
									setState(228);
									orderbyItem();
								}
							}
							setState(233);
							_errHandler.sync(this);
							_la = _input.LA(1);
				}
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

	public final OrderbyItemContext orderbyItem() throws RecognitionException {
		OrderbyItemContext _localctx = new OrderbyItemContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_orderbyItem);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(236);
				commonExpr();
				setState(238);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == DESC || _la == ASC) {
					{
						setState(237);
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
				setState(240);
				match(TOP);
				setState(241);
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
				setState(243);
				match(SKIPRULE);
				setState(244);
				integerValue();
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

	public final JoinEntitiesContext joinEntities() throws RecognitionException {
		JoinEntitiesContext _localctx = new JoinEntitiesContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_joinEntities);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
				setState(249);
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
						case 1: {
							{
								setState(246);
								entityName();
								setState(247);
								match(SLASH);
							}
						}
						break;
						default:
							throw new NoViableAltException(this);
					}
					setState(251);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input, 20, _ctx);
				} while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
				setState(253);
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

	public final CommonExprContext commonExpr() throws RecognitionException {
		CommonExprContext _localctx = new CommonExprContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_commonExpr);
		try {
			setState(258);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
				case NULL_TOKEN:
				case NULL_SPEC_LITERAL:
				case BOOLEAN_VALUE:
				case INTEGER:
				case FLOAT:
				case QUOTED_STRING:
				case DECIMAL_LITERAL:
				case TIME_OF_DAY_VALUE:
			case DURATION_VALUE:
			case DATE_VALUE:
			case DATE_TIME_OFFSET_VALUE:
				enterOuterAlt(_localctx, 1);
			{
				setState(255);
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
				setState(256);
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
				setState(257);
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
				setState(260);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ADD) | (1L << SUB) | (1L << MUL) | (1L << DIV) | (1L << MOD))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(261);
				number();
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

	public final MethodExprContext methodExpr() throws RecognitionException {
		MethodExprContext _localctx = new MethodExprContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_methodExpr);
		try {
			setState(269);
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
					setState(263);
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
				setState(264);
				calenderMethodExp();
				}
				break;
			case NOW:
			case MAX_DATE_TIME:
			case MIN_DATE_TIME:
				enterOuterAlt(_localctx, 3);
			{
				setState(265);
				singleMethodCallExpr();
				}
				break;
			case CONCAT:
				enterOuterAlt(_localctx, 4);
			{
				setState(266);
				concatMethodExpr();
				}
				break;
			case SUBSTRING:
				enterOuterAlt(_localctx, 5);
			{
				setState(267);
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
				setState(268);
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
				setState(271);
				compareMethodName();
				setState(272);
				match(OPEN);
				setState(276);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 23, _ctx)) {
					case 1: {
						setState(273);
						joinEntities();
					}
					break;
					case 2: {
						setState(274);
						attributeName();
					}
					break;
					case 3: {
						setState(275);
						commonExpr();
					}
					break;
				}
				setState(278);
				match(COMMA);
				setState(279);
				commonExpr();
				setState(280);
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
				setState(282);
				methodName();
				setState(283);
				match(OPEN);
				setState(287);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 24, _ctx)) {
					case 1: {
						setState(284);
						methodCallExpr();
					}
					break;
					case 2: {
						setState(285);
						joinEntities();
					}
					break;
					case 3: {
						setState(286);
						attributeName();
					}
					break;
				}
				setState(289);
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
				setState(291);
				calenderMethodName();
				setState(292);
				match(OPEN);
				setState(296);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 25, _ctx)) {
					case 1: {
						setState(293);
						joinEntities();
					}
					break;
					case 2: {
						setState(294);
						attributeName();
					}
					break;
					case 3: {
						setState(295);
						primitiveLiteral();
					}
					break;
				}
				setState(298);
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
				setState(300);
				match(CONCAT);
				setState(301);
				match(OPEN);
				setState(305);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
					case CONCAT: {
						setState(302);
						concatMethodExpr();
					}
					break;
					case NULL_TOKEN:
					case NULL_SPEC_LITERAL:
			case BOOLEAN_VALUE:
			case INTEGER:
			case FLOAT:
			case QUOTED_STRING:
			case DECIMAL_LITERAL:
			case TIME_OF_DAY_VALUE:
			case DURATION_VALUE:
			case DATE_VALUE:
			case DATE_TIME_OFFSET_VALUE: {
				setState(303);
				primitiveLiteral();
			}
			break;
					case ID: {
						setState(304);
						attributeName();
					}
					break;
					default:
						throw new NoViableAltException(this);
				}
				setState(307);
				match(COMMA);
				setState(310);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
					case NULL_TOKEN:
					case NULL_SPEC_LITERAL:
					case BOOLEAN_VALUE:
					case INTEGER:
					case FLOAT:
					case QUOTED_STRING:
					case DECIMAL_LITERAL:
					case TIME_OF_DAY_VALUE:
			case DURATION_VALUE:
			case DATE_VALUE:
			case DATE_TIME_OFFSET_VALUE: {
				setState(308);
				primitiveLiteral();
			}
			break;
					case ID: {
						setState(309);
						attributeName();
					}
					break;
					default:
						throw new NoViableAltException(this);
				}
				setState(312);
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
				setState(314);
				_la = _input.LA(1);
				if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NOW) | (1L << MAX_DATE_TIME) | (1L << MIN_DATE_TIME))) != 0))) {
					_errHandler.recoverInline(this);
				} else {
					if (_input.LA(1) == Token.EOF) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(315);
				match(OPEN);
				setState(316);
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
				setState(318);
				match(SUBSTRING);
				setState(319);
				match(OPEN);
				setState(323);
				_errHandler.sync(this);
				switch (getInterpreter().adaptivePredict(_input, 28, _ctx)) {
					case 1: {
						setState(320);
						joinEntities();
					}
					break;
					case 2: {
						setState(321);
						attributeName();
					}
					break;
					case 3: {
						setState(322);
						commonExpr();
					}
					break;
				}
				setState(325);
				match(COMMA);
				setState(326);
				commonExpr();
				setState(327);
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
				setState(329);
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
				setState(331);
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
				setState(333);
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
				setState(335);
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
				setState(337);
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
			setState(350);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
				case QUOTED_STRING:
					enterOuterAlt(_localctx, 1);
				{
					setState(339);
					quotedString();
				}
				break;
				case NULL_SPEC_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
					setState(340);
					nullSpecLiteral();
				}
				break;
			case NULL_TOKEN:
				enterOuterAlt(_localctx, 3);
			{
				setState(341);
				nullToken();
				}
				break;
			case BOOLEAN_VALUE:
				enterOuterAlt(_localctx, 4);
			{
				setState(342);
				booleanValue();
				}
				break;
			case DURATION_VALUE:
				enterOuterAlt(_localctx, 5);
			{
				setState(343);
				durationValue();
				}
				break;
			case DATE_VALUE:
				enterOuterAlt(_localctx, 6);
			{
				setState(344);
				dateValue();
				}
				break;
			case DATE_TIME_OFFSET_VALUE:
				enterOuterAlt(_localctx, 7);
			{
				setState(345);
				dateTimeOffsetValue();
				}
				break;
			case TIME_OF_DAY_VALUE:
				enterOuterAlt(_localctx, 8);
			{
				setState(346);
				timeOfDayValue();
				}
				break;
			case DECIMAL_LITERAL:
				enterOuterAlt(_localctx, 9);
			{
				setState(347);
				decimalLiteral();
				}
				break;
			case FLOAT:
				enterOuterAlt(_localctx, 10);
			{
				setState(348);
				floatValue();
				}
				break;
			case INTEGER:
				enterOuterAlt(_localctx, 11);
			{
				setState(349);
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
		} finally {
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
				setState(352);
				match(SLASH);
				setState(353);
				match(COUNT);
				setState(356);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == EQUAL) {
					{
						setState(354);
						match(EQUAL);
						setState(355);
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

	public final OpenParContext openPar() throws RecognitionException {
		OpenParContext _localctx = new OpenParContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_openPar);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(358);
				match(OPEN);
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

	public final CloseParContext closePar() throws RecognitionException {
		CloseParContext _localctx = new CloseParContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_closePar);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(360);
				match(CLOSE);
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
		enterRule(_localctx, 74, RULE_logicalOperator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(362);
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
		} finally {
			exitRule();
		}
		return _localctx;
	}

	public final EntityNameContext entityName() throws RecognitionException {
		EntityNameContext _localctx = new EntityNameContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_entityName);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(364);
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

	public final AttributeNameContext attributeName() throws RecognitionException {
		AttributeNameContext _localctx = new AttributeNameContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_attributeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(366);
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
		enterRule(_localctx, 80, RULE_uuidIdValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(368);
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

	public final QuotedStringContext quotedString() throws RecognitionException {
		QuotedStringContext _localctx = new QuotedStringContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_quotedString);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(370);
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
		enterRule(_localctx, 84, RULE_nullSpecLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(372);
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
		enterRule(_localctx, 86, RULE_nullToken);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(374);
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
		enterRule(_localctx, 88, RULE_booleanValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(376);
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
		enterRule(_localctx, 90, RULE_durationValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(378);
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
		enterRule(_localctx, 92, RULE_dateValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(380);
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
		enterRule(_localctx, 94, RULE_dateTimeOffsetValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(382);
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
		enterRule(_localctx, 96, RULE_timeOfDayValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(384);
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
		enterRule(_localctx, 98, RULE_decimalLiteral);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(386);
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
		enterRule(_localctx, 100, RULE_floatValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(388);
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
		enterRule(_localctx, 102, RULE_integerValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(390);
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

	private boolean filterExpression_sempred(FilterExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
			case 0:
				return precpred(_ctx, 2);
		}
		return true;
	}

	public static class OdataRelativeUriContext extends ParserRuleContext {
		public OdataRelativeUriContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public ResourcePathContext resourcePath() {
			return getRuleContext(ResourcePathContext.class, 0);
		}

		public TerminalNode QUESTION() {
			return getToken(ODataParser.QUESTION, 0);
		}

		public QueryOptionsContext queryOptions() {
			return getRuleContext(QueryOptionsContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_odataRelativeUri;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterOdataRelativeUri(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitOdataRelativeUri(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitOdataRelativeUri(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class ResourcePathContext extends ParserRuleContext {
		public ResourcePathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public EmbeddedEntitySetContext embeddedEntitySet() {
			return getRuleContext(EmbeddedEntitySetContext.class, 0);
		}

		public EntityUUIDContext entityUUID() {
			return getRuleContext(EntityUUIDContext.class, 0);
		}

		public EntityContext entity() {
			return getRuleContext(EntityContext.class, 0);
		}

		public CountStatementContext countStatement() {
			return getRuleContext(CountStatementContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_resourcePath;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterResourcePath(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitResourcePath(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitResourcePath(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class EntityContext extends ParserRuleContext {
		public EntityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public EntityNameContext entityName() {
			return getRuleContext(EntityNameContext.class, 0);
		}

		public EntityCastContext entityCast() {
			return getRuleContext(EntityCastContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_entity;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterEntity(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitEntity(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitEntity(this);
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
			return getRuleContext(EntityNameContext.class, i);
		}

		public TerminalNode SLASH() {
			return getToken(ODataParser.SLASH, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_entityCast;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterEntityCast(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitEntityCast(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitEntityCast(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class EntityUUIDContext extends ParserRuleContext {
		public EntityUUIDContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public EntityNameContext entityName() {
			return getRuleContext(EntityNameContext.class, 0);
		}

		public TerminalNode SLASH() {
			return getToken(ODataParser.SLASH, 0);
		}

		public UuidIdValueContext uuidIdValue() {
			return getRuleContext(UuidIdValueContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_entityUUID;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterEntityUUID(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitEntityUUID(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitEntityUUID(this);
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
			return getRuleContext(EntityUUIDContext.class, i);
		}

		public List<TerminalNode> SLASH() {
			return getTokens(ODataParser.SLASH);
		}

		public TerminalNode SLASH(int i) {
			return getToken(ODataParser.SLASH, i);
		}

		public List<EntityCastContext> entityCast() {
			return getRuleContexts(EntityCastContext.class);
		}

		public EntityCastContext entityCast(int i) {
			return getRuleContext(EntityCastContext.class, i);
		}

		public List<EntityContext> entity() {
			return getRuleContexts(EntityContext.class);
		}

		public EntityContext entity(int i) {
			return getRuleContext(EntityContext.class, i);
		}

		@Override
		public int getRuleIndex() {
			return RULE_embeddedEntitySet;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterEmbeddedEntitySet(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitEmbeddedEntitySet(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitEmbeddedEntitySet(this);
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
			return getRuleContext(QueryOptionContext.class, i);
		}

		public List<TerminalNode> AMPERSAND() {
			return getTokens(ODataParser.AMPERSAND);
		}

		public TerminalNode AMPERSAND(int i) {
			return getToken(ODataParser.AMPERSAND, i);
		}

		@Override
		public int getRuleIndex() {
			return RULE_queryOptions;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterQueryOptions(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitQueryOptions(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitQueryOptions(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class QueryOptionContext extends ParserRuleContext {
		public QueryOptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public FilterContext filter() {
			return getRuleContext(FilterContext.class, 0);
		}

		public List<TerminalNode> AMPERSAND() {
			return getTokens(ODataParser.AMPERSAND);
		}

		public TerminalNode AMPERSAND(int i) {
			return getToken(ODataParser.AMPERSAND, i);
		}

		public List<ExpandContext> expand() {
			return getRuleContexts(ExpandContext.class);
		}

		public ExpandContext expand(int i) {
			return getRuleContext(ExpandContext.class, i);
		}

		public List<OrderbyContext> orderby() {
			return getRuleContexts(OrderbyContext.class);
		}

		public OrderbyContext orderby(int i) {
			return getRuleContext(OrderbyContext.class, i);
		}

		public List<SkipStatementContext> skipStatement() {
			return getRuleContexts(SkipStatementContext.class);
		}

		public SkipStatementContext skipStatement(int i) {
			return getRuleContext(SkipStatementContext.class, i);
		}

		public List<TopStatementContext> topStatement() {
			return getRuleContexts(TopStatementContext.class);
		}

		public TopStatementContext topStatement(int i) {
			return getRuleContext(TopStatementContext.class, i);
		}

		@Override
		public int getRuleIndex() {
			return RULE_queryOption;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterQueryOption(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitQueryOption(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitQueryOption(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class FilterContext extends ParserRuleContext {
		public FilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode FILTER() {
			return getToken(ODataParser.FILTER, 0);
		}

		public TerminalNode EQUAL() {
			return getToken(ODataParser.EQUAL, 0);
		}

		public FilterExpressionContext filterExpression() {
			return getRuleContext(FilterExpressionContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_filter;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterFilter(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitFilter(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitFilter(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class ExpandContext extends ParserRuleContext {
		public ExpandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode EXPAND() {
			return getToken(ODataParser.EXPAND, 0);
		}

		public TerminalNode EQUAL() {
			return getToken(ODataParser.EQUAL, 0);
		}

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class, 0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class, 0);
		}

		public BoolCommonExprContext boolCommonExpr() {
			return getRuleContext(BoolCommonExprContext.class, 0);
		}

		public TerminalNode SLASH() {
			return getToken(ODataParser.SLASH, 0);
		}

		public FilterContext filter() {
			return getRuleContext(FilterContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_expand;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterExpand(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitExpand(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitExpand(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class FilterExpressionContext extends ParserRuleContext {
		public FilterExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public FilterExpressionContext() {
		}

		@Override
		public int getRuleIndex() {
			return RULE_filterExpression;
		}

		public void copyFrom(FilterExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}

	public static class BinaryExpressionContext extends FilterExpressionContext {
		public FilterExpressionContext left;
		public LogicalOperatorContext op;
		public FilterExpressionContext right;

		public BinaryExpressionContext(FilterExpressionContext ctx) {
			copyFrom(ctx);
		}

		public List<FilterExpressionContext> filterExpression() {
			return getRuleContexts(FilterExpressionContext.class);
		}

		public FilterExpressionContext filterExpression(int i) {
			return getRuleContext(FilterExpressionContext.class, i);
		}

		public LogicalOperatorContext logicalOperator() {
			return getRuleContext(LogicalOperatorContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterBinaryExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitBinaryExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitBinaryExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class BoolExpressionContext extends FilterExpressionContext {
		public BoolExpressionContext(FilterExpressionContext ctx) {
			copyFrom(ctx);
		}

		public BoolCommonExprContext boolCommonExpr() {
			return getRuleContext(BoolCommonExprContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterBoolExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitBoolExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitBoolExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class ParenExpressionContext extends FilterExpressionContext {
		public ParenExpressionContext(FilterExpressionContext ctx) {
			copyFrom(ctx);
		}

		public OpenParContext openPar() {
			return getRuleContext(OpenParContext.class, 0);
		}

		public FilterExpressionContext filterExpression() {
			return getRuleContext(FilterExpressionContext.class, 0);
		}

		public CloseParContext closePar() {
			return getRuleContext(CloseParContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterParenExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitParenExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitParenExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class BoolCommonExprContext extends ParserRuleContext {
		public BoolCommonExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public BoolCommonExprContext() {
		}

		@Override
		public int getRuleIndex() {
			return RULE_boolCommonExpr;
		}

		public void copyFrom(BoolCommonExprContext ctx) {
			super.copyFrom(ctx);
		}
	}

	public static class CompareMethodExpressionContext extends BoolCommonExprContext {
		public CompareMethodExpressionContext(BoolCommonExprContext ctx) {
			copyFrom(ctx);
		}

		public CompareMethodExprContext compareMethodExpr() {
			return getRuleContext(CompareMethodExprContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterCompareMethodExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitCompareMethodExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitCompareMethodExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class ComparatorExpressionContext extends BoolCommonExprContext {
		public LeftComparatorExprContext left;
		public ComparisonOperatorContext op;
		public RightComparatorExprContext right;

		public ComparatorExpressionContext(BoolCommonExprContext ctx) {
			copyFrom(ctx);
		}

		public LeftComparatorExprContext leftComparatorExpr() {
			return getRuleContext(LeftComparatorExprContext.class, 0);
		}

		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class, 0);
		}

		public RightComparatorExprContext rightComparatorExpr() {
			return getRuleContext(RightComparatorExprContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterComparatorExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitComparatorExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitComparatorExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class CountComparatorExpressionContext extends BoolCommonExprContext {
		public Token left;
		public ComparisonOperatorContext op;
		public PrimitiveLiteralContext right;

		public CountComparatorExpressionContext(BoolCommonExprContext ctx) {
			copyFrom(ctx);
		}

		public TerminalNode COUNT() {
			return getToken(ODataParser.COUNT, 0);
		}

		public ComparisonOperatorContext comparisonOperator() {
			return getRuleContext(ComparisonOperatorContext.class, 0);
		}

		public PrimitiveLiteralContext primitiveLiteral() {
			return getRuleContext(PrimitiveLiteralContext.class, 0);
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterCountComparatorExpression(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitCountComparatorExpression(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitCountComparatorExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class LeftComparatorExprContext extends ParserRuleContext {
		public LeftComparatorExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public MethodCallExprContext methodCallExpr() {
			return getRuleContext(MethodCallExprContext.class, 0);
		}

		public CalenderMethodExpContext calenderMethodExp() {
			return getRuleContext(CalenderMethodExpContext.class, 0);
		}

		public ConcatMethodExprContext concatMethodExpr() {
			return getRuleContext(ConcatMethodExprContext.class, 0);
		}

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class, 0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class, 0);
		}

		public CommonExprContext commonExpr() {
			return getRuleContext(CommonExprContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_leftComparatorExpr;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterLeftComparatorExpr(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitLeftComparatorExpr(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitLeftComparatorExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class RightComparatorExprContext extends ParserRuleContext {
		public RightComparatorExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public MethodCallExprContext methodCallExpr() {
			return getRuleContext(MethodCallExprContext.class, 0);
		}

		public CalenderMethodExpContext calenderMethodExp() {
			return getRuleContext(CalenderMethodExpContext.class, 0);
		}

		public ConcatMethodExprContext concatMethodExpr() {
			return getRuleContext(ConcatMethodExprContext.class, 0);
		}

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class, 0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class, 0);
		}

		public CommonExprContext commonExpr() {
			return getRuleContext(CommonExprContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_rightComparatorExpr;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterRightComparatorExpr(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitRightComparatorExpr(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitRightComparatorExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class OrderbyContext extends ParserRuleContext {
		public OrderbyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode ORDERBY() {
			return getToken(ODataParser.ORDERBY, 0);
		}

		public List<OrderbyItemContext> orderbyItem() {
			return getRuleContexts(OrderbyItemContext.class);
		}

		public OrderbyItemContext orderbyItem(int i) {
			return getRuleContext(OrderbyItemContext.class, i);
		}

		public List<TerminalNode> COMMA() {
			return getTokens(ODataParser.COMMA);
		}

		public TerminalNode COMMA(int i) {
			return getToken(ODataParser.COMMA, i);
		}

		@Override
		public int getRuleIndex() {
			return RULE_orderby;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterOrderby(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitOrderby(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitOrderby(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class OrderbyItemContext extends ParserRuleContext {
		public OrderbyItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public CommonExprContext commonExpr() {
			return getRuleContext(CommonExprContext.class, 0);
		}

		public TerminalNode ASC() {
			return getToken(ODataParser.ASC, 0);
		}

		public TerminalNode DESC() {
			return getToken(ODataParser.DESC, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_orderbyItem;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterOrderbyItem(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitOrderbyItem(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitOrderbyItem(this);
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

	public static class CommonExprContext extends ParserRuleContext {
		public CommonExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public PrimitiveLiteralContext primitiveLiteral() {
			return getRuleContext(PrimitiveLiteralContext.class, 0);
		}

		public MethodExprContext methodExpr() {
			return getRuleContext(MethodExprContext.class, 0);
		}

		public MathExprContext mathExpr() {
			return getRuleContext(MathExprContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_commonExpr;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterCommonExpr(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitCommonExpr(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitCommonExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class MathExprContext extends ParserRuleContext {
		public MathExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public NumberContext number() {
			return getRuleContext(NumberContext.class, 0);
		}

		public TerminalNode ADD() {
			return getToken(ODataParser.ADD, 0);
		}

		public TerminalNode SUB() {
			return getToken(ODataParser.SUB, 0);
		}

		public TerminalNode MUL() {
			return getToken(ODataParser.MUL, 0);
		}

		public TerminalNode DIV() {
			return getToken(ODataParser.DIV, 0);
		}

		public TerminalNode MOD() {
			return getToken(ODataParser.MOD, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_mathExpr;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterMathExpr(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitMathExpr(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitMathExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class MethodExprContext extends ParserRuleContext {
		public MethodExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public MethodCallExprContext methodCallExpr() {
			return getRuleContext(MethodCallExprContext.class, 0);
		}

		public CalenderMethodExpContext calenderMethodExp() {
			return getRuleContext(CalenderMethodExpContext.class, 0);
		}

		public SingleMethodCallExprContext singleMethodCallExpr() {
			return getRuleContext(SingleMethodCallExprContext.class, 0);
		}

		public ConcatMethodExprContext concatMethodExpr() {
			return getRuleContext(ConcatMethodExprContext.class, 0);
		}

		public SubstringMethodCallExprContext substringMethodCallExpr() {
			return getRuleContext(SubstringMethodCallExprContext.class, 0);
		}

		public CompareMethodExprContext compareMethodExpr() {
			return getRuleContext(CompareMethodExprContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_methodExpr;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterMethodExpr(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitMethodExpr(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitMethodExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class CompareMethodExprContext extends ParserRuleContext {
		public CompareMethodExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public CompareMethodNameContext compareMethodName() {
			return getRuleContext(CompareMethodNameContext.class, 0);
		}

		public TerminalNode OPEN() {
			return getToken(ODataParser.OPEN, 0);
		}

		public TerminalNode COMMA() {
			return getToken(ODataParser.COMMA, 0);
		}

		public List<CommonExprContext> commonExpr() {
			return getRuleContexts(CommonExprContext.class);
		}

		public CommonExprContext commonExpr(int i) {
			return getRuleContext(CommonExprContext.class, i);
		}

		public TerminalNode CLOSE() {
			return getToken(ODataParser.CLOSE, 0);
		}

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class, 0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_compareMethodExpr;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterCompareMethodExpr(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitCompareMethodExpr(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitCompareMethodExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class MethodCallExprContext extends ParserRuleContext {
		public MethodCallExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public MethodNameContext methodName() {
			return getRuleContext(MethodNameContext.class, 0);
		}

		public TerminalNode OPEN() {
			return getToken(ODataParser.OPEN, 0);
		}

		public TerminalNode CLOSE() {
			return getToken(ODataParser.CLOSE, 0);
		}

		public MethodCallExprContext methodCallExpr() {
			return getRuleContext(MethodCallExprContext.class, 0);
		}

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class, 0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_methodCallExpr;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterMethodCallExpr(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitMethodCallExpr(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitMethodCallExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class CalenderMethodExpContext extends ParserRuleContext {
		public CalenderMethodExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public CalenderMethodNameContext calenderMethodName() {
			return getRuleContext(CalenderMethodNameContext.class, 0);
		}

		public TerminalNode OPEN() {
			return getToken(ODataParser.OPEN, 0);
		}

		public TerminalNode CLOSE() {
			return getToken(ODataParser.CLOSE, 0);
		}

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class, 0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class, 0);
		}

		public PrimitiveLiteralContext primitiveLiteral() {
			return getRuleContext(PrimitiveLiteralContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_calenderMethodExp;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterCalenderMethodExp(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitCalenderMethodExp(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitCalenderMethodExp(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class ConcatMethodExprContext extends ParserRuleContext {
		public ConcatMethodExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode OPEN() {
			return getToken(ODataParser.OPEN, 0);
		}

		public TerminalNode COMMA() {
			return getToken(ODataParser.COMMA, 0);
		}

		public TerminalNode CLOSE() {
			return getToken(ODataParser.CLOSE, 0);
		}

		public TerminalNode CONCAT() {
			return getToken(ODataParser.CONCAT, 0);
		}

		public ConcatMethodExprContext concatMethodExpr() {
			return getRuleContext(ConcatMethodExprContext.class, 0);
		}

		public List<PrimitiveLiteralContext> primitiveLiteral() {
			return getRuleContexts(PrimitiveLiteralContext.class);
		}

		public PrimitiveLiteralContext primitiveLiteral(int i) {
			return getRuleContext(PrimitiveLiteralContext.class, i);
		}

		public List<AttributeNameContext> attributeName() {
			return getRuleContexts(AttributeNameContext.class);
		}

		public AttributeNameContext attributeName(int i) {
			return getRuleContext(AttributeNameContext.class, i);
		}

		@Override
		public int getRuleIndex() {
			return RULE_concatMethodExpr;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterConcatMethodExpr(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitConcatMethodExpr(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitConcatMethodExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class SingleMethodCallExprContext extends ParserRuleContext {
		public TerminalNode OPEN() {
			return getToken(ODataParser.OPEN, 0);
		}

		public TerminalNode CLOSE() {
			return getToken(ODataParser.CLOSE, 0);
		}

		public SingleMethodCallExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode MIN_DATE_TIME() {
			return getToken(ODataParser.MIN_DATE_TIME, 0);
		}

		public TerminalNode MAX_DATE_TIME() {
			return getToken(ODataParser.MAX_DATE_TIME, 0);
		}

		public TerminalNode NOW() {
			return getToken(ODataParser.NOW, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_singleMethodCallExpr;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterSingleMethodCallExpr(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).exitSingleMethodCallExpr(this);
		}

		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if (visitor instanceof ODataParserVisitor)
				return ((ODataParserVisitor<? extends T>) visitor).visitSingleMethodCallExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class SubstringMethodCallExprContext extends ParserRuleContext {
		public SubstringMethodCallExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode OPEN() {
			return getToken(ODataParser.OPEN, 0);
		}

		public TerminalNode SUBSTRING() {
			return getToken(ODataParser.SUBSTRING, 0);
		}

		public TerminalNode COMMA() {
			return getToken(ODataParser.COMMA, 0);
		}

		public List<CommonExprContext> commonExpr() {
			return getRuleContexts(CommonExprContext.class);
		}

		public TerminalNode CLOSE() {
			return getToken(ODataParser.CLOSE, 0);
		}

		public JoinEntitiesContext joinEntities() {
			return getRuleContext(JoinEntitiesContext.class, 0);
		}

		public AttributeNameContext attributeName() {
			return getRuleContext(AttributeNameContext.class, 0);
		}

		public CommonExprContext commonExpr(int i) {
			return getRuleContext(CommonExprContext.class, i);
		}

		@Override
		public int getRuleIndex() {
			return RULE_substringMethodCallExpr;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataParserListener)
				((ODataParserListener) listener).enterSubstringMethodCallExpr(this);
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

		public BooleanValueContext booleanValue() {
			return getRuleContext(BooleanValueContext.class, 0);
		}

		public TerminalNode EQUAL() {
			return getToken(ODataParser.EQUAL, 0);
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

	public static class OpenParContext extends ParserRuleContext {
		public TerminalNode OPEN() {
			return getToken(ODataParser.OPEN, 0);
		}

		public OpenParContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
		public TerminalNode CLOSE() {
			return getToken(ODataParser.CLOSE, 0);
		}

		public CloseParContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
		public TerminalNode AND() {
			return getToken(ODataParser.AND, 0);
		}

		public TerminalNode OR() {
			return getToken(ODataParser.OR, 0);
		}

		public LogicalOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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

	public static class EntityNameContext extends ParserRuleContext {
		public TerminalNode ID() {
			return getToken(ODataParser.ID, 0);
		}

		public EntityNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitEntityName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitEntityName(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class AttributeNameContext extends ParserRuleContext {
		public TerminalNode ID() {
			return getToken(ODataParser.ID, 0);
		}

		public AttributeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitAttributeName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitAttributeName(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class UuidIdValueContext extends ParserRuleContext {
		public TerminalNode UUID() {
			return getToken(ODataParser.UUID, 0);
		}

		public UuidIdValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitUuidIdValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitUuidIdValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class QuotedStringContext extends ParserRuleContext {
		public TerminalNode QUOTED_STRING() {
			return getToken(ODataParser.QUOTED_STRING, 0);
		}

		public QuotedStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitQuotedString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitQuotedString(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class NullSpecLiteralContext extends ParserRuleContext {
		public TerminalNode NULL_SPEC_LITERAL() {
			return getToken(ODataParser.NULL_SPEC_LITERAL, 0);
		}

		public NullSpecLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitNullSpecLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitNullSpecLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class NullTokenContext extends ParserRuleContext {
		public TerminalNode NULL_TOKEN() {
			return getToken(ODataParser.NULL_TOKEN, 0);
		}

		public NullTokenContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitNullToken(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitNullToken(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class BooleanValueContext extends ParserRuleContext {
		public TerminalNode BOOLEAN_VALUE() {
			return getToken(ODataParser.BOOLEAN_VALUE, 0);
		}

		public BooleanValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitBooleanValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitBooleanValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class DurationValueContext extends ParserRuleContext {
		public TerminalNode DURATION_VALUE() {
			return getToken(ODataParser.DURATION_VALUE, 0);
		}

		public DurationValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitDurationValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitDurationValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class DateValueContext extends ParserRuleContext {
		public TerminalNode DATE_VALUE() {
			return getToken(ODataParser.DATE_VALUE, 0);
		}

		public DateValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitDateValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitDateValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class DateTimeOffsetValueContext extends ParserRuleContext {
		public TerminalNode DATE_TIME_OFFSET_VALUE() {
			return getToken(ODataParser.DATE_TIME_OFFSET_VALUE, 0);
		}

		public DateTimeOffsetValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitDateTimeOffsetValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitDateTimeOffsetValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class TimeOfDayValueContext extends ParserRuleContext {
		public TerminalNode TIME_OF_DAY_VALUE() {
			return getToken(ODataParser.TIME_OF_DAY_VALUE, 0);
		}

		public TimeOfDayValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitTimeOfDayValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitTimeOfDayValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class DecimalLiteralContext extends ParserRuleContext {
		public TerminalNode DECIMAL_LITERAL() {
			return getToken(ODataParser.DECIMAL_LITERAL, 0);
		}

		public DecimalLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitDecimalLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitDecimalLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class FloatValueContext extends ParserRuleContext {
		public TerminalNode FLOAT() {
			return getToken(ODataParser.FLOAT, 0);
		}

		public FloatValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitFloatValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitFloatValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public static class IntegerValueContext extends ParserRuleContext {
		public TerminalNode INTEGER() {
			return getToken(ODataParser.INTEGER, 0);
		}

		public IntegerValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
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
			if ( listener instanceof ODataParserListener ) ((ODataParserListener)listener).exitIntegerValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ODataParserVisitor ) return ((ODataParserVisitor<? extends T>)visitor).visitIntegerValue(this);
			else return visitor.visitChildren(this);
		}
	}
}