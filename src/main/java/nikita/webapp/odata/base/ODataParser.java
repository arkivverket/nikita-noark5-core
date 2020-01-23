// Generated from /home/tsodring/tmp/nikita-noark5-core/src/main/antlr4/ODataParser.g4 by ANTLR 4.7.2
package nikita.webapp.odata.base;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
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
            RULE_stringCompareCommand = 13, RULE_substringExpression = 14, RULE_indexOfExpression = 15,
            RULE_lengthExpression = 16, RULE_timeExpression = 17, RULE_stringModifierExpression = 18,
            RULE_concatExpression = 19, RULE_comparisonExpression = 20, RULE_integerComparatorExpression = 21,
            RULE_integerCompareCommand = 22, RULE_floatComparatorExpression = 23,
            RULE_floatCommand = 24, RULE_floatOrIntegerValue = 25, RULE_orderByClause = 26,
            RULE_orderByExpression = 27, RULE_orderAscDesc = 28, RULE_comparisonOperator = 29,
            RULE_logicalOperator = 30, RULE_columnName = 31, RULE_entityName = 32,
            RULE_attributeName = 33, RULE_packageName = 34, RULE_value = 35, RULE_singleQuotedString = 36,
            RULE_integerValue = 37, RULE_floatValue = 38, RULE_systemIdValue = 39;
    public static final String[] ruleNames = makeRuleNames();
    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;
    public static final String _serializedATN =
            "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3Q\u0136\4\2\t\2\4" +
                    "\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" +
                    "\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22" +
                    "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31" +
                    "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!" +
                    "\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\3\2\3\2\3\2\3" +
                    "\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4" +
                    "\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4q\n\4\3\4\3\4\3\5\3\5\3\5\5\5" +
                    "x\n\5\3\5\3\5\3\5\3\5\3\5\5\5\177\n\5\5\5\u0081\n\5\3\6\3\6\3\6\3\6\3" +
                    "\6\3\6\3\6\3\6\3\6\7\6\u008c\n\6\f\6\16\6\u008f\13\6\3\7\3\7\3\7\3\b\3" +
                    "\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13" +
                    "\u00a4\n\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u00b0" +
                    "\n\13\3\13\3\13\3\13\5\13\u00b5\n\13\5\13\u00b7\n\13\3\f\3\f\3\r\3\r\3" +
                    "\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3" +
                    "\20\3\20\5\20\u00cd\n\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21" +
                    "\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23" +
                    "\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25" +
                    "\3\25\3\25\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30" +
                    "\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\33\3\33\5\33\u010e" +
                    "\n\33\3\34\3\34\3\34\3\34\3\34\7\34\u0115\n\34\f\34\16\34\u0118\13\34" +
                    "\3\35\3\35\5\35\u011c\n\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#" +
                    "\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3)\2\3\n*\2\4\6\b\n\f\16\20" +
                    "\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNP\2\13\3\2\r\17" +
                    "\3\2\37\"\3\2\25\27\3\2\31\36\3\2#%\3\2./\3\2&+\3\2\60\61\4\2IIKK\2\u0125" +
                    "\2R\3\2\2\2\4a\3\2\2\2\6p\3\2\2\2\b\u0080\3\2\2\2\n\u0082\3\2\2\2\f\u0090" +
                    "\3\2\2\2\16\u0093\3\2\2\2\20\u0096\3\2\2\2\22\u0099\3\2\2\2\24\u00b6\3" +
                    "\2\2\2\26\u00b8\3\2\2\2\30\u00ba\3\2\2\2\32\u00bc\3\2\2\2\34\u00c3\3\2" +
                    "\2\2\36\u00c5\3\2\2\2 \u00d2\3\2\2\2\"\u00db\3\2\2\2$\u00e2\3\2\2\2&\u00e7" +
                    "\3\2\2\2(\u00ee\3\2\2\2*\u00f5\3\2\2\2,\u00f9\3\2\2\2.\u0100\3\2\2\2\60" +
                    "\u0102\3\2\2\2\62\u0109\3\2\2\2\64\u010d\3\2\2\2\66\u010f\3\2\2\28\u0119" +
                    "\3\2\2\2:\u011d\3\2\2\2<\u011f\3\2\2\2>\u0121\3\2\2\2@\u0123\3\2\2\2B" +
                    "\u0125\3\2\2\2D\u0127\3\2\2\2F\u0129\3\2\2\2H\u012b\3\2\2\2J\u012d\3\2" +
                    "\2\2L\u012f\3\2\2\2N\u0131\3\2\2\2P\u0133\3\2\2\2RS\7N\2\2ST\7D\2\2TU" +
                    "\5B\"\2UV\7D\2\2VW\5P)\2WX\7D\2\2XY\5B\"\2YZ\7D\2\2Z[\7\b\2\2[\\\7N\2" +
                    "\2\\]\7D\2\2]^\5B\"\2^_\7D\2\2_`\5P)\2`\3\3\2\2\2ab\5\6\4\2bc\5\b\5\2" +
                    "c\5\3\2\2\2de\5F$\2ef\7D\2\2fg\5B\"\2gq\3\2\2\2hi\5F$\2ij\7D\2\2jk\5B" +
                    "\"\2kl\7D\2\2lm\5P)\2mn\7D\2\2no\5B\"\2oq\3\2\2\2pd\3\2\2\2ph\3\2\2\2" +
                    "qr\3\2\2\2rs\79\2\2s\7\3\2\2\2tw\5\f\7\2uv\7@\2\2vx\5\16\b\2wu\3\2\2\2" +
                    "wx\3\2\2\2x\u0081\3\2\2\2y\u0081\5\20\t\2z\u0081\5\22\n\2{~\5\66\34\2" +
                    "|}\7@\2\2}\177\5\16\b\2~|\3\2\2\2~\177\3\2\2\2\177\u0081\3\2\2\2\u0080" +
                    "t\3\2\2\2\u0080y\3\2\2\2\u0080z\3\2\2\2\u0080{\3\2\2\2\u0081\t\3\2\2\2" +
                    "\u0082\u0083\b\6\1\2\u0083\u0084\5\f\7\2\u0084\u008d\3\2\2\2\u0085\u0086" +
                    "\f\4\2\2\u0086\u0087\5<\37\2\u0087\u0088\5\n\6\5\u0088\u008c\3\2\2\2\u0089" +
                    "\u008a\f\3\2\2\u008a\u008c\5<\37\2\u008b\u0085\3\2\2\2\u008b\u0089\3\2" +
                    "\2\2\u008c\u008f\3\2\2\2\u008d\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e" +
                    "\13\3\2\2\2\u008f\u008d\3\2\2\2\u0090\u0091\7\4\2\2\u0091\u0092\5\24\13" +
                    "\2\u0092\r\3\2\2\2\u0093\u0094\7\n\2\2\u0094\u0095\5L\'\2\u0095\17\3\2" +
                    "\2\2\u0096\u0097\7\5\2\2\u0097\u0098\5L\'\2\u0098\21\3\2\2\2\u0099\u009a" +
                    "\7\6\2\2\u009a\u009b\5L\'\2\u009b\23\3\2\2\2\u009c\u009d\5\26\f\2\u009d" +
                    "\u009e\5\24\13\2\u009e\u009f\5\30\r\2\u009f\u00a3\3\2\2\2\u00a0\u00a1" +
                    "\5> \2\u00a1\u00a2\5\24\13\2\u00a2\u00a4\3\2\2\2\u00a3\u00a0\3\2\2\2\u00a3" +
                    "\u00a4\3\2\2\2\u00a4\u00b7\3\2\2\2\u00a5\u00b0\5\32\16\2\u00a6\u00b0\5" +
                    "*\26\2\u00a7\u00b0\5,\27\2\u00a8\u00b0\5\60\31\2\u00a9\u00b0\5\36\20\2" +
                    "\u00aa\u00b0\5 \21\2\u00ab\u00b0\5\"\22\2\u00ac\u00b0\5&\24\2\u00ad\u00b0" +
                    "\5$\23\2\u00ae\u00b0\5(\25\2\u00af\u00a5\3\2\2\2\u00af\u00a6\3\2\2\2\u00af" +
                    "\u00a7\3\2\2\2\u00af\u00a8\3\2\2\2\u00af\u00a9\3\2\2\2\u00af\u00aa\3\2" +
                    "\2\2\u00af\u00ab\3\2\2\2\u00af\u00ac\3\2\2\2\u00af\u00ad\3\2\2\2\u00af" +
                    "\u00ae\3\2\2\2\u00b0\u00b4\3\2\2\2\u00b1\u00b2\5> \2\u00b2\u00b3\5\24" +
                    "\13\2\u00b3\u00b5\3\2\2\2\u00b4\u00b1\3\2\2\2\u00b4\u00b5\3\2\2\2\u00b5" +
                    "\u00b7\3\2\2\2\u00b6\u009c\3\2\2\2\u00b6\u00af\3\2\2\2\u00b7\25\3\2\2" +
                    "\2\u00b8\u00b9\7\66\2\2\u00b9\27\3\2\2\2\u00ba\u00bb\7\67\2\2\u00bb\31" +
                    "\3\2\2\2\u00bc\u00bd\5\34\17\2\u00bd\u00be\7\66\2\2\u00be\u00bf\5B\"\2" +
                    "\u00bf\u00c0\78\2\2\u00c0\u00c1\5J&\2\u00c1\u00c2\7\67\2\2\u00c2\33\3" +
                    "\2\2\2\u00c3\u00c4\t\2\2\2\u00c4\35\3\2\2\2\u00c5\u00c6\7\24\2\2\u00c6" +
                    "\u00c7\7\66\2\2\u00c7\u00c8\5B\"\2\u00c8\u00c9\78\2\2\u00c9\u00cc\5L\'" +
                    "\2\u00ca\u00cb\78\2\2\u00cb\u00cd\5L\'\2\u00cc\u00ca\3\2\2\2\u00cc\u00cd" +
                    "\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00cf\7\67\2\2\u00cf\u00d0\5<\37\2" +
                    "\u00d0\u00d1\5J&\2\u00d1\37\3\2\2\2\u00d2\u00d3\7\22\2\2\u00d3\u00d4\7" +
                    "\66\2\2\u00d4\u00d5\5B\"\2\u00d5\u00d6\78\2\2\u00d6\u00d7\5J&\2\u00d7" +
                    "\u00d8\7\67\2\2\u00d8\u00d9\5<\37\2\u00d9\u00da\5J&\2\u00da!\3\2\2\2\u00db" +
                    "\u00dc\7\21\2\2\u00dc\u00dd\7\66\2\2\u00dd\u00de\5B\"\2\u00de\u00df\7" +
                    "\67\2\2\u00df\u00e0\5<\37\2\u00e0\u00e1\5L\'\2\u00e1#\3\2\2\2\u00e2\u00e3" +
                    "\t\3\2\2\u00e3\u00e4\7\66\2\2\u00e4\u00e5\5B\"\2\u00e5\u00e6\7\67\2\2" +
                    "\u00e6%\3\2\2\2\u00e7\u00e8\t\4\2\2\u00e8\u00e9\7\66\2\2\u00e9\u00ea\5" +
                    "B\"\2\u00ea\u00eb\78\2\2\u00eb\u00ec\5H%\2\u00ec\u00ed\7\67\2\2\u00ed" +
                    "\'\3\2\2\2\u00ee\u00ef\7\30\2\2\u00ef\u00f0\7\66\2\2\u00f0\u00f1\5B\"" +
                    "\2\u00f1\u00f2\78\2\2\u00f2\u00f3\5H%\2\u00f3\u00f4\7\67\2\2\u00f4)\3" +
                    "\2\2\2\u00f5\u00f6\5D#\2\u00f6\u00f7\5<\37\2\u00f7\u00f8\5H%\2\u00f8+" +
                    "\3\2\2\2\u00f9\u00fa\5.\30\2\u00fa\u00fb\7\66\2\2\u00fb\u00fc\5B\"\2\u00fc" +
                    "\u00fd\7\67\2\2\u00fd\u00fe\5<\37\2\u00fe\u00ff\5L\'\2\u00ff-\3\2\2\2" +
                    "\u0100\u0101\t\5\2\2\u0101/\3\2\2\2\u0102\u0103\5\62\32\2\u0103\u0104" +
                    "\7\66\2\2\u0104\u0105\5D#\2\u0105\u0106\7\67\2\2\u0106\u0107\5<\37\2\u0107" +
                    "\u0108\5\64\33\2\u0108\61\3\2\2\2\u0109\u010a\t\6\2\2\u010a\63\3\2\2\2" +
                    "\u010b\u010e\5N(\2\u010c\u010e\5L\'\2\u010d\u010b\3\2\2\2\u010d\u010c" +
                    "\3\2\2\2\u010e\65\3\2\2\2\u010f\u0110\7,\2\2\u0110\u0111\7-\2\2\u0111" +
                    "\u0116\58\35\2\u0112\u0113\78\2\2\u0113\u0115\58\35\2\u0114\u0112\3\2" +
                    "\2\2\u0115\u0118\3\2\2\2\u0116\u0114\3\2\2\2\u0116\u0117\3\2\2\2\u0117" +
                    "\67\3\2\2\2\u0118\u0116\3\2\2\2\u0119\u011b\5D#\2\u011a\u011c\5:\36\2" +
                    "\u011b\u011a\3\2\2\2\u011b\u011c\3\2\2\2\u011c9\3\2\2\2\u011d\u011e\t" +
                    "\7\2\2\u011e;\3\2\2\2\u011f\u0120\t\b\2\2\u0120=\3\2\2\2\u0121\u0122\t" +
                    "\t\2\2\u0122?\3\2\2\2\u0123\u0124\7I\2\2\u0124A\3\2\2\2\u0125\u0126\7" +
                    "F\2\2\u0126C\3\2\2\2\u0127\u0128\7F\2\2\u0128E\3\2\2\2\u0129\u012a\7F" +
                    "\2\2\u012aG\3\2\2\2\u012b\u012c\t\n\2\2\u012cI\3\2\2\2\u012d\u012e\7J" +
                    "\2\2\u012eK\3\2\2\2\u012f\u0130\7G\2\2\u0130M\3\2\2\2\u0131\u0132\7H\2" +
                    "\2\u0132O\3\2\2\2\u0133\u0134\7E\2\2\u0134Q\3\2\2\2\20pw~\u0080\u008b" +
                    "\u008d\u00a3\u00af\u00b4\u00b6\u00cc\u010d\u0116\u011b";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    private static final String[] _LITERAL_NAMES = makeLiteralNames();
    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    static {
        RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION);
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
                "stringCompareCommand", "substringExpression", "indexOfExpression", "lengthExpression",
                "timeExpression", "stringModifierExpression", "concatExpression", "comparisonExpression",
                "integerComparatorExpression", "integerCompareCommand", "floatComparatorExpression",
                "floatCommand", "floatOrIntegerValue", "orderByClause", "orderByExpression",
                "orderAscDesc", "comparisonOperator", "logicalOperator", "columnName",
                "entityName", "attributeName", "packageName", "value", "singleQuotedString",
                "integerValue", "floatValue", "systemIdValue"
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
                setState(80);
                match(ODATA_ARK);
                setState(81);
                match(SLASH);
                setState(82);
                entityName();
                setState(83);
                match(SLASH);
                setState(84);
                systemIdValue();
                setState(85);
                match(SLASH);
                setState(86);
                entityName();
                setState(87);
                match(SLASH);
                setState(88);
                match(REF);
                setState(89);
                match(ODATA_ARK);
                setState(90);
                match(SLASH);
                setState(91);
                entityName();
                setState(92);
                match(SLASH);
                setState(93);
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
                setState(95);
                entityBase();
                setState(96);
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
                setState(110);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 0, _ctx)) {
                    case 1: {
                        {
                            setState(98);
                            packageName();
                            setState(99);
                            match(SLASH);
                            setState(100);
                            entityName();
                        }
                    }
                    break;
                    case 2: {
                        {
                            setState(102);
                            packageName();
                            setState(103);
                            match(SLASH);
                            setState(104);
                            entityName();
                            setState(105);
                            match(SLASH);
                            setState(106);
                            systemIdValue();
                            setState(107);
                            match(SLASH);
                            setState(108);
                            entityName();
                        }
                    }
                    break;
                }
                setState(112);
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
                setState(126);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case FILTER: {
                        {
                            setState(114);
                            filterStatement();
                            setState(117);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            if (_la == AMPERSAND) {
                                {
                                    setState(115);
                                    match(AMPERSAND);
                                    setState(116);
                                    countStatement();
                                }
                            }

                        }
                    }
                    break;
                    case TOP: {
                        setState(119);
                        topStatement();
                    }
                    break;
                    case SKIPRULE: {
                        setState(120);
                        skipStatement();
                    }
                    break;
                    case ORDER: {
                        {
                            setState(121);
                            orderByClause();
                            setState(124);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            if (_la == AMPERSAND) {
                                {
                                    setState(122);
                                    match(AMPERSAND);
                                    setState(123);
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
                    setState(129);
                    filterStatement();
                }
                _ctx.stop = _input.LT(-1);
                setState(139);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 5, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            setState(137);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 4, _ctx)) {
                                case 1: {
                                    _localctx = new PredicateContext(_parentctx, _parentState);
                                    _localctx.left = _prevctx;
                                    pushNewRecursionContext(_localctx, _startState, RULE_predicate);
                                    setState(131);
                                    if (!(precpred(_ctx, 2)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 2)");
                                    setState(132);
                                    comparisonOperator();
                                    setState(133);
                                    ((PredicateContext) _localctx).right = predicate(3);
                                }
                                break;
                                case 2: {
                                    _localctx = new PredicateContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_predicate);
                                    setState(135);
                                    if (!(precpred(_ctx, 1)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 1)");
                                    setState(136);
                                    comparisonOperator();
                                }
                                break;
                            }
                        }
                    }
                    setState(141);
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
                setState(142);
                match(FILTER);
                setState(143);
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
                setState(145);
                match(COUNT);
                setState(146);
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
                setState(148);
                match(TOP);
                setState(149);
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
                setState(151);
                match(SKIPRULE);
                setState(152);
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
            setState(180);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case LR_BRACKET:
                    enterOuterAlt(_localctx, 1);
                {
                    {
                        setState(154);
                        boolExpressionLeft();
                        setState(155);
                        filterExpression();
                        setState(156);
                        boolExpressionRight();
                    }
                    setState(161);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 6, _ctx)) {
                        case 1: {
                            setState(158);
                            logicalOperator();
                            setState(159);
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
                    setState(173);
                    _errHandler.sync(this);
                    switch (_input.LA(1)) {
                        case CONTAINS:
                        case STARTSWITH:
                        case ENDSWITH: {
                            setState(163);
                            stringCompareExpression();
                        }
                        break;
                        case NOARK_ENTITY: {
                            setState(164);
                            comparisonExpression();
                        }
                        break;
                        case DAY:
                        case MONTH:
                        case YEAR:
                        case HOUR:
                        case MINUTE:
                        case SECOND: {
                            setState(165);
                            integerComparatorExpression();
                        }
                        break;
                        case ROUND:
                        case FLOOR:
                        case CEILING: {
                            setState(166);
                            floatComparatorExpression();
                        }
                        break;
                        case SUBSTRING: {
                            setState(167);
                            substringExpression();
                        }
                        break;
                        case INDEXOF: {
                            setState(168);
                            indexOfExpression();
                        }
                        break;
                        case LENGTH: {
                            setState(169);
                            lengthExpression();
                        }
                        break;
                        case TOLOWER:
                        case TOUPPER:
                        case TRIM: {
                            setState(170);
                            stringModifierExpression();
                        }
                        break;
                        case NOW:
                        case MAXDATETIME:
                        case MINDATETIME:
                        case TOTALOFFSETMINUTES: {
                            setState(171);
                            timeExpression();
                        }
                        break;
                        case CONCAT: {
                            setState(172);
                            concatExpression();
                        }
                        break;
                        default:
                            throw new NoViableAltException(this);
                    }
                    setState(178);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 8, _ctx)) {
                        case 1: {
                            setState(175);
                            logicalOperator();
                            setState(176);
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
                setState(182);
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
                setState(184);
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
                setState(186);
                stringCompareCommand();
                setState(187);
                match(LR_BRACKET);
                setState(188);
                entityName();
                setState(189);
                match(COMMA);
                setState(190);
                singleQuotedString();
                setState(191);
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

    public final StringCompareCommandContext stringCompareCommand() throws RecognitionException {
        StringCompareCommandContext _localctx = new StringCompareCommandContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_stringCompareCommand);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(193);
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
        enterRule(_localctx, 28, RULE_substringExpression);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(195);
                match(SUBSTRING);
                setState(196);
                match(LR_BRACKET);
                setState(197);
                entityName();
                setState(198);
                match(COMMA);
                setState(199);
                integerValue();
                setState(202);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == COMMA) {
                    {
                        setState(200);
                        match(COMMA);
                        setState(201);
                        integerValue();
                    }
                }

                setState(204);
                match(RR_BRACKET);
                setState(205);
                comparisonOperator();
                setState(206);
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
        enterRule(_localctx, 30, RULE_indexOfExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(208);
                match(INDEXOF);
                setState(209);
                match(LR_BRACKET);
                setState(210);
                entityName();
                setState(211);
                match(COMMA);
                setState(212);
                singleQuotedString();
                setState(213);
                match(RR_BRACKET);
                setState(214);
                comparisonOperator();
                setState(215);
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
        enterRule(_localctx, 32, RULE_lengthExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(217);
                match(LENGTH);
                setState(218);
                match(LR_BRACKET);
                setState(219);
                entityName();
                setState(220);
                match(RR_BRACKET);
                setState(221);
                comparisonOperator();
                setState(222);
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
        enterRule(_localctx, 34, RULE_timeExpression);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(224);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NOW) | (1L << MAXDATETIME) | (1L << MINDATETIME) | (1L << TOTALOFFSETMINUTES))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(225);
                match(LR_BRACKET);
                setState(226);
                entityName();
                setState(227);
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
        enterRule(_localctx, 36, RULE_stringModifierExpression);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(229);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << TOLOWER) | (1L << TOUPPER) | (1L << TRIM))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(230);
                match(LR_BRACKET);
                setState(231);
                entityName();
                setState(232);
                match(COMMA);
                setState(233);
                value();
                setState(234);
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
        enterRule(_localctx, 38, RULE_concatExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(236);
                match(CONCAT);
                setState(237);
                match(LR_BRACKET);
                setState(238);
                entityName();
                setState(239);
                match(COMMA);
                setState(240);
                value();
                setState(241);
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
        enterRule(_localctx, 40, RULE_comparisonExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(243);
                attributeName();
                setState(244);
                comparisonOperator();
                setState(245);
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
        enterRule(_localctx, 42, RULE_integerComparatorExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(247);
                integerCompareCommand();
                setState(248);
                match(LR_BRACKET);
                setState(249);
                entityName();
                setState(250);
                match(RR_BRACKET);
                setState(251);
                comparisonOperator();
                setState(252);
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
        enterRule(_localctx, 44, RULE_integerCompareCommand);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(254);
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
        enterRule(_localctx, 46, RULE_floatComparatorExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(256);
                floatCommand();
                setState(257);
                match(LR_BRACKET);
                setState(258);
                attributeName();
                setState(259);
                match(RR_BRACKET);
                setState(260);
                comparisonOperator();
                setState(261);
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
        enterRule(_localctx, 48, RULE_floatCommand);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(263);
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
        enterRule(_localctx, 50, RULE_floatOrIntegerValue);
        try {
            setState(267);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case FLOAT:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(265);
                    floatValue();
                }
                break;
                case INTEGER:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(266);
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
        enterRule(_localctx, 52, RULE_orderByClause);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(269);
                match(ORDER);
                setState(270);
                match(BY);
                setState(271);
                orderByExpression();
                setState(276);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == COMMA) {
                    {
                        {
                            setState(272);
                            match(COMMA);
                            setState(273);
                            orderByExpression();
                        }
                    }
                    setState(278);
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
        enterRule(_localctx, 54, RULE_orderByExpression);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(279);
                attributeName();
                setState(281);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == DESC || _la == ASC) {
                    {
                        setState(280);
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
        enterRule(_localctx, 56, RULE_orderAscDesc);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(283);
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
        enterRule(_localctx, 58, RULE_comparisonOperator);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(285);
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
        enterRule(_localctx, 60, RULE_logicalOperator);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(287);
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
        enterRule(_localctx, 62, RULE_columnName);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(289);
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
        enterRule(_localctx, 64, RULE_entityName);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(291);
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
        enterRule(_localctx, 66, RULE_attributeName);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(293);
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
        enterRule(_localctx, 68, RULE_packageName);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(295);
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
        enterRule(_localctx, 70, RULE_value);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(297);
                _la = _input.LA(1);
                if (!(_la == ID || _la == STRING_LITERAL)) {
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
        enterRule(_localctx, 72, RULE_singleQuotedString);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(299);
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
        enterRule(_localctx, 74, RULE_integerValue);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(301);
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
        enterRule(_localctx, 76, RULE_floatValue);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(303);
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
        enterRule(_localctx, 78, RULE_systemIdValue);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(305);
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

        public EntityNameContext entityName() {
            return getRuleContext(EntityNameContext.class, 0);
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
    }

    public static class ComparisonExpressionContext extends ParserRuleContext {
        public ComparisonExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public AttributeNameContext attributeName() {
            return getRuleContext(AttributeNameContext.class, 0);
        }

        public ComparisonOperatorContext comparisonOperator() {
            return getRuleContext(ComparisonOperatorContext.class, 0);
        }

        public ValueContext value() {
            return getRuleContext(ValueContext.class, 0);
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
    }

    public static class ValueContext extends ParserRuleContext {
        public ValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        public TerminalNode ID() {
            return getToken(ODataParser.ID, 0);
        }

        public TerminalNode STRING_LITERAL() {
            return getToken(ODataParser.STRING_LITERAL, 0);
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
    }
}
