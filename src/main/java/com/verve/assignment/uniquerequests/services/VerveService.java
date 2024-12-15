package com.verve.assignment.uniquerequests.services;

public interface VerveService {

    String REQUEST_TEMPLATE = "%s?count=%d";


    void processAcceptRequest(final int id, final String endpoint);

    void persistUniqueRequest();
}
