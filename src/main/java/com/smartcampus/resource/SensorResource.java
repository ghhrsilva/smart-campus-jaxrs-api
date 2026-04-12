package com.smartcampus.resource;

import com.smartcampus.exception.InvalidRoomException;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/sensors")
public class SensorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors(@QueryParam("type") String type) {

        if (type == null || type.isBlank()) {
            return DataStore.sensors;
        }

        List<Sensor> filteredSensors = new ArrayList<>();

        for (Sensor sensor : DataStore.sensors) {
            if (sensor.getType().equalsIgnoreCase(type)) {
                filteredSensors.add(sensor);
            }
        }

        return filteredSensors;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSensor(Sensor sensor) {

        for (Sensor existingSensor : DataStore.sensors) {
            if (existingSensor.getId().equalsIgnoreCase(sensor.getId())) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Sensor with this ID already exists")
                        .build();
            }
        }

        Room matchedRoom = null;
        for (Room room : DataStore.rooms) {
            if (room.getId().equalsIgnoreCase(sensor.getRoomId())) {
                matchedRoom = room;
                break;
            }
        }

        if (matchedRoom == null) {
            throw new InvalidRoomException("Invalid roomId: room does not exist");
        }

        DataStore.sensors.add(sensor);
        matchedRoom.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }
}