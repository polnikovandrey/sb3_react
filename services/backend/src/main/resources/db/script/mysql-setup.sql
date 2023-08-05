drop database if exists sb3_react;
drop user if exists `sb3_react_admin`@`%`;
drop user if exists `sb3_react_user`@`%`;
create database if not exists sb3_react character set utf8mb4 collate utf8mb4_unicode_ci;
create user if not exists `sb3_react_admin`@`%` identified with mysql_native_password by 'password';
grant select, insert, update, delete, create, drop, references, index, alter, execute, create view, show view, create routine, alter routine, event, trigger on `sb3_react`.* to `sb3_react_admin`@`%`;
create user if not exists `sb3_react_user`@`%` identified with mysql_native_password by 'password';
grant select, insert, update, delete, show view on `sb3_react`.* to `sb3_react_user`@`%`;
flush privileges;