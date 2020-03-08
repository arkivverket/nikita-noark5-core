var app = angular.module('nikita', []);

var postliste = app.controller('PostlisteController', ['$scope', '$http', function ($scope, $http) {
  // FIXME find href for rel
  // 'https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkiv/'
  // dynamically
  url = base_url + "/api/arkivstruktur/arkiv";
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
      if (relation == 'https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkivdel/') {
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
      if (relation == 'https://rel.arkivverket.no/noark5/v5/api/sakarkiv/saksmappe/') {
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
      if (relation == 'https://rel.arkivverket.no/noark5/v5/api/sakarkiv/journalpost/') {
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
              if (relation == 'https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/dokumentbeskrivelse/') {
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
