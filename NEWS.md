Major changes in Nikita Noark 5 Core releases
=============================================

These are the highlevel changes. For details, see the git history.

Release 0.6 2021-06-10 (d1ba5fc7e8bad0cfdce45ac20354b19d10ebbc7b)
----------------------

* Refactor metadata entity search
* Remove redundant security configuration
* Make OpenAPI documentation work
* Change database structure / inheritance model to a more sensible approach
* Make it possible to move entities around the fonds structure
* Implemented a number of missing endpoints
* Make sure yml files are in sync
* Implemented/finalised storing and use of
    - Business Specific Metadata
    - Norwegian National Identifiers
    - Cross Reference
    - Keyword
    - StorageLocation
    - Author
    - Screening for relevant objects
    - ChangeLog
    - EventLog
* Make generation of updated docker image part of successful CI pipeline
* Implement pagination for all list requests
    - Refactor code to support lists
    - Refactor code for readability
    - Standardise the controller/service code
* Finalise File->CaseFile expansion and Record->registryEntry/recordNote expansion
* Improved Continuous Integration (CI) approach via gitlab
* Changed conversion approach to generate tagged PDF documents
* Updated dependencies
    - For security reasons
    - Brought codebase to spring-boot version 2.5.0
    - Remove import of necessary dependencies
    - Remove non-used metrics classes
* Added new analysis to CI including
* Implemented storing of Keyword
* Implemented storing of Screening and ScreeningMetadata
* Improved OData support
    - Better support for inheritance in queries where applicable
    - Brought in more OData tests
    - Improved OData/hibernate understanding of queries
    - Implement $count, $orderby
    - Finalise $top and $skip
    - Make sure & is used between query parameters
* Improved Testing in codebase
    - A new approach for integration tests to make test more readable
    - Introduce tests in parallel with code development for TDD approach
    - Remove test that required particular access to storage
* Implement case-handling process from received email to case-handler
    - Develop required GUI elements (digital postroom from email)
    - Introduced leader, quality control and postroom roles
* Make PUT requests return 200 OK not 201 CREATED
* Make DELETE requests return 204 NO CONTENT not 200 OK
* Replaced 'oppdatert*' with 'endret*' everywhere to match latest spec
* Upgrade Gitlab CI to use python > 3 for CI scripts
* Bug fixes
    - Fix missing ALLOW
    - Fix reading of objects from jar file during start-up
    - Reduce the number of warnings in the codebase
    - Fix delete problems
    - Make better use of cascade for "leaf" objects
    - Add missing annotations where relevant
    - Remove the use of ETAG for delete
    - Fix missing/wrong/broken rels discovered by runtest
    - Drop unofficial convertFil (konverterFil) end point
    - Fix regex problem for dateTime
    - Fix multiple static analysis issues discovered by coverity
    - Fix proxy problem when looking for object class names
    - Add many missing translated Norwegian to English (internal) attribute/entity names
    - Change UUID generation approach to allow code also set a value
    - Fix problem with Part/PartParson
    - Fix problem with empty OData search results
    - Fix metadata entity domain problem
* General Improvements
    - Makes future refactoring easier as coupling is reduced
    - Allow some constant variables to be set from property file
    - Refactor code to make reflection work better across codebase
    - Reduce the number of @Service layer classes used in @Controller classes
    - Be more consistent on naming of similar variable types
    - Start printing rels/href if they are applicable
    - Cleaner / standardised approach to deleting objects
    - Avoid concatenation when using StringBuilder
    - Consolidate code to avoid duplication
    - Tidy formatting for a more consistent reading style across similar class files
    - Make throw a log.error message not an log.info message
    - Make throw print the log value rather than printing in multiple places
    - Add some missing pronom codes
    - Fix time formatting issue in Gitlab CI
    - Remove stale / unused code
    - Use only UUID datatype rather than combination String/UUID for systemID
    - Mark variables final and @NotNull where relevant to indicate intention
* Change Date values to DateTime to maintain compliance with Noark 5 standard
* Domain model improvements using Hypersistence Optimizer
    - Move @Transactional from class to methods to avoid borrowing the JDBC Connection unnecessarily
    - Fix OneToOne performance issues
    - Fix ManyToMany performance issues
    - Add missing bidirectional synchronization support
    - Fix ManyToMany performance issue
* Make List<> and Set<> use final-keyword to avoid potential problems during update operations
* Changed internal URLs, replaced "hateoas-api" with "api".
* Implemented storing of Precedence.
* Corrected handling of screening.
* Corrected \_links collection returned for list of mixed entity types to match the specific entity.
* Improved several internal structures.

Release 0.5 2020-03-02 (commit bf83be0610b1fdc6b56b4c94ab07c3d1716940cb)
----------------------

