package ru.zhem.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.zhem.client.BadRequestException;
import ru.zhem.client.WorkIntervalRestClient;
import ru.zhem.entity.WorkInterval;
import ru.zhem.entity.payload.NewWorkIntervalPayload;
import ru.zhem.entity.payload.UpdateWorkIntervalPayload;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final WorkIntervalRestClient workIntervalRestClient;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "service/admin/common/dashboard";
    }

    @GetMapping("/workintervals")
    public String getWorkIntervalList(Model model) {
        model.addAttribute("workIntervals", this.workIntervalRestClient.findAllWorkIntervals());
        return "service/admin/workIntervals/list";
    }

    @GetMapping("/workintervals/{workIntervalId:\\d+}")
    public String getWorkInterval(@PathVariable("workIntervalId") long workIntervalId, Model model) {
        WorkInterval workInterval = this.workIntervalRestClient.findWorkInterval(workIntervalId)
                .orElseThrow(() -> new NoSuchElementException("WorkInterval is not found"));
        model.addAttribute("workInterval", workInterval);
        return "service/admin/workIntervals/workInterval";
    }

    @GetMapping("/workintervals/create")
    public String getNewWorkIntervalPage() {
        return "service/admin/workIntervals/new";
    }

    @PostMapping("/workintervals/create")
    public String createWorkInterval(@Valid NewWorkIntervalPayload payload, BindingResult bindingResult,
                                     Model model, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (Object error : bindingResult.getAllErrors()) {
                if (error instanceof FieldError fieldError) {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("payload", payload);
            model.addAttribute("errors", errors);
            return "service/admin/workIntervals/new";
        } else {
            try {
                this.workIntervalRestClient.createWorkInterval(payload.date(), payload.startTime());
                return "redirect:/admin/workintervals";
            } catch (BadRequestException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                model.addAttribute("payload", payload);
                model.addAttribute("errors", exception.getErrors());
                return "service/admin/workIntervals/new";
            }
        }
    }

    @GetMapping("/workintervals/{workInterval:\\d+}/edit")
    public String getEditWorkIntervalPage(@PathVariable("workInterval") long workIntervalId, Model model) {
        WorkInterval workInterval = this.workIntervalRestClient.findWorkInterval(workIntervalId)
                .orElseThrow(() -> new NoSuchElementException("WorkInterval is not found"));
        model.addAttribute("workIntervalId", workIntervalId);
        model.addAttribute("workInterval", workInterval);
        return "service/admin/workIntervals/edit";
    }

    @PostMapping("/workintervals/{workInterval:\\d+}/edit")
    public String editWorkInterval(@PathVariable("workInterval") long workIntervalId, @Valid UpdateWorkIntervalPayload payload,
                                   BindingResult bindingResult, Model model, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (Object error : bindingResult.getAllErrors()) {
                if (error instanceof FieldError fieldError) {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
            }
            model.addAttribute("workIntervalId", workIntervalId);
            model.addAttribute("payload", payload);
            model.addAttribute("errors", errors);
            return "service/admin/workIntervals/edit";
        } else {
            try {
                this.workIntervalRestClient.updateWorkInterval(workIntervalId, payload.date(), payload.startTime());
                return "redirect:/admin/workintervals";
            } catch (BadRequestException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                model.addAttribute("workIntervalId", workIntervalId);
                model.addAttribute("payload", payload);
                model.addAttribute("errors", exception.getErrors());
                return "service/admin/workIntervals/edit";
            }
        }
    }

    @PostMapping("/workintervals/{workIntervalId:\\d+}/delete")
    public String deleteWorkInterval(@PathVariable("workIntervalId") long workIntervalId) {
        this.workIntervalRestClient.deleteWorkInterval(workIntervalId);
        return "redirect:/admin/workintervals";
    }
}
