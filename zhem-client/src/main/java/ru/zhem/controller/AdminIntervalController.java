package ru.zhem.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zhem.controller.util.ControllerUtil;
import ru.zhem.dto.response.IntervalCreationDto;
import ru.zhem.dto.response.IntervalUpdateDto;
import ru.zhem.entity.Status;
import ru.zhem.exceptions.CustomBindException;
import ru.zhem.service.interfaces.IntervalService;
import ru.zhem.service.util.CalendarUtil;

import java.time.YearMonth;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/intervals")
public class AdminIntervalController {

    private final IntervalService intervalService;

    private final CalendarUtil calendarUtil;

    private final ControllerUtil controllerUtil;

    @GetMapping
    public String showAllIntervals(@RequestParam(value = "year", required = false) Integer year,
                                   @RequestParam(value = "month", required = false) Integer month, Model model) {
        YearMonth yearMonth = this.calendarUtil.calcPrevNextMonth(model, year, month);
        model.addAttribute("statusAvailable", Status.AVAILABLE);
        model.addAttribute("mapOfIntervals",
                this.controllerUtil.generateIntervalCalendarForMonth(yearMonth, false));

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
            Map<String, String> errors = this.controllerUtil.getErrors(bindingResult);
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

    @PostMapping("/update")
    public String updateInterval(Long intervalId, @Valid IntervalUpdateDto intervalDto,
                                 BindingResult bindingResult, HttpServletResponse response,
                                 RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "year", required = false) Integer year,
                                 @RequestParam(value = "month", required = false) Integer month) {
        String uriToRedirect = year != null && month != null
                ? String.format("redirect:/admin/intervals?year=%s&month=%s", year, month)
                : "redirect:/admin/intervals";
        redirectAttributes.addFlashAttribute("message", "Интервал не изменен");
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = this.controllerUtil.getErrors(bindingResult);
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

    @PostMapping("/delete")
    public String deleteInterval(Long intervalId, RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "year", required = false) Integer year,
                                 @RequestParam(value = "month", required = false) Integer month,
                                 HttpServletResponse response) {
        String uriToRedirect = year != null && month != null
                ? String.format("redirect:/admin/intervals?year=%s&month=%s", year, month)
                : "redirect:/admin/intervals";
        try {
            this.intervalService.deleteIntervalById(intervalId);
            redirectAttributes.addFlashAttribute("message", "Интервал удален");
            return uriToRedirect;
        } catch (CustomBindException exception) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            redirectAttributes.addFlashAttribute("message", "Интервал не удален");
            redirectAttributes.addFlashAttribute("errors", exception.getErrors());
            return uriToRedirect;
        }
    }

}