package com.verve.assignment.uniquerequests.services.impl;


import com.verve.assignment.uniquerequests.exceptions.RetryableException;
import com.verve.assignment.uniquerequests.models.SendCountRequestModel;
import com.verve.assignment.uniquerequests.repositories.VerveDataRepository;
import com.verve.assignment.uniquerequests.services.VerveService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.verve.assignment.uniquerequests.configurations.DependencyConfiguration.VERVE_COUNT_TOPIC_NAME_KEY;

@Slf4j
@Service
@Profile("extension")
public class ExtensionVerveService implements VerveService {

    @NonNull
    private final VerveDataRepository verveDataRepository;

    @NonNull
    private final RestTemplate restTemplate;

    @NonNull
    public final KafkaTemplate<String, SendCountRequestModel> kafkaTemplate;

    public ExtensionVerveService(@NonNull final VerveDataRepository verveDataRepository,
                                 @NonNull RestTemplate restTemplate,
                                 @NonNull KafkaTemplate<String, SendCountRequestModel> kafkaTemplate) {
        this.verveDataRepository = verveDataRepository;
        this.restTemplate = restTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void processAcceptRequest(int id, String endpoint) {
        verveDataRepository.upsert(id);
        if (endpoint != null && !endpoint.isBlank()) {
            sendPostRequestToEndpoint(endpoint);
        }
    }

    private void sendPostRequestToEndpoint(String endpoint) {
        final long uniqueCount = verveDataRepository.getUniqueCount();
        final SendCountRequestModel requestModel = SendCountRequestModel.builder().count(uniqueCount).build();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(endpoint, requestModel, String.class);
        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            log.error("Sending http post request failed for endpoint: {} and count: {}", endpoint, uniqueCount);
            throw new RetryableException(String.format(
                    "Sending http post request failed for endpoint: %s and count: %d", endpoint, uniqueCount));
        }

        log.info("Successfully sent request post request failed for endpoint: {} and count: {}", endpoint, uniqueCount);
    }

    @Override
    public void persistUniqueRequest() {
        long fetched = verveDataRepository.fetchAndReset();
        final SendCountRequestModel requestModel = SendCountRequestModel.builder()
                .count(fetched).build();
        kafkaTemplate.send(System.getenv(VERVE_COUNT_TOPIC_NAME_KEY), requestModel)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Sent message=[{}] with offset=[{}]", requestModel, result.getRecordMetadata().offset());
                    } else {
                        log.error("Unable to send message=[{}] due to : ", requestModel, ex);
                    }
                });
    }
}
