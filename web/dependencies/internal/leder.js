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

var leaderController = app.controller('LeaderController',
    ['$scope', '$http', 'loginService', 'nikitaService',
        function ($scope, $http, loginService, nikitaService) {
            // Grab a copy of the authentication token
            $scope.token = GetUserToken();
            console.log("PostMottak view. Token is " + $scope.token);
            // get values used in drop downs. These will probably be replaced
            // by metadata calls to nikita
            //$scope.fileStatusList = fileStatusList;
            $scope.documentStatusList = documentStatusList;
            $scope.variantFormatList = variantFormatList;
            $scope.registryEntryStatusList = registryEntryStatusList;
            $scope.registryEntryTypeList = registryEntryTypeList;
            $scope.formatList = formatList;

            $scope.caseStatusList = caseFileStatusList;
            $scope.selectedCaseStatus = 'Under behandling';
            $scope.associatedWithRecordAsList = associatedWithRecordAsList;

            $scope.showFileBreadcrumb = false;
            $scope.showDocumentBreadcrumb = false;
            $scope.showRegistrationBreadcrumb = false;
            $scope.showRegistrationListBreadcrumb = false;
            $scope.showDocumentListBreadcrumb = false;

            $scope.registrationList = [];
            $scope.userList = [];
            $scope.userList.push('admin@example.com');
            $scope.selectedUser = 'admin@example.com';
            $scope.documentObjectList = [];

            // Disable all cards except the file list one
            $scope.showRegistryEntryListCard = true;
            $scope.showDocumentListCard = false;
            $scope.showDocumentCard = false;
            $scope.showRegistrationListCard = false;
            $scope.showRegistrationCard = false;

            // Set default values for drop downs
            $scope.selectedFileStatus = "Opprettet";
            $scope.selectedAssociatedWithRecordAs = "Hoveddokument";
            $scope.selectedDocumentStatus = "Dokumentet er under redigering";

            $scope.selectedRegistryEntryType = "Utgående dokument";
            $scope.selectedRegistryEntryStatus = "Ferdigstilt fra saksbehandler";

            // Create variables to bind with ng-model and modals so we can blank them out
            // For file
            $scope.newDescriptionForFile = "";
            $scope.newTitleForFile = "";
            $scope.newTitleForFile = "";
            // For document
            $scope.newIdForDocument = "";

            $scope.newDescriptionForDocument = "";

            // For Registration
            $scope.newDescriptionForRegistration = "";

            $scope.documentList = [
                {
                    dokumentnummer: 1,
                    filnavn: "Kul filnavn.pdf",
                    tilknyttetRegistreringSom: {
                        "kode": "H",
                        "kodenavn": "Hoveddokument"
                    },
                    tilknyttetDato: "2020-06-30+02:00"
                },
                {
                    dokumentnummer: 2,
                    filnavn: "Kul filnavn.pdf",
                    tilknyttetRegistreringSom: {
                        "kode": "V",
                        "kodenavn": "Vedlegg"
                    },
                    tilknyttetDato: "2020-06-30+02:00"
                }
            ];

            url = baseUrl + "api/sakarkiv/journalpost?" +
                encodeURIComponent("$filter") + "=" +
                encodeURIComponent("journalstatus/kode eq 'F'");

            console.log("url is " + url);
            $http({
                method: 'GET',
                url: url,
                headers: {'Authorization': $scope.token}
            }).then(function successCallback(response) {
                $scope.registryEntryList = response.data.results;
                console.log("Retrieved the following registryEntryList " + JSON.stringify($scope.registryEntryList));
            }, function errorCallback(response) {
                console.log("Problem with call to url [" +
                    $scope.registryEntry._links[REL_REGISTRY_ENTRY].href + "] response is "
                    + response);
            });


            // GET the application root. There you get a HREF to REL_FILE_STRUCTURE
            // Then you GET the REL_FILE_STRUCTURE. Make a note of HREFS for:
            //    REL_FILE_STRUCTURE : Get a list of all file
            //    REL_FILE_STRUCTURE_NEW_File : Create a new file


            // GET the application root.
            // From here you can used REL_FONDS_STRUCTURE to get a list of all fonds

            /*
                        (async () => {
                            try {

$scope.selectedUser;
                                let seriesList = await nikitaService.getSeriesList($scope.token);
                                $scope.seriesList = seriesList.results;
                                $scope.$apply($scope.seriesList);
                                $scope.selectedSeries = $scope.seriesList[0];
                                $scope.fileList = await nikitaService.getFileList($scope.token, $scope.selectedSeries);
                                if ($scope.fileList === undefined) {
                                    $scope.fileList = [];
                                }
                                $scope.$apply($scope.fileList);

                            } catch (error) {
                                console.log(error.message);
                            }
                        })();

            */

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

            $scope.doShowFileListCard = function () {
                /*                (async () => {
                                    try {
                                        $scope.fileList = await nikitaService.getFileList($scope.token, $scope.selectedSeries);
                                        $scope.$apply($scope.fileList);
                                    } catch (error) {
                                        console.log(error.message);
                                    }
                                })();*/
                disableAllCards();
                $scope.showFileListCard = true;
                hideAllBreadcrumbs();
            };

            $scope.doShowFileCard = function () {
                disableAllCards();
                $scope.showFileCard = true;
                hideAllBreadcrumbs();
                $scope.showFileBreadcrumb = true;
            };

            $scope.doShowRegistryEntryListCard = function () {
                disableAllCards();
                hideAllBreadcrumbs();
                $scope.showRegistryEntryListCard = true;
                $scope.showRegistryEntryListBreadcrumb = true;
                $scope.showCaseFileBreadcrumb = true;
                $scope.getRegistryEntry();
            };

            $scope.doLoadCreateCaseFileModal = function () {
                $scope.newDocumentType = "";
                $scope.newTitleForDocument = "";
                $scope.selectedDocumentType = "";
            };

            $scope.doShowRegistryEntryCard = function () {
                disableAllCards();
                hideAllBreadcrumbs();
                $scope.showRegistryEntryCard = true;
                $scope.showRegistryEntryListBreadcrumb = true;
                $scope.showRegistryEntryBreadcrumb = true;
            };

            $scope.doShowDocumentListCard = function () {
                disableAllCards();
                $scope.showDocumentListCard = true;
                hideAllBreadcrumbs();
                $scope.showRegistryEntryListBreadcrumb = true;
                $scope.showRegistryEntryBreadcrumb = true;
                $scope.showDocumentListBreadcrumb = true;
                $scope.getListDocument();
            };

            $scope.getRegistryEntry = function () {
                /*console.log("url is " + $scope.caseFile._links[REL_REGISTRY_ENTRY].href);
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
                });*/
            };

            $scope.fileSelected = function (file) {

                $scope.doShowFileCard();
                // Retrieve the latest copy of the data and pull out the ETAG
                // Find the self link of the current file and issue a GET

                $scope.file = file;
                $scope.userList.push(file.opprettetAv)
                $scope.selectedUser = file.opprettetAv;

                /*
                                $http({
                                    method: 'GET',
                                    url: file._links[REL_SELF].href,
                                    headers: {'Authorization': $scope.token}
                                }).then(function successCallback(response) {
                                    $scope.file = response.data;
                                    $scope.fileETag = response.headers('eTag');
                                    console.log("Retrieved the following file " + JSON.stringify($scope.file));
                                    console.log("The ETAG header for the file is " + $scope.fileETag);
                                    for (let i = 0; i < fileStatusList.length; i++) {
                                        if (fileStatusList[i].id === $scope.file.saksstatus.kode)
                                            $scope.selectedFileStatus = fileStatusList[i].value;
                                    }
                                    $scope.$apply($scope.file);
                                });
                                */

            };

            $scope.registryEntrySelected = function (registryEntry) {

                $scope.doShowRegistryEntryCard();

                $scope.registryEntry = registryEntry;
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
                    $scope.selectedRegistryEntryStatus = $scope.registryEntry.journalstatus.kodenavn;
                    $scope.selectedRegistryEntryType = $scope.registryEntry.journalposttype.kodenavn;
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

            $scope.getListDocument = function () {
                $http({
                    method: 'GET',
                    url: $scope.registration._links[REL_DOCUMENT_DESCRIPTION].href,
                    headers: {'Authorization': $scope.token}
                }).then(function successCallback(response) {
                    $scope.documentList = response.data.results;
                    console.log("Retrieved the following documentList " +
                        JSON.stringify($scope.documentList));
                });
            };

            $scope.selectDocumentObject = function (selectedDocumentObject) {
                $scope.documentObject = selectedDocumentObject;
                console.log("Setting selected documentObject do to " +
                    JSON.stringify($scope.documentObject));
            };

            $scope.documentSelected = function (document) {
                $scope.doShowDocumentCard();
                // Retrieve the latest copy of the data and pull out the ETAG
                // Find the self link of the current document and issue a GET
                /*                $http({
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
                                });*/
            };

            /**
             * updateRegistryEntry
             *
             * Undertakes a PUT request to the core with data fields from the webpage
             *
             */
            $scope.updateRegistryEntry = function () {

                let registryEntryStatusCode = '';
                let registryEntryStatusCodeName = '';
                let registryEntryTypeCode = '';
                let registryEntryTypeCodeName = '';

                for (let i = 0; i < registryEntryStatusList.length; i++) {
                    if (registryEntryStatusList[i].value === $scope.selectedRegistryEntryStatus) {
                        registryEntryStatusCode = registryEntryStatusList[i].id;
                        registryEntryStatusCodeName = registryEntryStatusList[i].value;
                        break;
                    }
                }

                for (let i = 0; i < registryEntryTypeList.length; i++) {
                    if (registryEntryTypeList[i].value === $scope.selectedRegistryEntryType) {
                        registryEntryTypeCode = registryEntryTypeList[i].id;
                        registryEntryTypeCodeName = registryEntryTypeList[i].value;
                        break;
                    }
                }

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
                        'journalstatus': {
                            'kode': registryEntryStatusCode,
                            'kodenavn': registryEntryStatusCodeName,
                        },
                        'journalposttype': {
                            'kode': registryEntryTypeCode,
                            'kodenavn': registryEntryTypeCodeName,
                        }
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

            /**
             * download the document. The code here is adapted from:
             *  https://stackoverflow.com/questions/14215049/how-to-download-file-using-angularjs-and-calling-mvc-api
             */
            $scope.doDownloadDocument = function (documentObject) {

                $scope.documentObject = documentObject;

                let type = $scope.documentObject.mimeType;
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

