package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zhem.dto.ZhemUserCreationDto;
import ru.zhem.dto.ZhemUserDto;
import ru.zhem.dto.ZhemUserUpdateDto;
import ru.zhem.dto.mapper.UserMapper;
import ru.zhem.entity.ZhemUser;
import ru.zhem.exception.RoleNotFoundException;
import ru.zhem.exception.ZhemUserNotFoundException;
import ru.zhem.exception.ZhemUserWithDuplicateEmailException;
import ru.zhem.exception.ZhemUserWithDuplicatePhoneException;
import ru.zhem.service.ZhemUserService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-api/v1/users")
public class ZhemUserRestController {

    private final ZhemUserService zhemUserService;

    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<?> findAllUsers(@RequestParam(value = "role", required = false) String role) {
        try {
            List<ZhemUser> allUsers = this.zhemUserService.findAllUsers(role);
            List<ZhemUserDto> allUsersPayload = allUsers.stream()
                    .map(userMapper::fromEntity)
                    .toList();

            return ResponseEntity.ok()
                    .body(allUsersPayload);
        } catch (RoleNotFoundException exception) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_FOUND, exception.getMessage()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(problemDetail);
        }
    }

    @GetMapping("/user/{userId:\\d+}")
    public ResponseEntity<?> findUserById(@PathVariable("userId") long userId) {
        try {
            ZhemUser foundedUser = this.zhemUserService.findUserById(userId);
            return ResponseEntity.ok()
                    .body(this.userMapper.fromEntity(foundedUser));
        } catch (ZhemUserNotFoundException exception) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_FOUND, exception.getMessage()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(problemDetail);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> findUserByPhone(@RequestParam("phone") String phone) {
        try {
            ZhemUser foundedUser = this.zhemUserService.findUserByPhone(phone);
            return ResponseEntity.ok()
                    .body(this.userMapper.fromEntity(foundedUser));
        } catch (ZhemUserNotFoundException exception) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_FOUND, exception.getMessage()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(problemDetail);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@Valid @RequestBody ZhemUserCreationDto userDto,
                                        BindingResult bindingResult) throws BindException {
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
            } catch (ZhemUserWithDuplicatePhoneException | ZhemUserWithDuplicateEmailException exception) {
                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST, exception.getMessage()
                );
                return ResponseEntity.badRequest()
                        .body(problemDetail);
            }
        }
    }

    @PatchMapping("/user/{userId:\\d+}")
    public ResponseEntity<?> updateUserById(@PathVariable("userId") long userId, @Valid @RequestBody ZhemUserUpdateDto userDto,
                                            BindingResult bindingResult) throws BindException {
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
            } catch (ZhemUserWithDuplicatePhoneException | ZhemUserWithDuplicateEmailException
                     | ZhemUserNotFoundException exception) {
                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                        HttpStatus.BAD_REQUEST, exception.getMessage()
                );
                return ResponseEntity.badRequest()
                        .body(problemDetail);
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
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST, exception.getMessage()
            );
            return ResponseEntity.badRequest()
                    .body(problemDetail);
        }
    }

}
