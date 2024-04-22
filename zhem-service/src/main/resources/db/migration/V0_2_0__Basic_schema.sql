create table if not exists zhem.files (
    file_id bigserial primary key,
    name varchar(256) not null,
    type varchar(48) not null,
    created_at timestamp default now(),
    updated_at timestamp default now()
)