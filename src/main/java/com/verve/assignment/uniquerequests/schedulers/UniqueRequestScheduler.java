package com.verve.assignment.uniquerequests.schedulers;

import com.verve.assignment.uniquerequests.services.VerveService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class UniqueRequestScheduler {

    @NonNull
    private final VerveService verveService;

    public UniqueRequestScheduler(@NonNull VerveService verveService) {
        this.verveService = verveService;
    }

    @Scheduled(fixedRate = 60000)
    public void persistUniqueRequestCount() {
        log.info("Persisting unique request count to disk at timestamp: {}", LocalDateTime.now());
        verveService.persistUniqueRequest();
        log.info("Successfully persisted unique request count to disk at timestamp: {}", LocalDateTime.now());
    }
}
