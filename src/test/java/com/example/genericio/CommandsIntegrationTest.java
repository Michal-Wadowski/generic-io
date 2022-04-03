package com.example.genericio;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandsIntegrationTest {

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

        Commands commands = new Commands(serialPortWrapper);

        // when
        commands.ping();
        Response response = commands.getResponse();

        // then
        assertThat(response)
                .isNotNull()
                .extracting("command")
                .isEqualTo((short)2);
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
        assertThrows(FileNotFoundException.class, () -> {
            new SerialPortWrapperImpl(configuration);
        });
    }
}