#!/usr/bin/env bash


# This is a project internal script used as a basic test mechanism. It will be replaced by a proper testing framework
# later.

# Put in directory location of json files to populate core with
#$1 should be set to /PATH_TO_PROJECT/nikita-noark5-core/scripts/json-example-data
# or simple ./populate_core_with_data.sh ./json-example-data
curl_files_dir=$1;

# Pick up directory with JSON files
if [[ -n "$curl_files_dir" ]]; then
    length=${#curl_files_dir}
    length=$(expr $length - 1)
    if [ "${curl_files_dir:length}" != "/" ]; then
      echo "Adding missing trailing slash"
      curl_files_dir=$curl_files_dir"/"
    fi
    echo "running with json files in $curl_files_dir";
else
    echo "usage: $0 directory";
    exit;
fi

# login to the core using the JWT method
authToken=$(curl -s   -H 'Authorization: Basic bmlraXRhLWNsaWVudDpzZWNyZXQ=' --header Accept:application/json --header Content-Type:application/json -X POST  "http://localhost:8092/noark5v5/oauth/token?grant_type=password&client_id=nikita-client&username=admin@example.com&password=password" | jq '."access_token"' | sed 's/\"//g');
echo "Token is " $authToken;
# Note It seems to returning the word "null" if empty
if [ $authToken == "null" ]; then
    echo "Did not get a proper token back from the core."
    exit;
fi

echo "Token is " $authToken;


# Setup common curl options
contentTypeForPost+=(--header "Content-Type:application/vnd.noark5-v5+json");
curlOpts+=( -s --header "Accept:application/vnd.noark5-v5+json" --header "Authorization:Bearer $authToken");
curlPostOpts+=("${curlOpts[@]}" "${contentTypeForPost[@]}" -X POST );
curlPutOpts+=("${curlOpts[@]}" "${contentTypeForPost[@]}" -X PUT );

# Setup curl options for fonds
curloptsCreateFonds+=("${curlPostOpts[@]}");
curloptsCreateFonds+=( --data @"$curl_files_dir"fonds-data.json  'http://localhost:8092/noark5v5/api/arkivstruktur/ny-arkiv' );

# Create a fonds object and capture the systemId
systemIDCreatedFonds=$(curl "${curloptsCreateFonds[@]}" | jq '.systemID' | sed 's/\"//g');
printf "created Fonds 1             ($systemIDCreatedFonds) \n";
#echo  "${curloptsCreateFonds[@]}";

# Setup curl options for fondsCreator from existing fonds
curloptsCreateFondsCreator+=("${curlPostOpts[@]}");
curloptsCreateFondsCreator+=( --data @"$curl_files_dir"fonds-creator-data.json  'http://localhost:8092/noark5v5/api/arkivstruktur/arkiv/'$systemIDCreatedFonds'/ny-arkivskaper' );
systemIDCreatedFondsCreator=$(curl "${curloptsCreateFondsCreator[@]}" | jq '.systemID' | sed 's/\"//g');
printf "created FondsCreator 2(arkiv)($systemIDCreatedFondsCreator) \n";


# Setup curl options for series
curloptsCreateSeries+=("${curlPostOpts[@]}");
curloptsCreateSeries+=( --data @"$curl_files_dir"series-data.json  'http://localhost:8092/noark5v5/api/arkivstruktur/arkiv/'$systemIDCreatedFonds'/ny-arkivdel' )

# Create a series object and capture the systemId
systemIDCreatedSeries=$(curl "${curloptsCreateSeries[@]}" | jq '.systemID' | sed 's/\"//g');
printf "created  Series 1            ($systemIDCreatedSeries) \n";
#echo ${curloptsCreateSeries[@]};

# Setup curl options for file
curloptsCreateFile+=("${curlPostOpts[@]}");
curloptsCreateFile+=( --data @"$curl_files_dir"file-data.json  'http://localhost:8092/noark5v5/api/arkivstruktur/arkivdel/'$systemIDCreatedSeries'/ny-mappe/' )
systemIDCreatedFile=$(curl "${curloptsCreateFile[@]}" | jq '.systemID' | sed 's/\"//g');
printf "created   File 1              ($systemIDCreatedFile) \n";

# Setup curl options for Record
curloptsCreateRecord+=("${curlPostOpts[@]}");
curloptsCreateRecord+=( --data @"$curl_files_dir"record-data.json 'http://localhost:8092/noark5v5/api/arkivstruktur/mappe/'$systemIDCreatedFile'/ny-registrering' )
systemIDCreatedRecord=$(curl "${curloptsCreateRecord[@]}" | jq '.systemID' |  sed 's/\"//g');
printf "created   Record 1              ($systemIDCreatedRecord) \n";


# Setup curl options for documentDescription
curloptsCreateDocumentDescription+=("${curlPostOpts[@]}");
curloptsCreateDocumentDescription+=( --data @"$curl_files_dir"document-description-data.json  'http://localhost:8092/noark5v5/api/arkivstruktur/registrering/'$systemIDCreatedRecord'/ny-dokumentbeskrivelse' )
systemIDCreatedDocumentDescription=$(curl "${curloptsCreateDocumentDescription[@]}" | jq '.systemID' |  sed 's/\"//g');
printf "created   DocumentDescription 1              ($systemIDCreatedDocumentDescription) \n";

# Setup curl options for documentObject
curloptsCreateDocumentObject+=("${curlPostOpts[@]}");
curloptsCreateDocumentObject+=( --data @"$curl_files_dir"document-object-data.json  'http://localhost:8092/noark5v5/api/arkivstruktur/dokumentbeskrivelse/'$systemIDCreatedDocumentDescription'/ny-dokumentobjekt' )
systemIDCreatedDocumentObject=$(curl "${curloptsCreateDocumentObject[@]}" | jq '.systemID' | sed 's/\"//g');
printf "created      DocumentObject      ($systemIDCreatedDocumentObject) associated with ($systemIDCreatedDocumentDescription) \n";
#echo "${curloptsCreateDocumentObject[@]}" "\n";
