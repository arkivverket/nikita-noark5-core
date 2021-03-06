/**
 * Created by tsodring on 6/7/17.
 */
let app = angular.module('nikita', ['ngFileUpload', 'angular.img'])
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
            const settings = {
                method: 'POST',
                headers: {
                    Accept: 'application/json',
                    'Content-Type': 'application/json',
                }
            };
            await fetch(url + "?token=" + token, settings).then(function (response) {
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

            this.getRecords = async function (url, token) {
                let response = await fetch(url, {
                    headers: {
                        'Authorization': token
                    }
                });
                let records = await response.json();

                for (let i = 0; i < records.results.length; i++) {
                    if (records.results[i]._links[REL_DOCUMENT_DESCRIPTION].href !== undefined) {
                        let responsePromiseDD = await fetch(records.results[i]._links[REL_DOCUMENT_DESCRIPTION].href, {
                            headers: {
                                'Authorization': token
                            }
                        });
                        let docDesc = await responsePromiseDD.json();
                        if (docDesc.count > 0) {
                            for (let j = 0; j < docDesc.results.length; j++) {
                                if (docDesc.results[j]._links[REL_DOCUMENT_OBJECT].href !== undefined) {
                                    let responsePromiseDO = await fetch(docDesc.results[j]._links[REL_DOCUMENT_OBJECT].href, {
                                        headers: {
                                            'Authorization': token
                                        }
                                    });
                                    let docObj = await responsePromiseDO.json();
                                    records.results[i]['dokumentobjekt'] = {};
                                    records.results[i]['dokumentobjekt'] = docObj.results[0];
                                    records.results[i]['file'] = docObj.results[0]._links[REL_GET_FILE].href;
                                }
                            }
                        }
                    }
                }
                return records;
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
                response = await fetch(fondsStructure._links[REL_SERIES].href, {
                    headers: {
                        'Authorization': token
                    }
                });
                return await response.json();
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

            this.getCaseFileList2 = async function (token) {

                url = baseUrl + "api/sakarkiv/saksmappe?" +
                    encodeURIComponent("$filter") + "=" +
                    encodeURIComponent("saksstatus/kode eq 'F'");
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
             * Using the baseUrl (manually set in config.js), get the root of arkivstruktur.
             * First you use the url corresponding to the root of the application, then
             */
            this.getFileList = async function (token) {

                url = baseUrl + "api/arkivstruktur/arkivdel?" +
                    encodeURIComponent("$filter") + "=" +
                    encodeURIComponent("tittel eq 'Postmottak'");
                let response = await fetch(url, {
                    headers: {
                        'Authorization': token
                    }
                });

                let result = await response.json();
                console.log("curl -v " + url + " -H \"Authorization: " + token + "\"");
                console.log(result);
                url2 = result.results[0]._links[REL_FILE].href;
                console.log(url2);
                let response2 = await fetch(url2, {
                    headers: {
                        'Authorization': token
                    }
                });
                let result2 = await response2.json();
                if (Object.keys(result).length === 0) {
                    return [];
                }
                return result2.results;
            };

            /*
                let newRecordURL = fileResponse.results[0]._links[REL_NEW_RECORD].href;
                let recordResponse = await fetch(newRecordURL, {
                    method: 'POST',
                    headers: {
                        "Content-type": "application/vnd.noark5+json",
                        'Authorization': token,
                    },
                    body: JSON.stringify( {
                        "tittel":"E-post tittel"
                    })
                });
                console.log(recordResponse);
                let newDocDecURL = recordResponse.results[0]._links[REL_NEW_DOCUMENT_DESCRIPTION].href;
                let docDecResponse= await fetch(newDocDecURL, {
                    method: 'POST',
                    headers: {
                        "Content-type": "application/vnd.noark5+json",
                        'Authorization': token,
                    },
                    body: JSON.stringify({
                        "tittel": "E-post tittel",
                        "tilknyttetRegistreringSom": { "kode": "H", "kodenavn": "Hoveddokument" },
                        "dokumentstatus": { "kode": "F", "kodenavn": "Dokumentet er ferdigstilt" },
                        "dokumenttype": { "kode": "B", "kodenavn": "Brev" }
                    })
                });
                console.log(docDecResponse);
                let newDocObjURL = docDecResponse.results[0]._links[REL_NEW_DOCUMENT_OBJECT].href;
                let docObjResponse= await fetch(newDocObjURL, {
                    method: 'POST',
                    headers: {
                        "Content-type": "application/vnd.noark5+json",
                        'Authorization': token,
                    },
                    body: JSON.stringify( {
                        "variantformat": { "kode": "A", "kodenavn": "Arkivformat" },
                        "format": { "kode": "fmt/95" },
                        "filnavn" : "Brev fra jomar"
                    })
                });
                console.log(docObjResponse);
            }
            */

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

        }
    )
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
    }).directive('imageModalDir', function () {
        return {
            restrict: 'A',
            link: function (scope, element) {
                scope.dismissImageModal = function () {
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
