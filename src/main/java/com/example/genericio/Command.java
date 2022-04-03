package com.example.genericio;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum Command {

    PING((short) 1),
    PONG((short) 2);

    private static final Map<Short, Command> commandsMap = Map.of(
            (short) 1, PING,
            (short) 2, PONG
    );

    private final Short commandId;

    public static Command fromValue(short command) {
        return commandsMap.get(command);
    }
}
