alter table zhem.t_users
    rename to t_clients;

alter table zhem.t_appointments
    rename column id_user to id_client;