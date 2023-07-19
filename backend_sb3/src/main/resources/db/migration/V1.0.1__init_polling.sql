create table polls
(
    id                   bigint       not null auto_increment primary key,
    question             varchar(140) not null,
    expiration_date_time datetime(6)  not null,
    created_at           datetime(6)  not null,
    created_by           bigint       not null,
    updated_at           datetime(6)  not null,
    updated_by           bigint       not null
);
create table choices
(
    id      bigint      not null auto_increment primary key,
    poll_id bigint      not null,
    text    varchar(40) not null,
    constraint fk_choices_polls foreign key (poll_id) references polls (id)
);
create table votes
(
    id         bigint      not null auto_increment primary key,
    user_id    bigint      not null,
    choice_id  bigint      not null,
    poll_id    bigint      not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    constraint poll_user unique (poll_id, user_id),
    constraint fk_votes_polls foreign key (poll_id) references polls (id),
    constraint fk_votes_users foreign key (user_id) references users (id)
);