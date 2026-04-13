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
*Developed for 5COSC022W: Client-Server Architectures (2025/26)*
