delete
from as_postal_address
where 1;
delete
from as_residing_address
where 1;
delete
from as_contact_information
where 1;
delete
from as_record_part
where 1;
delete
from as_document_description
where 1;
delete
from as_part_person
where 1;
delete
from as_correspondence_part_person
where 1;
delete
from as_record
where 1;
delete
from as_file
where 1;
delete
from as_series
where 1;
delete
from as_fonds
where 1;
insert into as_fonds (owned_by, system_id, title, description, created_date, created_by, version)
values ('anonymousUser', '3318a63f-11a7-4ec9-8bf1-4144b7f281cf', 'test title alpha', 'test description 1',
        '2019-04-08 00:00:00', 'admin', 0);
insert into as_series (owned_by, system_id, title, description, created_date, created_by, series_status_code,
                       series_status_code_name, series_fonds_id, version)
values ('anonymousUser', 'f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d', 'test title bravo', 'test description 2',
        '2019-04-08 00:00:00', 'admin', 'O', 'Opprettet', '3318a63f-11a7-4ec9-8bf1-4144b7f281cf', 0);
insert into as_file (owned_by, system_id, title, description, created_date, created_by, file_series_id, version)
values ('anonymousUser', 'f1677c47-99e1-42a7-bda2-b0bbc64841b7', 'test title bravo', 'test description 2',
        '2019-04-08 00:00:00', 'admin', 'f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d', 0);
insert into as_file (owned_by, system_id, title, description, created_date, created_by, file_series_id, version)
values ('anonymousUser', '43d305de-b3c8-4922-86fd-45bd26f3bf01', 'test title charlie', 'test description 2',
        '2020-04-08 00:00:00', 'admin', 'f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d', 0);
insert into as_record (owned_by, system_id, title, description, created_date, created_by, record_file_id, version)
values ('anonymousUser', 'dc600862-3298-4ec0-8541-3e51fb900054', 'test title record', 'test description record',
        '2019-04-08 00:00:00', 'admin', 'f1677c47-99e1-42a7-bda2-b0bbc64841b7', 0);

insert into as_document_description (owned_by, system_id, title,
                                     description, document_type_code,
                                     created_date, created_by, document_type_code_name,
                                     associated_with_record_as_code, associated_with_record_as_code_name,
                                     document_number, association_date)
values ('anonymousUser', '66b92e78-b75d-4b0f-9558-4204ab31c2d1', 'test title bravo',
        'test description bravo', 'B',
        '2019-04-08 00:00:00', 'admin@example.com', 'Brev',
        'H', 'Hoveddokument', 1, '2020-04-08');


insert into as_correspondence_part_person(system_id, created_by, created_date, last_modified_by, last_modified_date,
                                          owned_by, version, correspondence_part_type_code,
                                          correspondence_part_type_code_name, f_pk_record_id)
values ('7f000101-730c-1c94-8173-0c0ded71003c', 'anonymousUser', '2020-07-01 22:25:06', 'anonymousUser',
        '2020-07-01 22:25:32', 'anonymousUser', '1', 'EA', 'Avsender', 'dc600862-3298-4ec0-8541-3e51fb900054');
insert into as_part_person(system_id, created_by, created_date, last_modified_by, last_modified_date, owned_by, version,
                           part_role_code, part_role_code_name, title)
values ('8131049d-dcac-43d8-bee4-656e72842da9', 'anonymousUser', '2020-07-01 22:25:06', 'anonymousUser',
        '2020-07-01 22:25:32', 'anonymousUser', '1', 'KLI', 'Klient', 'title');
insert into as_record_part(f_pk_record_id, f_pk_part_id)
values ('dc600862-3298-4ec0-8541-3e51fb900054', 'dc600862-3298-4ec0-8541-3e51fb900054');
