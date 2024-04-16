package ru.zhem.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.zhem.entity.IntervalStatus;
import ru.zhem.repository.IntervalRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DatabaseCleanupService {

    private final IntervalRepository intervalRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupIntervalsFromDatabase() {
        this.intervalRepository.deleteOldAvailableIntervals(LocalDate.now(), IntervalStatus.AVAILABLE);
    }

}
