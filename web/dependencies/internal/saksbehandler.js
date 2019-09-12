/**
 * Enables the following functionality:
 *  1. Sets the caseFile object correctly for the arkiv.html page
 *  2. Allows a user to POST the contents of the arkiv data
 *
 *  Note: Because we have trouble getting
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
    ['$scope', '$http', 'loginService',
      function ($scope, $http, loginService) {
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

      // TODO : DELETE THESE
      $scope.newTitleForRegistryEntry = "Test brev";
      $scope.newPublicTitleForRegistryEntry = "Test brev";
      $scope.newDescriptionForRegistryEntry = "Test brev";
      $scope.newRegistryEntryStatus = "Journalført";
      $scope.newRegistryEntryType = "Inngående dokument";
      $scope.newDocumentType = "Brev";
      $scope.newTitleForDocument = "Dokument tittel";
      $scope.selectedDocumentType = "Brev";
      $scope.selectedFormat = "odt";
      $scope.selectedVariantFormat = "Produksjonsformat";

      $scope.documentObjectList = "";

      $scope.disableAllButtons = false;
      pageLoad();


      function pageLoad() {

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
        $scope.newTitleForCaseFile = "Test";
        // For document
        $scope.newIdForDocument = "";

        $scope.newDescriptionForDocument = "";

        // For RegistryEntry
        $scope.selectedDocumentMediumRegistryEntry = "";
        $scope.selectedDocumentMediumNewRegistryEntry = "";
        $scope.newDescriptionForRegistryEntry = "";
        $scope.newTitleForRegistryEntry = "";
        $scope.newRegistryEntryStatus = "";
        $scope.newRegistryEntryType = "";

        // GET the application root. There you get a HREF to REL_CASEFILE_STRUCTURE
        // Then you GET the REL_CASEFILE_STRUCTURE. Make a note of HREFS for:
        //    REL_CASEFILE_STRUCTURE : Get a list of all caseFile
        //    REL_CASEFILE_STRUCTURE_NEW_CaseFile : Create a new caseFile


        $http({
          method: 'GET',
          url: baseUrl,
          headers: {'Authorization': $scope.token}
        }).then(function successCallback(response) {
            $scope.applicationRoot = response.data;
            for (var rel in $scope.applicationRoot._links) {
              var relation = $scope.applicationRoot._links[rel].rel;
              if (relation == REL_FONDS_STRUCTURE) {
                var fondsStructureHref = $scope.applicationRoot._links[rel].href;
                console.log("fondsStructureHref is : " + JSON.stringify(fondsStructureHref));
                $http({
                  method: 'GET',
                  url: fondsStructureHref,
                  headers: {'Authorization': $scope.token},
                }).then(function successCallback(response) {
                  $scope.fondsStructure = response.data._links;
                  for (var rel in response.data._links) {
                    var relation = response.data._links[rel].rel;
                    if (relation == REL_FONDS_STRUCTURE_FONDS) {
                      $scope.hrefFonds = response.data._links[rel].href;
                      console.log("Attempting " + $scope.hrefFonds);
                      $http({
                        method: 'GET',
                        url: $scope.hrefFonds,
                        headers: {'Authorization': $scope.token}
                      }).then(function successCallback(response) {
                        $scope.fondsList = response.data.results;
                        for (var fondsrel in $scope.fondsList) {
                          console.log("Checking  " + JSON.stringify($scope.fondsList[fondsrel]));
                          for (var rel in $scope.fondsList[fondsrel]._links) {
                            var relation = $scope.fondsList[fondsrel]._links[rel].rel;
                            if (relation === REL_SERIES) {
                              $http({
                                method: 'GET',
                                url: $scope.fondsList[fondsrel]._links[rel].href,
                                headers: {'Authorization': $scope.token}
                              }).then(function successCallback(response) {
                                $scope.seriesList = response.data.results;
                                console.log("Retrieved the following seriesList " + JSON.stringify($scope.seriesList));

                                if (typeof $scope.seriesList === 'undefined' || $scope.seriesList < 1) {
                                  alert("Kan ikke koble til en arkivdel. Kan ikke gjøre noe uten en arkivdel! Du må først opprette en arkivdel med arkivar rollen");
                                  $scope.disableAllButtons = true;
                                  return;
                                }
                                for (var seriesRel in $scope.seriesList) {
                                  for (var rel in $scope.seriesList[seriesRel]._links) {
                                    var relation = $scope.seriesList[seriesRel]._links[rel].rel;
                                    //console.log("REL " + relation + ", " + JSON.stringify($scope.seriesList[seriesRel]._links[rel].href));
                                    if (relation === REL_CASE_FILE) {
                                      console.log("GET " + relation + ", " + JSON.stringify($scope.seriesList[seriesRel]._links[rel].href));
                                      $scope.hrefCaseFile = $scope.seriesList[seriesRel]._links[rel].href;
                                      $http({
                                        method: 'GET',
                                        url: $scope.seriesList[seriesRel]._links[rel].href,
                                        headers: {'Authorization': $scope.token}
                                      }).then(function successCallback(response) {
                                          $scope.caseFileList = response.data.results;
                                          console.log("Retrieved " + JSON.stringify($scope.caseFileList));
                                        }, function errorCallback(response) {
                                          if (response.status == -1) {
                                            console.log(MSG_NIKITA_DOWN_LOG + JSON.stringify(response));
                                            alert(MSG_NIKITA_DOWN);
                                          } else {
                                            console.log(MSG_NIKITA_UNKNOWN_ERROR_LOG + JSON.stringify(response));
                                            alert(MSG_NIKITA_UNKNOWN_ERROR);
                                          }
                                        }
                                      );
                                    }
                                    else if (relation === REL_NEW_CASE_FILE) {
                                      $scope.hrefNewCaseFile = $scope.seriesList[seriesRel]._links[rel].href;
                                    }
                                  }
                                }
                              }, function errorCallback(response) {
                                if (response.status == -1) {
                                  console.log(MSG_NIKITA_DOWN_LOG + JSON.stringify(response));
                                  alert(MSG_NIKITA_DOWN);
                                } else {
                                  console.log(MSG_NIKITA_UNKNOWN_ERROR_LOG + JSON.stringify(response));
                                  alert(MSG_NIKITA_UNKNOWN_ERROR);
                                }
                              });
                            }
                          }
                        }
                      }, function errorCallback(response) {
                        if (response.status == -1) {
                          console.log(MSG_NIKITA_DOWN_LOG + JSON.stringify(response));
                          alert(MSG_NIKITA_DOWN);
                        } else {
                          console.log(MSG_NIKITA_UNKNOWN_ERROR_LOG + JSON.stringify(response));
                          alert(MSG_NIKITA_UNKNOWN_ERROR);
                        }
                      });
                    }
                    else if (relation === REL_FONDS_STRUCTURE_NEW_FONDS) {
                      $scope.hrefNewFonds = response.data._links[rel].href;
                      console.log("hrefNewFonds (new) is : " + JSON.stringify($scope.hrefNewFonds));
                    }
                  }
                }, function errorCallback(response) {
                  if (response.status == -1) {
                    console.log(MSG_NIKITA_DOWN_LOG + JSON.stringify(response));
                    alert(MSG_NIKITA_DOWN);
                  } else {
                    console.log(MSG_NIKITA_UNKNOWN_ERROR_LOG + JSON.stringify(response));
                    alert(MSG_NIKITA_UNKNOWN_ERROR);
                  }
                });
              }
              else if (relation === REL_LOGOUT_OAUTH2) {
                $scope.hrefLogout = $scope.applicationRoot._links[rel].href;
                console.log("hrefLogout is : " + JSON.stringify($scope.hrefLogout));
              }
            }
          }, function errorCallback(response) {
            if (response.status == -1) {
              console.log(MSG_NIKITA_DOWN_LOG + JSON.stringify(response));
              alert(MSG_NIKITA_DOWN);
            } else {
              console.log(MSG_NIKITA_UNKNOWN_ERROR_LOG + JSON.stringify(response));
              alert(MSG_NIKITA_UNKNOWN_ERROR);
            }
          }
        );
      }

      /**
       * updateCaseFile
       *
       * Undertakes a PUT request to the core with data fields from the webpage
       *
       */
      $scope.updateCaseFile = function () {

        for (var rel in $scope.caseFile._links) {
          var relation = $scope.caseFile._links[rel].rel;
          if (relation === REL_SELF) {
            var urlCaseFile = $scope.caseFile._links[rel].href;
            console.log(" Attempting to update arkiv with following address = " + urlCaseFile);
            console.log(" Current ETAG is = [" + $scope.caseFileETag + "]");
            $http({
              url: urlCaseFile,
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
                saksstatus: $scope.caseFile.saksstatus
              }
            }).then(function successCallback(response) {
              console.log(" put on caseFile data returned= " + JSON.stringify(response.data));
              // Pick up and make a note of the ETAG so we can update the object
              $scope.caseFileETag = response.headers('ETag');
              $scope.caseFile = response.data;
            });
          }
        }
      };

      /**
       * updateRegistryEntry
       *
       * Undertakes a PUT request to the core with data fields from the webpage
       *
       */
      $scope.updateRegistryEntry = function () {

        for (var rel in $scope.registryEntry._links) {
          var relation = $scope.registryEntry._links[rel].rel;
          if (relation === REL_SELF) {
            var urlRegistryEntry = $scope.registryEntry._links[rel].href;
            console.log(" Attempting to update registryEntry with following address = " + urlRegistryEntry);
            console.log(" Current ETAG is = [" + $scope.registryEntryETag + "]");
            $http({
              url: urlRegistryEntry,
              method: "PUT",
              headers: {
                'Content-Type': 'application/vnd.noark5+json',
                'Authorization': $scope.token,
                'ETag': $scope.registryEntryETag
              },
              data: {
                tittel: $scope.registryEntry.tittel,
                beskrivelse: $scope.registryEntry.beskrivelse,
                dokumentmedium: $scope.selectedDocumentMediumRegistryEntry,
                arkivdelstatus: $scope.selectedRegistryEntryStatus
              },
            }).then(function successCallback(response) {
              console.log(" put on registryEntry data returned= " + JSON.stringify(response.data));
              // Pick up and make a note of the ETAG so we can update the object
              $scope.registryEntryETag = response.headers('ETag');
              $scope.registryEntry = response.data;
            });
          }
        }
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

        for (var rel in $scope.document._links) {
          var relation = $scope.document._links[rel].rel;
          if (relation === REL_SELF) {
            console.log("href for updating a document is " + $scope.document._links[rel].href);
            console.log(" Attempting to update document with following ETAG = " + $scope.documentETag);
            $http({
              url: $scope.document._links[rel].href,
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
          }
        }
      };

      /**
       *
       * create a new documentDescription associated with the current registryEntry
       */
      $scope.createDocumentDescription = function () {

        for (var rel in $scope.registryEntry._links) {
          var relation = $scope.registryEntry._links[rel].rel;
          if (relation === REL_NEW_DOCUMENT_DESCRIPTION) {
            console.log("href for creating a document is " + $scope.registryEntry._links[rel].href);
            $http({
              url: $scope.registryEntry._links[rel].href,
              method: "POST",
              headers: {
                'Content-Type': 'application/vnd.noark5+json',
                'Authorization': $scope.token,
              },
              data: {
                'tittel': $scope.newTitleForDocument,
                'beskrivelse': $scope.newDescriptionForDocument,
                'tilknyttetRegistreringSom': $scope.selectedAssociatedWithRecordAs,
                'dokumenttype': $scope.selectedDocumentType
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
          }
        }
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

        for (var rel in $scope.documentDescription._links) {
          var relation = $scope.documentDescription._links[rel].rel;
          if (relation === REL_NEW_DOCUMENT_OBJECT) {
            console.log("href for creating a document is " + $scope.documentDescription._links[rel].href);
            $http({
              url: $scope.documentDescription._links[rel].href,
              method: "POST",
              headers: {
                'Content-Type': 'application/vnd.noark5+json',
                'Authorization': $scope.token,
              },
              data: {
                'format': $scope.selectedFormat,
                'mimeType': $scope.selectedMimeType,
                'variantformat': $scope.selectedVariantFormat
              }
            }).then(function successCallback(response) {
              console.log("POST on document data returned= " + JSON.stringify(response.data));
              $scope.doDismissNewDocumentObjectModal();

              // Update the document object so fields in GUI are changed
              $scope.document = response.data;
              $scope.documentObjectList.push(response.data);

              // Pick up and make a note of the ETAG so we can update the object
              $scope.documentETag = response.headers('eTag');
              $scope.documentETag = '"' + $scope.documentETag + '"';
              console.log("Etag after post on document is = " + $scope.documentETag);
            });
          }
        }
      };


      /**
       *
       * create a new correspondencePart associated with the current registryEntry
       */
      $scope.createCorrespondencePart = function () {

        for (var rel in $scope.registryEntry._links) {
          if ($scope.registryEntry._links[rel].rel === REL_NEW_CORRESPONDENCE_PART_PERSON) {
            let href = $scope.registryEntry._links[rel].href;
            console.log("href for createCorrespondencePart is " + href);
            $http({
              url: href,
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
                dnummer: $scope.newCorrespondencepartPerson.dnummer,
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
          }
        }
      };


      /**
       *
       * create a new RegistryEntry associated with the current caseFile
       */
      $scope.createRegistryEntry = function () {

        for (var rel in $scope.caseFile._links) {
          var relation = $scope.caseFile._links[rel].rel;
          if (relation === REL_NEW_REGISTRY_ENTRY) {
            console.log("href for creating a document is " + $scope.caseFile._links[rel].href);
            $http({
              url: $scope.caseFile._links[rel].href,
              method: "POST",
              headers: {
                'Content-Type': 'application/vnd.noark5+json',
                'Authorization': $scope.token,
              },
              data: {
                tittel: $scope.newTitleForRegistryEntry,
                beskrivelse: $scope.newDescriptionForRegistryEntry,
                journalstatus: $scope.newRegistryEntryStatus,
                journalposttype: $scope.newRegistryEntryType,
                dokumentmedium: $scope.selectedDocumentMediumNewRegistryEntry,
              },
            }).then(function successCallback(response) {
              console.log("POST on registryEntry data returned= " + JSON.stringify(response.data));
              $scope.doDismissNewRegistryEntryModal();
              // Update the document object so fields in GUI are changed
              $scope.registryEntry = response.data;
              // Pick up and make a note of the ETAG so we can update the object
              $scope.registryEntryETag = response.headers('eTag');
              $scope.registryEntryETag = '"' + $scope.registryEntryETag + '"';
              console.log("Etag after post on registryEntry is = " + $scope.registryEntryETag);
            });
          }
        }
      };

      /**
       * createCaseFile
       *
       * Undertakes POST request to the core with data fields from the webpage
       * Results in a new caseFile object being created and returned. Web-page is
       * updated so the newly returned caseFile is shown.
       */
      $scope.createCaseFile = function () {
        console.log("Calling createCaseFile with " + $scope.hrefNewCaseFile);
        $http({
          url: $scope.hrefNewCaseFile,
          method: "POST",
          headers: {
            'Content-Type': 'application/vnd.noark5+json',
            'Authorization': $scope.token,
          },
          data: {
            tittel: $.trim($scope.newTitleForCaseFile),
            offentligTittel: $.trim($scope.newPublicTitleForCaseFile),
            beskrivelse: $.trim($scope.newDescriptionForCaseFile),
            dokumentmedium: $scope.selectedDocumentMediumNewCaseFile,
            // Temporary removed. Will add in administrativeunit functionality and this will have values
            // Same with keyword. Waiting to check implementation
            // noekkelord: $.trim($scope.newKeywordForCaseFile),
            // administrativEnhet: $.trim($scope.newAdministrativeUnitForCaseFile),
            saksansvarlig: $.trim($scope.newCaseResponsibleForCaseFile),
            saksstatus: $.trim($scope.newCaseStatusForCaseFile),
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
        disableAllCards();
        $scope.showCaseFileListCard = true;
        hideAllBreadcrumbs();
        pageLoad();
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
      };

      $scope.doLoadDocumentDescriptionModal = function () {

      };

      $scope.doLoadCorrespondencePartModal = function () {

      };

      $scope.checkDocumentMediumForRegistryEntry = function () {

        console.log("Change detected " + $scope.caseFile.dokumentmedium + " " + $scope.selecteddoLoadRegistryEntryModalDocumentMediumRegistryEntry);
        if ($scope.caseFile.dokumentmedium !== $scope.selectedDocumentMediumRegistryEntry) {

          if ($scope.caseFile.dokumentmedium === "Elektronisk arkiv") {
            $scope.selectedDocumentMediumRegistryEntry = $scope.caseFile.dokumentmedium;
          }
          else if ($scope.caseFile.dokumentmedium === "Fysisk arkiv") {
            $scope.selectedDocumentMediumRegistryEntry = $scope.caseFile.dokumentmedium;
          }
        }
      };

      $scope.correspondencePartSelected = function (correspondencePart) {
        $scope.doShowCorrespondencePartCard();

        // Retrieve the latest copy of the data and pull out the ETAG
        // Find the self link of the current correspondencePart and issue a GET

        for (var rel in correspondencePart._links) {
          var relation = correspondencePart._links[rel].rel;
          if (relation === REL_SELF) {
            var urlToCorrespondencePart = correspondencePart._links[rel].href;
            var token = $scope.token;
            console.log("Retrieving " + urlToCorrespondencePart);
            $http({
              method: 'GET',
              url: urlToCorrespondencePart,
              headers: {'Authorization': token}
            }).then(function successCallback(response) {
              $scope.correspondencepartPerson = response.data;
              $scope.correspondencePartPersonETag = response.headers('eTag');
              console.log("Retrieved the following correspondencePart " + JSON.stringify($scope.correspondencepartPerson));
              console.log("The ETAG header for the correspondencePart is " + $scope.correspondencePartPersonETag);
            });
          }
        }
      };

      $scope.caseFileSelected = function (caseFile) {

        $scope.doShowCaseFileCard();
        // Retrieve the latest copy of the data and pull out the ETAG
        // Find the self link of the current caseFile and issue a GET

        for (var rel in caseFile._links) {
          var relation = caseFile._links[rel].rel;
          if (relation == REL_SELF) {
            var urlToCaseFile = caseFile._links[rel].href;
            var token = $scope.token;
            $http({
              method: 'GET',
              url: urlToCaseFile,
              headers: {'Authorization': token}
            }).then(function successCallback(response) {
              $scope.caseFile = response.data;
              $scope.caseFileETag = response.headers('eTag');
              console.log("Retrieved the following caseFile " + JSON.stringify($scope.caseFile));
              console.log("The ETAG header for the caseFile is " + $scope.caseFileETag);
              $scope.selectedDocumentMediumCaseFile = $scope.caseFile.dokumentmedium;
              $scope.selectedCaseFileStatus = $scope.caseFile.saksstatus;
            });
          }
        }
      };


      $scope.doGetCorrespondencePartTemplate = function () {

        for (let rel in $scope.registryEntry._links) {
          // find one that contains a link to a  ny-korrespondansepartperson
          if ($scope.registryEntry._links[rel].rel === REL_NEW_CORRESPONDENCE_PART_PERSON) {
            // Issue a GET for the ny-korrespondansepartperson
            console.log("href is (" +
              $scope.registryEntry._links[rel].href + ")");
            let href = $scope.registryEntry._links[rel].href;
            $http({
              method: 'GET',
              url: href,
              headers: {'Authorization': $scope.token}
            }).then(function successCallback(response) {
              console.log("Result from GET (" +
                href + ") is " +
                JSON.stringify(response.data));
              // populate the values in the GUI
              $scope.newCorrespondencepartPerson = response.data;
            }, function errorCallback(response) {
              console.log("Problem with call to url [" +
                $scope.registryEntry._links[rel].rel + "] response is "
                + response);
            });
          }
        }
      };


      $scope.registryEntrySelected = function (registryEntry) {

        $scope.doShowRegistryEntryCard();
        // Retrieve the latest copy of the data and pull out the ETAG
        // Find the self link of the current registryEntry and issue a GET
        for (var rel in registryEntry._links) {
          var relation = registryEntry._links[rel].rel;
          if (relation == REL_SELF) {
            $http({
              method: 'GET',
              url: registryEntry._links[rel].href,
              headers: {'Authorization': $scope.token}
            }).then(function successCallback(response) {
              $scope.registryEntry = response.data;
              $scope.registryEntryETag = response.headers('eTag');
              console.log("Retrieved the following registryEntry " + JSON.stringify($scope.registryEntry));
              console.log("The ETAG header for the registryEntry is " + $scope.registryEntryETag);
              $scope.selectedDocumentMediumRegistryEntry = $scope.registryEntry.dokumentmedium;
              $scope.selectedRegistryEntryStatus = $scope.registryEntry.journalstatus;
            });
          }
        }
      };

      $scope.fetchDocumentObjects = function () {

        for (var rel in $scope.documentDescription._links) {
          var relation = $scope.documentDescription._links[rel].rel;
          if (relation == REL_DOCUMENT_OBJECT) {
            $http({
              method: 'GET',
              url: $scope.documentDescription._links[rel].href,
              headers: {'Authorization': $scope.token}
            }).then(function successCallback(response) {
              $scope.documentObjectList = response.data.results;
              console.log("Retrieved the following documentObjectList " + JSON.stringify($scope.documentObjectList));
            });
          }
        }
      };

      $scope.documentSelected = function (document) {
        $scope.doShowDocumentCard();
        // Retrieve the latest copy of the data and pull out the ETAG
        // Find the self link of the current document and issue a GET
        for (var rel in document._links) {
          var relation = document._links[rel].rel;
          if (relation == REL_SELF) {
            $http({
              method: 'GET',
              url: document._links[rel].href,
              headers: {'Authorization': $scope.token}
            }).then(function successCallback(response) {
              $scope.documentDescription = response.data;
              $scope.selectedAssociatedWithRecordAs = $scope.documentDescription.tilknyttetRegistreringSom;
              $scope.selectedDocumentStatus = $scope.documentDescription.dokumentstatus;
              $scope.documentETag = response.headers('eTag');
              $scope.fetchDocumentObjects();
              console.log("Retrieved the following document " + JSON.stringify($scope.documentDescription));
              console.log("The ETAG header for the document is " + $scope.documentETag);
            });
          }
        }
      };

      $scope.getListDocument = function () {
        for (var rel in $scope.registryEntry._links) {
          var relation = $scope.registryEntry._links[rel].rel;
          if (relation == REL_DOCUMENT_DESCRIPTION) {
            $http({
              method: 'GET',
              url: $scope.registryEntry._links[rel].href,
              headers: {'Authorization': $scope.token}
            }).then(function successCallback(response) {
              $scope.documentList = response.data.results;
              console.log("Retrieved the following documentList " +
                JSON.stringify($scope.documentList));
            });
          }
        }
      };

      $scope.getListCorrespondencePart = function () {
        for (var rel in $scope.registryEntry._links) {
          var relation = $scope.registryEntry._links[rel].rel;
          // Should be all correspondnaceparts
          if (relation === REL_CORRESPONDENCE_PART_PERSON) {
            $http({
              method: 'GET',
              url: $scope.registryEntry._links[rel].href,
              headers: {'Authorization': $scope.token}
            }).then(function successCallback(response) {
              $scope.correspondencePartList = response.data.results;
              console.log("Retrieved the following correspondencePartList " +
                JSON.stringify($scope.correspondencePartList));
            });
          }
        }
      };

      $scope.selectDocumentObject = function (selectedDocumentObject) {

        $scope.documentObject = selectedDocumentObject;

        console.log("Setting selected documentObject do to " + JSON.stringify($scope.documentObject));


      };

      $scope.uploadFiles = function (documentObject, file, errFiles) {
        $scope.f = file;

        for (rel in documentObject._links) {
          if (documentObject._links[rel].rel === REL_DOCUMENT_FILE) {
            $scope.errFile = errFiles && errFiles[0];
            if (file) {
              var xhr = new XMLHttpRequest();
              xhr.withCredentials = true;
              xhr.addEventListener("readystatechange", function () {
                if (this.readyState === 4) {
                  if (this.status != 200) {
                    alert("Kunne ikke laste opp fil. Kjernen sier følgende: " +
                      this.responseText.message);
                  }
                  else {
                    $scope.fetchDocumentObjects();
                  }
                }
              });
              xhr.open("POST", documentObject._links[rel].href);
              var blob = new Blob([file], {type: documentObject.selectedMimeType});
              xhr.setRequestHeader('content-type', documentObject.selectedMimeType);
              xhr.setRequestHeader('Authorization', $scope.token);
              xhr.setRequestHeader("X-File-Name", file.name);
              xhr.send(blob);
            }
          }
        }
      };

      $scope.getRegistryEntry = function () {
        for (var rel in $scope.caseFile._links) {
          var relation = $scope.caseFile._links[rel].rel;
          if (relation == REL_REGISTRY_ENTRY) {
            $http({
              method: 'GET',
              url: $scope.caseFile._links[rel].href,
              headers: {'Authorization': $scope.token}
            }).then(function successCallback(response) {
              $scope.registryEntryList = response.data.results;
              console.log("Retrieved the following registryEntryList " + JSON.stringify($scope.registryEntryList));
            });
          }
        }
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
        let format = $scope.documentObject.format;
        let filename = "mildertidignavn." + format;

        for (rel in $scope.documentObject._links) {
          if ($scope.documentObject._links[rel].rel === REL_DOCUMENT_FILE) {

            $http({
              method: 'GET',
              url: $scope.documentObject._links[rel].href,
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
      }
    }
  ])
;

