package com.verve.assignment.uniquerequests.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerveController {

    @GetMapping("/api/verve/accept")
    public ResponseEntity<String> acceptRequest(@RequestParam final int id,
                                                @RequestParam(required = false) final String endpoint) {
        return ResponseEntity.ok("ok");
    }
}
