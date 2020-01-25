/**
 * Created by tsodring on 6/7/17.
 */
let app = angular.module('nikita', ['ngFileUpload'])
    .service('loginService', function () {
        /**
         * Using the baseUrl (manually set in config.js), get the OIDC details of the server
         */
        this.getLoginHrefViaOIDC = async function () {
            let response = await fetch(baseUrl);
            if (typeof response !== undefined) {
                let data = await response.json();
                response = await fetch(data._links[REL_OIDC].href);
                data = await response.json();
                console.log("OIDC Data " + JSON.stringify(data));
                return data;
            }
        };
        /**
         * Do the actual login to nikita
         */
        this.doLogin = async function (url, userId, password) {
            let queryString =
                "?grant_type=password&" +
                "username=" + encodeURIComponent(userId) + "&" +
                "password=" + encodeURIComponent(password) + "&" +
                "client_id=" + encodeURIComponent(oauthClientId);

            console.log("Attempting to login using [" + url + "], " + "Username [" + userId + "]");
            let response = await fetch(url + queryString, {
                method: 'POST',
                headers: {
                    "Content-type": "application/x-www-form-urlencoded; charset=utf-8",
                    'Authorization': 'Basic ' + btoa('nikita-client:secret')
                }
            });
            if (typeof response !== undefined) {
                return await response.json();
            }
        };
        /**
         * Do the actual login to nikita
         */
        this.doLogout = async function (url, token) {
            console.log("Attempting logout on [" + url + "]. using token [" + token + "]");
            let response = await fetch(url + "?token=" + token, {
                headers: {
                    'Authorization': token
                }
            });
            if (typeof response !== undefined) {
                return await response.json();
            }
        };
        /**
         * Check tokens validity
         *
         * 401 is returned for unauthorized,
         * 200 if token is authorised along with a payload
         *
         * Assuming 200 means token is OK
         */
        this.doCheckToken = async function (url, token) {
            let tokenValid = false;
            console.log("Checking validity of token [" + token + "]. using [" + url + "]");
            await fetch(url + "?token=" + token).then(function (response) {
                if (response.status === 200) {
                    tokenValid = true;
                }
            }).catch(function (error) {
                console.log(error);
            });
            return tokenValid;
        };
    })
    .service('nikitaService', function () {
        /**
         * Using the baseUrl (manually set in config.js), get the root of arkivstruktur.
         * First you use the url corresponding to the root of the application, then
         */
        this.getFondsStructureRoot = async function (token) {
            let response = await fetch(baseUrl, {
                headers: {
                    'Authorization': token
                }
            });
            let data = await response.json();
            response = await fetch(data._links[REL_FONDS_STRUCTURE].href, {
                headers: {
                    'Authorization': token
                }
            });
            return await response.json();
        };

        this.getSeries = async function (url, token) {
            let response = await fetch(url, {
                headers: {
                    'Authorization': token
                }
            });
            let data = await response.json();
            return data;
        };


        /**
         * Using the baseUrl (manually set in config.js), get the root of arkivstruktur.
         * First you use the url corresponding to the root of the application, then
         */
        this.getSeriesList = async function (token) {
            let response = await fetch(baseUrl, {
                headers: {
                    'Authorization': token
                }
            });

            let fondsStructure = await this.getFondsStructureRoot(token);
            let seriesList = await this.getObject(
                fondsStructure._links[REL_SERIES].href, token);
            return await seriesList;
        };
        /**
         * Using the baseUrl (manually set in config.js), get the root of arkivstruktur.
         * First you use the url corresponding to the root of the application, then
         */
        this.getCaseFileList = async function (token, series) {

            url = series._links[REL_CASE_FILE].href;
            console.log("curl -v " + url + " -H \"Authorization: " + token + "\"");

            let response = await fetch(url, {
                headers: {
                    'Authorization': token
                }
            });
            let result = await response.json();
            if (Object.keys(result).length === 0) {
                return [];
            }
            return result.results;
        };

        /**
         * Using the given URL (rel=https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkiv/"),
         * get the list of fonds
         */
        this.getFondsList = async function (urlFondsList, token) {
            let response = await fetch(urlFondsList, {
                headers: {
                    'Authorization': token
                }
            });
            return await response.json();
        };

        this.getObject = async function (url, token) {
            let response = await fetch(url, {
                headers: {
                    'Authorization': token
                }
            });
            return await response.json();
        }
    })
    .directive('newUserModalDir', function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                scope.dismissNewUserModal = function () {
                    element.modal('hide');
                };
            }
        };
    })
    .directive('newCaseFileModalDir', function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                scope.dismissNewCaseFileModal = function () {
                    element.modal('hide');
                };
            }
        };
    })

    .directive('newDocumentDescriptionModalDir', function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                scope.dismissNewDocumentDescriptionModal = function () {
                    element.modal('hide');
                };
            }
        };
    })
    .directive('newDocumentObjectModalDir', function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                scope.dismissNewDocumentObjectModal = function () {
                    element.modal('hide');
                };
            }
        };
    })
    .directive('newCorrespondencePartModalDir', function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                scope.dismissNewCorrespondencePartModal = function () {
                    element.modal('hide');
                };
            }
        };
    })
    .directive('newRegistryEntryModalDir', function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                scope.dismissNewRegistryEntryModal = function () {
                    element.modal('hide');
                };
            }
        };
    })
    .directive('newFondsModalDir', function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                scope.dismissNewFondsModal = function () {
                    element.modal('hide');
                };
            }
        };
    }).directive('newFondsCreatorModalDir', function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                scope.dismissNewFondsCreatorModal = function () {
                    element.modal('hide');
                };
            }
        };
    }).directive('newSeriesModalDir', function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                scope.dismissNewSeriesModal = function () {
                    element.modal('hide');
                };
            }
        };
    });
angular.module('footer-module', [])
    .directive('footer', [function () {
        return {
            restrict: 'A',
            templateUrl: 'footer.html',
            scope: true,
            transclude: false
        };
    }]);
