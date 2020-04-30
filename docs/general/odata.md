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
The client will need to URL-encode the URL. This is outside the scope of the OData handling as spring gives the ODataController a decoded URL. Our code does not handle URL-decode the URL. Our code does unescape some parts of the OData query where necessary.  

## Handling IN queries

The API spec identifies the following query as a valid query it should be possible to search with:

    ../api/arkivstruktur/Mappe?$filter=klasse/klasseID eq '12/2' and klasse/klassifikasjonssystem/klassifikasjonstype/kode eq 'GBNR' 

I am unsure if the second part is a valid OData query

    klasse/klassifikasjonssystem/klassifikasjonstype/kode eq 'GBNR'

The query basically says that we must find all klassifikasjonssystem that are of type 'GBNR' that a klasse entity is connected to. As it stands I am not sure how to approach such a query, so we are leaving it for now and will concentrate on implementing the easier example:

    klasse/klasseID eq '12/2'

So we are looking at undertaking a query like:
    
    ../api/arkivstruktur/Mappe?$filter=klasse/klasseID eq '12/2'
    
I believe this is an IN query         

SQL (Norwegian Noark names) :

    select * from mappe where mappe.referanseKlasse IN     
          (select systemID from klasse where klasseID = '12/2')     

SQL (English nikita names) :

    select * from as_file where as_file.file_class_id IN     
          (select system_id from as_class where class_id = '12/2') 

It is not clear to me how to construct a generic approach (OData to S/HQL) to
 create the query. It is straightforward to pick up the *klasse/klasseID eq '12/2'*
 and handle it as *"klasse/klasseID"* can be parsed as entity/attribute and then
 the code dealing with the parser details can turn *"klasse/klasseID"* into a 

      IN (select SOMETHING from as_class where class_id = '12/2')

The *SOMETHING* is the issue here as outside of our Noark context, it is not
possible to know what *SOMETHING* is. But within the Noark/nikita context it
is not a problem to figure out that *SOMETHING* is system_id. We can even add
a method to the @Entity classes that pulls out the name of the primary key or 
it may be possible to do via reflection. 

The previous part is also difficult to solve 
   
    select * from as_file where as_file.file_class_id IN

How can I tell what the foreign key between the as_file table, and the 
 as_class table are. Perhaps it can be solved with reflection or hibernate/JPA
 might be able to give me that information. 
 
While the above OData query is relevant another more relevant query perhaps to 
explore is based on the national identifiers. Some example national identifier queries:

       api/arkivstruktur/Mappe?$filter=bygning/bygningsnummer eq 10 and bygning/endringsloepenummer eq 20
       api/arkivstruktur/Mappe?$filter=matrikkel/kommunenummer eq '110' and matrikkel/gaardsnummer eq 22 and matrikkel/bruksnummer eq 12
       api/arkivstruktur/Mappe?$filter=posisjon/x eq 1.233 and posisjon/y eq 9.233 and posisjon/y eq 22.45  
       api/arkivstruktur/Mappe?$filter=plan/planidentifikasjon eq 'min super plan' and plan/fylkesnummer eq '23' 
       api/arkivstruktur/Mappe?$filter=foedselsnummer eq '010182121212'
       api/arkivstruktur/Mappe?$filter=dnummer eq '123456789' 

Solving these with IN clauses may also be straightforward, however foedselsnummer and dnummer appear to both be an entity and attribute at the same time. Unless there is an entity/attribute description for national identifiers, the parsing code will assume that foedselsnummer is an attribute belonging to mappe and not a foreign key reference. Therefor we will have to have some kind of entity identifier. Two approaches quickly pop to mind.

1. Same name for entity and attribute   


        api/arkivstruktur/Mappe?$filter=foedselsnummer/foedselsnummer eq '010182121212'
        api/arkivstruktur/Mappe?$filter=dnummer/dnummer eq '123456789' 

2. nasjonalidentifikator as entity name


       api/arkivstruktur/Mappe?$filter=nasjonalidentifikator/foedselsnummer eq '010182121212'
       api/arkivstruktur/Mappe?$filter=nasjonalidentifikator/dnummer eq '123456789'        

I don't need to solve this approach right now, but we should take a look at this soon.       
       
## Known problems in our implementation

### Lexer problem

The following OData query can be parsed:

    arkivstruktur/mappe?$filter=klasse/klasseID eq '12'

but this one causes a warning  / console message

    arkivstruktur/mappe?$filter=klasse/klasseID eq 12

The parser is picking 12 up as an INTEGER rather than as an *ID* or *QUOTED_STRING*

    line 1:47 mismatched input '12' expecting {ID, QUOTED_STRING}
    
This is related to the way the lexer sees INTEGER defined first and passing 12 as an integer to the 
parser rather than a literal or quoted literal. There is no point in spending days now trying to fix
this, so I am documenting this, so we can revisit it later.     