* Updated to Noark 5 versjon 5.0 API specification.
    - Changed formatting of \_links from [] to {} to match IETF draft on JSON HAL.
    - Merged Registrering og Basisregistrering in version 4 to combined Registrering.
    - DokumentObjekt is now subtype of ArkivEnhet.
    - Introducing new entity Arkivnotat.
    - Changed all relation keys to use /v5/ instead of /v4/.
    - Corrected to use new official relation keys when possible.
    - Renamed Sakspart to Part and connect it to Mappe, Registrering
      and Dokumentbeskrivelse instead of only Saksmappe.
    - Moved Korrespondansepart connection from Journalpost to
      Registrering.
    - Moved Part and Korrespondansepart from package sakarkiv to
      arkivstruktur.
    - Renamed presedensstatus to presedensStatus.
    - Use new JSON content-type "application/vnd.noark5+json".
    - Updated prepopulated format list to use PRONOM codes.
    - Implemented endpoint for system information.
    - Implemented national identifiers for both file and record.
    - Implemented comments.
    - implemented sign off.
    - implemented conversion.
 * Improved/implemented OData search and paging support for more entities.
 * No longer exposes attribute Dokumentobjekt.referanseDokumentfil,
   one should use the relation in \_links instead.
 * Corrected relation keys under
   https://rel.arkivverket.no/noark5/v5/api/administrasjon/, replacing
   'administrasjon' with 'admin'.
 * Fixed several security and stability issues discovered by Coverity.
 * Corrected handling ETag errors, now return code 409.
 * Improved handling of Kryssreferanse.
 * Changed internal database model to use UUID/SystemID as primary keys
   in tables.
 * Changed internal database table names to use package prefix.
 * Changed time zone handling for date and datetime attributes, to be
   more according to the new definition in the API specification.
 * Change revoke-token to only drop token on POST requests, not GET.
 * Updated to newer Spring version.
 * Changed primary key and URL component for metadata code lists to
   use the 'kode' value instead of a SystemID.
 * Corrected implementation of Part and Sakspart.
 * Changed instance lists with subtypes (like .../registrering/ and
   .../mappe/) to include the attributes and \_links entries for the
   subtype in the supertype lists.
 * Adjusted \_links relations to make it possible to figure out the
   entity of an instance using the self->href->relation key lookup
   method.
 * Fixed several end points to make sure GET, PUT, POST and DELETE
   match each other.
 * Updated DELETE endpoints to work with UUID based entity
   identifiers.
 * Restructured code to use more common URL related constants in entry
   point values and replace @RequestMapping with method specific
   annotations.
 * Added first unit test code.
 * Updated web GUI to work with the updated API.
 * Changed integer fields, enforce them as numeric.
 * Rewrote and simplify metadata handling to use common service and
   controller code instead of duplicating for each type.
 * Implemented the remaining metadata types.
 * Changed Country list source from Wikipedia to Debian iso-codes and
   updated the list of Countries.
 * Many many corrections and improvements.

Release 0.4 2019-05-22 (commit 18d69a0dafa2f776bfae3f6d8b3835d6faba70c1)
----------------------
 * Roll out OData handling to all endpoints where applicable.
 * Changed the relation key for "ny-journalpost" to the official one.
 * Better link generation on outgoing links.
 * Tidy up code and make code and approaches more consistent throughout
   the codebase.
 * Update rels to be in compliance with updated version in the
   interface standard.
 * Avoid printing links on empty objects as they can't have links.
 * Small bug fixes and improvements.
 * Start moving generation of outgoing links to @Service layer so
   access control can be used when generating links.
 * Log exception that was being swallowed so it's traceable.
 * Fix name mapping problem.
 * Update templated printing so templated should only be printed if it
   is set true. Requires more work to roll out across entire
   application.
 * Remove Record->DocumentObject as per domain model of n5v5.
 * Add ability to delete lists filtered with OData.
 * Return NO_CONTENT (204) on delete as per interface standard.
 * Introduce support for ConstraintViolationException exception.
 * Make Service classes extend NoarkService.
 * Make code base respect X-Forwarded-Host, X-Forwarded-Proto and
   X-Forwarded-Port.
 * Update CorrespondencePart* code to be more in line with Single
   Responsibility Principle.
 * Make package name follow directory structure.
 * Make sure Document number starts at 1, not 0.
 * Fix isues discovered by FindBugs.
 * Update from Date to OffsetDateTime.
 * Fix wrong tablename.
 * Introduce Service layer tests.
 * Improvements to CorrespondencePart.
 * Continued work on Class / Classificationsystem.
 * Fix feature where authors were stored as storageLocations.
 * Update HQL builder for OData.
 * Update OData search capability from webpage.

