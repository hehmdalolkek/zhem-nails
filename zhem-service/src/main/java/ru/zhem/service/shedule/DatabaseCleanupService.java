package ru.zhem.service.shedule;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.zhem.entity.Status;
import ru.zhem.repository.IntervalRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseCleanupService {

    private final IntervalRepository intervalRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupIntervalsFromDatabase() {
        this.intervalRepository.deleteOldAvailableIntervals(LocalDate.now(), Status.AVAILABLE);
        log.info("The database has been cleared of outdated data.");
    }

}
