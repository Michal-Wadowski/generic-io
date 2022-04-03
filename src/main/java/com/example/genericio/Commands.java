package com.example.genericio;

import lombok.AllArgsConstructor;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static com.example.genericio.Command.PING;

@AllArgsConstructor
public class Commands {

    private final SerialPortWrapper serialPortWrapper;

    public void ping() {
        serialPortWrapper.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
                .putShort((short) 2)
                .putShort(PING.getCommandId())
                .array()
        );
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
                return new Response(Command.fromValue(command));
            }
        } catch (IOException ignored) {
        }
        return null;
    }
}
