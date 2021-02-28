lexer grammar ODataLexer;

channels { ERRORCHANNEL }

SPACE:                               [ ]+    -> channel(HIDDEN);

// Keywords
// OData Query strings
FILTER:                              '$'F I L T E R;
TOP:                                 '$'T O P '=';
SKIPRULE:                            '$'S K I P'='; // SKIP is a reserved word
SKIPTOKEN:                           '$skiptoken';
ORDERBY:                             '$orderby';
REF:                                 '$ref?$id=';
EXPAND:                              '$expand';
COUNT:                               '$count';
SELECT:                              '$select=';
DOLLARID:                            '$id=';

// Filter expressions
CONTAINS:                            C O N T A I N S;
STARTSWITH:                          S T A R T S W I T H;
ENDSWITH:                            E N D S W I T H;
// STRING FUNCTIONS
SUBSTRINGOF:                         S U B S T R I N G O F;
LENGTH:                              L E N G T H;
INDEXOF:                             I N D E X O F;
REPLACE:                             R E P L A C E;
SUBSTRING:                           S U B S T R I N G;
TOLOWER:                             T O L O W E R;
TOUPPER:                             T O U P P E R;
TRIM:                                T R I M;
CONCAT:                              C O N C A T;
// DATE/TIME FUNCTIONS
DAY:                                 D A Y;
MONTH:                               M O N T H;
YEAR:                                Y E A R;
HOUR:                                H O U R;
MINUTE:                              M I N U T E;
SECOND:                              S E C O N D;
NOW:                                 N O W;
TIME:                                T I M E;
MAX_DATE_TIME:                       M A X D  A T E T I M E;
MIN_DATE_TIME:                       M I N D A T E T I M E;
TOTAL_OFFSET_MINUTES:                T O T A L O F F S E T M I N U T E S;
FRACTIONAL_SECONDS:                  F R A C T I O N A L S E C O N D S;
TOTAL_SECONDS:                       T O T A L S E C O N D S;
GEO_INTERSECTS:                      G E O DOT I N T E R S E C T S;
GEO_DISTANCE:                        G E O DOT D I S T A N C E;
GEO_LENGTH:                          G E O DOT L E N G T H;
// Math|rounding function
ROUND:                               R O U N D;
FLOOR:                               F L O O R;
CEILING:                             C E I L I N G;

CAST:                                C A S T;
ISOF:                                I S O F;

// Comparators
EQUAL:                               '=';
EQ:                                  E Q; // EQUAL
GT:                                  G T; // GREATER THAN
LT:                                  L T; // LESS THAN
GE:                                  G E; // GREATER THAN OR EQUAL
LE:                                  L E; // LESS THAN OR EQUAL
NE:                                  N E; // NOT EQUAL

ADD:                                 A D D;
SUB:                                 S U B;
MUL:                                 M U L;
DIV:                                 D I V;
MOD:                                 M O D;

// ORDERING
ORDER:                               O R D E R;
BY:                                  B Y;
DESC:                                D E S C;
ASC:                                 A S C;

// logical operators
OR:                                  O R;
AND:                                 A N D;
NOT:                                 N O T;

SEPERATOR:                           '://';
HTTP:                                H T T P;
HTTPS:                               H T T P S;
OPEN:                                '(';
CLOSE:                               ')';
COMMA:                               ',';
QUESTION:                            '?';
DOLLAR:                              '$';
SEMI:                                ';';
AT_SIGN:                             '@';
BAR:                                 '|';
SINGLE_QUOTE_SYMB:                   '\'';
DOUBLE_QUOTE_SYMB:                   '"';
REVERSE_QUOTE_SYMB:                  '`';
COLON_SYMB:                          ':';
AMPERSAND:                           '&';
NULL_TOKEN:                          N U L L;
NULL_SPEC_LITERAL:                   '\\' 'N';
DOT:                                 '.';
SLASH:                               '/';
UNDERSCORE:                          '_';

EDM:                                 'Edm';
COLLECTION:                          'Collection';
GEOGRAPHY :                          'Geography';
GEOMETRY:                            'Geometry';
BINARY:                              'Binary' ;
BOOLEAN:                             'Boolean';
BYTE:                                'Byte';
DATE:                                'Date';
DATETIMEOFFSET:                      'DateTimeOffset';
DECIMAL:                             'Decimal';
DOUBLE:                              'Double';
DURATION:                            'Duration';
GUID:                                'Guid';
INT16:                               'Int16';
INT32:                               'Int32';
INT64:                               'Int64';
SBYTE:                               'SByte';
SINGLE:                              'Single';
STREAM:                              'Stream';
STRING:                              'String';
TIMEOFDAY:                           'TimeOfDay';

LINESTRING:                           L I N E S T R I N G;
MULTILINESTRING:                     'MultiLineString';
MULTIPOINT:                          'MultiPoint';
MULTIPOLYGON:                        'MultiPolygon';
POINT:                               'Point';
POLYGON:                             'Polygon';
BOOLEAN_VALUE:                       (T R U E) | (F A L S E);

