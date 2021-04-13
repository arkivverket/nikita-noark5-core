/**
 * Enables the following functionality:
 * Provide a list of everything that has been imported from the email system as a File
 * and registrations.
 *
 * A File is shown as an email and attachments are registrations. It is possible to convert the file
 * to a File.
 */

var postMottakController = app.controller('PostMottakController',
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
            $scope.showFileListCard = true;
            $scope.showFileCard = false;
            $scope.showDocumentListCard = false;
            $scope.showDocumentCard = false;
            $scope.showRegistrationListCard = false;
            $scope.showRegistrationCard = false;

            // Set default values for drop downs
            $scope.selectedFileStatus = "Opprettet";
            $scope.selectedAssociatedWithRecordAs = "Hoveddokument";
            $scope.selectedDocumentStatus = "Dokumentet er under redigering";

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
                    filnavn: "Kul filnavn.pdf"
                },
                {
                    dokumentnummer: 2,
                    filnavn: "Kul filnavn.pdf"
                }
            ];

            $scope.fileList = [
                {
                    "systemID": "f1677c47-99e1-42a7-bda2-b0bbc64841b7",
                    "tittel": "Test for utvikling 1",
                    "opprettetDato": "2020-06-30T15:35:43.128158+02:00",
                    "opprettetAv": "admin@example.com",
                    "virksomhetsspesifikkeMetadata": {
                        "avsender": "joe@example.com",
                        "mottaker": "postmottak@example.com",
                        "innhold": "Hei, dette er epost fra meg "
                    }
                },
                {
                    "systemID": "f32c1fa0-8e42-4236-8f40-e006940ea70b",
                    "tittel": "Enda en test for utvikling",
                    "opprettetDato": "2020-06-30T15:35:43.128158+02:00",
                    "opprettetAv": "admin@example.com",
                    "virksomhetsspesifikkeMetadata": {
                        "avsender": "frank@example.com",
                        "mottaker": "postmottak@example.com, advokat@example.com",
                        "innhold": "Hei, dette er epost fra meg "
                    }
                }
            ];

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
                $scope.showFileListCard = false;
                $scope.showFileCard = false;
            }

            /**
             * As this is a single-page-application, we need to hide and show breadcrumbs.
             * This is a helper method to hide all breadcrumbs and the caller can then
             * show the breadcrumb they want to show.
             *
             */
            function hideAllBreadcrumbs() {
                $scope.showFileBreadcrumb = false;
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

            $scope.doLoadCreateCaseFileModal = function () {
                $scope.newDocumentType = "";
                $scope.newTitleForDocument = "";
                $scope.selectedDocumentType = "";
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

