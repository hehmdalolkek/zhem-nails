alter table zhem.appointments_services
    alter column service_id drop not null;

alter table zhem.appointments_services
    drop constraint if exists appointments_services_service_id_fkey;

alter table zhem.appointments_services
    add constraint appointment_services_service_id_on_delete foreign key (service_id)
    references zhem.services(service_id) on delete set null;