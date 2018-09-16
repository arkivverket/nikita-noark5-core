/*
insert into nikita_user (pk_user_id, system_id, username, firstname, lastname, password, account_non_locked, credentials_non_expired, account_non_expired, enabled, account_created_date) values (1, '56a9dcc2-bb0a-4812-ad75-b5f8e096aaed', 'admin@example.com', 'Frank', 'Grimes', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', true, true, true, true, '2016-08-08 00:00:00');
insert into nikita_user (pk_user_id, system_id, username, firstname, lastname, password, account_non_locked, credentials_non_expired, account_non_expired, enabled, account_created_date) values (2, '65a66580-9c3e-438d-92a0-8cba7862828e', 'recordkeeper@example.com', 'Moe', 'Szyslak', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', true, true, true, true, '2016-08-08 00:00:00');
insert into nikita_user (pk_user_id, system_id, username, firstname, lastname, password, account_non_locked, credentials_non_expired, account_non_expired, enabled, account_created_date) values (3, 'e0408691-1128-46b6-ab33-170eaf3f70f1', 'casehandlercase_handler@example.com', 'Rainier', 'Wolfcastle', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', true, true, true, true, '2016-08-08 00:00:00');
insert into nikita_user (pk_user_id, system_id, username, firstname, lastname, password, account_non_locked, credentials_non_expired, account_non_expired, enabled, account_created_date) values (4, 'f0419f0c-1b67-4ef0-b0d6-962f3812d887', 'leaderleader@example.com', 'Johnny', 'Tightlips', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', true, true, true, true, '2016-08-08 00:00:00');
insert into nikita_user (pk_user_id, system_id, username, firstname, lastname, password, account_non_locked, credentials_non_expired, account_non_expired, enabled, account_created_date) values (5, '195ba547-9a74-4ad8-ba22-b783680cd4ff', 'gjest@example.com', 'Cletus', 'Spuckler', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', true, true, true, true, '2016-08-08 00:00:00');

INSERT INTO nikita_authority (id, authority_name) VALUES (1, 'RECORDS_MANAGER');
INSERT INTO nikita_authority (id, authority_name) VALUES (2, 'RECORDS_KEEPER');
INSERT INTO nikita_authority (id, authority_name) VALUES (3, 'CASE_HANDLER');
INSERT INTO nikita_authority (id, authority_name) VALUES (4, 'LEADER');
INSERT INTO nikita_authority (id, authority_name) VALUES (5, 'GUEST');

insert into nikita_user_authority (f_pk_user_id, authority_id) values (1, 1);
insert into nikita_user_authority (f_pk_user_id, authority_id) values (2, 2);
insert into nikita_user_authority (f_pk_user_id, authority_id) values (3, 3);
insert into nikita_user_authority (f_pk_user_id, authority_id) values (4, 4);
insert into nikita_user_authority (f_pk_user_id, authority_id) values (5, 5);
*/
