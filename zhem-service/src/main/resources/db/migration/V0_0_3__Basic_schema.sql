alter table zhem.appointments
    add column status varchar(32) not null default 'CONFIRMED';

alter table "zhem-db".zhem.appointments
    drop constraint if exists appointments_interval_id_key;

create unique index if not exists idx_unique_interval_id_confirmed
    on zhem.appointments (interval_id)
    where status = 'CONFIRMED';