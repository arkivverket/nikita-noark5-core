var app = angular.module('nikita-arkivar', []);

/**
 * Enables the following functionality:
 *  1. Sets the fonds object correctly for the arkiv.html page
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
app.directive('newFondsModalDir', function () {
  return {
    restrict: 'A',
    link: function (scope, element) {
      scope.dismissNewFondsModal = function () {
        element.modal('hide');
      };
    }
  };
});

app.directive('newFondsCreatorModalDir', function () {
  return {
    restrict: 'A',
    link: function (scope, element) {
      scope.dismissNewFondsCreatorModal = function () {
        element.modal('hide');
      };
    }
  };
});

app.directive('newSeriesModalDir', function () {
  return {
    restrict: 'A',
    link: function (scope, element) {
      scope.dismissNewSeriesModal = function () {
        element.modal('hide');
      };
    }
  };
});

var fondsController = app.controller('ArkivarController', ['$scope', '$http',
    function ($scope, $http) {

      // Grab a copy of the authentication token
      $scope.token = GetUserToken();

      // get values used in drop downs. These will probably be replaced
      // by metadata calls to nikita
      $scope.documentMediumList = documentMediumList;
      $scope.fondsStatusList = fondsStatusList;
      $scope.seriesStatusList = seriesStatusList;

      $scope.showFondsBreadcrumb = false;
      $scope.showFondsCreatorBreadcrumb = false;
      $scope.showSeriesBreadcrumb = false;

      pageLoad();


      function pageLoad() {

        // Disable all cards except the fonds list one
        $scope.showFondsListCard = true;
        $scope.showFondsCard = false;
        $scope.showFondsCreatorListCard = false;
        $scope.showFondsCreatorCard = false;
        $scope.showSeriesListCard = false;
        $scope.showSeriesCard = false;

        // Set default values for drop downs
        $scope.selectedFondsStatus = "Opprettet";
        $scope.selectedDocumentMedium = "Elektronisk arkiv";

        // Create variables to bind with ng-model and modals so we can blank them out
        // For fonds
        $scope.newDescriptionForFonds = "";
        $scope.newTitleForFonds = "";
        // For fondsCreator
        $scope.newIdForFondsCreator = "";
        $scope.newNameForFondsCreator = "";
        $scope.newDescriptionForFondsCreator = "";
        // For Series
        $scope.selectedDocumentMediumForSeries = "";
        $scope.newDescriptionForSeries = "";
        $scope.newTitleForSeries = "";

        // GET the application root. There you get a HREF to REL_FONDS_STRUCTURE
        // Then you GET the REL_FONDS_STRUCTURE. Make a note of HREFS for:
        //    REL_FONDS_STRUCTURE : Get a list of all fonds
        //    REL_FONDS_STRUCTURE_NEW_FONDS : Create a new fonds

        $http({
          method: 'GET',
          url: baseUrl,
          headers: {'Authorization': $scope.token}
        }).then(function successCallback(response) {
            $scope.applicationRoot = response.data;
            for (var rel in $scope.applicationRoot.links) {
              var relation = $scope.applicationRoot.links[rel].rel;

              if (relation == REL_FONDS_STRUCTURE) {
                var fondsStructureHref = $scope.applicationRoot.links[rel].href;
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
                      $http({
                        method: 'GET',
                        url: $scope.hrefFonds,
                        headers: {'Authorization': $scope.token}
                      }).then(function successCallback(response) {
                        $scope.fondsList = response.data.results;
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
                $scope.hrefLogout = $scope.applicationRoot.links[rel].href;
                console.log("hrefLogout is : " + JSON.stringify($scope.hrefLogout));
              }
            }
          }, function errorCallback(response) {
            if (response.status == -1) {
              consol
              e.log(MSG_NIKITA_DOWN_LOG + JSON.stringify(response));
              alert(MSG_NIKITA_DOWN);
            } else {
              console.log(MSG_NIKITA_UNKNOWN_ERROR_LOG + JSON.stringify(response));
              alert(MSG_NIKITA_UNKNOWN_ERROR);
            }
          }
        );
      };

      /**
       * updateFonds
       *
       * Undertakes a PUT request to the core with data fields from the webpage
       *
       */
      $scope.updateFonds = function () {

        for (var rel in $scope.fonds.links) {
          var relation = $scope.fonds.links[rel].rel;
          if (relation === REL_SELF) {
            var urlFonds = $scope.fonds.links[rel].href;
            console.log(" Attempting to update arkiv with following address = " + urlFonds);
            console.log(" Current ETAG is = [" + $scope.fondsETag + "]");
            $http({
              url: urlFonds,
              method: "PUT",
              headers: {
                'Content-Type': 'application/vnd.noark5-v4+json',
                'Authorization': $scope.token,
                'ETag': $scope.fondsETag
              },
              data: {
                tittel: $scope.fonds.tittel,
                beskrivelse: $scope.fonds.beskrivelse,
                dokumentmedium: $scope.selectedDocumentMedium,
                arkivstatus: $scope.selectedFondsStatus
              },
            }).then(function successCallback(response) {
              console.log(" put/post on fonds data returned= " + JSON.stringify(response.data));
              // Pick up and make a note of the ETAG so we can update the object
              $scope.fondsETag = response.headers('ETag');
              $scope.fonds = response.data;
            });
          }
        }
      };

      /**
       * updateSeries
       *
       * Undertakes a PUT request to the core with data fields from the webpage
       *
       */
      $scope.updateSeries = function () {

        for (var rel in $scope.series.links) {
          var relation = $scope.series.links[rel].rel;
          if (relation === REL_SELF) {
            var urlSeries = $scope.series.links[rel].href;
            console.log(" Attempting to update series with following address = " + urlSeries);
            console.log(" Current ETAG is = [" + $scope.seriesETag + "]");
            $http({
              url: urlSeries,
              method: "PUT",
              headers: {
                'Content-Type': 'application/vnd.noark5-v4+json',
                'Authorization': $scope.token,
                'ETag': $scope.seriesETag
              },
              data: {
                tittel: $scope.series.tittel,
                beskrivelse: $scope.series.beskrivelse,
                dokumentmedium: $scope.selectedDocumentMedium,
                arkivdelstatus: $scope.selectedSeriesStatus
              },
            }).then(function successCallback(response) {
              console.log(" put/post on series data returned= " + JSON.stringify(response.data));
              // Pick up and make a note of the ETAG so we can update the object
              $scope.seriesETag = response.headers('ETag');
              $scope.series = response.data;
            });
          }
        }
      };


      /**
       * createFondsCreator
       *
       * Undertakes either a PUT or a POST request to the core with data fields from the webpage
       *
       * The action is decided by whether or not $scope.createFonds == true. If it's true then we will
       * create a fonds. If it's false, we are updating a fonds.
       *
       */

      $scope.updateFondsCreator = function () {

        for (var rel in $scope.fondsCreator.links) {
          var relation = $scope.fondsCreator.links[rel].rel;
          if (relation === REL_SELF) {
            console.log("href for updating a fondsCreator is " + $scope.fondsCreator.links[rel].href);
            console.log(" Attempting to update fondsCreator with following ETAG = " + $scope.fondsCreatorETag);
            $http({
              url: $scope.fondsCreator.links[rel].href,
              method: "PUT",
              headers: {
                'Content-Type': 'application/vnd.noark5-v4+json',
                'Authorization': $scope.token,
                'ETAG': $scope.fondsCreatorETag
              },
              data: {
                arkivskaperID: $scope.fondsCreator.arkivskaperID,
                arkivskaperNavn: $scope.fondsCreator.arkivskaperNavn,
                beskrivelse: $scope.fondsCreator.beskrivelse
              },
            }).then(function successCallback(response) {
              console.log(" PUT on fondsCreator data returned= " + JSON.stringify(response.data));
              $scope.fondsCreatorETag = response.headers('ETag');
              // Update the fondsCreator object so fields in GUI are changed
              $scope.fondsCreator = response.data;
              // Pick up and make a note of the ETAG so we can update the object
              console.log("Etag after PUT on fondsCreator is = " + $scope.fondsCreatorETag);
              // Now we can edit the fonds object, add fondsCreator
            }, function errorCallback(request, response) {
              console.log(method + " PUT request = " + JSON.stringify(request));
            });
          }
        }
      };

      /**
       *
       * create a new FondsCreator associated with the current fonds
       */
      $scope.createFondsCreator = function () {

        for (var rel in $scope.fonds.links) {
          var relation = $scope.fonds.links[rel].rel;
          if (relation === REL_NEW_FONDS_CREATOR) {
            console.log("href for creating a fondsCreator is " + $scope.fonds.links[rel].href);
            console.log("href for creating a fondsCreator is " + $scope.fonds.links[rel].href);
            $http({
              url: $scope.fonds.links[rel].href,
              method: "POST",
              headers: {
                'Content-Type': 'application/vnd.noark5-v4+json',
                'Authorization': $scope.token,
              },
              data: {
                arkivskaperID: $scope.newIdForFondsCreator,
                arkivskaperNavn: $scope.newNameForFondsCreator,
                beskrivelse: $scope.newDescriptionForFondsCreator
              },
            }).then(function successCallback(response) {
              console.log("POST on fondsCreator data returned= " + JSON.stringify(response.data));

              $scope.doDismissNewFondsCreatorModal();

              // Update the fondsCreator object so fields in GUI are changed
              $scope.fondsCreator = response.data;

              // Pick up and make a note of the ETAG so we can update the object
              $scope.fondsCreatorETag = response.headers('eTag');

              $scope.fondsCreatorETag = '"' + $scope.fondsCreatorETag + '"';
              console.log("Etag after post on fondsCreator is = " + $scope.fondsCreatorETag);
            });
          }
        }
      };

      /**
       *
       * create a new Series associated with the current fonds
       */
      $scope.createSeries = function () {

        for (var rel in $scope.fonds.links) {
          var relation = $scope.fonds.links[rel].rel;
          if (relation === REL_NEW_SERIES) {
            console.log("href for creating a fondsCreator is " + $scope.fonds.links[rel].href);
            $http({
              url: $scope.fonds.links[rel].href,
              method: "POST",
              headers: {
                'Content-Type': 'application/vnd.noark5-v4+json',
                'Authorization': $scope.token,
              },
              data: {
                tittel: $scope.newTitleForSeries,
                beskrivelse: $scope.newDescriptionForSeries,
                dokumentmedium: $scope.selectedDocumentMedium,
                arkivdelstatus: "Opprettet"
              },
            }).then(function successCallback(response) {
              console.log("POST on series data returned= " + JSON.stringify(response.data));
              $scope.doDismissNewSeriesModal();
              // Update the fondsCreator object so fields in GUI are changed
              $scope.series = response.data;
              // Pick up and make a note of the ETAG so we can update the object
              $scope.seriesETag = response.headers('eTag');
              $scope.seriesETag = '"' + $scope.seriesETag + '"';
              console.log("Etag after post on series is = " + $scope.seriesETag);
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
        $scope.showFondsListCard = false;
        $scope.showFondsCard = false;
        $scope.showFondsCreatorListCard = false;
        $scope.showFondsCreatorCard = false;
        $scope.showSeriesListCard = false;
        $scope.showSeriesCard = false;
      };


      /**
       * createFonds
       *
       * Undertakes POST request to the core with data fields from the webpage
       * Results in a new fonds object being created and returned. Web-page is
       * updated so the newly returned fonds is shown.
       */
      $scope.createFonds = function () {

        $http({
          url: $scope.hrefNewFonds,
          method: "POST",
          headers: {
            'Content-Type': 'application/vnd.noark5-v4+json',
            'Authorization': $scope.token,
          },
          data: {
            tittel: $.trim(document.getElementById("nyTittelArkiv").value),
            beskrivelse: $.trim(document.getElementById("nyBeskrivelseArkiv").value),
            dokumentmedium: $.trim($scope.selectedDocumentMedium),
            arkivstatus: $.trim($scope.selectedFondsStatus)
          }
        }).then(function successCallback(response) {
            console.log("POST to create new fonds data returned= " + JSON.stringify(response.data));

            $scope.doDismissNewFondsModal();
            // Update the fonds object so fields in GUI are changed
            $scope.fonds = response.data;
            $scope.fondsList.push($scope.fonds);
            // Pick up and make a note of the ETAG so we can update the object
            $scope.fondsETag = response.headers('eTag');
            $scope.fondsETag = '"' + $scope.fondsETag + '"';
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

      $scope.doShowFondsListCard = function () {
        disableAllCards();
        $scope.showFondsListCard = true;
        $scope.showFondsBreadcrumb = false;
        $scope.showFondsCreatorBreadcrumb = false;
        $scope.showSeriesBreadcrumb = false;
        pageLoad();
      };

      $scope.doShowFondsCard = function () {
        disableAllCards();
        $scope.showFondsCard = true;
        $scope.showFondsBreadcrumb = true;
        $scope.showFondsCreatorBreadcrumb = false;
        $scope.showSeriesBreadcrumb = false;
      };

      $scope.doShowSeriesCard = function () {
        disableAllCards();
        $scope.showSeriesCard = true;
        $scope.showFondsBreadcrumb = true;
        $scope.showFondsCreatorBreadcrumb = false;
        $scope.showSeriesBreadcrumb = true;
      };

      $scope.doShowFondsCreatorCard = function () {
        disableAllCards();
        $scope.showFondsCreatorCard = true;
        $scope.showFondsBreadcrumb = true;
        $scope.showFondsCreatorBreadcrumb = true;
        $scope.showSeriesBreadcrumb = false;
      };

      $scope.doShowFondsCreatorListCard = function () {
        disableAllCards();
        $scope.showFondsCreatorListCard = true;
        $scope.showFondsBreadcrumb = true;
        $scope.showFondsCreatorBreadcrumb = true;
        $scope.showSeriesBreadcrumb = false;
        $scope.getListFondsCreator();
      };

      $scope.doShowSeriesListCard = function () {
        disableAllCards();
        $scope.showSeriesListCard = true;
        $scope.showFondsBreadcrumb = true;
        $scope.showSeriesBreadcrumb = true;
        $scope.getSeriesCreator();
      };

      $scope.doDismissNewFondsModal = function () {
        $scope.dismissNewFondsModal();
        $scope.newTitleForFonds = "";
        $scope.newDescriptionForFonds = "";
      };

      $scope.doDismissNewSeriesModal = function () {
        $scope.dismissNewSeriesModal();
        $scope.newIdForFondsCreator = "";
        $scope.newNameForFondsCreator = "";
        $scope.newDescriptionForFondsCreator = "";
      };

      $scope.doDismissNewFondsCreatorModal = function () {
        $scope.dismissNewFondsCreatorModal();
        $scope.newDescriptionForSeries = "";
        $scope.newTitleForSeries = "";
      };

      /**
       *  fonds_selected
       *
       */

      $scope.fondsCreatorSelected = function (fondsCreator) {
      };

      $scope.fondsSelected = function (fonds) {

        $scope.doShowFondsCard();
        // Retrieve the latest copy of the data and pull out the ETAG
        // Find the self link of the current fonds and issue a GET

        for (var rel in fonds.links) {
          var relation = fonds.links[rel].rel;
          if (relation == REL_SELF) {
            var urlToFonds = fonds.links[rel].href;
            var token = $scope.token;
            $http({
              method: 'GET',
              url: urlToFonds,
              headers: {'Authorization': token}
            }).then(function successCallback(response) {
              $scope.fonds = response.data;
              $scope.fondsETag = response.headers('eTag');
              console.log("Retrieved the following fonds " + JSON.stringify($scope.fonds));
              console.log("The ETAG header for the fonds is " + $scope.fondsETag);
              $scope.selectedDocumentMedium = $scope.fonds.dokumentmedium;
              $scope.selectedFondsStatus = $scope.fonds.arkivstatus;
            });
          }
        }
      };

      $scope.seriesSelected = function (series) {

        $scope.doShowSeriesCard();
        // Retrieve the latest copy of the data and pull out the ETAG
        // Find the self link of the current series and issue a GET
        for (var rel in series.links) {
          var relation = series.links[rel].rel;
          if (relation == REL_SELF) {
            $http({
              method: 'GET',
              url: series.links[rel].href,
              headers: {'Authorization': $scope.token}
            }).then(function successCallback(response) {
              $scope.series = response.data;
              $scope.seriesETag = response.headers('eTag');
              console.log("Retrieved the following series " + JSON.stringify($scope.series));
              console.log("The ETAG header for the series is " + $scope.seriesETag);
              $scope.selectedDocumentMedium = $scope.series.dokumentmedium;
              $scope.selectedSeriesStatus = $scope.series.arkivdelstatus;
            });
          }
        }
      };

      $scope.fondsCreatorSelected = function (fondsCreator) {
        $scope.doShowFondsCreatorCard();
        // Retrieve the latest copy of the data and pull out the ETAG
        // Find the self link of the current fondsCreator and issue a GET
        for (var rel in fondsCreator.links) {
          var relation = fondsCreator.links[rel].rel;
          if (relation == REL_SELF) {
            $http({
              method: 'GET',
              url: fondsCreator.links[rel].href,
              headers: {'Authorization': $scope.token}
            }).then(function successCallback(response) {
              $scope.fondsCreator = response.data;
              $scope.fondsCreatorETag = response.headers('eTag');
              console.log("Retrieved the following fondsCreator " + JSON.stringify($scope.fondsCreator));
              console.log("The ETAG header for the fondsCreator is " + $scope.fondsCreatorETag);
              $scope.selectedDocumentMedium = $scope.fondsCreator.dokumentmedium;
              $scope.selectedFondsCreatorStatus = $scope.fondsCreator.arkivdelstatus;
            });
          }
        }
      };

      $scope.getListFondsCreator = function () {
        for (var rel in $scope.fonds.links) {
          var relation = $scope.fonds.links[rel].rel;
          if (relation == REL_FONDS_CREATOR) {
            $http({
              method: 'GET',
              url: $scope.fonds.links[rel].href,
              headers: {'Authorization': $scope.token}
            }).then(function successCallback(response) {
              $scope.fondsCreatorList = response.data.results;
              console.log("Retrieved the following fondsCreatorList " + JSON.stringify($scope.fondsCreatorList));
            });
          }
        }
      };

      $scope.getSeriesCreator = function () {
        for (var rel in $scope.fonds.links) {
          var relation = $scope.fonds.links[rel].rel;
          if (relation == REL_SERIES) {
            $http({
              method: 'GET',
              url: $scope.fonds.links[rel].href,
              headers: {'Authorization': $scope.token}
            }).then(function successCallback(response) {
              $scope.seriesList = response.data.results;
              console.log("Retrieved the following seriesList " + JSON.stringify($scope.seriesList));
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

