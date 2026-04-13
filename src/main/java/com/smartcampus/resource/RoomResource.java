package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/rooms")
public class RoomResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getRooms() {
        return DataStore.rooms;
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId) {
        for (Room room : DataStore.rooms) {
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

        // Null guard — prevents NullPointerException on malformed body
        if (room.getId() == null || room.getId().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Room ID is required")
                    .build();
        }

        for (Room existingRoom : DataStore.rooms) {
            if (existingRoom.getId().equalsIgnoreCase(room.getId())) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Room with this ID already exists")
                        .build();
            }
        }

        DataStore.rooms.add(room);

        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }

    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        for (Room room : DataStore.rooms) {
            if (room.getId().equalsIgnoreCase(roomId)) {

                if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
                    throw new RoomNotEmptyException("Room cannot be deleted: sensors still assigned");
                }

                DataStore.rooms.remove(room);
                return Response.ok("Room deleted successfully").build();
            }
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("Room not found")
                .build();
    }
}