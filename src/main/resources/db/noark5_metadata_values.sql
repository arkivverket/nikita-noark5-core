/* formatted with https://sqlformat.org */
/* document_medium / dokumentmedium  */
INSERT INTO document_medium (system_id, code, description, version)
VALUES ('48e067ae-d794-4216-8a9d-7cb0ef03472c', 'Fysisk medium', 'Bare fysiske dokumenter', 0);


INSERT INTO document_medium (system_id, code, description, version)
VALUES ('1b617405-9dd6-4187-bee6-81ed4f4a0e1f', 'Elektronisk arkiv', 'Bare elektroniske dokumenter', 0);


INSERT INTO document_medium (system_id, code, description, version)
VALUES ('00d21eea-c231-4542-8e3a-964f441fbbd3', 'Blandet fysisk og elektronisk arkiv', 'Blanding av fysiske og elektroniske dokumenter ', 0);

/* fonds_status / arkivstatus */
INSERT INTO fonds_status (system_id, code, description, version)
VALUES ('9ed3f5d3-9934-446d-85b8-b2ada9498aa7', 'O', 'Opprettet', 0);


INSERT INTO fonds_status (system_id, code, description, version)
VALUES ('b94eb9cf-e71f-4d68-964b-b68c7f9962b6', 'A', 'Avsluttet', 0);

/* series_status / arkivdelstatus*/
INSERT INTO series_status (system_id, code, description, version)
VALUES ('21dca458-5715-455b-baef-6c81c6e1e73a', 'A', 'Aktiv periode', 0);


INSERT INTO series_status (system_id, code, description, version)
VALUES ('24ee7d87-4e5e-46d8-ba9a-d236609fdeeb', 'O', 'Overlappingsperiode', 0);


INSERT INTO series_status (system_id, code, description, version)
VALUES ('a67ae905-a556-4cf6-bfa9-43ad15836f61', 'P', 'Avsluttet periode', 0);


INSERT INTO series_status (system_id, code, description, version)
VALUES ('30bec9da-7299-4f50-9233-8a556cc5708c', 'U', 'Uaktuelle mapper', 0);

/* sign_off_method / avskrivningsmaate */
INSERT INTO sign_off_method (system_id, code, description, version)
VALUES ('5ccea231-c2cc-4507-8a72-709c9c54bcd5', 'BU', 'Besvart med brev', 0);


INSERT INTO sign_off_method (system_id, code, description, version)
VALUES ('3ea3d7c2-2479-46a4-b56d-8cc0c31208a7', 'BE', 'Besvart med e-post', 0);


INSERT INTO sign_off_method (system_id, code, description, version)
VALUES ('e549d858-f6d6-4d86-b811-014ade0ca6c6', 'TLF', 'Besvart på telefon', 0);


INSERT INTO sign_off_method (system_id, code, description, version)
VALUES ('ec14aac1-df6d-495a-bcf5-caa9a0c18bb8', 'TE', 'Tatt til etterretning', 0);


INSERT INTO sign_off_method (system_id, code, description, version)
VALUES ('c62c80d9-7700-4243-b2e1-fd14af9a9559', 'TO', 'Tatt til orientering', 0);

/* document_status / dokumentstatus */
INSERT INTO document_status (system_id, code, description, version)
VALUES ('553287bc-4592-4107-8567-0402458fdc4e', 'B', 'Dokumentet er under redigering', 0);


INSERT INTO document_status (system_id, code, description, version)
VALUES ('29ad0b9a-fe52-4e4b-af9a-1a4841f6ad43', 'F', 'Dokumentet er ferdigstilt', 0);


INSERT INTO document_type (system_id, code, description, version)
VALUES ('26ef04f8-5efb-4f19-9cde-d99a40d8cb1b', 'B', 'Brev', 0);


INSERT INTO document_type (system_id, code, description, version)
VALUES ('e12f0bf6-aac4-4bb0-8f23-9847f0b67453', 'R', 'Rundskriv', 0);


INSERT INTO document_type (system_id, code, description, version)
VALUES ('1f4ec7b5-a717-433d-a3dc-d357e6579de0', 'F', 'Faktura', 0);


INSERT INTO document_type (system_id, code, description, version)
VALUES ('99281649-0d0c-402f-9494-7df99b7d73f1', 'O', 'Ordrebekreftelse', 0);

/* electronic_signature_security_level / elektronsiksignatursikkerhetsnivaa */
INSERT INTO electronic_signature_security_level (system_id, code, description, version)
VALUES ('dc55f30f-a549-40ed-86e3-b3104cb1f7f5', 'SK', 'Symmetrisk kryptert', 0);


