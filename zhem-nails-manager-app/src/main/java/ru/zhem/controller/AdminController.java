package ru.zhem.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.zhem.client.BadRequestException;
import ru.zhem.client.WorkIntervalRestClient;
import ru.zhem.entity.WorkInterval;
import ru.zhem.entity.payload.NewWorkIntervalPayload;
import ru.zhem.entity.payload.UpdateWorkIntervalPayload;

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
    public String getWorkIntervalsList(Model model) {
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
    public String createWorkInterval(NewWorkIntervalPayload payload,
                                     Model model, HttpServletResponse response) {
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

    @GetMapping("/workintervals/{workInterval:\\d+}/edit")
    public String getEditWorkIntervalPage(@PathVariable("workInterval") long workIntervalId, Model model) {
        WorkInterval workInterval = this.workIntervalRestClient.findWorkInterval(workIntervalId)
                .orElseThrow(() -> new NoSuchElementException("WorkInterval is not found"));
        model.addAttribute("workInterval", workInterval);
        return "service/admin/workIntervals/edit";
    }

    @PostMapping("/workintervals/{workInterval:\\d+}/edit")
    public String editWorkInterval(@PathVariable("workInterval") long workIntervalId, Model model,
                                   UpdateWorkIntervalPayload payload, HttpServletResponse response) {
        try {
            this.workIntervalRestClient.updateWorkInterval(workIntervalId, payload.date(), payload.startTime());
            return "redirect:/admin/workintervals";
        } catch (BadRequestException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "service/admin/workIntervals/new";
        }
    }

    @PostMapping("/workintervals/{workIntervalId:\\d+}/delete")
    public String deleteWorkInterval(@PathVariable("workIntervalId") long workIntervalId) {
        this.workIntervalRestClient.deleteWorkInterval(workIntervalId);
        return "redirect:/admin/workintervals";
    }
}
