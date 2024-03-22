package ru.zhem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.zhem.client.WorkIntervalRestClient;

@RequiredArgsConstructor
@Controller
@RequestMapping("/workintervals")
public class WorkIntervalController {

    private final WorkIntervalRestClient workIntervalRestClient;

    @GetMapping
    public String getWorkIntervalsList(Model model) {
        model.addAttribute("workIntervals", this.workIntervalRestClient.findAllWorkIntervals());
        return "service/workIntervals/list";
    }

}