INSERT INTO electronic_signature_security_level (system_id, code, description, version)
VALUES ('e1872a1e-4233-4a6e-884d-d8bdb3c48754', 'V', 'Sendt med PKI/virksomhetssertifikat', 0);


INSERT INTO electronic_signature_security_level (system_id, code, description, version)
VALUES ('2aa9657b-8498-4d33-8e41-0acf649a8d6d', 'PS', 'Sendt med PKI/"personstandard"-sertifikat', 0);


INSERT INTO electronic_signature_security_level (system_id, code, description, version)
VALUES ('558401c6-b99e-4adf-9576-811da4c7e461', 'PH', 'Sendt med PKI/"person høy"-sertifikat', 0);

/* electronic_signature_verified / elektronsiksignaturverifisert */
INSERT INTO electronic_signature_verified (, system_id, code, description, version)
VALUES ('e8ff13ad-dba4-450b-92b7-f41da771b300', 'I', 'Signatur påført, ikke verifisert', 0);


INSERT INTO electronic_signature_verified (, system_id, code, description, version)
VALUES ('f2006c7b-44f4-4a84-8c62-bbcc7f9ee608', 'V', 'Signatur påført og verifisert', 0);

/* flow_Status flytstatus */
INSERT INTO flow_status (system_id, code, description, version)
VALUES ('d854743a-8c5a-4791-b56c-a10fd240e768', 'G', 'Godkjent', 0);


INSERT INTO flow_status (system_id, code, description, version)
VALUES ('949ad9cf-59ff-492e-9ce5-8a3bdced9cc9', 'I', 'Ikke godkjent', 0);


INSERT INTO flow_status (system_id, code, description, version)
VALUES ('8a802fc2-7d9c-43ae-babd-72cc19d47a46', 'S', 'Sendt tilbake til saksbehandler med kommentarer', 0);

/* format / format */
INSERT INTO format (system_id, code, description, version)
VALUES ('93ac2b0f-1729-4c78-a917-a0f494a035ca', 'RA-TEKST', 'Ren tekst', 0);


INSERT INTO format (system_id, code, description, version)
VALUES ('ba4c8c2c-4072-4fa7-b194-8812ea38697f', 'RA-TIFF6', 'TIFF versjon 6', 0);


INSERT INTO format (system_id, code, description, version)
VALUES ('eb53170a-14b1-48e2-a5c4-965fef380ca6', 'RA-PDF', 'PDF/A - ISO 19005-1:2005', 0);


INSERT INTO format (system_id, code, description, version)
VALUES ('3d2c0bad-c8e3-4f03-b52e-d99867d53230', 'RA-XML', 'XML', 0);


INSERT INTO format (system_id, code, description, version)
VALUES ('18468b06-b491-4029-9e6f-395ad1d27b03', 'RA-JPEG', 'JPEG', 0);


INSERT INTO format (system_id, code, description, version)
VALUES ('d5063523-83a2-4e51-b8aa-925385849e33', 'RA-SOSI', 'SOSI', 0);


INSERT INTO format (system_id, code, description, version)
VALUES ('9ae48e86-3c9e-47fd-bbf2-4d9f67b7de85', 'RA-MPEG-2', 'MPEG-2', 0);


INSERT INTO format (system_id, code, description, version)
VALUES ('7412833e-ceaf-40fd-95b5-e634f4640f99', 'RA-MP3', 'MP3', 0);

/* Graderingskode : ClassifiedCode */
INSERT INTO classified_code(system_id, code, description, version)
VALUES ('16665453-136d-4d28-a3c2-68fbd2fa0b5e', 'SH', 'Strengt hemmelig (sikkerhetsgrad)', 0);


INSERT INTO classified_code(system_id, code, description, version)
VALUES ('04ebca54-7672-4433-858b-0c684916d625', 'H', 'Hemmelig (sikkerhetsgrad)', 0);


INSERT INTO classified_code(system_id, code, description, version)
VALUES ('11dc8ec9-0328-4166-a7d9-e1ea192d5eda', 'K', 'Konfidensielt (sikkerhetsgrad)', 0);


INSERT INTO classified_code(system_id, code, description, version)
VALUES ('c8c2ad9a-d812-4c3b-b912-3924dd176373', 'B', 'Begrenset (sikkerhetsgrad)', 0);


INSERT INTO classified_code(system_id, code, description, version)
VALUES ('538b04f8-a156-41b3-b9dc-55fd04655d8d', 'F', 'Fortrolig (beskyttelsesgrad)', 0);


