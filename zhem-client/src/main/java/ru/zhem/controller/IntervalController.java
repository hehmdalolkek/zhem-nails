package ru.zhem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.zhem.controller.util.ControllerUtil;
import ru.zhem.service.interfaces.IntervalService;
import ru.zhem.service.util.CalendarUtil;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

@RequiredArgsConstructor
@Controller
@RequestMapping("/intervals")
public class IntervalController {

    private final ControllerUtil controllerUtil;

    private final CalendarUtil calendarUtil;

    private final IntervalService intervalService;

    @GetMapping
    public String initShowAvailableIntervals(@RequestParam(value = "year", required = false) Integer year,
                                             @RequestParam(value = "month", required = false) Integer month, Model model,
                                             Locale locale) {
        YearMonth yearMonth = this.calendarUtil.calcPrevNextMonth(model, year, month);
        model.addAttribute("mapOfIntervals",
                this.controllerUtil.generateIntervalCalendarForMonth(yearMonth, true));
        model.addAttribute("mapOfIntervalsIsEmpty",
                this.intervalService.findAllAvailableIntervals(year, month).isEmpty());
        String monthTitle = yearMonth.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, locale);
        monthTitle = monthTitle.replaceFirst(monthTitle.substring(0, 1), monthTitle.substring(0, 1).toUpperCase());
        model.addAttribute("monthTitle", monthTitle);
        return "common/intervals/intervals";
    }

}
