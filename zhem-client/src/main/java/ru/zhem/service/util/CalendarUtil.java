package ru.zhem.service.util;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ru.zhem.exceptions.InvalidDateException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

@Component
public class CalendarUtil {

    public YearMonth calcPrevNextMonth(Model model, Integer year, Integer month) {
        if (Objects.isNull(year) || Objects.isNull(month)) {
            year = LocalDate.now().getYear();
            month = LocalDate.now().getMonthValue();
        }
        if (month < 1 || month > 12) {
            throw new InvalidDateException("Invalid date");
        }
        YearMonth yearMonth = YearMonth.of(year, month);
        YearMonth prevYearMonth = yearMonth.minusMonths(1);
        YearMonth nextYearMonth = yearMonth.plusMonths(1);
        model.addAttribute("prevYearMonth", prevYearMonth);
        model.addAttribute("nextYearMonth", nextYearMonth);
        model.addAttribute("yearMonth", yearMonth);
        return yearMonth;
    }

}
