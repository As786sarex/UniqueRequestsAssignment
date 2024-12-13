package com.verve.assignment.uniquerequests.services.impl;

import com.verve.assignment.uniquerequests.repositories.VerveDataRepository;
import com.verve.assignment.uniquerequests.services.VerveService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("basic")
public class DefaultVerveService implements VerveService {


    private final VerveDataRepository verveDataRepository;

    public DefaultVerveService(VerveDataRepository verveDataRepository) {
        this.verveDataRepository = verveDataRepository;
    }

    @Override
    public String processAcceptRequest(int id, String endpoint) {
        return "";
    }
}
