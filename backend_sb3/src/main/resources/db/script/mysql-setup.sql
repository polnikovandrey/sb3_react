drop database if exists polling;
drop user if exists `polling_admin`@`%`;
drop user if exists `polling_user`@`%`;
create database if not exists polling character set utf8mb4 collate utf8mb4_unicode_ci;
create user if not exists `polling_admin`@`%` identified with mysql_native_password by 'password';
grant select, insert, update, delete, create, drop, references, index, alter, execute, create view, show view, create routine, alter routine, event, trigger on `polling`.* to `polling_admin`@`%`;
create user if not exists `polling_user`@`%` identified with mysql_native_password by 'password';
grant select, insert, update, delete, show view on `polling`.* to `polling_user`@`%`;
flush privileges;