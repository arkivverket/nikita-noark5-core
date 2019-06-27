lexer grammar ODataLexer;

/**
As OData filter is basically a SQL where, this lexer copies content from:

https://github.com/antlr/grammars-v5/blob/master/mysql/MySqlLexer.g4

Their (MySQL Positive Technologies grammar) lexer is published with a MIT (c).

Note the UUID description is taken from Bart Kiers answer on the following SO
question:

https://stackoverflow.com/questions/40632247/error-in-parsing-guid-through-antlr
*/

channels { ERRORCHANNEL }

SPACE:                               [ ]+    -> channel(HIDDEN);

// Keywords
// OData Query strings
FILTER:                              '$filter=';
TOP:                                 '$top=';
SKIPRULE:                            '$skip='; // SKIP is a reserved word
ORDERBY:                             '$orderby=';
REF:                                 '$ref?$id=';
EXPAND:                              '$expand=';
COUNT:                               '$count=';
SELECT:                              '$select=';
DOLLARID:                            '$id=';

// Filter expressions
CONTAINS:                            'contains';
STARTSWITH:                          'startswith';
ENDSWITH:                            'endswith';
// String functions
SUBSTRINGOF:                         'substringof';
LENGTH:                              'length';
INDEXOF:                             'indexof';
REPLACE:                             'replace';
SUBSTRING:                           'substring';
TOLOWER:                             'tolower';
TOUPPER:                             'toupper';
TRIM:                                'trim';
CONCAT:                              'concat';
// date/time functions
DAY:                                 'day';
MONTH:                               'month';
YEAR:                                'year';
HOUR:                                'hour';
MINUTE:                              'minute';
SECOND:                              'second';
NOW:                                 'now';
MAXDATETIME:                         'maxdatetime';
MINDATETIME:                         'mindatetime';
TOTALOFFSETMINUTES:                  'totaloffsetminutes';
// Math/rounding function
ROUND:                               'round';
FLOOR:                               'floor';
CEILING:                             'ceiling';

// Comparators
EQ:                                  'eq'; // equal
GT:                                  'gt'; // greater than
LT:                                  'lt'; // less than
GE:                                  'ge'; // greater than or equal
LE:                                  'le'; // less than or equal
NE:                                  'ne'; // not equal

// ordering
ORDER:                               'order';
BY:                                  'by';
DESC:                                'desc';
ASC:                                 'asc';

// logical operators
OR:                                  'or';
AND:                                 'and';
NOT:                                 'not';

SEPERATOR:                           '://';
HTTP:                                'http';
HTTPS:                               'https';
//DOT:                                 '.';
LR_BRACKET:                          '(';
RR_BRACKET:                          ')';
COMMA:                               ',';
QUESTION:                            '?';
SEMI:                                ';';
AT_SIGN:                             '@';
SINGLE_QUOTE_SYMB:                   '\'';
DOUBLE_QUOTE_SYMB:                   '"';
REVERSE_QUOTE_SYMB:                  '`';
COLON_SYMB:                          ':';
AMPERSAND:                           '&';
NULL_LITERAL:                        'NULL';
NULL_SPEC_LITERAL:                   '\\' 'N';
DOT:                                 '.';
SLASH:                               '/';

UUID: BLOCK BLOCK '-' BLOCK '-' BLOCK '-' BLOCK '-' BLOCK BLOCK BLOCK;

NOARK_ENTITY:                        ID_LITERAL+ '-'? ID_LITERAL*;

INTEGER:                             DEC_DIGIT+;
FLOAT:                               DEC_DIGIT+ DOT DEC_DIGIT+;
ID:                                  ID_LITERAL;
QUOTED_STRING:                       SQUOTA_STRING;
STRING_LITERAL:                      DQUOTA_STRING | SQUOTA_STRING;
DECIMAL_LITERAL:                     DEC_DIGIT+;
HEXADECIMAL_LITERAL:                 'X' '\'' (HEX_DIGIT HEX_DIGIT)+ '\''
                                     | '0X' HEX_DIGIT+;

ODATA_ARK:                           '/odata/arkivstruktur';
HEX
   : ('%' [a-fA-F0-9] [a-fA-F0-9]) +
   ;

STRING
   : ([a-zA-Z~0-9] | HEX) ([a-zA-Z0-9.-] | HEX)*
   ;

fragment EXPONENT_NUM_PART:          'E' [-+]? DEC_DIGIT+;
fragment ID_LITERAL:                 [a-zA-Z_$0-9]*?[a-zA-Z_$]+?[a-zA-Z_$0-9]*;
//fragment ID_LITERAL:                 [A-Z_$0-9]*?[A-Z_$]+?[A-Z_$0-9]*;
fragment DQUOTA_STRING:              '"' ( '\\'. | '""' | ~('"'| '\\') )* '"';
fragment SQUOTA_STRING:              '\'' ('\\'. | '\'\'' | ~('\'' | '\\'))* '\'';
fragment BQUOTA_STRING:              '`' ( '\\'. | '``' | ~('`'|'\\'))* '`';
fragment HEX_DIGIT:                  [0-9A-F];
fragment DEC_DIGIT:                  [0-9];
fragment BIT_STRING_L:               'B' '\'' [01]+ '\'';
fragment BLOCK: [A-Za-z0-9] [A-Za-z0-9] [A-Za-z0-9] [A-Za-z0-9];


// Last tokens must generate Errors

ERROR_RECONGNIGION:                  .    -> channel(ERRORCHANNEL);
