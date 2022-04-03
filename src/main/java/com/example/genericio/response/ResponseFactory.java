package com.example.genericio.response;

import com.example.genericio.SerialPortWrapper;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
public class ResponseFactory {

    private final SerialPortWrapper serialPortWrapper;

    private final Map<Short, Function<ByteBuffer, GenericResponse>> responseCodesMap = Map.of(
            PongResponse.COMMAND_ID, PongResponse::new
    );

    public GenericResponse getResponse() {
        try {
            InputStream inputStream = serialPortWrapper.getInputStream();

            ByteBuffer bytesBuffered;
            bytesBuffered = ByteBuffer.wrap(inputStream.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN);

            short responseSize = bytesBuffered.getShort();

            if (responseSize >= 2) {
                byte[] commandBytes = inputStream.readNBytes(responseSize);
                bytesBuffered = ByteBuffer.wrap(commandBytes).order(ByteOrder.LITTLE_ENDIAN);
                short command = bytesBuffered.getShort();

                Function<ByteBuffer, GenericResponse> newInstanceFunction = responseCodesMap.get(command);
                if (newInstanceFunction != null) {
                    return newInstanceFunction.apply(bytesBuffered);
                } else {
                    throw new IllegalArgumentException("Command " + command + " not found");
                }
            }
        } catch (IOException ignored) {
        }

        return null;
    }
}
