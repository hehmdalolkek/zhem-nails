package ru.zhem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.zhem.controller.payload.NewClientPayload;
import ru.zhem.controller.payload.UpdateClientPayload;
import ru.zhem.entity.Appointment;
import ru.zhem.entity.Client;
import ru.zhem.service.ClientService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/service-api/v1/clients")
@RequiredArgsConstructor
public class ClientRestController {

    private final ClientService clientService;

    @GetMapping("/{phone:\\d+}")
    public Client getClient(@PathVariable("phone") BigDecimal phone) {
        return this.clientService.findClient(phone)
                .orElseThrow(() -> new NoSuchElementException("Client is not found"));
    }

    @GetMapping
    public Iterable<Client> findClients() {
        return this.clientService.findClients();
    }

    @PostMapping
    public ResponseEntity<?> createClient(@Valid @RequestBody NewClientPayload payload, UriComponentsBuilder uriComponentsBuilder,
                                          BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Client client = this.clientService.createClient(payload.phone(), payload.name(), payload.surname(), payload.password());
            return ResponseEntity.created(
                    uriComponentsBuilder
                            .replacePath("/api/v1/clients/{phone}")
                            .build(Map.of("phone", client.getPhone()))
            ).body(client);
        }
    }

    @PatchMapping("/{phone:\\d+}")
    public ResponseEntity<?> updateClient(@PathVariable("phone") BigDecimal phone, @Valid @RequestBody UpdateClientPayload payload,
                                          BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            this.clientService.updateClient(phone, payload.phone(), payload.name(), payload.surname(), payload.password());
            return ResponseEntity.noContent()
                    .build();
        }
    }

    @DeleteMapping("/{phone:\\d+}")
    public ResponseEntity<Void> deleteClient(@PathVariable("phone") BigDecimal phone) {
        this.clientService.findClient(phone)
                .orElseThrow(() -> new NoSuchElementException("Client is not found"));
        this.clientService.deleteClient(phone);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/{phone:\\d+}/appointments")
    public Iterable<Appointment> findAppointmentsByClient(@PathVariable("phone") BigDecimal phone) {
        this.clientService.findClient(phone)
                .orElseThrow(() -> new NoSuchElementException("Client is not found"));
        return this.clientService.findAppointmentsByClientPhone(phone);
    }

}
