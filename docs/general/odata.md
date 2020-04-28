# OData

The Noark5 API requires the implementation of the OData standard for metadata search. 

## Related resources

  * []()
  * [OData ABNF construction rules](https://docs.oasis-open.org/odata/odata/v4.0/errata02/os/complete/abnf/odata-abnf-construction-rules.txt)
  * [MySQL grammar in ANTLR 4](http://blog.ptsecurity.com/2018/01/mysql-grammar-in-antlr-4.html) 
  * [OData cheatsheet](https://help.nintex.com/en-us/insight/OData/HE_CON_ODATAQueryCheatSheet.htm)

## OData limitations

OData enables a SQL-like approach to search in in REST endpoints

## Nikitas OData implementation

Nikita implements OData using antlr4. If you want to see how we have implemented OData please take a look at *ODataLexer.g4* and *ODataParser.g4*. These are originally adapted from the MySQL antlr4 descriptions. 

The class NikitaODataWalker contains the main functionality for parsing. There are a number of subclasses (NikitaODataToHQLWalker, NikitaODataToSQLWalker and NikitaODataToESWalker). Currently, nikita is only actively developing the HQL version. The others are documentation showing how it could be possible to extend the base class for OData translation.

The queries our OData implementation supports are documented in TestOData.java.
 
## Escaping
The client will need to URL-encode the URL. This is outside the scope of the OData handling as spring gives the ODataController a decode URL. Our code does not handle URL-decode the URL. Our code does unescape some parts of the OData query where necessery  
