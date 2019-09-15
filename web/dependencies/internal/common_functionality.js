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
                if (typeof response !== 'undefined') {
                    let data = await response.json();
                    response = await fetch(data._links[REL_OIDC].href);
                    data = await response.json();
                    console.log("OIDC Data " + JSON.stringify(data));
                    return data;
                }
            },
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
                    if (typeof response !== 'undefined') {
                        let data = await response.json();
                        return data;
                    }
                }
        },
        /**
         * Do the actual login to nikita
         */
        this.doLogout = async function (url, token) {

            console.log("Attempting logout on [" + url + "]. using token [" + token + "]");
            let response = await fetch(url + queryString, {
                headers: {
                    'Authorization': token
                }
            });
            if (typeof response !== 'undefined') {
                let data = await response.json();
                return data;
            }
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

        /**
         * Using the given URL (rel=https://rel.arkivverket.no/noark5/v5/api/arkivstruktur/arkiv/"),
         * get the list of fonds
         */
        this.getFondsList = async function (urlFonds, token) {
            let response = await fetch(urlFonds, {
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
