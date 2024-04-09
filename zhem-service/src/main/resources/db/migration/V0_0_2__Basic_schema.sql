alter table "zhem-db".zhem.intervals
    drop column status;

alter table "zhem-db".zhem.intervals
    add column status varchar(32) not null default 'AVAILABLE';

drop type "zhem-db".public.status;