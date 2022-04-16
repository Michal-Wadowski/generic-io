package com.example.genericio.response;

import java.nio.ByteBuffer;
import java.util.stream.IntStream;

public class NVICSetPriorityResponse extends VoidResponse {

    public NVICSetPriorityResponse(ByteBuffer byteBuffer) {
        super(byteBuffer);
    }
}
