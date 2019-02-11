var app = angular.module('nikita-browse', []);

/**
 *
 */
app.directive('loginModalDir', function () {
  return {
    restrict: 'A',
    link: function (scope, element) {
      scope.dismissLoginModal = function () {
        element.modal('hide');
      };
    }
  };
});

var browseController = app.controller('BrowseController',
  ['$scope', '$http', '$httpParamSerializer',
    function ($scope, $http, $httpParamSerializer) {

      // Grab a copy of the authentication token
      $scope.token = GetUserToken();

      // connect to nikita, do a GET on application root and make a note of the
      // login REL/HREF. This also serves a a check that nikita is actually running
      // before a user tries to login.
      $http({
        method: 'GET',
        url: baseUrl,
        headers: {'Authorization': $scope.token}
      }).then(function successCallback(response) {

        console.log("Application Root data is: " + JSON.stringify(response.data));
        $scope.current = response.data;

        for (var rel in response.data._links) {
          var relation = response.data._links[rel].rel;
          //console.log ("REL [" + relation + "], " + REL_CHECK_TOKEN)
          if (relation === REL_LOGIN_OAUTH2) {
            $scope.loginHref = response.data._links[rel].href;
            console.log("Setting login href to " + JSON.stringify($scope.loginHref));
          } else if (relation === REL_ADMIN_NEW_USER) {
            $scope.newUserHref = response.data._links[rel].href;
            console.log("Setting new user href to " + JSON.stringify($scope.newUserHref));
          }
          else if (relation === REL_LOGOUT_OAUTH2) {
            $scope.hrefLogout = response.data._links[rel].href;
            console.log("Setting logout href to " + JSON.stringify($scope.hrefLogout));
          }
          else if (relation === REL_CHECK_TOKEN) {
            var token = $scope.token;
            if ($scope.token.startsWith("Bearer ")) {
              token = $scope.token.substr(7);
            }
            console.log("token is [" + token + "]");
            $scope.hrefCheckToken = response.data._links[rel].href;
            console.log("check-token address is [" + $scope.hrefCheckToken + "?token=" + token + "]");
            console.log("Setting check-token href to " + JSON.stringify($scope.hrefCheckToken));
          }
        }
      }, function errorCallback(response) {
        if (response.status === -1) {
          console.log("Looks like nikita is down at the moment : " + JSON.stringify(response));
          alert("Problemer med å koble meg opp mot nikita-kjernen. Ser ut som om nikita er nede. Prøv igjen senere eller" +
            " kontakt administrator.");
        } else {
          console.log("You are not logged into nikita: " + JSON.stringify(response));
          $http({
            method: 'GET',
            url: baseUrl,
          }).then(function successCallback(response) {

            console.log("Application Root data is: " + JSON.stringify(response.data));
            $scope.current = response.data;

            for (var rel in response.data._links) {
              var relation = response.data._links[rel].rel;
              if (relation === REL_LOGIN_OAUTH2) {
                $scope.loginHref = response.data._links[rel].href;
                console.log("Setting login href to " + JSON.stringify($scope.loginHref));
              }
            }
          });
        }
      });

      /**
       *
       * filter away the links part
       *
       * Example derived from:
       * https://stackoverflow.com/questions/14788652/how-to-filter-key-value-with-ng-repeat-in-angularjs
       * @param items
       */
      $scope.filterLinks = function (items) {
        var result = {};
        angular.forEach(items, function (value, key) {
          if (key !== '_links') {
            result[key] = value;
          }
        });
        return result;
      };


      $scope.selectLink = function (href) {
        $http({
          method: 'GET',
          url: href,
          headers: {'Authorization': $scope.token}
        }).then(function successCallback(response) {
          $scope.current = response.data;
        });

      };

      $scope.showLoginWindow = function () {

      };

      /**
       * doLogin
       *
       * Perform the actual login and redirect the logged in user to the correct page
       *
       */

      $scope.doLogin = function () {
        console.log("Attempting to login using [" + $scope.loginHref + "]");
        console.log("Username [" + $scope.emailAddress + "]");

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
          console.log("Logging in.token is " + JSON.stringify(data.data));
          $scope.token = data.data;
          SetUsername($scope.emailAddress);
          location.reload();
        }, function (response) {
          alert(JSON.stringify(response));
        });
        $scope.dismissLoginModal();
      };

      $scope.doLogout = function () {
        console.log("logoutAddress is " + $scope.hrefLogout);
        var token = GetUserToken();
        console.log("token is " + JSON.stringify(token));
        if (typeof $scope.hrefLogout !== 'undefined') {
          $http({
            method: 'GET',
            url: $scope.hrefLogout,
            headers: {'Authorization': token}
          }).then(function successCallback(response) {
            $scope.projects = null;
            console.log(" GET urlForLogout[" + $scope.hrefLogout +
              "] returned " + JSON.stringify(response));
            $scope.current = "";
          }, function errorCallback(response) {
            alert("Kunne ikke logge ut. " +
              JSON.stringify(response));
            console.log(" GET urlForLogout[" + $scope.hrefLogout +
              "] returned " + JSON.stringify(response));
          });
        }
      };
    }
  ])
;

