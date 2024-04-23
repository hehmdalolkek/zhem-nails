package ru.zhem.service.shedule;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.zhem.entity.Status;
import ru.zhem.repository.IntervalRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DatabaseCleanupService {

    private final IntervalRepository intervalRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupIntervalsFromDatabase() {
        this.intervalRepository.deleteOldAvailableIntervals(LocalDate.now(), Status.AVAILABLE);
        System.out.println(LocalDateTime.now() + " --- The database has been cleared of outdated data.");
    }

}
