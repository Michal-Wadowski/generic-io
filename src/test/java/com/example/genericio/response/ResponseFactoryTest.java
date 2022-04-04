package com.example.genericio.response;

import com.example.genericio.SerialPortWrapper;
import com.example.genericio.command.CommandIds;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
        GenericResponse command = responseFactory.getResponse();

        // then
        assertThat(command)
                .isNotNull()
                .isInstanceOf(PongResponse.class);
    }
}