package com.example.genericio.response;

import com.example.genericio.SerialPortWrapper;
import com.example.genericio.command.CommandFactory;
import com.example.genericio.command.CommandIds;
import com.example.genericio.command.GPIO;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseFactoryTest {

    private final CommandFactory commandFactory = new CommandFactory();

    @Test
    void should_create_PongResponse_from_value() {
        // given
        SerialPortWrapper serialPortWrapper = new SerialPortWrapper() {
            @Override
            public void write(byte[] bytes) {

            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(new byte[]{2, 0, (byte) CommandIds.PONG_COMMAND.ordinal(), 0});
            }

            @Override
            public void close() {

            }
        };
        ResponseFactory responseFactory = new ResponseFactory(serialPortWrapper);

        // where
        GenericResponse response = responseFactory.sendCommand(commandFactory.ping());

        // then
        assertThat(response)
                .isNotNull()
                .isInstanceOf(PongResponse.class);
    }

    @Test
    void ResponseFactory_should_send_multiple_commands() {
        // given
        final int[] writingCount = {0};

        SerialPortWrapper serialPortWrapper = new SerialPortWrapper() {

            @Override
            public void write(byte[] bytes) {
                writingCount[0]++;
            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(new byte[]{
                        2, 0, (byte) CommandIds.PONG_COMMAND.ordinal(), 0,
                        3, 0, (byte) CommandIds.READ_PIN_RESULT.ordinal(), 0, 0
                });
            }

            @Override
            public void close() {

            }
        };
        ResponseFactory responseFactory = new ResponseFactory(serialPortWrapper);

        // when
        List<GenericResponse> responses = responseFactory.sendCommands(
                commandFactory.ping(),
                commandFactory.readPin().setPin(GPIO.Pin.GPIO_PIN_0).setPort(GPIO.Port.GPIOA)
        );

        // then
        assertThat(writingCount[0])
                .isEqualTo(2);

        assertThat(responses)
                .isNotNull()
                .hasSize(2);
    }
}