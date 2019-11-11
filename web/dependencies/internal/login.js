/**
 * LoginController
 *
 * This file provides the following functionality:
 *
 *  1. When the page loads the nikita core is called to retrieve the login URL
 *    - In this case, we look for a OAUTH2 REL (https://nikita.arkivlab.no/noark5/v5/login/rfc6749)
 *  2. When a successful login occurs, the user is pushed to the correct html page
 *   - This process will also issue a GET to the root of the application and
 *     retrieve and GET the content behind https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/.
 *     This object is then stored in localstorage so the application knows how to retrieve
 *     fonds objects and create them.
 *
 */

var login = app.controller('LoginController',
    ['$scope', '$http', 'loginService',
        function ($scope, $http, loginService) {

            // Grab a copy of the authentication token
            $scope.token = GetUserToken();
            $scope.oidc = GetOIDCInfo();

            if ($scope.oidc === undefined) {
                console.log("Missing OIDC information. Missing token functionality");
            }

            $scope.errorMessage = MSG_NIKITA_DOWN;
            $scope.error = false;

            //$scope.emailAddress = "admin@example.com";
            //$scope.password = "password";

            // This sets 'arkivar' to be the default choice on the webpage
            $scope.selectedLoginRole = "arkivar";
            $scope.selectedLoginRole = "saksbehandler";

            // Set values for dropdown in webpage
            $scope.loginOptions = loginOptions;

            // index.html, booleans to display cards
            $scope.showLogin = true;
            $scope.showAbout = false;
            $scope.showSourceCode = false;

            // Set account details blank
            $scope.emailAddressRegister = "";
            $scope.passwordRegister = "";
            $scope.repeatPasswordRegister = "";
            $scope.firstnameRegister = "";
            $scope.lastnameRegister = "";

            (async () => {
                try {
                    $scope.oidc = await loginService.getLoginHrefViaOIDC();
                    SetOIDCInfo($scope.oidc);
                } catch (error) {
                    $scope.$apply($scope.error = true);
                }
            })();

            /**
             * doLogin
             *
             * Perform the actual login and redirect the logged in user to the correct page
             *
             */
            $scope.doLogin = function () {
                (async () => {
                    try {
                        $scope.tokenInfo = await loginService.doLogin($scope.oidc[LOGIN_ENDPOINT],
                            $scope.emailAddress, $scope.password);
                        $scope.token = $scope.tokenInfo[OAUTH_ACCESS_TOKEN];
                        console.log("Token is " + $scope.token);
                        SetUserToken("Bearer " + $scope.token);
                        SetUserTokenInfo($scope.tokenInfo);
                        SetUsername($scope.emailAddress);
                        console.log("Logging in. Token is " + JSON.stringify(
                            $scope.tokenInfo[OAUTH_ACCESS_TOKEN]) +
                            ". Redirecting to page for " + $scope.selectedLoginRole);
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
            };

            /**
             * createAccount
             *
             * Create an account for nikita
             *
             */
            $scope.createUser = function () {
                let url = $scope.oidc[REGISTRATION];
                console.log("Attempting to create User using [" + url + "]");
                if ($scope.repeatPasswordRegister === $scope.passwordRegister) {
                    $http({
                        url: url,
                        method: "POST",
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Basic ' + btoa('nikita-client:secret')
                        },
                        data: {
                            brukerNavn: $scope.emailAddressRegister,
                            passord: $scope.passwordRegister,
                            fornavn: $scope.firstnameRegister,
                            etternavn: $scope.lastnameRegister
                        }
                    }).then(function (data) {
                        console.log("Created new user. " + JSON.stringify(data.data));
                        $scope.dismissNewUserModal();
                        $scope.emailAddress = $scope.emailAddressRegister;
                    }, function (response) {
                        $scope.dismissNewUserModal();
                        console.log(JSON.stringify(response));
                    });
                } else {
                    $scope.passwordNotMatching = true;
                    console.log("NO MATCH");
                }
            };

            $scope.showLoginCard = function () {
                $scope.showLogin = true;
                $scope.showAbout = false;
                $scope.showSourceCode = false;
            };

            $scope.showAboutCard = function () {
                $scope.showLogin = false;
                $scope.showAbout = true;
                $scope.showSourceCode = false;
            };

            $scope.showSourceCodeCard = function () {
                $scope.showLogin = false;
                $scope.showAbout = false;
                $scope.showSourceCode = true;
            };
        }]);
