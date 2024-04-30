package ru.zhem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zhem.common.annotation.LogAnnotation;
import ru.zhem.dto.mapper.RoleMapper;
import ru.zhem.dto.request.RoleCreationDto;
import ru.zhem.dto.response.PostDto;
import ru.zhem.dto.response.RoleDto;
import ru.zhem.entity.Role;
import ru.zhem.service.interfaces.RoleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-api/v1/roles")
public class RoleRestController {

    private final RoleService roleService;

    private final RoleMapper roleMapper;

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = RoleCreationDto.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created role",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = PostDto.class
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
    @LogAnnotation(module = "Role", operation = "Create new role")
    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleCreationDto roleDto) {
        Role createdRole = this.roleService.createRole(this.roleMapper.fromCreationDto(roleDto));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.roleMapper.fromEntity(createdRole));
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find role by title",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = RoleDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Role not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "Role", operation = "Get role by title")
    @GetMapping("/role/{title}")
    public ResponseEntity<?> findRoleByTitle(@PathVariable("title") String title) {
        Role role = this.roleService.findRoleByTitle(title);
        return ResponseEntity.ok()
                .body(this.roleMapper.fromEntity(role));
    }

}
