package com.example.genericio.response;

import lombok.Getter;

import java.nio.ByteBuffer;

public class TimInstanceReadResponse implements GenericResponse {

    @Getter
    private final int value;

    public TimInstanceReadResponse(ByteBuffer byteBuffer) {
        value = byteBuffer.getInt();
    }

}
