var app = angular.module('nikita-search', []);

/**
 *
 */
app.directive('loginModal', function () {
  return {
    restrict: 'A',
    link: function (scope, element) {
      scope.dismissLoginModal = function () {
        element.modal('hide');
      };
    }
  };
});

var searchController = app.controller('SearchController',
  ['$scope', '$http', '$httpParamSerializer',
    function ($scope, $http, $httpParamSerializer) {

      $scope.odataQuery = "mappe?$filter=contains(tittel, 'Oslo') $top=2$skip=4$orderby=tittel desc";

      $scope.baseUrlForOdataSearch = GetBaseURLForODataSearch();

      // Grab a copy of the authentication token
      $scope.token = GetUserToken();
      var loggedInUser = GetUsername();
      if (loggedInUser !== null && loggedInUser !== undefined
        && loggedInUser !== "") {
        $scope.loggedIn = true;
        $scope.emailAddress = loggedInUser;
      }

      // before a user tries to login.
      $http({
        method: 'GET',
        url: baseUrl
      }).then(function successCallback(response) {
        for (var rel in response.data._links) {
          var relation = response.data._links[rel].rel;
          if (relation === REL_LOGIN_OAUTH2) {
            $scope.loginHref = response.data._links[rel].href;
            console.log("Setting login href to " + JSON.stringify($scope.loginHref));
          }
        }
      });

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

        var encodedQuery = encodeURIComponent($scope.odataQuery);
        // The question mark cannot be encoded because the server can't distinguish the entity string

        var queryPart = encodedQuery.replace("%3F", "?");
        queryPart = queryPart.replace(/'/g, "%27");
        var httpRequest = $scope.baseUrlForOdataSearch + "/" + queryPart;
        httpRequest = httpRequest.replace("api", "odata");
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

      /**
       * doLogin
       *
       * Perform the actual login and redirect the logged in user to the correct page
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
          $scope.token = "Bearer " + data.data.access_token;
          console.log("Actual token is [" + $scope.token + "]");
          SetUsername($scope.emailAddress);
          $scope.loggedIn = true;
          $scope.baseUrlForOdataSearch = "";
          $http({
            method: 'GET',
            url: baseUrl,
            headers: {'Authorization': $scope.token}
          }).then(function successCallback(response) {
            for (var rel in response.data._links) {
              var relation = response.data._links[rel].rel;
              //console.log ("REL [" + relation + "], " + REL_CHECK_TOKEN)
              if (relation === REL_FONDS_STRUCTURE) {
                $scope.baseUrlForOdataSearch = response.data._links[rel].href;
                SetBaseURLForODataSearch($scope.baseUrlForOdataSearch);
              }
            }
          });
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
            $scope.emailAddress = "";
            SetUsername("");
            $scope.loggedIn = false;
            $http({
              method: 'GET',
              url: baseUrl,
            }).then(function successCallback(response) {

              console.log("Application Root data is: " + JSON.stringify(response.data));

              if (angular.isArray(response.data)) {
                $scope.results = response.data;
              } else {
                $scope.results = [];
                $scope.results.push(response.data);
              }

              $scope.parentHrefStack = [];
              $scope.parentHrefStack.push(baseUrl);
              $scope.parentHref = baseUrl;
              $scope.selectedHref = baseUrl;
              console.log("$scope.parentHref:" + $scope.parentHref);

              for (var rel in response.data._links) {
                var relation = response.data._links[rel].rel;
                if (relation === REL_LOGIN_OAUTH2) {
                  $scope.loginHref = response.data._links[rel].href;
                  console.log("Setting login href to " + JSON.stringify($scope.loginHref));
                }
              }
            });


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

