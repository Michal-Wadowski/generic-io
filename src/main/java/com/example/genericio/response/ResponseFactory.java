package com.example.genericio.response;

import com.example.genericio.SerialPortWrapper;
import com.example.genericio.command.CommandIds;
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
            (short)CommandIds.PONG_COMMAND.ordinal(), PongResponse::new,
            (short)CommandIds.WRITE_PIN_OK.ordinal(), WritePinResponse::new,
            (short)CommandIds.READ_PIN_RESULT.ordinal(), ReadPinResponse::new,
            (short)CommandIds.GPIO_INIT_OK.ordinal(), GpioInitResponse::new
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
