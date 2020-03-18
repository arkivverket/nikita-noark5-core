/**
 * Enables the following functionality:
 *  1. Sets the caseFile object correctly for the arkiv.html page
 *  2. Allows a user to POST the contents of the arkiv data
 *
 *  Note: Because we have trouble getting
 *
 *
 *  if ($scope.selectedDocumentMedium == 'Elektronisk arkiv '. Note the extra space. Needs to be removed later!
 *
 *  Note, if we load the page from a list, we are missing the etag to update the object. We could handle this by
 *  'locking' the record in the GUI and make the user 'unlock' it and then issue a GET. Alternatively we could issue a
 *  GET as soon as a user changes something. Both of these require a lot of effort JS-side.
 *
 *  So to keep things simple, we always issue a GET on the load of arkiv.html so we have an ETAG handy!
 *
 */


var caseHandlerController = app.controller('CaseHandlerController',
    ['$scope', '$http', 'loginService', 'nikitaService',
        function ($scope, $http, loginService, nikitaService) {
            // Grab a copy of the authentication token
            $scope.token = GetUserToken();
            console.log("CaseHandler view. Token is " + $scope.token);
            // get values used in drop downs. These will probably be replaced
            // by metadata calls to nikita
            $scope.documentMediumList = documentMediumList;
            $scope.storageLocationList = storageLocationList;
            $scope.caseFileStatusList = caseFileStatusList;
            $scope.registryEntryStatusList = registryEntryStatusList;
            $scope.registryEntryTypeList = registryEntryTypeList;
            $scope.documentStatusList = documentStatusList;
            $scope.variantFormatList = variantFormatList;
            $scope.formatList = formatList;
            $scope.correspondencePartTypeList = correspondencePartTypeList;
            $scope.selectedCorrespondencePartType = "Avsender";


            $scope.associatedWithRecordAsList = associatedWithRecordAsList;
            $scope.selectedStorageLocation = "Sentralarkivet";

            $scope.showCaseFileBreadcrumb = false;
            $scope.showDocumentBreadcrumb = false;
            $scope.showRegistryEntryBreadcrumb = false;
            $scope.showRegistryEntryListBreadcrumb = false;
            $scope.showCorrespondencePartBreadcrumb = false;
            $scope.showDocumentListBreadcrumb = false;
            $scope.showCorrespondencePartListBreadcrumb = false;

            $scope.newCaseResponsibleForCaseFile = GetUsername();
            $scope.newCaseStatusForCaseFile = "Opprettet av saksbehandler";

            $scope.registryEntryList = [];


            // TODO : DELETE THESE
            $scope.newTitleForRegistryEntry = "";
            $scope.newPublicTitleForRegistryEntry = "";
            $scope.newDescriptionForRegistryEntry = "";
            $scope.newRegistryEntryStatus = "Journalført";
            $scope.newRegistryEntryType = "Inngående dokument";
            $scope.newDocumentType = "Brev";
            $scope.newTitleForDocument = "";
            $scope.selectedDocumentType = "Brev";
            $scope.selectedFormat = "odt";
            $scope.selectedVariantFormat = "Produksjonsformat";

            $scope.documentObjectList = [];

            $scope.disableAllButtons = false;

            $scope.newCorrespondencepartPerson = {};
            $scope.newCorrespondencepartPerson.postadresse = {};
            $scope.newCorrespondencepartPerson.bostedsadresse = {};
            $scope.newCorrespondencepartPerson.kontaktinformasjon = {};

            // Disable all cards except the caseFile list one
            $scope.showCaseFileListCard = true;
            $scope.showCaseFileCard = false;
            $scope.showDocumentListCard = false;
            $scope.showCorrespondencePartListCard = false;
            $scope.showCorrespondencePartCard = false;
            $scope.showDocumentCard = false;
            $scope.showRegistryEntryListCard = false;
            $scope.showRegistryEntryCard = false;

            // Set default values for drop downs
            $scope.selectedCaseFileStatus = "Opprettet";
            $scope.selectedDocumentMediumCaseFile = "";
            $scope.selectedDocumentMediumNewCaseFile = "Elektronisk arkiv";
            $scope.selectedAssociatedWithRecordAs = "Hoveddokument";
            $scope.selectedDocumentStatus = "Dokumentet er under redigering";

            // Create variables to bind with ng-model and modals so we can blank them out
            // For caseFile
            $scope.newDescriptionForCaseFile = "";
            $scope.newTitleForCaseFile = "";
            $scope.newTitleForCaseFile = "";
            // For document
            $scope.newIdForDocument = "";

            $scope.newDescriptionForDocument = "";

            // For RegistryEntry
            $scope.selectedDocumentMediumRegistryEntry = "";
            $scope.selectedDocumentMediumNewRegistryEntry = "";
            $scope.newDescriptionForRegistryEntry = "";
            $scope.newTitleForRegistryEntry = "";

            // GET the application root. There you get a HREF to REL_CASEFILE_STRUCTURE
            // Then you GET the REL_CASEFILE_STRUCTURE. Make a note of HREFS for:
            //    REL_CASEFILE_STRUCTURE : Get a list of all caseFile
            //    REL_CASEFILE_STRUCTURE_NEW_CaseFile : Create a new caseFile


            // GET the application root.
            // From here you can used REL_FONDS_STRUCTURE to get a list of all fonds
            (async () => {
                try {


                    let seriesList = await nikitaService.getSeriesList($scope.token);
                    $scope.seriesList = seriesList.results;
                    $scope.$apply($scope.seriesList);
                    $scope.selectedSeries = $scope.seriesList[0];
                    $scope.caseFileList = await nikitaService.getCaseFileList($scope.token, $scope.selectedSeries);
                    if ($scope.caseFileList === undefined) {
                        $scope.caseFileList = [];
                    }
                    $scope.$apply($scope.caseFileList);

                } catch (error) {
                    console.log(error.message);
                }
            })();

            /**
             * updateCaseFile
             *
             * Undertakes a PUT request to the core with data fields from the webpage
             *
             */
            $scope.updateCaseFile = function () {
                let url = $scope.caseFile._links[REL_SELF].href;
                console.log("Attempting to update caseFile with " + url);
                console.log("ETAG being used is  " + $scope.caseFileETag);

                let caseFileStatus = {};

                for (let i = 0; i < caseFileStatusList.length; i++) {
                    if (caseFileStatusList[i].value === $scope.selectedCaseFileStatus) {
                        caseFileStatus.kode = caseFileStatusList[i].id;
                        caseFileStatus.kodenavn = caseFileStatusList[i].value;
                        break;
                    }
                }

                $http({
                    url: url,
                    method: "PUT",
                    headers: {
                        'Content-Type': 'application/vnd.noark5+json',
                        'Authorization': $scope.token,
                        'ETag': $scope.caseFileETag
                    },
                    data: {
                        tittel: $scope.caseFile.tittel,
                        beskrivelse: $scope.caseFile.beskrivelse,
                        offentligTittel: $scope.caseFile.offentligTittel,
                        saksansvarlig: $.trim($scope.caseFile.saksansvarlig),
                        saksstatus: caseFileStatus
                    }
                }).then(function successCallback(response) {
                        console.log(" put on caseFile data returned= " + JSON.stringify(response.data));
                        // Pick up and make a note of the ETAG so we can update the object
                        $scope.caseFileETag = response.headers('ETag');
                        $scope.caseFile = response.data;
                    },
                    function errorCallback(response) {
                        if (response.status == -1) {
                            console.log(MSG_NIKITA_DOWN_LOG + JSON.stringify(response));
                            alert(MSG_NIKITA_DOWN);
                        } else {
                            console.log(MSG_NIKITA_UNKNOWN_ERROR_LOG + JSON.stringify(response));
                            alert(MSG_NIKITA_UNKNOWN_ERROR);
                        }
                    });
            };

            /**
             * updateRegistryEntry
             *
             * Undertakes a PUT request to the core with data fields from the webpage
             *
             */
            $scope.updateRegistryEntry = function () {

                let url = $scope.registryEntry._links[REL_SELF].href;
                console.log(" Attempting to update registryEntry with following address = " + url);
                console.log(" Current ETAG is = [" + $scope.registryEntryETag + "]");
                $http({
                    url: url,
                    method: "PUT",
                    headers: {
                        'Content-Type': 'application/vnd.noark5+json',
                        'Authorization': $scope.token,
                        'ETag': $scope.registryEntryETag
                    },
                    data: {
                        tittel: $scope.registryEntry.tittel,
                        beskrivelse: $scope.registryEntry.beskrivelse,
                        dokumentmedium: {
                            kode: 'E',
                            kodenavn: 'Elektronisk arkiv'
                        },
                        journalstatus: $scope.selectedRegistryEntryStatus
                    },
                }).then(function successCallback(response) {
                        console.log(" put on registryEntry data returned= " + JSON.stringify(response.data));
                        // Pick up and make a note of the ETAG so we can update the object
                        $scope.registryEntryETag = response.headers('ETag');
                        $scope.registryEntry = response.data;
                    },
                    function errorCallback(response) {
                        if (response.status == -1) {
                            console.log(MSG_NIKITA_DOWN_LOG + JSON.stringify(response));
                            alert(MSG_NIKITA_DOWN);
                        } else {
                            console.log(MSG_NIKITA_UNKNOWN_ERROR_LOG + JSON.stringify(response));
                            alert(MSG_NIKITA_UNKNOWN_ERROR);
                        }
                    });
            };


            /**
             * updateDocument
             *
             * Undertakes either a PUT request to the core with data fields from the webpage
             *
             * The action is decided by whether or not $scope.createCaseFile == true. If it's true then we will
             * create a caseFile. If it's false, we are updating a caseFile.
             *
             */

            $scope.updateDocument = function () {

                let url = $scope.document._links[REL_SELF].href;
                console.log(" Attempting to update documentDescription with following address = " + url);
                console.log(" Current ETAG is = [" + $scope.documentETag + "]");
                $http({
                    url: url,
                    method: "PUT",
                    headers: {
                        'Content-Type': 'application/vnd.noark5+json',
                        'Authorization': $scope.token,
                        'ETAG': $scope.documentETag
                    },
                    data: {
                        arkivskaperID: $scope.document.arkivskaperID,
                        arkivskaperNavn: $scope.document.arkivskaperNavn,
                        beskrivelse: $scope.document.beskrivelse
                    },
                }).then(function successCallback(response) {
                    console.log(" PUT on document data returned= " + JSON.stringify(response.data));
                    $scope.documentETag = response.headers('ETag');
                    // Update the document object so fields in GUI are changed
                    $scope.document = response.data;
                    // Pick up and make a note of the ETAG so we can update the object
                    console.log("Etag after PUT on document is = " + $scope.documentETag);
                    // Now we can edit the caseFile object, add document
                }, function errorCallback(request, response) {
                    console.log(method + " PUT request = " + JSON.stringify(request));
                });
            };

            /**
             *
             * create a new documentDescription associated with the current registryEntry
             */
            $scope.createDocumentDescription = function () {

                let url = $scope.registryEntry._links[REL_NEW_DOCUMENT_DESCRIPTION].href;

                let associatedWithRecordAsCode = "H";
                let associatedWithRecordAsCodeName = "Hoveddokument";
                for (i = 0; i < associatedWithRecordAsList.length; i++) {
                    if (associatedWithRecordAsList[i].value === $scope.selectedAssociatedWithRecordAs) {
                        associatedWithRecordAsCode = associatedWithRecordAsList[i].id;
                        associatedWithRecordAsCodeName = associatedWithRecordAsList[i].value;
                    }
                }
                console.log("Setting associatedWithRecordAsCode " + associatedWithRecordAsCode);
                console.log("Setting associatedWithRecordAsCodeName " + associatedWithRecordAsCodeName);

                let documentStatusCode = "B";
                let documentStatusCodeName = "Dokumentet er under redigering";
                for (i = 0; i < documentStatusList.length; i++) {
                    if (documentStatusList[i].value === $scope.selectedDocumentStatus) {
                        documentStatusCode = documentStatusList[i].id;
                        documentStatusCodeName = documentStatusList[i].value;
                    }
                }
                console.log("Setting documentStatusCode " + documentStatusCode);
                console.log("Setting documentStatusCodeName " + documentStatusCodeName);

                console.log("Calling create documentDescription with " + url);
                $http({
                    url: url,
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/vnd.noark5+json',
                        'Authorization': $scope.token,
                    },
                    data: {
                        'tittel': $scope.newTitleForDocument,
                        'beskrivelse': $scope.newDescriptionForDocument,
                        'tilknyttetRegistreringSom': {
                            kode: associatedWithRecordAsCode,
                            kodenavn: associatedWithRecordAsCodeName
                        },
                        'dokumentstatus': {
                            kode: documentStatusCode,
                            kodenavn: documentStatusCodeName
                        },
                        'dokumenttype': {
                            kode: 'R',
                            kodenavn: 'Rundskriv'
                        }
                    },
                }).then(function successCallback(response) {
                    console.log("POST on document data returned= " + JSON.stringify(response.data));
                    $scope.doDismissNewDocumentDescriptionModal();

                    // Update the document object so fields in GUI are changed
                    $scope.document = response.data;

                    // Pick up and make a note of the ETAG so we can update the object
                    $scope.documentETag = response.headers('eTag');
                    $scope.documentETag = '"' + $scope.documentETag + '"';
                    console.log("Etag after post on document is = " + $scope.documentETag);
                });
            };

            /**
             *
             * create a new documentObject associated with the current documentDescription
             */
            $scope.createDocumentObject = function () {

                for (let i = 0; i < formatList.length; i++) {
                    if (formatList[i].id === $scope.selectedFormat)
                        $scope.selectedMimeType = formatList[i].value;
                }
                console.log("Setting mimetype to " + $scope.selectedMimeType);


                let variantFormatCode = "P";
                let variantFormatCodeName = "Produksjonsformat";
                for (i = 0; i < variantFormatList.length; i++) {
                    if (variantFormatList[i].value === $scope.selectedVariantFormat) {
                        variantFormatCode = variantFormatList[i].id;
                        variantFormatCodeName = variantFormatList[i].value;
                    }
                }
                console.log("Setting variantFormatCode " + variantFormatCode);
                console.log("Setting variantFormatCodeName " + variantFormatCodeName);

                let formatCode = "fmt/136";
                let formatCodeName = "OpenDocument Text (odt)";
                for (i = 0; i < formatList.length; i++) {
                    if (formatList[i].value === $scope.selectedFormat) {
                        formatCode = formatList[i].id;
                        formatCodeName = formatList[i].value;
                    }
                }
                console.log("Setting formatCode " + formatCode);
                console.log("Setting formatCodeName " + formatCodeName);

                let url = $scope.documentDescription._links[REL_NEW_DOCUMENT_OBJECT].href;
                console.log("Calling create documentDescription with " + url);

                $http({
                    url: url,
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/vnd.noark5+json',
                        'Authorization': $scope.token,
                    },
                    data: {
                        'format': $scope.selectedFormat,
                        'mimeType': $scope.selectedMimeType,
                        'variantformat': {
                            kode: variantFormatCode,
                            kodenavn: variantFormatCodeName
                        },
                        'format': {
                            kode: formatCode,
                            kodenavn: formatCodeName
                        }
                    }
                }).then(function successCallback(response) {
                    console.log("POST on document data returned= " + JSON.stringify(response.data));
                    $scope.doDismissNewDocumentObjectModal();

                    $scope.document = response.data;
                    if ($scope.documentObjectList === undefined) {
                        $scope.documentObjectList = [];
                    }

                    $scope.documentObjectList.push(response.data);

                    // Pick up and make a note of the ETAG so we can update the object
                    $scope.documentETag = response.headers('eTag');
                    $scope.documentETag = '"' + $scope.documentETag + '"';
                    console.log("Etag after post on document is = " + $scope.documentETag);
                });
            };


            /**
             *
             * create a new correspondencePart associated with the current registryEntry
             */
            $scope.createCorrespondencePart = function () {

                let url = $scope.registryEntry._links[REL_NEW_CORRESPONDENCE_PART_PERSON].href;
                console.log("Calling create CorrespondencePartPerson with " + url);

                $http({
                    url: url,
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/vnd.noark5+json',
                        'Authorization': $scope.token,
                    },
                    data: {
                        korrespondanseparttype: {
                            kode: "EA",
                            beskrivelse: $scope.selectedCorrespondencePartType
                        },
                        foedselsnummer: $scope.newCorrespondencepartPerson.foedselsnummer,
                        dNummer: $scope.newCorrespondencepartPerson.dNummer,
                        navn: $scope.newCorrespondencepartPerson.navn,
                        postadresse: {
                            adresselinje1: $scope.newCorrespondencepartPerson.postadresse.adresselinje1,
                            adresselinje2: $scope.newCorrespondencepartPerson.postadresse.adresselinje2,
                            adresselinje3: $scope.newCorrespondencepartPerson.postadresse.adresselinje3,
                            postnummer: $scope.newCorrespondencepartPerson.postadresse.postnummer,
                            poststed: $scope.newCorrespondencepartPerson.postadresse.poststed,
                            landkode: $scope.newCorrespondencepartPerson.postadresse.landkode
                        },
                        bostedsadresse: {
                            adresselinje1: $scope.newCorrespondencepartPerson.bostedsadresse.adresselinje1,
                            adresselinje2: $scope.newCorrespondencepartPerson.bostedsadresse.adresselinje2,
                            adresselinje3: $scope.newCorrespondencepartPerson.bostedsadresse.adresselinje3,
                            postnummer: $scope.newCorrespondencepartPerson.bostedsadresse.postnummer,
                            poststed: $scope.newCorrespondencepartPerson.bostedsadresse.poststed,
                            landkode: $scope.newCorrespondencepartPerson.bostedsadresse.landkode
                        },
                        kontaktinformasjon: {
                            epostadresse: $scope.newCorrespondencepartPerson.kontaktinformasjon.epostadresse,
                            mobiltelefon: $scope.newCorrespondencepartPerson.kontaktinformasjon.mobiltelefon,
                            telefonnummer: $scope.newCorrespondencepartPerson.kontaktinformasjon.telefonnummer
                        }
                    },
                }).then(function successCallback(response) {
                    console.log("POST on document data returned= " + JSON.stringify(response.data));
                    $scope.doDismissNewDocumentDescriptionModal();

                    // Update the document object so fields in GUI are changed
                    $scope.correspondencepartPerson = response.data;

                    // Pick up and make a note of the ETAG so we can update the object
                    $scope.documentETag = response.headers('eTag');
                    $scope.documentETag = '"' + $scope.documentETag + '"';
                    console.log("Etag after post on document is = " + $scope.documentETag);
                });
            };


            /**
             *
             * create a new RegistryEntry associated with the current caseFile
             */
            $scope.createRegistryEntry = function () {

                let url = $scope.caseFile._links[REL_NEW_REGISTRY_ENTRY].href;
                console.log("Calling create RegistryEntry with " + url);

                let journalpostStatusCode = "J";
                let journalpostStatusCodeName = "Journalført";
                for (i = 0; i < registryEntryStatusList.length; i++) {
                    if (registryEntryStatusList[i].value === $scope.newRegistryEntryStatus) {
                        journalpostStatusCode = registryEntryStatusList[i].id;
                        journalpostStatusCodeName = registryEntryStatusList[i].value;
                    }
                }
                console.log("Setting journalpostStatusCode " + journalpostStatusCode);
                console.log("Setting journalpostStatusCodeName " + journalpostStatusCodeName);

                let journalpostTypeCode = "J";
                let journalpostTypeCodeName = "Journalført";
                for (i = 0; i < registryEntryTypeList.length; i++) {
                    if (registryEntryTypeList[i].value === $scope.newRegistryEntryType) {
                        journalpostTypeCode = registryEntryTypeList[i].id;
                        journalpostTypeCodeName = registryEntryTypeList[i].value;
                    }
                }
                console.log("Setting journalpostTypeCode " + journalpostTypeCode);
                console.log("Setting journalpostTypeCodeName " + journalpostTypeCodeName);

                $http({
                    url: url,
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/vnd.noark5+json',
                        'Authorization': $scope.token,
                    },
                    data: {
                        tittel: $scope.newTitleForRegistryEntry,
                        beskrivelse: $scope.newDescriptionForRegistryEntry,
                        dokumentmedium: {
                            kode: 'E',
                            kodenavn: 'Elektronisk arkiv'
                        },
                        journalposttype: {
                            kode: journalpostTypeCode,
                            kodenavn: journalpostTypeCodeName
                        },
                        journalstatus: {
                            kode: journalpostStatusCode,
                            kodenavn: journalpostStatusCodeName
                        }
                    },
                }).then(function successCallback(response) {
                        console.log("POST on registryEntry data returned= " + JSON.stringify(response.data));
                        $scope.doDismissNewRegistryEntryModal();
                        // Update the document object so fields in GUI are changed
                        $scope.registryEntry = response.data;
                        $scope.registryEntryList.push($scope.registryEntry);

                        // Pick up and make a note of the ETAG so we can update the object
                        $scope.registryEntryETag = response.headers('eTag');
                        $scope.registryEntryETag = '"' + $scope.registryEntryETag + '"';
                        console.log("Etag after post on registryEntry is = " + $scope.registryEntryETag);
                    },
                    function errorCallback(response) {
                        if (response.status == -1) {
                            console.log(MSG_NIKITA_DOWN_LOG + JSON.stringify(response));
                            alert(MSG_NIKITA_DOWN);
                        } else {
                            console.log(MSG_NIKITA_UNKNOWN_ERROR_LOG + JSON.stringify(response));
                            alert(MSG_NIKITA_UNKNOWN_ERROR);
                        }
                    });
            };

            /**
             * createCaseFile
             *
             * Undertakes POST request to the core with data fields from the webpage
             * Results in a new caseFile object being created and returned. Web-page is
             * updated so the newly returned caseFile is shown.
             */
            $scope.createCaseFile = function () {
                let url = $scope.selectedSeries._links[REL_NEW_CASE_FILE].href;
                console.log("Calling createCaseFile with " + url);
                $http({
                    url: url,
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/vnd.noark5+json',
                        'Authorization': $scope.token,
                    },
                    data: {
                        tittel: $.trim($scope.newTitleForCaseFile),
                        offentligTittel: $.trim($scope.newPublicTitleForCaseFile),
                        beskrivelse: $.trim($scope.newDescriptionForCaseFile),
                        dokumentmedium: {
                            kode: 'E',
                            kodenavn: 'Elektronisk arkiv'
                        },
                        // Temporary removed. Will add in administrativeunit functionality and this will have values
                        // Same with keyword. Waiting to check implementation
                        // noekkelord: $.trim($scope.newKeywordForCaseFile),
                        // administrativEnhet: $.trim($scope.newAdministrativeUnitForCaseFile),
                        saksansvarlig: $.trim($scope.newCaseResponsibleForCaseFile),
                        saksstatus: {
                            "kode": "R",
                            "kodenavn": "Opprettet av saksbehandler"
                        }
                    }
                }).then(function successCallback(response) {
                        console.log("POST to create new caseFile data returned= " + JSON.stringify(response.data));

                        $scope.doDismissNewCaseFileModal();
                        // Update the caseFile object so fields in GUI are changed
                        $scope.caseFile = response.data;
                        $scope.caseFileList.push($scope.caseFile);
                        // Pick up and make a note of the ETAG so we can update the object
                        $scope.caseFileETag = response.headers('eTag');
                        $scope.caseFileETag = '"' + $scope.caseFileETag + '"';

                        for (let i = 0; i < caseFileStatusList.length; i++) {
                            if (caseFileStatusList[i].id === $scope.caseFile.saksstatus.kode) {
                                $scope.selectedCaseFileStatus = caseFileStatusList[i].value;
                                break;
                            }
                        }
                    },
                    function errorCallback(response) {
                        if (response.status == -1) {
                            console.log(MSG_NIKITA_DOWN_LOG + JSON.stringify(response));
                            alert(MSG_NIKITA_DOWN);
                        } else {
                            console.log(MSG_NIKITA_UNKNOWN_ERROR_LOG + JSON.stringify(response));
                            alert(MSG_NIKITA_UNKNOWN_ERROR);
                        }
                    }
                )
            };


            /**
             * As this is a single-page-application, we need to hide and show cards.
             * This is a helper method to hide all cards and the caller can then
             * show the card they want to show.
             *
             */
            function disableAllCards() {
                $scope.showCaseFileListCard = false;
                $scope.showCaseFileCard = false;
                $scope.showRegistryEntryListCard = false;
                $scope.showRegistryEntryCard = false;
                $scope.showDocumentListCard = false;
                $scope.showDocumentCard = false;
                $scope.showCorrespondencePartListCard = false;
                $scope.showCorrespondencePartCard = false;
            }

            /**
             * As this is a single-page-application, we need to hide and show breadcrumbs.
             * This is a helper method to hide all breadcrumbs and the caller can then
             * show the breadcrumb they want to show.
             *
             */
            function hideAllBreadcrumbs() {
                $scope.showCaseFileBreadcrumb = false;
                $scope.showRegistryEntryListBreadcrumb = false;
                $scope.showRegistryEntryBreadcrumb = false;
                $scope.showDocumentListBreadcrumb = false;
                $scope.showDocumentBreadcrumb = false;
                $scope.showCorrespondencePartListBreadcrumb = false;
                $scope.showCorrespondencePartBreadcrumb = false;
            }

            $scope.doShowCaseFileListCard = function () {
                (async () => {
                    try {
                        $scope.caseFileList = await nikitaService.getCaseFileList($scope.token, $scope.selectedSeries);
                        $scope.$apply($scope.caseFileList);
                    } catch (error) {
                        console.log(error.message);
                    }
                })();
                disableAllCards();
                $scope.showCaseFileListCard = true;
                hideAllBreadcrumbs();
            };

            $scope.doShowCaseFileCard = function () {
                disableAllCards();
                $scope.showCaseFileCard = true;
                hideAllBreadcrumbs();
                $scope.showCaseFileBreadcrumb = true;
            };

            $scope.doShowRegistryEntryCard = function () {
                disableAllCards();
                hideAllBreadcrumbs();
                $scope.showRegistryEntryCard = true;
                $scope.showCaseFileBreadcrumb = true;
                $scope.showRegistryEntryListBreadcrumb = true;
                $scope.showRegistryEntryBreadcrumb = true;
            };

            $scope.doShowCorrespondencePartCard = function () {
                disableAllCards();
                hideAllBreadcrumbs();
                $scope.showCaseFileBreadcrumb = true;
                $scope.showRegistryEntryListBreadcrumb = true;
                $scope.showRegistryEntryBreadcrumb = true;
                $scope.showCorrespondencePartListBreadcrumb = true;
                $scope.showCorrespondencePartBreadcrumb = true;
                $scope.showCorrespondencePartCard = true;
            };


            $scope.doShowDocumentCard = function () {
                disableAllCards();
                hideAllBreadcrumbs();
                $scope.showCaseFileBreadcrumb = true;
                $scope.showRegistryEntryListBreadcrumb = true;
                $scope.showRegistryEntryBreadcrumb = true;
                $scope.showDocumentListBreadcrumb = true;
                $scope.showDocumentBreadcrumb = true;
                $scope.showDocumentCard = true;
            };

            $scope.doShowDocumentListCard = function () {
                disableAllCards();
                $scope.showDocumentListCard = true;
                hideAllBreadcrumbs();
                $scope.showCaseFileBreadcrumb = true;
                $scope.showRegistryEntryListBreadcrumb = true;
                $scope.showRegistryEntryBreadcrumb = true;
                $scope.showDocumentListBreadcrumb = true;
                $scope.getListDocument();
            };

            $scope.doShowCorrespondencePartListCard = function () {
                disableAllCards();
                hideAllBreadcrumbs();
                $scope.showCaseFileBreadcrumb = true;
                $scope.showRegistryEntryListBreadcrumb = true;
                $scope.showRegistryEntryBreadcrumb = true;
                $scope.showCorrespondencePartListBreadcrumb = true;
                $scope.showCorrespondencePartListCard = true;
                $scope.getListCorrespondencePart();
            };

            $scope.doShowRegistryEntryListCard = function () {
                disableAllCards();
                hideAllBreadcrumbs();
                $scope.showRegistryEntryListCard = true;
                $scope.showRegistryEntryListBreadcrumb = true;
                $scope.showCaseFileBreadcrumb = true;
                $scope.getRegistryEntry();
            };

            $scope.doDismissNewCaseFileModal = function () {
                $scope.dismissNewCaseFileModal();
                $scope.newTitleForCaseFile = "";
                $scope.newDescriptionForCaseFile = "";
            };

            $scope.doDismissNewRegistryEntryModal = function () {
                $scope.dismissNewRegistryEntryModal();
                $scope.newIdForDocument = "";
                $scope.newNameForDocument = "";
                $scope.newDescriptionForDocument = "";
            };

            $scope.doDismissNewDocumentDescriptionModal = function () {
                $scope.dismissNewDocumentDescriptionModal();
                $scope.newDescriptionForDocument = "";
                $scope.newTitleForDocument = "";
            };

            $scope.doDismissNewDocumentObjectModal = function () {
                $scope.dismissNewDocumentObjectModal();
            };

            $scope.doDismissNewCorrespondencePartModal = function () {
                $scope.dismissNewCorrespondencePartModal();
            };

            $scope.doLoadRegistryEntryModal = function () {
                $scope.selectedDocumentMediumNewRegistryEntry = $scope.caseFile.dokumentmedium;
                $scope.newRegistryEntryStatus = 'Journalført';
                $scope.newRegistryEntryType = 'Inngående dokument';
            };

            $scope.doLoadDocumentDescriptionModal = function () {
                $scope.newDocumentType = "";
                $scope.newTitleForDocument = "";
                $scope.selectedDocumentType = "";
            };

            $scope.doLoadCorrespondencePartModal = function () {

            };

            $scope.checkDocumentMediumForRegistryEntry = function () {

                console.log("Change detected " + $scope.caseFile.dokumentmedium + " " + $scope.selecteddoLoadRegistryEntryModalDocumentMediumRegistryEntry);
                if ($scope.caseFile.dokumentmedium !== $scope.selectedDocumentMediumRegistryEntry) {

                    if ($scope.caseFile.dokumentmedium === "Elektronisk arkiv") {
                        $scope.selectedDocumentMediumRegistryEntry = $scope.caseFile.dokumentmedium;
                    } else if ($scope.caseFile.dokumentmedium === "Fysisk arkiv") {
                        $scope.selectedDocumentMediumRegistryEntry = $scope.caseFile.dokumentmedium;
                    }
                }
            };

            $scope.correspondencePartSelected = function (correspondencePart) {
                $scope.doShowCorrespondencePartCard();

                // Retrieve the latest copy of the data and pull out the ETAG
                // Find the self link of the current correspondencePart and issue a GET
                let url = correspondencePart._links[REL_SELF].href;
                let token = $scope.token;
                console.log("Retrieving " + url);
                $http({
                    method: 'GET',
                    url: url,
                    headers: {'Authorization': token}
                }).then(function successCallback(response) {
                    $scope.correspondencepartPerson = response.data;
                    $scope.correspondencePartPersonETag = response.headers('eTag');
                    console.log("Retrieved the following correspondencePart " + JSON.stringify($scope.correspondencepartPerson));
                    console.log("The ETAG header for the correspondencePart is " + $scope.correspondencePartPersonETag);
                });
            };

            $scope.caseFileSelected = function (caseFile) {

                $scope.doShowCaseFileCard();
                // Retrieve the latest copy of the data and pull out the ETAG
                // Find the self link of the current caseFile and issue a GET

                $http({
                    method: 'GET',
                    url: caseFile._links[REL_SELF].href,
                    headers: {'Authorization': $scope.token}
                }).then(function successCallback(response) {
                    $scope.caseFile = response.data;
                    $scope.caseFileETag = response.headers('eTag');
                    console.log("Retrieved the following caseFile " + JSON.stringify($scope.caseFile));
                    console.log("The ETAG header for the caseFile is " + $scope.caseFileETag);
                    $scope.selectedDocumentMediumCaseFile = $scope.caseFile.dokumentmedium;

                    for (let i = 0; i < caseFileStatusList.length; i++) {
                        if (caseFileStatusList[i].id === $scope.caseFile.saksstatus.kode)
                            $scope.selectedCaseFileStatus = caseFileStatusList[i].value;
                    }
                    $scope.$apply($scope.caseFile);
                });
            };

            $scope.doGetCorrespondencePartTemplate = function () {

                let url = $scope.registryEntry._links[REL_NEW_CORRESPONDENCE_PART_PERSON].href;
                console.log("url is (" + url + ")");
                $http({
                    method: 'GET',
                    url: url,
                    headers: {'Authorization': $scope.token}
                }).then(function successCallback(response) {
                    console.log("Result from GET (" +
                        href + ") is " +
                        JSON.stringify(response.data));
                    // populate the values in the GUI
                    $scope.newCorrespondencepartPerson = response.data;
                }, function errorCallback(response) {
                    console.log("Problem with call to url [" +
                        $scope.registryEntry._links[REL_NEW_CORRESPONDENCE_PART_PERSON].rel + "] response is "
                        + response);
                });
            };


            $scope.registryEntrySelected = function (registryEntry) {

                $scope.doShowRegistryEntryCard();
                // Retrieve the latest copy of the data and pull out the ETAG
                // Find the self link of the current registryEntry and issue a GET
                $http({
                    method: 'GET',
                    url: registryEntry._links[REL_SELF].href,
                    headers: {'Authorization': $scope.token}
                }).then(function successCallback(response) {
                    $scope.registryEntry = response.data;
                    $scope.registryEntryETag = response.headers('eTag');
                    console.log("Retrieved the following registryEntry " + JSON.stringify($scope.registryEntry));
                    console.log("The ETAG header for the registryEntry is " + $scope.registryEntryETag);
                    $scope.selectedDocumentMediumRegistryEntry = $scope.registryEntry.dokumentmedium;
                    $scope.selectedRegistryEntryStatus = $scope.registryEntry.journalstatus;
                });
            };

            $scope.fetchDocumentObjects = function () {
                $http({
                    method: 'GET',
                    url: $scope.documentDescription._links[REL_DOCUMENT_OBJECT].href,
                    headers: {'Authorization': $scope.token}
                }).then(function successCallback(response) {
                    $scope.documentObjectList = response.data.results;
                    if ($scope.documentObjectList === undefined) {
                        $scope.documentObjectList = [];
                    }
                    console.log("Retrieved the following documentObjectList " + JSON.stringify($scope.documentObjectList));
                });
            };

            $scope.documentSelected = function (document) {
                $scope.doShowDocumentCard();
                // Retrieve the latest copy of the data and pull out the ETAG
                // Find the self link of the current document and issue a GET
                $http({
                    method: 'GET',
                    url: document._links[REL_SELF].href,
                    headers: {'Authorization': $scope.token}
                }).then(function successCallback(response) {
                    $scope.documentDescription = response.data;
                    $scope.selectedAssociatedWithRecordAs = $scope.documentDescription.tilknyttetRegistreringSom.kodenavn;
                    $scope.selectedDocumentStatus = $scope.documentDescription.dokumentstatus.kodenavn;
                    $scope.documentETag = response.headers('eTag');
                    $scope.fetchDocumentObjects();
                    console.log("Retrieved the following document " + JSON.stringify($scope.documentDescription));
                    console.log("The ETAG header for the document is " + $scope.documentETag);
                });
            };

            $scope.getListDocument = function () {
                $http({
                    method: 'GET',
                    url: $scope.registryEntry._links[REL_DOCUMENT_DESCRIPTION].href,
                    headers: {'Authorization': $scope.token}
                }).then(function successCallback(response) {
                    $scope.documentList = response.data.results;
                    console.log("Retrieved the following documentList " +
                        JSON.stringify($scope.documentList));
                });
            };

            $scope.getListCorrespondencePart = function () {
                $http({
                    method: 'GET',
                    url: $scope.registryEntry._links[REL_CORRESPONDENCE_PART].href,
                    headers: {'Authorization': $scope.token}
                }).then(function successCallback(response) {
                    $scope.correspondencePartList = response.data.results;
                    console.log("Retrieved the following correspondencePartList " +
                        JSON.stringify($scope.correspondencePartList));
                });
            };

            $scope.selectDocumentObject = function (selectedDocumentObject) {
                $scope.documentObject = selectedDocumentObject;
                console.log("Setting selected documentObject do to " +
                    JSON.stringify($scope.documentObject));
            };

            $scope.uploadFiles = function (documentObject, file, errFiles) {
                $scope.f = file;

                $scope.errFile = errFiles && errFiles[0];
                if (file) {
                    var xhr = new XMLHttpRequest();
                    xhr.withCredentials = true;
                    xhr.addEventListener("readystatechange", function () {
                        if (this.readyState === 4) {
                            if (this.status != 200 && this.status != 201) {
                                console.log("Status " + this.status);
                                console.log("Status " + this.responseText);
                                alert("Kunne ikke laste opp fil. Kjernen sier følgende: " +
                                    this.responseText + " status " + this.status);
                            } else {
                                $scope.fetchDocumentObjects();
                            }
                        }
                    });
                    xhr.open("POST", documentObject._links[REL_DOCUMENT_FILE].href);
                    var blob = new Blob([file], {type: documentObject.selectedMimeType});
                    xhr.setRequestHeader('content-type', documentObject.selectedMimeType);
                    xhr.setRequestHeader('Authorization', $scope.token);
                    xhr.setRequestHeader("X-File-Name", file.name);
                    xhr.send(blob);
                }
            };

            $scope.getRegistryEntry = function () {
                console.log("url is " + $scope.caseFile._links[REL_REGISTRY_ENTRY].href);
                $http({
                    method: 'GET',
                    url: $scope.caseFile._links[REL_REGISTRY_ENTRY].href,
                    headers: {'Authorization': $scope.token}
                }).then(function successCallback(response) {
                    $scope.registryEntryList = response.data.results;
                    console.log("Retrieved the following registryEntryList " + JSON.stringify($scope.registryEntryList));
                }, function errorCallback(response) {
                    console.log("Problem with call to url [" +
                        $scope.registryEntry._links[REL_REGISTRY_ENTRY].href + "] response is "
                        + response);
                });
            };

            $scope.doLogout = function () {
                (async () => {
                    try {
                        $scope.oidc = await loginService.doLogout();
                        changeLocation($scope, "login.html", false);
                    } catch (error) {
                        console.log("Problem logging out " + error.message)
                    }
                })();
            };

            $scope.seriesSelectedUpdate = function () {

                let url = $scope.selectedSeries._links[REL_SELF].href;
                let token = $scope.token;

                console.log("Getting url  " + url);
                console.log("Getting url  " + token);

                (async () => {
                    try {
                        $scope.caseFileList = await nikitaService.getCaseFileList($scope.token,
                            $scope.selectedSeries);
                        if ($scope.caseFileList === undefined) {
                            $scope.caseFileList = [];
                        }
                        $scope.$apply($scope.caseFileList);
                    } catch (error) {
                        console.log(error.message);
                    }
                })();

            };

            /**
             * download the document. The code here is adapted from:
             *  https://stackoverflow.com/questions/14215049/how-to-download-file-using-angularjs-and-calling-mvc-api
             */
            $scope.doDownloadDocument = function (documentObject) {

                $scope.documentObject = documentObject;

                let type = $scope.documentObject.mimeType;
                let format = $scope.documentObject.format;
                let filename = "mildertidignavn." + format;
                $http({
                    method: 'GET',
                    url: $scope.documentObject._links[REL_DOCUMENT_FILE].href,
                    headers: {
                        'Authorization': $scope.token,
                        'Accept': type
                    },
                    responseType: 'arraybuffer'
                }).then(function successCallback(response) {

                    var file = new Blob([response.data], {
                        type: type
                    });

                    var fileURL = URL.createObjectURL(file);
                    var a = document.createElement('a');
                    a.href = fileURL;
                    a.target = '_blank';
                    a.download = documentObject.filnavn;
                    document.body.appendChild(a);
                    a.click();
                    document.body.removeChild(a);

                }, function errorCallback(response) {
                    alert("Kunne ikke laste ned. " +
                        JSON.stringify(response));
                    console.log(" GET documentHref[" + $scope.documentHref +
                        "] returned " + JSON.stringify(response));
                });
            }
        }
    ])
;

