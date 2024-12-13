package com.verve.assignment.uniquerequests.services;

import com.verve.assignment.uniquerequests.models.AcceptRequestModel;

public interface VerveService {

    String processAcceptRequest(final int id, final String endpoint);

    default String processAcceptRequest(final AcceptRequestModel acceptRequestModel) {
        return processAcceptRequest(acceptRequestModel.getId(), acceptRequestModel.getEndpoint());
    }
}