UUID: BLOCK BLOCK '-' BLOCK '-' BLOCK '-' BLOCK '-' BLOCK BLOCK BLOCK;

INTEGER:                             DIGIT+;
FLOAT:                               NEGATIVE? DIGIT+ DOT DIGIT+;
BSM_ID:                              BSM_LITERAL; // Business specific Metdata
ID:                                  ID_LITERAL;
QUOTED_UUID:                         '\'' UUID '\'';
QUOTED_STRING:                       SQUOTA_STRING;
STRING_LITERAL:                      DQUOTA_STRING;
DECIMAL_LITERAL:                     FLOAT (E NEGATIVE? DIGIT+);

TIME_OF_DAY_VALUE:   HOUR_DEF COLON_SYMB MINUTE ( COLON_SYMB SECOND (DOT FRACTIONAL_SECONDS )?);
DURATION_VALUE: NEGATIVE P (DIGIT D) (DIGIT H) (DIGIT M) (DIGIT (DOT DIGIT)? S);

DATE_VALUE: YEAR '-' MONTH '-' DAY;

DATE_TIME_OFFSET_VALUE:     YEAR_DEF '-' MONTH_DEF '-' DAY_DEF T HOUR_DEF COLON_SYMB
                            MINUTE_DEF ( COLON_SYMB SECOND_DEF ( DOT FRACTIONAL_SECONDS ))
                                     ( Z  | NEGATIVE HOUR_DEF COLON_SYMB MINUTE_DEF );

HOUR_DEF : ( '0' | '1' ) DIGIT | '2' ( '0' | '1' | '2' | '3' );
MINUTE_DEF : ZERO_TO_FIFTY_NINE;
SECOND_DEF : ZERO_TO_FIFTY_NINE;

ZERO_TO_FIFTY_NINE : ( '0' | '1' | '2' | '3' | '4' | '5' ) DIGIT;
ONE_TO_NINE  : '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9';

YEAR_DEF  : NEGATIVE* ( '0' DIGIT DIGIT DIGIT | ONE_TO_NINE DIGIT DIGIT DIGIT);

MONTH_DEF : '0' ONE_TO_NINE | '1' ( '0' | '1' | '2' );
DAY_DEF   : '0' ONE_TO_NINE | ( '1' | '2' ) DIGIT | '3' ( '0' | '1' );

HEX
   : ('%' [a-fA-F0-9] [a-fA-F0-9]) +
   ;

SINGLE_CHAR_SMALL :                  [a-z];
SINGLE_CHAR :                        [a-zA-Z~0-9];
NEGATIVE:                            '-';
DEC_OCTECT :                         [1][0-9][0-9] | [2][0-4][0-9] | [2][5][0-5] | [1-9][0-9] | [0-9];

fragment EXPONENT_NUM_PART:          'E' [-+]? DIGIT+;
fragment BSM_LITERAL:                [a-zA-Z_$0-9]*?'-'*?[a-zA-Z_$0-9]*?':'+[a-zA-Z_$]+?[a-zA-Z_$0-9]*;
fragment ID_LITERAL:                 [a-zA-Z_$0-9]*?[a-zA-Z_$]+?[a-zA-Z_$0-9]*;
fragment DQUOTA_STRING:              '"' ( '\\'.| '""'| ~('"'| '\\') )* '"';
fragment SQUOTA_STRING:              '\'' ('\\'.| '\'\''| ~('\''| '\\'))* '\'';
fragment BQUOTA_STRING:              '`' ( '\\'.| '``'| ~('`'|'\\'))* '`';

fragment BLOCK: [A-Za-z0-9] [A-Za-z0-9] [A-Za-z0-9] [A-Za-z0-9];

fragment A: [aA];
fragment B: [bB];
fragment C: [cC];
fragment D: [dD];
fragment E: [eE];
fragment F: [fF];
fragment G: [gG];
fragment H: [hH];
fragment I: [iI];
fragment J: [jJ];
fragment K: [kK];
fragment L: [lL];
fragment M: [mM];
fragment N: [nN];
fragment O: [oO];
fragment P: [pP];
fragment Q: [qQ];
fragment R: [rR];
fragment S: [sS];
fragment T: [tT];
fragment U: [uU];
fragment V: [vV];
fragment W: [wW];
fragment X: [xX];
fragment Y: [yY];
fragment Z: [zZ];

fragment DIGIT:    [0-9];
fragment DIGITS:   DIGIT+;
fragment HEXDIGIT: [0-9a-fA-F];

HEX_NUMBER: ('0x' HEXDIGIT+) | ('x\'' HEXDIGIT+ '\'');
BIN_NUMBER: ('0b' [01]+) | ('b\'' [01]+ '\'');

// Last tokens must generate Errors
ERROR_RECONGNIGION:                  .    -> channel(ERRORCHANNEL);
