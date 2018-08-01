var app = angular.module('nikita', []);


/**
 * LoginController
 *
 * This file provides the following functionaliy:
 *
 *  1. When the page loads the nikita core is called to retrieve the login URL
 *    - In this case,we look for a OAUTH2 REL (http://nikita.arkivlab.no/noark5/v4/login/rfc6749)
 *  2. When a successful login occurs, the user is pushed to the correct html page
 *   - This process will also issue a GET to the root of the application and
 *     retrieve and GET the content behind http://rel.kxml.no/noark5/v4/api/arkivstruktur/.
 *     This object is then stored in localstorage so the application knows how to retrieve
 *     fonds objects and create them.
 *
 */

var login = app.controller('LoginController', ['$scope', '$http', function ($scope, $http) {

  // This sets 'arkivar' to be the default choice on the webpage
  $scope.selectedLoginRole = "arkivar";

  $http({
    method: 'GET',
    url: baseUrl,
  }).then(function successCallback(response) {

    console.log("Application Root data is: " + JSON.stringify(response.data));

    for (var rel in response.data.links) {
      var relation = response.data.links[rel].rel;
      if (relation == REL_LOGIN_OAUTH2) {
        $scope.loginHref = response.data.links[rel].href + "?grant_type=password&client_id=nikita-client&username=admin&password=password";
        console.log("Setting login href to " + JSON.stringify($scope.loginHref));
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

  $scope.loginOptions = loginOptions;

  $scope.doLogin = function () {
    console.log("Attempting to login using [" + $scope.loginHref + "]");

    $http({
      url: $scope.loginHref,
      method: "POST",
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + btoa('nikita-client:secret')
      }
    }).then(function (data, status, headers, config) {
      SetUserToken("Bearer " + data.data.access_token);
      console.log("Logging in.token is " + JSON.stringify(data));
      console.log("Logging in.token is " + JSON.stringify(data.data));
      console.log("Logging in. redirecting to page for " + $scope.selectedLoginOption);
      if ($scope.selectedLoginRole === 'arkivar') {
        changeLocation($scope, fondsListPageName, true);
      }
      else if ($scope.selectedLoginRole === 'saksbehandler') {
        //caseHandlerDashboardPageName
        changeLocation($scope, "./saksbehandler-dashboard.html", true);
      }
    }, function (data, status, headers, config) {
      alert(/*JSON.stringify(data) +*/ JSON.stringify(data));
    });
  };
}]);
