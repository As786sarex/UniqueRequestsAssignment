# Thought Process and Design

## How to run this application

### Prerequisites
1. Docker Desktop

### Steps to run

1. Clone this GitHub repository using the following command.
```bash
  git clone git@github.com:As786sarex/UniqueRequestsAssignment.git
```
2. Run application using docker compose.
```bash
  docker-compose up --build
```

---

When planning the solution, I focused on ensuring the application is easy to scale, reliable, and capable of handling
high traffic. The goal was to meet all the requirements while keeping the design straightforward and efficient. Here's
how I approached it:

---

## **Handling 10,000 Requests Per Second**

To process a large number of requests, I used **Spring Boot (Spring Web)** because it is efficient and well-suited for
building REST APIs. I also configured the Tomcat server to handle high traffic by:

1. Adjusting the thread pool size to allow more concurrent requests.
2. Tuning connection timeouts for better resource management.

I used thread-safe data structures like `ConcurrentHashMap` to handle request processing without slowing down the
system.

---

## **Endpoint Design**

The `/api/verve/accept` endpoint was designed to:

1. Accept a required query parameter `id` (integer) and an optional query parameter `endpoint` (string).
2. Return `"ok"` if the request was processed successfully and `"failed"` in case of any issues.
3. If an `endpoint` is provided, the application sends an HTTP request with the count of unique IDs received in the
   current minute.

---

## **Tracking Unique Requests**

To track unique requests within a minute:

1. I used a `ConcurrentHashMap` to store IDs in memory. This is fast and ensures thread safety while allowing the
   application to process multiple requests at the same time.
2. For scenarios where the application is deployed with multiple instances (behind a load balancer), I used **Redis** as
   a distributed cache. Redis helps synchronize IDs across all instances, ensuring that duplicates are identified even
   when processed by different instances.

---

## **Logging Unique Request Counts**

Every minute, the application logs the count of unique `id` values received. I used **Spring’s scheduling feature** to
execute a task every 60 seconds. This task:

1. Counts the unique IDs stored in memory or Redis.
2. Logs the count using **SLF4J**, a standard logging library.
3. Clears the stored IDs to prepare for the next minute.

---

## **Handling the Optional Endpoint Parameter**

If the `endpoint` parameter is provided:

1. The application sends an HTTP GET request to the specified URL, adding the unique count as a query parameter (e.g.,
   `?count=100`).
2. For better extensibility (as per Extension 1), I also designed a POST request option where the application sends the
   unique count in a JSON payload.
3. The application logs the HTTP response status code for tracking and debugging.

I used **RestTemplate** to handle these HTTP calls, as it is simple and integrates well with Spring Web.

---

## **Extensions**

### **Extension 1: Replace GET with POST**

Instead of appending the unique count as a query parameter in a GET request, the application sends it as a JSON payload
in a POST request. This provides more flexibility in the data structure sent to the external endpoint.

### **Extension 2: Distributed Deduplication**

To ensure deduplication across multiple instances, I used **Redis**. Redis acts as a central cache, storing all unique
IDs globally. This way, even if two instances receive the same `id` at the same time, only one of them will process it
as unique.

### **Extension 3: Replace Logging with Streaming**

Instead of logging the count of unique requests to a file, the application streams this data to **Kafka**. This allows
other systems to consume the unique counts in real-time, enabling better integration with monitoring tools or analytics
platforms.

---

## **High-Level Architecture**

The system is broken into these main parts:

1. **REST Controller**: Handles incoming requests and validates the input parameters.
2. **Request Processing Service**: Handles deduplication and processes the optional `endpoint` parameter.
3. **Scheduler**: Logs or streams the count of unique IDs every minute.
4. **Distributed Cache (Redis)**: Ensures deduplication works across multiple instances.
5. **Streaming Service (Kafka)**: Sends unique counts to a Kafka topic instead of writing them to a file.

---

## **Scalability and Performance Considerations**

1. **Efficient Request Handling**: Configured the Tomcat thread pool to handle many concurrent connections without
   delays.
2. **Fast Deduplication**: Used `ConcurrentHashMap` for in-memory operations and Redis for distributed deduplication.
3. **Real-Time Streaming**: Replaced file-based logging with Kafka for better scalability and integration with other
   systems.
4. **Error Handling**: Added proper error handling for scenarios like Redis unavailability or HTTP request failures,
   ensuring the application remains stable under load.

---

By keeping the design simple and modular, the application is easy to extend and maintain while being capable of handling
high traffic efficiently.