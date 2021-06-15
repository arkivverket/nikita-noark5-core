/**
 * BildeLoginController
 *
 * This is a temporary addition to the codebase to explore a Picture GUI.
 *
 * I want to keep this separate from the other code until we find out what we will
 * do with this code
 *
 */

var login = app.controller('BildeLoginController',
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
            $scope.selectedLoginRole = "bilde";

            $scope.loginOptions = [
                {id: 'BE', value: "bilde"}
            ];

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
                        if ($scope.selectedLoginRole === 'bilde') {
                            changeLocation($scope, 'picture.html', true);
                        }
                    } catch (error) {
                        $scope.errorMessage = JSON.stringify(error.message);
                        $scope.$apply($scope.error = true);
                    }
                })();
            };
        }]);
