package com.smartcampus.model;

import java.util.UUID;

public class SensorReading {

    private String id;        // Unique reading event ID (UUID)
    private double value;
    private long timestamp;

    public SensorReading() {}

    public SensorReading(double value, long timestamp) {
        this.id = UUID.randomUUID().toString();
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}