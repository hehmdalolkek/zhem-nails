alter table zhem.t_workintervals
    alter column c_is_booked set default false;

alter table zhem.t_workintervals
    add constraint unique_workday_time unique (id_workday, c_start_time);

alter table zhem.t_appointments
    add constraint unique_workinterval unique (id_workinterval);