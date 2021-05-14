-- Record 1
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('ba848de1-e1d7-42fe-9ea1-77127db206fd', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_record (system_id, title, description, record_file_id)
values ('ba848de1-e1d7-42fe-9ea1-77127db206fd', 'test title record', 'test description record',
        'f1677c47-99e1-42a7-bda2-b0bbc64841b7');
-- Record 2
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('92e53dff-bd83-4a39-8485-9e34942a1583', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_record (system_id, title, description, record_file_id)
values ('92e53dff-bd83-4a39-8485-9e34942a1583', 'test title record', 'test description record',
        'f1677c47-99e1-42a7-bda2-b0bbc64841b7');
-- Record 3
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('bde603e8-ed29-43a3-8b38-52b796ca0b73', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_record (system_id, title, description, record_file_id)
values ('bde603e8-ed29-43a3-8b38-52b796ca0b73', 'test title record', 'test description record',
        'f1677c47-99e1-42a7-bda2-b0bbc64841b7');
-- File 1
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('7e857df9-d453-48bf-ac4c-19beebf81091', '2020-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_file (system_id, title, description, file_series_id)
values ('7e857df9-d453-48bf-ac4c-19beebf81091', 'test title bravo', 'test description 2',
        'f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d');
-- File 2
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('b7da0a57-2983-4389-a65e-d87343da78eb', '2020-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_file (system_id, title, description, file_series_id)
values ('b7da0a57-2983-4389-a65e-d87343da78eb', 'test title bravo', 'test description 2',
        'f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d');
-- File 3
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('01d0ef0b-b043-457e-8a8b-4ac2128a6637', '2020-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_file (system_id, title, description, file_series_id)
values ('01d0ef0b-b043-457e-8a8b-4ac2128a6637', 'test title bravo', 'test description 2',
        'f1102ae8-6c4c-4d93-aaa5-7c6220e50c4d');
-- Class 1
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('06ae4940-3d86-4a69-a59f-f1a27b26f2a8', '2020-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_class (system_id, title, class_id, class_classification_system_id)
values ('06ae4940-3d86-4a69-a59f-f1a27b26f2a8', 'test title FROM class', 'class_id_value',
        '2d0b2dc1-f3bb-4239-bf04-582b1085581c');
-- Class 2
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('bd303a71-48e9-4c5d-9ee8-7df7d514d4e1', '2020-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_class (system_id, title, class_id, class_classification_system_id)
values ('bd303a71-48e9-4c5d-9ee8-7df7d514d4e1', 'test title TO class', 'class_id_value',
        '2d0b2dc1-f3bb-4239-bf04-582b1085581c');
-- Class 2
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('d6b0d4ba-3b2d-4896-84fa-5d9ba1756cf4', '2020-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into as_class (system_id, title, class_id, class_classification_system_id)
values ('d6b0d4ba-3b2d-4896-84fa-5d9ba1756cf4', 'test title TO class', 'class_id_value',
        '2d0b2dc1-f3bb-4239-bf04-582b1085581c');
