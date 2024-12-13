package com.verve.assignment.uniquerequests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UniqueRequestsAssignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniqueRequestsAssignmentApplication.class, args);
	}

}
