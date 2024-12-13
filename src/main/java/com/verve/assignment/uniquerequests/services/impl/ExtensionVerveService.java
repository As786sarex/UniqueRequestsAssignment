package com.verve.assignment.uniquerequests.services.impl;


import com.verve.assignment.uniquerequests.exceptions.NotImplementedException;
import com.verve.assignment.uniquerequests.services.VerveService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("extension")
public class ExtensionVerveService implements VerveService {

    @Override
    public String processAcceptRequest(final int id, final String endpoint) {
        throw new NotImplementedException("Extension verve service does not implement this function.");
    }
}
