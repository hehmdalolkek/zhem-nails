package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.zhem.controller.payload.NewZhemUserPayload;
import ru.zhem.controller.payload.ReturnZhemUserPayload;
import ru.zhem.controller.payload.UpdateZhemUserPayload;
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

    @GetMapping
    public ResponseEntity<?> findAllUsers(@RequestParam(value = "role", required = false) String role) {
        try {
            List<ZhemUser> allUsers = this.zhemUserService.findAllUsers(role);
            List<ReturnZhemUserPayload> allUsersPayload = allUsers.stream()
                    .map(ReturnZhemUserPayload::fromEntity)
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
                    .body(ReturnZhemUserPayload.fromEntity(foundedUser));
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
                    .body(ReturnZhemUserPayload.fromEntity(foundedUser));
        } catch (ZhemUserNotFoundException exception) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatus.NOT_FOUND, exception.getMessage()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(problemDetail);
        }
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@Valid @RequestBody NewZhemUserPayload payload,
                                        BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            try {
                ZhemUser createdUser = this.zhemUserService.createUser(payload.getPhone(), payload.getPassword(),
                        payload.getEmail(), payload.getFirstName(), payload.getLastName(), payload.getRoles());
                return ResponseEntity.created(URI.create("/service-api/v1/users/user/" + createdUser.getId()))
                        .body(ReturnZhemUserPayload.fromEntity(createdUser));
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
    public ResponseEntity<?> updateUserById(@PathVariable("userId") long userId, @Valid @RequestBody UpdateZhemUserPayload payload,
                                            BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            try {
                ZhemUser updatedUser = this.zhemUserService.updateUser(userId, payload.getPhone(), payload.getPassword(),
                        payload.getEmail(), payload.getFirstName(), payload.getLastName());
                return ResponseEntity.ok()
                        .body(ReturnZhemUserPayload.fromEntity(updatedUser));
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
