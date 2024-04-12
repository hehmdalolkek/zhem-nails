package ru.zhem.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import ru.zhem.dto.request.AppointmentDto;
import ru.zhem.dto.request.DailyAppointmentDto;
import ru.zhem.dto.response.AppointmentCreationDto;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class AppointmentRestClientImpl implements AppointmentRestClient {

    private static final ParameterizedTypeReference<List<DailyAppointmentDto>> APPOINTMENT_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    @Override
    public List<DailyAppointmentDto> findAllAppointmentsByUser(long userId) {
        return null;
    }

    @Override
    public List<DailyAppointmentDto> findAllAppointments(int year, int month) {
        return this.restClient.get()
                .uri("/service-api/v1/appointments?year={year}&month={month}", year, month)
                .retrieve()
                .body(APPOINTMENT_TYPE_REFERENCE);
    }

    @Override
    public Optional<AppointmentDto> findAppointmentById(long appointmentId) {
        return Optional.empty();
    }

    @Override
    public void createAppointment(AppointmentCreationDto appointmentDto) {
        this.restClient.post()
                .uri("/service-api/v1/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .body(appointmentDto)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public void updateAppointment(long appointmentId, AppointmentCreationDto appointmentDto) {

    }

    @Override
    public void deleteAppointment(long appointmentId) {

    }
}
