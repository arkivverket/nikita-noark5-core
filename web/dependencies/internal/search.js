/**
 * This is currently only intended to be used by nikita.oslomet.no. That is
 * the reason for hard-coded values.
 */
var searchController = app.controller('SearchController',
    ['$scope', '$http', 'loginService',
        function ($scope, $http, loginService) {

            $scope.odataQuery = "arkivstruktur/mappe?$filter=contains(tittel, 'file')&$top=2&$orderby=tittel desc";
            $scope.baseUrlForOdataSearch = "https://nikita.oslomet.no/noark5v5/api/";

            // It's not a secret
            $scope.emailAddress = "admin@example.com";
            $scope.password = "password";
            doLoginSearch();

            // Grab a copy of the authentication token
            $scope.token = GetUserToken();
            var loggedInUser = GetUsername();
            if (loggedInUser !== null && loggedInUser !== undefined
                && loggedInUser !== "") {
                $scope.loggedIn = true;
                $scope.emailAddress = loggedInUser;
            }

            function doLoginSearch() {
                (async () => {
                    try {
                        $scope.tokenInfo = await loginService.doLogin(
                            "https://nikita.oslomet.no/noark5v5/oauth/token",
                            $scope.emailAddress, $scope.password);
                        $scope.token = "Bearer " + $scope.tokenInfo[OAUTH_ACCESS_TOKEN];
                        console.log("Token is " + $scope.token);
                        SetUserToken("Bearer " + $scope.token);
                        SetUserTokenInfo($scope.tokenInfo);
                        SetUsername($scope.emailAddress);
                        console.log("Logging in. Token is " + JSON.stringify(
                            $scope.tokenInfo[OAUTH_ACCESS_TOKEN]));
                        if ($scope.selectedLoginRole === ROLE_RECORDS_MANAGER) {
                            changeLocation($scope, recordsManagerPage, true);
                        } else if ($scope.selectedLoginRole === ROLE_CASE_HANDLER) {
                            changeLocation($scope, caseHandlerPage, true);
                        }
                    } catch (error) {
                        $scope.errorMessage = JSON.stringify(error.message);
                        $scope.$apply($scope.error = true);
                    }
                })();
            }

            function getJson() {
                try {
                    if ($scope.odataResult != undefined)
                        return JSON.parse($scope.odataResult);
                    else
                        return JSON.parse("{}");
                } catch (ex) {
                    alert('Wrong JSON Format: ' + ex);
                }
            }

            var editor = new JsonEditor('#json-display', getJson());

            $('#translate').on('click', function () {
                editor.load(getJson());
            });


            $scope.doSearch = function () {

                if ($scope.baseUrlForOdataSearch === "") {
                    console.log("$scope.baseUrlForOdataSearch is empty, don't know what to do!");
                    return;
                }

                // Need a better way to verify the text
                if ($scope.odataQuery.length < 5) {
                    console.log("searchString.length < 5, don't know what to do!");
                    return;
                }
                const query = $scope.odataQuery.split("?");
                const queryParameters = query[1].split("$");
                let finalQuery = query[0] + "?";
                for (let i = 0; i < queryParameters.length; i++) {
                    if (queryParameters[i] !== "") {
                        let parameterCommand = queryParameters[i].split("=");
                        finalQuery += "$" + parameterCommand[0] + "=" +
                            encodeURIComponent(parameterCommand[1]);
                        if (!parameterCommand[1].endsWith("&")) {
                            finalQuery += encodeURIComponent("&");
                        }
                    }
                }
                finalQuery = finalQuery.substring(0, finalQuery.length - 3);
                var httpRequest = $scope.baseUrlForOdataSearch + "/" + finalQuery;
                $http({
                    method: 'GET',
                    url: httpRequest,
                    headers: {'Authorization': $scope.token}
                }).then(function successCallback(response) {
                    $scope.odataResult = JSON.stringify(response.data);
                    editor.load(getJson());
                }, function (response) {
                    alert(JSON.stringify(response));
                });
            };
        }
    ])
;

