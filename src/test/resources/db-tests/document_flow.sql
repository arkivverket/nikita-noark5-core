-- Record / RegistryEntry
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('8f6b084f-d727-4b46-bbe2-14bed2135fa9', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);

-- Record / RecordNote
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('11b32a9e-802d-43de-9bb5-c951e3bbe95b', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);

-- DocumentFlow / RegistryEntry
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('cf0f41f7-65e8-4471-85f3-18ff223cbdb0', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);

-- DocumentFlow / RecordNote
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('e9eef604-c540-456e-bf46-62ddddaa68af', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);

insert into as_record (system_id, title, description, record_file_id)
values ('8f6b084f-d727-4b46-bbe2-14bed2135fa9', 'test title record', 'test description record',
        'f1677c47-99e1-42a7-bda2-b0bbc64841b7');

INSERT INTO sa_registry_entry (system_id, document_date, due_date, freedom_assessment_date, number_of_attachments,
                               record_date, record_sequence_number, record_year, records_management_unit,
                               registry_entry_number, registry_entry_status_code, registry_entry_status_code_name,
                               registry_entry_type_code, registry_entry_type_code_name, sent_date)
VALUES ('8f6b084f-d727-4b46-bbe2-14bed2135fa9', '2019-04-08 00:00:00', '2019-04-08 00:00:00', '2019-04-08 00:00:00', 1,
        '2019-04-08 00:00:00', 2, '2021', 'records_management_unit ', '22', 'J ', 'Journalført ', 'I',
        'Inngående dokument', '2019-04-08 00:00:00');

insert into as_record (system_id, title, description, record_file_id)
values ('11b32a9e-802d-43de-9bb5-c951e3bbe95b', 'test title record', 'test description record',
        'f1677c47-99e1-42a7-bda2-b0bbc64841b7');

INSERT INTO sa_record_note (system_id, document_date, due_date, freedom_assessment_date,
                            number_of_attachments, sent_date)
VALUES ('11b32a9e-802d-43de-9bb5-c951e3bbe95b', '2019-04-08 00:00:00', '2019-04-08 00:00:00', '2019-04-08 00:00:00', 1,
        '2019-04-08 00:00:00');


INSERT INTO as_document_flow
(system_id,
 flow_comment,
 flow_from,
 flow_received_date,
 flow_sent_date,
 flow_status_code,
 flow_status_code_name,
 flow_to,
    --reference_flow_from,
    --reference_flow_to,
    --document_flow_flow_from_id,
    --document_flow_flow_to_id,
 document_flow_registry_entry_id)
VALUES ('cf0f41f7-65e8-4471-85f3-18ff223cbdb0',
        'flow_comment',
        'flow_from',
        '2019-04-08 00:00:00',
        '2019-04-08 00:00:00',
        'S',
        'Sendt tilbake til saksbehandler med kommentarer',
        'flow_to',
           --'reference_flow_from',
           -- 'reference_flow_to',
--    'document_flow_flow_from_id',
--    'document_flow_flow_to_id',
        '8f6b084f-d727-4b46-bbe2-14bed2135fa9');