INSERT INTO classified_code(system_id, code, description, version)
VALUES ('803e731d-0012-4958-944c-39ccd3e39609', 'SF', 'Strengt fortrolig (beskyttelsesgrad)', 0);

/* Hendelsetype : EventType */ /* registry_entry_status journalpoststatus */
INSERT INTO registry_entry_status(system_id, code, description, version)
VALUES ('0b4cfcb9-e979-4cdd-94d3-1660172976a4', 'J', 'Journalført', 0);


INSERT INTO registry_entry_status(system_id, code, description, version)
VALUES ('2f140067-8239-4ea3-88a3-c42380ec3885', 'F', 'Ferdigstilt fra saksbehandler', 0);


INSERT INTO registry_entry_status(system_id, code, description, version)
VALUES ('218deb74-f9e0-48e2-a523-3f47a6a44aba', 'G', 'Godkjent av leder', 0);


INSERT INTO registry_entry_status(system_id, code, description, version)
VALUES ('35940d96-e531-4e5b-8f0a-63f8c7d08cb0', 'E', 'Ekspedert', 0);


INSERT INTO registry_entry_status(system_id, code, description, version)
VALUES ('74aef294-f3d1-4e77-a3c1-abcb45a7eeea', 'A', 'Arkivert', 0);


INSERT INTO registry_entry_status(system_id, code, description, version)
VALUES ('8bc27628-f722-41c5-9ce9-4f46fc54bf34', 'U', 'Utgår', 0);


INSERT INTO registry_entry_status(system_id, code, description, version)
VALUES ('395ff299-e60d-4550-979e-5b682d8e4f61', 'M', 'Midlertidig registrering av', 0);

/* registry_entry_type journalposttype */
INSERT INTO registry_entry_type(system_id, code, description, version)
VALUES ('4827e5e0-0fae-4b9e-a01d-88ce00e8c62e', 'I', 'Inngående dokument', 0);


INSERT INTO registry_entry_type(system_id, code, description, version)
VALUES ('f87a8b7f-bb31-4dea-98c6-13e1ac17e24f', 'U', 'Utgående dokument', 0);


INSERT INTO registry_entry_type(system_id, code, description, version)
VALUES ('001f176e-d68a-42f4-95f8-555a72c1a748', 'N', 'Organinternt dokument for oppfølging', 0);


INSERT INTO registry_entry_type(system_id, code, description, version)
VALUES ('2014243a-5d33-4fd3-98af-eda7ddd4cdc8', 'X', 'Organinternt dokument uten oppfølging', 0);


INSERT INTO registry_entry_type(system_id, code, description, version)
VALUES ('07c06822-50e8-47fb-a760-eaff70eaa64e', 'S', 'Saksframlegg', 0);

/* correspondence_part_type korrespondanseparttype */
INSERT INTO correspondence_part_type (system_id, code, description, version)
VALUES ('5b4588a3-7e4d-43ae-a3a8-4033e52944c0', 'EA', 'Avsender', 0);


INSERT INTO correspondence_part_type (system_id, code, description, version)
VALUES ('c4a2097c-8cd1-496f-9cf5-bd16de1d85fe', 'EM', 'Mottaker', 0);


INSERT INTO correspondence_part_type (system_id, code, description, version)
VALUES ('6c2ace2f-05b5-45c0-96da-cff41cc5b2e7', 'EK', 'Kopimottaker', 0);


INSERT INTO correspondence_part_type (system_id, code, description, version)
VALUES ('dcb06157-c8d3-4b43-a065-5cc21e80fbf6', 'GM', 'Gruppemottaker', 0);


INSERT INTO correspondence_part_type (system_id, code, description, version)
VALUES ('896d5f6c-2093-492d-a531-7dd845d45808', 'IA', 'Intern avsender', 0);


INSERT INTO correspondence_part_type (system_id, code, description, version)
VALUES ('77e1fbc4-e480-4e41-a97e-f429b816629e', 'IM', 'Intern mottaker', 0);


INSERT INTO correspondence_part_type (system_id, code, description, version)
VALUES ('78e59b26-819c-4cf7-abe1-69fb6e457bcb', 'IK', 'Intern kopimottaker', 0);

/* precedence_status / presedensstatus */
INSERT INTO precedence_status (system_id, code, description, version)
VALUES ('f2c39e2e-8353-49ca-91a1-6ae48c658cd1', 'G', 'Gjeldende', 0);


INSERT INTO precedence_status (system_id, code, description, version)
VALUES ('cacc4170-6149-4d65-8811-7ca5855e089b', 'F', 'Foreldet', 0);

