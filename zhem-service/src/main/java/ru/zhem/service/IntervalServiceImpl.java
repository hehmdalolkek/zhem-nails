package ru.zhem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.zhem.repository.IntervalRepository;

@Service
@RequiredArgsConstructor
public class IntervalServiceImpl implements IntervalService {

    private final IntervalRepository intervalRepository;

}
