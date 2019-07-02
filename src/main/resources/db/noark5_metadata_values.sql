/* formatted with https://sqlformat.org */
/* document_medium / dokumentmedium  */
INSERT INTO md_document_medium (system_id, code, description, version)
VALUES('Fysisk medium', 'Bare fysiske dokumenter', 0);


INSERT INTO md_document_medium (system_id, code, description, version)
VALUES('Elektronisk arkiv', 'Bare elektroniske dokumenter', 0);


INSERT INTO md_document_medium (system_id, code, description, version)
VALUES('Blandet fysisk og elektronisk arkiv', 'Blanding av fysiske og elektroniske dokumenter ', 0);

/* fonds_status / arkivstatus */
INSERT INTO md_fonds_status (system_id, code, description, version)
VALUES('O', 'Opprettet', 0);


INSERT INTO md_fonds_status (system_id, code, description, version)
VALUES('A', 'Avsluttet', 0);

/* series_status / arkivdelstatus*/
INSERT INTO md_series_status (system_id, code, description, version)
VALUES('A', 'Aktiv periode', 0);


INSERT INTO md_series_status (system_id, code, description, version)
VALUES('O', 'Overlappingsperiode', 0);


INSERT INTO md_series_status (system_id, code, description, version)
VALUES('P', 'Avsluttet periode', 0);


INSERT INTO md_series_status (system_id, code, description, version)
VALUES('U', 'Uaktuelle mapper', 0);

/* sign_off_method / avskrivningsmaate */
INSERT INTO md_sign_off_method (system_id, code, description, version)
VALUES('BU', 'Besvart med brev', 0);


INSERT INTO md_sign_off_method (system_id, code, description, version)
VALUES('BE', 'Besvart med e-post', 0);


INSERT INTO md_sign_off_method (system_id, code, description, version)
VALUES('TLF', 'Besvart på telefon', 0);


INSERT INTO md_sign_off_method (system_id, code, description, version)
VALUES('TE', 'Tatt til etterretning', 0);


INSERT INTO md_sign_off_method (system_id, code, description, version)
VALUES('TO', 'Tatt til orientering', 0);

/* document_status / dokumentstatus */
INSERT INTO md_document_status (system_id, code, description, version)
VALUES('B', 'Dokumentet er under redigering', 0);


INSERT INTO md_document_status (system_id, code, description, version)
VALUES('F', 'Dokumentet er ferdigstilt', 0);


INSERT INTO md_document_type (system_id, code, description, version)
VALUES('B', 'Brev', 0);


INSERT INTO md_document_type (system_id, code, description, version)
VALUES('R', 'Rundskriv', 0);


INSERT INTO md_document_type (system_id, code, description, version)
VALUES('F', 'Faktura', 0);


INSERT INTO md_document_type (system_id, code, description, version)
VALUES('O', 'Ordrebekreftelse', 0);

/* electronic_signature_security_level / elektronsiksignatursikkerhetsnivaa */
INSERT INTO md_electronic_signature_security_level (system_id, code, description, version)
VALUES('SK', 'Symmetrisk kryptert', 0);


INSERT INTO md_electronic_signature_security_level (system_id, code, description, version)
VALUES('V', 'Sendt med PKI/virksomhetssertifikat', 0);


INSERT INTO md_electronic_signature_security_level (system_id, code, description, version)
VALUES('PS', 'Sendt med PKI/"personstandard"-sertifikat', 0);


INSERT INTO md_electronic_signature_security_level (system_id, code, description, version)
VALUES('PH', 'Sendt med PKI/"person høy"-sertifikat', 0);

/* electronic_signature_verified / elektronsiksignaturverifisert */
INSERT INTO md_electronic_signature_verified (, system_id, code, description, version)
VALUES('I', 'Signatur påført, ikke verifisert', 0);


INSERT INTO md_electronic_signature_verified (, system_id, code, description, version)
VALUES('V', 'Signatur påført og verifisert', 0);

/* flow_Status flytstatus */
INSERT INTO md_flow_status (system_id, code, description, version)
VALUES('G', 'Godkjent', 0);


INSERT INTO md_flow_status (system_id, code, description, version)
VALUES('I', 'Ikke godkjent', 0);


INSERT INTO md_flow_status (system_id, code, description, version)
VALUES('S', 'Sendt tilbake til saksbehandler med kommentarer', 0);

/* format / format */
INSERT INTO md_format (system_id, code, description, version)
VALUES('RA-TEKST', 'Ren tekst', 0);


INSERT INTO md_format (system_id, code, description, version)
VALUES('RA-TIFF6', 'TIFF versjon 6', 0);


INSERT INTO md_format (system_id, code, description, version)
VALUES('RA-PDF', 'PDF/A - ISO 19005-1:2005', 0);


INSERT INTO md_format (system_id, code, description, version)
VALUES('RA-XML', 'XML', 0);


INSERT INTO md_format (system_id, code, description, version)
VALUES('RA-JPEG', 'JPEG', 0);


INSERT INTO md_format (system_id, code, description, version)
VALUES('RA-SOSI', 'SOSI', 0);


