create table if not exists zhem.services
(
    service_id serial primary key,
    title      varchar(32) not null check ( length(trim(title)) >= 2 ) unique
);

create table if not exists zhem.appointments_services
(
    appointment_service_id bigserial primary key,
    appointment_id         bigint not null references zhem.appointments (appointment_id),
    service_id             int    not null references zhem.services (service_id),
    constraint unique_appointment_id_service_id unique (appointment_id, service_id)
);