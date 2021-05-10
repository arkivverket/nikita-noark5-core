-- Fonds
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('388a1d7e-de4d-4da5-bbd8-5a0f1b9c8843', '2020-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_fonds(system_id, title)
values ('388a1d7e-de4d-4da5-bbd8-5a0f1b9c8843', 'test title fonds');
-- Series
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('312ea7a2-f570-4183-a4b4-b30adf3b62dd', '2020-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_series(system_id, title, series_status_code, series_status_code_name, series_fonds_id)
values ('312ea7a2-f570-4183-a4b4-b30adf3b62dd', 'test title series', 'O', 'Opprettet',
        '3318a63f-11a7-4ec9-8bf1-4144b7f281cf');
-- File
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('a9145ece-13ee-4d51-a880-0879ed225302', '2020-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_file (system_id, title, description, file_series_id)
values ('a9145ece-13ee-4d51-a880-0879ed225302', 'test title bravo', 'test description 2',
        'f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d');
-- Record
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('4b063ea2-227e-4a39-82bb-66d590ce4ebf', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_record (system_id, title, description, record_file_id)
values ('4b063ea2-227e-4a39-82bb-66d590ce4ebf', 'test title record', 'test description record',
        'f1677c47-99e1-42a7-bda2-b0bbc64841b7');
-- StorageLocation for Fonds
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('4fda62eb-e87c-4f50-a895-a6547694faf4', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
INSERT INTO as_storage_location (storage_location, system_id)
VALUES ('storage_locationX for series', '4fda62eb-e87c-4f50-a895-a6547694faf4');
-- StorageLocation for Series
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('acd24eeb-92eb-48a4-925c-8418aa54b538', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
INSERT INTO as_storage_location (storage_location, system_id)
VALUES ('storage_locationX for series', 'acd24eeb-92eb-48a4-925c-8418aa54b538');
-- StorageLocation for File
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('81cea881-1203-4e3f-943c-c0294e81e528', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
INSERT INTO as_storage_location (storage_location, system_id)
VALUES ('storage_locationX for file', '81cea881-1203-4e3f-943c-c0294e81e528');
-- StorageLocation for Record
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('7f37fcff-b402-44dc-b4dc-a184692f95aa', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
INSERT INTO as_storage_location (storage_location, system_id)
VALUES ('storage_locationX for record', '7f37fcff-b402-44dc-b4dc-a184692f95aa');
-- Join table associations
INSERT INTO as_fonds_storage_location (f_pk_fonds_id, f_pk_storage_location_id)
VALUES ('388a1d7e-de4d-4da5-bbd8-5a0f1b9c8843', '4fda62eb-e87c-4f50-a895-a6547694faf4');
INSERT INTO as_series_storage_location (f_pk_series_id, f_pk_storage_location_id)
VALUES ('312ea7a2-f570-4183-a4b4-b30adf3b62dd', 'acd24eeb-92eb-48a4-925c-8418aa54b538');
INSERT INTO as_file_storage_location (f_pk_file_id, f_pk_storage_location_id)
VALUES ('a9145ece-13ee-4d51-a880-0879ed225302', '81cea881-1203-4e3f-943c-c0294e81e528');
INSERT INTO as_record_storage_location (f_pk_record_id, f_pk_storage_location_id)
VALUES ('4b063ea2-227e-4a39-82bb-66d590ce4ebf', '7f37fcff-b402-44dc-b4dc-a184692f95aa');
