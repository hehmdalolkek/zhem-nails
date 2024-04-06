create schema if not exists zhem;

create table if not exists zhem.users
(
    user_id    bigserial primary key,
    phone      varchar(11)  not null check ( phone similar to '79[0-9]{9}' ) unique,
    password   varchar(256) not null,
    email      varchar(256) check ( email similar to '([a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\.[a-zA-Z0-9_-]+)' ) unique,
    first_name varchar(32)  not null check ( length(trim(first_name)) >= 2 ),
    last_name  varchar(32) check ( length(trim(last_name)) >= 2 ),
    created_at timestamp default now(),
    updated_at timestamp default now()
);

create table if not exists zhem.roles
(
    role_id serial primary key,
    title   varchar(32) not null check ( length(trim(title)) >= 2 ) unique
);

create table if not exists zhem.users_roles
(
    user_role_id bigserial primary key,
    user_id      bigint not null references zhem.users (user_id),
    role_id      int    not null references zhem.roles (role_id),
    constraint unique_user_id_role_id unique (user_id, role_id)
);

create type status as enum ('AVAILABLE', 'BOOKED');

create table if not exists zhem.intervals
(
    interval_id bigserial primary key,
    date        date   not null,
    time        time   not null,
    status      status not null default 'AVAILABLE',
    created_at  timestamp       default now(),
    updated_at  timestamp       default now(),
    constraint unique_date_time unique (date, time)
);

create table if not exists zhem.appointments
(
    appointment_id bigserial primary key,
    user_id        bigint not null references zhem.users (user_id),
    interval_id    bigint not null unique references zhem.intervals (interval_id),
    details        varchar(256) check ( length(trim(details)) >= 2 ),
    created_at     timestamp default now(),
    updated_at     timestamp default now()
);