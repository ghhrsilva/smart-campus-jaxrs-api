package com.smartcampus.resource;

import com.smartcampus.model.Room;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/rooms")
public class RoomResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getRooms() {
        List<Room> rooms = new ArrayList<>();

        rooms.add(new Room("LIB-301", "Library Quiet Study", 40));
        rooms.add(new Room("ENG-102", "Engineering Lab", 25));

        return rooms;
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId) {
        List<Room> rooms = new ArrayList<>();

        rooms.add(new Room("LIB-301", "Library Quiet Study", 40));
        rooms.add(new Room("ENG-102", "Engineering Lab", 25));

        for (Room room : rooms) {
            if (room.getId().equalsIgnoreCase(roomId)) {
                return Response.ok(room).build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("Room not found")
                .build();
    }
}