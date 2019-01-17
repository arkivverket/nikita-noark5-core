// Generated from /home/tsodring/git/nikita-noark5-core/src/main/resources/odata/OData.g4 by ANTLR 4.7
package nikita.webapp.odata;

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
			T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, T__4 = 5, T__5 = 6, T__6 = 7, T__7 = 8, T__8 = 9,
			T__9 = 10, T__10 = 11, T__11 = 12, T__12 = 13, T__13 = 14, T__14 = 15, T__15 = 16, WHITESPACE = 17,
			EQ = 18, GT = 19, LT = 20, GE = 21, LE = 22, AND = 23, OR = 24, ASC = 25, DESC = 26, TOP = 27,
			SKIP_ = 28, COUNT = 29, ORDERBY = 30, WS = 31, DIGITS = 32, HEX = 33, STRING = 34, STRING_CONTENT = 35,
			UUID = 36, COLON = 37, SEPERATOR = 38;
	public static final int
			RULE_odataURL = 0, RULE_scheme = 1, RULE_host = 2, RULE_slash = 3, RULE_contextPath = 4,
			RULE_api = 5, RULE_functionality = 6, RULE_parentResource = 7, RULE_resource = 8,
			RULE_systemId = 9, RULE_port = 10, RULE_fromContextPath = 11, RULE_nikitaObjects = 12,
			RULE_odataCommand = 13, RULE_filter = 14, RULE_count = 15, RULE_search = 16,
			RULE_top = 17, RULE_skip = 18, RULE_orderby = 19, RULE_searchCommand = 20,
			RULE_filterCommand = 21, RULE_command = 22, RULE_comparatorCommand = 23,
			RULE_contains = 24, RULE_startsWith = 25, RULE_attribute = 26, RULE_value = 27,
			RULE_sortOrder = 28, RULE_comparator = 29, RULE_operator = 30, RULE_leftCurlyBracket = 31,
			RULE_rightCurlyBracket = 32, RULE_and = 33, RULE_or = 34, RULE_eq = 35,
			RULE_gt = 36, RULE_lt = 37, RULE_ge = 38, RULE_le = 39, RULE_string = 40,
			RULE_stringContent = 41, RULE_number = 42, RULE_uuid = 43, RULE_asc = 44,
			RULE_desc = 45;
	public static final String[] ruleNames = {
			"odataURL", "scheme", "host", "slash", "contextPath", "api", "functionality",
			"parentResource", "resource", "systemId", "port", "fromContextPath", "nikitaObjects",
			"odataCommand", "filter", "count", "search", "top", "skip", "orderby",
			"searchCommand", "filterCommand", "command", "comparatorCommand", "contains",
			"startsWith", "attribute", "value", "sortOrder", "comparator", "operator",
			"leftCurlyBracket", "rightCurlyBracket", "and", "or", "eq", "gt", "lt",
			"ge", "le", "string", "stringContent", "number", "uuid", "asc", "desc"
	};
	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	public static final String _serializedATN =
			"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3(\u010c\4\2\t\2\4" +
					"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t" +
					"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22" +
					"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31" +
					"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!" +
					"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4" +
					",\t,\4-\t-\4.\t.\4/\t/\3\2\3\2\3\2\3\2\3\2\5\2d\n\2\3\2\3\2\3\3\3\3\3" +
					"\4\3\4\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13" +
					"\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16" +
					"\3\16\3\16\5\16\u008e\n\16\3\17\3\17\3\17\3\17\3\17\7\17\u0095\n\17\f" +
					"\17\16\17\u0098\13\17\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\23\3\23" +
					"\3\23\3\24\3\24\3\24\3\25\3\25\3\25\5\25\u00ab\n\25\3\25\3\25\5\25\u00af" +
					"\n\25\7\25\u00b1\n\25\f\25\16\25\u00b4\13\25\3\26\3\26\3\27\3\27\5\27" +
					"\u00ba\n\27\3\27\3\27\3\27\5\27\u00bf\n\27\3\30\3\30\5\30\u00c3\n\30\3" +
					"\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\32\3\33\3" +
					"\33\3\33\3\33\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\5" +
					"\36\u00e1\n\36\3\37\3\37\3\37\3\37\3\37\5\37\u00e8\n\37\3 \3 \5 \u00ec" +
					"\n \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3" +
					"+\3+\3,\3,\3-\3-\3.\3.\3/\3/\3/\2\2\60\2\4\6\b\n\f\16\20\22\24\26\30\32" +
					"\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVXZ\\\2\3\3\2\3\4\2\u00f0\2" +
					"^\3\2\2\2\4g\3\2\2\2\6i\3\2\2\2\bk\3\2\2\2\nm\3\2\2\2\fp\3\2\2\2\16s\3" +
					"\2\2\2\20v\3\2\2\2\22x\3\2\2\2\24z\3\2\2\2\26|\3\2\2\2\30~\3\2\2\2\32" +
					"\u008d\3\2\2\2\34\u0096\3\2\2\2\36\u0099\3\2\2\2 \u009c\3\2\2\2\"\u009e" +
					"\3\2\2\2$\u00a1\3\2\2\2&\u00a4\3\2\2\2(\u00a7\3\2\2\2*\u00b5\3\2\2\2," +
					"\u00b9\3\2\2\2.\u00c2\3\2\2\2\60\u00c4\3\2\2\2\62\u00c8\3\2\2\2\64\u00d1" +
					"\3\2\2\2\66\u00da\3\2\2\28\u00dc\3\2\2\2:\u00e0\3\2\2\2<\u00e7\3\2\2\2" +
					">\u00eb\3\2\2\2@\u00ed\3\2\2\2B\u00ef\3\2\2\2D\u00f1\3\2\2\2F\u00f3\3" +
					"\2\2\2H\u00f5\3\2\2\2J\u00f7\3\2\2\2L\u00f9\3\2\2\2N\u00fb\3\2\2\2P\u00fd" +
					"\3\2\2\2R\u00ff\3\2\2\2T\u0101\3\2\2\2V\u0103\3\2\2\2X\u0105\3\2\2\2Z" +
					"\u0107\3\2\2\2\\\u0109\3\2\2\2^_\5\4\3\2_`\7(\2\2`c\5\6\4\2ab\7\'\2\2" +
					"bd\5\26\f\2ca\3\2\2\2cd\3\2\2\2de\3\2\2\2ef\5\30\r\2f\3\3\2\2\2gh\t\2" +
					"\2\2h\5\3\2\2\2ij\5R*\2j\7\3\2\2\2kl\7\5\2\2l\t\3\2\2\2mn\7\5\2\2no\5" +
					"R*\2o\13\3\2\2\2pq\7\5\2\2qr\5R*\2r\r\3\2\2\2st\7\5\2\2tu\5R*\2u\17\3" +
					"\2\2\2vw\5R*\2w\21\3\2\2\2xy\5R*\2y\23\3\2\2\2z{\5X-\2{\25\3\2\2\2|}\7" +
					"\"\2\2}\27\3\2\2\2~\177\5\n\6\2\177\u0080\5\f\7\2\u0080\u0081\5\16\b\2" +
					"\u0081\u0082\7\5\2\2\u0082\u0083\5\32\16\2\u0083\u0084\7\6\2\2\u0084\u0085" +
					"\5\34\17\2\u0085\31\3\2\2\2\u0086\u0087\5\20\t\2\u0087\u0088\7\5\2\2\u0088" +
					"\u0089\5\24\13\2\u0089\u008a\7\5\2\2\u008a\u008b\5\22\n\2\u008b\u008e" +
					"\3\2\2\2\u008c\u008e\5\22\n\2\u008d\u0086\3\2\2\2\u008d\u008c\3\2\2\2" +
					"\u008e\33\3\2\2\2\u008f\u0095\5\36\20\2\u0090\u0095\5$\23\2\u0091\u0095" +
					"\5&\24\2\u0092\u0095\5(\25\2\u0093\u0095\5 \21\2\u0094\u008f\3\2\2\2\u0094" +
					"\u0090\3\2\2\2\u0094\u0091\3\2\2\2\u0094\u0092\3\2\2\2\u0094\u0093\3\2" +
					"\2\2\u0095\u0098\3\2\2\2\u0096\u0094\3\2\2\2\u0096\u0097\3\2\2\2\u0097" +
					"\35\3\2\2\2\u0098\u0096\3\2\2\2\u0099\u009a\7\7\2\2\u009a\u009b\5,\27" +
					"\2\u009b\37\3\2\2\2\u009c\u009d\7\b\2\2\u009d!\3\2\2\2\u009e\u009f\7\t" +
					"\2\2\u009f\u00a0\5*\26\2\u00a0#\3\2\2\2\u00a1\u00a2\7\n\2\2\u00a2\u00a3" +
					"\5V,\2\u00a3%\3\2\2\2\u00a4\u00a5\7\13\2\2\u00a5\u00a6\5V,\2\u00a6\'\3" +
					"\2\2\2\u00a7\u00a8\7\f\2\2\u00a8\u00aa\5\66\34\2\u00a9\u00ab\5:\36\2\u00aa" +
					"\u00a9\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab\u00b2\3\2\2\2\u00ac\u00ae\5\66" +
					"\34\2\u00ad\u00af\5:\36\2\u00ae\u00ad\3\2\2\2\u00ae\u00af\3\2\2\2\u00af" +
					"\u00b1\3\2\2\2\u00b0\u00ac\3\2\2\2\u00b1\u00b4\3\2\2\2\u00b2\u00b0\3\2" +
					"\2\2\u00b2\u00b3\3\2\2\2\u00b3)\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b5\u00b6" +
					"\5R*\2\u00b6+\3\2\2\2\u00b7\u00ba\5.\30\2\u00b8\u00ba\5\60\31\2\u00b9" +
					"\u00b7\3\2\2\2\u00b9\u00b8\3\2\2\2\u00ba\u00be\3\2\2\2\u00bb\u00bc\5>" +
					" \2\u00bc\u00bd\5,\27\2\u00bd\u00bf\3\2\2\2\u00be\u00bb\3\2\2\2\u00be" +
					"\u00bf\3\2\2\2\u00bf-\3\2\2\2\u00c0\u00c3\5\62\32\2\u00c1\u00c3\5\64\33" +
					"\2\u00c2\u00c0\3\2\2\2\u00c2\u00c1\3\2\2\2\u00c3/\3\2\2\2\u00c4\u00c5" +
					"\5\66\34\2\u00c5\u00c6\5<\37\2\u00c6\u00c7\5T+\2\u00c7\61\3\2\2\2\u00c8" +
					"\u00c9\7\r\2\2\u00c9\u00ca\5@!\2\u00ca\u00cb\5\66\34\2\u00cb\u00cc\7\16" +
					"\2\2\u00cc\u00cd\7\17\2\2\u00cd\u00ce\58\35\2\u00ce\u00cf\7\17\2\2\u00cf" +
					"\u00d0\5B\"\2\u00d0\63\3\2\2\2\u00d1\u00d2\7\20\2\2\u00d2\u00d3\5@!\2" +
					"\u00d3\u00d4\5\66\34\2\u00d4\u00d5\7\16\2\2\u00d5\u00d6\7\17\2\2\u00d6" +
					"\u00d7\58\35\2\u00d7\u00d8\7\17\2\2\u00d8\u00d9\5B\"\2\u00d9\65\3\2\2" +
					"\2\u00da\u00db\5R*\2\u00db\67\3\2\2\2\u00dc\u00dd\5R*\2\u00dd9\3\2\2\2" +
					"\u00de\u00e1\5Z.\2\u00df\u00e1\5\\/\2\u00e0\u00de\3\2\2\2\u00e0\u00df" +
					"\3\2\2\2\u00e1;\3\2\2\2\u00e2\u00e8\5H%\2\u00e3\u00e8\5J&\2\u00e4\u00e8" +
					"\5L\'\2\u00e5\u00e8\5N(\2\u00e6\u00e8\5P)\2\u00e7\u00e2\3\2\2\2\u00e7" +
					"\u00e3\3\2\2\2\u00e7\u00e4\3\2\2\2\u00e7\u00e5\3\2\2\2\u00e7\u00e6\3\2" +
					"\2\2\u00e8=\3\2\2\2\u00e9\u00ec\5D#\2\u00ea\u00ec\5F$\2\u00eb\u00e9\3" +
					"\2\2\2\u00eb\u00ea\3\2\2\2\u00ec?\3\2\2\2\u00ed\u00ee\7\21\2\2\u00eeA" +
					"\3\2\2\2\u00ef\u00f0\7\22\2\2\u00f0C\3\2\2\2\u00f1\u00f2\7\31\2\2\u00f2" +
					"E\3\2\2\2\u00f3\u00f4\7\32\2\2\u00f4G\3\2\2\2\u00f5\u00f6\7\24\2\2\u00f6" +
					"I\3\2\2\2\u00f7\u00f8\7\25\2\2\u00f8K\3\2\2\2\u00f9\u00fa\7\26\2\2\u00fa" +
					"M\3\2\2\2\u00fb\u00fc\7\27\2\2\u00fcO\3\2\2\2\u00fd\u00fe\7\30\2\2\u00fe" +
					"Q\3\2\2\2\u00ff\u0100\7$\2\2\u0100S\3\2\2\2\u0101\u0102\7%\2\2\u0102U" +
					"\3\2\2\2\u0103\u0104\7\"\2\2\u0104W\3\2\2\2\u0105\u0106\7&\2\2\u0106Y" +
					"\3\2\2\2\u0107\u0108\7\33\2\2\u0108[\3\2\2\2\u0109\u010a\7\34\2\2\u010a" +
					"]\3\2\2\2\17c\u008d\u0094\u0096\u00aa\u00ae\u00b2\u00b9\u00be\u00c2\u00e0" +
					"\u00e7\u00eb";
	public static final ATN _ATN =
			new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
			new PredictionContextCache();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);
	private static final String[] _LITERAL_NAMES = {
			null, "'http'", "'https'", "'/'", "'?'", "'$filter='", "'$count'", "'$search='",
			"'$top='", "'$skip='", "'$orderby='", "'contains'", "','", "'''", "'startsWith'",
			"'('", "')'", null, "'eq'", "'gt'", "'lt'", "'ge'", "'le'", "'and'", "'or'",
			"'asc'", "'desc'", "'top'", "'skip'", "'count'", "'orderby'", null, null,
			null, null, null, null, "':'", "'://'"
	};
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

	private static final String[] _SYMBOLIC_NAMES = {
			null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, null, null, "WHITESPACE", "EQ", "GT", "LT", "GE", "LE",
			"AND", "OR", "ASC", "DESC", "TOP", "SKIP_", "COUNT", "ORDERBY", "WS",
			"DIGITS", "HEX", "STRING", "STRING_CONTENT", "UUID", "COLON", "SEPERATOR"
	};

	static {
		RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION);
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

	@Override
	public String getGrammarFileName() {
		return "OData.g4";
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

	public final OdataURLContext odataURL() throws RecognitionException {
		OdataURLContext _localctx = new OdataURLContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_odataURL);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(92);
				scheme();
				setState(93);
				match(SEPERATOR);
				setState(94);
				host();
				setState(97);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == COLON) {
					{
						setState(95);
						match(COLON);
						setState(96);
						port();
					}
				}

				setState(99);
				fromContextPath();
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

	public final SchemeContext scheme() throws RecognitionException {
		SchemeContext _localctx = new SchemeContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_scheme);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(101);
				_la = _input.LA(1);
				if (!(_la == T__0 || _la == T__1)) {
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

	public final HostContext host() throws RecognitionException {
		HostContext _localctx = new HostContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_host);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(103);
				string();
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

	public final SlashContext slash() throws RecognitionException {
		SlashContext _localctx = new SlashContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_slash);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(105);
				match(T__2);
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

	public final ContextPathContext contextPath() throws RecognitionException {
		ContextPathContext _localctx = new ContextPathContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_contextPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(107);
				match(T__2);
				setState(108);
				string();
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

	public final ApiContext api() throws RecognitionException {
		ApiContext _localctx = new ApiContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_api);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(110);
				match(T__2);
				setState(111);
				string();
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

	public final FunctionalityContext functionality() throws RecognitionException {
		FunctionalityContext _localctx = new FunctionalityContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_functionality);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(113);
				match(T__2);
				setState(114);
				string();
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

	public final ParentResourceContext parentResource() throws RecognitionException {
		ParentResourceContext _localctx = new ParentResourceContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_parentResource);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(116);
				string();
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

	public final ResourceContext resource() throws RecognitionException {
		ResourceContext _localctx = new ResourceContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_resource);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(118);
				string();
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

	public final SystemIdContext systemId() throws RecognitionException {
		SystemIdContext _localctx = new SystemIdContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_systemId);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(120);
				uuid();
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

	public final PortContext port() throws RecognitionException {
		PortContext _localctx = new PortContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_port);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(122);
				match(DIGITS);
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

	public final FromContextPathContext fromContextPath() throws RecognitionException {
		FromContextPathContext _localctx = new FromContextPathContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_fromContextPath);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(124);
				contextPath();
				setState(125);
				api();
				setState(126);
				functionality();
				setState(127);
				match(T__2);
				setState(128);
				nikitaObjects();
				setState(129);
				match(T__3);
				setState(130);
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

	public final NikitaObjectsContext nikitaObjects() throws RecognitionException {
		NikitaObjectsContext _localctx = new NikitaObjectsContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_nikitaObjects);
		try {
			setState(139);
			_errHandler.sync(this);
			switch (getInterpreter().adaptivePredict(_input, 1, _ctx)) {
				case 1:
					enterOuterAlt(_localctx, 1);
				{
					{
						setState(132);
						parentResource();
						setState(133);
						match(T__2);
						setState(134);
						systemId();
						setState(135);
						match(T__2);
						setState(136);
						resource();
					}
				}
				break;
				case 2:
					enterOuterAlt(_localctx, 2);
				{
					setState(138);
					resource();
				}
				break;
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
		enterRule(_localctx, 26, RULE_odataCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__4) | (1L << T__5) | (1L << T__7) | (1L << T__8) | (1L << T__9))) != 0)) {
					{
						setState(146);
						_errHandler.sync(this);
						switch (_input.LA(1)) {
							case T__4: {
								setState(141);
								filter();
							}
							break;
							case T__7: {
								setState(142);
								top();
							}
							break;
							case T__8: {
								setState(143);
								skip();
							}
							break;
							case T__9: {
								setState(144);
								orderby();
							}
							break;
							case T__5: {
								setState(145);
								count();
							}
							break;
							default:
								throw new NoViableAltException(this);
						}
					}
					setState(150);
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

	public final FilterContext filter() throws RecognitionException {
		FilterContext _localctx = new FilterContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_filter);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(151);
				match(T__4);
				setState(152);
				filterCommand();
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

	public final CountContext count() throws RecognitionException {
		CountContext _localctx = new CountContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_count);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(154);
				match(T__5);
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

	public final SearchContext search() throws RecognitionException {
		SearchContext _localctx = new SearchContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_search);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(156);
				match(T__6);
				setState(157);
				searchCommand();
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

	public final TopContext top() throws RecognitionException {
		TopContext _localctx = new TopContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_top);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(159);
				match(T__7);
				setState(160);
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

	public final SkipContext skip() throws RecognitionException {
		SkipContext _localctx = new SkipContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_skip);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(162);
				match(T__8);
				setState(163);
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

	public final OrderbyContext orderby() throws RecognitionException {
		OrderbyContext _localctx = new OrderbyContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_orderby);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(165);
				match(T__9);
				setState(166);
				attribute();
				setState(168);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == ASC || _la == DESC) {
					{
						setState(167);
						sortOrder();
					}
				}

				setState(176);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la == STRING) {
					{
						{
							setState(170);
							attribute();
							setState(172);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la == ASC || _la == DESC) {
								{
									setState(171);
									sortOrder();
								}
							}

						}
					}
					setState(178);
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

	public final SearchCommandContext searchCommand() throws RecognitionException {
		SearchCommandContext _localctx = new SearchCommandContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_searchCommand);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(179);
				string();
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

	public final FilterCommandContext filterCommand() throws RecognitionException {
		FilterCommandContext _localctx = new FilterCommandContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_filterCommand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(183);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
					case T__10:
					case T__13: {
						setState(181);
						command();
					}
					break;
					case STRING: {
						setState(182);
						comparatorCommand();
					}
					break;
					default:
						throw new NoViableAltException(this);
				}
				setState(188);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la == AND || _la == OR) {
					{
						setState(185);
						operator();
						setState(186);
						filterCommand();
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

	public final CommandContext command() throws RecognitionException {
		CommandContext _localctx = new CommandContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_command);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(192);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
					case T__10: {
						setState(190);
						contains();
					}
					break;
					case T__13: {
						setState(191);
						startsWith();
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

	public final ComparatorCommandContext comparatorCommand() throws RecognitionException {
		ComparatorCommandContext _localctx = new ComparatorCommandContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_comparatorCommand);
		try {
			enterOuterAlt(_localctx, 1);
			{
				{
					setState(194);
					attribute();
					setState(195);
					comparator();
					setState(196);
					stringContent();
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

	public final ContainsContext contains() throws RecognitionException {
		ContainsContext _localctx = new ContainsContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_contains);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(198);
				match(T__10);
				setState(199);
				leftCurlyBracket();
				setState(200);
				attribute();
				setState(201);
				match(T__11);
				setState(202);
				match(T__12);
				setState(203);
				value();
				setState(204);
				match(T__12);
				setState(205);
				rightCurlyBracket();
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

	public final StartsWithContext startsWith() throws RecognitionException {
		StartsWithContext _localctx = new StartsWithContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_startsWith);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(207);
				match(T__13);
				setState(208);
				leftCurlyBracket();
				setState(209);
				attribute();
				setState(210);
				match(T__11);
				setState(211);
				match(T__12);
				setState(212);
				value();
				setState(213);
				match(T__12);
				setState(214);
				rightCurlyBracket();
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

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(216);
				string();
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
		enterRule(_localctx, 54, RULE_value);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(218);
				string();
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

	public final SortOrderContext sortOrder() throws RecognitionException {
		SortOrderContext _localctx = new SortOrderContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_sortOrder);
		try {
			setState(222);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
				case ASC:
					enterOuterAlt(_localctx, 1);
				{
					setState(220);
					asc();
				}
				break;
				case DESC:
					enterOuterAlt(_localctx, 2);
				{
					setState(221);
					desc();
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

	public final ComparatorContext comparator() throws RecognitionException {
		ComparatorContext _localctx = new ComparatorContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_comparator);
		try {
			setState(229);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
				case EQ:
					enterOuterAlt(_localctx, 1);
				{
					setState(224);
					eq();
				}
				break;
				case GT:
					enterOuterAlt(_localctx, 2);
				{
					setState(225);
					gt();
				}
				break;
				case LT:
					enterOuterAlt(_localctx, 3);
				{
					setState(226);
					lt();
				}
				break;
				case GE:
					enterOuterAlt(_localctx, 4);
				{
					setState(227);
					ge();
				}
				break;
				case LE:
					enterOuterAlt(_localctx, 5);
				{
					setState(228);
					le();
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

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_operator);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(233);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
					case AND: {
						setState(231);
						and();
					}
					break;
					case OR: {
						setState(232);
						or();
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

	public final LeftCurlyBracketContext leftCurlyBracket() throws RecognitionException {
		LeftCurlyBracketContext _localctx = new LeftCurlyBracketContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_leftCurlyBracket);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(235);
				match(T__14);
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

	public final RightCurlyBracketContext rightCurlyBracket() throws RecognitionException {
		RightCurlyBracketContext _localctx = new RightCurlyBracketContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_rightCurlyBracket);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(237);
				match(T__15);
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

	public final AndContext and() throws RecognitionException {
		AndContext _localctx = new AndContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_and);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(239);
				match(AND);
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

	public final OrContext or() throws RecognitionException {
		OrContext _localctx = new OrContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_or);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(241);
				match(OR);
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

	public final EqContext eq() throws RecognitionException {
		EqContext _localctx = new EqContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_eq);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(243);
				match(EQ);
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

	public final GtContext gt() throws RecognitionException {
		GtContext _localctx = new GtContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_gt);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(245);
				match(GT);
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

	public final LtContext lt() throws RecognitionException {
		LtContext _localctx = new LtContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_lt);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(247);
				match(LT);
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

	public final GeContext ge() throws RecognitionException {
		GeContext _localctx = new GeContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_ge);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(249);
				match(GE);
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

	public final LeContext le() throws RecognitionException {
		LeContext _localctx = new LeContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_le);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(251);
				match(LE);
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

	public final StringContext string() throws RecognitionException {
		StringContext _localctx = new StringContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_string);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(253);
				match(STRING);
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

	public final StringContentContext stringContent() throws RecognitionException {
		StringContentContext _localctx = new StringContentContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_stringContent);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(255);
				match(STRING_CONTENT);
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

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_number);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(257);
				match(DIGITS);
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

	public final UuidContext uuid() throws RecognitionException {
		UuidContext _localctx = new UuidContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_uuid);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(259);
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

	public final AscContext asc() throws RecognitionException {
		AscContext _localctx = new AscContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_asc);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(261);
				match(ASC);
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

	public final DescContext desc() throws RecognitionException {
		DescContext _localctx = new DescContext(_ctx, getState());
		enterRule(_localctx, 90, RULE_desc);
		try {
			enterOuterAlt(_localctx, 1);
			{
				setState(263);
				match(DESC);
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

	public static class OdataURLContext extends ParserRuleContext {
		public OdataURLContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public SchemeContext scheme() {
			return getRuleContext(SchemeContext.class, 0);
		}

		public TerminalNode SEPERATOR() {
			return getToken(ODataParser.SEPERATOR, 0);
		}

		public HostContext host() {
			return getRuleContext(HostContext.class, 0);
		}

		public FromContextPathContext fromContextPath() {
			return getRuleContext(FromContextPathContext.class, 0);
		}

		public TerminalNode COLON() {
			return getToken(ODataParser.COLON, 0);
		}

		public PortContext port() {
			return getRuleContext(PortContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_odataURL;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterOdataURL(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitOdataURL(this);
		}
	}

	public static class SchemeContext extends ParserRuleContext {
		public SchemeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_scheme;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterScheme(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitScheme(this);
		}
	}

	public static class HostContext extends ParserRuleContext {
		public HostContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public StringContext string() {
			return getRuleContext(StringContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_host;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterHost(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitHost(this);
		}
	}

	public static class SlashContext extends ParserRuleContext {
		public SlashContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_slash;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterSlash(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitSlash(this);
		}
	}

	public static class ContextPathContext extends ParserRuleContext {
		public ContextPathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public StringContext string() {
			return getRuleContext(StringContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_contextPath;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterContextPath(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitContextPath(this);
		}
	}

	public static class ApiContext extends ParserRuleContext {
		public ApiContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public StringContext string() {
			return getRuleContext(StringContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_api;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterApi(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitApi(this);
		}
	}

	public static class FunctionalityContext extends ParserRuleContext {
		public FunctionalityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public StringContext string() {
			return getRuleContext(StringContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_functionality;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterFunctionality(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitFunctionality(this);
		}
	}

	public static class ParentResourceContext extends ParserRuleContext {
		public ParentResourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public StringContext string() {
			return getRuleContext(StringContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_parentResource;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterParentResource(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitParentResource(this);
		}
	}

	public static class ResourceContext extends ParserRuleContext {
		public ResourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public StringContext string() {
			return getRuleContext(StringContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_resource;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterResource(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitResource(this);
		}
	}

	public static class SystemIdContext extends ParserRuleContext {
		public SystemIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public UuidContext uuid() {
			return getRuleContext(UuidContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_systemId;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterSystemId(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitSystemId(this);
		}
	}

	public static class PortContext extends ParserRuleContext {
		public PortContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode DIGITS() {
			return getToken(ODataParser.DIGITS, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_port;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterPort(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitPort(this);
		}
	}

	public static class FromContextPathContext extends ParserRuleContext {
		public FromContextPathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public ContextPathContext contextPath() {
			return getRuleContext(ContextPathContext.class, 0);
		}

		public ApiContext api() {
			return getRuleContext(ApiContext.class, 0);
		}

		public FunctionalityContext functionality() {
			return getRuleContext(FunctionalityContext.class, 0);
		}

		public NikitaObjectsContext nikitaObjects() {
			return getRuleContext(NikitaObjectsContext.class, 0);
		}

		public OdataCommandContext odataCommand() {
			return getRuleContext(OdataCommandContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_fromContextPath;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterFromContextPath(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitFromContextPath(this);
		}
	}

	public static class NikitaObjectsContext extends ParserRuleContext {
		public NikitaObjectsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public ParentResourceContext parentResource() {
			return getRuleContext(ParentResourceContext.class, 0);
		}

		public SystemIdContext systemId() {
			return getRuleContext(SystemIdContext.class, 0);
		}

		public ResourceContext resource() {
			return getRuleContext(ResourceContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_nikitaObjects;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterNikitaObjects(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitNikitaObjects(this);
		}
	}

	public static class OdataCommandContext extends ParserRuleContext {
		public OdataCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public List<FilterContext> filter() {
			return getRuleContexts(FilterContext.class);
		}

		public FilterContext filter(int i) {
			return getRuleContext(FilterContext.class, i);
		}

		public List<TopContext> top() {
			return getRuleContexts(TopContext.class);
		}

		public TopContext top(int i) {
			return getRuleContext(TopContext.class, i);
		}

		public List<SkipContext> skip() {
			return getRuleContexts(SkipContext.class);
		}

		public SkipContext skip(int i) {
			return getRuleContext(SkipContext.class, i);
		}

		public List<OrderbyContext> orderby() {
			return getRuleContexts(OrderbyContext.class);
		}

		public OrderbyContext orderby(int i) {
			return getRuleContext(OrderbyContext.class, i);
		}

		public List<CountContext> count() {
			return getRuleContexts(CountContext.class);
		}

		public CountContext count(int i) {
			return getRuleContext(CountContext.class, i);
		}

		@Override
		public int getRuleIndex() {
			return RULE_odataCommand;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterOdataCommand(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitOdataCommand(this);
		}
	}

	public static class FilterContext extends ParserRuleContext {
		public FilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public FilterCommandContext filterCommand() {
			return getRuleContext(FilterCommandContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_filter;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterFilter(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitFilter(this);
		}
	}

	public static class CountContext extends ParserRuleContext {
		public CountContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_count;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterCount(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitCount(this);
		}
	}

	public static class SearchContext extends ParserRuleContext {
		public SearchContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public SearchCommandContext searchCommand() {
			return getRuleContext(SearchCommandContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_search;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterSearch(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitSearch(this);
		}
	}

	public static class TopContext extends ParserRuleContext {
		public TopContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public NumberContext number() {
			return getRuleContext(NumberContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_top;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterTop(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitTop(this);
		}
	}

	public static class SkipContext extends ParserRuleContext {
		public SkipContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public NumberContext number() {
			return getRuleContext(NumberContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_skip;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterSkip(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitSkip(this);
		}
	}

	public static class OrderbyContext extends ParserRuleContext {
		public OrderbyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}

		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class, i);
		}

		public List<SortOrderContext> sortOrder() {
			return getRuleContexts(SortOrderContext.class);
		}

		public SortOrderContext sortOrder(int i) {
			return getRuleContext(SortOrderContext.class, i);
		}

		@Override
		public int getRuleIndex() {
			return RULE_orderby;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterOrderby(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitOrderby(this);
		}
	}

	public static class SearchCommandContext extends ParserRuleContext {
		public SearchCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public StringContext string() {
			return getRuleContext(StringContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_searchCommand;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterSearchCommand(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitSearchCommand(this);
		}
	}

	public static class FilterCommandContext extends ParserRuleContext {
		public FilterCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public CommandContext command() {
			return getRuleContext(CommandContext.class, 0);
		}

		public ComparatorCommandContext comparatorCommand() {
			return getRuleContext(ComparatorCommandContext.class, 0);
		}

		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class, 0);
		}

		public FilterCommandContext filterCommand() {
			return getRuleContext(FilterCommandContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_filterCommand;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterFilterCommand(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitFilterCommand(this);
		}
	}

	public static class CommandContext extends ParserRuleContext {
		public CommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public ContainsContext contains() {
			return getRuleContext(ContainsContext.class, 0);
		}

		public StartsWithContext startsWith() {
			return getRuleContext(StartsWithContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_command;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterCommand(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitCommand(this);
		}
	}

	public static class ComparatorCommandContext extends ParserRuleContext {
		public ComparatorCommandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public AttributeContext attribute() {
			return getRuleContext(AttributeContext.class, 0);
		}

		public ComparatorContext comparator() {
			return getRuleContext(ComparatorContext.class, 0);
		}

		public StringContentContext stringContent() {
			return getRuleContext(StringContentContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_comparatorCommand;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterComparatorCommand(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitComparatorCommand(this);
		}
	}

	public static class ContainsContext extends ParserRuleContext {
		public ContainsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public LeftCurlyBracketContext leftCurlyBracket() {
			return getRuleContext(LeftCurlyBracketContext.class, 0);
		}

		public AttributeContext attribute() {
			return getRuleContext(AttributeContext.class, 0);
		}

		public ValueContext value() {
			return getRuleContext(ValueContext.class, 0);
		}

		public RightCurlyBracketContext rightCurlyBracket() {
			return getRuleContext(RightCurlyBracketContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_contains;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterContains(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitContains(this);
		}
	}

	public static class StartsWithContext extends ParserRuleContext {
		public StartsWithContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public LeftCurlyBracketContext leftCurlyBracket() {
			return getRuleContext(LeftCurlyBracketContext.class, 0);
		}

		public AttributeContext attribute() {
			return getRuleContext(AttributeContext.class, 0);
		}

		public ValueContext value() {
			return getRuleContext(ValueContext.class, 0);
		}

		public RightCurlyBracketContext rightCurlyBracket() {
			return getRuleContext(RightCurlyBracketContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_startsWith;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterStartsWith(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitStartsWith(this);
		}
	}

	public static class AttributeContext extends ParserRuleContext {
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public StringContext string() {
			return getRuleContext(StringContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_attribute;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterAttribute(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitAttribute(this);
		}
	}

	public static class ValueContext extends ParserRuleContext {
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public StringContext string() {
			return getRuleContext(StringContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_value;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterValue(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitValue(this);
		}
	}

	public static class SortOrderContext extends ParserRuleContext {
		public SortOrderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public AscContext asc() {
			return getRuleContext(AscContext.class, 0);
		}

		public DescContext desc() {
			return getRuleContext(DescContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_sortOrder;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterSortOrder(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitSortOrder(this);
		}
	}

	public static class ComparatorContext extends ParserRuleContext {
		public ComparatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public EqContext eq() {
			return getRuleContext(EqContext.class, 0);
		}

		public GtContext gt() {
			return getRuleContext(GtContext.class, 0);
		}

		public LtContext lt() {
			return getRuleContext(LtContext.class, 0);
		}

		public GeContext ge() {
			return getRuleContext(GeContext.class, 0);
		}

		public LeContext le() {
			return getRuleContext(LeContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_comparator;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterComparator(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitComparator(this);
		}
	}

	public static class OperatorContext extends ParserRuleContext {
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public AndContext and() {
			return getRuleContext(AndContext.class, 0);
		}

		public OrContext or() {
			return getRuleContext(OrContext.class, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_operator;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterOperator(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitOperator(this);
		}
	}

	public static class LeftCurlyBracketContext extends ParserRuleContext {
		public LeftCurlyBracketContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_leftCurlyBracket;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterLeftCurlyBracket(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitLeftCurlyBracket(this);
		}
	}

	public static class RightCurlyBracketContext extends ParserRuleContext {
		public RightCurlyBracketContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_rightCurlyBracket;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterRightCurlyBracket(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitRightCurlyBracket(this);
		}
	}

	public static class AndContext extends ParserRuleContext {
		public AndContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode AND() {
			return getToken(ODataParser.AND, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_and;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterAnd(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitAnd(this);
		}
	}

	public static class OrContext extends ParserRuleContext {
		public OrContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode OR() {
			return getToken(ODataParser.OR, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_or;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterOr(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitOr(this);
		}
	}

	public static class EqContext extends ParserRuleContext {
		public EqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode EQ() {
			return getToken(ODataParser.EQ, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_eq;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterEq(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitEq(this);
		}
	}

	public static class GtContext extends ParserRuleContext {
		public GtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode GT() {
			return getToken(ODataParser.GT, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_gt;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterGt(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitGt(this);
		}
	}

	public static class LtContext extends ParserRuleContext {
		public LtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode LT() {
			return getToken(ODataParser.LT, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_lt;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterLt(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitLt(this);
		}
	}

	public static class GeContext extends ParserRuleContext {
		public GeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode GE() {
			return getToken(ODataParser.GE, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_ge;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterGe(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitGe(this);
		}
	}

	public static class LeContext extends ParserRuleContext {
		public LeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode LE() {
			return getToken(ODataParser.LE, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_le;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterLe(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitLe(this);
		}
	}

	public static class StringContext extends ParserRuleContext {
		public StringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode STRING() {
			return getToken(ODataParser.STRING, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_string;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterString(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitString(this);
		}
	}

	public static class StringContentContext extends ParserRuleContext {
		public StringContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode STRING_CONTENT() {
			return getToken(ODataParser.STRING_CONTENT, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_stringContent;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterStringContent(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitStringContent(this);
		}
	}

	public static class NumberContext extends ParserRuleContext {
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode DIGITS() {
			return getToken(ODataParser.DIGITS, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_number;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterNumber(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitNumber(this);
		}
	}

	public static class UuidContext extends ParserRuleContext {
		public UuidContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		public TerminalNode UUID() {
			return getToken(ODataParser.UUID, 0);
		}

		@Override
		public int getRuleIndex() {
			return RULE_uuid;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterUuid(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitUuid(this);
		}
	}

	public static class AscContext extends ParserRuleContext {
		public TerminalNode ASC() {
			return getToken(ODataParser.ASC, 0);
		}

		public AscContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_asc;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterAsc(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitAsc(this);
		}
	}

	public static class DescContext extends ParserRuleContext {
		public TerminalNode DESC() {
			return getToken(ODataParser.DESC, 0);
		}

		public DescContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}

		@Override
		public int getRuleIndex() {
			return RULE_desc;
		}

		@Override
		public void enterRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).enterDesc(this);
		}

		@Override
		public void exitRule(ParseTreeListener listener) {
			if (listener instanceof ODataListener)
				((ODataListener) listener).exitDesc(this);
		}
	}
}
