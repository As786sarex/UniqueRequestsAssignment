package com.verve.assignment.uniquerequests.controllers;

import com.verve.assignment.uniquerequests.models.SendCountRequestModel;
import com.verve.assignment.uniquerequests.services.VerveService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class VerveController {

    public static final String FAILED_RESPONSE = "failed";
    public static final String OK_RESPONSE = "ok";

    @NonNull
    private final VerveService verveService;

    public VerveController(@NonNull VerveService verveService) {
        this.verveService = verveService;
    }

    @GetMapping("/api/verve/accept")
    public ResponseEntity<String> acceptRequest(@RequestParam final int id,
                                                @RequestParam(required = false) final String endpoint) {
        try {
            verveService.processAcceptRequest(id, endpoint);
            return ResponseEntity.ok(OK_RESPONSE);
        } catch (Exception e) {
            log.error("Unknown exception occurred with id: {}, endpoint: {}", id, endpoint, e);
            return ResponseEntity.ok(FAILED_RESPONSE);
        }
    }

    @GetMapping("/persist")
    public ResponseEntity<String> persist(@RequestParam final int count) {
        log.info("Received get request with count: {}", count);
        return ResponseEntity.ok(String.valueOf(count));
    }

    @PostMapping("/persist")
    public ResponseEntity<String> persist(@NonNull @RequestBody final SendCountRequestModel requestModel) {
        log.info("Received post request with count: {}", requestModel.getCount());
        return ResponseEntity.ok(String.valueOf(requestModel.getCount()));
    }
}
