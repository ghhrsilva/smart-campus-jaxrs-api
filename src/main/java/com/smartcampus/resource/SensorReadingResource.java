package com.smartcampus.resource;

import com.smartcampus.exception.SensorMaintenanceException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

// NOTE: No class-level @Path here — this is a Sub-Resource class.
// It is reached via the locator method in SensorResource.
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadings() {
        for (Sensor sensor : DataStore.sensors) {
            if (sensor.getId().equalsIgnoreCase(sensorId)) {
                return Response.ok(sensor.getReadings()).build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("Sensor not found")
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
        for (Sensor sensor : DataStore.sensors) {
            if (sensor.getId().equalsIgnoreCase(sensorId)) {

                if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
                    throw new SensorMaintenanceException("Sensor is under maintenance");
                }

                // Auto-generate UUID if client did not supply one
                if (reading.getId() == null || reading.getId().isBlank()) {
                    reading.setId(UUID.randomUUID().toString());
                }

                reading.setTimestamp(System.currentTimeMillis());
                sensor.getReadings().add(reading);
                sensor.setCurrentValue(reading.getValue());

                return Response.status(Response.Status.CREATED)
                        .entity(reading)
                        .build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("Sensor not found")
                .build();
    }
}