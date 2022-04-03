package com.example.genericio;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandsTest {

    @Test
    void ping_should_send_size_and_command_packet() throws IOException {
        // given
        Configuration configuration = new Configuration() {
            @Override
            public String getSerialPortPath() {
                return "ttyFake";
            }
        };

        SerialPortWrapper serialPortWrapper = new SerialPortWrapper() {
            byte[] written;

            @Override
            public void write(byte[] bytes) {
                written = bytes;
            }

            @Override
            public int bytesAvailable() {
                return 0;
            }

            @Override
            public InputStream getInputStream() {
                return null;
            }
        };

        Commands commands = new Commands(serialPortWrapper);

        // when
        commands.ping();

        // then
        assertThat(serialPortWrapper)
                .extracting("written")
                .isEqualTo(new byte[]{2, 0, 1, 0});
    }

    @Test
    void should_receive_command_from_port_stream() throws IOException {
        // given
        Configuration configuration = new Configuration() {
            @Override
            public String getSerialPortPath() {
                return "ttyFake";
            }
        };

        SerialPortWrapper serialPortWrapper = new SerialPortWrapper() {
            byte[] written;

            @Override
            public void write(byte[] bytes) {
                written = bytes;
            }

            @Override
            public int bytesAvailable() {
                return 4;
            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(new byte[] {2, 0, 2, 0});
            }
        };

        Commands commands = new Commands(serialPortWrapper);

        // when
        Response response = commands.getResponse();

        // then
        assertThat(response)
                .isNotNull()
                .extracting("command")
                .isEqualTo((short)2);
    }
}