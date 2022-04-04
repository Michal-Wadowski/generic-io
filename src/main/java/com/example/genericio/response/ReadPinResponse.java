package com.example.genericio.response;

import lombok.Getter;

import java.nio.ByteBuffer;

public class ReadPinResponse implements GenericResponse {

    @Getter
    private final boolean isSet;

    public ReadPinResponse(ByteBuffer byteBuffer) {
        isSet = byteBuffer.get() != 0;
    }

}
