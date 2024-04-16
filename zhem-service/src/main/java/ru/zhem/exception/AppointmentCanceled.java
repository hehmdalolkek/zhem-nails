package ru.zhem.exception;

public class AppointmentCanceled extends RuntimeException {
    public AppointmentCanceled(String message) {
        super(message);
    }
}
