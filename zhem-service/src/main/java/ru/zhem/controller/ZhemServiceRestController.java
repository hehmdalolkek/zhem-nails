package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zhem.common.annotation.LogAnnotation;
import ru.zhem.dto.mapper.ZhemServiceMapper;
import ru.zhem.dto.request.ZhemServiceCreationDto;
import ru.zhem.entity.ZhemService;
import ru.zhem.service.interfaces.ZhemServiceService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-api/v1/services")
public class ZhemServiceRestController {

    private final ZhemServiceMapper zhemServiceMapper;

    private final ZhemServiceService zhemServiceService;

    @LogAnnotation(module = "ZhemService", operation = "Get all services")
    @GetMapping
    public ResponseEntity<?> findAllServices() {
        List<ZhemService> services = this.zhemServiceService.findAllServices();
        return ResponseEntity.ok()
                .body(services.stream().map(this.zhemServiceMapper::fromEntity));
    }

    @LogAnnotation(module = "ZhemService", operation = "Create new service")
    @PostMapping
    public ResponseEntity<?> createService(@Valid @RequestBody ZhemServiceCreationDto serviceDto) {
        ZhemService createdService = this.zhemServiceService.createService(
                this.zhemServiceMapper.fromCreationDto(serviceDto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.zhemServiceMapper.fromEntity(createdService));
    }

    @LogAnnotation(module = "ZhemService", operation = "Delete service by id")
    @DeleteMapping("/service/{serviceId}")
    public ResponseEntity<?> deleteService(@PathVariable("serviceId") int serviceId) {
        this.zhemServiceService.deleteServiceById(serviceId);
        return ResponseEntity.ok()
                .build();
    }

}
