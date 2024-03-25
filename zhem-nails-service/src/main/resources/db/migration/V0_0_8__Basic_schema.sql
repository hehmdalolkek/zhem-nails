delete from zhem.t_appointments;

delete from zhem.t_clients;

alter table zhem.t_clients
    add column c_password varchar(18) not null;