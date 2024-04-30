package ru.zhem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zhem.common.annotation.LogAnnotation;
import ru.zhem.dto.mapper.UserMapper;
import ru.zhem.dto.request.RoleCreationDto;
import ru.zhem.dto.request.ZhemUserCreationDto;
import ru.zhem.dto.request.ZhemUserUpdateDto;
import ru.zhem.dto.response.PostDto;
import ru.zhem.dto.response.ZhemServiceDto;
import ru.zhem.dto.response.ZhemUserAuthDto;
import ru.zhem.dto.response.ZhemUserDto;
import ru.zhem.entity.ZhemUser;
import ru.zhem.service.interfaces.ZhemUserService;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-api/v1/users")
public class ZhemUserRestController {

    private final ZhemUserService zhemUserService;

    private final UserMapper userMapper;

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find all users",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = ZhemUserDto.class
                                            )
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "ZhemUser", operation = "Get all users with role CLIENT")
    @GetMapping
    public ResponseEntity<?> findAllClients(@RequestParam(value = "firstName", required = false) String firstName,
                                            @RequestParam(value = "lastName", required = false) String lastName,
                                            @RequestParam(value = "phone", required = false) String phone,
                                            @RequestParam(value = "email", required = false) String email) {
        List<ZhemUserDto> allUsersPayload;
        List<ZhemUser> allUsers;
        if (Objects.nonNull(firstName) || Objects.nonNull(lastName)
                || Objects.nonNull(phone) || Objects.nonNull(email)) {
            allUsers = this.zhemUserService.findAllClientsBy(firstName, lastName, phone, email);
        } else {
            allUsers = this.zhemUserService.findAllClients();
        }
        allUsersPayload = allUsers.stream()
                .map(userMapper::fromEntity)
                .toList();
        return ResponseEntity.ok()
                .body(allUsersPayload);
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find all users by page",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(
                                            schema = @Schema(
                                                    implementation = Page.class
                                            )
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "ZhemUser", operation = "Get all users with role CLIENT by page")
    @GetMapping("/pageable")
    public ResponseEntity<?> findAllClientsByPage(@ParameterObject Pageable pageable) {
        Page<ZhemUserDto> allUsers = this.zhemUserService.findAllClientsByPage(pageable)
                .map(this.userMapper::fromEntity);

        return ResponseEntity.ok()
                .body(allUsers);
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find user by id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ZhemUserDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "ZhemUser", operation = "Get user by id")
    @GetMapping("/user/{userId:\\d+}")
    public ResponseEntity<?> findUserById(@PathVariable("userId") long userId) {
        ZhemUser foundedUser = this.zhemUserService.findUserById(userId);
        return ResponseEntity.ok()
                .body(this.userMapper.fromEntity(foundedUser));
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find user by phone for auth",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ZhemUserAuthDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "ZhemUser", operation = "Get user for auth by phone")
    @GetMapping("/user/auth/{phone}")
    public ResponseEntity<?> findUserByPhone(@PathVariable("phone") String phone,
                                             @RequestParam(value = "admin", required = false) boolean isAdmin) {
        ZhemUser foundedUser = this.zhemUserService.findUserByPhone(phone, isAdmin);
        return ResponseEntity.ok()
                .body(this.userMapper.fromEntityForAuth(foundedUser));
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Find user by phone",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ZhemUserDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "ZhemUser", operation = "Get user by id")
    @GetMapping("/user/phone/{phone}")
    public ResponseEntity<?> findUserByPhone(@PathVariable("phone") String phone) {
        ZhemUser foundedUser = this.zhemUserService.findUserByPhone(phone, false);
        return ResponseEntity.ok()
                .body(this.userMapper.fromEntity(foundedUser));
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = ZhemUserCreationDto.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created user",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ZhemUserDto.class
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
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "User with email/phone is already exists",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "ZhemUser", operation = "Create new user")
    @PostMapping()
    public ResponseEntity<?> createUser(@Valid @RequestBody ZhemUserCreationDto userDto) {
        ZhemUser createdUser = this.zhemUserService.createUser(this.userMapper.fromCreationDto(userDto));
        return ResponseEntity.created(URI.create("/service-api/v1/users/user/" + createdUser.getId()))
                .body(this.userMapper.fromEntity(createdUser));
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = ZhemUserUpdateDto.class
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Updated user",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ZhemUserDto.class
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
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "User with email/phone is already exists",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "ZhemUser", operation = "Update user by id")
    @PatchMapping("/user/{userId:\\d+}")
    public ResponseEntity<?> updateUserById(@PathVariable("userId") long userId, @Valid @RequestBody ZhemUserUpdateDto userDto) {
        ZhemUser updatedUser = this.zhemUserService.updateUser(userId, this.userMapper.fromUpdateDto(userDto));
        return ResponseEntity.ok()
                .body(this.userMapper.fromEntity(updatedUser));
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleted user"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "ZhemUser", operation = "Delete user by id")
    @DeleteMapping("/user/{userId:\\d+}")
    public ResponseEntity<?> deleteUserById(@PathVariable("userId") long userId) {
        this.zhemUserService.deleteUserById(userId);
        return ResponseEntity.ok()
                .build();
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Check admin is exists",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            type = "boolean"
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "ZhemUser", operation = "Check admin is exists")
    @GetMapping("/user/check/admin")
    public ResponseEntity<?> adminIsExists() {
        return ResponseEntity.ok()
                .body(this.zhemUserService.adminIsExists());
    }

    @Operation(
            security = @SecurityRequirement(name = "basicAuth"),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Get admin",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ZhemUserDto.class
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Admin not found",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            implementation = ProblemDetail.class
                                    )
                            )
                    )
            }
    )
    @LogAnnotation(module = "ZhemUser", operation = "Get admin")
    @GetMapping("/user/admin")
    public ResponseEntity<?> findAdmin() {
        ZhemUser foundedAdmin = this.zhemUserService.findAdmin();
        return ResponseEntity.ok()
                .body(this.userMapper.fromEntity(foundedAdmin));
    }

}
