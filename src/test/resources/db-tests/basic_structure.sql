insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('3318a63f-11a7-4ec9-8bf1-4144b7f281cf', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('f1677c47-99e1-42a7-bda2-b0bbc64841b7', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('43d305de-b3c8-4922-86fd-45bd26f3bf01', '2020-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('dc600862-3298-4ec0-8541-3e51fb900054', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('7f000101-730c-1c94-8173-0c0ded71003c', '2020-07-01 22:25:06', 'admin@example.com', 'admin@example.com', 0);
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('8131049d-dcac-43d8-bee4-656e72842da9', '2020-07-01 22:25:06', 'admin@example.com', 'admin@example.com', 0);
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('66b92e78-b75d-4b0f-9558-4204ab31c2d1', '2020-07-01 22:25:06', 'admin@example.com', 'admin@example.com', 0);
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('ebcefc44-73e5-485e-94c9-1b210359c125', '2020-07-01 22:25:06', 'admin@example.com', 'admin@example.com', 0);

insert into as_fonds (system_id, title, description)
values ('3318a63f-11a7-4ec9-8bf1-4144b7f281cf', 'test title alpha', 'test description 1');

insert into as_series (system_id, title, description, series_status_code, series_status_code_name, series_fonds_id)
values ('f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d', 'test title bravo', 'test description 2', 'O', 'Opprettet',
        '3318a63f-11a7-4ec9-8bf1-4144b7f281cf');

insert into as_file (system_id, title, description, file_series_id)
values ('f1677c47-99e1-42a7-bda2-b0bbc64841b7', 'test title bravo', 'test description 2',
        'f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d');

insert into as_file (system_id, title, description, file_series_id)
values ('43d305de-b3c8-4922-86fd-45bd26f3bf01', 'test title charlie', 'test description 2',
        'f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d');

insert into as_record (system_id, title, description, record_file_id)
values ('dc600862-3298-4ec0-8541-3e51fb900054', 'test title record', 'test description record',
        'f1677c47-99e1-42a7-bda2-b0bbc64841b7');

insert into as_document_description (system_id, title, description, document_type_code, document_type_code_name,
                                     associated_with_record_as_code, associated_with_record_as_code_name,
                                     document_number, association_date)
values ('66b92e78-b75d-4b0f-9558-4204ab31c2d1', 'test title bravo', 'test description bravo', 'B', 'Brev',
        'H', 'Hoveddokument', 1, '2020-04-08');

insert into as_record_document_description (f_pk_record_id, f_pk_document_description_id)
values ('dc600862-3298-4ec0-8541-3e51fb900054', '66b92e78-b75d-4b0f-9558-4204ab31c2d1');


insert into as_correspondence_part(system_id, correspondence_part_type_code,
                                   correspondence_part_type_code_name, f_pk_record_id)
values ('7f000101-730c-1c94-8173-0c0ded71003c', 'EA', 'Avsender', 'dc600862-3298-4ec0-8541-3e51fb900054');

insert into as_correspondence_part_person(system_id, name, social_security_number)
values ('7f000101-730c-1c94-8173-0c0ded71003c', 'Hans Gruber II', '987654321369852');

insert into as_part(system_id, name, part_role_code, part_role_code_name, title)
values ('8131049d-dcac-43d8-bee4-656e72842da9', 'Hans Gruber', 'KLI', 'Klient', 'title');

insert into as_part_person(system_id, social_security_number)
values ('8131049d-dcac-43d8-bee4-656e72842da9', '1234567895655');

insert into as_part(system_id, name, part_role_code, part_role_code_name, title)
values ('ebcefc44-73e5-485e-94c9-1b210359c125', 'Hans Gruber', 'ADV', 'Advokat', 'title');

insert into as_part_unit(system_id, organisation_number)
values ('ebcefc44-73e5-485e-94c9-1b210359c125', '02020202022');

insert into as_file_part(f_pk_file_id, f_pk_part_id)
values ('f1677c47-99e1-42a7-bda2-b0bbc64841b7', 'ebcefc44-73e5-485e-94c9-1b210359c125');

insert into as_file_part(f_pk_file_id, f_pk_part_id)
values ('43d305de-b3c8-4922-86fd-45bd26f3bf01', '8131049d-dcac-43d8-bee4-656e72842da9');

insert into as_record_part(f_pk_record_id, f_pk_part_id)
values ('dc600862-3298-4ec0-8541-3e51fb900054', '8131049d-dcac-43d8-bee4-656e72842da9');
