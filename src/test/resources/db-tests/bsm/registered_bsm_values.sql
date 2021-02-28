delete
from md_bsm
where 1;
insert into md_bsm (system_id, attribute_name, created_date, created_by, datatype, outdated, description, source)
values ('5a14399e-b392-449d-88b8-54db81b6bd2d', 'ppt-v1:datohenvist', '2019-04-08 00:00:00+02:00', 'admin', 'date',
        false, 'description', 'source');
insert into md_bsm (system_id, attribute_name, created_date, created_by, datatype, outdated, description, source)
values ('7258d969-92bb-4587-bfa6-8d1132481819', 'ppt-v1:datotidvedtakferdig', '2019-04-08 00:00:00+02:00', 'admin',
        'datetime', false, 'description', 'source');
insert into md_bsm (system_id, attribute_name, created_date, created_by, datatype, outdated, description, source)
values ('45cd563b-b523-4c96-8bfa-0dcbddaddd6d', 'ppt-v1:sakferdig', '2019-04-08 00:00:00', 'admin', 'boolean', false,
        'description', 'source');
insert into md_bsm (system_id, attribute_name, created_date, created_by, datatype, outdated, description, source)
values ('a70c0c71-8e6c-43e2-a06d-53998441769b', 'ppt-v1:skolekontakt', '2019-04-08 00:00:00', 'admin', 'string', false,
        'description', 'source');
insert into md_bsm (system_id, attribute_name, created_date, created_by, datatype, outdated, description, source)
values ('146438e3-ad00-477c-aec8-85c5e9b25b54', 'ppt-v1:refSkole', '2019-04-08 00:00:00', 'admin', 'uri', false,
        'description', 'source');
insert into md_bsm (system_id, attribute_name, created_date, created_by, datatype, outdated, description, source)
values ('df478be4-e61d-4f27-aa3b-82336fde6503', 'ppt-v1:snittKarakter', '2019-04-08 00:00:00', 'admin', 'double', false,
        'description', 'source');
insert into md_bsm (system_id, attribute_name, created_date, created_by, datatype, outdated, description, source)
values ('fae7e19b-2be4-4c4b-afc6-18c373ca8526', 'ppt-v1:antallDagerVurdert', '2019-04-08 00:00:00', 'admin', 'integer',
        false, 'description', 'source');
