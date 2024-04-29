package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zhem.dto.mapper.RoleMapper;
import ru.zhem.dto.request.RoleCreationDto;
import ru.zhem.entity.Role;
import ru.zhem.service.interfaces.RoleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-api/v1/roles")
public class RoleRestController {

    private final RoleService roleService;

    private final RoleMapper roleMapper;

    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleCreationDto roleDto) {
        Role createdRole = this.roleService.createRole(this.roleMapper.fromCreationDto(roleDto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.roleMapper.fromEntity(createdRole));
    }

    @GetMapping("/role/{title}")
    public ResponseEntity<?> findRoleByTitle(@PathVariable("title") String title) {
        Role role = this.roleService.findRoleByTitle(title);
        return ResponseEntity.ok()
                .body(this.roleMapper.fromEntity(role));
    }

}
