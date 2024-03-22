alter table zhem.t_workintervals
    drop constraint unique_workday_time;

alter table zhem.t_workintervals
    drop column id_workday;

alter table zhem.t_workintervals
    add column c_date date not null default '1970-01-01';

alter table zhem.t_workintervals
    add constraint unique_date_time unique (c_date, c_start_time);
