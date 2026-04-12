package com.smartcampus.store;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    public static final List<Room> rooms = new ArrayList<>();
    public static final List<Sensor> sensors = new ArrayList<>();

    static {
        Room room1 = new Room("LIB-301", "Library Quiet Study", 40);
        room1.getSensorIds().add("TEMP-001");

        Room room2 = new Room("ENG-102", "Engineering Lab", 25);

        rooms.add(room1);
        rooms.add(room2);

        sensors.add(new Sensor("TEMP-001", "Temperature", "ACTIVE", 24.5, "LIB-301"));
        sensors.add(new Sensor("CO2-001", "CO2", "ACTIVE", 420.0, "ENG-102"));
    }
}