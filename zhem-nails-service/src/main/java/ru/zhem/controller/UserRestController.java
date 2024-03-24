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

import java.math.BigDecimal;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/service-api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/{phone:\\d+}")
    public User getUser(@PathVariable("phone") BigDecimal phone) {
        return this.userService.findUser(phone)
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
                            .replacePath("/api/v1/users/{phone}")
                            .build(Map.of("phone", user.getPhone()))
            ).body(user);
        }
    }

    @PatchMapping("/{phone:\\d+}")
    public ResponseEntity<?> updateUser(@PathVariable("phone") BigDecimal phone, @Valid @RequestBody UpdateUserPayload payload,
                                        BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.userService.updateUser(phone, payload.phone(), payload.name(), payload.surname());
            return ResponseEntity.noContent()
                    .build();
        }
    }

    @DeleteMapping("/{phone:\\d+}")
    public ResponseEntity<Void> deleteUser(@PathVariable("phone") BigDecimal phone) {
        this.userService.findUser(phone)
                .orElseThrow(() -> new NoSuchElementException("User is not found"));
        this.userService.deleteUser(phone);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/{phone:\\d+}/appointments")
    public Iterable<Appointment> findAppointmentsByUser(@PathVariable("phone") BigDecimal phone) {
        this.userService.findUser(phone)
                .orElseThrow(() -> new NoSuchElementException("User is not found"));
        return this.userService.findAppointmentsByUserPhone(phone);
    }

}
