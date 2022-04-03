package com.example.genericio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Commands {

    private SerialPortWrapper serialPortWrapper;

    public Commands(SerialPortWrapper serialPortWrapper) {
        this.serialPortWrapper = serialPortWrapper;
    }

    public void ping() {
        serialPortWrapper.write(new byte[]{2, 0, 1, 0});
    }

    public Response getResponse() {
        try {
            InputStream inputStream = serialPortWrapper.getInputStream();

            ByteBuffer bytesBuffered;
            bytesBuffered = ByteBuffer.wrap(inputStream.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN);

            short size = bytesBuffered.getShort();

            if (size >= 2) {
                bytesBuffered = ByteBuffer.wrap(inputStream.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN);
                short command = bytesBuffered.getShort();

                return new Response(command);
            }
        } catch (IOException ignored) {
        }
        return null;
    }
}
