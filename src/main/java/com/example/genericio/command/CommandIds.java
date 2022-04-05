package com.example.genericio.command;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommandIds {
    PING_COMMAND,
    PONG_COMMAND,
    WRITE_PIN,
    WRITE_PIN_OK,
    READ_PIN,
    READ_PIN_RESULT,
    GPIO_INIT,
    GPIO_INIT_OK
}
