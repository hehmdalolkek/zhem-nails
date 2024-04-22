create table if not exists zhem.examples
(
    example_id bigserial primary key,
    title      varchar(32)  not null check ( length(trim(title)) >= 2 ),
    file_name  varchar(256) not null,
    created_at timestamp default now(),
    updated_at timestamp default now()
);