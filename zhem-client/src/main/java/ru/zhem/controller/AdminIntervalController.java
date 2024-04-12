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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.zhem.client.IntervalRestClient;
import ru.zhem.dto.request.DailyIntervalsDto;
import ru.zhem.dto.request.IntervalDto;
import ru.zhem.dto.response.IntervalCreationDto;
import ru.zhem.entity.Status;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/intervals")
public class AdminIntervalController {

    private final IntervalRestClient intervalRestClient;

    @GetMapping
    public String showAllIntervals(@RequestParam(value = "year", required = false) Integer year,
                                   @RequestParam(value = "month", required = false) Integer month, Model model) {
        if (Objects.isNull(year) || Objects.isNull(month)) {
            year = LocalDate.now().getYear();
            month = LocalDate.now().getMonth().getValue();
        }
        YearMonth yearMonth = YearMonth.of(year, month);
        YearMonth prevYearMonth = yearMonth.minusMonths(1);
        YearMonth nextYearMonth = yearMonth.plusMonths(1);
        model.addAttribute("prevYearMonth", prevYearMonth);
        model.addAttribute("nextYearMonth", nextYearMonth);
        model.addAttribute("statusAvailable", Status.AVAILABLE);
        model.addAttribute("mapOfIntervals", this.generateCalendarForMonth(yearMonth));

        return "/admin/intervals/intervals";
    }

    private Map<LocalDate, List<IntervalDto>> generateCalendarForMonth(YearMonth yearMonth) {
        Map<LocalDate, List<IntervalDto>> mapOfIntervals = new LinkedHashMap<>();
        LocalDate startOfMonth = yearMonth.atDay(1);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();

        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)) {
            mapOfIntervals.put(date, new ArrayList<>());
        }

        List<DailyIntervalsDto> dailyIntervals = this.intervalRestClient.findAllIntervals(yearMonth.getYear(), yearMonth.getMonthValue());
        for (DailyIntervalsDto dailyInterval : dailyIntervals) {
            mapOfIntervals.put(dailyInterval.getDate(), dailyInterval.getIntervals());
        }

        return mapOfIntervals;
    }

    @PostMapping
    public String createInterval(@Valid IntervalCreationDto intervalDto, BindingResult bindingResult,
                                 HttpServletResponse response, RedirectAttributes redirectAttributes,
                                 @RequestParam(value = "year", required = false) Integer year,
                                 @RequestParam(value = "month", required = false) Integer month) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            redirectAttributes.addFlashAttribute("created", false);
            redirectAttributes.addFlashAttribute("enteredData", intervalDto);
            redirectAttributes.addFlashAttribute("errors", errors);
        } else {
            this.intervalRestClient.createInterval(intervalDto);
            redirectAttributes.addFlashAttribute("created", true);
        }

        if (Objects.isNull(year) || Objects.isNull(month)) {
            return "redirect:/admin/intervals";
        } else {
            String uriToRedirect = "redirect:/admin/intervals?year=%s&month=%s";
            return String.format(uriToRedirect, year, month);
        }
    }


}