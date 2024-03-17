create schema if not exists zhem;

create table if not exists zhem.t_users
(
    id           serial primary key,
    c_phone      decimal(11, 0) not null check ( c_phone > 0 ) unique,
    c_name       varchar(32) not null check ( length(trim(c_name)) >= 2 ),
    c_surname    varchar(32) check ( length(trim(c_surname)) >= 2 ),
    c_created_at timestamp
);

create table if not exists zhem.t_workdays
(
    id     serial primary key,
    c_date date not null unique
);

create table if not exists zhem.t_workintervals
(
    id           serial primary key,
    id_workday   int     not null references zhem.t_workdays (id),
    c_start_time time    not null,
    c_is_booked  boolean not null
);

create table if not exists zhem.t_appointments
(
    id              serial primary key,
    id_user         int not null references zhem.t_users (id),
    id_workinterval int not null references zhem.t_workintervals (id),
    c_created_at    timestamp,
    c_updated_at    timestamp
);