parser grammar ODataParser;

options { tokenVocab=ODataLexer; }

@header {
package nikita.webapp.odata.base;
}

/*
Basic grammar to handle OData syntax for nikita. Note this grammar will require
a lot more work to be complete.

Grammar portion describing HTTP adapted from
https://github.com/antlr/grammars-v5/blob/master/url/url.g4. (C) as per BSD
declaration

totalseconds, based on a duration, but there are no duration fields in Noark

Remember to set the output directory to:
src/main/java/nikita/webapp/odata/base
*/

// 1. TODO: Note it looks like we really need to replace integerComparatorExpression with a
//  queryFunctionComparatorExpression as this can catch a lot more and make coding simpler.
// As per: https://docs.oasis-open.org/odata/odata/v4.01/odata-v4.01-part1-protocol.html#sec_BuiltinQueryFunctions
// 2. Better error handling. Also we see that neq rather than ne did not throw
// an error. We need to throw 400 Bad request quicker when things are
// inncorrect. This will be an ODataParseExecption that is caught and rethrown
// as a 400 BR.
// 3. Better handling of JOINFunction. Now JOIN is a manual call for
// complextype. This should be done via parsing likely, reduce code and
// complexity
// 4. ManyToMany join needs to be implemented
// 5. Rename classes:
//   NikitaODataWalker -> ODataWalker
//   NikitaODataToHQLWalker -> ODataToHQL
// 6. Introduce NikitaODataToHQL to capture problem where  metadata/kode needs to be  metadata.kode


// arkivstruktur/dokumentobjekt?$filter=contains(filnavn, '<20190803045988.RT234511@mail.redemash.com>')
// arkivstruktur/dokumentbeskrivelse/cf8e1d0d-e94d-4d07-b5ed-46ba2df0465e/dokumentobjekt?$filter=contains(filnavn, '<20190803045988.RT234511@mail.redemash.com>')

referenceStatement
    :
    // Handles examples like:
    // odata/arkivstruktur/mappe/3fd16099-0d1d-4902-a42f-f44c1aaf431f/ny-kryssreferanse/$ref?$id=odata/arkivstruktur/basisregistrering/245ff5c7-c74b-4e92-89f5-78ab0ed6b50d
    // arkivstruktur/registrering/cf8e1d0d-e94d-4d07-b5ed-46ba2df0465e/dokumentbeskrivelse/$ref?$id=arkivstruktur/dokumentbeskrivelse/1fa94a89-3550-470b-a220-92dd4d709044
     ODATA_ARK SLASH entityName SLASH systemIdValue SLASH entityName SLASH REF ODATA_ARK
     SLASH entityName SLASH  systemIdValue
    ;

odataQuery
    :
    entityBase odataCommand
    ;

entityBase
    : (
       (packageName SLASH entityName) |
       (packageName SLASH entityName SLASH systemIdValue SLASH entityName)
      ) QUESTION;

odataCommand
    : ((filterStatement (AMPERSAND countStatement)?)
    |  topStatement | skipStatement
    |  (orderByClause (AMPERSAND countStatement)?))
    ;

predicate
    : filterStatement
    | left=predicate comparisonOperator right=predicate
    | predicate comparisonOperator
    ;

filterStatement
    : FILTER filterExpression
    ;

countStatement
    : COUNT integerValue
    ;

topStatement
    : TOP integerValue
    ;

skipStatement
    : SKIPRULE integerValue
    ;

filterExpression
    : (boolExpressionLeft filterExpression boolExpressionRight)
      (logicalOperator filterExpression)?
    |
      (stringCompareExpression | inComparisonExpression | comparisonExpression |
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
    : stringCompareCommand '(' (attributeName | joinEntities) ',' singleQuotedString ')'
    ;

inComparisonExpression
   :
   joinEntities comparisonOperator value
   ;

joinEntities
   :
   (entityName '/')+ attributeName
   ;

stringCompareCommand
    : CONTAINS | STARTSWITH | ENDSWITH
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
    : (joinEntities | attributeName) comparisonOperator value
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
    : integerCompareCommand '(' (attributeName | joinEntities)  ')' comparisonOperator integerValue
    ;

integerCompareCommand
    : YEAR | MONTH | DAY | HOUR | MINUTE | SECOND
    ;
/*
http://docs.oasis-open.org/odata/odata/v5.0/cs01/part2-url-conventions/odata-v5.0-cs01-part2-url-conventions.html#_Toc362965324
shows that the following can return a decimal value

$filter=round(Decimal) eq 100
$filter=floor(Decimal) eq 0
$filter=ceiling(Decimal) eq 1
*/
floatComparatorExpression
    : floatCommand '(' attributeName ')' comparisonOperator
      floatOrIntegerValue
    ;

floatCommand
    : ROUND | FLOOR | CEILING
    ;

floatOrIntegerValue
    : floatValue | integerValue
    ;

orderByClause
    : ORDER BY orderByExpression (',' orderByExpression)*
    ;

orderByExpression
    : attributeName orderAscDesc?
    ;

orderAscDesc
    : ASC | DESC
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
    :
    AND | OR
    ;

columnName
    :
    ID
    ;

entityName
    :
    NOARK_ENTITY
    ;

attributeName
    :
    NOARK_ENTITY
    ;

packageName
    :
    NOARK_ENTITY
    ;

value
    : ID | QUOTED_STRING
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
    : UUID
    ;


