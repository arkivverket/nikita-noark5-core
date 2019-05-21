/**
 * Created by tsodring on 5/2/17.
 */


// Set the base url for application
var baseUrl = 'http://localhost:8092/noark5v4/';
console.log("Setting nikita app_url: " + baseUrl);
var recordsManagerPage = 'arkivar.html';
var caseHandlerPage = 'saksbehandler.html';
var oauthClientId = "nikita-client";

/*
 These should probably be implemented as a 2D-array where we can easily pull out the required value
 */

var REL_NEW_REGISTRY_ENTRY = 'https://rel.arkivverket.no/noark5/v4/api/sakarkiv/ny-journalpost/';
var REL_REGISTRY_ENTRY = 'https://rel.arkivverket.no/noark5/v4/api/sakarkiv/journalpost/';
var REL_NEW_DOCUMENT_DESCRIPTION = 'https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/ny-dokumentbeskrivelse/';
var REL_DOCUMENT_DESCRIPTION = 'https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/dokumentbeskrivelse/';
var REL_DOCUMENT_OBJECT = 'https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/dokumentobjekt/';
var REL_NEW_DOCUMENT_OBJECT = 'https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/ny-dokumentobjekt/';
var REL_NEW_CORRESPONDENCE_PART_PERSON = 'https://rel.arkivverket.no/noark5/v4/api/sakarkiv/ny-korrespondansepartperson/';
var REL_CORRESPONDENCE_PART_PERSON = 'https://rel.arkivverket.no/noark5/v4/api/sakarkiv/korrespondansepartperson/';
var REL_CASE_FILE = 'https://rel.arkivverket.no/noark5/v4/api/sakarkiv/saksmappe/';
var REL_NEW_CASE_FILE = 'https://rel.arkivverket.no/noark5/v4/api/sakarkiv/ny-saksmappe/';
var REL_DOCUMENT_FILE = 'https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/fil/';
var REL_SERIES = "https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/arkivdel/";
var REL_FONDS_STRUCTURE = 'https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/';
var REL_FONDS_STRUCTURE_FONDS = 'https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/arkiv/';
var REL_FONDS_STRUCTURE_NEW_FONDS = 'https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/ny-arkiv/';
var REL_FONDS_CREATOR = "https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/arkivskaper/";
var REL_SERIES = "https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/arkivdel/";
var REL_NEW_SERIES = "https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/ny-arkivdel/";
var REL_NEW_FONDS_CREATOR = "https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/ny-arkivskaper/";
var REL_FONDS_CREATOR = "https://rel.arkivverket.no/noark5/v4/api/arkivstruktur/arkivskaper/";
var REL_SELF = 'self';
var REL_LOGIN_OAUTH2 = 'https://nikita.arkivlab.no/noark5/v4/login/rfc6749/';
var REL_LOGOUT_OAUTH2 = 'https://nikita.arkivlab.no/noark5/v4/logout/rfc6749/';
var REL_CHECK_TOKEN = 'https://nikita.arkivlab.no/noark5/v4/oauth/check_token/rfc6749/';
var REL_ADMIN_NEW_USER = 'https://rel.arkivverket.no/noark5/v4/api/administrasjon/ny-bruker/';



var MSG_NIKITA_DOWN = "Problemer med å koble meg opp mot nikita-kjernen. Ser ut som om nikita er nede. Prøv igjen senere eller kontakt administrator.";
var MSG_NIKITA_DOWN_LOG = "Looks like nikita is down at the moment : ";
var MSG_NIKITA_UNKNOWN_ERROR = "Ukjent problem med å koble meg opp mot nikita-kjernen. Prøv igjen senere eller kontakt administrator.";
var MSG_NIKITA_UNKNOWN_ERROR_LOG = "Unknown error when connecting to nikita. Message is : " ;

// These will be picked up from the database
var mimeTypeList = [
    {id: 'rfc822', value: 'message/rfc822'},
    {id: 'pdf', value: 'application/pdf'},
    {id: 'odt', value: 'application/vnd.oasis.opendocument.text'},
    {id: 'ods', value: 'application/vnd.oasis.opendocument.spreadsheet'},
    {id: 'odp', value: 'application/vnd.oasis.opendocument.presentation'},
    {id: 'tiff', value: 'image/tiff'},
    {id: 'jpeg', value: 'image/jpeg'},
];

var variantFormatList = [
    {id: 'P', value: 'Produksjonsformat'},
    {id: 'A', value: 'Arkivformat'},
    {id: 'O', value: 'Dokument hvor deler av innholdet er skjermet'}];

