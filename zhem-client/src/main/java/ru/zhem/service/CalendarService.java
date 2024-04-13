package ru.zhem.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.zhem.exceptions.BadRequestException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

@Service
public class CalendarService {

    public YearMonth calcPrevNextMonth(Model model, Integer year, Integer month) {
        if (Objects.isNull(year) || Objects.isNull(month)) {
            year = LocalDate.now().getYear();
            month = LocalDate.now().getMonthValue();
        }
        if (month < 1 || month > 12) {
            throw new BadRequestException(
                    ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid date"));
        }
        YearMonth yearMonth = YearMonth.of(year, month);
        YearMonth prevYearMonth = yearMonth.minusMonths(1);
        YearMonth nextYearMonth = yearMonth.plusMonths(1);
        model.addAttribute("prevYearMonth", prevYearMonth);
        model.addAttribute("nextYearMonth", nextYearMonth);
        return yearMonth;
    }

}
