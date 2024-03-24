delete from zhem.t_appointments;

update zhem.t_workintervals set c_is_booked = false where c_is_booked = true;

alter table zhem.t_appointments drop constraint if exists t_appointments_id_user_fkey;

alter table zhem.t_users drop constraint if exists t_users_pkey;

alter table zhem.t_users drop constraint if exists t_users_c_phone_key;

alter table zhem.t_users add primary key (c_phone);

alter table zhem.t_appointments
    alter column id_user type decimal(11, 0);

alter table zhem.t_appointments
    add constraint t_appointments_c_phone_fkey
        foreign key (id_user) references zhem.t_users(c_phone);

alter table zhem.t_users drop column id;