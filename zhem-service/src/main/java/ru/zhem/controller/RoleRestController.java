package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.zhem.dto.mapper.RoleMapper;
import ru.zhem.dto.request.RoleCreationDto;
import ru.zhem.entity.Role;
import ru.zhem.exception.*;
import ru.zhem.service.RoleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-api/v1/roles")
public class RoleRestController {

    private final RoleService roleService;

    private final RoleMapper roleMapper;

    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleCreationDto roleDto,
                                        BindingResult bindingResult) throws BindException, ConflictException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            try {
                Role createdRole = this.roleService.createRole(this.roleMapper.fromCreationDto(roleDto));
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(this.roleMapper.fromEntity(createdRole));
            } catch (RoleWithDuplicateTitleException exception) {
                bindingResult.addError(new FieldError(
                        "Role", "title", "Роль с таким названием уже существует"));
                throw new ConflictException(bindingResult);
            }
        }
    }

    @GetMapping("/role/{title}")
    public ResponseEntity<?> findRoleByTitle(@PathVariable("title") String title) {
        try {
            Role role = this.roleService.findRoleByTitle(title);
            return ResponseEntity.ok()
                    .body(this.roleMapper.fromEntity(role));
        } catch (RoleNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

}
