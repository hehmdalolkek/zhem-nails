package ru.zhem.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zhem.dto.response.IntervalCreationDto;
import ru.zhem.dto.response.IntervalUpdateDto;
import ru.zhem.entity.Status;
import ru.zhem.exceptions.CustomBindException;
import ru.zhem.service.CalendarService;
import ru.zhem.service.IntervalService;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/intervals")
public class AdminIntervalController {

    private final IntervalService intervalService;

    private final CalendarService calendarService;

    @GetMapping
    public String showAllIntervals(@RequestParam(value = "year", required = false) Integer year,
                                   @RequestParam(value = "month", required = false) Integer month, Model model) {
        YearMonth yearMonth = this.calendarService.calcPrevNextMonth(model, year, month);
        model.addAttribute("statusAvailable", Status.AVAILABLE);
        model.addAttribute("mapOfIntervals",
                this.intervalService.generateIntervalCalendarForMonth(yearMonth));

        return "/admin/intervals/intervals";
    }

    @PostMapping
    public String createInterval(@Valid IntervalCreationDto intervalDto, BindingResult bindingResult,
                                 HttpServletResponse response, RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "year", required = false) Integer year,
                                 @RequestParam(value = "month", required = false) Integer month) {
        String uriToRedirect = year != null && month != null
                ? String.format("redirect:/admin/intervals?year=%s&month=%s", year, month)
                : "redirect:/admin/intervals";
        redirectAttributes.addFlashAttribute("message", "Интервал не создан");
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            redirectAttributes.addFlashAttribute("errors", errors);
        } else {
            try {
                this.intervalService.createInterval(intervalDto);
                redirectAttributes.addFlashAttribute("message", "Интервал создан");
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                redirectAttributes.addFlashAttribute("errors", exception.getErrors());
                return uriToRedirect;
            }
        }
        return uriToRedirect;
    }

    @PostMapping("/update/{intervalId:\\d+}")
    public String updateInterval(@PathVariable("intervalId") long intervalId, @Valid IntervalUpdateDto intervalDto,
                                 BindingResult bindingResult, HttpServletResponse response,
                                 RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "year", required = false) Integer year,
                                 @RequestParam(value = "month", required = false) Integer month) {
        String uriToRedirect = year != null && month != null
                ? String.format("redirect:/admin/intervals?year=%s&month=%s", year, month)
                : "redirect:/admin/intervals";
        redirectAttributes.addFlashAttribute("message", "Интервал не изменен");
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            redirectAttributes.addFlashAttribute("errors", errors);
        } else {
            try {
                this.intervalService.updateInterval(intervalId, intervalDto);
                redirectAttributes.addFlashAttribute("message", "Интервал изменен");
            } catch (CustomBindException exception) {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                redirectAttributes.addFlashAttribute("errors", exception.getErrors());
                return uriToRedirect;
            }
        }
        return uriToRedirect;
    }


}