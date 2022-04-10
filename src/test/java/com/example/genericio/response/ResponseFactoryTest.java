package com.example.genericio.response;

import com.example.genericio.SerialPortWrapper;
import com.example.genericio.command.CommandIds;
import com.example.genericio.command.GPIO;
import com.example.genericio.command.PingCommand;
import com.example.genericio.command.ReadPin;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseFactoryTest {

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
        GenericResponse response = responseFactory.sendCommand(new PingCommand());

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
                        3, 0, (byte) CommandIds.READ_PIN_RESPONSE.ordinal(), 0, 0
                });
            }

            @Override
            public void close() {

            }
        };
        ResponseFactory responseFactory = new ResponseFactory(serialPortWrapper);

        // when
        List<GenericResponse> responses = responseFactory.sendCommands(
                new PingCommand(),
                ReadPin.builder().pin(GPIO.Pin.GPIO_PIN_0).port(GPIO.Port.GPIOA).build()
        );

        // then
        assertThat(writingCount[0])
                .isEqualTo(2);

        assertThat(responses)
                .isNotNull()
                .hasSize(2);
    }
}