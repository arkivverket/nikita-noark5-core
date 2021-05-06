-- Record / RegistryEntry
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('16c2f0af-c684-49e8-bc4f-bc21de1578bb', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);

-- File / CaseFile
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('ccefaca8-4eda-4164-84c8-4f2176312f29', '2020-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);

-- Precedence for RegistryEntry
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('137dbc34-5669-4fdf-867e-985c3f1de60f', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);

-- Precedence for CaseFile
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('e23cc0fe-d03f-4d6b-87e9-898deeb7d7da', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);

insert into as_record (system_id, title, description, record_file_id)
values ('16c2f0af-c684-49e8-bc4f-bc21de1578bb', 'test title record', 'test description record',
        'f1677c47-99e1-42a7-bda2-b0bbc64841b7');

INSERT INTO sa_registry_entry (system_id, document_date, due_date, freedom_assessment_date, number_of_attachments,
                               record_date, record_sequence_number, record_year, records_management_unit,
                               registry_entry_number, registry_entry_status_code, registry_entry_status_code_name,
                               registry_entry_type_code, registry_entry_type_code_name, sent_date)
VALUES ('16c2f0af-c684-49e8-bc4f-bc21de1578bb', '2019-04-08 00:00:00', '2019-04-08 00:00:00', '2019-04-08 00:00:00', 1,
        '2019-04-08 00:00:00', 2, '2021', 'records_management_unit ', '22', 'J ', 'Journalført ', 'I',
        'Inngående dokument', '2019-04-08 00:00:00');

INSERT INTO sa_precedence(system_id, title, precedence_approved_by, precedence_approved_date, precedence_authority,
                          precedence_date, precedence_status_code, precedence_status_code_name, source_of_law)
VALUES ('137dbc34-5669-4fdf-867e-985c3f1de60f', 'title of precedence', 'admin@example.com', '2019-04-08 00:00:00',
        'precedence_authority: ', '2019-04-08 00:00:00', 'G', 'Gjeldende', 'source of law of precedence');

INSERT INTO sa_registry_entry_precedence(f_pk_record_id, f_pk_precedence_id)
VALUES ('16c2f0af-c684-49e8-bc4f-bc21de1578bb', '137dbc34-5669-4fdf-867e-985c3f1de60f');

insert into as_file (system_id, title, description, file_series_id)
values ('ccefaca8-4eda-4164-84c8-4f2176312f29', 'test title charlie', 'test description 2',
        'f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d');

insert into sa_case_file (system_id, case_date, case_responsible, case_sequence_number, case_status_code,
                          case_status_code_name, case_year, case_file_administrative_unit_id)
values ('ccefaca8-4eda-4164-84c8-4f2176312f29', '2021-04-24 14:55:13', 'admin@example.com', '1', 'R',
        'Opprettet av saksbehandler', '2021', 'c3d4affc-66a0-4663-b63a-6ecc4f3d6009');

INSERT INTO sa_precedence(system_id, title, precedence_approved_by, precedence_approved_date, precedence_authority,
                          precedence_date, precedence_status_code, precedence_status_code_name, source_of_law)
VALUES ('e23cc0fe-d03f-4d6b-87e9-898deeb7d7da', 'title of precedence', 'admin@example.com', '2019-04-08 00:00:00',
        'precedence_authority: ', '2019-04-08 00:00:00', 'G', 'Gjeldende', 'source of law of precedence');

INSERT INTO sa_case_file_precedence(f_pk_case_file_id, f_pk_precedence_id)
VALUES ('ccefaca8-4eda-4164-84c8-4f2176312f29', 'e23cc0fe-d03f-4d6b-87e9-898deeb7d7da');
