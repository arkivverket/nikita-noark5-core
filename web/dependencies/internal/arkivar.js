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

var fondsController = app.controller('ArkivarController', ['$scope', '$http', '$window',
  function ($scope, $http, $window) {

    // Grab a copy of the authentication token
    $scope.token = GetUserToken();

    // get values used in drop downs. These will probably be replaced
    // by metadata calls to nikita
    $scope.documentMediumList = documentMediumList;
    $scope.fondsStatusList = fondsStatusList;

    pageLoad();


    function pageLoad () {

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
      $scope.newDescriptionForFonds = "";
      $scope.newTitleForFonds = "";

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
                  else if (relation == REL_FONDS_STRUCTURE_NEW_FONDS) {
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
     * createFondsCreator
     *
     * Undertakes either a PUT or a POST request to the core with data fields from the webpage
     *
     * The action is decided by whether or not $scope.createFonds == true. If it's true then we will
     * create a fonds. If it's false, we are updating a fonds.
     *
     */

    $scope.post_or_put_fonds_creator = function () {
      var urlFondsCreator = '';

      // check that it's not null, create a popup here if it is
      var method = '';
      if ($scope.createFondsCreator) {
        method = "POST";
        var currentFonds = GetChosenFonds();
        if (currentFonds != null) {
          // Check that currentFonds.links exists??
          // Find the self link
          for (var rel in currentFonds.links) {
            var relation = currentFonds.links[rel].rel;
            if (relation === REL_NEW_FONDS_CREATOR) {
              urlFondsCreator = currentFonds.links[rel].href;
              console.log("relation is " + relation);
              console.log("href is " + currentFonds.links[rel].href);

              $http({
                url: urlFondsCreator,
                method: method,
                headers: {
                  'Content-Type': 'application/vnd.noark5-v4+json',
                  'Authorization': GetUserToken(),
                },
                data: {
                  arkivskaperID: $.trim(document.getElementById("arkivskaperID").value),
                  beskrivelse: $.trim(document.getElementById("fondsCreatorBeskrivelse").value),
                  arkivskaperNavn: $.trim(document.getElementById("arkivskaperNavn").value)
                },
              }).then(function successCallback(response) {
                console.log(method + " post on fondsCreator data returned= " + JSON.stringify(response.data));

                // Update the fondsCreator object so fields in GUI are changed
                $scope.fondsCreator = response.data;
                // Pick up and make a note of the ETAG so we can update the object
                $scope.fondsCreatorETag = response.headers('eTag');

                // TODO: Figure out what's happening here!
                // For some reason, the returned ETAG on fondscreators/arkivskaper is missing "".
                // Debugging shows it's there server side, but client side it's missing. I don't have
                // time to figure out why, but we have avoid doing the following!
                $scope.fondsCreatorETag = '"' + $scope.fondsCreatorETag + '"';
                console.log("Etag after post on fondsCreator is = " + $scope.fondsCreatorETag);

                // Now we can edit the fonds object, add fondsCreator
                $scope.createFondsCreator = false;
              });
            }
          }
        }
      } else {
        method = "PUT";
        var currentFondsCreator = $scope.fondsCreator;
        if (currentFondsCreator != null) {
          // Check that currentFondsCreator.links exists??
          // Find the self link
          for (var rel in currentFondsCreator.links) {
            var relation = currentFondsCreator.links[rel].rel;
            if (relation === REL_SELF) {
              urlFondsCreator = currentFondsCreator.links[rel].href;
              console.log(method + " Attempting to update forndscreator with following address = " + urlFondsCreator);
              console.log(method + " Attempting to update forndscreator with following ETAG = " + $scope.fondsCreatorETag);
              $http({
                url: urlFondsCreator,
                method: method,
                headers: {
                  'Content-Type': 'application/vnd.noark5-v4+json',
                  'Authorization': GetUserToken(),
                  'ETAG': $scope.fondsCreatorETag
                },
                data: {
                  arkivskaperID: $.trim(document.getElementById("arkivskaperID").value),
                  beskrivelse: $.trim(document.getElementById("fondsCreatorBeskrivelse").value),
                  arkivskaperNavn: $.trim(document.getElementById("arkivskaperNavn").value)
                },
              }).then(function successCallback(response) {

                console.log(method + " PUT on fondsCreator data returned= " + JSON.stringify(response.data));
                // Update the fondsCreator object so fields in GUI are changed
                $scope.fondsCreator = response.data;
                // Pick up and make a note of the ETAG so we can update the object
                // For some reason, the returned ETAG on fondscreators/arkivskaper is missing "".
                // Debugging shows it's there server side, but client side it's missing. I don't have
                // time to figure out why, but we have avoid doing the following!
                $scope.fondsCreatorETag = '"' + $scope.fondsCreatorETag + '"';
                console.log("Etag after PUT on fondsCreator is = " + $scope.fondsCreatorETag);
                // Now we can edit the fonds object, add fondsCreator
                $scope.createFondsCreator = false;
              }, function errorCallback(request, response) {
                console.log(method + " PUT request = " + JSON.stringify(request));
              });
            }
          }
        }
        else {
          alert("Something went wrong. Attempt to update a fonds object that is not registered as existing yet!");
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
      pageLoad();
    };

    $scope.doDismissNewFondsModal = function () {
      $scope.dismissNewFondsModal();
      $scope.newDescriptionForFonds = "";
      $scope.newTitleForFonds = "";
    };


    /**
     *  fonds_selected
     *
     */

    $scope.fonds_selected = function (fonds) {
      disableAllCards();
      $scope.showFondsCard = true;

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

  }])
;

