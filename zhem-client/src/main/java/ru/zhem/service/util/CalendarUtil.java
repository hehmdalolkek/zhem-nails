package ru.zhem.service.util;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;

@Component
public class CalendarUtil {

    public YearMonth calcPrevNextMonth(Model model, Integer year, Integer month) {
        if (Objects.isNull(year) || Objects.isNull(month)) {
            year = LocalDate.now().getYear();
            month = LocalDate.now().getMonthValue();
        }
        YEAR.checkValidValue(year);
        MONTH_OF_YEAR.checkValidValue(month);
        YearMonth yearMonth = YearMonth.of(year, month);
        YearMonth prevYearMonth = yearMonth.minusMonths(1);
        YearMonth nextYearMonth = yearMonth.plusMonths(1);
        model.addAttribute("prevYearMonth", prevYearMonth);
        model.addAttribute("nextYearMonth", nextYearMonth);
        model.addAttribute("yearMonth", yearMonth);
        return yearMonth;
    }

}
