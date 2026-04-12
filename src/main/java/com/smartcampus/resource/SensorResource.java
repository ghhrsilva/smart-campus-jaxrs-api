package com.smartcampus.resource;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/sensors")
public class SensorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors() {
        return DataStore.sensors;
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
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid roomId: room does not exist")
                    .build();
        }

        DataStore.sensors.add(sensor);
        matchedRoom.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }
}