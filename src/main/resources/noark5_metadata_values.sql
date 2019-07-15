/* formatted with https://sqlformat.org */
/* document_medium / dokumentmedium  */
INSERT INTO md_document_medium (code, description)
{ "code" : "F', 'Fysisk medium');


INSERT INTO md_document_medium (code, description)
{ "code" : "E', 'Elektronisk arkiv');


INSERT INTO md_document_medium (code, description)
{ "code" : "B', 'Blandet fysisk og elektronisk arkiv');

/* fonds_status / arkivstatus */
INSERT INTO md_fonds_status (code, description)
{ "code" : "O', 'Opprettet');


INSERT INTO md_fonds_status (code, description)
{ "code" : "A', 'Avsluttet');

/* series_status / arkivdelstatus*/
INSERT INTO md_series_status (code, description)
{ "code" : "A', 'Aktiv periode');


INSERT INTO md_series_status (code, description)
{ "code" : "O', 'Overlappingsperiode');


INSERT INTO md_series_status (code, description)
{ "code" : "P', 'Avsluttet periode');


INSERT INTO md_series_status (code, description)
{ "code" : "U', 'Uaktuelle mapper');

/* sign_off_method / avskrivningsmaate */
INSERT INTO md_sign_off_method (code, description)
{ "code" : "BU', 'Besvart med brev');


INSERT INTO md_sign_off_method (code, description)
{ "code" : "BE', 'Besvart med e-post');


INSERT INTO md_sign_off_method (code, description)
{ "code" : "TLF', 'Besvart på telefon');


INSERT INTO md_sign_off_method (code, description)
{ "code" : "TE', 'Tatt til etterretning');


INSERT INTO md_sign_off_method (code, description)
{ "code" : "TO', 'Tatt til orientering');

/* document_status / dokumentstatus */
INSERT INTO md_document_status (code, description)
{ "code" : "B', 'Dokumentet er under redigering');


INSERT INTO md_document_status (code, description)
{ "code" : "F', 'Dokumentet er ferdigstilt');


INSERT INTO md_document_type (code, description)
{ "code" : "B', 'Brev');


INSERT INTO md_document_type (code, description)
{ "code" : "R', 'Rundskriv');


INSERT INTO md_document_type (code, description)
{ "code" : "F', 'Faktura');


INSERT INTO md_document_type (code, description)
{ "code" : "O', 'Ordrebekreftelse');

/* electronic_signature_security_level / elektronsiksignatursikkerhetsnivaa */
INSERT INTO md_electronic_signature_security_level (code, description)
{ "code" : "SK', 'Symmetrisk kryptert');


INSERT INTO md_electronic_signature_security_level (code, description)
{ "code" : "V', 'Sendt med PKI/virksomhetssertifikat');


INSERT INTO md_electronic_signature_security_level (code, description)
{ "code" : "PS', 'Sendt med PKI/"personstandard"-sertifikat');


INSERT INTO md_electronic_signature_security_level (code, description)
{ "code" : "PH', 'Sendt med PKI/"person høy"-sertifikat');

/* electronic_signature_verified / elektronsiksignaturverifisert */
INSERT INTO md_electronic_signature_verified (, code, description)
{ "code" : "I', 'Signatur påført, ikke verifisert');


INSERT INTO md_electronic_signature_verified (, code, description)
{ "code" : "V', 'Signatur påført og verifisert');

/* flow_Status flytstatus */
INSERT INTO md_flow_status (code, description)
{ "code" : "G', 'Godkjent');


INSERT INTO md_flow_status (code, description)
{ "code" : "I', 'Ikke godkjent');


INSERT INTO md_flow_status (code, description)
{ "code" : "S', 'Sendt tilbake til saksbehandler med kommentarer');

/* format / format */
INSERT INTO md_format (code, description)
{ "code" : "RA-TEKST', 'Ren tekst');


INSERT INTO md_format (code, description)
{ "code" : "RA-TIFF6', 'TIFF versjon 6');


INSERT INTO md_format (code, description)
{ "code" : "RA-PDF', 'PDF/A - ISO 19005-1:2005');


INSERT INTO md_format (code, description)
{ "code" : "RA-XML', 'XML');


INSERT INTO md_format (code, description)
{ "code" : "RA-JPEG', 'JPEG');


INSERT INTO md_format (code, description)
{ "code" : "RA-SOSI', 'SOSI');


INSERT INTO md_format (code, description)
{ "code" : "RA-MPEG-2', 'MPEG-2');


INSERT INTO md_format (code, description)
{ "code" : "RA-MP3', 'MP3');

/* Graderingskode : ClassifiedCode */
INSERT INTO md_classified_code(code, description)
{ "code" : "SH', 'Strengt hemmelig (sikkerhetsgrad)');


INSERT INTO md_classified_code(code, description)
{ "code" : "H', 'Hemmelig (sikkerhetsgrad)');


INSERT INTO md_classified_code(code, description)
{ "code" : "K', 'Konfidensielt (sikkerhetsgrad)');


INSERT INTO md_classified_code(code, description)
{ "code" : "B', 'Begrenset (sikkerhetsgrad)');


INSERT INTO md_classified_code(code, description)
{ "code" : "F', 'Fortrolig (beskyttelsesgrad)');


INSERT INTO md_classified_code(code, description)
{ "code" : "SF', 'Strengt fortrolig (beskyttelsesgrad)');

/* Hendelsetype : EventType */ /* registry_entry_status journalpoststatus */
INSERT INTO md_registry_entry_status(code, description)
{ "code" : "J', 'Journalført');


INSERT INTO md_registry_entry_status(code, description)
{ "code" : "F', 'Ferdigstilt fra saksbehandler');


INSERT INTO md_registry_entry_status(code, description)
{ "code" : "G', 'Godkjent av leder');


INSERT INTO md_registry_entry_status(code, description)
{ "code" : "E', 'Ekspedert');


INSERT INTO md_registry_entry_status(code, description)
{ "code" : "A', 'Arkivert');


INSERT INTO md_registry_entry_status(code, description)
{ "code" : "U', 'Utgår');


INSERT INTO md_registry_entry_status(code, description)
{ "code" : "M', 'Midlertidig registrering av');

/* registry_entry_type journalposttype */
INSERT INTO md_registry_entry_type(code, description)
{ "code" : "I', 'Inngående dokument');


INSERT INTO md_registry_entry_type(code, description)
{ "code" : "U', 'Utgående dokument');


INSERT INTO md_registry_entry_type(code, description)
{ "code" : "N', 'Organinternt dokument for oppfølging');


INSERT INTO md_registry_entry_type(code, description)
{ "code" : "X', 'Organinternt dokument uten oppfølging');


INSERT INTO md_registry_entry_type(code, description)
{ "code" : "S', 'Saksframlegg');

/* correspondence_part_type korrespondanseparttype */
INSERT INTO md_correspondence_part_type (code, description)
{ "code" : "EA', 'Avsender');


INSERT INTO md_correspondence_part_type (code, description)
{ "code" : "EM', 'Mottaker');


INSERT INTO md_correspondence_part_type (code, description)
{ "code" : "EK', 'Kopimottaker');


INSERT INTO md_correspondence_part_type (code, description)
{ "code" : "GM', 'Gruppemottaker');


INSERT INTO md_correspondence_part_type (code, description)
{ "code" : "IA', 'Intern avsender');


INSERT INTO md_correspondence_part_type (code, description)
{ "code" : "IM', 'Intern mottaker');


INSERT INTO md_correspondence_part_type (code, description)
{ "code" : "IK', 'Intern kopimottaker');

/* precedence_status / presedensstatus */
INSERT INTO md_precedence_status (code, description)
{ "code" : "G', 'Gjeldende');


INSERT INTO md_precedence_status (code, description)
{ "code" : "F', 'Foreldet');

/* part_role / sakspartrolle */
INSERT INTO md_part_role (code, description)
{ "code" : "KLI', 'Klient');


INSERT INTO md_part_role (code, description)
{ "code" : "PÅ', 'Pårørende');


INSERT INTO md_part_role (code, description)
{ "code" : "FORM', 'Formynder');


INSERT INTO md_part_role (code, description)
{ "code" : "ADV', 'Advokat');

/* file_type / mappetype
 Note. This is an open code list in the standard. I have added the following
 values based on common sense.
*/
INSERT INTO md_file_type (code, description)
{ "code" : "SKM', 'Saksmappe');


INSERT INTO md_file_type (code, description)
{ "code" : "PLM', 'Personalmappe');


INSERT INTO md_file_type (code, description)
{ "code" : "BYM', 'Byggemappe');


INSERT INTO md_file_type (code, description)
{ "code" : "MOM', 'Møtemappe');

/* comment / merknad
 Note. This is an open code list in the standard. The following values are taken
 from the Noark 4 standard
*/
INSERT INTO md_comment_type (code, description)
{ "code" : "MS', 'Merknad fra saksbehandler');


INSERT INTO md_comment_type (code, description)
{ "code" : "ML', 'Merknad fra leder');


INSERT INTO md_comment_type (code, description)
{ "code" : "MA', 'Merknad fra arkivansvarlig');

/* classification_type / klassifikasjonstype
*/
INSERT INTO md_classification_type (code, description)
{ "code" : "GBN', 'Gårds- og bruksnummer Valgfri');


INSERT INTO md_classification_type (code, description)
{ "code" : "FH', 'Funksjonsbasert, hierarkisk Valgfri');


INSERT INTO md_classification_type (code, description)
{ "code" : "EH', 'Emnebasert, hierarkisk arkivnøkkel');


INSERT INTO md_classification_type (code, description)
{ "code" : "E1', 'Emnebasert, ett nivå');


INSERT INTO md_classification_type (code, description)
{ "code" : "KK', 'K-koder');


INSERT INTO md_classification_type (code, description)
{ "code" : "MF', 'Mangefasettert, ikke hierarki');


INSERT INTO md_classification_type (code, description)
{ "code" : "UO', 'Objektbasert');


INSERT INTO md_classification_type (code, description)
{ "code" : "PNR', 'Fødselsnummer');

/* screening_document / skjermingdokument*/
INSERT INTO md_screening_document (code, description)
{ "code" : "H', 'Skjerming av hele dokumentet');


INSERT INTO md_screening_document (code, description)
{ "code" : "D', 'Skjerming av deler av dokumentet');

/* screening_metadata / skjermingmetadata*/
INSERT INTO md_screening_metadata (code, description)
{ "code" : "KID', 'Skjerming klasseID');


INSERT INTO md_screening_metadata (code, description)
{ "code" : "TKL', 'Skjerming tittel klasse');


INSERT INTO md_screening_metadata (code, description)
{ "code" : "TM1', 'Skjerming tittel mappe - unntatt første linje');


INSERT INTO md_screening_metadata (code, description)
{ "code" : "TMO', 'Skjerming tittel mappe - utvalgte ord');


INSERT INTO md_screening_metadata (code, description)
{ "code" : "NPS', 'Skjerming navn part i sak');


INSERT INTO md_screening_metadata (code, description)
{ "code" : "TR1', 'Skjerming tittel registrering - unntatt første linje');


INSERT INTO md_screening_metadata (code, description)
{ "code" : "TRO', 'Skjerming tittel registrering - utvalgte ord');


INSERT INTO md_screening_metadata (code, description)
{ "code" : "NA', 'Skjerming navn avsender');


INSERT INTO md_screening_metadata (code, description)
{ "code" : "NM', 'Skjerming navn mottaker');


INSERT INTO md_screening_metadata (code, description)
{ "code" : "TD', 'Skjerming tittel dokumentbeskrivelse');


INSERT INTO md_screening_metadata (code, description)
VALUES(1'06706ffe-acd9-46c6-a67f-0a8733b2de06', 'MT', 'Skjerming merknadstekst');


INSERT INTO md_screening_metadata (code, description)
VALUES(1'2f68901a-a60a-4212-8b6a-7505c02d7104', 'M', 'Midlertidig skjerming');

/* case_status / saksstatus*/
INSERT INTO md_case_status(code, description)
{ "code" : "B', 'Under behandling');


INSERT INTO md_case_status(code, description)
{ "code" : "A', 'Avsluttet');


INSERT INTO md_case_status(code, description)
{ "code" : "U', 'Utgår');


INSERT INTO md_case_status(code, description)
{ "code" : "R', 'Opprettet av saksbehandler');


INSERT INTO md_case_status(code, description)
{ "code" : "S', 'Avsluttet av saksbehandler');


INSERT INTO md_case_status(code, description)
{ "code" : "P', 'Unntatt prosesstyring');


INSERT INTO md_case_status(code, description)
{ "code" : "F', 'Ferdig fra saksbehandler');

/* variant_format / variantformat */
INSERT INTO md_variant_format(code, description)
{ "code" : "P', 'Produksjonsformat');


INSERT INTO md_variant_format(code, description)
{ "code" : "A', 'Arkivformat');


INSERT INTO md_variant_format(code, description)
{ "code" : "O', 'Dokument hvor deler av innholdet er skjermet');

