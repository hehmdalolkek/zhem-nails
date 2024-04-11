package ru.zhem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zhem.dto.constraints.NullOrNotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentUpdateDto {

    private Long userId;

    private Long intervalId;

    private String details;

}
