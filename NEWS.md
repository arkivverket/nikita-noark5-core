Major changes in Nikita Noark 5 Core releases
=============================================

These are the highlevel changes.  For details, see the git history.

Release 0.6 2020-XX-XX (commit X)
----------------------
 * Changed internal URLs, replaced "hateoas-api" with "api".
 * Implemented storing of Precedence.
 * Corrected handling of screening.
 * Corrected \_links collection returned for list of mixed entity
   types to match the specific entity.
 * Improved several internal structures.

Release 0.5 2020-03-02 (commit bf83be0610b1fdc6b56b4c94ab07c3d1716940cb)
----------------------
 * Updated to Noark 5 versjon 5.0 API specification.
    - Changed formatting of \_links from [] to {} to match IETF draft
      on JSON HAL.
    - Merged Registrering og Basisregistrering in version 4 to
      combined Registrering.
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
