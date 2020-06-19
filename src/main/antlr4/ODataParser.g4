parser grammar ODataParser;
/**
 * Basic grammar to handle OData syntax for nikita. Note this grammar will
 * require work to be a complete OData implementation. This implementation
 * may be of use to others developing an OData implementation.
 * Remember to set the output directory to:
 *  src/main/java/nikita/webapp/odata/base
*/
options { tokenVocab=ODataLexer; }

@header {
package nikita.webapp.odata.base;
}


// 1. ManyToMany join needs to be implemented
// 2. Introduce NikitaODataToHQL to capture problem where  metadata/kode needs to be  metadata.kode

odataRelativeUri:
    resourcePath (QUESTION queryOptions)?;

resourcePath:
    (embeddedEntitySet | entityUUID | entity) countStatement?;

entity:
    entityName | entityCast;

entityCast:
    entityName SLASH entityName;

entityUUID:
    entityName SLASH uuidIdValue;

embeddedEntitySet:
    entityUUID  (SLASH entityUUID | (SLASH (entityCast | entity)))*;

queryOptions:
    queryOption (AMPERSAND queryOption)*;

// Note: Listener must make sure only one of each is possible
queryOption:
    filter ('&'expand | '&'orderBy | '&'skipStatement | '&'topStatement | '&'topStatement)*;

filter:
    FILTER EQUAL filterExpression;

expand:
    EXPAND EQUAL (joinEntities | attributeName | boolCommonExpr) (SLASH filter)?;

filterExpression:
    openPar filterExpression closePar                                 #parenExpression
    | left=filterExpression op=logicalOperator right=filterExpression #binaryExpression
    | boolCommonExpr                                                  #boolExpression
    ;

boolCommonExpr:
    left=leftComparatorExpr op=comparisonOperator right=rightComparatorExpr   #comparatorExpression
    | left=COUNT op=comparisonOperator right=primitiveLiteral                 #countComparatorExpression
    | compareMethodExpr                                                       #compareMethodExpression
    ;

leftComparatorExpr:
    methodCallExpr | calenderMethodExp | concatMethodExpr | joinEntities | attributeName | commonExpr;

rightComparatorExpr:
    methodCallExpr | calenderMethodExp | concatMethodExpr | joinEntities | attributeName | commonExpr;

orderBy:
    ORDERBY EQUAL orderByItem (COMMA orderByItem )*;

orderByItem:
    orderAttributeName (sortOrder)?;

topStatement:
    TOP integerValue;

skipStatement:
    SKIPRULE integerValue;

joinEntities:
     (entityName SLASH)+ attributeName;

commonExpr:
    primitiveLiteral | methodExpr | mathExpr;

mathExpr:
    (ADD | SUB | MUL | DIV | MOD) number;

methodExpr:
     methodCallExpr | calenderMethodExp | singleMethodCallExpr |
     concatMethodExpr | substringMethodCallExpr | compareMethodExpr;

compareMethodExpr:
    compareMethodName
    OPEN
     (joinEntities | attributeName | commonExpr) COMMA commonExpr
    CLOSE
    ;

methodCallExpr:
    methodName OPEN (methodCallExpr | joinEntities | attributeName) CLOSE;

calenderMethodExp:
    calenderMethodName OPEN (joinEntities | attributeName | primitiveLiteral) CLOSE;

concatMethodExpr:
    CONCAT
    OPEN
     (concatMethodExpr | primitiveLiteral | attributeName)
    COMMA
     (primitiveLiteral | attributeName)
    CLOSE
    ;

singleMethodCallExpr:
    (MIN_DATE_TIME | MAX_DATE_TIME | NOW) OPEN CLOSE;

substringMethodCallExpr:
    SUBSTRING OPEN (joinEntities | attributeName | commonExpr) COMMA commonExpr CLOSE;

comparisonOperator:
    EQ | GT | LT | LE | GE | NE;

compareMethodName:
    CONTAINS | STARTSWITH | ENDSWITH | INDEXOF | GEO_INTERSECTS | GEO_DISTANCE;

methodName:
    LENGTH | TOLOWER | TOUPPER | TRIM | ROUND | FLOOR | CEILING | GEO_LENGTH
    ;

calenderMethodName:
    YEAR | MONTH | DAY | HOUR | MINUTE | SECOND | FRACTIONAL_SECONDS
    | TOTAL_SECONDS | DATE | TIME | TOTAL_OFFSET_MINUTES
    ;

number:
    INTEGER | FLOAT;

primitiveLiteral:
    quotedString |
    nullSpecLiteral |
    nullToken |
    booleanValue |
    durationValue |
    dateValue |
    dateTimeOffsetValue |
    timeOfDayValue |
    decimalLiteral |
    floatValue |
    integerValue;

countStatement:
    SLASH COUNT (EQUAL booleanValue)?;

openPar: OPEN;
closePar: CLOSE;
logicalOperator: AND | OR;
sortOrder: ASC | DESC;
entityName: ID;
attributeName: ID;
orderAttributeName: ID;
uuidIdValue: UUID;
quotedString: QUOTED_STRING;
nullSpecLiteral: NULL_SPEC_LITERAL ;
nullToken: NULL_TOKEN;
booleanValue: BOOLEAN_VALUE ;
durationValue: DURATION_VALUE ;
dateValue: DATE_VALUE ;
dateTimeOffsetValue: DATE_TIME_OFFSET_VALUE ;
timeOfDayValue: TIME_OF_DAY_VALUE ;
decimalLiteral: DECIMAL_LITERAL ;
floatValue: FLOAT ;
integerValue: INTEGER;
