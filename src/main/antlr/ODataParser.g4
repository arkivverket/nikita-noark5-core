parser grammar ODataParser;

options { tokenVocab=ODataLexer; }
/*
created by tsodring 25/05/2018
Basic grammar to handle OData syntax for nikita. Note this grammar will require
a lot more work to be complete.

Grammar portion describing HTTP adapted from
https://github.com/antlr/grammars-v5/blob/master/url/url.g4. (C) as per BSD
declaration

totalseconds, based on a duration, but there are no duration fields in Noark
*/


// arkivstruktur/dokumentobjekt?$filter=contains(filnavn, '<20190803045988.RT234511@mail.redemash.com>')
// arkivstruktur/dokumentbeskrivelse/12345/dokumentobjekt?$filter=contains(filnavn, '<20190803045988.RT234511@mail.redemash.com>')


odataQuery
    :
    entityBase odataCommand
    ;

entityBase
    : ((entityName SLASH entityName) | entityName) QUESTION;

odataCommand
    : ((filterStatement (AMPERSAND countStatement)?)
    |  topStatement | skipStatement
    |  (orderByClause (AMPERSAND countStatement)?))
    |  referenceStatement
    ;

predicate
    : filterStatement
    | left=predicate comparisonOperator right=predicate
    | predicate comparisonOperator
    ;

filterStatement
    : FILTER filterExpression
    ;

// Value associated with $id is an odata syntax with host / contextpath / api
// http://localhost:49708/api/arkivstruktur/Dokumentbeskrivelse/1fa94a89-3550-470b-a220-92dd4d709044
// Need (stringvalue /)+ uuidValue. Shold sort it!
referenceStatement
    : DOLLARID scheme SEPERATOR host (COLON port)?
    ;

scheme
    : (HTTP | HTTPS);
host
    : stringValue (DOT stringValue)+;
port
    : integerValue;

countStatement
    : COUNT integerValue
    ;

topStatement
    : TOP integerValue
    ;

skipStatement
    : SKIP integerValue
    ;

filterExpression
    : (boolExpressionLeft filterExpression boolExpressionRight)
      (logicalOperator filterExpression)?
    |
      (stringCompareExpression | comparisonExpression |
       integerComparatorExpression | floatComparatorExpression |
       substringExpression | indexOfExpression | lengthExpression |
       stringModifierExpression | timeExpression | concatExpression)
      (logicalOperator filterExpression)?
    ;

boolExpressionLeft
    : LR_BRACKET
    ;

boolExpressionRight
    : RR_BRACKET
    ;

stringCompareExpression
    : (CONTAINS | STARTSWITH | ENDSWITH)
    '(' entityName ',' singleQuotedString ')'
    ;

/*
Given tittel= "Søknad om rehabilitering"

 - $filter=substring(tittel, 1)
   will return "øknad om rehabilitering"

 - $filter=substring(tittel, 1, 4)
   will return "økna"

A comparison operator (eq, ne) can be added for further filtering
*/
substringExpression
    : SUBSTRING '(' entityName ',' integerValue (',' integerValue)? ')'
    comparisonOperator singleQuotedString
    ;

indexOfExpression
    :  INDEXOF'(' entityName ',' singleQuotedString ')'
       comparisonOperator singleQuotedString
    ;

lengthExpression
    : LENGTH '(' entityName ')' comparisonOperator integerValue
    ;

timeExpression
    : (NOW | MAXDATETIME | MINDATETIME | TOTALOFFSETMINUTES) '(' entityName ')'
    ;

stringModifierExpression
    : (TOLOWER | TOUPPER | TRIM) '(' entityName ',' value ')'
    ;

concatExpression
    : CONCAT '(' entityName ',' value ')'
    ;

comparisonExpression
    : entityName comparisonOperator value
    ;
/*
$filter=year(DateTime) eq 2010
$filter=month(DateTime) eq 2 // February (month of year)
$filter=day(DateTime) eq 21 // 21st day of the month
$filter=hour(DateTime) eq 1 // start at 0 or 1? Assuming 0
$filter=minute(DateTime) eq 42 // start at 0 or 1? Assuming 0
$filter=second(DateTime) eq 55 // what is after 59? 60 or 0?
*/
integerComparatorExpression
    : (YEAR | MONTH | DAY | HOUR | MINUTE | SECOND )
     '(' entityName  ')' comparisonOperator integerValue
    ;

/*
http://docs.oasis-open.org/odata/odata/v5.0/cs01/part2-url-conventions/odata-v5.0-cs01-part2-url-conventions.html#_Toc362965324
shows that the following can return a decimal value

$filter=round(Decimal) eq 100
$filter=floor(Decimal) eq 0
$filter=ceiling(Decimal) eq 1
*/
floatComparatorExpression
    : (ROUND | FLOOR | CEILING)
     '(' entityName  ')' comparisonOperator (floatValue | integerValue)
    ;

orderByClause
    : ORDER BY orderByExpression (',' orderByExpression)*
    ;


orderByExpression
    : entityName order=(ASC | DESC)?
    ;

nullNotnull
    : NOT? (NULL_LITERAL | NULL_SPEC_LITERAL)
    ;

/*
Examples of comparisonOperator queries:

All documents with a date from 2018
 - $filter=year(dokumentetsDato) eq 2018

All documents with filnavn equal to 'important letter.odt'
 -$filter=filnavn eq 'important letter.odt'

All documents with a date after 2018 (from 2019 upwards)
 - $filter=year(dokumentetsDato) gt 2018

All documents with a date before 2018 (from 2017 downwards)
 - $filter=year(dokumentetsDato) lt 2018

All documents with a date before 2019 (including 2018)
 - $filter=year(dokumentetsDato) lte 2018

All documents with a date before 2019 (including 2018)
 - $filter=year(dokumentetsDato) gte 2018

All documents with a date not equal to 2019
 - $filter=year(dokumentetsDato) ne 2018
*/
comparisonOperator
    : EQ | GT | LT | LE | GE | NE
    ;

logicalOperator
    : AND | OR
    ;

columnName
    : ID
    ;

entityName
    : ID
    ;

value
    : ID | STRING_LITERAL
    ;
stringValue
    : ID
    ;


singleQuotedString
    :
    QUOTED_STRING
    ;

integerValue
    : INTEGER
    ;

floatValue
    : FLOAT
    ;

systemIdValue
    : UUID EOF
    ;
