package com.verve.assignment.uniquerequests;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class UniqueRequestsAssignmentApplicationTests {

	public static final String TEMPLATE = "http://localhost:8080/api/verve/accept?id=%d&endpoint=http://localhost:8080/persist";
	static HttpClient httpClient = HttpClient.newHttpClient();


	@Test
	void basicTests() throws InterruptedException {

		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for (int i = 0; i <= 1000; i++) {
			Task task = new Task(i);
			executorService.submit(task);
		}
		boolean awaited = executorService.awaitTermination(10, TimeUnit.SECONDS);
	}


	static HttpRequest getHttpRequest(int id) throws URISyntaxException {
		return HttpRequest.newBuilder()
				.uri(new URI(String.format(TEMPLATE, id)))
				.GET()
				.build();
	}

	static class Task implements Callable<String> {

		private final int id;

        Task(int id) {
            this.id = id;
        }

        @Override
		public String call() throws Exception {
			HttpRequest httpRequest = getHttpRequest(id);
			HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
			if (httpResponse.statusCode() != HttpStatus.OK.value()) throw new RuntimeException();
			return httpResponse.body();
		}
	}
}
