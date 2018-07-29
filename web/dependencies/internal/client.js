var app = angular.module('nikita', []);


var changeLocation = function ($scope, url, forceReload) {
    $scope = $scope || angular.element(document).scope();
    console.log("URL" + url);
    if (forceReload || $scope.$$phase) {
        window.location = url;
    }
    else {
        //only use this if you want to replace the history stack
        //$location.path(url).replace();

        //this this if you want to change the URL and add it to the history stack
        $location.path(url);
        $scope.$apply();
    }
};

var updateIndexView = function (url, $scope, $http) {
    $scope.current = url;
    if (url.lastIndexOf("/") == (url.length - 1)) {
        parent = "..";
    } else {
        parent = ".";
    }
    $scope.parent = resolveUrl(url, parent);
    token = GetUserToken();
    $http({
        method: 'GET',
        url: url,
        headers: {'Authorization': token},
    }).then(function successCallback(response) {
        $scope.allow = response.headers('Allow');
        $scope.links = response.data.links;
        $scope.data = response.data;
        $scope.results = response.data.results;
        delete $scope.data.links;
        delete $scope.data.results;
    }, function errorCallback(response) {
        // TODO: what should we do when it fails?
        $scope.allow = '[unknown - GET failed]';
        $scope.links = '';
        $scope.data = '';
        $scope.results = '';
    });
};

var controller = app.controller('MainController', ['$scope', '$http', function ($scope, $http) {
    token = GetUserToken();
    console.log("token=" + $scope.token);
    $scope.app_version = "xyz";
    $http({
        method: 'GET',
        url: '/version',
        headers: {'Authorization': token},
    }).then(function successCallback(response) {
        $scope.app_version = response.data;
    }, function errorCallback(response) {
        // TODO: what should we do when it fails?
    });

    updateIndexView(base_url, $scope, $http);

    $scope.hrefSelected = function (href) {
        console.log('href link clicked ' + href);
        href = href.split("{")[0];
        updateIndexView(href, $scope, $http);
    }
}]);

var login = app.controller('LoginController', ['$scope', '$http', function ($scope, $http) {

    $scope.selectedLoginOption = "arkivar";

    console.log("LoginController");
    console.log("LoginOptions " + JSON.stringify(loginOptions));
    $scope.loginOptions = loginOptions;

    $scope.send_form = function () {
        console.log($scope.password);
        console.log($scope.username);
        selectedLoginOption = document.getElementById("login_type").value;

      login_url_to_use = login_url + '?grant_type=password&client_id=nikita-client&username=admin&password=password';
      console.log("Attempting to login using [" + login_url_to_use + "]");

        $http({
            url: login_url_to_use,
            method: "POST",
            headers: {'Content-Type': 'application/json',
                      'Authorization': 'Basic ' + btoa('nikita-client:secret')
            }
        }).then(function (data, status, headers, config) {
            SetUserToken("Bearer " + data.data.access_token);
            console.log("Logging in.token is " + JSON.stringify(data));
            console.log("Logging in.token is " + JSON.stringify(data.data));
            console.log("Logging in. redirecting to page for " + $scope.selectedLoginOption);
            if (selectedLoginOption == 'arkivar') {
                changeLocation($scope, fondsListPageName, true);
            }
            else if (selectedLoginOption == 'saksbehandler') {
                //caseHandlerDashboardPageName
                changeLocation($scope, "./saksbehandler-dashboard.html", true);
            }
        }, function (data, status, headers, config) {
            alert(/*JSON.stringify(data) +*/ JSON.stringify(data));
        });
    };
}]);

var postliste = app.controller('PostlisteController', ['$scope', '$http', function ($scope, $http) {
    // FIXME find href for rel
    // 'http://rel.kxml.no/noark5/v4/api/arkivstruktur/arkiv/'
    // dynamically
    url = base_url + "/hateoas-api/arkivstruktur/arkiv";
    token = GetUserToken();
    $http({
        method: 'GET',
        url: url,
        headers: {'Authorization': token},
    }).then(function successCallback(response) {
        $scope.status = 'success';
        $scope.fonds = response.data.results;
    }, function errorCallback(response) {
        $scope.status = 'failure';
        $scope.fonds = '';
    });
    $scope.series = '';
    $scope.casefiles = '';

    $scope.fondsUpdate = function (fonds) {
        console.log('fonds selected ' + fonds.tittel);
        $scope.casefiles = '';
        for (rel in fonds.links) {
            href = fonds.links[rel].href;
            relation = fonds.links[rel].rel;
            if (relation == 'http://rel.kxml.no/noark5/v4/api/arkivstruktur/arkivdel/') {
                console.log("fetching " + href);
                $http({
                    method: 'GET',
                    url: href,
                    headers: {'Authorization': GetUserToken()},
                }).then(function successCallback(response) {
                    $scope.series = response.data.results;
                }, function errorCallback(response) {
                    $scope.series = '';
                });
            }
        }
    };
    $scope.seriesUpdate = function (series) {
        console.log('series selected ' + series.tittel);
        if (!series) {
            return
        }
        for (rel in series.links) {
            href = series.links[rel].href;
            relation = series.links[rel].rel;
            if (relation == 'http://rel.kxml.no/noark5/v4/api/sakarkiv/saksmappe/') {
                console.log("fetching " + href);
                $http({
                    method: 'GET',
                    url: href,
                    headers: {'Authorization': GetUserToken()},
                }).then(function successCallback(response) {
                    $scope.casefiles = response.data.results;
                }, function errorCallback(response) {
                    $scope.casefiles = '';
                });
            }
        }
    };
    $scope.fileSelected = function (file) {
        console.log('file selected ' + file.tittel);
        if (file.records) {
            file.records = '';
            return;
        }
        for (rel in file.links) {
            relation = file.links[rel].rel;
            if (relation == 'http://rel.kxml.no/noark5/v4/api/sakarkiv/journalpost/') {
                href = file.links[rel].href;
                console.log("fetching " + href);
                $http({
                    method: 'GET',
                    url: href,
                    headers: {'Authorization': GetUserToken()},
                }).then(function successCallback(response) {
                    response.data.results.forEach(function (record) {
                        console.log("record " + record);
                        for (rel in record.links) {
                            relation = record.links[rel].rel;
                            console.log("found " + relation);
                            if (relation == 'http://rel.kxml.no/noark5/v4/api/arkivstruktur/dokumentbeskrivelse/') {
                                href = record.links[rel].href;
                                console.log("fetching " + href);
                                $http({
                                    method: 'GET',
                                    url: href,
                                    headers: {'Authorization': GetUserToken()},
                                }).then(function successCallback(docdesc) {
                                    record.dokumentbeskrivelse =
                                        docdesc.data.results[0];
                                }, function errorCallback(docdesc) {
                                    record.dokumentbeskrivelse = '';
                                });
                            }
                        }
                    });
                    file.records = response.data.results;
                }, function errorCallback(response) {
                    file.records = '';
                });
            }
        }
    }
}]);