/* part_role / sakspartrolle */
INSERT INTO part_role (system_id, code, description, version)
VALUES ('391e6756-1885-4ef9-b2cb-226a7bbcd381', 'KLI', 'Klient', 0);


INSERT INTO part_role (system_id, code, description, version)
VALUES ('882ada83-04ca-4a24-81ba-5572d653804d', 'PÅ', 'Pårørende', 0);


INSERT INTO part_role (system_id, code, description, version)
VALUES ('b49e85df-1512-4ad4-8ca9-5fe712bb16f7', 'FORM', 'Formynder', 0);


INSERT INTO part_role (system_id, code, description, version)
VALUES ('cd0373f5-01c3-4586-b9b7-9280b3e579b9', 'ADV', 'Advokat', 0);

/* file_type / mappetype
 Note. This is an open code list in the standard. I have added the following
 values based on common sense.
*/
INSERT INTO file_type (system_id, code, description, version)
VALUES ('2a14a47d-4bbc-4d0d-b3e7-267b105ec539', 'SKM', 'Saksmappe', 0);


INSERT INTO file_type (system_id, code, description, version)
VALUES ('84766390-8799-4e7d-96ab-1e5f26596804', 'PLM', 'Personalmappe', 0);


INSERT INTO file_type (system_id, code, description, version)
VALUES ('b027b486-9b42-48a0-88e3-c2a6ddc91589', 'BYM', 'Byggemappe', 0);


INSERT INTO file_type (system_id, code, description, version)
VALUES ('1b8c9632-b5d1-49a1-a0ec-2ec2ee3e7483', 'MOM', 'Møtemappe', 0);

/* comment / merknad
 Note. This is an open code list in the standard. The following values are taken
 from the Noark 4 standard
*/
INSERT INTO comment_type (system_id, code, description, version)
VALUES ('bf105de3-7eb7-4abc-b836-b89180df462c', 'MS', 'Merknad fra saksbehandler', 0);


INSERT INTO comment_type (system_id, code, description, version)
VALUES ('c7d484ab-fb3c-4da5-b475-f39c085086e0', 'ML', 'Merknad fra leder', 0);


INSERT INTO comment_type (system_id, code, description, version)
VALUES ('7c1b04df-26b6-4a91-a949-3cb1717bd901', 'MA', 'Merknad fra arkivansvarlig', 0);

/* classification_type / klassifikasjonstype
*/
INSERT INTO classification_type (system_id, code, description, version)
VALUES ('d10dddcd-74c0-430a-8b03-32b489fb6900', 'GBN', 'Gårds- og bruksnummer Valgfri', 0);


INSERT INTO classification_type (system_id, code, description, version)
VALUES ('0a101af4-ce12-47b2-9c48-6c7d9b72a260', 'FH', 'Funksjonsbasert, hierarkisk Valgfri', 0);


INSERT INTO classification_type (system_id, code, description, version)
VALUES ('cf999e08-d4ee-4afe-9d85-604c56fb4e0c', 'EH', 'Emnebasert, hierarkisk arkivnøkkel', 0);


INSERT INTO classification_type (system_id, code, description, version)
VALUES ('50efdb25-bc8d-4ed8-abfc-3e12f6d314a1', 'E1', 'Emnebasert, ett nivå', 0);


INSERT INTO classification_type (system_id, code, description, version)
VALUES ('2577e645-0207-4f12-8a58-8bc4dda2e7f6', 'KK', 'K-koder', 0);


INSERT INTO classification_type (system_id, code, description, version)
VALUES ('f32705c8-6572-4fd8-b9f0-96adc69202e2', 'MF', 'Mangefasettert, ikke hierarki', 0);


INSERT INTO classification_type (system_id, code, description, version)
VALUES ('b8b8982f-4464-4d9a-9103-e0682f353722', 'UO', 'Objektbasert', 0);


INSERT INTO classification_type (system_id, code, description, version)
VALUES ('9f8b839e-35c5-4382-a774-3f42f00e75a4', 'PNR', 'Fødselsnummer', 0);

/* screening_document / skjermingdokument*/
INSERT INTO screening_document (system_id, code, description, version)
VALUES ('bef2f7ea-c0ec-4e66-8344-79c47f08a83d', 'H', 'Skjerming av hele dokumentet', 0);


INSERT INTO screening_document (system_id, code, description, version)
VALUES ('ebb87206-7bc9-45c4-8903-bff19f7f814e', 'D', 'Skjerming av deler av dokumentet', 0);

