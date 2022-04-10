package com.example.genericio.response;

import com.example.genericio.SerialPortWrapper;
import com.example.genericio.command.CommandIds;
import com.example.genericio.command.GenericCommand;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
public class ResponseFactory {

    private final SerialPortWrapper serialPortWrapper;

    private final Map<Short, Function<ByteBuffer, GenericResponse>> responseCodesMap = Map.of(
            (short) CommandIds.PONG_COMMAND.ordinal(), PongResponse::new,
            (short) CommandIds.WRITE_PIN_RESPONSE.ordinal(), WritePinResponse::new,
            (short) CommandIds.READ_PIN_RESPONSE.ordinal(), ReadPinResponse::new,
            (short) CommandIds.GPIO_INIT_RESPONSE.ordinal(), GpioInitResponse::new,
            (short) CommandIds.TIM_START_RESPONSE.ordinal(), TimStartResponse::new,
            (short) CommandIds.TIM_STOP_RESPONSE.ordinal(), TimStopResponse::new,
            (short) CommandIds.TIM_INIT_RESPONSE.ordinal(), TimInitResponse::new,
            (short) CommandIds.TIM_DEINIT_RESPONSE.ordinal(), TimDeInitResponse::new,
            (short) CommandIds.TIM_CONFIG_CHANNEL_RESPONSE.ordinal(), TimConfigChannelResponse::new
    );

    public GenericResponse sendCommand(GenericCommand command) {
        doSendCommand(command);
        return fetchResponse(serialPortWrapper.getInputStream());
    }

    private GenericResponse fetchResponse(InputStream inputStream) {
        try {
            ByteBuffer bytesBuffered;
            bytesBuffered = ByteBuffer.wrap(inputStream.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN);

            short responseSize = bytesBuffered.getShort();

            if (responseSize >= 2) {
                byte[] commandBytes = inputStream.readNBytes(responseSize);
                bytesBuffered = ByteBuffer.wrap(commandBytes).order(ByteOrder.LITTLE_ENDIAN);
                short commandId = bytesBuffered.getShort();

                Function<ByteBuffer, GenericResponse> newInstanceFunction = responseCodesMap.get(commandId);
                if (newInstanceFunction != null) {
                    return newInstanceFunction.apply(bytesBuffered);
                } else {
                    throw new IllegalArgumentException("Command " + commandId + " not found");
                }
            }
        } catch (IOException ignored) {
        }

        return null;
    }

    private void doSendCommand(GenericCommand command) {
        serialPortWrapper.write(command.getBytes());
    }

    public List<GenericResponse> sendCommands(GenericCommand... commands) {
        for (GenericCommand command : commands) {
            doSendCommand(command);
        }

        ArrayList<GenericResponse> responses = new ArrayList<>(commands.length);
        for (int i = 0; i < commands.length; i++) {
            InputStream inputStream = serialPortWrapper.getInputStream();
            responses.add(fetchResponse(inputStream));
        }
        return responses;
    }
}
