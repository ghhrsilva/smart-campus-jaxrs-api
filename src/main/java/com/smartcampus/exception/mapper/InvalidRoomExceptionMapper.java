package com.smartcampus.exception.mapper;

import com.smartcampus.exception.InvalidRoomException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidRoomExceptionMapper implements ExceptionMapper<InvalidRoomException> {

    @Override
    public Response toResponse(InvalidRoomException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ex.getMessage())
                .build();
    }
}