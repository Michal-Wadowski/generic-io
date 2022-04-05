package com.example.genericio;

import com.example.genericio.command.CommandFactory;
import com.example.genericio.command.CommandIds;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommandFactoryTest {

    @Test
    void PingCommand_getBytes_should_returns_size_and_command_packet() {
        // given
        CommandFactory commandFactory = new CommandFactory();

        // when
        byte[] bytes = commandFactory.ping().getBytes();

        // then
        assertThat(bytes).isEqualTo(new byte[]{2, 0, (byte) CommandIds.PING_COMMAND.ordinal(), 0});
    }
}