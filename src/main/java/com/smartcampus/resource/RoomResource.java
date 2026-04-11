package com.smartcampus.resource;

import com.smartcampus.model.Room;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/rooms")
public class RoomResource {

    private static final List<Room> rooms = new ArrayList<>();

    static {
        rooms.add(new Room("LIB-301", "Library Quiet Study", 40));
        rooms.add(new Room("ENG-102", "Engineering Lab", 25));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getRooms() {
        return rooms;
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId) {
        for (Room room : rooms) {
            if (room.getId().equalsIgnoreCase(roomId)) {
                return Response.ok(room).build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("Room not found")
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {
        for (Room existingRoom : rooms) {
            if (existingRoom.getId().equalsIgnoreCase(room.getId())) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Room with this ID already exists")
                        .build();
            }
        }

        rooms.add(room);

        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }

    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        for (Room room : rooms) {
            if (room.getId().equalsIgnoreCase(roomId)) {
                rooms.remove(room);
                return Response.ok("Room deleted successfully").build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("Room not found")
                .build();
    }
}