INSERT INTO md_format (system_id, code, description, version)
VALUES('RA-MPEG-2', 'MPEG-2', 0);


INSERT INTO md_format (system_id, code, description, version)
VALUES('RA-MP3', 'MP3', 0);

/* Graderingskode : ClassifiedCode */
INSERT INTO md_classified_code(system_id, code, description, version)
VALUES('SH', 'Strengt hemmelig (sikkerhetsgrad)', 0);


INSERT INTO md_classified_code(system_id, code, description, version)
VALUES('H', 'Hemmelig (sikkerhetsgrad)', 0);


INSERT INTO md_classified_code(system_id, code, description, version)
VALUES('K', 'Konfidensielt (sikkerhetsgrad)', 0);


INSERT INTO md_classified_code(system_id, code, description, version)
VALUES('B', 'Begrenset (sikkerhetsgrad)', 0);


INSERT INTO md_classified_code(system_id, code, description, version)
VALUES('F', 'Fortrolig (beskyttelsesgrad)', 0);


INSERT INTO md_classified_code(system_id, code, description, version)
VALUES('SF', 'Strengt fortrolig (beskyttelsesgrad)', 0);

/* Hendelsetype : EventType */ /* registry_entry_status journalpoststatus */
INSERT INTO md_registry_entry_status(system_id, code, description, version)
VALUES('J', 'Journalført', 0);


INSERT INTO md_registry_entry_status(system_id, code, description, version)
VALUES('F', 'Ferdigstilt fra saksbehandler', 0);


INSERT INTO md_registry_entry_status(system_id, code, description, version)
VALUES('G', 'Godkjent av leder', 0);


INSERT INTO md_registry_entry_status(system_id, code, description, version)
VALUES('E', 'Ekspedert', 0);


INSERT INTO md_registry_entry_status(system_id, code, description, version)
VALUES('A', 'Arkivert', 0);


INSERT INTO md_registry_entry_status(system_id, code, description, version)
VALUES('U', 'Utgår', 0);


INSERT INTO md_registry_entry_status(system_id, code, description, version)
VALUES('M', 'Midlertidig registrering av', 0);

/* registry_entry_type journalposttype */
INSERT INTO md_registry_entry_type(system_id, code, description, version)
VALUES('I', 'Inngående dokument', 0);


INSERT INTO md_registry_entry_type(system_id, code, description, version)
VALUES('U', 'Utgående dokument', 0);


INSERT INTO md_registry_entry_type(system_id, code, description, version)
VALUES('N', 'Organinternt dokument for oppfølging', 0);


INSERT INTO md_registry_entry_type(system_id, code, description, version)
VALUES('X', 'Organinternt dokument uten oppfølging', 0);


INSERT INTO md_registry_entry_type(system_id, code, description, version)
VALUES('S', 'Saksframlegg', 0);

/* correspondence_part_type korrespondanseparttype */
INSERT INTO md_correspondence_part_type (system_id, code, description, version)
VALUES('EA', 'Avsender', 0);


INSERT INTO md_correspondence_part_type (system_id, code, description, version)
VALUES('EM', 'Mottaker', 0);


INSERT INTO md_correspondence_part_type (system_id, code, description, version)
VALUES('EK', 'Kopimottaker', 0);


INSERT INTO md_correspondence_part_type (system_id, code, description, version)
VALUES('GM', 'Gruppemottaker', 0);


INSERT INTO md_correspondence_part_type (system_id, code, description, version)
VALUES('IA', 'Intern avsender', 0);


INSERT INTO md_correspondence_part_type (system_id, code, description, version)
VALUES('IM', 'Intern mottaker', 0);


INSERT INTO md_correspondence_part_type (system_id, code, description, version)
VALUES('IK', 'Intern kopimottaker', 0);

/* precedence_status / presedensstatus */
INSERT INTO md_precedence_status (system_id, code, description, version)
VALUES('G', 'Gjeldende', 0);


INSERT INTO md_precedence_status (system_id, code, description, version)
VALUES('F', 'Foreldet', 0);

/* part_role / sakspartrolle */
INSERT INTO md_part_role (system_id, code, description, version)
VALUES('KLI', 'Klient', 0);


INSERT INTO md_part_role (system_id, code, description, version)
VALUES('PÅ', 'Pårørende', 0);


INSERT INTO md_part_role (system_id, code, description, version)
VALUES('FORM', 'Formynder', 0);


INSERT INTO md_part_role (system_id, code, description, version)
VALUES('ADV', 'Advokat', 0);

/* file_type / mappetype
 Note. This is an open code list in the standard. I have added the following
 values based on common sense.
*/
INSERT INTO md_file_type (system_id, code, description, version)
VALUES('SKM', 'Saksmappe', 0);


INSERT INTO md_file_type (system_id, code, description, version)
VALUES('PLM', 'Personalmappe', 0);


INSERT INTO md_file_type (system_id, code, description, version)
VALUES('BYM', 'Byggemappe', 0);


INSERT INTO md_file_type (system_id, code, description, version)
VALUES('MOM', 'Møtemappe', 0);