Release 0.3 2019-03-22 (commit 7d712e2e796ab86013915c027db17bb6f3416f16)
----------------------
 * Tidied up inconsistencies in how ClassificationSystem and Class are  
   developed.
 * Tidied up known inconsistencies between domain model and hateaos
   links.
 * Added experimental code for blockchain integration. 	
 * Make token expiry time configurable at upstart from properties file.
 * Continued work on OData search syntax.
 * Started work on pagination for entities, partly implemented for
   Saksmappe.
 * Finalise ClassifiedCode Metadata entity.
 * Implement mechanism to check if authentication token is still valid.
   This allows the GUI to return a more sensible message to the user if
   the token is expired.
 * Reintroduce browse.html page to allow user to browse JSON API using
   hateoas links.
 * Fix bug in handling file/mappe sequence number.  Year change was
   not properly handled.
 * Update application yml files to be in sync with current development.
 * Stop 'converting' everything to PDF using libreoffice.  Only
   convert the file formats doc, ppt, xls, docx, pptx, xlsx, odt, odp
   and ods.
 * Continued code style fixing, making code more readable.
 * Minor bug fixes.

Release 0.2.1 2018-11-02 (commit 2bf7dfb7f39067c09b9db1d473b4b2c42cd602de)
----------------------
 * Fixed bug resulting in a class cast exception.
 * Corrected handling of administrativEnhet.
 * Fixed bug in some \_links href entries containing double slashes.
 * Reintroduce Allow headers for OPTIONS.
 * Fixed bug when creating saksmappe, failing to find the correct
   administrativEnhet when there were several with identical names.
 * Fixed bug causing empty \_links when creating dokumentbeskrivelse.

Release 0.2 2018-10-17 (commit d7c3feaa945e2b0cfc19055a201091c7b3840b89)
------------------------
 * Fix typos in REL names.
 * Tidy up error message reporting.
 * Fix issue where we used Integer.valueOf(), not Integer.getInteger().
 * Change some String handling to StringBuffer.
 * Fix error reporting.
 * Code tidy-up.
 * Fix issue using static non-synchronized SimpleDateFormat to avoid
   race conditions.
 * Fix problem where deserialisers were treating integers as strings.
 * Update methods to make them null-safe.
 * Fix many issues reported by coverity.
 * Improve equals(), compareTo() and hash() in domain model.
 * Improvements to the domain model for metadata classes.
 * Fix CORS issues when downloading document.
 * Implementation of case-handling with registryEntry and document
   upload.
 * Better support in Javascript for OPTIONS.
 * Adding concept description of mail integration.
 * Improve setting of default values for GET on ny-journalpost.
 * Better handling of required values during deserialisation.
 * Changed tilknyttetDato (M620) from date to dateTime.
 * Corrected some opprettetDato (M600) (de)serialisation errors.
 * Improve parse error reporting.
 * Started on OData search and filtering.
 * Added Contributor Covenant Code of Conduct to project.
 * Moved repository and project from Github to Gitlab.
 * Restructured repository, moved code into src/ and web/.
 * Updated code to use Spring Boot version 2.
 * Added support for OAuth2 authentication.
 * Fixed several bugs discovered by Coverity.
 * Corrected handling of date/datetime fields.
 * Improved error reporting when rejecting during deserializatoin.
 * Adjusted default values provided for ny-arkivdel, ny-mappe,
   ny-saksmappe, ny-journalpost and ny-dokumentbeskrivelse.
 * Several fixes for korrespondansepart*.
 * Updated web GUI:
    - Now handle both file upload and download.
    - Uses new OAuth2 authentication for login.
    - Forms now fetches default values from API using GET.
    - Added RFC 822 (email), TIFF and JPEG to list of possible file formats.

Release 0.1.1 2017-06-09 (commit a3932c87b22aee272e2a0385bb8a7d029a73faf4)
--------------------------------------------------------------------------
 * Continued work on the angularjs GUI, including document upload.
 * Implemented correspondencepartPerson, correspondencepartUnit and
   correspondencepartInternal.
 * Applied for coverity coverage and started submitting code on regular
   basis.
 * Started fixing bugs reported by coverity.
 * Corrected and completed HATEOAS links to make sure entire API is
   available via URLs in \_links.
 * Corrected all relation URLs to use trailing slash.
 * Add initial support for storing data in ElasticSearch.
 * Now able to receive and store uploaded files in the archive.
 * Changed JSON output for object lists to have relations in \_links.
 * Improve JSON output for empty object lists.
 * Now uses correct MIME type application/vnd.noark5-v4+json.
 * Added support for docker container images
 * Added simple API browser implemented in JavaScript/Angular.
 * Started on archive client implemented in JavaScript/Angular.
 * Started on prototype to show the public mail journal.
 * Improved performance by disabling Sprint FileWatcher.
 * Added support for 'arkivskaper', 'saksmappe' and 'journalpost'.
 * Added support for some metadata codelists.
 * Added support for Cross-origin resource sharing (CORS).
 * Changed login method from Basic Auth to JSON Web Token (RFC 7519)
   style.
 * Added support for GET-ing ny-* URLs.
 * Added support for modifying entities using PUT and eTag.
 * Added support for returning XML output on request.
 * Removed support for English field and class names, limiting ourself
   to the official names.
 * ...

Release 0.1 2017-01-31 (commit 6ec4acb9c1d5b72fd4bf58074769233e78483bb4)
-----------------------
 * Able to store archive metadata.
