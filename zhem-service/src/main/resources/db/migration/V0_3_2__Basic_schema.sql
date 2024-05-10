alter table zhem.appointments_services
    drop constraint appointments_services_appointment_id_fkey;

alter table zhem.appointments_services
    add foreign key (appointment_id) references zhem.appointments
        on delete cascade;