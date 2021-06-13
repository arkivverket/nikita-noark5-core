/**
 * Temporary file to explore the use of nikita to provide management of a
 * picture archive.
 *
 */
var pictureController = app.controller('PictureController',
    ['$scope', '$http', 'loginService', 'nikitaService',
        function ($scope, $http, loginService, nikitaService) {

            $scope.baseUrl = "https://nikita.oslomet.no/noark5v5/api/arkivstruktur/";

            $scope.selectedArkivbildareCode = "z";
            $scope.selectedArkivbildareValue = "Alle organisasjoner";
            $scope.selectedArkivbildare = "Alle organisasjoner";

            $scope.arkivbildareList = [
                {id: 'z', value: 'Alle organisasjoner'}];


            $scope.arkivbildareInput = 'Alle organisasjoner';
            $scope.anmarkningarInput = '';
            $scope.motivInput = '';

            // Grab a copy of the authentication token
            $scope.token = GetUserToken();
            var loggedInUser = GetUsername();
            if (loggedInUser !== null && loggedInUser !== undefined
                && loggedInUser !== "") {
                $scope.loggedIn = true;
                $scope.emailAddress = loggedInUser;
            }

            (async () => {
                try {
                    let records = await nikitaService.getRecords($scope.baseUrl + 'registrering/', $scope.token);
                    $scope.records = records.results;
                    $scope.resultListCount = records.count;
                    $scope.$apply($scope.records);
                } catch (error) {
                    console.log(error.message);
                }
            })();

            $http.defaults.headers.common['Authorization'] = $scope.token;

            // Hide the image modal on request
            $scope.doDismissShowImage = function () {
                $scope.dismissImageModal();
            };

            <!--
            $scope.negativeExists = {
                Active: true,
                statusText: function () {
                    return $scope.selectedRecord.virksomhetsspesifikkeMetadata['tab:negativ'] ? 'Ja' : 'Nei';
                }
            };
            -->
            $scope.showImageModal = function () {

            };

            $scope.imageSelected = function (record) {
                $scope.selectedRecord = record;
            };

            $scope.doPictureSearch = function () {

                if ($scope.baseUrl === "") {
                    console.log("$scope.baseUrl is empty, don't know what to do!");
                    return;
                }

                let arkivbildareQuery = '';
                let anmarkningarQuery = '';
                let motivQuery = '';

                if ($scope.selectedArkivbildare !== 'Alle organisasjoner') {
                    arkivbildareQuery = encodeURIComponent("contains(tab:arkivbildare, '" +
                        $scope.selectedArkivbildare + "')").replace(/[!'()*]/g, function (c) {
                        return '%' + c.charCodeAt(0).toString(16);
                    });
                }

                if ($scope.anmarkningarInput.length > 0) {
                    if ($scope.selectedArkivbildare !== 'Alle organisasjoner') {
                        anmarkningarQuery = "+and+";
                    }

                    let query = encodeURIComponent("contains(tab:anmarkningar, '" +
                        $scope.anmarkningarInput + "')").replace(/[!'()*]/g, function (c) {
                        return '%' + c.charCodeAt(0).toString(16);
                    });
                    anmarkningarQuery += query;

                }

                if ($scope.motivInput.length > 0) {
                    if ($scope.selectedArkivbildare !== 'Alle organisasjoner' ||
                        $scope.anmarkningarInput.length > 0) {
                        motivQuery = "+and+";
                    }
                    let query = encodeURIComponent("contains(tab:motiv, '" +
                        $scope.motivInput + "')").replace(/[!'()*]/g, function (c) {
                        return '%' + c.charCodeAt(0).toString(16);
                    });
                    ;
                    motivQuery += query;
                }
                let finalQuery = '?%24filter%3D' + arkivbildareQuery + anmarkningarQuery + motivQuery;
                var httpRequest = $scope.baseUrl + "registrering" + finalQuery;
                let token = GetUserToken();
                $http({
                    method: 'GET',
                    url: httpRequest,
                    headers: {'Authorization': token}
                }).then(function successCallback(response) {
                    $scope.records = [];
                    $scope.records = response.data.results;
                    $scope.resultListCount = response.data.count;
                    for (let i = 0; i < $scope.records.length; i++) {
                        if ($scope.records[i]._links[REL_DOCUMENT_DESCRIPTION].href !== undefined) {
                            $http({
                                method: 'GET',
                                url: $scope.records[i]._links[REL_DOCUMENT_DESCRIPTION].href,
                                headers: {'Authorization': token}
                            }).then(function successCallback(response) {
                                let dokbesk = response.data.results;
                                if (dokbesk !== undefined) {
                                    for (let j = 0; j < dokbesk.length; j++) {
                                        if (dokbesk[j]._links[REL_DOCUMENT_OBJECT].href !== undefined) {
                                            $http({
                                                method: 'GET',
                                                url: dokbesk[j]._links[REL_DOCUMENT_OBJECT].href,
                                                headers: {'Authorization': token}
                                            }).then(function successCallback(response) {
                                                let dokobj = response.data;
                                                $scope.records[i]['dokumentobjekt'] = {};
                                                $scope.records[i]['dokumentobjekt'] = dokobj.results[0];
                                                $scope.records[i]['file'] = dokobj.results[0]._links[REL_GET_FILE].href;
                                            }, function (response) {
                                                alert(JSON.stringify(response));
                                            });
                                        }
                                    }
                                }
                            }, function (response) {
                                alert(JSON.stringify(response));
                            });
                        }
                    }
                }, function (response) {
                    alert(JSON.stringify(response));
                });
            }
        }
    ])
;
