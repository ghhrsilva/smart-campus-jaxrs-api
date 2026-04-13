# 🏛️ Smart Campus JAX-RS API
### *Enterprise-Grade Facility & Sensor Management Service*

A high-performance, RESTful web service engineered with **Jakarta RESTful Web Services (JAX-RS)** and powered by an embedded **Grizzly HTTP Server**. This project demonstrates advanced client-server architectural patterns, resource nesting, and real-time observability.

---

## 🏗️ 1. API Design Overview

The architecture is built on three core pillars of modern RESTful design:

*   **Logical Resource Hierarchy**: The API reflects the physical campus structure. Rooms are primary resources, while Sensors and Readings are modeled as nested sub-resources to maintain tight data integrity.
*   **Sub-Resource Locator Pattern**: To manage complexity, we utilize a delegated routing strategy. The `SensorResource` acts as a locator, delegating historical data management to a dedicated `SensorReadingResource`. This ensures clean separation of concerns.
*   **Observability & Error Resilience**:
    - **Global Filtering**: Every request and response is intercepted by a JAX-RS filter for standardized logging.
    - **Exception Mapping**: Business logic failures (such as maintenance constraints or room occupancy rules) are automatically mapped to semantically accurate HTTP status codes (400, 403, 409).
*   **In-Memory Architecture**: Uses thread-safe data structures to simulate a high-speed campus environment without external database dependencies.

---

## 🚀 2. Build & Launch Instructions

### Prerequisites
- **Java 21 JDK** (Recommended)
- **Apache Maven 3.x**

### Step-by-Step Launch
1.  **Clone & Navigate**:
    ```bash
    git clone [your-repo-link]
    cd smart-campus-jaxrs-api
    ```
2.  **Compile & Install**:
    ```bash
    mvn clean install
    ```
3.  **Start the Server**:
    Run the `Main` class directly via Maven:
    ```bash
    mvn exec:java -Dexec.mainClass="com.smartcampus.Main"
    ```
    *The server will be available at:* `http://localhost:8080/api/v1/`

---

## 🧪 3. Sample curl Commands

Use these commands to verify the core functionality and business logic constraints:

| Feature | curl Command | Expected Result |
| :--- | :--- | :--- |
| **API Discovery** | `curl -X GET http://localhost:8080/api/v1/` | Base API info & links |
| **Filtering** | `curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"` | List of Temperature sensors |
| **Business Rule** | `curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301` | **409 Conflict** (Sensors assigned) |
| **Sub-Resource** | `curl -X POST -H "Content-Type: application/json" -d '{"value":22.5}' http://localhost:8080/api/v1/sensors/TEMP-001/readings` | **201 Created** (With UUID) |
| **Constraint** | `curl -X POST -H "Content-Type: application/json" -d '{"value":5.0}' http://localhost:8080/api/v1/sensors/CO2-001/readings` | **403 Forbidden** (Maintenance mode) |

---

## 📁 4. Project Structure
- `com.smartcampus.model`: POJO data models (Room, Sensor, SensorReading).
- `com.smartcampus.resource`: JAX-RS Resource classes and sub-resource locators.
- `com.smartcampus.exception`: Custom business exceptions and mappers.
- `com.smartcampus.filter`: HTTP logging and observability filters.
- `com.smartcampus.store`: In-memory thread-safe data structures.

---

## 📑 5. Conceptual Report (Theory & Design)

### Part 1: Service Architecture & Setup
**Q1.1: Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this impacts synchronization.**
**Answer:** By default, JAX-RS resource classes are request-scoped. A new instance is created for every incoming HTTP request and discarded after the response. Because of this, class-level fields are not shared between requests. In this project, I used static collections within a centralized `DataStore` class to persist data. This requires careful synchronization (using thread-safe collections) to prevent race conditions when multiple clients interact with the API simultaneously.

**Q1.2: Why is the provision of "Hypermedia" (HATEOAS) considered a hallmark of advanced RESTful design? How does this benefit developers?**
**Answer:** HATEOAS makes an API self-descriptive. By providing links in the response (as seen in our discovery endpoint), the client doesn't need to hardcode URLs. If the API structure evolves, the client can follow the provided links instead of needing code updates. This decouples the client from the server’s internal resource structure.

### Part 2: Room Management
**Q2.1: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects?**
**Answer:** Returning only IDs saves bandwidth, which is ideal for low-speed clients, but it requires "N+1" requests to fetch details for each room. Returning full objects increases the initial payload size but allows the client to populate a full UI view in a single round-trip, improving performance in high-latency environments.

**Q2.2: Is the DELETE operation idempotent in your implementation? Provide a detailed justification.**
**Answer:** Yes, the DELETE operation is idempotent. The first call removes the resource (200 OK). Subsequent calls for the same ID return 404 Not Found. While the status codes differ, the side effect on the server is the same: the room remains deleted. Multiple calls have the same outcome as a single call.

### Part 3: Sensor Operations
**Q3.1: Explain the technical consequences if a client attempts to send data in a different format (e.g., XML) when @Consumes(MediaType.APPLICATION_JSON) is used.**
**Answer:** The JAX-RS runtime will reject the request and return an **HTTP 415 Unsupported Media Type** error. This ensures our internal POJO mapping logic is never exposed to malformed or unexpected data formats that it cannot parse.

**Q3.2: Contrast using @QueryParam vs @PathParam for filtering. Why is the query parameter approach generally considered superior for searching?**
**Answer:** Path parameters identify a specific resource, whereas query parameters modify a result set. The query parameter approach is superior for searching because it allows for optional, combinable, and flexible criteria (like `?type=CO2`) without "polluting" the URL hierarchy or breaking the hierarchical path structure.

### Part 4: Deep Nesting with Sub-Resources
**Q4.1: Discuss the architectural benefits of the Sub-Resource Locator pattern.**
**Answer:** It promotes the Single Responsibility Principle. By delegating nested routes (like `/sensors/{id}/readings`) to a dedicated `SensorReadingResource` class, the main `SensorResource` stays clean. This prevents the "God Class" anti-pattern and makes the code easier to maintain and test.

### Part 5: Advanced Error Handling & Logging
**Q5.1: Why is HTTP 422 often considered more semantically accurate than 404 when the issue is a missing reference inside a valid JSON payload?**
**Answer:** 404 implies the URL does not exist. However, if the address is correct but the data (like a roomId) is logically invalid, **422 Unprocessable Entity** accurately tells the client that the request was understood, but the content is semantically invalid.

**Q5.2: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces.**
**Answer:** Stack traces reveal sensitive details like class names, directory structures, and library versions. Attackers use this to identify specific vulnerabilities (CVEs) or understand the server's internal logic to craft precise injection attacks. Our `GlobalExceptionMapper` prevents this by returning generic error messages.

**Q5.3: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging?**
**Answer:** Filters centralize logging logic, applying it universally to all requests and responses. This eliminates code duplication, ensuring consistent observability across all endpoints, and allows us to modify the logging format in one single file.

---
*Developed for 5COSC022W: Client-Server Architectures (2025/26)*
