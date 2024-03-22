package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.zhem.controller.payload.NewUserPayload;
import ru.zhem.controller.payload.UpdateUserPayload;
import ru.zhem.entity.Appointment;
import ru.zhem.entity.User;
import ru.zhem.service.UserService;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/service-api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/{userId:\\d+}")
    public User getUser(@PathVariable("userId") long userId) {
        return this.userService.findUser(userId)
                .orElseThrow(() -> new NoSuchElementException("User is not found"));
    }

    @GetMapping
    public Iterable<User> findUsers() {
        return this.userService.findUsers();
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody NewUserPayload payload, UriComponentsBuilder uriComponentsBuilder,
                                        BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            User user = this.userService.createUser(payload.phone(), payload.name(), payload.surname());
            return ResponseEntity.created(
                    uriComponentsBuilder
                            .replacePath("/api/v1/users/{userId}")
                            .build(Map.of("userId", user.getId()))
            ).body(user);
        }
    }

    @PatchMapping("/{userId:\\d+}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") long userId, @Valid @RequestBody UpdateUserPayload payload,
                                        BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.userService.updateUser(userId, payload.phone(), payload.name(), payload.surname());
            return ResponseEntity.noContent()
                    .build();
        }
    }

    @DeleteMapping("/{userId:\\d+}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") long userId) {
        this.userService.findUser(userId)
                .orElseThrow(() -> new NoSuchElementException("User is not found"));
        this.userService.deleteUser(userId);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/{userId:\\d+}/appointments")
    public Iterable<Appointment> findAppointmentsByUser(@PathVariable("userId") long userId) {
        this.userService.findUser(userId)
                .orElseThrow(() -> new NoSuchElementException("User is not found"));
        return this.userService.findAppointmentsByUserId(userId);
    }

}
