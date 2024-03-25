package ru.zhem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.zhem.client.WorkIntervalRestClient;

@RequiredArgsConstructor
@Controller
public class CommonController {

    private final WorkIntervalRestClient workIntervalRestClient;

    // TODO реализовать выдачу разных представлений для гостя, юзера и админа
    @GetMapping("/workintervals")
    public String getAvailableWorkIntervals(Model model) {
        model.addAttribute("workIntervals",
                this.workIntervalRestClient.findAvailableWorkIntervalsGroupedByDate());
        return "service/common/workIntervals/list";
    }
}
