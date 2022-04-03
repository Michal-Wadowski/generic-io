package com.example.genericio.response;

import java.nio.ByteBuffer;

public class PongResponse implements GenericResponse {

    public static final short COMMAND_ID = 2;

    public PongResponse(ByteBuffer byteBuffer) {
    }

}
