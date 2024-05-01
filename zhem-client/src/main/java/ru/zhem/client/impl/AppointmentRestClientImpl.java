package ru.zhem.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.zhem.client.interfaces.AppointmentRestClient;
import ru.zhem.dto.response.PaginatedResponse;
import ru.zhem.dto.request.AppointmentDto;
import ru.zhem.dto.request.DailyAppointmentDto;
import ru.zhem.dto.response.AppointmentCreationDto;
import ru.zhem.dto.response.AppointmentUpdateDto;
import ru.zhem.common.exceptions.CustomBindException;
import ru.zhem.common.exceptions.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class AppointmentRestClientImpl implements AppointmentRestClient {

    private static final ParameterizedTypeReference<List<DailyAppointmentDto>> APPOINTMENT_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };
    private static final ParameterizedTypeReference<PaginatedResponse<AppointmentDto>> APPOINTMENT_PAGE_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    @Override
    public Page<AppointmentDto> findAllAppointmentsByUser(long userId, Pageable pageable) {
        return this.restClient.get()
                .uri("/service-api/v1/appointments/user/{userId}?page={page}&size={size}",
                        userId, pageable.getPageNumber(), pageable.getPageSize())
                .retrieve()
                .body(APPOINTMENT_PAGE_TYPE_REFERENCE);
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
        try {
            return Optional.ofNullable(
                    this.restClient.get()
                            .uri("/service-api/v1/appointments/appointment/{appointmentId}", appointmentId)
                            .retrieve()
                            .body(AppointmentDto.class)
            );
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public void createAppointment(AppointmentCreationDto appointmentDto) {
        try {
            this.restClient.post()
                    .uri("/service-api/v1/appointments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(appointmentDto)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest | HttpClientErrorException.Conflict exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new CustomBindException((Map<String, String>) problemDetail.getProperties().get("errors"));
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException(exception.getResponseBodyAs(ProblemDetail.class));
        }
    }

    @Override
    public void updateAppointment(long appointmentId, AppointmentUpdateDto appointmentDto) {
        try {
            this.restClient.patch()
                    .uri("/service-api/v1/appointments/appointment/{appointmentId}", appointmentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(appointmentDto)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest | HttpClientErrorException.Conflict exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new CustomBindException((Map<String, String>) problemDetail.getProperties().get("errors"));
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException(exception.getResponseBodyAs(ProblemDetail.class));
        }
    }

    @Override
    public void deleteAppointment(long appointmentId) {
        try {
            this.restClient.delete()
                    .uri("/service-api/v1/appointments/appointment/{appointmentId}", appointmentId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NotFoundException(exception.getResponseBodyAs(ProblemDetail.class));
        }
    }

    @Override
    public Optional<AppointmentDto> findAppointmentByInterval(long intervalId) {
        try {
            return Optional.ofNullable(this.restClient.get()
                    .uri("/service-api/v1/appointments/appointment/interval/{intervalId}", intervalId)
                    .retrieve()
                    .body(AppointmentDto.class));
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }
}
