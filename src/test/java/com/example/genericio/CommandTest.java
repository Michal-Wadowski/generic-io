package com.example.genericio;

import org.junit.jupiter.api.Test;

import static com.example.genericio.Command.PING;
import static com.example.genericio.Command.PONG;
import static org.assertj.core.api.Assertions.assertThat;

class CommandTest {

    @Test
    void PING_fromValue() {
        // given/when
        Command command = Command.fromValue((short) 1);

        // then
        assertThat(command)
                .isNotNull()
                .isEqualTo(PING);
    }

    @Test
    void PONG_fromValue() {
        // given/when
        Command command = Command.fromValue((short) 2);

        // then
        assertThat(command)
                .isNotNull()
                .isEqualTo(PONG);
    }
}