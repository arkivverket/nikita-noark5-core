var app = angular.module('nikita-arkivar', []);

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
app.directive('newCaseFileModalDir', function () {
  return {
    restrict: 'A',
    link: function (scope, element) {
      scope.dismissNewCaseFileModal = function () {
        element.modal('hide');
      };
    }
  };
});

app.directive('newDocumentModalDir', function () {
  return {
    restrict: 'A',
    link: function (scope, element) {
      scope.dismissNewDocumentModal = function () {
        element.modal('hide');
      };
    }
  };
});

app.directive('newRegistryEntryModalDir', function () {
  return {
    restrict: 'A',
    link: function (scope, element) {
      scope.dismissNewRegistryEntryModal = function () {
        element.modal('hide');
      };
    }
  };
});

var saksbehandlerController = app.controller('SaksbehandlerController', ['$scope', '$http',
    function ($scope, $http) {

      // Grab a copy of the authentication token
      $scope.token = GetUserToken();

      // get values used in drop downs. These will probably be replaced
      // by metadata calls to nikita
      $scope.documentMediumList = documentMediumList;
      $scope.storageLocationList = storageLocationList;
      $scope.caseFileStatusList = caseFileStatusList;
      $scope.registryEntryStatusList = registryEntryStatusList;
      $scope.selectedStorageLocation = "Sentralarkivet";

      $scope.showCaseFileBreadcrumb = false;
      $scope.showDocumentBreadcrumb = false;
      $scope.showRegistryEntryBreadcrumb = false;

      pageLoad();


      function pageLoad() {

        // Disable all cards except the caseFile list one
        $scope.showCaseFileListCard = true;
        $scope.showCaseFileCard = false;
        $scope.showDocumentListCard = false;
        $scope.showDocumentCard = false;
        $scope.showRegistryEntryListCard = false;
        $scope.showRegistryEntryCard = false;

        // Set default values for drop downs
        $scope.selectedCaseFileStatus = "Opprettet";
        $scope.selectedDocumentMediumCaseFile = "";
        $scope.selectedDocumentMediumNewCaseFile = "Elektronisk arkiv";

        // Create variables to bind with ng-model and modals so we can blank them out
        // For caseFile
        $scope.newDescriptionForCaseFile = "";
        $scope.newTitleForCaseFile = "";
        // For document
        $scope.newIdForDocument = "";
        $scope.newNameForDocument = "";
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
                          //console.log("Checking  " + JSON.stringify($scope.fondsList[fondsrel]));
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

                                for (var seriesRel in $scope.seriesList) {
                                  //console.log("Checking  " + JSON.stringify($scope.fondsList[fondsrel]));
                                  for (var rel in $scope.seriesList[seriesRel]._links) {
                                    var relation = $scope.seriesList[seriesRel]._links[rel].rel;
                                    if (relation === REL_CASE_FILE) {
                                      $scope.hrefCaseFile = $scope.seriesList[seriesRel]._links[rel].href;
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
              consol;
              e.log(MSG_NIKITA_DOWN_LOG + JSON.stringify(response));
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
                'Content-Type': 'application/vnd.noark5-v4+json',
                'Authorization': $scope.token,
                'ETag': $scope.caseFileETag
              },
              data: {
                tittel: $scope.caseFile.tittel,
                beskrivelse: $scope.caseFile.beskrivelse,
                dokumentmedium: $scope.selectedDocumentMediumCaseFile,
                arkivstatus: $scope.selectedCaseFileStatus
              },
            }).then(function successCallback(response) {
              console.log(" put/post on caseFile data returned= " + JSON.stringify(response.data));
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
                'Content-Type': 'application/vnd.noark5-v4+json',
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
              console.log(" put/post on registryEntry data returned= " + JSON.stringify(response.data));
              // Pick up and make a note of the ETAG so we can update the object
              $scope.registryEntryETag = response.headers('ETag');
              $scope.registryEntry = response.data;
            });
          }
        }
      };


      /**
       * createDocument
       *
       * Undertakes either a PUT or a POST request to the core with data fields from the webpage
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
                'Content-Type': 'application/vnd.noark5-v4+json',
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
       * create a new Document associated with the current caseFile
       */
      $scope.createDocument = function () {

        for (var rel in $scope.caseFile._links) {
          var relation = $scope.caseFile._links[rel].rel;
          if (relation === REL_NEW_CaseFile_CREATOR) {
            console.log("href for creating a document is " + $scope.caseFile._links[rel].href);
            console.log("href for creating a document is " + $scope.caseFile._links[rel].href);
            $http({
              url: $scope.caseFile._links[rel].href,
              method: "POST",
              headers: {
                'Content-Type': 'application/vnd.noark5-v4+json',
                'Authorization': $scope.token,
              },
              data: {
                arkivskaperID: $scope.newIdForDocument,
                arkivskaperNavn: $scope.newNameForDocument,
                beskrivelse: $scope.newDescriptionForDocument
              },
            }).then(function successCallback(response) {
              console.log("POST on document data returned= " + JSON.stringify(response.data));

              $scope.doDismissNewDocumentModal();

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
       * create a new RegistryEntry associated with the current caseFile
       */
      $scope.createRegistryEntry = function () {

        for (var rel in $scope.caseFile._links) {
          var relation = $scope.caseFile._links[rel].rel;
          if (relation === REL_NEW_RegistryEntry) {
            console.log("href for creating a document is " + $scope.caseFile._links[rel].href);
            $http({
              url: $scope.caseFile._links[rel].href,
              method: "POST",
              headers: {
                'Content-Type': 'application/vnd.noark5-v4+json',
                'Authorization': $scope.token,
              },
              data: {
                tittel: $scope.newTitleForRegistryEntry,
                beskrivelse: $scope.newDescriptionForRegistryEntry,
                dokumentmedium: $scope.selectedDocumentMediumNewRegistryEntry,
                arkivdelstatus: "Opprettet"
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
       * As this is a single-page-application, we need to hide and show cards.
       * This is a helper method to hide all cards and the caller can then
       * show the card they want to show.
       *
       */
      function disableAllCards() {
        $scope.showCaseFileListCard = false;
        $scope.showCaseFileCard = false;
        $scope.showDocumentListCard = false;
        $scope.showDocumentCard = false;
        $scope.showRegistryEntryListCard = false;
        $scope.showRegistryEntryCard = false;
      }

      /**
       * createCaseFile
       *
       * Undertakes POST request to the core with data fields from the webpage
       * Results in a new caseFile object being created and returned. Web-page is
       * updated so the newly returned caseFile is shown.
       */
      $scope.createCaseFile = function () {

        $http({
          url: $scope.hrefNewCaseFile,
          method: "POST",
          headers: {
            'Content-Type': 'application/vnd.noark5-v4+json',
            'Authorization': $scope.token,
          },
          data: {
            tittel: $.trim(document.getElementById("nyTittelSaksmappe").value),
            beskrivelse: $.trim(document.getElementById("nyBeskrivelseSaksmappe").value),
            dokumentmedium: $.trim($scope.selectedDocumentMediumNewCaseFile),
            saksmappestatus: $.trim($scope.selectedCaseFileStatus)
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

      $scope.doShowCaseFileListCard = function () {
        disableAllCards();
        $scope.showCaseFileListCard = true;
        $scope.showCaseFileBreadcrumb = false;
        $scope.showDocumentBreadcrumb = false;
        $scope.showRegistryEntryBreadcrumb = false;
        pageLoad();
      };

      $scope.doShowCaseFileCard = function () {
        disableAllCards();
        $scope.showCaseFileCard = true;
        $scope.showCaseFileBreadcrumb = true;
        $scope.showDocumentBreadcrumb = false;
        $scope.showRegistryEntryBreadcrumb = false;
      };

      $scope.doShowRegistryEntryCard = function () {
        disableAllCards();
        $scope.showRegistryEntryCard = true;
        $scope.showCaseFileBreadcrumb = true;
        $scope.showDocumentBreadcrumb = false;
        $scope.showRegistryEntryBreadcrumb = true;
      };

      $scope.doShowDocumentCard = function () {
        disableAllCards();
        $scope.showDocumentCard = true;
        $scope.showCaseFileBreadcrumb = true;
        $scope.showDocumentBreadcrumb = true;
        $scope.showRegistryEntryBreadcrumb = false;
      };

      $scope.doShowDocumentListCard = function () {
        disableAllCards();
        $scope.showDocumentListCard = true;
        $scope.showCaseFileBreadcrumb = true;
        $scope.showDocumentBreadcrumb = true;
        $scope.showRegistryEntryBreadcrumb = false;
        $scope.getListDocument();
      };

      $scope.doShowRegistryEntryListCard = function () {
        disableAllCards();
        $scope.showRegistryEntryListCard = true;
        $scope.showCaseFileBreadcrumb = true;
        $scope.showRegistryEntryBreadcrumb = true;
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

      $scope.doDismissNewDocumentModal = function () {
        $scope.dismissNewDocumentModal();
        $scope.newDescriptionForRegistryEntry = "";
        $scope.newTitleForRegistryEntry = "";
      };

      $scope.doLoadRegistryEntryModal = function () {
        $scope.selectedDocumentMediumNewRegistryEntry = $scope.caseFile.dokumentmedium;
      };
      /**
       *  caseFile_selected
       *
       */

      $scope.documentSelected = function (document) {
      };

      $scope.checkDocumentMediumForRegistryEntry = function () {

        console.log("Change detected " + $scope.caseFile.dokumentmedium + " " + $scope.selectedDocumentMediumRegistryEntry);
        if ($scope.caseFile.dokumentmedium !== $scope.selectedDocumentMediumRegistryEntry) {

          if ($scope.caseFile.dokumentmedium === "Elektronisk arkiv") {
            $scope.selectedDocumentMediumRegistryEntry = $scope.caseFile.dokumentmedium;
          }
          else if ($scope.caseFile.dokumentmedium === "Fysisk arkiv") {
            $scope.selectedDocumentMediumRegistryEntry = $scope.caseFile.dokumentmedium;
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
              $scope.selectedCaseFileStatus = $scope.caseFile.arkivstatus;
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
              $scope.selectedRegistryEntryStatus = $scope.registryEntry.arkivdelstatus;
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
              $scope.document = response.data;
              $scope.documentETag = response.headers('eTag');
              console.log("Retrieved the following document " + JSON.stringify($scope.document));
              console.log("The ETAG header for the document is " + $scope.documentETag);
            });
          }
        }
      };

      $scope.getListDocument = function () {
        for (var rel in $scope.caseFile._links) {
          var relation = $scope.caseFile._links[rel].rel;
          if (relation == REL_CaseFile_CREATOR) {
            $http({
              method: 'GET',
              url: $scope.caseFile._links[rel].href,
              headers: {'Authorization': $scope.token}
            }).then(function successCallback(response) {
              $scope.documentList = response.data.results;
              console.log("Retrieved the following documentList " + JSON.stringify($scope.documentList));
            });
          }
        }
      };

      $scope.getRegistryEntry = function () {
        for (var rel in $scope.caseFile._links) {
          var relation = $scope.caseFile._links[rel].rel;
          if (relation == REL_RegistryEntry) {
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
        console.log("Attempting logout on [" + $scope.hrefLogout + "]. using token [" +
          $scope.token + "]");
        $http({
          method: 'GET',
          url: $scope.hrefLogout,
          headers: {'Authorization': $scope.token}
        }).then(function successCallback(response) {
          $scope.token = "";
          console.log(" GET to doLogout [" + $scope.hrefLogout +
            "] returned " + JSON.stringify(response));
          changeLocation($scope, "login.html", false);
        }, function errorCallback(response) {
          alert("Problemer med å logge ut. Du kan se bort fra denne meldingen!");
          console.log(" GET urlForLogout[" + $scope.hrefLogout +
            "] returned " + JSON.stringify(response));
        });
      };
    }
  ])
;

