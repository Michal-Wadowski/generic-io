package com.example.genericio;

import com.example.genericio.command.GenericCommand;
import com.example.genericio.response.*;
import com.example.genericio.response.ExtendedResponse.ExtendedResponseArgument;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@AllArgsConstructor
@Slf4j
public class CommandsExecutor {

    private final SerialPortWrapper serialPortWrapper;

    private final Map<Short, Function<ByteBuffer, GenericResponse>> responseCodesMap = Map.ofEntries(
            Map.entry((short) CommandResponseIds.PONG_COMMAND.ordinal(), PongResponse::new),
            Map.entry((short) CommandResponseIds.WRITE_PIN_RESPONSE.ordinal(), WritePinResponse::new),
            Map.entry((short) CommandResponseIds.READ_PIN_RESPONSE.ordinal(), ReadPinResponse::new),
            Map.entry((short) CommandResponseIds.GPIO_INIT_RESPONSE.ordinal(), GpioInitResponse::new),
            Map.entry((short) CommandResponseIds.TIM_START_RESPONSE.ordinal(), TimStartResponse::new),
            Map.entry((short) CommandResponseIds.TIM_STOP_RESPONSE.ordinal(), TimStopResponse::new),
            Map.entry((short) CommandResponseIds.TIM_INIT_RESPONSE.ordinal(), TimInitResponse::new),
            Map.entry((short) CommandResponseIds.TIM_DEINIT_RESPONSE.ordinal(), TimDeInitResponse::new),
            Map.entry((short) CommandResponseIds.TIM_CONFIG_CHANNEL_RESPONSE.ordinal(), TimConfigChannelResponse::new),
            Map.entry((short) CommandResponseIds.TIM_INSTANCE_UPDATE_RESPONSE.ordinal(),
                    TimInstanceUpdateResponse::new),
            Map.entry((short) CommandResponseIds.TIM_INSTANCE_READ_RESPONSE.ordinal(), TimInstanceReadResponse::new),
            Map.entry((short) CommandResponseIds.DMA_INIT_RESPONSE.ordinal(), DmaInitResponse::new),
            Map.entry((short) CommandResponseIds.ADC_INIT_RESPONSE.ordinal(), AdcInitResponse::new),
            Map.entry((short) CommandResponseIds.ADC_CONFIG_CHANNEL_RESPONSE.ordinal(), AdcConfigChannelResponse::new),
            Map.entry((short) CommandResponseIds.ADC_START_RESPONSE.ordinal(), AdcStartResponse::new),
            Map.entry((short) CommandResponseIds.NVIC_SET_PRIORITY_RESPONSE.ordinal(), NVICSetPriorityResponse::new),
            Map.entry((short) CommandResponseIds.NVIC_ENABLE_IRQ_RESPONSE.ordinal(), NvocEnableIrqResponse::new),
            Map.entry((short) CommandResponseIds.COMMAND_UTILS_RESPONSE.ordinal(), CommandUtilsResponse::new),
            Map.entry((short) CommandResponseIds.BUFFER_RESPONSE.ordinal(), BufferResponse::new)
    );

    public GenericResponse sendCommand(GenericCommand command) {
        doSendCommand(command);
        return fetchResponse(serialPortWrapper.getInputStream(), command);
    }

    private GenericResponse fetchResponse(InputStream inputStream, GenericCommand command) {
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
                    GenericResponse response = newInstanceFunction.apply(bytesBuffered);
                    if (response instanceof ExtendedResponse) {
                        ExtendedResponseArgument extendedResponse = new ExtendedResponseArgument(command, inputStream);
                        ((ExtendedResponse) response).extendedResponse(extendedResponse);
                    }
                    return response;
                } else {
                    throw new IllegalArgumentException("Command " + commandId + " not found");
                }
            }
        } catch (IOException ioException) {
            log.warn("fetchResponse() error", ioException);
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
        for (GenericCommand command : commands) {
            InputStream inputStream = serialPortWrapper.getInputStream();
            responses.add(fetchResponse(inputStream, command));
        }
        return responses;
    }
}