var documentTypeList = [
    {id: 'B', value: 'Brev'},
    {id: 'F', value: 'Faktura'},
    {id: 'O', value: 'Ordrebekreftelse'}];

var documentMediumList = [
    {id: 'F', value: 'Fysisk arkiv'},
    {id: 'E', value: 'Elektronisk arkiv'},
    {id: 'B', value: 'Blandet fysisk og elektronisk arkiv'}];

// This is a fictitious list
var storageLocationList = [
  {id: 'S', value: 'Sentralarkivet'},
  {id: 'F', value: 'Fjernarkivet'}];

var registryEntryTypeList = [
    {id: 'I', value: 'Inngående dokument'},
    {id: 'U', value: 'Utgående dokument'},
    {id: 'N', value: 'Organinternt dokument for oppfølging'},
    {id: 'S', value: 'Organinternt dokument uten oppfølging'}];

var registryEntryStatusList = [
    {id: 'J', value: 'Journalført'},
    {id: 'F', value: 'Ferdigstilt fra saksbehandler'},
    {id: 'G', value: 'Godkjent av leder'},
    {id: 'E', value: 'Ekspedert'},
    {id: 'A', value: 'Arkivert'},
    {id: 'U', value: 'Utgår'},
    {id: 'M', value: 'Midlertidig registrering av'}];

var fondsStatusList = [
    {id: 'O', value: 'Opprettet'},
    {id: 'A', value: 'Avsluttet'}];

var caseFileStatusList = [
  {id: 'OS', value: 'Opprettet av saksbehandler'},
  {id: 'OA', value: 'Opprettet av arkivtjenesten'},
    {id: 'UB', value: 'Under behandling'},
    {id: 'U', value: 'Utgår'},
    {id: 'A', value: 'Avsluttet'}];

var seriesStatusList = [
    {id: 'O', value: 'Opprettet'},
    {id: 'A', value: 'Avsluttet'}];

var associatedWithRecordAsList = [
    {id: 'H', value: 'Hoveddokument'},
    {id: 'V', value: 'Vedlegg'}];

var correspondencePartTypeList = [
    {id: 'EA', value: 'Avsender'},
    {id: 'EM', value: 'Mottaker'},
    {id: 'EK', value: 'Kopimottaker'},
    {id: 'GM', value: 'Gruppemottaker'},
    {id: 'IA', value: 'Intern avsender'},
    {id: 'IM', value: 'Intern mottaker'},
    {id: 'IK', value: 'Intern kopimottaker'}
];

var documentStatusList = [
    {id: 'B', value: 'Dokumentet er under redigering'},
    {id: 'F', value: 'Dokumentet er ferdigstilt'}];

