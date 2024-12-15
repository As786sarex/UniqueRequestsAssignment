package com.verve.assignment.uniquerequests.services.impl;

import com.verve.assignment.uniquerequests.exceptions.RetryableException;
import com.verve.assignment.uniquerequests.repositories.VerveDataRepository;
import com.verve.assignment.uniquerequests.services.VerveService;
import com.verve.assignment.uniquerequests.utils.UniqueCountLoggerUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@Profile("basic")
public class DefaultVerveService implements VerveService {

    @NonNull
    private final VerveDataRepository verveDataRepository;

    @NonNull
    private final RestTemplate restTemplate;

    public DefaultVerveService(@NonNull final VerveDataRepository verveDataRepository,
                               @NonNull final RestTemplate restTemplate) {
        this.verveDataRepository = verveDataRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public void processAcceptRequest(int id, String endpoint) {
        verveDataRepository.upsert(id);
        if (endpoint != null && !endpoint.isBlank()) {
            sendGetRequestToEndpoint(endpoint);
        }
    }

    private void sendGetRequestToEndpoint(String endpoint) {
        final long uniqueCount = verveDataRepository.getUniqueCount();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(endpoint, String.class, Map.of("count", uniqueCount));
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            log.error("Sending http get request failed for endpoint: {} and count: {}", endpoint, uniqueCount);
            throw new RetryableException(String.format(
                    "Sending http get request failed for endpoint: %s and count: %d", endpoint, uniqueCount));
        }

        log.info("Successfully sent request get request failed for endpoint: {} and count: {}", endpoint, uniqueCount);
    }

    @Override
    public void persistUniqueRequest() {
        long fetched = verveDataRepository.fetchAndReset();
        UniqueCountLoggerUtil.logCount(fetched);
    }
}
