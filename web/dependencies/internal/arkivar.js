/**
 * Controller for Fonds / FondsCreator / Series.
 * <p>
 * When page loads, the token is checked to make sure it is still valid. If
 * the token is not valid, the user is redirected to login page. If the token is
 * valid, the application root is fetched and from there a list of fonds.
 * <p>
 * Following functionality is provided:
 * <p>
 * 1. Create / Update fonds
 * 2. Create / Update fondsCreator
 * 3. Create / Update series
 */
app.controller('ArkivarController',
    ['$scope', '$http', 'loginService', 'nikitaService',
        function ($scope, $http, loginService, nikitaService) {

            // Grab a copy of the authentication token
            $scope.token = GetUserToken();
            $scope.oidc = GetOIDCInfo();

            if ($scope.oidc === undefined) {
                console.log("Missing OIDC information. Missing token functionality");
            }
            checkToken();

            $scope.fondsList = [];
            // get values used in drop downs. These will probably be replaced
            // by metadata calls to nikita
            $scope.fondsStatusList = fondsStatusList;
            $scope.seriesStatusList = seriesStatusList;

            $scope.showFondsBreadcrumb = false;
            $scope.showFondsCreatorBreadcrumb = false;
            $scope.showSeriesBreadcrumb = false;

            // Disable all cards except the fonds list one
            $scope.showFondsListCard = true;
            $scope.showFondsCard = false;
            $scope.showFondsCreatorListCard = false;
            $scope.showFondsCreatorCard = false;
            $scope.showSeriesListCard = false;
            $scope.showSeriesCard = false;

            // Set default values for drop downs
            $scope.selectedFondsStatusCode = "O";
            $scope.selectedFondsStatus = "Opprettet";

            // Create variables to bind with ng-model and modals so we can blank them out
            // For fonds
            $scope.newTitleForFonds = "";

            // For fondsCreator
            $scope.newIdForFondsCreator = "";
            $scope.newNameForFondsCreator = "";
            $scope.newDescriptionForFondsCreator = "";

            // For Series
            $scope.newDescriptionForSeries = "";
            $scope.newTitleForSeries = "";

            // GET the application root.
            // From here you can used REL_FONDS_STRUCTURE to get a list of all fonds
            (async () => {
                try {
                    $scope.fondsStructure = await nikitaService.getFondsStructureRoot($scope.token);
                    let fondsListObject = await nikitaService.getFondsList(
                        $scope.fondsStructure._links[REL_FONDS_STRUCTURE_FONDS].href, $scope.token);
                    if ("results" in fondsListObject) {
                        $scope.fondsList = await fondsListObject.results;
                    }
                    $scope.$apply($scope.fondsList);
                } catch (error) {
                    console.log(error.message);
                }
            })();

            /**
             * function updateFonds
             *
             * Undertakes a PUT request to the core with applicable data fields from the webpage.
             * Adds the following headers: Content-Type, Authorization and ETAG
             *
             */
            $scope.updateFonds = function () {

                let urlFonds = $scope.fonds._links[REL_SELF].href;
                console.log("Update fonds with following address = " + urlFonds);
                console.log("Current ETAG is = [" + $scope.fondsETag + "]");
                $http({
                    url: urlFonds,
                    method: "PUT",
                    headers: {
                        'Content-Type': 'application/vnd.noark5+json',
                        'Authorization': $scope.token,
                        'ETag': $scope.fondsETag
                    },
                    data: {
                        tittel: $scope.fonds.tittel,
                        beskrivelse: $scope.fonds.beskrivelse,
                        arkivstatus: {
			    kode: $scope.selectedFondsStatusCode,
			    kodenavn: $scope.selectedFondsStatus
			}
                    }
                }).then(function successCallback(response) {
                    console.log("PUT on fonds data returned= " + JSON.stringify(response.data));
                    // Pick up and make a note of the ETAG so we can update the object
                    $scope.fondsETag = response.headers('ETag');
                    console.log("Etag after PUT on fonds is = " + $scope.fondsETag);
                    $scope.fonds = response.data;
                }, function errorCallback(request) {
                    console.log("PUT request failed = " + JSON.stringify(request));
                });
            };

            /**
             * function updateSeries
             *
             * Undertakes a PUT request to the core with applicable data fields from the webpage.
             * Adds the following headers: Content-Type, Authorization and ETAG
             *
             */
            $scope.updateSeries = function () {
                let urlSeries = $scope.series._links[REL_SELF].href;
                console.log("Update series with following address = " + urlSeries);
                console.log("Current ETAG is = [" + $scope.seriesETag + "]");
                $http({
                    url: urlSeries,
                    method: "PUT",
                    headers: {
                        'Content-Type': 'application/vnd.noark5+json',
                        'Authorization': $scope.token,
                        'ETag': $scope.seriesETag
                    },
                    data: {
                        tittel: $scope.series.tittel,
                        beskrivelse: $scope.series.beskrivelse,
                        arkivdelstatus: {
			    kode: $scope.selectedSeriesStatusCode,
			    kodenavn: $scope.selectedSeriesStatus
			}
                    }
                }).then(function successCallback(response) {
                    console.log("PUT on series data returned= " + JSON.stringify(response.data));
                    $scope.seriesETag = response.headers('ETag');
                    console.log("Etag after PUT on series is = " + $scope.seriesETag);
                    $scope.series = response.data;
                }, function errorCallback(request) {
                    console.log("PUT request failed = " + JSON.stringify(request));
                });
            };

            /**
             * function updateFondsCreator
             *
             * Undertakes a PUT request to the core with applicable data fields from the webpage.
             * Adds the following headers: Content-Type, Authorization and ETAG
             *
             */
            $scope.updateFondsCreator = function () {
                let urlFondsCreator = $scope.fondsCreator._links[REL_SELF].href;
                console.log("Update fondsCreator with following address " + urlFondsCreator);
                console.log("Current ETAG is = [" + $scope.fondsCreatorETag + "]");
                $http({
                    url: urlFondsCreator,
                    method: "PUT",
                    headers: {
                        'Content-Type': 'application/vnd.noark5+json',
                        'Authorization': $scope.token,
                        'ETAG': $scope.fondsCreatorETag
                    },
                    data: {
                        arkivskaperID: $scope.fondsCreator.arkivskaperID,
                        arkivskaperNavn: $scope.fondsCreator.arkivskaperNavn,
                        beskrivelse: $scope.fondsCreator.beskrivelse
                    }
                }).then(function successCallback(response) {
                    console.log("PUT on fondsCreator data returned= " + JSON.stringify(response.data));
                    $scope.fondsCreatorETag = response.headers('ETag');
                    console.log("Etag after PUT on fondsCreator is = " + $scope.fondsCreatorETag);
                    // Update the fondsCreator object so fields in GUI are changed
                    $scope.fondsCreator = response.data;
                }, function errorCallback(request) {
                    console.log("PUT request failed = " + JSON.stringify(request));
                });
            };

            /**
             * function createFondsCreator
             *
             * Undertakes a POST request to the core to create a FondsCreator with applicable
             * data fields from the webpage. Adds the following headers:
             * Content-Type and Authorization
             *
             */

            $scope.createFondsCreator = function () {
                let urlFondsCreator = $scope.fonds._links[REL_NEW_FONDS_CREATOR].href;
                console.log("Create fondsCreator with following address " + urlFondsCreator);
                $http({
                    url: urlFondsCreator,
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/vnd.noark5+json',
                        'Authorization': $scope.token
                    },
                    data: {
                        arkivskaperID: $scope.newIdForFondsCreator,
                        arkivskaperNavn: $scope.newNameForFondsCreator,
                        beskrivelse: $scope.newDescriptionForFondsCreator
                    }
                }).then(function successCallback(response) {
                    console.log("POST on fondsCreator data returned= " +
                        JSON.stringify(response.data));
                    $scope.doDismissNewFondsCreatorModal();
                    // Update the fondsCreator object so fields in GUI are changed
                    $scope.fondsCreator = response.data;
                    // Pick up and make a note of the ETAG so we can update the object
                    $scope.fondsCreatorETag = response.headers('eTag');
                    console.log("Etag after post on fondsCreator is = " + $scope.fondsCreatorETag);
                    $scope.reloadFonds();
                }, function errorCallback(request) {
                    console.log("POST request (new FondsCreator) failed = " + JSON.stringify(request));
                });
            };

            /**
             * function createSeries
             *
             * Undertakes a POST request to the core to create a Series with applicable
             * data fields from the webpage. Adds the following headers:
             * Content-Type and Authorization
             *
             */
            $scope.createSeries = function () {
                let urlNewSeries = $scope.fonds._links[REL_NEW_SERIES].href;
                console.log("Create series with following address " + urlNewSeries);
                $http({
                    url: urlNewSeries,
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/vnd.noark5+json',
                        'Authorization': $scope.token,
                    },
                    data: {
                        tittel: $scope.newTitleForSeries,
                        beskrivelse: $scope.newDescriptionForSeries,
                        arkivdelstatus: {
			    kode: "A",
			    kodenavn: "Aktiv periode"
			}
                    },
                }).then(function successCallback(response) {
                    console.log("POST on series data returned= " +
                        JSON.stringify(response.data));
                    $scope.doDismissNewSeriesModal();
                    // Update the series object so fields in GUI are changed
                    $scope.series = response.data;
                    // Pick up and make a note of the ETAG so we can update the object
                    $scope.seriesETag = response.headers('eTag');
                    console.log("Etag after POST on series is = " + $scope.seriesETag);
                    $scope.reloadFonds();
                }, function errorCallback(request) {
                    console.log("POST request (new Series) failed = " + JSON.stringify(request));
                });
            };

            /**
             * createFonds
             *
             * Undertakes POST request to the core with data fields from the webpage
             * Results in a new fonds object being created and returned. Web-page is
             * updated so the newly returned fonds is shown.
             */
            $scope.createFonds = function () {
                let urlFonds = $scope.fondsStructure._links[REL_FONDS_STRUCTURE_NEW_FONDS].href;
                console.log("Create fonds with following address " + urlFonds);

                $http({
                    url: urlFonds,
                    method: "POST",
                    headers: {
                        'Content-Type': 'application/vnd.noark5+json',
                        'Authorization': $scope.token,
                    },
                    data: {
                        tittel: $.trim(document.getElementById("nyTittelArkiv").value),
                        beskrivelse: $.trim(document.getElementById("nyBeskrivelseArkiv").value),
                        arkivstatus: {
			    'kode': $.trim($scope.selectedFondsStatusCode),
			    'kodenavn': $.trim($scope.selectedFondsStatus)
			}
                    }
                }).then(function successCallback(response) {
                    console.log("POST to create new fonds data returned= " + JSON.stringify(response.data));

                    $scope.doDismissNewFondsModal();
                    // Update the fonds object so fields in GUI are changed
                    $scope.fonds = response.data;
                    $scope.fondsList.push($scope.fonds);
                    // Pick up and make a note of the ETAG so we can update the object
                    $scope.fondsETag = response.headers('eTag');
                }, function errorCallback(request) {
                    console.log("POST request (new FondsCreator) failed = " + JSON.stringify(request));
                })
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
            }

            $scope.doShowFondsListCard = function () {
                disableAllCards();
                $scope.showFondsListCard = true;
                $scope.showFondsBreadcrumb = false;
                $scope.showFondsCreatorBreadcrumb = false;
                $scope.showSeriesBreadcrumb = false;
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
                $scope.getSeriesList();
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

            $scope.doLoadSeriesModal = function () {
                $scope.selectedDocumentMediumNewSeries = $scope.fonds.dokumentmedium;
            };


            $scope.fondsSelected = function (fonds) {
                $scope.doShowFondsCard();
                // Retrieve the latest copy of the data and pull out the ETAG
                // Find the self link of the current fonds and issue a GET
                let urlToFonds = fonds._links[REL_SELF].href;
                $http({
                    method: 'GET',
                    url: urlToFonds,
                    headers: {'Authorization': $scope.token}
                }).then(function successCallback(response) {
                    $scope.fonds = response.data;
                    $scope.fondsETag = response.headers('eTag');
                    console.log("Retrieved the following fonds " + JSON.stringify($scope.fonds));
                    console.log("The ETAG header for the fonds is " + $scope.fondsETag);
                    $scope.selectedFondsStatusCode = $scope.fonds.arkivstatus.kode;
                    $scope.selectedFondsStatus = $scope.fonds.arkivstatus.kodenavn;
                });
            };

            $scope.seriesSelected = function (series) {

                $scope.doShowSeriesCard();
                // Retrieve the latest copy of the data and pull out the ETAG
                // Find the self link of the current series and issue a GET
                $http({
                    method: 'GET',
                    url: series._links[REL_SELF].href,
                    headers: {'Authorization': $scope.token}
                }).then(function successCallback(response) {
                    $scope.series = response.data;
                    $scope.seriesETag = response.headers('eTag');
                    console.log("Retrieved the following series " + JSON.stringify($scope.series));
                    console.log("The ETAG header for the series is " + $scope.seriesETag);
                    $scope.selectedDocumentMediumSeries = $scope.series.dokumentmedium;
                    $scope.selectedSeriesStatusCode = $scope.series.arkivdelstatus.kode;
                    $scope.selectedSeriesStatus = $scope.series.arkivdelstatus.kodenavn;
                });
            };

            $scope.fondsCreatorSelected = function (fondsCreator) {
                $scope.doShowFondsCreatorCard();
                // Retrieve the latest copy of the data and pull out the ETAG
                // Find the self link of the current fondsCreator and issue a GET
                $http({
                    method: 'GET',
                    url: fondsCreator._links[REL_SELF].href,
                    headers: {'Authorization': $scope.token}
                }).then(function successCallback(response) {
                    $scope.fondsCreator = response.data;
                    $scope.fondsCreatorETag = response.headers('eTag');
                    console.log("Retrieved the following fondsCreator " +
                        JSON.stringify($scope.fondsCreator));
                    console.log("The ETAG header for the fondsCreator is " +
                        $scope.fondsCreatorETag);
                });
            };

            $scope.getListFondsCreator = function () {
                if (REL_FONDS_CREATOR in $scope.fonds._links) {
                    $http({
                        method: 'GET',
                        url: $scope.fonds._links[REL_FONDS_CREATOR].href,
                        headers: {'Authorization': $scope.token}
                    }).then(function successCallback(response) {
                        $scope.fondsCreatorList = response.data.results;
                        console.log("Retrieved the following fondsCreatorList " +
                            JSON.stringify($scope.fondsCreatorList));
                    });
                } else {
                    console.log("Fonds " + JSON.stringify($scope.fonds)
                        + "has no child " + REL_FONDS_CREATOR);
                }
            };

            $scope.getSeriesList = function () {
                if (REL_SERIES in $scope.fonds._links) {
                    $http({
                        method: 'GET',
                        url: $scope.fonds._links[REL_SERIES].href,
                        headers: {'Authorization': $scope.token}
                    }).then(function successCallback(response) {
                        $scope.seriesList = response.data.results;
                        console.log("Retrieved the following seriesList " +
                            JSON.stringify($scope.seriesList));
                    });
                } else {
                    console.log("Fonds " + JSON.stringify($scope.fonds)
                        + " has no child " + REL_SERIES);
                }
            };

            $scope.doLogout = function () {
                console.log("Attempting logout on [" + $scope.oidc[LOGOUT_ENDPOINT].href + "]. using token [" +
                    $scope.token + "]");

                (async () => {
                    try {
                        await loginService.doLogout(
                            $scope.oidc[LOGOUT_ENDPOINT].href);
                    } catch (error) {
                        console.log(error.message);
                    }
                })();
                changeLocation($scope, loginPage, false);
            };

            /**
             * function reloadFonds
             *
             * There is a requirement to reload the fonds object in response to _links that
             * will be added when the first series or the first fondsCreator object is added
             */
            $scope.reloadFonds = function () {
                (async () => {
                    try {
                        $scope.fonds = await nikitaService.getObject(
                            $scope.fonds._links[REL_SELF].href, $scope.token);
                        $scope.$apply($scope.fondsList = $scope.fonds);
                    } catch (error) {
                        console.log(error.message);
                    }
                })();
            };

            /**
             * function checkToken
             *
             * Check to see if the token is still valid. If not redirect to
             * login page.
             */
            function checkToken() {
                (async () => {
                    try {

                        if ($scope.token !== undefined) {
                            let values = $scope.token.split(" ");
                            if (values.length === 2) {
                                let token = values[1];
                                let changeToLogin = await loginService.doCheckToken(
                                    $scope.oidc[CHECK_TOKEN_ENDPOINT], token);
                                if (changeToLogin === false) {
                                    changeLocation($scope, loginPage, true);
                                }
                            } else {
                                changeLocation($scope, loginPage, true);
                            }
                        }
                    } catch (error) {
                        console.log(error.message);
                    }
                })();
            }
        }
    ])
;

