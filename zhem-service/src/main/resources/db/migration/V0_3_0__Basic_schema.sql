drop table if exists zhem.examples;

create table if not exists zhem.files
(
    file_id    bigserial primary key,
    path       varchar(256) not null check ( length(trim(path)) >= 2 ),
    type       varchar(32)  not null check ( length(trim(type)) >= 2 ),
    created_at timestamp default now(),
    updated_at timestamp default now()
);

create table if not exists zhem.posts
(
    post_id    bigserial primary key,
    file_id    bigint not null references zhem.files (file_id),
    content    varchar(256) check ( length(trim(content)) > 0 ),
    created_at timestamp default now(),
    updated_at timestamp default now()
);