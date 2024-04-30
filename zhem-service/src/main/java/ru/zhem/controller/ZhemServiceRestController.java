package ru.zhem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zhem.common.annotation.LogAnnotation;
import ru.zhem.dto.mapper.ZhemServiceMapper;
import ru.zhem.dto.request.RoleCreationDto;
import ru.zhem.dto.request.ZhemServiceCreationDto;
import ru.zhem.dto.response.PostDto;
import ru.zhem.dto.response.ZhemServiceDto;
import ru.zhem.entity.ZhemService;
import ru.zhem.service.interfaces.ZhemServiceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-api/v1/services")
public class ZhemServiceRestController {

    private final ZhemServiceMapper zhemServiceMapper;

    private final ZhemServiceService zhemServiceService;

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find all services",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = ZhemServiceDto.class
                                            )
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "ZhemService", operation = "Get all services")
    @GetMapping
    public ResponseEntity<?> findAllServices() {
        List<ZhemService> services = this.zhemServiceService.findAllServices();
        return ResponseEntity.ok()
                .body(services.stream().map(this.zhemServiceMapper::fromEntity));
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = ZhemServiceCreationDto.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created service",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ZhemServiceDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad credentials",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "ZhemService", operation = "Create new service")
    @PostMapping
    public ResponseEntity<?> createService(@Valid @RequestBody ZhemServiceCreationDto serviceDto) {
        ZhemService createdService = this.zhemServiceService.createService(
                this.zhemServiceMapper.fromCreationDto(serviceDto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.zhemServiceMapper.fromEntity(createdService));
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleted service"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Service not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "ZhemService", operation = "Delete service by id")
    @DeleteMapping("/service/{serviceId}")
    public ResponseEntity<?> deleteService(@PathVariable("serviceId") int serviceId) {
        this.zhemServiceService.deleteServiceById(serviceId);
        return ResponseEntity.ok()
                .build();
    }

}
