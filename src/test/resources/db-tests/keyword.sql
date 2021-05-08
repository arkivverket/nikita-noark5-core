-- Record
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('99c2f1af-dd84-19e8-dd4f-cc21fe1578ff', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_record (system_id, title, description, record_file_id)
values ('99c2f1af-dd84-19e8-dd4f-cc21fe1578ff', 'test title record', 'test description record',
        'f1677c47-99e1-42a7-bda2-b0bbc64841b7');
-- File
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('48c81365-7193-4481-bc84-b025248fb310', '2020-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_file (system_id, title, description, file_series_id)
values ('48c81365-7193-4481-bc84-b025248fb310', 'test title bravo', 'test description 2',
        'f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d');
-- Class
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('596c85fb-a6c4-4381-86b4-81df05234028', '2020-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_class (system_id, title, class_id, class_classification_system_id)
values ('596c85fb-a6c4-4381-86b4-81df05234028', 'test title class', 'class_id_value',
        '2d0b2dc1-f3bb-4239-bf04-582b1085581c');
-- Keyword for Record
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('6bcd4138-3d7b-46d6-9f93-cb1565730212', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
INSERT INTO as_keyword (keyword, system_id)
VALUES ('keywordX for record', '6bcd4138-3d7b-46d6-9f93-cb1565730212');
-- Keyword for File
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('81cea881-1203-4e3f-943c-c0294e81e528', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
INSERT INTO as_keyword (keyword, system_id)
VALUES ('keywordX for file', '81cea881-1203-4e3f-943c-c0294e81e528');
-- Keyword for Class
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('1e29bf5c-f6d8-4b5b-aa6e-b2272e34a2ad', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
INSERT INTO as_keyword (keyword, system_id)
VALUES ('keywordX for class', '1e29bf5c-f6d8-4b5b-aa6e-b2272e34a2ad');
-- Join table associations
INSERT INTO as_class_keyword (f_pk_class_id, f_pk_keyword_id)
VALUES ('596c85fb-a6c4-4381-86b4-81df05234028', '1e29bf5c-f6d8-4b5b-aa6e-b2272e34a2ad');
INSERT INTO as_record_keyword (f_pk_record_id, f_pk_keyword_id)
VALUES ('99c2f1af-dd84-19e8-dd4f-cc21fe1578ff', '6bcd4138-3d7b-46d6-9f93-cb1565730212');
INSERT INTO as_file_keyword (f_pk_file_id, f_pk_keyword_id)
VALUES ('48c81365-7193-4481-bc84-b025248fb310', '81cea881-1203-4e3f-943c-c0294e81e528');
