package com.smartcampus.exception.mapper;

import com.smartcampus.exception.SensorMaintenanceException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SensorMaintenanceExceptionMapper implements ExceptionMapper<SensorMaintenanceException> {

    @Override
    public Response toResponse(SensorMaintenanceException ex) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(ex.getMessage())
                .build();
    }
}