package com.example.genericio.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CommandIds {
    PING_COMMAND,
    PONG_COMMAND,
    WRITE_PIN,
    WRITE_PIN_OK,
    READ_PIN,
    READ_PIN_RESULT;
}
