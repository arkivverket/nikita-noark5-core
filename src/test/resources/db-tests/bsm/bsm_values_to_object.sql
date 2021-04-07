insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('eebe2161-3fcc-4b6d-9920-0b570b00bed8', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('ab4f2105-446a-4283-b129-319bef66aa87', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('d738ca59-6211-478a-8851-eb1686633d3c', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('57d113b9-7a8d-4f78-9664-7d0681ef3188', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('055a8e2d-5f4c-4530-906a-ae566157bd3e', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('21162dfc-7266-4af3-befa-90e66794cf7b', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('4c3b661c-25e8-4169-8f59-c6b2723daf22', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);
insert into system_id_entity(system_id, created_date, created_by, owned_by, version)
values ('42f69f1e-0608-4213-979e-e763cb0b4176', '2019-04-08 00:00:00', 'admin@example.com', 'admin@example.com', 0);


insert into bsm_base (system_id, data_type, value_name, string_value, is_null_value, fk_bsm_file_id)
values ('eebe2161-3fcc-4b6d-9920-0b570b00bed8', 'string', 'ppt-v1:skolekontakt', 'Harald Harfarge', false,
        '43d305de-b3c8-4922-86fd-45bd26f3bf01');
insert into bsm_base (system_id, data_type, value_name, string_value, fk_bsm_file_id)
values ('ab4f2105-446a-4283-b129-319bef66aa87', 'string', 'ppt-v1:skolekontakt', null,
        'f1677c47-99e1-42a7-bda2-b0bbc64841b7');
insert into bsm_base (system_id, data_type, value_name, boolean_value, is_null_value, fk_bsm_file_id)
values ('d738ca59-6211-478a-8851-eb1686633d3c', 'boolean', 'ppt-v1:sakferdig', true, false,
        '43d305de-b3c8-4922-86fd-45bd26f3bf01');
insert into bsm_base (system_id, data_type, value_name, integer_value, is_null_value, fk_bsm_file_id)
values ('57d113b9-7a8d-4f78-9664-7d0681ef3188', 'integer', 'ppt-v1:antallDagerVurdert', 8, false,
        '43d305de-b3c8-4922-86fd-45bd26f3bf01');
insert into bsm_base (system_id, data_type, value_name, double_value, is_null_value, fk_bsm_file_id)
values ('055a8e2d-5f4c-4530-906a-ae566157bd3e', 'double', 'ppt-v1:snittKarakter', 1.2, false,
        '43d305de-b3c8-4922-86fd-45bd26f3bf01');
insert into bsm_base (system_id, data_type, value_name, offsetdatetime_value, is_null_value, fk_bsm_file_id)
values ('21162dfc-7266-4af3-befa-90e66794cf7b', 'date', 'ppt-v1:datohenvist',
        '2020-07-01 00:00:00+02:00', false, '43d305de-b3c8-4922-86fd-45bd26f3bf01');
insert into bsm_base (system_id, data_type, value_name, offsetdatetime_value, is_null_value, fk_bsm_file_id)
values ('4c3b661c-25e8-4169-8f59-c6b2723daf22', 'datetime', 'ppt-v1:datotidvedtakferdig',
        '2020-07-01 23:25:06+02:00', false, '43d305de-b3c8-4922-86fd-45bd26f3bf01');
insert into bsm_base (system_id, data_type, value_name, uri_value, is_null_value, fk_bsm_file_id)
values ('42f69f1e-0608-4213-979e-e763cb0b4176', 'uri', 'ppt-v1:refSkole',
        'https://skole.eksempel.com', false, '43d305de-b3c8-4922-86fd-45bd26f3bf01');