/* screening_metadata / skjermingmetadata*/
INSERT INTO screening_metadata (system_id, code, description, version)
VALUES ('d09621ac-3ae7-470b-93c5-c02f22c52124', 'KID', 'Skjerming klasseID', 0);


INSERT INTO screening_metadata (system_id, code, description, version)
VALUES ('faff8507-edf9-464f-b539-080c3504e4bb', 'TKL', 'Skjerming tittel klasse', 0);


INSERT INTO screening_metadata (system_id, code, description, version)
VALUES ('3d24337c-e7d0-4b42-a153-115c59e0ec75', 'TM1', 'Skjerming tittel mappe - unntatt første linje', 0);


INSERT INTO screening_metadata (system_id, code, description, version)
VALUES ('a524d3bd-8da0-423d-acb2-e2870d959724', 'TMO', 'Skjerming tittel mappe - utvalgte ord', 0);


INSERT INTO screening_metadata (system_id, code, description, version)
VALUES ('f9a1cd40-2175-44ac-8262-bae0c3543e04', 'NPS', 'Skjerming navn part i sak', 0);


INSERT INTO screening_metadata (system_id, code, description, version)
VALUES ('a0c2a727-dbd6-4197-b928-82698eeadbeb', 'TR1', 'Skjerming tittel registrering - unntatt første linje', 0);


INSERT INTO screening_metadata (system_id, code, description, version)
VALUES ('2197150e-0566-42a6-9838-687605e40b9b', 'TRO', 'Skjerming tittel registrering - utvalgte ord', 0);


INSERT INTO screening_metadata (system_id, code, description, version)
VALUES ('f5d9452b-2acb-40e6-84bd-422796f01008', 'NA', 'Skjerming navn avsender', 0);


INSERT INTO screening_metadata (system_id, code, description, version)
VALUES ('0c688727-5b5d-4f59-be91-58850a869e9f', 'NM', 'Skjerming navn mottaker', 0);


INSERT INTO screening_metadata (system_id, code, description, version)
VALUES ('b720d937-e512-49a8-a02e-a540ed57f72e', 'TD', 'Skjerming tittel dokumentbeskrivelse', 0);


INSERT INTO screening_metadata (system_id, code, description, version)
VALUES (1'06706ffe-acd9-46c6-a67f-0a8733b2de06', 'MT', 'Skjerming merknadstekst', 0);


INSERT INTO screening_metadata (system_id, code, description, version)
VALUES (1'2f68901a-a60a-4212-8b6a-7505c02d7104', 'M', 'Midlertidig skjerming', 0);

/* case_status / saksstatus*/
INSERT INTO case_status(system_id, code, description, version)
VALUES ('33f1e0f1-ba44-4c9b-9493-28998c4f9fe4', 'B', 'Under behandling', 0);


INSERT INTO case_status(system_id, code, description, version)
VALUES ('985a4607-0a24-4427-91e0-95438d6a2529', 'A', 'Avsluttet', 0);


INSERT INTO case_status(system_id, code, description, version)
VALUES ('51f2d7c2-8146-4735-94c2-543d3cc17263', 'U', 'Utgår', 0);


INSERT INTO case_status(system_id, code, description, version)
VALUES ('a23b0a12-5e02-4edf-b75b-896a7744d7ca', 'R', 'Opprettet av saksbehandler', 0);


INSERT INTO case_status(system_id, code, description, version)
VALUES ('0c9e8e57-1a5a-4c11-9f1b-5d646b6a683f', 'S', 'Avsluttet av saksbehandler', 0);


INSERT INTO case_status(system_id, code, description, version)
VALUES ('d26041b1-50cd-4a8b-9431-0833dfaa45d3', 'P', 'Unntatt prosesstyring', 0);


INSERT INTO case_status(system_id, code, description, version)
VALUES ('6cc5cca6-8ae5-4a6d-a7d6-9383067a38a1', 'F', 'Ferdig fra saksbehandler', 0);

/* variant_format / variantformat */
INSERT INTO variant_format(system_id, code, description, version)
VALUES ('04c49db4-5a6f-47a9-a763-d7d3d6fee2a2', 'P', 'Produksjonsformat', 0);


INSERT INTO variant_format(system_id, code, description, version)
VALUES ('0998cf74-d352-4aca-b8d7-ce6deba18ea4', 'A', 'Arkivformat', 0);


INSERT INTO variant_format(system_id, code, description, version)
VALUES ('3a89bb13-e796-4f44-a2ee-509fbbbef0aa', 'O', 'Dokument hvor deler av innholdet er skjermet', 0);

