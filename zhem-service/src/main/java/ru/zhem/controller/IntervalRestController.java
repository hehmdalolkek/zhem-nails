package ru.zhem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.zhem.service.IntervalService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/service-api/v1/intervals")
public class IntervalRestController {

    private final IntervalService intervalService;

}
