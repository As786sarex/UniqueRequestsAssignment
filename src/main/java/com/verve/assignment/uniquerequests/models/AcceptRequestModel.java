package com.verve.assignment.uniquerequests.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcceptRequestModel {
    private Integer id;
    private String endpoint;
}
