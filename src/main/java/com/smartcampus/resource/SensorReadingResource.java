package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/sensors/{sensorId}/readings")
public class SensorReadingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getReadings(@PathParam("sensorId") String sensorId) {
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
    public Response addReading(@PathParam("sensorId") String sensorId, SensorReading reading) {
        for (Sensor sensor : DataStore.sensors) {
            if (sensor.getId().equalsIgnoreCase(sensorId)) {

                reading.setTimestamp(System.currentTimeMillis());
                sensor.getReadings().add(reading);

                // coursework side effect: update current sensor value
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