package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

@Path("/sensors")
public class SensorResource {

    private static final List<Sensor> sensors = new ArrayList<>();

    static {
        sensors.add(new Sensor("TEMP-001", "Temperature", "ACTIVE", 24.5, "LIB-301"));
        sensors.add(new Sensor("CO2-001", "CO2", "ACTIVE", 420.0, "ENG-102"));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors() {
        return sensors;
    }
}