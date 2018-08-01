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

var fondsController = app.controller('ArkivarController', ['$scope', '$http',
  function ($scope, $http) {

    $scope.showFondsList = true;
    $scope.showFonds = false;
    $scope.showFondsCreatorList = false;
    $scope.showSeriesList = false;

    $scope.selectedFondsStatus = "Opprettet";
    $scope.selectedDocumentMedium = "Elektronisk arkiv";

    // Used to show or hide a text message
    $scope.createFonds = false;
    $scope.createFondsCreator = true;

    console.log("User token is set to " + GetUserToken());

    $scope.documentMediumList = documentMediumList;
    $scope.fondsStatusList = fondsStatusList;
    $scope.oppbevaringsStedDisabled = false;

    $scope.token = GetUserToken();
    $scope.show_create_fonds = true;

    $http({
      method: 'GET',
      url: baseUrl,
      headers: {'Authorization': $scope.token},
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
            $scope.fondsStructure = response.data.links;
            console.log("fondsStructure is : " + JSON.stringify($scope.fondsStructure));
          }, function errorCallback(response) {
            if (response.status == -1) {
              console.log("Looks like nikita is down at the moment : " + JSON.stringify(response));
              alert("Problemer med å koble meg opp mot nikita-kjernen. Ser ut som om nikita er nede. Prøv igjen senere eller" +
                " kontakt administrator.");
            } else {
              console.log("Unknown error when connecting to nikita. Message is : " + JSON.stringify(response));
              alert("Ukjent problem med å koble meg opp mot nikita-kjernen. Prøv igjen senere eller kontakt administrator.");
            }
          });
        }
      }
    }, function errorCallback(response) {
      if (response.status == -1) {
        console.log("Looks like nikita is down at the moment : " + JSON.stringify(response));
        alert("Problemer med å koble meg opp mot nikita-kjernen. Ser ut som om nikita er nede. Prøv igjen senere eller" +
          " kontakt administrator.");
      } else {
        console.log("Unknown error when connecting to nikita. Message is : " + JSON.stringify(response));
        alert("Ukjent problem med å koble meg opp mot nikita-kjernen. Prøv igjen senere eller kontakt administrator.");
      }

    });

    /**
     *  fonds_selected
     *
     *  When a user presses "Opprett nytt arkiv" or clicks on a individual row of a fonds item from
     *  the list of fonds, this function calls a change location to the page arkiv.html
     *
     * @param fonds Note: Can be null if creating a new fonds
     */
    $scope.fonds_selected = function (fonds) {

      console.log('fonds_selected clicked ' + JSON.stringify(fonds));

      disableAllViews();
      $scope.showFonds = true;
      $scope.fonds = fonds;

      $scope.selectedDocumentMedium = fonds.dokumentmedium;
      $scope.selectedFondsStatus = fonds.arkivstatus;

      if (fonds == null) {
        // Important as business logic requires this to be null if we are creating a top-level fonds
        SetChosenFonds(null);
      }
      else {
        SetChosenFonds(fonds);
      }
    };

    $scope.getFonds = function () {


      $scope.fonds = GetChosenFonds();
      console.log("current fonds is " + $scope.fonds);

      // We are either creating a new fonds or retrieving an existing one
      if ($scope.fonds != null) {

        // Retrieve the latest copy of the data and pull out the ETAG
        // Find the self link of the current fonds and issue a GET

        for (var rel in $scope.fonds.links) {
          var relation = $scope.fonds.links[rel].rel;
          if (relation == REL_SELF) {
            var urlToFonds = $scope.fonds.links[rel].href;
            var token = GetUserToken();
            $http({
              method: 'GET',
              url: urlToFonds,
              headers: {'Authorization': token}
            }).then(function successCallback(response) {
              // This returns a list and later we will handle a list properly in GUI, but right now I just
              // need to fetch the first one. I also need an ETAG in case it is to be edited, so I have to
              // retrieve (again) the object I am actually out after
              $scope.fonds = response.data;
              $scope.fondsETag = response.headers('eTag');
              console.log("Retrieved the following fonds " + JSON.stringify($scope.fonds));
              console.log("The ETAG header for the fonds is " + $scope.fondsETag);
            });
          }
        }

        $scope.selectedDocumentMedium = $scope.fonds.dokumentmedium;
        $scope.selectedFondsStatus = $scope.fonds.arkivstatus;
        console.log("scope.selectedFondsStatus is set to [" + $scope.selectedFondsStatus + "]");

        // If we are loading the data from a fonds, check to see if it has any related fondsCreator data
        for (var rel in $scope.fonds.links) {
          var relation = $scope.fonds.links[rel].rel;
          if (relation == REL_FONDS_CREATOR) {
            var urlFondsCreators = $scope.fonds.links[rel].href;
            var token = GetUserToken();
            $http({
              method: 'GET',
              url: urlFondsCreators,
              headers: {'Authorization': token}
            }).then(function successCallback(response) {
                // This returns a list and later we will handle a list properly in GUI, but right now I just
                // need to fetch the first one. I also need an ETAG in case it is to be edited, so I have to
                // retrieve (again) the object I am actually out after
                $scope.fondsCreator = response.data;
                console.log("Found following fondsCreators " + JSON.stringify(response.data.results));
                console.log("[0] Found following fondsCreators " + JSON.stringify(response.data.results[0]));

                // We got the list and picked out the first. Now we need to retrieve a ETAG
                if (response.data.results) {
                  $scope.fondsCreator = response.data.results[0];
                  if ($scope.fondsCreator.arkivskaperID != null) {
                    $scope.createFondsCreator = false;
                    console.log("$scope.fondsCreator.arkivskaperID " + $scope.fondsCreator.arkivskaperID);
                    for (var rel in $scope.fondsCreator.links) {
                      var relation = $scope.fondsCreator.links[rel].rel;
                      console.log("relation " + relation);
                      if (relation == REL_SELF) {
                        var urlFondsCreator = $scope.fondsCreator.links[rel].href;
                        console.log("Getting urlFondsCreator" + urlFondsCreator);
                        var token = GetUserToken();
                        $http({
                          method: 'GET',
                          url: urlFondsCreator,
                          headers: {'Authorization': token}
                        }).then(function successCallback(response) {
                          // This returns a list and later we will handle a list properly in GUI, but right now I just
                          // need to fetch the first one. I also need an ETAG in case it is to be edited, so I have to
                          // retrieve (again) the object I am actually out after
                          $scope.fondsCreator = response.data;
                          $scope.fondsCreatorETag = response.headers('eTag');
                        });
                      }
                    }

                  }

                }
              }
            );
          }
        }
      }
      else {
        $scope.createFonds = true;
      }

      console.log("createFonds is set to " + $scope.createFonds);

    };

    // Probably don't need this!!
    $scope.documentMedium_selected = function () {

      if ($scope.selectedDocumentMedium == 'Elektronisk arkiv ') {
        $scope.oppbevaringsStedDisabled = true;
      }
      else {
        $scope.oppbevaringsStedDisabled = false;
      }
      console.log("oppbevaringsStedDisabled value [" + $scope.oppbevaringsStedDisabled +
        "] val is [" + $scope.selectedDocumentMedium + "], [Elektronisk arkiv]");
    };

    /**
     * series_list_selected
     *
     * Assumes the current fonds is set. Simply calls arkivdeliste.html
     */
    $scope.series_list_selected = function () {
      changeLocation($scope, seriesListPageName, false);
    };


    /**
     * updateFonds
     *
     * Undertakes a PUT request to the core with data fields from the webpage
     *
     *
     */

    $scope.updateFonds = function () {

      if ($scope.fonds != null) {
        // Check that currentFonds.links exists??
        // Find the self link
        for (var rel in $scope.fonds.links) {
          var relation = $scope.fonds.links[rel].rel;
          if (relation === REL_SELF) {
            var urlFonds = $scope.fonds.links[rel].href;
            console.log(method + " Attempting to update arkiv with following address = " + urlFonds);
            console.log(method + " Current ETAG is = [" + $scope.fondsETag + "]");
            $http({
              url: urlFonds,
              method: "PUT",
              headers: {
                'Content-Type': 'application/vnd.noark5-v4+json',
                'Authorization': GetUserToken(),
                'ETAG': $scope.fondsETag
              },
              data: {
                tittel: $.trim(document.getElementById("tittel").value),
                beskrivelse: $.trim(document.getElementById("beskrivelse").value),
                dokumentmedium: $.trim($scope.selectedDocumentMedium),
                arkivstatus: $.trim($scope.selectedFondsStatus)
              },
            }).then(function successCallback(response) {
              console.log(method + " put/post on fonds data returned= " + JSON.stringify(response.data));
              // Pick up and make a note of the ETAG so we can update the object
              $scope.fondsETag = response.headers('eTag');
              $scope.fonds = response.data;
              SetChosenFonds($scope.fonds);
            });
          }
        }
      }
      else {
        alert("Noe gikk galt her! Kan ikke oppdatere en arkiv objekt som ikke finnes!");
      }
    };

    /**
     * createFonds
     *
     * Undertakes POST request to the core with data fields from the webpage
     *
     */
    $scope.createFonds = function () {

      var urlFonds = create_fonds_address;
      $http({
        url: urlFonds,
        method: "POST",
        headers: {
          'Content-Type': 'application/vnd.noark5-v4+json',
          'Authorization': GetUserToken(),
        },
        data: {
          tittel: $.trim(document.getElementById("tittel").value),
          beskrivelse: $.trim(document.getElementById("beskrivelse").value),
          dokumentmedium: $.trim($scope.selectedDocumentMedium),
          arkivstatus: $.trim($scope.selectedFondsStatus)
        },
      }).then(function successCallback(response) {
        console.log(method + " put/post on fonds data returned= " + JSON.stringify(response.data));
        // Update the fonds object so fields in GUI are changed
        $scope.fonds = response.data;
        // Pick up and make a note of the ETAG so we can update the object
        $scope.fondsETag = response.headers('eTag');
        // TODO : This needs to be tided up. Find out why etag is missing ""!!
        $scope.fondsETag = '"' + $scope.fondsETag + '"';

        // Now we can edit the fonds object, add fondsCreator
        $scope.createFonds = false;
        SetChosenFonds($scope.fonds);
      });
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

    function disableAllViews() {
      $scope.showFondsList = false;
      $scope.showFonds = false;
      $scope.showFondsCreatorList = false;
      $scope.showFondsCreator = false;
      $scope.showSeriesList = false;
      $scope.showSeries = false;
    }
  }
]);