/* comment / merknad
 Note. This is an open code list in the standard. The following values are taken
 from the Noark 4 standard
*/
INSERT INTO md_comment_type (system_id, code, description, version)
VALUES('MS', 'Merknad fra saksbehandler', 0);


INSERT INTO md_comment_type (system_id, code, description, version)
VALUES('ML', 'Merknad fra leder', 0);


INSERT INTO md_comment_type (system_id, code, description, version)
VALUES('MA', 'Merknad fra arkivansvarlig', 0);

/* classification_type / klassifikasjonstype
*/
INSERT INTO md_classification_type (system_id, code, description, version)
VALUES('GBN', 'Gårds- og bruksnummer Valgfri', 0);


INSERT INTO md_classification_type (system_id, code, description, version)
VALUES('FH', 'Funksjonsbasert, hierarkisk Valgfri', 0);


INSERT INTO md_classification_type (system_id, code, description, version)
VALUES('EH', 'Emnebasert, hierarkisk arkivnøkkel', 0);


INSERT INTO md_classification_type (system_id, code, description, version)
VALUES('E1', 'Emnebasert, ett nivå', 0);


INSERT INTO md_classification_type (system_id, code, description, version)
VALUES('KK', 'K-koder', 0);


INSERT INTO md_classification_type (system_id, code, description, version)
VALUES('MF', 'Mangefasettert, ikke hierarki', 0);


INSERT INTO md_classification_type (system_id, code, description, version)
VALUES('UO', 'Objektbasert', 0);


INSERT INTO md_classification_type (system_id, code, description, version)
VALUES('PNR', 'Fødselsnummer', 0);

/* screening_document / skjermingdokument*/
INSERT INTO md_screening_document (system_id, code, description, version)
VALUES('H', 'Skjerming av hele dokumentet', 0);


INSERT INTO md_screening_document (system_id, code, description, version)
VALUES('D', 'Skjerming av deler av dokumentet', 0);

/* screening_metadata / skjermingmetadata*/
INSERT INTO md_screening_metadata (system_id, code, description, version)
VALUES('KID', 'Skjerming klasseID', 0);


INSERT INTO md_screening_metadata (system_id, code, description, version)
VALUES('TKL', 'Skjerming tittel klasse', 0);


INSERT INTO md_screening_metadata (system_id, code, description, version)
VALUES('TM1', 'Skjerming tittel mappe - unntatt første linje', 0);


INSERT INTO md_screening_metadata (system_id, code, description, version)
VALUES('TMO', 'Skjerming tittel mappe - utvalgte ord', 0);


INSERT INTO md_screening_metadata (system_id, code, description, version)
VALUES('NPS', 'Skjerming navn part i sak', 0);


INSERT INTO md_screening_metadata (system_id, code, description, version)
VALUES('TR1', 'Skjerming tittel registrering - unntatt første linje', 0);


INSERT INTO md_screening_metadata (system_id, code, description, version)
VALUES('TRO', 'Skjerming tittel registrering - utvalgte ord', 0);


INSERT INTO md_screening_metadata (system_id, code, description, version)
VALUES('NA', 'Skjerming navn avsender', 0);


INSERT INTO md_screening_metadata (system_id, code, description, version)
VALUES('NM', 'Skjerming navn mottaker', 0);


INSERT INTO md_screening_metadata (system_id, code, description, version)
VALUES('TD', 'Skjerming tittel dokumentbeskrivelse', 0);


INSERT INTO md_screening_metadata (system_id, code, description, version)
VALUES(1'06706ffe-acd9-46c6-a67f-0a8733b2de06', 'MT', 'Skjerming merknadstekst', 0);


INSERT INTO md_screening_metadata (system_id, code, description, version)
VALUES(1'2f68901a-a60a-4212-8b6a-7505c02d7104', 'M', 'Midlertidig skjerming', 0);

/* case_status / saksstatus*/
INSERT INTO md_case_status(system_id, code, description, version)
VALUES('B', 'Under behandling', 0);


INSERT INTO md_case_status(system_id, code, description, version)
VALUES('A', 'Avsluttet', 0);


INSERT INTO md_case_status(system_id, code, description, version)
VALUES('U', 'Utgår', 0);


INSERT INTO md_case_status(system_id, code, description, version)
VALUES('R', 'Opprettet av saksbehandler', 0);


INSERT INTO md_case_status(system_id, code, description, version)
VALUES('S', 'Avsluttet av saksbehandler', 0);


INSERT INTO md_case_status(system_id, code, description, version)
VALUES('P', 'Unntatt prosesstyring', 0);


INSERT INTO md_case_status(system_id, code, description, version)
VALUES('F', 'Ferdig fra saksbehandler', 0);

/* variant_format / variantformat */
INSERT INTO md_variant_format(system_id, code, description, version)
VALUES('P', 'Produksjonsformat', 0);


INSERT INTO md_variant_format(system_id, code, description, version)
VALUES('A', 'Arkivformat', 0);


INSERT INTO md_variant_format(system_id, code, description, version)
VALUES('O', 'Dokument hvor deler av innholdet er skjermet', 0);

