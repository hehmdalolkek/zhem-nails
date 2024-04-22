package ru.zhem.controller.common;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.zhem.exceptions.BadRequestException;
import ru.zhem.exceptions.InvalidDateException;
import ru.zhem.service.CalendarService;
import ru.zhem.service.IntervalService;

import java.time.YearMonth;

@RequiredArgsConstructor
@Controller
@RequestMapping("/intervals")
public class IntervalController {

    private final IntervalService intervalService;

    private final CalendarService calendarService;

    @GetMapping
    public String initShowAvailableIntervals(@RequestParam(value = "year", required = false) Integer year,
                                             @RequestParam(value = "month", required = false) Integer month, Model model) {
        try {
            YearMonth yearMonth = this.calendarService.calcPrevNextMonth(model, year, month);
            model.addAttribute("mapOfIntervals",
                    this.intervalService.generateIntervalCalendarForMonth(yearMonth, true));
            return "common/intervals/intervals";
        } catch (InvalidDateException exception) {
            throw new BadRequestException(ProblemDetail.forStatusAndDetail(
                    HttpStatus.BAD_REQUEST, "Invalid date"
            ));
        }
    }

}
