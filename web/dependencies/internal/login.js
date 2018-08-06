var app = angular.module('nikita', []);

/**
 * LoginController
 *
 * This file provides the following functionality:
 *
 *  1. When the page loads the nikita core is called to retrieve the login URL
 *    - In this case, we look for a OAUTH2 REL (http://nikita.arkivlab.no/noark5/v4/login/rfc6749)
 *  2. When a successful login occurs, the user is pushed to the correct html page
 *   - This process will also issue a GET to the root of the application and
 *     retrieve and GET the content behind http://rel.kxml.no/noark5/v4/api/arkivstruktur/.
 *     This object is then stored in localstorage so the application knows how to retrieve
 *     fonds objects and create them.
 *
 */

app.directive('newUserModalDir', function () {
  return {
    restrict: 'A',
    link: function (scope, element) {
      scope.dismissNewUserModal = function () {
        element.modal('hide');
      };
    }
  };
});


var login = app.controller('LoginController',
  ['$scope', '$http', '$httpParamSerializer',
    function ($scope, $http, $httpParamSerializer) {

      // This sets 'arkivar' to be the default choice on the webpage
      $scope.selectedLoginRole = "arkivar";

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

      // connect to nikita, do a GET on application root and make a note of the
      // login REL/HREF. This also serves a a check that nikita is actually running
      // before a user tries to login.
      $http({
        method: 'GET',
        url: baseUrl,
      }).then(function successCallback(response) {

        console.log("Application Root data is: " + JSON.stringify(response.data));

        for (var rel in response.data.links) {
          var relation = response.data.links[rel].rel;
          if (relation === REL_LOGIN_OAUTH2) {
            $scope.loginHref = response.data.links[rel].href;
            console.log("Setting login href to " + JSON.stringify($scope.loginHref));
          }
          if (relation === REL_ADMIN_NEW_USER) {
            $scope.newUserHref = response.data.links[rel].href;
            console.log("Setting new user href to " + JSON.stringify($scope.newUserHref));
          }
        }
      }, function errorCallback(response) {
        if (response.status === -1) {
          console.log("Looks like nikita is down at the moment : " + JSON.stringify(response));
          alert("Problemer med å koble meg opp mot nikita-kjernen. Ser ut som om nikita er nede. Prøv igjen senere eller" +
            " kontakt administrator.");
        } else {
          console.log("Unknown error when connecting to nikita. Message is : " + JSON.stringify(response));
          alert("Ukjent problem med å koble meg opp mot nikita-kjernen. Prøv igjen senere eller kontakt administrator.");
        }
      });

      /**
       * doLogin
       *
       * Perform the actual login and redirect the logged in user to the correct page
       *
       */

      $scope.doLogin = function () {
        console.log("Attempting to login using [" + $scope.loginHref + "]");


        //+ "?grant_type=password&client_id=nikita-client&username=admin&password=password"
        $http({
          url: $scope.loginHref,
          method: "POST",
          headers: {
            "Content-type": "application/x-www-form-urlencoded; charset=utf-8",
            'Authorization': 'Basic ' + btoa('nikita-client:secret')
          },
          data: $httpParamSerializer({
            grant_type: "password",
            username: $scope.emailAddress,
            password: $scope.password,
            client_id: oauthClientId
          })
        }).then(function (data) {
          SetUserToken("Bearer " + data.data.access_token);
          console.log("Logging in.token is " + JSON.stringify(data.data) + ". Redirecting to page for " +
            $scope.selectedLoginRole);
          if ($scope.selectedLoginRole === 'arkivar') {
            changeLocation($scope, recordsManagerPage, true);
          }
          else if ($scope.selectedLoginRole === 'saksbehandler') {
            changeLocation($scope, caseHandlerPage, true);
          }
        }, function (response) {
          alert(JSON.stringify(response));
        });
      };


      /**
       * createAccount
       *
       * Create an account for nikita
       *
       */

      $scope.createUser = function () {
        console.log("Attempting to create User using [" + $scope.newUserHref + "]");

        $http({
          url: $scope.newUserHref,
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
          alert(JSON.stringify(response));
        });
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
