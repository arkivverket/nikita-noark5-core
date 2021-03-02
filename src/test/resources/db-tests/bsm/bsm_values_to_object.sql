delete
from bsm_base
where 1;
insert into bsm_base (systemid, owned_by, data_type, value_name, string_value, fk_bsm_file_id)
values ('eebe2161-3fcc-4b6d-9920-0b570b00bed8', 'anonymousUser', 'string', 'ppt-v1:skolekontakt', 'Harald Harfarge',
        '43d305de-b3c8-4922-86fd-45bd26f3bf01');
insert into bsm_base (systemid, owned_by, data_type, value_name, boolean_value, fk_bsm_file_id)
values ('d738ca59-6211-478a-8851-eb1686633d3c', 'anonymousUser', 'boolean', 'ppt-v1:avsluttet', true,
        '43d305de-b3c8-4922-86fd-45bd26f3bf01');
insert into bsm_base (systemid, owned_by, data_type, value_name, integer_value, fk_bsm_file_id)
values ('57d113b9-7a8d-4f78-9664-7d0681ef3188', 'anonymousUser', 'integer', 'ppt-v1:antallDager', 8,
        '43d305de-b3c8-4922-86fd-45bd26f3bf01');
insert into bsm_base (systemid, owned_by, data_type, value_name, double_value, fk_bsm_file_id)
values ('055a8e2d-5f4c-4530-906a-ae566157bd3e', 'anonymousUser', 'double', 'ppt-v1:snitt', 4.5,
        '43d305de-b3c8-4922-86fd-45bd26f3bf01');
insert into bsm_base (systemid, owned_by, data_type, value_name, offsetdatetime_value, fk_bsm_file_id)
values ('21162dfc-7266-4af3-befa-90e66794cf7b', 'anonymousUser', 'date', 'ppt-v1:datohenvist',
        '2020-01-02 00:00:00+01:00',
        '43d305de-b3c8-4922-86fd-45bd26f3bf01');
insert into bsm_base (systemid, owned_by, data_type, value_name, offsetdatetime_value, fk_bsm_file_id)
values ('4c3b661c-25e8-4169-8f59-c6b2723daf22', 'anonymousUser', 'datetime', 'ppt-v1:meldingstidspunkt',
        '2020-07-01 22:25:06+01:00', '43d305de-b3c8-4922-86fd-45bd26f3bf01');