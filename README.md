# Smart Campus JAX-RS API

A high-performance RESTful web service for managing campus facilities, rooms, and sensors. This project is built using **JAX-RS (Jersey)** and an embedded **Grizzly HTTP Server**, utilizing in-memory data structures for high-speed resource management.

## 🏛️ API Design Overview

The API follows a strict resource-based hierarchy designed to reflect the physical structure of a campus:

- **Resource Nesting**: Employs the **Sub-Resource Locator** pattern for sensor readings (`/sensors/{id}/readings`), ensuring logic is delegated to specialized resource handlers.
- **Architectural Patterns**:
    - **Discovery**: Root endpoint provide hypermedia-style navigation links.
    - **Exception Mapping**: Custom `ExceptionMappers` translate business logic failures into specific HTTP status codes (400, 403, 409).
    - **Observability**: A custom `ContainerRequestFilter` and `ContainerResponseFilter` log every interaction using `java.util.logging.Logger`.

## 🛠️ Build and Launch Instructions

### Prerequisites
- Java 21
- Maven 3.x

### 1. Build the Project
Open your terminal in the root directory and run:
```bash
mvn clean install
```

### 2. Launch the Server
You can start the server directly using the Main class:
```bash
mvn exec:java -Dexec.mainClass="com.smartcampus.Main"
```
The server will start at: `http://localhost:8080/api/v1/`

---

## 🧪 Sample Interactions (Test Suite)

Below are five core interactions to verify the system integrity:

### 1. API Discovery
Explore available resource collections:
```bash
curl -X GET http://localhost:8080/api/v1/
```

### 2. Filtered Retrieval
Search for a specific type of sensor across campus:
```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"
```

### 3. Business Logic: Room Deletion Constraint
Attempt to delete a room that still has active sensors (Expect 409 Conflict):
```bash
curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301
```

### 4. Advanced Reading Submission (Sub-Resource)
Post a new reading to a specific sensor. The system will auto-generate a UUID and timestamp:
```bash
curl -X POST -H "Content-Type: application/json" -d '{"value":22.5}' http://localhost:8080/api/v1/sensors/TEMP-001/readings
```

### 5. Maintenance Mode Constraint
Attempt to post a reading to a sensor marked for maintenance (Expect 403 Forbidden):
```bash
curl -X POST -H "Content-Type: application/json" -d '{"value":500.0}' http://localhost:8080/api/v1/sensors/CO2-001/readings
```

---

## 📁 Project Structure
- `com.smartcampus.model`: POJO data models (Room, Sensor, SensorReading).
- `com.smartcampus.resource`: JAX-RS Resource classes and sub-resource locators.
- `com.smartcampus.exception`: Custom business exceptions and mappers.
- `com.smartcampus.filter`: HTTP logging and observability filters.
- `com.smartcampus.store`: In-memory thread-safe data structures.
