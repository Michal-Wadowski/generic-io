package com.example.genericio;

import com.example.genericio.command.CommandFactory;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommandFactoryTest {

    @Test
    void ping_should_send_size_and_command_packet() {
        // given
        SerialPortWrapper serialPortWrapper = new SerialPortWrapper() {
            byte[] written;

            @Override
            public void write(byte[] bytes) {
                written = bytes;
            }

            @Override
            public InputStream getInputStream() {
                return null;
            }
        };

        CommandFactory commandFactory = new CommandFactory(serialPortWrapper);

        // when
        commandFactory.ping().send();

        // then
        assertThat(serialPortWrapper)
                .extracting("written")
                .isEqualTo(new byte[]{2, 0, 1, 0});
    }
}