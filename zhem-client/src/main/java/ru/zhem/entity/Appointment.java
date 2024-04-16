package ru.zhem.entity;

public class Appointment extends BaseEntity {

    private ZhemUser user;

    private Interval interval;

    private AppointmentStatus status;

    private String details;

}
