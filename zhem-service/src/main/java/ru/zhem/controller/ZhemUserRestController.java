package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zhem.common.annotation.LogAnnotation;
import ru.zhem.dto.mapper.UserMapper;
import ru.zhem.dto.request.ZhemUserCreationDto;
import ru.zhem.dto.request.ZhemUserUpdateDto;
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

    @LogAnnotation(module = "ZhemUser", operation = "Get all users with role CLIENT by page")
    @GetMapping("/pageable")
    public ResponseEntity<?> findAllClientsByPage(Pageable pageable) {
        Page<ZhemUserDto> allUsers = this.zhemUserService.findAllClientsByPage(pageable)
                .map(this.userMapper::fromEntity);

        return ResponseEntity.ok()
                .body(allUsers);
    }

    @LogAnnotation(module = "ZhemUser", operation = "Get user by id")
    @GetMapping("/user/{userId:\\d+}")
    public ResponseEntity<?> findUserById(@PathVariable("userId") long userId) {
        ZhemUser foundedUser = this.zhemUserService.findUserById(userId);
        return ResponseEntity.ok()
                .body(this.userMapper.fromEntity(foundedUser));
    }

    @LogAnnotation(module = "ZhemUser", operation = "Get user for auth by phone")
    @GetMapping("/user/auth/{phone}")
    public ResponseEntity<?> findUserByPhone(@PathVariable("phone") String phone,
                                             @RequestParam(value = "admin", required = false) boolean isAdmin) {
        ZhemUser foundedUser = this.zhemUserService.findUserByPhone(phone, isAdmin);
        return ResponseEntity.ok()
                .body(this.userMapper.fromEntityForAuth(foundedUser));
    }

    @LogAnnotation(module = "ZhemUser", operation = "Get user by id")
    @GetMapping("/user/phone/{phone}")
    public ResponseEntity<?> findUserByPhone(@PathVariable("phone") String phone) {
        ZhemUser foundedUser = this.zhemUserService.findUserByPhone(phone, false);
        return ResponseEntity.ok()
                .body(this.userMapper.fromEntity(foundedUser));
    }

    @LogAnnotation(module = "ZhemUser", operation = "Create new user")
    @PostMapping()
    public ResponseEntity<?> createUser(@Valid @RequestBody ZhemUserCreationDto userDto) {
        ZhemUser createdUser = this.zhemUserService.createUser(this.userMapper.fromCreationDto(userDto));
        return ResponseEntity.created(URI.create("/service-api/v1/users/user/" + createdUser.getId()))
                .body(this.userMapper.fromEntity(createdUser));
    }

    @LogAnnotation(module = "ZhemUser", operation = "Update user by id")
    @PatchMapping("/user/{userId:\\d+}")
    public ResponseEntity<?> updateUserById(@PathVariable("userId") long userId, @Valid @RequestBody ZhemUserUpdateDto userDto) {
        ZhemUser updatedUser = this.zhemUserService.updateUser(userId, this.userMapper.fromUpdateDto(userDto));
        return ResponseEntity.ok()
                .body(this.userMapper.fromEntity(updatedUser));
    }

    @LogAnnotation(module = "ZhemUser", operation = "Delete user by id")
    @DeleteMapping("/user/{userId:\\d+}")
    public ResponseEntity<?> deleteUserById(@PathVariable("userId") long userId) {
        this.zhemUserService.deleteUserById(userId);
        return ResponseEntity.ok()
                .build();
    }

    @LogAnnotation(module = "ZhemUser", operation = "Check admin is exists")
    @GetMapping("/user/check/admin")
    public ResponseEntity<?> adminIsExists() {
        return ResponseEntity.ok()
                .body(this.zhemUserService.adminIsExists());
    }

    @LogAnnotation(module = "ZhemUser", operation = "Get admin")
    @GetMapping("/user/admin")
    public ResponseEntity<?> findAdmin() {
        ZhemUser foundedAdmin = this.zhemUserService.findAdmin();
        return ResponseEntity.ok()
                .body(this.userMapper.fromEntity(foundedAdmin));
    }

}
