// Generated from /home/tsodring/git/nikita-noark5-core/src/main/antlr4/ODataLexer.g4 by ANTLR 4.7.2
package nikita.webapp.odata.base;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ODataLexer extends Lexer {
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
            ERRORCHANNEL = 2;
    public static final String[] ruleNames = makeRuleNames();
    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;
    public static final String _serializedATN =
            "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2Q\u02f9\b\1\4\2\t" +
                    "\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13" +
                    "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22" +
                    "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31" +
                    "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!" +
                    "\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4" +
                    ",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t" +
                    "\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t=" +
                    "\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I" +
                    "\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT" +
                    "\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\3\2\6\2\u00b5\n\2\r\2\16\2\u00b6\3\2\3" +
                    "\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5" +
                    "\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3" +
                    "\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t" +
                    "\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13" +
                    "\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3" +
                    "\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16" +
                    "\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20" +
                    "\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21" +
                    "\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3\23" +
                    "\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25" +
                    "\3\25\3\25\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27" +
                    "\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31" +
                    "\3\32\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34" +
                    "\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36" +
                    "\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3\37\3 \3 \3 " +
                    "\3 \3 \3 \3 \3 \3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!\3!" +
                    "\3!\3!\3!\3!\3!\3\"\3\"\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3$\3$\3$\3$" +
                    "\3$\3$\3$\3$\3%\3%\3%\3&\3&\3&\3\'\3\'\3\'\3(\3(\3(\3)\3)\3)\3*\3*\3*" +
                    "\3+\3+\3+\3+\3+\3+\3,\3,\3,\3-\3-\3-\3-\3-\3.\3.\3.\3.\3/\3/\3/\3\60\3" +
                    "\60\3\60\3\60\3\61\3\61\3\61\3\61\3\62\3\62\3\62\3\62\3\63\3\63\3\63\3" +
                    "\63\3\63\3\64\3\64\3\64\3\64\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\3" +
                    "8\38\39\39\3:\3:\3;\3;\3<\3<\3=\3=\3>\3>\3?\3?\3@\3@\3@\3@\3@\3A\3A\3" +
                    "A\3B\3B\3C\3C\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3D\3E\6E\u023d\nE\r" +
                    "E\16E\u023e\3E\5E\u0242\nE\3E\7E\u0245\nE\fE\16E\u0248\13E\3F\6F\u024b" +
                    "\nF\rF\16F\u024c\3G\6G\u0250\nG\rG\16G\u0251\3G\3G\6G\u0256\nG\rG\16G" +
                    "\u0257\3H\3H\3I\3I\3J\3J\5J\u0260\nJ\3K\6K\u0263\nK\rK\16K\u0264\3L\3" +
                    "L\3L\3L\3L\6L\u026c\nL\rL\16L\u026d\3L\3L\3L\3L\3L\3L\6L\u0276\nL\rL\16" +
                    "L\u0277\5L\u027a\nL\3M\3M\3M\3M\3M\3M\3M\3M\3M\3M\3M\3M\3M\3M\3M\3M\3" +
                    "M\3M\3M\3M\3M\3N\3N\3N\6N\u0294\nN\rN\16N\u0295\3O\3O\5O\u029a\nO\3O\3" +
                    "O\7O\u029e\nO\fO\16O\u02a1\13O\3P\3P\5P\u02a5\nP\3P\6P\u02a8\nP\rP\16" +
                    "P\u02a9\3Q\7Q\u02ad\nQ\fQ\16Q\u02b0\13Q\3Q\6Q\u02b3\nQ\rQ\16Q\u02b4\3" +
                    "Q\7Q\u02b8\nQ\fQ\16Q\u02bb\13Q\3R\3R\3R\3R\3R\3R\7R\u02c3\nR\fR\16R\u02c6" +
                    "\13R\3R\3R\3S\3S\3S\3S\3S\3S\7S\u02d0\nS\fS\16S\u02d3\13S\3S\3S\3T\3T" +
                    "\3T\3T\3T\3T\7T\u02dd\nT\fT\16T\u02e0\13T\3T\3T\3U\3U\3V\3V\3W\3W\3W\6" +
                    "W\u02eb\nW\rW\16W\u02ec\3W\3W\3X\3X\3X\3X\3X\3Y\3Y\3Y\3Y\4\u02ae\u02b4" +
                    "\2Z\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35" +
                    "\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36" +
                    ";\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g\65i\66k\67" +
                    "m8o9q:s;u<w=y>{?}@\177A\u0081B\u0083C\u0085D\u0087E\u0089F\u008bG\u008d" +
                    "H\u008fI\u0091J\u0093K\u0095L\u0097M\u0099N\u009bO\u009dP\u009f\2\u00a1" +
                    "\2\u00a3\2\u00a5\2\u00a7\2\u00a9\2\u00ab\2\u00ad\2\u00af\2\u00b1Q\3\2" +
                    "\20\3\2\"\"\5\2\62;CHch\6\2\62;C\\c|\u0080\u0080\6\2/\60\62;C\\c|\4\2" +
                    "--//\7\2&&\62;C\\aac|\6\2&&C\\aac|\4\2$$^^\4\2))^^\4\2^^bb\4\2\62;CH\3" +
                    "\2\62;\3\2\62\63\5\2\62;C\\c|\2\u030e\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2" +
                    "\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2" +
                    "\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3" +
                    "\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3" +
                    "\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65" +
                    "\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3" +
                    "\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2" +
                    "\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2" +
                    "[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3" +
                    "\2\2\2\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2" +
                    "\2\2u\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2" +
                    "\2\2\u0081\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2\u0087\3\2\2\2\2\u0089" +
                    "\3\2\2\2\2\u008b\3\2\2\2\2\u008d\3\2\2\2\2\u008f\3\2\2\2\2\u0091\3\2\2" +
                    "\2\2\u0093\3\2\2\2\2\u0095\3\2\2\2\2\u0097\3\2\2\2\2\u0099\3\2\2\2\2\u009b" +
                    "\3\2\2\2\2\u009d\3\2\2\2\2\u00b1\3\2\2\2\3\u00b4\3\2\2\2\5\u00ba\3\2\2" +
                    "\2\7\u00c3\3\2\2\2\t\u00c9\3\2\2\2\13\u00d0\3\2\2\2\r\u00da\3\2\2\2\17" +
                    "\u00e4\3\2\2\2\21\u00ed\3\2\2\2\23\u00f5\3\2\2\2\25\u00fe\3\2\2\2\27\u0103" +
                    "\3\2\2\2\31\u010c\3\2\2\2\33\u0117\3\2\2\2\35\u0120\3\2\2\2\37\u012c\3" +
                    "\2\2\2!\u0133\3\2\2\2#\u013b\3\2\2\2%\u0143\3\2\2\2\'\u014d\3\2\2\2)\u0155" +
                    "\3\2\2\2+\u015d\3\2\2\2-\u0162\3\2\2\2/\u0169\3\2\2\2\61\u016d\3\2\2\2" +
                    "\63\u0173\3\2\2\2\65\u0178\3\2\2\2\67\u017d\3\2\2\29\u0184\3\2\2\2;\u018b" +
                    "\3\2\2\2=\u018f\3\2\2\2?\u019b\3\2\2\2A\u01a7\3\2\2\2C\u01ba\3\2\2\2E" +
                    "\u01c0\3\2\2\2G\u01c6\3\2\2\2I\u01ce\3\2\2\2K\u01d1\3\2\2\2M\u01d4\3\2" +
                    "\2\2O\u01d7\3\2\2\2Q\u01da\3\2\2\2S\u01dd\3\2\2\2U\u01e0\3\2\2\2W\u01e6" +
                    "\3\2\2\2Y\u01e9\3\2\2\2[\u01ee\3\2\2\2]\u01f2\3\2\2\2_\u01f5\3\2\2\2a" +
                    "\u01f9\3\2\2\2c\u01fd\3\2\2\2e\u0201\3\2\2\2g\u0206\3\2\2\2i\u020c\3\2" +
                    "\2\2k\u020e\3\2\2\2m\u0210\3\2\2\2o\u0212\3\2\2\2q\u0214\3\2\2\2s\u0216" +
                    "\3\2\2\2u\u0218\3\2\2\2w\u021a\3\2\2\2y\u021c\3\2\2\2{\u021e\3\2\2\2}" +
                    "\u0220\3\2\2\2\177\u0222\3\2\2\2\u0081\u0227\3\2\2\2\u0083\u022a\3\2\2" +
                    "\2\u0085\u022c\3\2\2\2\u0087\u022e\3\2\2\2\u0089\u023c\3\2\2\2\u008b\u024a" +
                    "\3\2\2\2\u008d\u024f\3\2\2\2\u008f\u0259\3\2\2\2\u0091\u025b\3\2\2\2\u0093" +
                    "\u025f\3\2\2\2\u0095\u0262\3\2\2\2\u0097\u0279\3\2\2\2\u0099\u027b\3\2" +
                    "\2\2\u009b\u0293\3\2\2\2\u009d\u0299\3\2\2\2\u009f\u02a2\3\2\2\2\u00a1" +
                    "\u02ae\3\2\2\2\u00a3\u02bc\3\2\2\2\u00a5\u02c9\3\2\2\2\u00a7\u02d6\3\2" +
                    "\2\2\u00a9\u02e3\3\2\2\2\u00ab\u02e5\3\2\2\2\u00ad\u02e7\3\2\2\2\u00af" +
                    "\u02f0\3\2\2\2\u00b1\u02f5\3\2\2\2\u00b3\u00b5\t\2\2\2\u00b4\u00b3\3\2" +
                    "\2\2\u00b5\u00b6\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7" +
                    "\u00b8\3\2\2\2\u00b8\u00b9\b\2\2\2\u00b9\4\3\2\2\2\u00ba\u00bb\7&\2\2" +
                    "\u00bb\u00bc\7h\2\2\u00bc\u00bd\7k\2\2\u00bd\u00be\7n\2\2\u00be\u00bf" +
                    "\7v\2\2\u00bf\u00c0\7g\2\2\u00c0\u00c1\7t\2\2\u00c1\u00c2\7?\2\2\u00c2" +
                    "\6\3\2\2\2\u00c3\u00c4\7&\2\2\u00c4\u00c5\7v\2\2\u00c5\u00c6\7q\2\2\u00c6" +
                    "\u00c7\7r\2\2\u00c7\u00c8\7?\2\2\u00c8\b\3\2\2\2\u00c9\u00ca\7&\2\2\u00ca" +
                    "\u00cb\7u\2\2\u00cb\u00cc\7m\2\2\u00cc\u00cd\7k\2\2\u00cd\u00ce\7r\2\2" +
                    "\u00ce\u00cf\7?\2\2\u00cf\n\3\2\2\2\u00d0\u00d1\7&\2\2\u00d1\u00d2\7q" +
                    "\2\2\u00d2\u00d3\7t\2\2\u00d3\u00d4\7f\2\2\u00d4\u00d5\7g\2\2\u00d5\u00d6" +
                    "\7t\2\2\u00d6\u00d7\7d\2\2\u00d7\u00d8\7{\2\2\u00d8\u00d9\7?\2\2\u00d9" +
                    "\f\3\2\2\2\u00da\u00db\7&\2\2\u00db\u00dc\7t\2\2\u00dc\u00dd\7g\2\2\u00dd" +
                    "\u00de\7h\2\2\u00de\u00df\7A\2\2\u00df\u00e0\7&\2\2\u00e0\u00e1\7k\2\2" +
                    "\u00e1\u00e2\7f\2\2\u00e2\u00e3\7?\2\2\u00e3\16\3\2\2\2\u00e4\u00e5\7" +
                    "&\2\2\u00e5\u00e6\7g\2\2\u00e6\u00e7\7z\2\2\u00e7\u00e8\7r\2\2\u00e8\u00e9" +
                    "\7c\2\2\u00e9\u00ea\7p\2\2\u00ea\u00eb\7f\2\2\u00eb\u00ec\7?\2\2\u00ec" +
                    "\20\3\2\2\2\u00ed\u00ee\7&\2\2\u00ee\u00ef\7e\2\2\u00ef\u00f0\7q\2\2\u00f0" +
                    "\u00f1\7w\2\2\u00f1\u00f2\7p\2\2\u00f2\u00f3\7v\2\2\u00f3\u00f4\7?\2\2" +
                    "\u00f4\22\3\2\2\2\u00f5\u00f6\7&\2\2\u00f6\u00f7\7u\2\2\u00f7\u00f8\7" +
                    "g\2\2\u00f8\u00f9\7n\2\2\u00f9\u00fa\7g\2\2\u00fa\u00fb\7e\2\2\u00fb\u00fc" +
                    "\7v\2\2\u00fc\u00fd\7?\2\2\u00fd\24\3\2\2\2\u00fe\u00ff\7&\2\2\u00ff\u0100" +
                    "\7k\2\2\u0100\u0101\7f\2\2\u0101\u0102\7?\2\2\u0102\26\3\2\2\2\u0103\u0104" +
                    "\7e\2\2\u0104\u0105\7q\2\2\u0105\u0106\7p\2\2\u0106\u0107\7v\2\2\u0107" +
                    "\u0108\7c\2\2\u0108\u0109\7k\2\2\u0109\u010a\7p\2\2\u010a\u010b\7u\2\2" +
                    "\u010b\30\3\2\2\2\u010c\u010d\7u\2\2\u010d\u010e\7v\2\2\u010e\u010f\7" +
                    "c\2\2\u010f\u0110\7t\2\2\u0110\u0111\7v\2\2\u0111\u0112\7u\2\2\u0112\u0113" +
                    "\7y\2\2\u0113\u0114\7k\2\2\u0114\u0115\7v\2\2\u0115\u0116\7j\2\2\u0116" +
                    "\32\3\2\2\2\u0117\u0118\7g\2\2\u0118\u0119\7p\2\2\u0119\u011a\7f\2\2\u011a" +
                    "\u011b\7u\2\2\u011b\u011c\7y\2\2\u011c\u011d\7k\2\2\u011d\u011e\7v\2\2" +
                    "\u011e\u011f\7j\2\2\u011f\34\3\2\2\2\u0120\u0121\7u\2\2\u0121\u0122\7" +
                    "w\2\2\u0122\u0123\7d\2\2\u0123\u0124\7u\2\2\u0124\u0125\7v\2\2\u0125\u0126" +
                    "\7t\2\2\u0126\u0127\7k\2\2\u0127\u0128\7p\2\2\u0128\u0129\7i\2\2\u0129" +
                    "\u012a\7q\2\2\u012a\u012b\7h\2\2\u012b\36\3\2\2\2\u012c\u012d\7n\2\2\u012d" +
                    "\u012e\7g\2\2\u012e\u012f\7p\2\2\u012f\u0130\7i\2\2\u0130\u0131\7v\2\2" +
                    "\u0131\u0132\7j\2\2\u0132 \3\2\2\2\u0133\u0134\7k\2\2\u0134\u0135\7p\2" +
                    "\2\u0135\u0136\7f\2\2\u0136\u0137\7g\2\2\u0137\u0138\7z\2\2\u0138\u0139" +
                    "\7q\2\2\u0139\u013a\7h\2\2\u013a\"\3\2\2\2\u013b\u013c\7t\2\2\u013c\u013d" +
                    "\7g\2\2\u013d\u013e\7r\2\2\u013e\u013f\7n\2\2\u013f\u0140\7c\2\2\u0140" +
                    "\u0141\7e\2\2\u0141\u0142\7g\2\2\u0142$\3\2\2\2\u0143\u0144\7u\2\2\u0144" +
                    "\u0145\7w\2\2\u0145\u0146\7d\2\2\u0146\u0147\7u\2\2\u0147\u0148\7v\2\2" +
                    "\u0148\u0149\7t\2\2\u0149\u014a\7k\2\2\u014a\u014b\7p\2\2\u014b\u014c" +
                    "\7i\2\2\u014c&\3\2\2\2\u014d\u014e\7v\2\2\u014e\u014f\7q\2\2\u014f\u0150" +
                    "\7n\2\2\u0150\u0151\7q\2\2\u0151\u0152\7y\2\2\u0152\u0153\7g\2\2\u0153" +
                    "\u0154\7t\2\2\u0154(\3\2\2\2\u0155\u0156\7v\2\2\u0156\u0157\7q\2\2\u0157" +
                    "\u0158\7w\2\2\u0158\u0159\7r\2\2\u0159\u015a\7r\2\2\u015a\u015b\7g\2\2" +
                    "\u015b\u015c\7t\2\2\u015c*\3\2\2\2\u015d\u015e\7v\2\2\u015e\u015f\7t\2" +
                    "\2\u015f\u0160\7k\2\2\u0160\u0161\7o\2\2\u0161,\3\2\2\2\u0162\u0163\7" +
                    "e\2\2\u0163\u0164\7q\2\2\u0164\u0165\7p\2\2\u0165\u0166\7e\2\2\u0166\u0167" +
                    "\7c\2\2\u0167\u0168\7v\2\2\u0168.\3\2\2\2\u0169\u016a\7f\2\2\u016a\u016b" +
                    "\7c\2\2\u016b\u016c\7{\2\2\u016c\60\3\2\2\2\u016d\u016e\7o\2\2\u016e\u016f" +
                    "\7q\2\2\u016f\u0170\7p\2\2\u0170\u0171\7v\2\2\u0171\u0172\7j\2\2\u0172" +
                    "\62\3\2\2\2\u0173\u0174\7{\2\2\u0174\u0175\7g\2\2\u0175\u0176\7c\2\2\u0176" +
                    "\u0177\7t\2\2\u0177\64\3\2\2\2\u0178\u0179\7j\2\2\u0179\u017a\7q\2\2\u017a" +
                    "\u017b\7w\2\2\u017b\u017c\7t\2\2\u017c\66\3\2\2\2\u017d\u017e\7o\2\2\u017e" +
                    "\u017f\7k\2\2\u017f\u0180\7p\2\2\u0180\u0181\7w\2\2\u0181\u0182\7v\2\2" +
                    "\u0182\u0183\7g\2\2\u01838\3\2\2\2\u0184\u0185\7u\2\2\u0185\u0186\7g\2" +
                    "\2\u0186\u0187\7e\2\2\u0187\u0188\7q\2\2\u0188\u0189\7p\2\2\u0189\u018a" +
                    "\7f\2\2\u018a:\3\2\2\2\u018b\u018c\7p\2\2\u018c\u018d\7q\2\2\u018d\u018e" +
                    "\7y\2\2\u018e<\3\2\2\2\u018f\u0190\7o\2\2\u0190\u0191\7c\2\2\u0191\u0192" +
                    "\7z\2\2\u0192\u0193\7f\2\2\u0193\u0194\7c\2\2\u0194\u0195\7v\2\2\u0195" +
                    "\u0196\7g\2\2\u0196\u0197\7v\2\2\u0197\u0198\7k\2\2\u0198\u0199\7o\2\2" +
                    "\u0199\u019a\7g\2\2\u019a>\3\2\2\2\u019b\u019c\7o\2\2\u019c\u019d\7k\2" +
                    "\2\u019d\u019e\7p\2\2\u019e\u019f\7f\2\2\u019f\u01a0\7c\2\2\u01a0\u01a1" +
                    "\7v\2\2\u01a1\u01a2\7g\2\2\u01a2\u01a3\7v\2\2\u01a3\u01a4\7k\2\2\u01a4" +
                    "\u01a5\7o\2\2\u01a5\u01a6\7g\2\2\u01a6@\3\2\2\2\u01a7\u01a8\7v\2\2\u01a8" +
                    "\u01a9\7q\2\2\u01a9\u01aa\7v\2\2\u01aa\u01ab\7c\2\2\u01ab\u01ac\7n\2\2" +
                    "\u01ac\u01ad\7q\2\2\u01ad\u01ae\7h\2\2\u01ae\u01af\7h\2\2\u01af\u01b0" +
                    "\7u\2\2\u01b0\u01b1\7g\2\2\u01b1\u01b2\7v\2\2\u01b2\u01b3\7o\2\2\u01b3" +
                    "\u01b4\7k\2\2\u01b4\u01b5\7p\2\2\u01b5\u01b6\7w\2\2\u01b6\u01b7\7v\2\2" +
                    "\u01b7\u01b8\7g\2\2\u01b8\u01b9\7u\2\2\u01b9B\3\2\2\2\u01ba\u01bb\7t\2" +
                    "\2\u01bb\u01bc\7q\2\2\u01bc\u01bd\7w\2\2\u01bd\u01be\7p\2\2\u01be\u01bf" +
                    "\7f\2\2\u01bfD\3\2\2\2\u01c0\u01c1\7h\2\2\u01c1\u01c2\7n\2\2\u01c2\u01c3" +
                    "\7q\2\2\u01c3\u01c4\7q\2\2\u01c4\u01c5\7t\2\2\u01c5F\3\2\2\2\u01c6\u01c7" +
                    "\7e\2\2\u01c7\u01c8\7g\2\2\u01c8\u01c9\7k\2\2\u01c9\u01ca\7n\2\2\u01ca" +
                    "\u01cb\7k\2\2\u01cb\u01cc\7p\2\2\u01cc\u01cd\7i\2\2\u01cdH\3\2\2\2\u01ce" +
                    "\u01cf\7g\2\2\u01cf\u01d0\7s\2\2\u01d0J\3\2\2\2\u01d1\u01d2\7i\2\2\u01d2" +
                    "\u01d3\7v\2\2\u01d3L\3\2\2\2\u01d4\u01d5\7n\2\2\u01d5\u01d6\7v\2\2\u01d6" +
                    "N\3\2\2\2\u01d7\u01d8\7i\2\2\u01d8\u01d9\7g\2\2\u01d9P\3\2\2\2\u01da\u01db" +
                    "\7n\2\2\u01db\u01dc\7g\2\2\u01dcR\3\2\2\2\u01dd\u01de\7p\2\2\u01de\u01df" +
                    "\7g\2\2\u01dfT\3\2\2\2\u01e0\u01e1\7q\2\2\u01e1\u01e2\7t\2\2\u01e2\u01e3" +
                    "\7f\2\2\u01e3\u01e4\7g\2\2\u01e4\u01e5\7t\2\2\u01e5V\3\2\2\2\u01e6\u01e7" +
                    "\7d\2\2\u01e7\u01e8\7{\2\2\u01e8X\3\2\2\2\u01e9\u01ea\7f\2\2\u01ea\u01eb" +
                    "\7g\2\2\u01eb\u01ec\7u\2\2\u01ec\u01ed\7e\2\2\u01edZ\3\2\2\2\u01ee\u01ef" +
                    "\7c\2\2\u01ef\u01f0\7u\2\2\u01f0\u01f1\7e\2\2\u01f1\\\3\2\2\2\u01f2\u01f3" +
                    "\7q\2\2\u01f3\u01f4\7t\2\2\u01f4^\3\2\2\2\u01f5\u01f6\7c\2\2\u01f6\u01f7" +
                    "\7p\2\2\u01f7\u01f8\7f\2\2\u01f8`\3\2\2\2\u01f9\u01fa\7p\2\2\u01fa\u01fb" +
                    "\7q\2\2\u01fb\u01fc\7v\2\2\u01fcb\3\2\2\2\u01fd\u01fe\7<\2\2\u01fe\u01ff" +
                    "\7\61\2\2\u01ff\u0200\7\61\2\2\u0200d\3\2\2\2\u0201\u0202\7j\2\2\u0202" +
                    "\u0203\7v\2\2\u0203\u0204\7v\2\2\u0204\u0205\7r\2\2\u0205f\3\2\2\2\u0206" +
                    "\u0207\7j\2\2\u0207\u0208\7v\2\2\u0208\u0209\7v\2\2\u0209\u020a\7r\2\2" +
                    "\u020a\u020b\7u\2\2\u020bh\3\2\2\2\u020c\u020d\7*\2\2\u020dj\3\2\2\2\u020e" +
                    "\u020f\7+\2\2\u020fl\3\2\2\2\u0210\u0211\7.\2\2\u0211n\3\2\2\2\u0212\u0213" +
                    "\7A\2\2\u0213p\3\2\2\2\u0214\u0215\7=\2\2\u0215r\3\2\2\2\u0216\u0217\7" +
                    "B\2\2\u0217t\3\2\2\2\u0218\u0219\7)\2\2\u0219v\3\2\2\2\u021a\u021b\7$" +
                    "\2\2\u021bx\3\2\2\2\u021c\u021d\7b\2\2\u021dz\3\2\2\2\u021e\u021f\7<\2" +
                    "\2\u021f|\3\2\2\2\u0220\u0221\7(\2\2\u0221~\3\2\2\2\u0222\u0223\7P\2\2" +
                    "\u0223\u0224\7W\2\2\u0224\u0225\7N\2\2\u0225\u0226\7N\2\2\u0226\u0080" +
                    "\3\2\2\2\u0227\u0228\7^\2\2\u0228\u0229\7P\2\2\u0229\u0082\3\2\2\2\u022a" +
                    "\u022b\7\60\2\2\u022b\u0084\3\2\2\2\u022c\u022d\7\61\2\2\u022d\u0086\3" +
                    "\2\2\2\u022e\u022f\5\u00afX\2\u022f\u0230\5\u00afX\2\u0230\u0231\7/\2" +
                    "\2\u0231\u0232\5\u00afX\2\u0232\u0233\7/\2\2\u0233\u0234\5\u00afX\2\u0234" +
                    "\u0235\7/\2\2\u0235\u0236\5\u00afX\2\u0236\u0237\7/\2\2\u0237\u0238\5" +
                    "\u00afX\2\u0238\u0239\5\u00afX\2\u0239\u023a\5\u00afX\2\u023a\u0088\3" +
                    "\2\2\2\u023b\u023d\5\u00a1Q\2\u023c\u023b\3\2\2\2\u023d\u023e\3\2\2\2" +
                    "\u023e\u023c\3\2\2\2\u023e\u023f\3\2\2\2\u023f\u0241\3\2\2\2\u0240\u0242" +
                    "\7/\2\2\u0241\u0240\3\2\2\2\u0241\u0242\3\2\2\2\u0242\u0246\3\2\2\2\u0243" +
                    "\u0245\5\u00a1Q\2\u0244\u0243\3\2\2\2\u0245\u0248\3\2\2\2\u0246\u0244" +
                    "\3\2\2\2\u0246\u0247\3\2\2\2\u0247\u008a\3\2\2\2\u0248\u0246\3\2\2\2\u0249" +
                    "\u024b\5\u00abV\2\u024a\u0249\3\2\2\2\u024b\u024c\3\2\2\2\u024c\u024a" +
                    "\3\2\2\2\u024c\u024d\3\2\2\2\u024d\u008c\3\2\2\2\u024e\u0250\5\u00abV" +
                    "\2\u024f\u024e\3\2\2\2\u0250\u0251\3\2\2\2\u0251\u024f\3\2\2\2\u0251\u0252" +
                    "\3\2\2\2\u0252\u0253\3\2\2\2\u0253\u0255\5\u0083B\2\u0254\u0256\5\u00ab" +
                    "V\2\u0255\u0254\3\2\2\2\u0256\u0257\3\2\2\2\u0257\u0255\3\2\2\2\u0257" +
                    "\u0258\3\2\2\2\u0258\u008e\3\2\2\2\u0259\u025a\5\u00a1Q\2\u025a\u0090" +
                    "\3\2\2\2\u025b\u025c\5\u00a5S\2\u025c\u0092\3\2\2\2\u025d\u0260\5\u00a3" +
                    "R\2\u025e\u0260\5\u00a5S\2\u025f\u025d\3\2\2\2\u025f\u025e\3\2\2\2\u0260" +
                    "\u0094\3\2\2\2\u0261\u0263\5\u00abV\2\u0262\u0261\3\2\2\2\u0263\u0264" +
                    "\3\2\2\2\u0264\u0262\3\2\2\2\u0264\u0265\3\2\2\2\u0265\u0096\3\2\2\2\u0266" +
                    "\u0267\7Z\2\2\u0267\u026b\7)\2\2\u0268\u0269\5\u00a9U\2\u0269\u026a\5" +
                    "\u00a9U\2\u026a\u026c\3\2\2\2\u026b\u0268\3\2\2\2\u026c\u026d\3\2\2\2" +
                    "\u026d\u026b\3\2\2\2\u026d\u026e\3\2\2\2\u026e\u026f\3\2\2\2\u026f\u0270" +
                    "\7)\2\2\u0270\u027a\3\2\2\2\u0271\u0272\7\62\2\2\u0272\u0273\7Z\2\2\u0273" +
                    "\u0275\3\2\2\2\u0274\u0276\5\u00a9U\2\u0275\u0274\3\2\2\2\u0276\u0277" +
                    "\3\2\2\2\u0277\u0275\3\2\2\2\u0277\u0278\3\2\2\2\u0278\u027a\3\2\2\2\u0279" +
                    "\u0266\3\2\2\2\u0279\u0271\3\2\2\2\u027a\u0098\3\2\2\2\u027b\u027c\7\61" +
                    "\2\2\u027c\u027d\7q\2\2\u027d\u027e\7f\2\2\u027e\u027f\7c\2\2\u027f\u0280" +
                    "\7v\2\2\u0280\u0281\7c\2\2\u0281\u0282\7\61\2\2\u0282\u0283\7c\2\2\u0283" +
                    "\u0284\7t\2\2\u0284\u0285\7m\2\2\u0285\u0286\7k\2\2\u0286\u0287\7x\2\2" +
                    "\u0287\u0288\7u\2\2\u0288\u0289\7v\2\2\u0289\u028a\7t\2\2\u028a\u028b" +
                    "\7w\2\2\u028b\u028c\7m\2\2\u028c\u028d\7v\2\2\u028d\u028e\7w\2\2\u028e" +
                    "\u028f\7t\2\2\u028f\u009a\3\2\2\2\u0290\u0291\7\'\2\2\u0291\u0292\t\3" +
                    "\2\2\u0292\u0294\t\3\2\2\u0293\u0290\3\2\2\2\u0294\u0295\3\2\2\2\u0295" +
                    "\u0293\3\2\2\2\u0295\u0296\3\2\2\2\u0296\u009c\3\2\2\2\u0297\u029a\t\4" +
                    "\2\2\u0298\u029a\5\u009bN\2\u0299\u0297\3\2\2\2\u0299\u0298\3\2\2\2\u029a" +
                    "\u029f\3\2\2\2\u029b\u029e\t\5\2\2\u029c\u029e\5\u009bN\2\u029d\u029b" +
                    "\3\2\2\2\u029d\u029c\3\2\2\2\u029e\u02a1\3\2\2\2\u029f\u029d\3\2\2\2\u029f" +
                    "\u02a0\3\2\2\2\u02a0\u009e\3\2\2\2\u02a1\u029f\3\2\2\2\u02a2\u02a4\7G" +
                    "\2\2\u02a3\u02a5\t\6\2\2\u02a4\u02a3\3\2\2\2\u02a4\u02a5\3\2\2\2\u02a5" +
                    "\u02a7\3\2\2\2\u02a6\u02a8\5\u00abV\2\u02a7\u02a6\3\2\2\2\u02a8\u02a9" +
                    "\3\2\2\2\u02a9\u02a7\3\2\2\2\u02a9\u02aa\3\2\2\2\u02aa\u00a0\3\2\2\2\u02ab" +
                    "\u02ad\t\7\2\2\u02ac\u02ab\3\2\2\2\u02ad\u02b0\3\2\2\2\u02ae\u02af\3\2" +
                    "\2\2\u02ae\u02ac\3\2\2\2\u02af\u02b2\3\2\2\2\u02b0\u02ae\3\2\2\2\u02b1" +
                    "\u02b3\t\b\2\2\u02b2\u02b1\3\2\2\2\u02b3\u02b4\3\2\2\2\u02b4\u02b5\3\2" +
                    "\2\2\u02b4\u02b2\3\2\2\2\u02b5\u02b9\3\2\2\2\u02b6\u02b8\t\7\2\2\u02b7" +
                    "\u02b6\3\2\2\2\u02b8\u02bb\3\2\2\2\u02b9\u02b7\3\2\2\2\u02b9\u02ba\3\2" +
                    "\2\2\u02ba\u00a2\3\2\2\2\u02bb\u02b9\3\2\2\2\u02bc\u02c4\7$\2\2\u02bd" +
                    "\u02be\7^\2\2\u02be\u02c3\13\2\2\2\u02bf\u02c0\7$\2\2\u02c0\u02c3\7$\2" +
                    "\2\u02c1\u02c3\n\t\2\2\u02c2\u02bd\3\2\2\2\u02c2\u02bf\3\2\2\2\u02c2\u02c1" +
                    "\3\2\2\2\u02c3\u02c6\3\2\2\2\u02c4\u02c2\3\2\2\2\u02c4\u02c5\3\2\2\2\u02c5" +
                    "\u02c7\3\2\2\2\u02c6\u02c4\3\2\2\2\u02c7\u02c8\7$\2\2\u02c8\u00a4\3\2" +
                    "\2\2\u02c9\u02d1\7)\2\2\u02ca\u02cb\7^\2\2\u02cb\u02d0\13\2\2\2\u02cc" +
                    "\u02cd\7)\2\2\u02cd\u02d0\7)\2\2\u02ce\u02d0\n\n\2\2\u02cf\u02ca\3\2\2" +
                    "\2\u02cf\u02cc\3\2\2\2\u02cf\u02ce\3\2\2\2\u02d0\u02d3\3\2\2\2\u02d1\u02cf" +
                    "\3\2\2\2\u02d1\u02d2\3\2\2\2\u02d2\u02d4\3\2\2\2\u02d3\u02d1\3\2\2\2\u02d4" +
                    "\u02d5\7)\2\2\u02d5\u00a6\3\2\2\2\u02d6\u02de\7b\2\2\u02d7\u02d8\7^\2" +
                    "\2\u02d8\u02dd\13\2\2\2\u02d9\u02da\7b\2\2\u02da\u02dd\7b\2\2\u02db\u02dd" +
                    "\n\13\2\2\u02dc\u02d7\3\2\2\2\u02dc\u02d9\3\2\2\2\u02dc\u02db\3\2\2\2" +
                    "\u02dd\u02e0\3\2\2\2\u02de\u02dc\3\2\2\2\u02de\u02df\3\2\2\2\u02df\u02e1" +
                    "\3\2\2\2\u02e0\u02de\3\2\2\2\u02e1\u02e2\7b\2\2\u02e2\u00a8\3\2\2\2\u02e3" +
                    "\u02e4\t\f\2\2\u02e4\u00aa\3\2\2\2\u02e5\u02e6\t\r\2\2\u02e6\u00ac\3\2" +
                    "\2\2\u02e7\u02e8\7D\2\2\u02e8\u02ea\7)\2\2\u02e9\u02eb\t\16\2\2\u02ea" +
                    "\u02e9\3\2\2\2\u02eb\u02ec\3\2\2\2\u02ec\u02ea\3\2\2\2\u02ec\u02ed\3\2" +
                    "\2\2\u02ed\u02ee\3\2\2\2\u02ee\u02ef\7)\2\2\u02ef\u00ae\3\2\2\2\u02f0" +
                    "\u02f1\t\17\2\2\u02f1\u02f2\t\17\2\2\u02f2\u02f3\t\17\2\2\u02f3\u02f4" +
                    "\t\17\2\2\u02f4\u00b0\3\2\2\2\u02f5\u02f6\13\2\2\2\u02f6\u02f7\3\2\2\2" +
                    "\u02f7\u02f8\bY\3\2\u02f8\u00b2\3\2\2\2\37\2\u00b6\u023e\u0241\u0246\u024c" +
                    "\u0251\u0257\u025f\u0264\u026d\u0277\u0279\u0295\u0299\u029d\u029f\u02a4" +
                    "\u02a9\u02ae\u02b4\u02b9\u02c2\u02c4\u02cf\u02d1\u02dc\u02de\u02ec\4\2" +
                    "\3\2\2\4\2";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    private static final String[] _LITERAL_NAMES = makeLiteralNames();
    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);
    public static String[] channelNames = {
            "DEFAULT_TOKEN_CHANNEL", "HIDDEN", "ERRORCHANNEL"
    };
    public static String[] modeNames = {
            "DEFAULT_MODE"
    };

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

    public ODataLexer(CharStream input) {
        super(input);
        _interp = new LexerATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    private static String[] makeRuleNames() {
        return new String[]{
                "SPACE", "FILTER", "TOP", "SKIPRULE", "ORDERBY", "REF", "EXPAND", "COUNT",
                "SELECT", "DOLLARID", "CONTAINS", "STARTSWITH", "ENDSWITH", "SUBSTRINGOF",
                "LENGTH", "INDEXOF", "REPLACE", "SUBSTRING", "TOLOWER", "TOUPPER", "TRIM",
                "CONCAT", "DAY", "MONTH", "YEAR", "HOUR", "MINUTE", "SECOND", "NOW",
                "MAXDATETIME", "MINDATETIME", "TOTALOFFSETMINUTES", "ROUND", "FLOOR",
                "CEILING", "EQ", "GT", "LT", "GE", "LE", "NE", "ORDER", "BY", "DESC",
                "ASC", "OR", "AND", "NOT", "SEPERATOR", "HTTP", "HTTPS", "LR_BRACKET",
                "RR_BRACKET", "COMMA", "QUESTION", "SEMI", "AT_SIGN", "SINGLE_QUOTE_SYMB",
                "DOUBLE_QUOTE_SYMB", "REVERSE_QUOTE_SYMB", "COLON_SYMB", "AMPERSAND",
                "NULL_LITERAL", "NULL_SPEC_LITERAL", "DOT", "SLASH", "UUID", "NOARK_ENTITY",
                "INTEGER", "FLOAT", "ID", "QUOTED_STRING", "STRING_LITERAL", "DECIMAL_LITERAL",
                "HEXADECIMAL_LITERAL", "ODATA_ARK", "HEX", "STRING", "EXPONENT_NUM_PART",
                "ID_LITERAL", "DQUOTA_STRING", "SQUOTA_STRING", "BQUOTA_STRING", "HEX_DIGIT",
                "DEC_DIGIT", "BIT_STRING_L", "BLOCK", "ERROR_RECONGNIGION"
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
        return "ODataLexer.g4";
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
    public String[] getChannelNames() {
        return channelNames;
    }

    @Override
    public String[] getModeNames() {
        return modeNames;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }
}
