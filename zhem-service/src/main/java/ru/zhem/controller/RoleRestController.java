package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zhem.controller.payload.NewRolePayload;
import ru.zhem.controller.payload.ReturnRolePayload;
import ru.zhem.entity.Role;
import ru.zhem.exception.RoleWithDuplicateTitleException;
import ru.zhem.service.RoleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-api/v1/roles")
public class RoleRestController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody NewRolePayload payload, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            try {
                Role createdRole = this.roleService.createRole(payload.getTitle());
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ReturnRolePayload.fromEntity(createdRole));
            } catch (RoleWithDuplicateTitleException exception) {
                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST, exception.getMessage()
                );
                return ResponseEntity.badRequest()
                        .body(problemDetail);
            }
        }
    }

}
