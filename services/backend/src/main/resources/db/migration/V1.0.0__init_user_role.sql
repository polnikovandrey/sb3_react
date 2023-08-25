drop table if exists roles;
create table roles
(
    id   bigint      not null auto_increment primary key,
    name varchar(20) not null unique
);
insert into roles(name)
values ('ROLE_ADMIN'),
       ('ROLE_USER');
drop table if exists users;
create table users
(
    id              bigint       not null auto_increment primary key,
    username        varchar(20)  not null unique,
    email           varchar(40)  not null unique,
    password        varchar(100) not null,
    first_name      varchar(20)  not null,
    last_name       varchar(20)  not null,
    middle_name     varchar(20)  not null default '',
    email_confirmed boolean      not null default false,
    created_at      datetime(6)  not null,
    updated_at      datetime(6)  not null,
    version         bigint       not null
);
drop table if exists user_roles;
create table user_roles
(
    role_id bigint not null,
    user_id bigint not null,
    primary key (role_id, user_id),
    constraint fk_user_roles_roles foreign key (role_id) references roles (id),
    constraint fk_user_roles_users foreign key (user_id) references users (id)
);