package com.example.genericio;

import com.example.genericio.command.CommandFactory;
import com.example.genericio.response.GenericResponse;
import com.example.genericio.response.PongResponse;
import com.example.genericio.response.ResponseFactory;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandFactoryIntegrationTest {

    @Test
    void ping_and_getResponse_should_use_real_communication() throws IOException {
        // given
        Configuration configuration = new Configuration() {
            @Override
            public String getSerialPortPath() {
                return "/dev/ttyACM0";
            }
        };

        SerialPortWrapper serialPortWrapper = new SerialPortWrapperImpl(configuration);

        CommandFactory commandFactory = new CommandFactory(serialPortWrapper);
        ResponseFactory responseFactory = new ResponseFactory(serialPortWrapper);

        // when
        commandFactory.ping().send();
        GenericResponse response = responseFactory.getResponse();

        // then
        assertThat(response)
                .isNotNull()
                .isInstanceOf(PongResponse.class);
    }

    @Test
    void new_SerialPort_should_fails_if_no_serial_device() {
        // given
        Configuration configuration = new Configuration() {
            @Override
            public String getSerialPortPath() {
                return "ttyFake";
            }
        };

        // when/then
        assertThrows(FileNotFoundException.class, () -> new SerialPortWrapperImpl(configuration));
    }
}