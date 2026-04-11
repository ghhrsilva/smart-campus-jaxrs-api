package com.smartcampus.resource;

import com.smartcampus.model.Room;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
}