var formatList = [
  {id: 'odt', value: 'application/vnd.oasis.opendocument.text'},
  {id: 'docx', value: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'},
  {id: 'doc', value: 'application/msword'},
  {id: 'ods', value: 'application/vnd.oasis.opendocument.spreadsheet'},
  {id: 'xlsx', value: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'},
  {id: 'xls', value: 'application/vnd.ms-excel'},
  {id: 'odp', value: 'application/vnd.oasis.opendocument.presentation'},
  {id: 'ppt', value: 'application/vnd.ms-powerpoint'},
  {id: 'pptx', value: 'application/vnd.openxmlformats-officedocument.presentationml.presentation'},
  {id: 'png', value: 'image/png'},
  {id: 'gif', value: 'image/gif'},
  {id: 'txt', value: 'text/plain'},
  {id: 'pdf', value: 'application/pdf'}];

var emptyList = [{id: '', value: ''},
    {id: '', value: ''},
    {id: '', value: ''},
    {id: '', value: ''}];

var loginOptions = [
    {id: 'SA', value: "saksbehandler"},
    {id: 'AR', value: "arkivar"}];

var SetUserToken = function (t) {
    localStorage.setItem("token", t);
    console.log("Adding token " + t + " to local storage");
};

var GetUserToken = function () {
    return localStorage.getItem("token");
};

var GetBaseURLForODataSearch = function () {
  return localStorage.getItem("base-url-odata");
};

var SetBaseURLForODataSearch = function (t) {
  localStorage.setItem("base-url-odata", t);
  console.log("Adding base-url-odata " + t + " to local storage");
};

var SetUsername = function (t) {
  localStorage.setItem("username", t);
  console.log("Adding username " + t + " to local storage");
};

var GetUsername = function () {
  return localStorage.getItem("username");
};

var setApplicationRoot = function (t) {
    localStorage.setItem("applicationRoot", t);
    console.log("Adding applicationRoot " + t + " to local storage");
};

var getApplicationRoot = function (t) {
    return localStorage.getItem("applicationRoot");
};

var GetFileSystemID = function (t) {
    return localStorage.getItem("current_case_file");
};

var GetLinkToChosenFile = function () {
    console.log("getting linktochosenfile=" + localStorage.getItem("linkToChosenFile"));
    return localStorage.getItem("linkToChosenFile");
};

var SetChosenCaseFile = function (t) {
    localStorage.setItem("chosenCaseFile", JSON.stringify(t));
    console.log("Setting chosenCaseFile=" + JSON.stringify(t));
};

var GetChosenCaseFile = function () {
    var chosenCaseFile = localStorage.getItem("chosenCaseFile");
    console.log("Getting chosenCaseFile=" + chosenCaseFile);
    return localStorage.getItem("chosenCaseFile");
};

var SetChosenRegistryEntryObject = function (t) {
    localStorage.setItem("chosenRegistryEntry", JSON.stringify(t));
    console.log("Setting chosenRegistryEntry=" + JSON.stringify(t));
};

var GetChosenRegistryEntryObject = function () {
    var chosenRegistryEntry = localStorage.getItem("chosenRegistryEntry");
    console.log("Getting chosenRegistryEntry=" + chosenRegistryEntry);
    return localStorage.getItem("chosenRegistryEntry");
};

var SetLinkToChosenRecord = function (t) {
    localStorage.setItem("linkToChosenRecord", t);
    console.log("Setting linkToChosenRecord=" + t);
};

var SetCurrentRecordSystemId = function (recordSystemId) {
    localStorage.setItem("currentRecordSystemId", recordSystemId);
    console.log("Setting currentRecordSystemId=" + recordSystemId);
};

var GetSeriesSystemID = function () {
    console.log("Getting chosen currentSeriesSystemId=" + localStorage.getItem("currentSeriesSystemId"));
    return localStorage.getItem("currentSeriesSystemId");
};

var GetLinkToChosenRecord = function () {
    return localStorage.getItem("linkToChosenRecord");
};


// href of the link to use when creating a document description
var GetLinkToCreateDocumentDescription = function () {
    return localStorage.getItem("linkToCreateDocumentDescription");
};

// href of the link to use when creating a document object
var SetLinkToCreateDocumentDescription = function (t) {
    localStorage.setItem("linkToCreateDocumentDescription", t);
    console.log("Setting linkToCreateDocumentDescription=" + t);
};


// href of the link to use when creating a document description
var GetChosenDocumentDescription = function () {
    return localStorage.getItem("chosenDocumentDescription");
};

// href of the link to use when creating a document object
var SetChosenDocumentDescription = function (t) {
    localStorage.setItem("chosenDocumentDescription", JSON.stringify(t));
    console.log("Setting chosenDocumentDescription=" + t);
};

// href of the link to use when getting a document description
var GetLinkToDocumentDescription = function () {
    return localStorage.getItem("linkToDocumentDescription");
};

// href of the link to use when getting a document object
var SetLinkToDocumentDescription = function (t) {
    localStorage.setItem("linkToDocumentDescription", t);
    console.log("Setting linkToDocumentDescription=" + t);
};

var SetLinkToChosenSeries = function (t) {
    localStorage.setItem("linkToChosenSeries", t);
    console.log("Setting linkToChosenSeries=" + t);
};

var SetChosenSeries = function (t) {
    localStorage.setItem("chosenSeries", JSON.stringify(t));
    console.log("Setting chosenSeries=" + JSON.stringify(t));
};

var GetChosenSeries = function () {
    console.log("Getting chosenSeries=" + localStorage.getItem("chosenSeries"));
    return JSON.parse(localStorage.getItem("chosenSeries"));
};


var GetLinkToCurrentSeries = function () {
    console.log("Getting linkToChosenSeries=" +
        localStorage.getItem("linkToChosenSeries"));
    return localStorage.getItem("linkToChosenSeries");
};

var SetLinkToCurrentSeries = function (t) {
    console.log("Setting linkToChosenSeries=" + t);
    return localStorage.setItem("linkToChosenSeries", t);
};

var SetLinkToChosenFile = function (t) {
    localStorage.setItem("linkToChosenFile", t);
    console.log("Setting linkToChosenFile=" + t);
};

var GetLinkToSeriesAllFile = function () {
    return localStorage.getItem("linkToSeriesAllFile");
};

var SetLinkToDocumentFile = function (t) {
    localStorage.setItem("linkToUploadDocumentFile", t);
    console.log("Setting linkToUploadDocumentFile=" + t);
};

var GetLinkToDocumentFile = function () {
    return localStorage.getItem("linkToUploadDocumentFile");
};

// href of the link to use when creating a document object
var SetLinkToCreateDocumentObject = function (t) {
    localStorage.setItem("linkToChosenDocumentObject", t);
    console.log("Setting linkToChosenDocumentObject=" + t);
};

var SetLinkToDocumentObject = function (t) {
    localStorage.setItem("linkToDocumentObject", t);
    console.log("Setting linkToDocumentObject=" + t);
};

var GetLinkToDocumentObject = function (t) {
    return localStorage.setItem("linkToDocumentObject");
};

var GetLinkToCurrentCorrespondencePartPerson = function () {
    console.log("Getting linkToChosenCorrespondencePartPerson=" +
        localStorage.getItem("linkToChosenCorrespondencePartPerson"));
    return localStorage.getItem("linkToChosenCorrespondencePartPerson");
};

var SetLinkToCurrentCorrespondencePartPerson = function (t) {
    localStorage.setItem("linkToChosenCorrespondencePartPerson", t);
    console.log("Setting linkToChosenCorrespondencePartPerson=" + t);
};

var GetLinkToCreateCorrespondencePartPerson = function () {
    console.log("Getting linkToCreateCorrespondencePartPerson=" +
        localStorage.getItem("linkToCreateCorrespondencePartPerson"));
    return localStorage.getItem("linkToCreateCorrespondencePartPerson");
};

var SetLinkToCreateCorrespondencePartPerson = function (t) {
    localStorage.setItem("linkToCreateCorrespondencePartPerson", t);
    console.log("Setting linkToCreateCorrespondencePartPerson=" + t);
};

var ClearLinkToCorrespondencePartPerson = function (t) {
    console.log("Removing linkToChosenCorrespondencePartPerson=" +
        localStorage.getItem("linkToChosenCorrespondencePartPerson"));
    localStorage.removeItem("linkToChosenCorrespondencePartPerson")
};

var GetLinkToCorrespondencePartUnit = function () {
    console.log("Getting linkToChosenCorrespondencePartUnit=" +
        localStorage.getItem("linkToChosenCorrespondencePartUnit"));
    return localStorage.getItem("linkToChosenCorrespondencePartUnit");
};

var SetLinkToCorrespondencePartUnit = function (t) {
    localStorage.setItem("linkToChosenCorrespondencePartUnit", t);
    console.log("Setting linkToChosenCorrespondencePartUnit=" + t);
};

var SetCurrentRegistryEntry = function (t) {
    localStorage.setItem('currentRegistryEntry', JSON.stringify(t));
    console.log("Setting currentRegistryEntry=" + JSON.stringify(t));
    console.log("The currentRegistryEntry value is the following =" + localStorage.getItem('currentRegistryEntry'));
};

var GetCurrentRegistryEntry = function () {
    console.log("Getting currentRegistryEntry=" + localStorage.getItem('currentRegistryEntry'));
    return localStorage.getItem('currentRegistryEntry');
};

var SetLinkToChosenCaseFile = function (t) {
    localStorage.setItem("linkToCurrentCaseFile", t);
    console.log("Setting linkToCurrentCaseFile=" + t);
};

var GetLinkToChosenCaseFile = function () {
    console.log("Getting linkToCurrentCaseFile=" + localStorage.getItem("linkToCurrentCaseFile"));
    return localStorage.getItem("linkToCurrentCaseFile");
};

var SetLinkToCreateRegistryEntry = function (t) {
    localStorage.setItem("linkToCreateRegistryEntry", t);
    console.log("Setting linkToCreateRegistryEntry=" + t);
};

var GetLinkToCreateRegistryEntry = function () {
    console.log("Getting linkToCreateRegistryEntry=" + localStorage.getItem("linkToCreateRegistryEntry"));
    return localStorage.getItem("linkToCreateRegistryEntry");
};

var SetLinkToGetRegistryEntry = function (t) {
    localStorage.setItem("linkToGetRegistryEntry", t);
    console.log("Setting linkToGetRegistryEntry=" + t);
};

var GetLinkToGetRegistryEntry = function () {
    console.log("Getting linkToGetRegistryEntry=" + localStorage.getItem("linkToGetRegistryEntry"));
    return localStorage.getItem("linkToGetRegistryEntry");
};

var changeLocation = function ($scope, url, forceReload) {
    $scope = $scope || angular.element(document).scope();
    console.log("URL" + url);
    if (forceReload || $scope.$$phase) {
        window.location = url;
    }
    else {
        $location.path(url);
        $scope.$apply();
    }
};

