package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.zhem.dto.mapper.UserMapper;
import ru.zhem.dto.request.ZhemUserCreationDto;
import ru.zhem.dto.request.ZhemUserUpdateDto;
import ru.zhem.dto.response.ZhemUserDto;
import ru.zhem.entity.ZhemUser;
import ru.zhem.exception.*;
import ru.zhem.service.ZhemUserService;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-api/v1/users")
public class ZhemUserRestController {

    private final ZhemUserService zhemUserService;

    private final UserMapper userMapper;

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

    @GetMapping("/pageable")
    public ResponseEntity<?> findAllClientsByPage(Pageable pageable) {
        Page<ZhemUserDto> allUsers = this.zhemUserService.findAllClientsByPage(pageable)
                .map(this.userMapper::fromEntity);

        return ResponseEntity.ok()
                .body(allUsers);
    }

    @GetMapping("/user/{userId:\\d+}")
    public ResponseEntity<?> findUserById(@PathVariable("userId") long userId) {
        try {
            ZhemUser foundedUser = this.zhemUserService.findUserById(userId);
            return ResponseEntity.ok()
                    .body(this.userMapper.fromEntity(foundedUser));
        } catch (ZhemUserNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    @GetMapping("/user/auth/{phone}")
    public ResponseEntity<?> findUserByPhone(@PathVariable("phone") String phone,
                                             @RequestParam(value = "admin", required = false) boolean isAdmin) {
        try {
            ZhemUser foundedUser = this.zhemUserService.findUserByPhone(phone, isAdmin);
            return ResponseEntity.ok()
                    .body(this.userMapper.fromEntityForAuth(foundedUser));
        } catch (ZhemUserNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    @GetMapping("/user/phone/{phone}")
    public ResponseEntity<?> findUserByPhone(@PathVariable("phone") String phone) {
        try {
            ZhemUser foundedUser = this.zhemUserService.findUserByPhone(phone, false);
            return ResponseEntity.ok()
                    .body(this.userMapper.fromEntity(foundedUser));
        } catch (ZhemUserNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@Valid @RequestBody ZhemUserCreationDto userDto,
                                        BindingResult bindingResult) throws BindException, ConflictException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            try {
                ZhemUser createdUser = this.zhemUserService.createUser(this.userMapper.fromCreationDto(userDto));
                return ResponseEntity.created(URI.create("/service-api/v1/users/user/" + createdUser.getId()))
                        .body(this.userMapper.fromEntity(createdUser));
            } catch (ZhemUserWithDuplicatePhoneException exception) {
                bindingResult.addError(new FieldError("ZhemUser", "phone", "Номер телефона уже зарегистрирован"));
                throw new ConflictException(bindingResult);
            } catch (ZhemUserWithDuplicateEmailException exception) {
                bindingResult.addError(new FieldError("ZhemUser", "email", "Электронная почта уже зарегистрирован"));
                throw new ConflictException(bindingResult);
            }
        }
    }

    @PatchMapping("/user/{userId:\\d+}")
    public ResponseEntity<?> updateUserById(@PathVariable("userId") long userId, @Valid @RequestBody ZhemUserUpdateDto userDto,
                                            BindingResult bindingResult) throws BindException, ConflictException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            try {
                ZhemUser updatedUser = this.zhemUserService.updateUser(userId, this.userMapper.fromUpdateDto(userDto));
                return ResponseEntity.ok()
                        .body(this.userMapper.fromEntity(updatedUser));
            } catch (ZhemUserNotFoundException exception) {
                throw new NotFoundException(exception.getMessage());
            } catch (ZhemUserWithDuplicatePhoneException exception) {
                bindingResult.addError(new FieldError("ZhemUser", "phone", "Номер телефона уже зарегистрирован"));
                throw new ConflictException(bindingResult);
            } catch (ZhemUserWithDuplicateEmailException exception) {
                bindingResult.addError(new FieldError("ZhemUser", "email", "Электронная почта уже зарегистрирован"));
                throw new ConflictException(bindingResult);
            }
        }
    }

    @DeleteMapping("/user/{userId:\\d+}")
    public ResponseEntity<?> deleteUserById(@PathVariable("userId") long userId) {
        try {
            this.zhemUserService.deleteUserById(userId);
            return ResponseEntity.ok()
                    .build();
        } catch (ZhemUserNotFoundException exception) {
            throw new NotFoundException(exception.getMessage());
        }
    }

    @GetMapping("/user/admin")
    public ResponseEntity<?> adminIsExists() {
        return ResponseEntity.ok()
                .body(this.zhemUserService.adminIsExists());
    }

}
